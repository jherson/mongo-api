package com.nowellpoint.mongodb.persistence.test.model;

import java.io.Serializable;

import com.nowellpoint.mongodb.persistence.annotation.EmbeddedDocument;
import com.nowellpoint.mongodb.persistence.annotation.Property;

@EmbeddedDocument
public class Urls implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2429897591039053055L;

	@Property
	private String enterprise;

	@Property
	private String metadata;

	@Property
	private String partner;

	@Property
	private String rest;

	@Property
	private String sobjects;

	@Property
	private String search;

	@Property
	private String query;

	@Property
	private String recent;

	@Property
	private String profile;

	@Property
	private String feeds;

	@Property
	private String feedItems;

	@Property
	private String groups; 

	@Property
	private String users;

	public String getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}

	public String getSobjects() {
		return sobjects;
	}

	public void setSobjects(String sobjects) {
		this.sobjects = sobjects;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getRecent() {
		return recent;
	}

	public void setRecent(String recent) {
		this.recent = recent;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getFeeds() {
		return feeds;
	}

	public void setFeeds(String feeds) {
		this.feeds = feeds;
	}

	public String getFeedItems() {
		return feedItems;
	}

	public void setFeedItems(String feedItems) {
		this.feedItems = feedItems;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}
}