package com.rhcloud.mongo.impl;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
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
	
	/**
	 * 
	 * @param host
	 * @param port
	 * @param databaseName
	 * @param username
	 * @param password
	 * @throws MongoDBConfigurationException
	 */
	
	public DocumentManagerFactoryImpl(String host, int port, String databaseName, String username, char[] password) throws MongoDBConfigurationException {
		
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
		 * set the default read preference
		 */
		
		mongo.setReadPreference(ReadPreference.primaryPreferred());
		
		/**
		 * log into the DB
		 */
		
		db = mongo.getDB(databaseName);
		
		/**
		 * handle authentication failure
		 */
		
		if (!db.authenticate(username, password)) {
			throw new MongoException(String.format("Failed to authenticate against db: %s", db));
		}
		
		/**
		 * clear password
		 */
		
		Arrays.fill(password, '*');
		
		/**
		 * 
		 */
		
		LOG.info("Connection to MongoDB...successful");
	}
	
	/**
	 * 
	 */
	
	@Override
	public void close() {
		mongo.close();
		isOpen = Boolean.FALSE;
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
		return new DocumentManagerImpl(mongo, db);
	}
}