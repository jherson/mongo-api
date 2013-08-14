package com.rhcloud.mongo.test.model;

import java.util.Date;

import org.bson.types.ObjectId;

import com.google.gson.annotations.SerializedName;
import com.rhcloud.mongo.model.MongoDBBaseEntity;
import com.rhcloud.mongo.annotation.Document;
import com.rhcloud.mongo.annotation.Id;

@Document(collection = "TestObjects")
public class MongoTestObject extends MongoDBBaseEntity {
		
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