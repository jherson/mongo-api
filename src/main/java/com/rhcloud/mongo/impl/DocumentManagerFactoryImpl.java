package com.rhcloud.mongo.impl;

import java.io.Serializable;
import java.net.UnknownHostException;

import javax.enterprise.inject.Produces;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.rhcloud.mongo.DocumentManager;
import com.rhcloud.mongo.DocumentManagerFactory;
import com.rhcloud.mongo.MongoDBConfig;
import com.rhcloud.mongo.annotation.MongoDBDatastore;
import com.rhcloud.mongo.exception.MongoDBConfigurationException;

public class DocumentManagerFactoryImpl implements DocumentManagerFactory, Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 8406720002314923220L;
	
	/**
	 * 
	 */
	
	private static MongoClient mongo;
	
	/**
	 * 
	 */
	
	private static DB db;
	
	/**
	 * createDocumentManager
	 * 
	 * @return DocumentManager
	 * @exception UnknownHostException
	 * @throws  
	 */
	
	@Produces
	@MongoDBDatastore
	public DocumentManager createDocumentManager() throws MongoDBConfigurationException, UnknownHostException {
		
		/**
		 * if the db has already been initialized build DocumentManager with existing 
		 * else establish the connection 
		 */
		
		DocumentManager documentManager = null;
		
		if (db != null) {
			
			/**
			 * wrap the MongoClient and DB into the DocumentManager
			 */
			
			documentManager = new DocumentManagerImpl(mongo, db);
			
		} else {
			
			/**
			 * configure MngoDB from Openshift environment variables 
			 */
			
			MongoDBConfig config = getEnvironmentConfig();
			documentManager = createDocumentManager(config); 
		}
		
		/**
		 * return DocumentManager
		 */
		
		return documentManager;
	}
	
	/**
	 * createDocumentManager
	 * 
	 * @param config
	 * @return DocumentManager
	 * @exception UnknownHostException
	 */

	public DocumentManager createDocumentManager(MongoDBConfig config) throws UnknownHostException {
		
		/**
		 * establish the connection to MongoDB
		 */
		
		mongo = new MongoClient(new ServerAddress(config.getHost(), config.getPort()));		
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
		
		/**
		 * wrap the MongoClient and DB into the DocumentManager
		 */
		
		DocumentManager documentManager = new DocumentManagerImpl(mongo, db);
		
		/**
		 * return DocumentManager
		 */
		
		return documentManager;
	}
	
	private MongoDBConfig getEnvironmentConfig() throws MongoDBConfigurationException {
		
		/**
		 * build the MongoDBConfig
		 */
		
		MongoDBConfig config = new MongoDBConfig();
		
		/**
		 * if the openshift mongodb host is set then use openshift mongdb instance
		 * else use the mongolab instance
		 */
		
		if (System.getenv("OPENSHIFT_MONGODB_DB_HOST") != null) {
			config.setHost(System.getenv("OPENSHIFT_MONGODB_DB_HOST"));
			config.setPort(Integer.decode(System.getenv("OPENSHIFT_MONGODB_DB_PORT")));
			config.setDatabase(System.getenv("OPENSHIFT_APP_NAME"));
			config.setUsername(System.getenv("OPENSHIFT_MONGODB_DB_USERNAME"));
			config.setPassword(System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD"));
		} else if (System.getenv("MONGOLAB_MONGODB_DB_HOST") != null) {
			config.setHost(System.getenv("MONGOLAB_MONGODB_DB_HOST"));
			config.setPort(Integer.decode(System.getenv("MONGOLAB_MONGODB_DB_PORT")));
			config.setDatabase(System.getenv("MONGOLAB_MONGODB_DB_NAME"));
			config.setUsername(System.getenv("MONGOLAB_MONGODB_DB_USERNAME"));
			config.setPassword(System.getenv("MONGOLAB_MONGODB_DB_PASSWORD"));
		} else {
			throw new MongoDBConfigurationException();
		}
		
		return config;
	}
}