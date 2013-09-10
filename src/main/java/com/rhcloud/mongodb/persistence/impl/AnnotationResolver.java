package com.rhcloud.mongodb.persistence.impl;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withName;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;

import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.rhcloud.mongodb.persistence.annotation.Document;
import com.rhcloud.mongodb.persistence.annotation.Id;
import com.rhcloud.mongodb.persistence.annotation.Index;

public class AnnotationResolver implements Serializable {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -3312853443514678792L;
	
	/**
	 * 
	 */
	
	private static Map<Class<?>, String> documentMap = Maps.newConcurrentMap();

	public static <T> String resolveCollection(Object object) {
		return resolveCollection(object.getClass());
	}
	
	public static <T> String resolveCollection(Class<T> clazz) {
		String collectionName = null;
		if (documentMap.containsKey(clazz)) {
			collectionName = documentMap.get(clazz);
		} else {
			collectionName = getCollectionName(clazz);	
			documentMap.put(clazz, collectionName);
		}
		return collectionName;
	}
	
	public static <T> DBObject resolveIndexes(Object object) {		
		DBObject dbObject = null;
		Annotation[] annotations = object.getClass().getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof Index) {
				if (dbObject == null) {
					dbObject = new BasicDBObject();
				}
				dbObject.put(((Index) annotation).key(), ((Index) annotation).acsending());
			}
		}
		return dbObject;
	}
	
	/**
	 * getId
	 * 
	 * @param object
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public static <T> Object resolveId(Object object) {
		Set<Field> fields = getAllFields(object.getClass(), withAnnotation(Id.class));
		String idField = fields.iterator().next().getName();
		String name = "get" + idField.substring(0, 1).toUpperCase() + idField.substring(1);
		Set<Method> methods = getAllMethods(object.getClass(), withName(name));
		Object id = null;
		if (methods.size() > 0) {
			try {
				id = methods.iterator().next().invoke(object, new Object[] {});
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if (id instanceof ObjectId) {
			return new ObjectId(id.toString());
		}
		return id;
	}
	
	private static <T> String getCollectionName(Class<T> clazz) {		
		Annotation annotation = clazz.getAnnotation(Document.class);
		if (annotation == null) {
			throw new RuntimeException("Class must be annotated with the Document annotation");
		}
		Document document = (Document) annotation;
		return document.collection();
	}
	
}