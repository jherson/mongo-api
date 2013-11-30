package com.nowellpoint.mongodb.persistence.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.nowellpoint.mongodb.persistence.DocumentManager;
import com.nowellpoint.mongodb.persistence.DocumentManagerFactory;
import com.nowellpoint.mongodb.persistence.DocumentNameResolver;
import com.nowellpoint.mongodb.persistence.annotation.EmbedMany;
import com.nowellpoint.mongodb.persistence.annotation.EmbedOne;
import com.nowellpoint.mongodb.persistence.annotation.EmbeddedDocument;
import com.nowellpoint.mongodb.persistence.annotation.Id;
import com.nowellpoint.mongodb.persistence.annotation.MappedSuperclass;
import com.nowellpoint.mongodb.persistence.annotation.PostPersist;
import com.nowellpoint.mongodb.persistence.annotation.PrePersist;
import com.nowellpoint.mongodb.persistence.annotation.Property;
import com.nowellpoint.mongodb.persistence.datastore.DatastoreConfig;
import com.nowellpoint.mongodb.persistence.exception.DatastoreConfigurationException;
import com.nowellpoint.mongodb.persistence.exception.PersistenceException;

public class DocumentManagerFactoryImpl implements DocumentManagerFactory, Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 8406720002314923220L;
	
	/**
	 * 
	 */
	
	private static final Logger LOG = Logger.getLogger(DocumentManagerFactoryImpl.class.getName());
	
	/**
	 * 
	 */
	
	public static final String ID = "_id"; 				
	
	/**
	 * 
	 */
	
	private MongoClient mongo;
	
	/**
	 * 
	 */
	
	private DB db;
	
	/**
	 * 
	 */
	
	private boolean isOpen;
	
	/**
	 * 
	 */
	
	private DocumentNameResolver documentNameResolver;
	
	/**
	 * 
	 * @param config
	 * @throws DatastoreConfigurationException
	 */
	
	public DocumentManagerFactoryImpl(DatastoreConfig config) throws DatastoreConfigurationException {
		
		/**
		 * 
		 */
		
		LOG.info("Connecting to MongoDB...(" + config.getDatabase() + "@" + config.getHost() + ":" + config.getPort() + ")");
		
		/**
		 * configure the MongoClient
		 */
		
		try {
			mongo = new MongoClient(new ServerAddress(config.getHost(), config.getPort()));
		} catch (UnknownHostException e) {
			throw new DatastoreConfigurationException(e);
		}		
		
		/**
		 * set the mongo options
		 */
		
		mongo.setReadPreference(ReadPreference.primaryPreferred());
		mongo.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		
		/**
		 * log into the DB
		 */
		
		db = mongo.getDB(config.getDatabase());
		
		/**
		 * handle authentication 
		 */
		
		if (config.getUsername() != null && config.getUsername().trim().length() > 0) {
			authenticate(config.getUsername(), config.getPassword());
		}
		
		/**
		 * set the isOpen flag 
		 */
		
		isOpen = Boolean.TRUE;
		
		/**
		 * 
		 */
		
		LOG.info("Connection to MongoDB...successful");
		
		/**
		 * add default document name resolver
		 */
		
		documentNameResolver = new DocumentNameResolverImpl().getInstance();
	}
	
	/**
	 * 
	 */
	
	@Override
	public DB getDB() {
		return db;
	}
	
	/**
	 * 
	 */
	
	@Override
	public void close() {
		if (isOpen) {
			mongo.close();
			isOpen = Boolean.FALSE;
		}
	}
	
	/**
	 * 
	 */
	
	@Override
	public boolean isOpen() {
		return isOpen;
	}
	
	/**
	 * 
	 */
	
	@Override
	public DocumentManager createDocumentManager() {
		return new DocumentManagerImpl(this);
	}
	
	/**
	 * 
	 */
	
	public <T> String resolveDocumentName(Class<T> clazz) {
		return documentNameResolver.resolveDocumentName(clazz);
	}
	
	/**
	 * 
	 */
	
	private void authenticate(String username, String password) throws MongoException {
		if (!db.authenticate(username, password.toCharArray())) {
			throw new MongoException(String.format("Failed to authenticate against db: %s", db));
		}
	}
	
	/**********************************************************************************************
	 * 
	 * private methods
	 * 
	 **********************************************************************************************/
	
	public void prePersist(Object object) {
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
	
	public void postPersist(Object object) {
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
		
		if (object.getClass().getSuperclass() != null && object.getClass().getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
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
	
	@SuppressWarnings("unchecked")
	protected <T> T convertDocumentToObject(Object object, DBObject document) {
		
		/**
		 * invoke setters on the superclass of object if the superclass is annotated with MappedSuperclass
		 */
		
		if (object.getClass().getSuperclass() != null && object.getClass().getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
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
		    Object value = method.invoke(object, new Object[] {});
            if (field.getType().isAssignableFrom(Locale.class)) {            	
		    	value = String.valueOf(value);
		    }
            return value;
		} catch (NoSuchMethodException e) {
			LOG.info("Unable to find get method for mapped property field: " + field.getName());
			return null;
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new PersistenceException(e);
		}
	}
	
	protected Object resolveId(Object object) {
		Object id = null;
		Set<Field> fields = getAllFields(object);
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {					
				id = getIdValue(object, field);
			}
		}
		return id;
	}
	
	protected void resolveId(Object object, Object id) {
		Set<Field> fields = getAllFields(object);
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {					
				invokeSetter(object.getClass(), object, field, id);
			}
		}
	}
	
	private void invokeGetters(Class<?> clazz, Object object, DBObject document) {
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
	
	private void invokeSetter(Class<?> clazz, Object object, Field field, Object value) {
		if (value != null) {
			try {
			    Method method = clazz.getMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[] {field.getType()});	
			    if (field.getType().isAssignableFrom(Locale.class)) {
			    	value = Locale.forLanguageTag(value.toString());
			    }
			    method.invoke(object, new Object[] {value});
			} catch (NoSuchMethodException e) {
				LOG.info("Unable to find set method for mapped property field: " + field.getName());
				return;
			} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new PersistenceException(e);
			}
		}
	}
	
	private void invokeSetters(Class<?> clazz, Object object, DBObject document) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Id.class)) {
				invokeSetter(clazz, object, field, document.get(ID));
			} else if (field.isAnnotationPresent(Property.class)) {	
				invokeSetter(clazz, object, field, document.get(getPropertyName(field)));
			} else if (field.isAnnotationPresent(EmbedOne.class)) {
				invokeSetter(clazz, object, field, convertEmbedOneToObject(field, document));
			} else if (field.isAnnotationPresent(EmbedMany.class)) {
				invokeSetter(clazz, object, field, document.get(getEmbedManyName(field)));
			}
		}
	}
	
	private Object convertEmbedOneToObject(Field field, DBObject document) {
		DBObject embed = (DBObject) document.get(getEmbedOneName(field));
		Object object = null;
		try {
			object = field.getType().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new PersistenceException(e);
		}
		return convertDocumentToObject(object, embed);
	}
	
	private BasicDBObject parseEmbedOne(Object object, Field field) throws PersistenceException {
		if (! field.getType().isAnnotationPresent(EmbeddedDocument.class)) {
			throw new PersistenceException("Missing EmbeddedDocument annotation from " + field.getType().getClass().getName());
		}
		
		Object embeddedDocument = getFieldValue(object, field);
		if (embeddedDocument == null) {
			try {
				embeddedDocument = field.getType().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new PersistenceException(e);
			}
		}
		
		return convertObjectToDocument(embeddedDocument);
	}
	
	/**
	 * 
	 * @param collection
	 * @return BasicDBList
	 */
	
	private BasicDBList parseEmbedMany(Object object, Field field) {
		ParameterizedType type = (ParameterizedType) field.getGenericType();
		Class<?> embeddedClass = (Class<?>) type.getActualTypeArguments()[0];
		if (! embeddedClass.isAnnotationPresent(EmbeddedDocument.class)) {
			throw new PersistenceException("Missing EmbeddedDocument annotation from " + embeddedClass.getClass().getName());
		}
		
		Object embeddedDocument = getFieldValue(object, field);	
		if (embeddedDocument == null) {
			try {
				embeddedDocument = field.getType().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new PersistenceException(e);
			}
		}
		
		List<?> list = (List<?>) embeddedDocument;
		BasicDBList dbList = new BasicDBList();
		for (Object embed : list) {
			dbList.add(convertObjectToDocument(embed));
		}
		
		return dbList;
	}
}