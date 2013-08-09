package com.rhcloud.mongo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;

/**
 * @author jherson
 *
 */
public class QueryImpl implements Query {
	
	/**
	 * 
	 */
	
	protected DB db;	
	
	/**
	 * 
	 */
	
	protected Gson gson;
	
	/**
	 * 
	 */
	
	protected QueryBuilder queryBuilder;
	
	/**
	 * 
	 */
	
	protected String collectionName;
	
	/**
	 * constructor
	 * 
	 * @param db
	 * @param gson
	 */
	
	public QueryImpl(DB db, Gson gson) {
		this.db = db;
		this.gson = gson;
		this.queryBuilder = QueryBuilder.start();
	}
	
	/**
	 * setCollectionName
	 * 
	 * @param collectionName that the document will be added to
	 * @return query object
	 */
	
	@Override
	public Query setCollectionName(String collectionName) {
		this.collectionName = collectionName;
		return this;
	}
	
	/**
	 * getCollectionName
	 * 
	 * @return collectionName that this document is part of
	 */
	
	@Override
	public String getCollectionName() {
		return collectionName;
	}
	
	/**
	 * put
	 * 
	 * @param key 
	 * @return query object
	 */
	
	@Override
	public Query put(String key) {
		queryBuilder.put(key);
		return this;
	}
	
	/**
	 * is
	 * 
	 * @param value
	 * @return query object 
	 */
	
	@Override
	public Query is(Object value) {
		queryBuilder.is(value);
		return this;
	}
	
	/**
	 * getSingleResult
	 * 
	 * @param clazz
	 * @return single document that matched the search criteria
	 */
	
	@Override
	public <T> T getSingleResult(Class<T> clazz) {
		DBCollection collection = db.getCollection(getCollectionName());
		return getAsObject(clazz, collection.findOne(queryBuilder.get()));

	}
	
	/**
	 * getResultList
	 * 
	 * @param clazz
	 * @return list of documents that matched the search criteria
	 */
	
	@Override
	public <T> List<T> getResultList(Class<T> clazz) {
		List<T> queryResult = new ArrayList<T>();
		
		DBCollection collection = db.getCollection(getCollectionName());
		
		DBCursor cursor = collection.find(queryBuilder.get());			
		while (cursor.hasNext()) {
			queryResult.add(getAsObject(clazz, cursor.next()));
		}
		return queryResult;
	}	
	
	private <T> T getAsObject(Class<T> clazz, Object object) {
		return gson.fromJson(JSON.serialize(object), clazz);
	}
}