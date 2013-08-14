package com.rhcloud.mongo.test.model;

import java.util.Date;

import com.rhcloud.mongo.model.BaseDocument;
import com.rhcloud.mongo.annotation.Document;

@Document(collection = "TestObjects")
public class MongoTestObject extends BaseDocument {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -1913878606286172065L;
	
	private String name;
	private Date creationDate;
	
	public MongoTestObject() {
		super();
		creationDate = new Date();
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}