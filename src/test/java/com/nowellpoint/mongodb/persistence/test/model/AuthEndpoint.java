package com.nowellpoint.mongodb.persistence.test.model;

import com.nowellpoint.mongodb.persistence.BaseDocument;
import com.nowellpoint.mongodb.persistence.annotation.Collection;
import com.nowellpoint.mongodb.persistence.annotation.Document;
import com.nowellpoint.mongodb.persistence.annotation.Property;

@Document
@Collection(name = "auth.endpoints")
public class AuthEndpoint extends BaseDocument {
	
	/**
	 * 
	 */

	private static final long serialVersionUID = -5260176680948633874L;
	
	/**
	 * 
	 */
	
	@Property(name = "name")
	private String name;
	
	/**
	 * 
	 */
	
	@Property(name = "hostname")
	private String hostname;
	
	/**
	 * 
	 */
	
	@Property(name = "isSandbox")
	private Boolean isSandbox;
	
	/**
	 * 
	 */
	
	@Property(name = "clientId")
	private String clientId;
	
	/**
	 * 
	 */
	
	@Property(name = "clientSecret")
	private String clientSecret;
	
	
	public AuthEndpoint() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Boolean getIsSandbox() {
		return isSandbox;
	}

	public void setIsSandbox(Boolean isSandbox) {
		this.isSandbox = isSandbox;
	}
	
	public String getClientId() {
		return clientId;
	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public String getClientSecret() {
		return clientSecret;
	}
	
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
}