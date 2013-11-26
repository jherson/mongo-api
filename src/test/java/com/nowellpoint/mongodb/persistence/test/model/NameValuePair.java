package com.nowellpoint.mongodb.persistence.test.model;

import com.nowellpoint.mongodb.persistence.BaseDocument;
import com.nowellpoint.mongodb.persistence.annotation.Collection;
import com.nowellpoint.mongodb.persistence.annotation.Document;
import com.nowellpoint.mongodb.persistence.annotation.Property;

@Document
@Collection(name = "properties")
public class NameValuePair extends BaseDocument {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -7838064767547511748L;
	
	/**
	 * 
	 */
	
	@Property(name = "name")
	private String name;
	
	/**
	 * 
	 */
	
	@Property(name = "value")
	private String value;

	
	public NameValuePair() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}