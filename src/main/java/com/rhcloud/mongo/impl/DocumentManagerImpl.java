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

package com.rhcloud.mongo.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.bson.types.ObjectId;

import com.google.common.collect.Maps;
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
import com.rhcloud.mongo.AnnotationScanner;
import com.rhcloud.mongo.DocumentManager;
import com.rhcloud.mongo.Query;
import com.rhcloud.mongo.adapter.DateTypeAdapter;
import com.rhcloud.mongo.adapter.ObjectIdTypeAdapter;

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
	
	private static final Gson gson = new GsonBuilder().serializeNulls()
			.registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter())
			.registerTypeAdapter(Date.class, new DateTypeAdapter())
			.create();
	
	/**
	 * 
	 */
	
	private static Map<Class<?>, String> mapper;
	
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
		
		DocumentManagerImpl.mapper = Maps.newConcurrentMap();
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
		String collectionName = null;
		if (mapper.containsKey(clazz)) {
			System.out.println("found");
			collectionName = mapper.get(clazz);
		} else {
			System.out.println("not found");
			collectionName = AnnotationScanner.getCollectionName(object);	
		}
		mapper.put(clazz, collectionName);
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
		String collectionName = null;
		if (mapper.containsKey(clazz)) {
			System.out.println("found");
			collectionName = mapper.get(clazz);
		} else {
			System.out.println("not found");
			collectionName = AnnotationScanner.getCollectionName(object);	
		}
		mapper.put(clazz, collectionName);
		//String collectionName = AnnotationScanner.getCollectionName(object);
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
				.field("_id")
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
		String collectionName = AnnotationScanner.getCollectionName(object);
		DBCollection collection = getDB().getCollection(collectionName);
		DBObject dbObject = getAsDBObject(object);	
		WriteResult wr = collection.remove(new BasicDBObject("_id", new ObjectId(dbObject.get("_id").toString())));
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
		String collectionName = AnnotationScanner.getCollectionName(object);
		DBCollection collection = getDB().getCollection(collectionName);
		Object id = AnnotationScanner.getId(object);
		WriteResult wr = collection.remove(new BasicDBObject("_id", id));
		if (wr.getError() != null) {
			throw new MongoException(wr.getLastError());
		}
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
		return (DBObject) JSON.parse(gson.toJson(object));
	}
	
	protected <T> T getAsObject(Class<T> clazz, Object object) {
		return gson.fromJson(JSON.serialize(object), clazz);
	}
}