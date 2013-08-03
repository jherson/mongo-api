package com.rhcloud.mongo.test.model;

import org.bson.types.ObjectId;

public class MongoTestObject {
	
	private ObjectId _id;
	private String name;
	
	public MongoTestObject() {
		
	}
	
	public ObjectId getId() {
		return _id;
	}
	
	public void setId(ObjectId _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
