package com.rhcloud.mongo.exception;

public class MongoDBConfigurationException extends Exception {

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
}