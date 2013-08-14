package com.rhcloud.mongo.impl;

import java.io.Serializable;
import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.rhcloud.mongo.DocumentManager;
import com.rhcloud.mongo.DocumentManagerFactory;
import com.rhcloud.mongo.MongoDBConfig;

public class DocumentManagerFactoryImpl implements DocumentManagerFactory, Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 8406720002314923220L;
	
	/**
	 * createDocumentManager
	 * 
	 * @param config
	 */

	public DocumentManager createDocumentManager(MongoDBConfig config) throws UnknownHostException {
		
		/**
		 * establish the connection to MongoDB
		 */
		
		MongoClient mongo = new MongoClient(new ServerAddress(config.getHost(), config.getPort()));		
		mongo.setReadPreference(config.getReadPreference());
		
		/**
		 * log into the DB
		 */
		
		DB db = mongo.getDB(config.getDatabase());
		
		/**
		 * handle authentication failure
		 */
		
		if (!db.authenticate(config.getUsername(), config.getPassword().toCharArray())) {
			throw new MongoException(String.format("Failed to authenticate against db: %s", db));
		}
		
		/**
		 * wrap the MongoClient and DB into the MongoDBDatastore object
		 */
		
		DocumentManager documentManager = new DocumentManagerImpl(mongo, db);
		return documentManager;
	}
}