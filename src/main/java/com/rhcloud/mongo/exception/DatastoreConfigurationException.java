package com.rhcloud.mongo.exception;

public class DatastoreConfigurationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5406288225201599388L;

	/**
	 * 
	 */
	
	public DatastoreConfigurationException() {
		super("MongoDB Configuration is invalid or missing.");
	}
	
	public DatastoreConfigurationException(String message) {
		super(message);
	}
	
	public DatastoreConfigurationException(Exception exception) {
		super(exception);
	}
}