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

package com.nowellpoint.mongodb.persistence.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.nowellpoint.mongodb.persistence.DocumentManager;
import com.nowellpoint.mongodb.persistence.DocumentManagerFactory;
import com.nowellpoint.mongodb.persistence.MongoExceptionCode;
import com.nowellpoint.mongodb.persistence.Query;
import com.nowellpoint.mongodb.persistence.annotation.EmbedMany;
import com.nowellpoint.mongodb.persistence.annotation.EmbedOne;
import com.nowellpoint.mongodb.persistence.annotation.EmbeddedDocument;
import com.nowellpoint.mongodb.persistence.annotation.Id;
import com.nowellpoint.mongodb.persistence.annotation.MappedSuperclass;
import com.nowellpoint.mongodb.persistence.annotation.PostPersist;
import com.nowellpoint.mongodb.persistence.annotation.PrePersist;
import com.nowellpoint.mongodb.persistence.annotation.Property;
import com.nowellpoint.mongodb.persistence.exception.DocumentExistsException;
import com.nowellpoint.mongodb.persistence.exception.PersistenceException;

/**
 * @author jherson
 *
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DocumentManagerImpl implements DocumentManager, Serializable {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -7038813059024799681L;
	
	/**
	 * 
	 */
	
	private static final String ID = "_id"; 
	
	/**
	 * 
	 */
	
	private DocumentManagerFactory dmf;
	
	/**
	 * 
	 */
	
	private DB db;
	
	/**
	 * constructor
	 * @param DocumentManagerFactory
	 */
	
	protected DocumentManagerImpl(DocumentManagerFactory documentManagerFactory) {
		this.dmf = documentManagerFactory;
		this.db = documentManagerFactory.getDB();
	}
	
	/**
	 * 
	 * @return
	 */
	
	@Override
	public DocumentManagerFactory getDocumentManagerFactory() {
		return dmf;
	}
	
	/**
	 * getDB
	 * 
	 * @return DB
	 */
	
	protected DB getDB() {
		return db;
	}
	
	/**
	 * inserts an object into a collection
	 * 
	 * @param clazz
	 * @param object to be inserted
	 * @return T
	 */
	
	@Override
	public <T> T insert(Class<T> clazz, Object object) {
		prePersist(object);
		String collectionName = AnnotationResolver.resolveCollection(object);		
		DBCollection collection = getDB().getCollection(collectionName);
		DBObject dbObject = convertObjectToDocument(object);
		try {
			collection.insert(dbObject);
		} catch (MongoException e) {
			if (e.getCode() == MongoExceptionCode.DOCUMENT_EXISTS) {
				throw new DocumentExistsException(e.getMessage());
			}
		}
		postPersist(object);
		return convertDocumentToObject(object, dbObject);
	}
	
	/**
	 * updates the object in the collection
	 * 
	 * @param clazz
	 * @param object to be updated
	 * @return the updated object
	 */
	
	@Override
	public <T> T update(Class<T> clazz, Object object) {		
		prePersist(object);
		String collectionName = AnnotationResolver.resolveCollection(object);		
		DBCollection collection = getDB().getCollection(collectionName);
		DBObject dbObject = convertObjectToDocument(object);
		WriteResult result = collection.save(dbObject);
		if (result.getError() != null) {
			throw new MongoException(result.getLastError());
		}
		postPersist(object);
		return convertDocumentToObject(clazz, dbObject);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @param id the Object of the object to query
	 * @return object found based on objectId
	 */
	
	@Override
	public <T> T find(Class<T> clazz, Object id) {
		try {
			return (T) createQuery(clazz)
				.field(ID)
				.isEqual(id)
				.getSingleResult();
		} catch (PersistenceException e) {
			return null;
		}
	}
	
	/**
	 * delete a document from a collection
	 * 
	 * @param clazz
	 * @param object to delete
	 */
	
	@Override
	public <T> void delete(Class<T> clazz, Object object) {		
		String collectionName = AnnotationResolver.resolveCollection(object);
		DBCollection collection = getDB().getCollection(collectionName);
		DBObject dbObject = convertObjectToDocument(object);	
		WriteResult wr = collection.remove(new BasicDBObject(ID, new ObjectId(dbObject.get(ID).toString())));
		if (wr.getError() != null) {
			throw new MongoException(wr.getLastError());
		}
	}
	
	/**
	 * delete a document from a collection
	 * 
	 * @param object
	 */
	
	@Override
	public <T> void delete(Object object) {
		String collectionName = AnnotationResolver.resolveCollection(object);
		DBCollection collection = getDB().getCollection(collectionName);
		Object id = resolveId(object);
		WriteResult wr = collection.remove(new BasicDBObject(ID, id));
		if (wr.getError() != null) {
			throw new MongoException(wr.getLastError());
		}
	}
	
	/**
	 * deleteAll - removes all documents in a collection
	 * 
	 * @param clazz
	 */
	
	@Override
	public <T> void deleteAll(Class<T> clazz) {
		String collectionName = AnnotationResolver.resolveCollection(clazz);
		DBCollection collection = getDB().getCollection(collectionName);
		collection.remove(new BasicDBObject());
	}
	
	/**
	 * createQuery
	 * 
	 * @param clazz
	 * @return Query
	 */
	
	@Override
	public <T> Query createQuery(Class<T> clazz) {		
		return new QueryImpl(this, clazz);
	}
	
	/**
	 * 
	 */
	
	@Override
	public void refresh(Object object) {
		
	}
	
	private void prePersist(Object object) {
		Set<Method> methods = getAllMethods(object);
		for (Method method : methods) {
			if (method.isAnnotationPresent(PrePersist.class)) {
			    try {
					method.invoke(object, new Object[] {});
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new PersistenceException(e);
				}
			    break;
			}
		}
	}
	
	private void postPersist(Object object) {
		Set<Method> methods = getAllMethods(object);
		for (Method method : methods) {
			if (method.isAnnotationPresent(PostPersist.class)) {
			    try {
					method.invoke(object, new Object[] {});
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new PersistenceException(e);
				}
			    break;
			}
		}
	}
	
	private Set<Field> getAllFields(Object object) {
		Set<Field> fields = new LinkedHashSet<Field>();
		if (object.getClass().getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
			fields.addAll(Arrays.asList(object.getClass().getSuperclass().getDeclaredFields()));
		}
		fields.addAll(Arrays.asList(object.getClass().getDeclaredFields()));
		return fields;
	}
	
	private Set<Method> getAllMethods(Object object) {
		Set<Method> methods = new LinkedHashSet<Method>();
		if (object.getClass().getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
			methods.addAll(Arrays.asList(object.getClass().getSuperclass().getDeclaredMethods()));
		}
		methods.addAll(Arrays.asList(object.getClass().getDeclaredMethods()));		
		return methods;
	}
	
	protected BasicDBObject convertObjectToDocument(Object object) {		
		
		/**
		 * create the MongoDB document
		 */
		
		BasicDBObject document = new BasicDBObject();
		
		/**
		 * invoke superclass getters if the superclass is annotated with MappedSuperclass
		 */
		
		if (object.getClass().getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
			invokeGetters(object.getClass().getSuperclass(), object, document);
		}
		
		/**
		 * invoke getters on the object itself
		 */
		
		invokeGetters(object.getClass(), object, document);
		
		/**
		 * return the document
		 */
		
		return document;
	}
	
	protected <T> T convertDocumentToObject(Object object, DBObject document) {
		
		/**
		 * invoke setters on the superclass of object if the superclass is annotated with MappedSuperclass
		 */
		
		if (object.getClass().getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
			invokeSetters(object.getClass().getSuperclass(), object, document);
		}
		
		/**
		 * invoke setters on the object itself
		 */
		
		invokeSetters(object.getClass(), object, document);
		
		/**
		 * return the object
		 */
		
		return (T) object;
	}
	
	private Object getIdValue(Object object, Field field) throws PersistenceException {
		Object id = getFieldValue(object, field);
		if (field.getType().isAssignableFrom(ObjectId.class)) {
	    	id = new ObjectId(id.toString()); 	
	    } 
		return id;
	}
	
	/**
	 * 
	 * @param object
	 * @param field
	 * @return Object
	 * @throws PersistenceException
	 */
	
	private Object parseProperty(Object object, Field field) throws PersistenceException {
		return getFieldValue(object, field);
	}
	
	private String getPropertyName(Field field) {
		String name = field.getAnnotation(Property.class).name();
		return name.trim().length() > 0 ? name : field.getName();
	}
	
	private String getEmbedOneName(Field field) {
		String name = field.getAnnotation(EmbedOne.class).name();
		return name.trim().length() > 0 ? name : field.getName();
	}
	
	private String getEmbedManyName(Field field) {
		String name = field.getAnnotation(EmbedMany.class).name();
		return name.trim().length() > 0 ? name : field.getName();
	}
	
	/**
	 * 
	 * @param object
	 * @param field
	 * @return
	 */
	
	private Object getFieldValue(Object object, Field field) {
		try {
		    Method method = object.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[] {});	
		    return method.invoke(object, new Object[] {});
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new PersistenceException(e);
		}	
	}
	
	private Object resolveId(Object object) {
		Object id = null;
		Set<Field> fields = getAllFields(object);
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {					
				id = getIdValue(object, field);
			}
		}
		return id;
	}
	
	private void invokeGetters(Class clazz, Object object, DBObject document) {
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				document.put(ID, getIdValue(object, field));
			} else if (field.isAnnotationPresent(Property.class)) {		
				document.put(getPropertyName(field), parseProperty(object, field));
			} else if (field.isAnnotationPresent(EmbedOne.class)) {
				document.put(getEmbedOneName(field), parseEmbedOne(object, field));
			} else if (field.isAnnotationPresent(EmbedMany.class)) {
				document.put(getEmbedManyName(field), parseEmbedMany(object, field));
			}
		}
	}
	
	private void invokeSetter(Class clazz, Object object, Field field, Object value) {
		try {
		    Method method = clazz.getMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[] {field.getType()});	
		    method.invoke(object, new Object[] {value});
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new PersistenceException(e);
		}
	}
	
	private void invokeSetters(Class clazz, Object object, DBObject document) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				invokeSetter(clazz, object, field, document.get(ID));
			} else if (field.isAnnotationPresent(Property.class)) {	
				invokeSetter(clazz, object, field, document.get(getPropertyName(field)));
			} else if (field.isAnnotationPresent(EmbedOne.class)) {
				invokeSetter(clazz, object, field, document.get(getEmbedOneName(field)));
			} else if (field.isAnnotationPresent(EmbedMany.class)) {
				invokeSetter(clazz, object, field, document.get(getEmbedManyName(field)));
			}
		}
	}
	
	private BasicDBObject parseEmbedOne(Object object, Field field) throws PersistenceException {
		validateEmbeddedDocument(field);
		Object embeddedDocument = getFieldValue(object, field);
		return convertObjectToDocument(embeddedDocument);
	}
	
	/**
	 * 
	 * @param collection
	 * @return BasicDBList
	 */
	
	private BasicDBList parseEmbedMany(Object object, Field field) {
		validateEmbeddedDocument(field);
		Object embeddedDocument = getFieldValue(object, field);		
		List<?> list = (List<?>) embeddedDocument;
		BasicDBList dbList = new BasicDBList();
		for (Object embed : list) {
			dbList.add(convertObjectToDocument(embed));
		}		
		return dbList;
	}
	
	private void validateEmbeddedDocument(Field field) {
		ParameterizedType type = (ParameterizedType) field.getGenericType();
		Class<?> embeddedClass = (Class<?>) type.getActualTypeArguments()[0];
		if (! embeddedClass.isAnnotationPresent(EmbeddedDocument.class)) {
			throw new PersistenceException("Missing EmbeddedDocument annotation from " + embeddedClass.getClass().getName());
		}
	}
}