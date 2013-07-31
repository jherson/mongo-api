package com.rhcloud.mongo.adapter;

import java.lang.reflect.Type;

import org.bson.types.ObjectId;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ObjectIdTypeAdapter implements JsonSerializer<ObjectId>, JsonDeserializer<ObjectId> {

	@Override
	public ObjectId deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
		if (jsonElement.isJsonNull())
			return null;

		return new ObjectId(jsonElement.getAsJsonObject().get("$oid").getAsString());
	}
	
	@Override
	public JsonElement serialize(ObjectId objectId, Type type, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("$oid", objectId.toString());
		return jsonObject;
	}
}