package solutions.techsur.common.microservice.exceptions;

public class StaleDataException extends Exception {
	public StaleDataException(String message) {
		super(message);
	}
}
