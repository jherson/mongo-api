package com.nowellpoint.mongodb.persistence.impl;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.nowellpoint.mongodb.persistence.DocumentManager;
import com.nowellpoint.mongodb.persistence.DocumentManagerFactory;
import com.nowellpoint.mongodb.persistence.datastore.DatastoreConfig;
import com.nowellpoint.mongodb.persistence.exception.DatastoreConfigurationException;

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
		 * set the default read preference
		 */
		
		mongo.setReadPreference(ReadPreference.primaryPreferred());
		
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
		return new DocumentManagerImpl(mongo, db);
	}
	
	/**
	 * 
	 */
	
	private void authenticate(String username, String password) throws MongoException {
		if (!db.authenticate(username, password.toCharArray())) {
			throw new MongoException(String.format("Failed to authenticate against db: %s", db));
		}
	}
}