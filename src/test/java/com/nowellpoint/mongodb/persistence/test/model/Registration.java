package com.nowellpoint.mongodb.persistence.test.model;

import java.util.Date;
import java.util.List;

import com.nowellpoint.mongodb.persistence.BaseDocument;
import com.nowellpoint.mongodb.persistence.annotation.Document;
import com.nowellpoint.mongodb.persistence.annotation.Collection;
import com.nowellpoint.mongodb.persistence.annotation.Property;
import com.nowellpoint.mongodb.persistence.annotation.EmbedOne;
import com.nowellpoint.mongodb.persistence.annotation.EmbedMany;

@Document
@Collection(name = "registrations")
public class Registration extends BaseDocument {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -936984558523054151L;
	
	/**
	 * 
	 */
	
	@Property
	private String email;
	
	/**
	 * 
	 */
	
	@Property
	private String name;
	
	/**
	 * 
	 */
	
	@EmbedOne
	private Organization organization;
	
	/**
	 * 
	 */
	
	@Property
	private String userId;
	
	/**
	 * 
	 */
	
	@Property
	private String userName;
	
	/**
	 * 
	 */

	@Property
	private String callbackUrl;
	
	/**
	 * 
	 */
	
	@Property
	private Date creationDate;
	
	/**
	 * 
	 */
	
	@EmbedMany
	protected List<RegistrationEmail> registrationEmails;
	
	public Registration() {
	    creationDate = new Date();	
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}
	
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public List<RegistrationEmail> getRegistrationEmails() {
		return registrationEmails;
	}
	
	public void setRegistrationEmails(List<RegistrationEmail> registrationEmails) {
		this.registrationEmails = registrationEmails;
	}
}