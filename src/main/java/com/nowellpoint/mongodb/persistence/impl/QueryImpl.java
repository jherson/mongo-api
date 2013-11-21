package com.nowellpoint.mongodb.persistence.impl;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBCollection;
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
	
	private DocumentManagerImpl documentManager;
	
	/**
	 * 
	 */
	
	private Class<T> clazz;
	
	/**
	 * 
	 */
	
	private QueryBuilder queryBuilder;
	
	/**
	 * constructor
	 * 
	 * @param documentManager
	 * @param clazz
	 */
	
	protected QueryImpl(DocumentManagerImpl documentManager, Class<T> clazz) {
		this.documentManager = documentManager;	
		this.clazz = clazz;
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
		
		DBObject dbObject = getDBCollection(clazz).findOne(queryBuilder.get());
		
		/**
		 * if no records are found then throw exception
		 */
		
		if (dbObject == null) {
			throw new PersistenceException("Query returned no records");
		}
		
		/**
		 * convert the document to an object 
		 */
		
		return documentManager.convertDocumentToObject(clazz, dbObject);
	}
	
	/**
	 * @param clazz
	 * @return list of documents that matched the search criteria
	 */
	
	@Override
	public List<T> getResultList() {		
		
		/**
		 * execute the find query
		 */
		
		DBCursor dbCursor = getDBCollection(clazz).find(queryBuilder.get());

		/**
		 * if no records are found then throw exception
		 */
		
		if (dbCursor == null) {
			throw new NoResultException("Query returned no records");
		}
		
		/**
		 * loop through the result converting documents to objects
		 */
		
		List<T> queryResult = new ArrayList<T>();
		try {
			while (dbCursor.hasNext()) {
				queryResult.add(documentManager.convertDocumentToObject(clazz, dbCursor.next()));
			}
		} finally {
			dbCursor.close();
		}
		
		/**
		 * return the list of records
		 */
		
		return queryResult;
	}
	
	/**	 
	 * @param clazz
	 * @return
	 */
	
	private DBCollection getDBCollection(Class<T> clazz) {
		return documentManager.getDB().getCollection(AnnotationResolver.resolveCollection(clazz));
	}
}