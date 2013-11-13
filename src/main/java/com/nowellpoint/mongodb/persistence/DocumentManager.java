
/**
 * 
 * Copyright 2013 John D. Herson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.nowellpoint.mongodb.persistence;

import org.bson.types.ObjectId;

import com.mongodb.DB;

/**
 * @author jherson
 *
 */
public interface DocumentManager {
	
	/**
	 * 
	 * @param db
	 */
	
	void setDB(DB db);
	
	/**
	 * insert
	 * @param clazz
	 * @param object
	 * @return T
	 */
	
	<T> T insert(Class<T> clazz, Object object);
	
	/**
	 * update
	 * 
	 * @param clazz
	 * @param object
	 * @return T
	 */
	
	<T> T update(Class<T> clazz, Object object);
	
	/**
	 * delete
	 * 
	 * @param clazz
	 * @param object
	 */
	
	<T> void delete(Class<T> clazz, Object object);

	/**
	 * delete
	 * 
	 * @param clazz
	 */
	
	<T> void delete(Object object);
	
	/**
	 * delete
	 * 
	 * @param clazz
	 */
	
	<T> void deleteAll(Class<T> clazz);
	
	/**
	 * find
	 * 
	 * @param clazz
	 * @param id
	 * @return T
	 */
	
	<T> T find(Class<T> clazz, ObjectId id);	
	
	/**
	 * createQuery
	 * 
	 * @param clazz
	 * @return Query
	 */
	
	<T> Query<T> createQuery(Class<T> clazz);
	
	/**
	 * close
	 */
	
	void close();
}
