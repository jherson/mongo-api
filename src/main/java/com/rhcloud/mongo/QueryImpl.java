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
 * @param <T>
 * @param <T>
 *
 */
public class QueryImpl<T> implements Query<T> {
	
	/**
	 * 
	 */
	
	protected DB db;	
	
	/**
	 * 
	 */
	
	protected Class<T> clazz;
	
	/**
	 * 
	 */
	
	protected DBCollection collection;
	
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
	
	public QueryImpl(Class<T> clazz, DB db, Gson gson) {
		this.db = db;
		this.clazz = clazz;		
		this.gson = gson;			
		this.collection = db.getCollection(AnnotationScanner.getCollectionName(clazz));
		this.queryBuilder = QueryBuilder.start();
	}
	
	/**
	 * put
	 * 
	 * @param key 
	 * @return Query object
	 */
	
	@Override
	public Query<T> put(String key) {
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
	public Query<T> is(Object value) {
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
	public T getSingleResult() {
		return getAsObject(clazz, collection.findOne(queryBuilder.get()));
	}
	
	/**
	 * getResultList
	 * 
	 * @param clazz
	 * @return list of documents that matched the search criteria
	 */
	
	@Override
	public List<T> getResultList() {			
		DBCursor cursor = collection.find(queryBuilder.get());
		
		List<T> queryResult = new ArrayList<T>();
		while (cursor.hasNext()) {
			queryResult.add(getAsObject(clazz, cursor.next()));
		}
		return queryResult;
	}
		
	private T getAsObject(Class<T> clazz, Object object) {
		return gson.fromJson(JSON.serialize(object), clazz);
	}
}