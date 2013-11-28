package com.nowellpoint.mongodb.persistence.test.model;

import java.io.Serializable;
import java.util.Locale;

import com.nowellpoint.mongodb.persistence.annotation.EmbedOne;
import com.nowellpoint.mongodb.persistence.annotation.EmbeddedDocument;
import com.nowellpoint.mongodb.persistence.annotation.Property;

@EmbeddedDocument
public class Identity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1378593246435277996L;

	@Property
	private String id;

	@Property
	private Boolean assertedUser;

	@Property
	private String userId;

	@Property
	private String organizationId;

	@Property
	private String username;

	@Property
	private String nickName;

	@Property
	private String displayName;

	@Property
	private String email;

	@Property
	private Boolean active;

	@Property
	private String userType;

	@Property
	private String language;

	@Property
	private Locale locale;

	@Property
	private String utcOffset;

	@EmbedOne
	private Photos photos;

	@EmbedOne
	private Urls urls;
	
	private String organizationName;

	public Identity() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getAssertedUser() {
		return assertedUser;
	}

	public void setAssertedUser(Boolean assertedUser) {
		this.assertedUser = assertedUser;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Urls getUrls() {
		return urls;
	}

	public void setUrls(Urls urls) {
		this.urls = urls;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getUtcOffset() {
		return utcOffset;
	}

	public void setUtcOffset(String utcOffset) {
		this.utcOffset = utcOffset;
	}

	public Photos getPhotos() {
		return photos;
	}

	public void setPhotos(Photos photos) {
		this.photos = photos;
	}
	
	public String getOrganizationName() {
		return organizationName;
	}
	
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
}