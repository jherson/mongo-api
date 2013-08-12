package com.rhcloud.mongo;

import java.util.List;

public interface Query<T> {
	
	/**
	 * put
	 * 
	 * @param key
	 * @return Query
	 */
	
	public Query<T> put(String key);
	
	/**
	 * is
	 * 
	 * @param value
	 * @return Query
	 */
	
	public Query<T> is(Object value);
	
	/**
	 * getSingleResult
	 * 
	 * @param clazz
	 * @return T
	 */
	
	public T getSingleResult();
	
	/**
	 * 
	 * @param clazz
	 * @return List<T>
	 */
	
	public List<T> getResultList();
}