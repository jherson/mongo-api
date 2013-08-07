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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author jherson
 *
 */

public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

	private static final String ISO_8061_FORMAT = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'";

	/**
	 *  deserialize a JsonElement into a java.util.Date. Used by GsonBuilder
	 *  
	 *  @param jsonElement to be deserialized
	 *  @param type
	 *  @param context
	 */
	
	@Override
	public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {				
		SimpleDateFormat format = new SimpleDateFormat(ISO_8061_FORMAT);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			return format.parse(jsonElement.getAsJsonObject().get("$date").getAsString());
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}

	/**
	 * serialize a java.util.Date into JsonElement. Used by GsonBuilder
	 * 
	 * @param date to be serialized
	 * @param type
	 * @param context
	 */
	
	@Override
	public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
		SimpleDateFormat format = new SimpleDateFormat(ISO_8061_FORMAT);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("$date", format.format(date));
		return jsonObject;
	}
}