package com.nowellpoint.mongodb.persistence.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.QueryBuilder;
import com.nowellpoint.mongodb.persistence.Query;

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
		return documentManager.getAsObject(clazz, getDBCollection(clazz).findOne(queryBuilder.get()));
	}
	
	/**
	 * @param clazz
	 * @return list of documents that matched the search criteria
	 */
	
	@Override
	public List<T> getResultList() {			
		DBCursor cursor = getDBCollection(clazz).find(queryBuilder.get());
		
		List<T> queryResult = Lists.newArrayList();
		try {
			while (cursor.hasNext()) {
				queryResult.add(documentManager.getAsObject(clazz, cursor.next()));
			}
		} finally {
			cursor.close();
		}
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