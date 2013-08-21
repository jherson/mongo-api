package com.rhcloud.mongo.impl;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.enterprise.inject.spi.InjectionPoint;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.rhcloud.mongo.DatastoreConfig;
import com.rhcloud.mongo.DocumentManager;
import com.rhcloud.mongo.DocumentManagerFactory;
import com.rhcloud.mongo.exception.MongoDBConfigurationException;
import com.rhcloud.mongo.qualifier.MongoDBDatastore;
import com.rhcloud.mongo.qualifier.Provider;

public class DocumentManagerFactoryImpl implements DocumentManagerFactory, Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 8406720002314923220L;
	
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
	 * constructor
	 * 
	 * @param mongo
	 * @param db
	 */
	
	public DocumentManagerFactoryImpl(MongoClient mongo, DB db) {
		this.mongo = mongo;
		this.db = db;
		this.isOpen = Boolean.TRUE;
	}
	
	/**
	 * constructor
	 * 
	 * @param config
	 * @throws MongoDBConfigurationException 
	 */
	
	public DocumentManagerFactoryImpl(DatastoreConfig config) throws MongoDBConfigurationException {
		initDB(config);
	}
	
	/**
	 * constructor
	 * 
	 * @param properties
	 * @throws MongoDBConfigurationException 
	 */
	
	public DocumentManagerFactoryImpl(Properties properties) throws MongoDBConfigurationException {
		
		/**
		 * build the config from properties 
		 */
		
		DatastoreConfig config = new DatastoreConfig();			
		config.setHost(properties.getProperty("mongodb.db.host"));
		config.setPort(Integer.decode(properties.getProperty("mongodb.db.port")));
		config.setDatabase(properties.getProperty("mongodb.db.name"));
		config.setUsername(properties.getProperty("mongodb.db.username"));
		config.setPassword(properties.getProperty("mongodb.db.password"));
		
		/**
		 * initialize the MongoDB instance
		 */
		
		initDB(config);
	}
	
	/**
	 * constructor
	 * 
	 * @param injectionPoint
	 * @throws MongoDBConfigurationException 
	 */
	
	public DocumentManagerFactoryImpl(InjectionPoint injectionPoint) throws MongoDBConfigurationException {
		
		/**
		 * get the MongoDBDatastore annotation from the injected class
		 */
		
		MongoDBDatastore datastore = injectionPoint.getAnnotated().getAnnotation(MongoDBDatastore.class);
		
		/**
		 * build the config 
		 */
		DatastoreConfig config = new DatastoreConfig();
		
		/**
		 * use environment variables to build the Datastore config
		 */
		
		if (datastore.provider().equals(Provider.OPENSHIFT)) {
			config.setHost(System.getenv("OPENSHIFT_MONGODB_DB_HOST"));
			config.setPort(Integer.decode(System.getenv("OPENSHIFT_MONGODB_DB_PORT")));
			config.setDatabase(System.getenv("OPENSHIFT_APP_NAME"));
			config.setUsername(System.getenv("OPENSHIFT_MONGODB_DB_USERNAME"));
			config.setPassword(System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD"));
		} else if (datastore.provider().equals(Provider.MONGOLAB)) {
			config.setHost(System.getenv("MONGOLAB_MONGODB_DB_HOST"));
			config.setPort(Integer.decode(System.getenv("MONGOLAB_MONGODB_DB_PORT")));
			config.setDatabase(System.getenv("MONGOLAB_MONGODB_DB_NAME"));
			config.setUsername(System.getenv("MONGOLAB_MONGODB_DB_USERNAME"));
			config.setPassword(System.getenv("MONGOLAB_MONGODB_DB_PASSWORD"));
		}
		
		/**
		 * initialize the MongoDB instance based for the chosen provider
		 */
		
		initDB(config);
	}

	/**
	 * createDocumentManager
	 */
	
	@Override
	public DocumentManager createDocumentManager() {
		return new DocumentManagerImpl(mongo, db);
	}
	
	@Override
	public void close() {
		mongo.close();
		isOpen = Boolean.FALSE;
	}
	
	@Override
	public boolean isOpen() {
		return isOpen;
	}
	
	private void initDB(DatastoreConfig config) throws MongoDBConfigurationException {

		/**
		 * configure the MongoClient
		 */
		
		try {
			mongo = new MongoClient(new ServerAddress(config.getHost(), config.getPort()));
		} catch (UnknownHostException e) {
			throw new MongoDBConfigurationException(e);
		}		
		
		/**
		 * set the ReadPreference
		 */
		
		mongo.setReadPreference(config.getReadPreference());
		
		/**
		 * log into the DB
		 */
		
		db = mongo.getDB(config.getDatabase());
		
		/**
		 * handle authentication failure
		 */
		
		if (!db.authenticate(config.getUsername(), config.getPassword().toCharArray())) {
			throw new MongoException(String.format("Failed to authenticate against db: %s", db));
		}
		
		/**
		 * set config to null to clear out credentials
		 */
		
		config = null;		
	}
}