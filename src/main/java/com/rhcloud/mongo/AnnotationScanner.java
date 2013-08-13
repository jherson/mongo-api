package com.rhcloud.mongo;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.getMethods;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import org.bson.types.ObjectId;

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
	
	@SuppressWarnings("unchecked")
	public static <T> Object getId(Object object) {
		Set<Field> fields = getAllFields(object.getClass(), withAnnotation(Id.class));
		String idField = fields.iterator().next().getName();
		String name = "get" + idField.substring(0, 1).toUpperCase() + idField.substring(1);
		Set<Method> methods = getMethods(object.getClass(), withName(name));
		Object id = null;
		if (methods.size() > 0) {
			try {
				id = methods.iterator().next().invoke(object, new Object[] {});
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (id instanceof ObjectId) {
			return new ObjectId(id.toString());
		}
		return id;
	}
}