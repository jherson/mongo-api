package com.nowellpoint.mongodb.persistence.test.model;

import java.io.Serializable;

import com.nowellpoint.mongodb.persistence.annotation.EmbeddedDocument;
import com.nowellpoint.mongodb.persistence.annotation.Property;

@EmbeddedDocument
public class Photos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5118469344092140757L;

	@Property
	private String picture;

	@Property
	private String thumbnail;


	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}