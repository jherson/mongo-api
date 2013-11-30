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

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.nowellpoint.mongodb.persistence.DocumentManager;
import com.nowellpoint.mongodb.persistence.DocumentManagerFactory;
import com.nowellpoint.mongodb.persistence.MongoExceptionCode;
import com.nowellpoint.mongodb.persistence.Query;
import com.nowellpoint.mongodb.persistence.annotation.Collection;
import com.nowellpoint.mongodb.persistence.exception.DocumentExistsException;
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
	
	private DocumentManagerFactoryImpl doumentManagerFactory;
	
	/**
	 * constructor
	 * @param DocumentManagerFactory
	 */
	
	protected DocumentManagerImpl(DocumentManagerFactoryImpl documentManagerFactory) {
		this.doumentManagerFactory = documentManagerFactory;
	}
	
	/**
	 * 
	 * @return
	 */
	
	@Override
	public DocumentManagerFactory getDocumentManagerFactory() {
		return doumentManagerFactory;
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
		doumentManagerFactory.prePersist(object);
		String collectionName = doumentManagerFactory.resolveDocumentName(object.getClass());	
		DBCollection collection = getDB().getCollection(collectionName);
		DBObject dbObject = doumentManagerFactory.convertObjectToDocument(object);
		try {
			collection.insert(dbObject);
		} catch (MongoException e) {
			if (e.getCode() == MongoExceptionCode.DOCUMENT_EXISTS) {
				throw new DocumentExistsException(e.getMessage());
			}
		}		
		doumentManagerFactory.resolveId(object, dbObject.get(DocumentManagerFactoryImpl.ID));
		doumentManagerFactory.postPersist(object);
		return (T) object;
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
		doumentManagerFactory.prePersist(object);
		String collectionName = doumentManagerFactory.resolveDocumentName(object.getClass());	
		DBCollection collection = getDB().getCollection(collectionName);
		DBObject dbObject = doumentManagerFactory.convertObjectToDocument(object);
		WriteResult result = collection.save(dbObject);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
		doumentManagerFactory.postPersist(object);
		return doumentManagerFactory.convertDocumentToObject(clazz, dbObject);
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
				.field(DocumentManagerFactoryImpl.ID)
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
		String collectionName = doumentManagerFactory.resolveDocumentName(object.getClass());
		DBCollection collection = getDB().getCollection(collectionName);
		DBObject dbObject = doumentManagerFactory.convertObjectToDocument(object);	
		WriteResult wr = collection.remove(new BasicDBObject(DocumentManagerFactoryImpl.ID, new ObjectId(dbObject.get(DocumentManagerFactoryImpl.ID).toString())));
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
		String collectionName = doumentManagerFactory.resolveDocumentName(object.getClass());
		DBCollection collection = getDB().getCollection(collectionName);
		Object id = doumentManagerFactory.resolveId(object);
		WriteResult wr = collection.remove(new BasicDBObject(DocumentManagerFactoryImpl.ID, id));
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
	
	@Override
	public <T> Query createQuery(Class<T> clazz) {	
		String collectionName = null;
		Collection collection = (Collection) clazz.getAnnotation(Collection.class);
		if (collection.name().trim().length() > 0) {
			collectionName = collection.name();
		} else {
			collectionName = clazz.getSimpleName();
		}
		
		return new QueryImpl(doumentManagerFactory, clazz, collectionName);
	}
	
	/**
	 * 
	 */
	
	@Override
	public void refresh(Object object) {
		object = find(object.getClass(), doumentManagerFactory.resolveId(object));		
	}
	
	/**
	 * getDB
	 * 
	 * @return DB
	 */
	
	private DB getDB() {
		return doumentManagerFactory.getDB();
	}
}