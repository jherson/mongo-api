package com.rhcloud.mongo;

import java.io.Serializable;
import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public class MongoDBDatastore implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 397801758398054504L;
	
	/**
	 * 
	 */
	
	private MongoDBDatastoreConfig config;
	
	/**
	 * 
	 */
	
	private MongoClient mongo;

	/**
	 * 
	 */
	
	private DB db;
	
	/**
	 * MongoDBDatastore
	 * @param config
	 */

	public MongoDBDatastore(MongoDBDatastoreConfig config) {
		this.config = config;				
	}	
	
	public void connect() throws UnknownHostException {
		
		/**
		 * establish the connection to MongoDB
		 */
		
		mongo = new MongoClient(new ServerAddress(config.getHost(), config.getPort()));		
		mongo.setReadPreference(ReadPreference.primaryPreferred());
		
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
	}
}