package solutions.techsur.common.microservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;
import solutions.techsur.common.microservice.exceptions.AppException;
import solutions.techsur.common.microservice.exceptions.InvalidRequestException;
import solutions.techsur.common.microservice.exceptions.ResourceNotFoundException;
import solutions.techsur.common.microservice.exceptions.StaleDataException;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CommonGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String MESSAGE = "message";
	public static final String STATUS = "status";
	public static final String ERROR_CODE = "errorCode";

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
		return makeResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return makeResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(StaleDataException.class)
	public ResponseEntity<?> handleStaleDataException(StaleDataException ex) {
		return makeResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<?> handleInvalidRequestException(InvalidRequestException ex) {
		return makeResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Builds a simple error response map with message and status.
	 */
	private ResponseEntity<?> makeResponse(String message, HttpStatus status) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put(MESSAGE, message);
		return new ResponseEntity<>(errorResponse, status);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request) {

		Map<String, String> fieldErrors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldErrors);
	}

	@ExceptionHandler(AppException.class)
	public ResponseEntity<Map<String, Object>> handleAppException(AppException ex) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put(ERROR_CODE, ex.getReason().getErrorCode());
		errorResponse.put(MESSAGE, ex.getMessage());
		errorResponse.put(STATUS, ex.getReason().getStatus().value());

		return new ResponseEntity<>(errorResponse, ex.getReason().getStatus());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(ERROR_CODE, 403);
		response.put(MESSAGE, "Access Denied");
		response.put(STATUS, HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, HttpServletRequest request) {
		logExceptionWithDetails("An unexpected error occurred", ex, request);
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put(ERROR_CODE, 500);
		errorResponse.put(MESSAGE, "An unexpected error occurred");
		errorResponse.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Logs exception details including request URL, headers, parameters and body if available.
	 */
	private void logExceptionWithDetails(String message, Exception ex, HttpServletRequest request) {
		String requestUrl = request.getRequestURI();

		StringBuilder headersBuilder = new StringBuilder();
		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				String value = request.getHeader(name);
				headersBuilder.append(name).append(": ").append(value);
				if (headerNames.hasMoreElements()) {
					headersBuilder.append(", ");
				}
			}
		}

		StringBuilder paramsBuilder = new StringBuilder();
		Enumeration<String> parameterNames = request.getParameterNames();
		if (parameterNames != null) {
			while (parameterNames.hasMoreElements()) {
				String name = parameterNames.nextElement();
				String value = request.getParameter(name);
				paramsBuilder.append(name).append(": ").append(value);
				if (parameterNames.hasMoreElements()) {
					paramsBuilder.append(", ");
				}
			}
		}

		String requestBody = "";
		if (request instanceof ContentCachingRequestWrapper) {
			ContentCachingRequestWrapper wrappedRequest = (ContentCachingRequestWrapper) request;
			byte[] contentAsByteArray = wrappedRequest.getContentAsByteArray();
			if (contentAsByteArray != null && contentAsByteArray.length > 0) {
				requestBody = new String(contentAsByteArray, StandardCharsets.UTF_8);
			}
		}

		log.error("{} | URL: {} | Headers: {} | Request Params: {} | Request Body: {}",
				message, requestUrl, headersBuilder.toString(), paramsBuilder.toString(), requestBody, ex);
	}
}