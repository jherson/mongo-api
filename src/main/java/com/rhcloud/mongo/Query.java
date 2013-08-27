package com.rhcloud.mongo;

import java.util.List;

public interface Query<T> {
	
	/**
	 * put
	 * 
	 * @param key
	 * @return Query
	 */
	
	Query<T> field(String key);
	
	/**
	 * is
	 * 
	 * @param value
	 * @return Query
	 */
	
	Query<T> isEqual(Object value);
	
	/**
	 * getSingleResult
	 * 
	 * @param clazz
	 * @return T
	 */
	
	T getSingleResult();
	
	/**
	 * 
	 * @param clazz
	 * @return List<T>
	 */
	
	List<T> getResultList();
}
