package com.rhcloud.mongo;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

public class MongoDBDatastoreFactory {
	
	public static MongoDBDatastore connect(MongoDBConfig config) throws UnknownHostException {
		
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
		
		MongoDBDatastore datastore = new MongoDBDatastoreImpl(mongo, db);
		return datastore;
	}
}
