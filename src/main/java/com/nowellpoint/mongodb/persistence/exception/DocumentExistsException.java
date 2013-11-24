package com.nowellpoint.mongodb.persistence.exception;

public class DocumentExistsException extends PersistenceException {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -2846081794090980568L;
	
	public DocumentExistsException(String message) {
		super(message);
	}
	
	public DocumentExistsException(Exception exception) {
		super(exception);
	}
}