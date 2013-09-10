package com.rhcloud.mongodb.persistence.test.model;

import java.util.Date;

import com.rhcloud.mongodb.persistence.BaseDocument;
import com.rhcloud.mongodb.persistence.annotation.Document;
import com.rhcloud.mongodb.persistence.annotation.Index;

@Document(collection = "TestObjects")
@Index(name="testindex", key = "name")
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