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
import java.util.Date;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.nowellpoint.mongodb.persistence.adapter.ObjectIdTypeAdapter;
import com.nowellpoint.mongodb.persistence.annotation.Id;

/**
 * 
 * @author jherson
 *
 */

public abstract class BaseDocument implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -3094028441765571549L;
	
	/**
	 * 
	 */
	
	private static final Gson GSON = new GsonBuilder().			
			serializeNulls().	
			registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).
			registerTypeAdapter(Date.class, new DateTypeAdapter()).
			create();
	
	/**
	 * 
	 */
	
	@Id
	@SerializedName("_id")
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
		GSON.fromJson(json, this.getClass());
	}
	
	/**
	 * constructor
	 * @param dbObject
	 */
	
	public BaseDocument(DBObject dbObject) {
		GSON.fromJson(JSON.serialize(dbObject), this.getClass());
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
	 * getAsJSON
	 * 
	 * @return json String
	 */
	
	public String getAsJSON() {
		return GSON.toJson(this);
	}	
	
	/**
	 * getAsDBObject
	 * 
	 * @return DBObject
	 */
	
	public DBObject getAsDBObject() {
		return (DBObject) JSON.parse(GSON.toJson(this));
	}
}
