package com.nowellpoint.mongodb.persistence.impl;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.nowellpoint.mongodb.persistence.Query;
import com.nowellpoint.mongodb.persistence.exception.NoResultException;
import com.nowellpoint.mongodb.persistence.exception.PersistenceException;

/**
 * @author jherson
 * @param <T>
 * @param <T>
 */

public class QueryImpl<T> implements Query<T> {
	
	/**
	 * 
	 */
	
	private DocumentManagerFactoryImpl documentManagerFactory;
	
	/**
	 * 
	 */
	
	private Class<T> clazz;
	
	/**
	 * 
	 */
	
	private QueryBuilder queryBuilder;
	
	/**
	 * 
	 */
	
	private String collectionName;
	
	/**
	 * constructor
	 * 
	 * @param documentManager
	 * @param clazz
	 */
	
	protected QueryImpl(DocumentManagerFactoryImpl documentManagerFactory, Class<T> clazz, String collectionName) {
		this.documentManagerFactory = documentManagerFactory;	
		this.clazz = clazz;
		this.collectionName = collectionName;
		this.queryBuilder = QueryBuilder.start();
	}
	
	/**
	 * field
	 * 
	 * @param key 
	 * @return Query object
	 */
	
	@Override
	public Query<T> field(String key) {
		queryBuilder.put(key);
		return this;
	}
	
	/**
	 * @param value
	 * @return query object 
	 */
	
	@Override
	public Query<T> isEqual(Object value) {
		queryBuilder.is(value);
		return this;
	}
	
	/**
	 * @param clazz
	 * @return single document that matched the search criteria
	 */
		
	@Override
	public T getSingleResult() {
		
		/**
		 * execute the findOne query
		 */
		
		DBObject document = documentManagerFactory.getDB().getCollection(collectionName).findOne(queryBuilder.get());
		
		/**
		 * if no records are found then throw exception
		 */
		
		if (document == null) {
			throw new NoResultException("Query returned no records");
		}
		
		/**
		 * instansiate the object
		 */
		
		Object object = null;
		try {
			object = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new PersistenceException(e);
		}
		
		/**
		 * convert the document to an object 
		 */
		
		return documentManagerFactory.convertDocumentToObject(object, document);
	}
	
	/**
	 * @param clazz
	 * @return list of documents that matched the search criteria
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getResultList() {		
		
		/**
		 * execute the find query
		 */
		
		DBCursor documents = documentManagerFactory.getDB().getCollection(collectionName).find(queryBuilder.get());

		/**
		 * if no records are found then throw exception
		 */
		
		if (documents == null) {
			throw new NoResultException("Query returned no records");
		}
		
		/**
		 * loop through the result converting documents to objects
		 */
		
		List<T> queryResult = new ArrayList<T>();
		try {
			while (documents.hasNext()) {
				Object object = clazz.newInstance();
				queryResult.add((T) documentManagerFactory.convertDocumentToObject(object, documents.next()));
			}
		} catch (InstantiationException | IllegalAccessException e) {
        	throw new PersistenceException(e);	
		} finally {
			documents.close();
		}
		
		/**
		 * return the list of records
		 */
		
		return queryResult;
	}
}