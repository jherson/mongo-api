package com.rhcloud.mongo.db;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.rhcloud.mongo.DocumentManagerFactory;
import com.rhcloud.mongo.exception.MongoDBConfigurationException;
import com.rhcloud.mongo.impl.DocumentManagerFactoryImpl;
import com.rhcloud.mongo.qualifier.MongoDBDatastore;

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
	 * 
	 */
	
	private Datastore() {
		
	}

	/**	 
	 * @return DocumentManagerFactory
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory() throws MongoDBConfigurationException {
		return createDocumentManagerFactory(Datastore.class.getClass().getResource("/META-INF/mongodb.cfg.xml").getFile(), "openshift");
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(String name) throws MongoDBConfigurationException {
		return createDocumentManagerFactory(Datastore.class.getClass().getResource("/META-INF/mongodb.cfg.xml").getFile(), name);
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
		config.setHost(properties.getProperty("mongodb.db.host"));
		config.setPort(Integer.decode(properties.getProperty("mongodb.db.port")));
		config.setDatabase(properties.getProperty("mongodb.db.name"));
		config.setUsername(properties.getProperty("mongodb.db.username"));
		config.setPassword(properties.getProperty("mongodb.db.password"));			
		
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
	 * @param name
	 * @return
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(String file, String name) throws MongoDBConfigurationException {
		
		if (documentManagerFactory != null && documentManagerFactory.isOpen()) {
			return documentManagerFactory;
		}
		
		return createDocumentManagerFactory(new File(file), name);
		
	}
	
	/**
	 * 
	 * @param file
	 * @param name
	 * @return
	 * @throws MongoDBConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(File xmlFile, String name) throws MongoDBConfigurationException {
		Properties properties = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			NodeList nodeList = document.getElementsByTagName("datastore");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					if (element.getAttribute("name").equals(name)) {
						properties = parseProperties(element.getElementsByTagName("properties"));
					}
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOG.severe(e.getMessage());
			throw new MongoDBConfigurationException(e);
		} 
		
		return createDocumentManagerFactory(properties);
	}
	
	//@Produces
	public static DocumentManagerFactory createDocumentManagerFactory(InjectionPoint injectionPoint) throws MongoDBConfigurationException {
		
		if (documentManagerFactory != null && documentManagerFactory.isOpen()) {
			return documentManagerFactory;
		}
		
		/**
		 * get the MongoDBDatastore annotation from the injected class
		 */
		
		MongoDBDatastore datastore = injectionPoint.getAnnotated().getAnnotation(MongoDBDatastore.class);
		
		/**
		 * load configuration from the mongodb.cfg.xml file from the META-INF folder
		 */
		
		LOG.info("searching WEB-INF for configuration file: mongodb.cfg.xml");
		
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();		
		String path = servletContext.getRealPath("/WEB-INF/classes");
		
		File configFile = new File(path + "/mongodb.cfg.xml");
		
		if (! configFile.exists()) {
			LOG.severe("unable to find the configuration file");
			throw new MongoDBConfigurationException();
		}		
		
		/**
		 * create the DocumentManagerFactory based on the default config
		 */
		
		return createDocumentManagerFactory(configFile, datastore.name());
	}
	
	/**
	 * 
	 * @param nodeList
	 * @return
	 */
	
	private static Properties parseProperties(NodeList nodeList) {
		Properties properties = new Properties();
		for (int i = 0; i < nodeList.item(0).getChildNodes().getLength(); i++) {
			Node node = nodeList.item(0).getChildNodes().item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element property = (Element) node;
				properties.put(property.getAttribute("name"), parsePropertyValue(property.getAttribute("value")));
			}
		}
		return properties;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	
	private static String parsePropertyValue(String value) {
		if (value.startsWith("${env.")) {
        	value = System.getenv(value.replace("${env.", "").replace('}', ' ').trim());
        } else if (value.startsWith("${property.")) {
        	value = System.getProperty(value.replace("${property.", "").replace('}', ' ').trim());
        }
		return value;
	}
}