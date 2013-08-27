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

package com.rhcloud.mongo.adapter;

import java.lang.reflect.Type;

import org.bson.types.ObjectId;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * 
 * @author jherson
 *
 */

public class ObjectIdTypeAdapter implements JsonSerializer<ObjectId>, JsonDeserializer<ObjectId> {

	/**
	 *  deserialize a JsonElement into a org.bson.types.ObjectId. Used by GsonBuilder
	 *  
	 *  @param jsonElement to be deserialized
	 *  @param type
	 *  @param context
	 */
	
	@Override
	public ObjectId deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
                ObjectId id = null;
		if (! jsonElement.isJsonNull()) {
                        id = new ObjectId(jsonElement.getAsJsonObject().get("$oid").getAsString());
                }

		return id;
	}
	
	/**
	 * serialize a org.bson.types.ObjectId into JsonElement. Used by GsonBuilder
	 * 
	 * @param objectId to be serialized
	 * @param type
	 * @param context
	 */
	
	@Override
	public JsonElement serialize(ObjectId objectId, Type type, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("$oid", objectId.toString());
		return jsonObject;
	}
}
