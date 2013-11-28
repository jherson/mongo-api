package com.nowellpoint.mongodb.persistence.test.model;

import java.io.Serializable;

/**
 * @author jherson
 *
 */

public class AuthError implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 963007186519338340L;

	/**
	 * 
	 */
	
	private String error;
	
	/**
	 * 
	 */
	
	private String errorDescription;
	
	/**
	 * constructor
	 */
	
	public AuthError() {
		
	}
	
	/**
	 * getError
	 * 
	 * @return error
	 */
	
	public String getError() {
		return error;
	}

	/**
	 * setError
	 * 
	 * @param error
	 */
	
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * getErrorDescription
	 * 
	 * @return
	 */
	
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * setErrorDescription
	 * 
	 * @param errorDescription
	 */
	
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
}