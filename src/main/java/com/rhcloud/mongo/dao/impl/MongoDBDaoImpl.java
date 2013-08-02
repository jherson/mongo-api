package com.rhcloud.mongo.dao.impl;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.rhcloud.mongo.adapter.ObjectIdTypeAdapter;
import com.rhcloud.mongo.dao.MongoDBDao;

public class MongoDBDaoImpl implements MongoDBDao, Serializable {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -7038813059024799681L;

	/**
	 * 
	 */
	
	private static final Gson gson = new GsonBuilder().			
			serializeNulls().	
			registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).
			registerTypeAdapter(Date.class, new DateTypeAdapter()).
			create();

	/**
	 * 
	 */
	
	protected DB db;	

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
		WriteResult result = collection.insert(dbObject);
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
		WriteResult result = collection.remove(dbObject);
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
	public DBObject update(String collectionName, DBObject dbObject) {
		DBCollection collection = db.getCollection(collectionName);
		WriteResult result = collection.save(dbObject);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
		return dbObject;
	}	
	
	@Override
	public <T> T insert(String collectionName, Class<T> clazz) {
		DBCollection collection = db.getCollection(collectionName);
		DBObject dbObject = getAsDBObject(clazz);	
		WriteResult result = collection.insert(dbObject);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
		return getAsJson(dbObject, clazz);
	}
	
	@Override
	public <T> void remove(String collectionName, Class<T> clazz) {
		DBCollection collection = db.getCollection(collectionName);
		DBObject dbObject = getAsDBObject(clazz);	
		WriteResult wr = collection.remove(new BasicDBObject("_id", new ObjectId(dbObject.get("_id").toString())));
		if (wr.getError() != null) {
			throw new MongoException(wr.getLastError());
		}
	}
	
	private <T> DBObject getAsDBObject(Class<T> clazz) {
		return (DBObject) JSON.parse(gson.toJson(clazz));
	}
	
	private <T> T getAsJson(DBObject dbObject, Class<T> clazz) {
		return gson.fromJson(JSON.serialize(dbObject), clazz);
	}
}