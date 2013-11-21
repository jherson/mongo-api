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

package com.nowellpoint.mongodb.persistence.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.nowellpoint.mongodb.persistence.DocumentManager;
import com.nowellpoint.mongodb.persistence.Query;
import com.nowellpoint.mongodb.persistence.annotation.EmbedMany;
import com.nowellpoint.mongodb.persistence.annotation.EmbedOne;
import com.nowellpoint.mongodb.persistence.annotation.EmbeddedDocument;
import com.nowellpoint.mongodb.persistence.annotation.Id;
import com.nowellpoint.mongodb.persistence.annotation.Property;
import com.nowellpoint.mongodb.persistence.exception.PersistenceException;

/**
 * @author jherson
 *
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DocumentManagerImpl implements DocumentManager, Serializable {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -7038813059024799681L;
	
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
		DBObject dbObject = convertObjectToDocument(object);	
		WriteResult result = collection.insert(dbObject);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
		return convertDocumentToObject(clazz, dbObject);
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
		DBObject dbObject = convertObjectToDocument(object);
		WriteResult result = collection.save(dbObject);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
		return convertDocumentToObject(clazz, dbObject);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @param id the ObjectId of the object to query
	 * @return object found based on objectId
	 */
	
	@Override
	public <T> T find(Class<T> clazz, ObjectId objectId) {
		return find(clazz, objectId);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @param id the Object of the object to query
	 * @return object found based on objectId
	 */
	
	@Override
	public <T> T find(Class<T> clazz, Object id) {
		try {
			return (T) createQuery(clazz)
				.field(ID)
				.isEqual(id)
				.getSingleResult();
		} catch (PersistenceException e) {
			return null;
		}
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
		DBObject dbObject = convertObjectToDocument(object);	
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
	
	protected BasicDBObject convertObjectToDocument(Object object) {			
		BasicDBObject document = new BasicDBObject();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				document.put(ID, getId(object, field));
			} else if (field.isAnnotationPresent(Property.class)) {		
				document.put(getPropertyName(field), getProperty(object, field));
			} else if (field.isAnnotationPresent(EmbedOne.class)) {
				document.put(getPropertyName(field), getEmbedOne(object, field));
			} else if (field.isAnnotationPresent(EmbedMany.class)) {
				document.put(getPropertyName(field), getEmbedMany(object, field));
			}
		}
		
		return document;
	}
	
	protected <T> T convertDocumentToObject(Class<T> clazz, DBObject document) {
		Object object = null;
		try {
			object = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new PersistenceException(e);
		}

		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				setFieldValue(object, field, document.get(ID));
			} else if (field.isAnnotationPresent(Property.class)) {		
				//document.put(getFieldName(field), getProperty(object, field));
			} else if (field.isAnnotationPresent(EmbedOne.class)) {
				//document.put(getFieldName(field), getEmbedOne(object, field));
			} else if (field.isAnnotationPresent(EmbedMany.class)) {
				//document.put(getFieldName(field), getEmbedMany(object, field));
			}
		}
		
		return (T) object;
	}
	
	private Object getId(Object object, Field field) throws PersistenceException {
		Object id = getFieldValue(object, field);
		if (field.getType().isAssignableFrom(ObjectId.class)) {
	    	id = new ObjectId(id.toString()); 	
	    } 
		return id;
	}
	
	/**
	 * 
	 * @param object
	 * @param field
	 * @return
	 * @throws PersistenceException
	 */
	
	private Object getProperty(Object object, Field field) throws PersistenceException {
		return getFieldValue(object, field);
	}
	
	/**
	 * 
	 * @param object
	 * @param field
	 * @return
	 */
	
	private Object getFieldValue(Object object, Field field) {
		try {
		    Method method = object.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[] {});	
		    return method.invoke(object, new Object[] {});
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new PersistenceException(e);
		}	
	}
	
	private void setFieldValue(Object object, Field field, Object value) {
		try {
		    Method method = object.getClass().getMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[] {field.getType()});	
		    method.invoke(object, new Object[] {value});
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new PersistenceException(e);
		}
	}
	
	private BasicDBObject getEmbedOne(Object object, Field field) throws PersistenceException {
		validateEmbeddedDocument(field);
		Object embeddedDocument = getFieldValue(object, field);
		return convertObjectToDocument(embeddedDocument);
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	
	private String getPropertyName(Field field) {
		String fieldName = null;
		if (field.isAnnotationPresent(Property.class)) {
			fieldName = field.getAnnotation(Property.class).name().trim().length() > 0 ? field.getAnnotation(Property.class).name() : field.getName();
		} else if (field.isAnnotationPresent(EmbedOne.class)) {
			fieldName = field.getAnnotation(EmbedOne.class).name().trim().length() > 0 ? field.getAnnotation(EmbedOne.class).name() : field.getName();
		} else if (field.isAnnotationPresent(EmbedMany.class)) {
			fieldName = field.getAnnotation(EmbedMany.class).name().trim().length() > 0 ? field.getAnnotation(EmbedMany.class).name() : field.getName();
		}
		return fieldName;
	}
	
	/**
	 * 
	 * @param collection
	 * @return BasicDBList
	 */
	
	private BasicDBList getEmbedMany(Object object, Field field) {
		validateEmbeddedDocument(field);
		Object embeddedDocument = getFieldValue(object, field);		
		List<?> list = (List<?>) embeddedDocument;
		BasicDBList dbList = new BasicDBList();
		for (Object embed : list) {
			dbList.add(convertObjectToDocument(embed));
		}		
		return dbList;
	}
	
	private void validateEmbeddedDocument(Field field) {
		ParameterizedType type = (ParameterizedType) field.getGenericType();
		Class<?> embeddedClass = (Class<?>) type.getActualTypeArguments()[0];
		if (! embeddedClass.isAnnotationPresent(EmbeddedDocument.class)) {
			throw new PersistenceException("Missing EmbeddedDocument annotation from " + embeddedClass.getClass().getName());
		}
	}
}