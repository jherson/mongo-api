package com.rhcloud.mongo;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

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
		return null;
	}
	
	/**
	 * createDocumentManagerFactory
	 * 
	 * @param properties
	 * @return DocumentManagerFactory
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(Properties properties) throws MongoDBConfigurationException {
		
		log.info("createDocumentManagerFactory: Properties");
		
		if (documentManagerFactory == null || ! documentManagerFactory.isOpen()) {			
			documentManagerFactory = new DocumentManagerFactoryImpl(properties);
		}
		
		return documentManagerFactory;
	}
	
	/**
	 * createDocumentManagerFactory
	 * 
	 * @param config
	 * @return DocumentManagerFactory
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(DatastoreConfig config) throws MongoDBConfigurationException {
		
		log.info("createDocumentManagerFactory: DatastoreConfig");
		
		if (documentManagerFactory == null || ! documentManagerFactory.isOpen()) {			
			documentManagerFactory = new DocumentManagerFactoryImpl(config);
		}
		
		return documentManagerFactory;
	}
}