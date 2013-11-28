package com.nowellpoint.mongodb.persistence.test.model;

import java.util.Date;

import com.nowellpoint.mongodb.persistence.BaseDocument;
import com.nowellpoint.mongodb.persistence.annotation.Collection;
import com.nowellpoint.mongodb.persistence.annotation.Document;
import com.nowellpoint.mongodb.persistence.annotation.EmbedOne;
import com.nowellpoint.mongodb.persistence.annotation.Property;

@Document
@Collection(name = "session")
public class Session extends BaseDocument {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -229867003839583005L;
	
	/**
	 * 
	 */
	
	@Property
	private Date sessionTime;
	
	/**
	 * 
	 */
	
	@Property
	private String sessionId;
	
	/**
	 * 
	 */
	
	@Property
	private String callbackURL;
	
	/**
	 * 
	 */
	
	@EmbedOne
	private Token token;
	
	/**
	 * 
	 */
	
	@EmbedOne
	private Identity identity;
	
	public Session() {
		setSessionTime(new Date());
	}
	
	public Date getSessionTime() {
		return sessionTime;
	}
	
	public void setSessionTime(Date sessionTime) {
		this.sessionTime = sessionTime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getCallbackURL() {
		return callbackURL;
	}

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
}