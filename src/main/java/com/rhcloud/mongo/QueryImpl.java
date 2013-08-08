package com.rhcloud.mongo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;

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
	
	public QueryImpl(DB db, Gson gson) {
		this.db = db;
		this.gson = gson;
		this.queryBuilder = QueryBuilder.start();
	}
	
	@Override
	public Query setCollectionName(String collectionName) {
		this.collectionName = collectionName;
		return this;
	}
	
	@Override
	public String getCollectionName() {
		return collectionName;
	}
	
	@Override
	public Query put(String key) {
		queryBuilder.put(key);
		return this;
	}
	
	@Override
	public Query is(Object value) {
		queryBuilder.is(value);
		return this;
	}
	
	@Override
	public <T> T getSingleResult(Class<T> clazz) {
		DBCollection collection = db.getCollection(getCollectionName());
		return getAsObject(clazz, collection.findOne(queryBuilder.get()));

	}
		
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