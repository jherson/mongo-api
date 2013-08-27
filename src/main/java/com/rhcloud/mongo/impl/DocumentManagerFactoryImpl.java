package com.rhcloud.mongo.impl;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.rhcloud.mongo.DocumentManager;
import com.rhcloud.mongo.DocumentManagerFactory;
import com.rhcloud.mongo.exception.MongoDBConfigurationException;

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
	
	private MongoClient mongo;
	
	/**
	 * 
	 */
	
	private DB db;
	
	/**
	 * 
	 */
	
	private boolean isOpen;
	
	
	public DocumentManagerFactoryImpl(String host, int port, String databaseName, String username, String password) throws MongoDBConfigurationException {
		
		/**
		 * 
		 */
		
		LOG.info("Connecting to MongoDB...(" + databaseName + "@" + host + ":" + port + ")");
		
		/**
		 * configure the MongoClient
		 */
		
		try {
			mongo = new MongoClient(new ServerAddress(host, port));
		} catch (UnknownHostException e) {
			throw new MongoDBConfigurationException(e);
		}		
		
		/**
		 * log into the DB
		 */
		
		db = mongo.getDB(databaseName);
		
		/**
		 * handle authentication failure
		 */
		
		if (!db.authenticate(username, password.toCharArray())) {
			throw new MongoException(String.format("Failed to authenticate against db: %s", db));
		}
		
		username = null;
		password = null;
		
		/**
		 * 
		 */
		
		LOG.info("Connection to MongoDB...successful");
	}
	
	/**
	 * constructor
	 * 
	 * @param injectionPoint
	 * @throws MongoDBConfigurationException 
	 */
	

	
	@Override
	public void close() {
		mongo.close();
		isOpen = Boolean.FALSE;
	}
	
	@Override
	public boolean isOpen() {
		return isOpen;
	}
	
	@Override
	public DocumentManager createDocumentManager() {
		return new DocumentManagerImpl(mongo, db);
	}
}