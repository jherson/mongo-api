package com.rhcloud.mongo;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.rhcloud.mongo.exception.MongoDBConfigurationException;
import com.rhcloud.mongo.impl.DocumentManagerFactoryImpl;

public class Datastore implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 3471338533194732229L;
	
	/**
	 * 
	 */
	
	private static final Logger log = Logger.getLogger(Datastore.class.getName());
	
	/**
	 * 
	 */
	
	private static DocumentManagerFactory documentManagerFactory;

	/**
	 * createDocumentManagerFactory
	 * 
	 * @return DocumentManagerFactory
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory() throws MongoDBConfigurationException {
		return createDocumentManagerFactory(getEnvironmentConfig());
	}
	
	/**
	 * createDocumentManagerFactory
	 * 
	 * @param config
	 * @return DocumentManagerFactory
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(DatastoreConfig config) throws MongoDBConfigurationException {
		
         if (documentManagerFactory == null || ! documentManagerFactory.isOpen()) {
			
			log.info("connecting to MongoDB (" + config.getDatabase() +  "@" + config.getHost() + ":" + config.getPort() + ")");
			
			MongoClient mongo = null;
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
			
			DB db = mongo.getDB(config.getDatabase());
			
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
			 * create the DocumentManagerFactory with the mongo client and DB
			 */
			
			documentManagerFactory = new DocumentManagerFactoryImpl(mongo, db);
		}
		
		return documentManagerFactory;
	}
	
	private static DatastoreConfig getEnvironmentConfig() throws MongoDBConfigurationException {
		
		/**
		 * build the MongoDBConfig
		 */
		
		DatastoreConfig config = new DatastoreConfig();
		
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