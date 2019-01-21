package com.monitor.Exception;

/**
 * General Monitor Service Exception indicating problem in 
 * MonitorService. The message and underlying cause should provide the details 
 * of the error.
 * @author akshayhiremath
 *
 */
public class MonitorServiceException extends Exception {
	private static final long serialVersionUID = -3291452259365752465L;
	private String message = "Monitor Service Exception";
	
	public MonitorServiceException() {
	}
	
	public MonitorServiceException(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
