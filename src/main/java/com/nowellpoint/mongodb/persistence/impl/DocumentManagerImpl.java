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
	
	
	protected DocumentManagerImpl(DocumentManagerFactoryImpl documentManagerFactory) {
		this.doumentManagerFactory = documentManagerFactory;
	}

	@Override
	public DocumentManagerFactory getDocumentManagerFactory() {
		return doumentManagerFactory;
	}
	
	@Override
	public void persist(Object document) {
		doumentManagerFactory.prePersist(document);
		String collectionName = doumentManagerFactory.resolveDocumentName(document.getClass());	
		DBCollection collection = getDB().getCollection(collectionName);
		DBObject dbObject = doumentManagerFactory.convertObjectToDocument(document);
		try {
			collection.insert(dbObject);
		} catch (MongoException e) {
			if (e.getCode() == MongoExceptionCode.DOCUMENT_EXISTS) {
				throw new DocumentExistsException(e.getMessage());
			}
		}		
		doumentManagerFactory.resolveId(document, dbObject.get(DocumentManagerFactoryImpl.ID));
		doumentManagerFactory.postPersist(document);
	}
	
	@Override
	public <T> T merge(T document) {		
		doumentManagerFactory.prePersist(document);
		String collectionName = doumentManagerFactory.resolveDocumentName(document.getClass());	
		DBCollection collection = getDB().getCollection(collectionName);
		Object documentId = doumentManagerFactory.resolveId(document);
		DBObject dbObject = doumentManagerFactory.convertObjectToDocument(document);				
		DBObject query = new BasicDBObject(DocumentManagerFactoryImpl.ID, documentId);
		WriteResult result = collection.update(query, dbObject);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
		doumentManagerFactory.postPersist(document);
		return document;
	}
	
	@Override
	public <T> T find(Class<T> documentClass, Object documentId) {
		try {
			return (T) createQuery(documentClass)
				.field(DocumentManagerFactoryImpl.ID)
				.isEqual(documentId)
				.getSingleResult();
		} catch (PersistenceException e) {
			return null;
		}
	}
	
	@Override
	public void remove(Object document) {
		String collectionName = doumentManagerFactory.resolveDocumentName(document.getClass());
		DBCollection collection = getDB().getCollection(collectionName);
		Object documentId = doumentManagerFactory.resolveId(document);
		WriteResult wr = collection.remove(new BasicDBObject(DocumentManagerFactoryImpl.ID, documentId));
		if (wr.getError() != null) {
			throw new MongoException(wr.getLastError());
		}
	}
	
	@Override
	public <T> Query createQuery(Class<T> clazz) {
		return new QueryImpl(doumentManagerFactory, clazz, doumentManagerFactory.resolveDocumentName(clazz));
	}
	
	@Override
	public void refresh(Object document) {
		document = find(document.getClass(), doumentManagerFactory.resolveId(document));		
	}
	
	/**
	 * getDB()
	 * @return DB
	 */
	
	private DB getDB() {
		return doumentManagerFactory.getDB();
	}
}