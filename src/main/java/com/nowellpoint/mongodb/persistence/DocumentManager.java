
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

/**
 * @author jherson
 *
 */
public interface DocumentManager {
	
	/**
	 * getDocumentManagerFactory
	 * @return DocumentManagerFactory
	 */
	
	DocumentManagerFactory getDocumentManagerFactory();
	
	/**
	 * persist
	 * @param document
	 */
	
	void persist(Object document);
	
	/**
	 * merge
	 * @param document
	 * @return T
	 */
	
	<T> T merge(T document);
	
	/**
	 * remove
	 * @param document
	 */
	
	void remove(Object document);
	
	/**
	 * find
	 * @param documentClass
	 * @param documentId
	 * @return T
	 */
	
	<T> T find(Class<T> documentClass, Object documentId);
	
	/**
	 * createQuery
	 * @param clazz
	 * @return Query<T>
	 */
	
	<T> Query<T> createQuery(Class<T> clazz);
	
	/**
	 * refresh
	 * @param document
	 */
	
	void refresh(Object document);
}