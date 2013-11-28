package com.nowellpoint.mongodb.persistence.test.model;

import java.io.Serializable;

import com.nowellpoint.mongodb.persistence.annotation.EmbeddedDocument;
import com.nowellpoint.mongodb.persistence.annotation.Property;

@EmbeddedDocument
public class Token implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4728657003050112974L;

	/**
	 * 
	 */
	
	@Property
	private String id;

	@Property
	private String issuedAt;

	@Property
	private String refreshToken;

	@Property
	private String instanceUrl;

	@Property
	private String signature;

	@Property
	private String accessToken;

	@Property
	private String error;

	@Property
	private String errorDescription;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(String issuedAt) {
		this.issuedAt = issuedAt;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getInstanceUrl() {
		return instanceUrl;
	}

	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
}