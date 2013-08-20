package com.rhcloud.mongo.spi;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Logger;

import org.bson.types.ObjectId;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.rhcloud.mongo.annotation.Document;
import com.rhcloud.mongo.annotation.Id;

public class AnnotationScanner {
	
	/**
	 * 
	 */
	
	private static final Logger log = Logger.getLogger(AnnotationScanner.class.getName());
	
	/**
	 * startScan
	 */

	public void startScan() {
		
		log.info(System.getProperty("java.class.path"));
		
		/**
		 * 
		 */
		
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath())
				.setScanners(new TypeAnnotationsScanner(), new MethodAnnotationsScanner(), new TypeElementsScanner()));

		/**
		 * 
		 */
		
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Document.class);
		
		log.info("MongoDB @Document annotations: " + annotated.size());
	}

	/**
	 * getCollectionName
	 * 
	 * @param clazz
	 * @return T
	 */
	
	public static <T> String getCollectionName(Object object) {		
		return getCollectionName(object.getClass());
	}
	
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
		Document document = (Document) annotation;
		return document.collection();
	}
	
	/**
	 * getId
	 * 
	 * @param object
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public static <T> Object getId(Object object) {
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
}