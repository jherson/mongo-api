package com.rhcloud.mongo.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.rhcloud.mongo.DocumentManagerFactory;
import com.rhcloud.mongo.exception.MongoDBConfigurationException;
import com.rhcloud.mongo.impl.DocumentManagerFactoryImpl;
import com.rhcloud.mongo.qualifier.MongoDBDatastore;
import com.rhcloud.mongo.qualifier.Provider;

public class Datastore implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 3471338533194732229L;
	
	/**
	 * 
	 */
	
	private static final Logger LOG = Logger.getLogger(Datastore.class.getName());
	
	/**
	 * 
	 */
	
	private static DocumentManagerFactory documentManagerFactory;

	/**	 
	 * @return DocumentManagerFactory
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory() throws MongoDBConfigurationException {
		return createDocumentManagerFactory(Datastore.class.getClass().getResource("/META-INF/mongodb-config.xml").getFile());
	}
	
	/**
	 * @param properties
	 * @return DocumentManagerFactory
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(Properties properties) throws MongoDBConfigurationException {
		
		if (documentManagerFactory != null && documentManagerFactory.isOpen()) {
			return documentManagerFactory;
		}
					
		DatastoreConfig config = new DatastoreConfig();
		config.setHost(System.getenv("mongodb.db.host"));
		config.setPort(Integer.decode(System.getenv("mongodb.db.port")));
		config.setDatabase(System.getenv("mongodb.db.name"));
		config.setUsername(System.getenv("mongodb.db.username"));
		config.setPassword(System.getenv("mongodb.db.password"));			
		
		return createDocumentManagerFactory(config);
	}
	
	/**
	 * @param config
	 * @return DocumentManagerFactory
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(DatastoreConfig config) throws MongoDBConfigurationException {
		
		if (documentManagerFactory != null && documentManagerFactory.isOpen()) {
			return documentManagerFactory;
		}
	
		return new DocumentManagerFactoryImpl(config);
	}
	
	/**
	 * 
	 * @param file
	 * @return DocumentManagerFactory
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(String file) throws MongoDBConfigurationException {
		
		if (documentManagerFactory != null && documentManagerFactory.isOpen()) {
			return documentManagerFactory;
		}
		
		DatastoreConfig config = null;
		try {
			JAXBContext context = JAXBContext.newInstance(DatastoreConfig.class);
			Unmarshaller u = context.createUnmarshaller();
			config = (DatastoreConfig) u.unmarshal(new FileInputStream(file));
		} catch (JAXBException e) {
			LOG.severe(e.getMessage());
			throw new MongoDBConfigurationException(e);
		} catch (FileNotFoundException e) {
			LOG.severe(e.getMessage());
			throw new MongoDBConfigurationException(e);
		} 
		
		return createDocumentManagerFactory(config);
	}
	
	@Produces
	@ApplicationScoped
	public DocumentManagerFactory createDocumentManagerFactory(InjectionPoint injectionPoint) throws MongoDBConfigurationException {
		
		if (documentManagerFactory != null && documentManagerFactory.isOpen()) {
			return documentManagerFactory;
		}
		
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
		} else {
			LOG.severe("no configuration available");
		}
		
		/**
		 * initialize the MongoDB instance based on the chosen provider
		 */
		
		return createDocumentManagerFactory(config);
	}
}
