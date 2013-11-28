package com.nowellpoint.mongodb.persistence.test.model;

import java.io.Serializable;

import com.nowellpoint.mongodb.persistence.annotation.EmbeddedDocument;

@EmbeddedDocument
public class RegistrationEmail implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 4427964101031888911L;
	
	/**
	 * 
	 */
	
	protected String emailText;
	

	public RegistrationEmail() {
		
	}
	
	public void setEmailText(String emailText) {
		this.emailText = emailText;
	}
	
	public String getEmailText() {
		return emailText;
	}
}