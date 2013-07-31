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

public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

	private static final String ISO_8061_FORMAT = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'";

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

	@Override
	public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
		SimpleDateFormat format = new SimpleDateFormat(ISO_8061_FORMAT);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("$date", format.format(date));
		return jsonObject;
	}
}