package com.monitor.Exception;

public class InputValidationException extends Exception {
	/**
	 * Exception indicating the errors in the input data to Service Monitor
	 */
	private static final long serialVersionUID = -6781452259381952465L;
	private String message = "Input Validation Exception";

	public InputValidationException() {
		
	}
	
	public InputValidationException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
