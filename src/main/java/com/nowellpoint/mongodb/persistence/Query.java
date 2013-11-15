package com.nowellpoint.mongodb.persistence;

import java.util.List;

public interface Query<T> {
	
	/**
	 * 
	 * @param key
	 * @return Query
	 */
	
	Query<T> field(String key);
	
	/**
	 * 
	 * @param value
	 * @return Query
	 */
	
	Query<T> isEqual(Object value);
	
	/**
	 * 
	 * @return T
	 */
	
	T getSingleResult();
	
	/**
	 * 
	 * @return
	 */
	
	List<T> getResultList();
}
