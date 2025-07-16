package solutions.techsur.common.microservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CommonGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String MESSAGE = "message";
	public static final String STATUS = "status";
	public static final String ERROR_CODE = "errorCode";

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
		return makeResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return makeResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(StaleDataException.class)
	public ResponseEntity<Map<String, String>> handleStaleDataException(StaleDataException ex) {
		return makeResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<Map<String, String>> handleInvalidRequestException(InvalidRequestException ex) {
		return makeResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<Map<String, String>> makeResponse(String message, HttpStatus status) {
		Map<String, String> errorDetails = new HashMap<>();
		errorDetails.put(MESSAGE, message);
		return new ResponseEntity<>(errorDetails, status);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		Map<String, String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
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

	private void logExceptionWithDetails(String message, Exception ex, HttpServletRequest request) {
		String requestUrl = request.getRequestURI();

		// Log headers
		StringJoiner headersJoiner = new StringJoiner(", ");
		var headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				String value = request.getHeader(name);
				headersJoiner.add(name + ": " + value);
			}
		}

		// Log parameters
		StringJoiner paramsJoiner = new StringJoiner(", ");
		var parameterNames = request.getParameterNames();
		if (parameterNames != null) {
			while (parameterNames.hasMoreElements()) {
				String name = parameterNames.nextElement();
				String value = request.getParameter(name);
				paramsJoiner.add(name + ": " + value);
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

		log.error("{} | URL: {} | Headers: [{}] | Request Params: [{}] | Request Body: {}",
				message, requestUrl, headersJoiner.toString(), paramsJoiner.toString(), requestBody, ex);
	}
}