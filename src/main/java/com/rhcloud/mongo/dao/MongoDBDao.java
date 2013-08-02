package com.rhcloud.mongo.dao;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public interface MongoDBDao {
	public void setDB(DB db);
	public DBObject insert(String collectionName, DBObject dbObject);
	public DBObject findOne(String collectionName, DBObject query);
	public void remove(String collectionName, DBObject dbObject);
	public DBObject update(String collectionName, DBObject dbObject);
	public DBCursor find(String collectionName);
	public DBCursor find(String collectionName, DBObject query);
	
	public <T> T insert(String collectionName, Class<T> clazz);
	public <T> void remove(String collectionName, Class<T> clazz);
}