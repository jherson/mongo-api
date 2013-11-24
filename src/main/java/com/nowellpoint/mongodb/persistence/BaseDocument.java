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

import java.io.Serializable;

import javax.json.Json;
import javax.json.JsonObject;

import org.bson.types.ObjectId;

import com.mongodb.DBObject;
import com.nowellpoint.mongodb.persistence.annotation.Id;
import com.nowellpoint.mongodb.persistence.annotation.MappedSuperclass;

/**
 * 
 * @author jherson
 *
 */

@MappedSuperclass
public abstract class BaseDocument implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -3094028441765571549L;
	
	/**
	 * 
	 */
	
	@Id
	private ObjectId id;
	
	/**
	 * constructor
	 */
	
	public BaseDocument() {
		
	}
	
	/**
	 * constructor
	 * @param json
	 */
	
	public BaseDocument(String json) {
		
	}
	
	/**
	 * constructor
	 * @param dbObject
	 */
	
	public BaseDocument(DBObject dbObject) {
		
	}
	
	/**
	 * get id of the object
	 * @return ObjectId
	 */
	
	public ObjectId getId() {
		return id;
	}

	/**
	 * setId
	 * @param id
	 */
	
	public void setId(ObjectId id) {
		this.id = id;
	}
	
	/**
	 * @return json String
	 */
	
	public String getAsJSON() {
		JsonObject json = Json.createObjectBuilder().add("_id", getId().toString()).build();
		return json.toString();
	}	
}