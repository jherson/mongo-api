package com.rhcloud.mongo.dao.impl;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.rhcloud.mongo.dao.MongoDBDao;

public class MongoDBDaoImpl implements MongoDBDao {

	private DB db;

	/**
	 * sets the MongoDB for use in this DAO
	 * 
	 * @param db 
	 */

	public void setDB(DB db) {
		this.db = db;
	}
	
	/**
	 * Executes a MongoDB insert of the specified DBObject into the specified collection
	 * 
	 * @param collectionName      
	 * @param dbObject
	 */
	
	@Override
	public DBObject insert(String collectionName, DBObject dbObject) {        	
		DBCollection collection = db.getCollection(collectionName);
		WriteResult result = collection.insert(dbObject, WriteConcern.FSYNC_SAFE);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
		return dbObject;
	}
	
	@Override
	public DBObject findOne(String collectionName, DBObject query) {
		DBCollection collection = db.getCollection(collectionName);	
		return collection.findOne(query);
	}
	
	@Override
	public DBCursor find(String collectionName) {
		DBCollection collection = db.getCollection(collectionName);		
		DBCursor cursor = collection.find();
		return cursor;
	}
	
	@Override
	public DBCursor find(String collectionName, DBObject query) {
		DBCollection collection = db.getCollection(collectionName);	
		DBCursor cursor = collection.find(query);
		return cursor;
	}
	
	/**
	 * Executes a MongoDB remove of the specified DBObject from the specified collection
	 * 
	 * @param collectionName
	 * @param dbObject 
	 */
	
	@Override
	public void remove(String collectionName, DBObject dbObject) {
		DBCollection collection = db.getCollection(collectionName);
		WriteResult result = collection.remove(dbObject, WriteConcern.FSYNC_SAFE);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
	}
	
	/**
	 * Executes a MongoDB update of of the specified DBObject into the specified collection
	 * 
	 * @param collectionName
	 * @param criteria
	 * @param dbObject
	 */
	
	@Override
	public DBObject update(String collectionName, DBObject criteria, DBObject dbObject) {
		DBCollection collection = db.getCollection(collectionName);
		WriteResult result = collection.update(criteria, dbObject, false, false, WriteConcern.FSYNC_SAFE);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
		return dbObject;
	}	
}