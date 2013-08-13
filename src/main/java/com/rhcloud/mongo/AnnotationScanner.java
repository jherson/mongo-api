package com.rhcloud.mongo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.rhcloud.mongo.annotation.Document;
import com.rhcloud.mongo.annotation.Id;

public class AnnotationScanner {

	/**
	 * getCollectionName
	 * 
	 * @param clazz
	 * @return T
	 */
	
	public static <T> String getCollectionName(Class<T> clazz) {		
		Annotation annotation = clazz.getAnnotation(Document.class);
		if (annotation == null) {
			throw new RuntimeException("Class must be annotated with the Document annotation");
		}
		Document document = (Document) annotation;;
		return document.collectionName();
	}
	
	public static <T> Field getIdField(Class<T> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class)) {
				return field;
			}
		}
		return null;
	}
}