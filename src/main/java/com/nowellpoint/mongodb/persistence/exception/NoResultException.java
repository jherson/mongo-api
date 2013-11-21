package com.nowellpoint.mongodb.persistence.exception;

public class NoResultException extends PersistenceException {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -2846081794090980568L;
	
	public NoResultException(String message) {
		super(message);
	}
	
	public NoResultException(Exception exception) {
		super(exception);
	}
}