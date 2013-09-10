/**
 * 
 * Copyright 2013 John D. Herson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.rhcloud.mongodb.persistence.impl;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.rhcloud.mongodb.persistence.DocumentManager;
import com.rhcloud.mongodb.persistence.Query;
import com.rhcloud.mongodb.persistence.adapter.DateTypeAdapter;
import com.rhcloud.mongodb.persistence.adapter.ObjectIdTypeAdapter;

/**
 * @author jherson
 *
 */

public class DocumentManagerImpl implements DocumentManager, Serializable {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -7038813059024799681L;

	/**
	 * 
	 */
	
	private static final Gson GSON = new GsonBuilder().serializeNulls()
			.registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter())
			.registerTypeAdapter(Date.class, new DateTypeAdapter())
			.create();
	
	/**
	 * 
	 */
	
	private static final String ID = "_id"; 
	
	/**
	 * 
	 */
	
	private MongoClient mongo; 
	
	/**
	 * 
	 */
	
	private DB db;
	
	/**
	 * constructor
	 */
	
	protected DocumentManagerImpl() {
		 
	}	
	
	/**
	 * constructor
	 * @param mongo
	 */
	
	protected DocumentManagerImpl(MongoClient mongo, DB db) {
		this.mongo = mongo;
		this.db = db;
	}

	/**
	 * sets the MongoDB database for us in this DocumentManger
	 * @param db 
	 */

	public void setDB(DB db) {
		this.db = db;
	}
	
	/**
	 * getDB
	 * 
	 * @return DB
	 */
	
	protected DB getDB() {
		return db;
	}
	
	/**
	 * inserts an object into a collection
	 * 
	 * @param clazz
	 * @param object to be inserted
	 * @return T
	 */
	
	@Override
	public <T> T insert(Class<T> clazz, Object object) {
		String collectionName = AnnotationResolver.resolveCollection(object);		
		DBCollection collection = getDB().getCollection(collectionName);
		DBObject dbObject = getAsDBObject(object);	
		WriteResult result = collection.insert(dbObject);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
		return getAsObject(clazz, dbObject);
	}
	
	/**
	 * updates the object in the collection
	 * 
	 * @param clazz
	 * @param object to be updated
	 * @return the updated object
	 */
	
	@Override
	public <T> T update(Class<T> clazz, Object object) {		
		String collectionName = AnnotationResolver.resolveCollection(object);		
		DBCollection collection = getDB().getCollection(collectionName);
		DBObject dbObject = getAsDBObject(object);
		WriteResult result = collection.save(dbObject);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
		return getAsObject(clazz, dbObject);
	}
	
	/**
	 * find a document in a collection using the objectId
	 * 
	 * @param <T>
	 * @param clazz
	 * @param id the ObjectId of the object to query
	 * @return object found based on objectId
	 */
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public <T> T find(Class<T> clazz, ObjectId id) {
		return (T) createQuery(clazz)
				.field(ID)
				.isEqual(id)
				.getSingleResult();
	}
	
	/**
	 * delete a document from a collection
	 * 
	 * @param clazz
	 * @param object to delete
	 */
	
	@Override
	public <T> void delete(Class<T> clazz, Object object) {		
		String collectionName = AnnotationResolver.resolveCollection(object);
		DBCollection collection = getDB().getCollection(collectionName);
		DBObject dbObject = getAsDBObject(object);	
		WriteResult wr = collection.remove(new BasicDBObject(ID, new ObjectId(dbObject.get(ID).toString())));
		if (wr.getError() != null) {
			throw new MongoException(wr.getLastError());
		}
	}
	
	/**
	 * delete a document from a collection
	 * 
	 * @param object
	 */
	
	@Override
	public <T> void delete(Object object) {
		String collectionName = AnnotationResolver.resolveCollection(object);
		DBCollection collection = getDB().getCollection(collectionName);
		Object id = AnnotationResolver.resolveId(object);
		WriteResult wr = collection.remove(new BasicDBObject(ID, id));
		if (wr.getError() != null) {
			throw new MongoException(wr.getLastError());
		}
	}
	
	/**
	 * deleteAll - removes all documents in a collection
	 * 
	 * @param clazz
	 */
	
	@Override
	public <T> void deleteAll(Class<T> clazz) {
		String collectionName = AnnotationResolver.resolveCollection(clazz);
		DBCollection collection = getDB().getCollection(collectionName);
		collection.remove(new BasicDBObject());
	}
	
	/**
	 * createQuery
	 * 
	 * @param clazz
	 * @return Query
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> Query createQuery(Class<T> clazz) {		
		return new QueryImpl(this, clazz);
	}
	
	/**
	 * close
	 */
	
	@Override
	public void close() {
		mongo.close();
	}
	
	protected <T> DBObject getAsDBObject(Object object) {		
		return (DBObject) JSON.parse(GSON.toJson(object));
	}
	
	protected <T> T getAsObject(Class<T> clazz, Object object) {
		return GSON.fromJson(JSON.serialize(object), clazz);
	}
}
