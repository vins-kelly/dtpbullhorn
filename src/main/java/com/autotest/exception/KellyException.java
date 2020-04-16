package com.autotest.exception;

public class KellyException extends Exception {
	
	public KellyException(String message) {
		super(message);
	}
	
	public KellyException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
