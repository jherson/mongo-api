package com.rhcloud.mongo;

import java.util.List;

public interface Query {

	/**
	 * setCollectionName
	 * 
	 * @param collectionName
	 * @return Query
	 */
	
	public Query setCollectionName(String collectionName);
	
	/**
	 * getCollectionName
	 * 
	 * @return String
	 */
	
	public String getCollectionName();
	
	/**
	 * put
	 * 
	 * @param key
	 * @return Query
	 */
	
	public Query put(String key);
	
	/**
	 * is
	 * 
	 * @param value
	 * @return
	 */
	
	public Query is(Object value);
	
	/**
	 * getSingleResult
	 * 
	 * @param clazz
	 * @return T
	 */
	
	public <T> T getSingleResult(Class<T> clazz);
	
	/**
	 * 
	 * @param clazz
	 * @return List<T>
	 */
	
	public <T> List<T> getResultList(Class<T> clazz);
}