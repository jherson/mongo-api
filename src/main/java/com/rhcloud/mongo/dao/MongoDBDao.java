
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

package com.rhcloud.mongo.dao;

import org.bson.types.ObjectId;

import com.mongodb.DB;
import com.rhcloud.mongo.Query;

/**
 * @author jherson
 *
 */
public interface MongoDBDao {
	
	
	public void setDB(DB db);
	public <T> T insert(Class<T> clazz, Object object);
	public <T> T update(Class<T> clazz, Object object);
	public <T> void delete(Class<T> clazz, Object object);
	public <T> T find(Class<T> clazz, ObjectId id);	
	
	public Query createQuery();
}