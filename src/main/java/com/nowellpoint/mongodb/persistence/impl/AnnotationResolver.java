package com.nowellpoint.mongodb.persistence.impl;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.nowellpoint.mongodb.persistence.annotation.Collection;
import com.nowellpoint.mongodb.persistence.annotation.Document;
import com.nowellpoint.mongodb.persistence.annotation.Id;
import com.nowellpoint.mongodb.persistence.annotation.Index;
import com.nowellpoint.mongodb.persistence.exception.PersistenceException;

public class AnnotationResolver implements Serializable {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -3312853443514678792L;
	
	/**
	 * 
	 */
	
	private static Map<Class<?>, String> documentMap = new ConcurrentHashMap<Class<?>, String>();

	public static <T> String resolveCollection(Object object) {
		return resolveCollection(object.getClass());
	}
	
	public static <T> String resolveCollection(Class<T> clazz) {
		String collectionName = null;
		if (documentMap.containsKey(clazz)) {
			collectionName = documentMap.get(clazz);
		} else {
			collectionName = resolveCollectionName(clazz);	
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
	
	public static Object getId(Object object) {
		Object id = null;
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {					
				try {
					MethodHandle handle = MethodHandles.lookup().findSetter(object.getClass(), field.getName(), object.getClass());					
					id = handle.invoke(object);
					
				} catch (Throwable e) {
					throw new PersistenceException(e);
				}
				
//				try {
//					Method method = object.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[] {});
//					id = method.invoke(object, new Object[] {});
//					break;
//				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//					throw new PersistenceException(e);
//				} 	
			}
		}
		
		if (id == null) {
			throw new PersistenceException("No Id field found for " + object.getClass().getName());
		}
		
		if (id instanceof ObjectId) {
            id = new ObjectId(id.toString());
        }
		
		return id;
	}
	
	public static void setId(Object object, Object id) {
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			System.out.println("set id: " + field.getName());
			if (field.isAnnotationPresent(Id.class)) {					
				try {
					MethodHandle handle = MethodHandles.lookup().findGetter(object.getClass(), field.getName(), object.getClass());
					handle.invokeWithArguments(id);
					
				} catch (Throwable e) {
					e.printStackTrace();
					throw new PersistenceException(e);
				}
				
//				try {
//					Method method = object.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[] {});
//					id = method.invoke(object, new Object[] {});
//					break;
//				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//					throw new PersistenceException(e);
//				} 	
			}
		}
	}
	
	//private static void invokeMethod()
	
	/**
	 * getId
	 * 
	 * @param object
	 * @return
	 */
	
	private static <T> String resolveCollectionName(Class<T> clazz) {	
		String collectionName = null;
		if (! clazz.isAnnotationPresent(Document.class)) {
			throw new IllegalArgumentException("Class must be annotated with the Document annotation");
		}
		
		if (! clazz.isAnnotationPresent(Collection.class)) {
			throw new IllegalArgumentException("Class must be annotated with the Collection annotation");
		}
		
		Collection collection = (Collection) clazz.getAnnotation(Collection.class);
		if (collection.name().trim().length() > 0) {
			collectionName = collection.name();
		} else {
			collectionName = clazz.getSimpleName();
		}
		return collectionName;
	}
	
}