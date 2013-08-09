package com.rhcloud.mongo;

import java.lang.annotation.Annotation;

import com.rhcloud.mongo.annotation.Document;

public class AnnotationScanner {

	/**
	 * getCollectionName
	 * 
	 * @param clazz
	 * @return
	 */
	
	public static <T> String getCollectionName(Class<T> clazz) {		
		Annotation annotation = clazz.getAnnotation(Document.class);
		if (annotation == null) {
			throw new RuntimeException("Class must be annotated with the Document annotation");
		}
		Document document = (Document) annotation;;
		return document.collectionName();
	}
}