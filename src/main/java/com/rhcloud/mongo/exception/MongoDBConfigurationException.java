package com.rhcloud.mongo.exception;

public class MongoDBConfigurationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5406288225201599388L;

	/**
	 * 
	 */
	
	public MongoDBConfigurationException() {
		super("MongoDB Configuration is invalid or missing.");
	}
	
	public MongoDBConfigurationException(String message) {
		super(message);
	}
	
	public MongoDBConfigurationException(Exception exception) {
		super(exception);
	}
}