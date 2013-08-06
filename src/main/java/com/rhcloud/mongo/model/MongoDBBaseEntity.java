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
package com.rhcloud.mongo.model;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.rhcloud.mongo.adapter.ObjectIdTypeAdapter;

public abstract class MongoDBBaseEntity implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -3094028441765571549L;
	
	/**
	 * 
	 */
	
	private static final Gson gson = new GsonBuilder().			
			serializeNulls().	
			registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).
			registerTypeAdapter(Date.class, new DateTypeAdapter()).
			create();
	
	/**
	 * 
	 */
	
	@SerializedName("_id")
	private ObjectId _id;
	
	public MongoDBBaseEntity() {
		
	}
	
	public MongoDBBaseEntity(String json) {
		gson.fromJson(json, this.getClass());
	}
	
	public ObjectId getId() {
		return _id;
	}

	public void setId(ObjectId id) {
		this._id = id;
	}
	
	public String getAsJSON() {
		return gson.toJson(this);
	}	
}