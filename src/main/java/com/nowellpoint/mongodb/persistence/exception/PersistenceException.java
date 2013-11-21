package com.nowellpoint.mongodb.persistence.exception;

public class PersistenceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5406288225201599388L;

	/**
	 * 
	 */
	
	public PersistenceException() {
		super("MongoDB Configuration is invalid or missing.");
	}
	
	public PersistenceException(String message) {
		super(message);
	}
	
	public PersistenceException(Exception exception) {
		super(exception);
	}
}