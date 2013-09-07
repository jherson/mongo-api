package com.rhcloud.mongo.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.rhcloud.mongo.DocumentManagerFactory;
import com.rhcloud.mongo.exception.DatastoreConfigurationException;
import com.rhcloud.mongo.impl.DocumentManagerFactoryImpl;

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
	 * @throws DatastoreConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory() throws DatastoreConfigurationException {
		return createDocumentManagerFactory(Datastore.class.getClass().getResourceAsStream("/META-INF/mongodb.cfg.xml"), "mongolab");
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws DatastoreConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(String name) throws DatastoreConfigurationException {
		LOG.info("loading config file for datasource name: " + name);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream configFile = loader.getResourceAsStream("/mongodb.cfg.xml");
		if (configFile == null) {
			LOG.info("no config file found");
			throw new DatastoreConfigurationException("no config file found");
		}
		return createDocumentManagerFactory(configFile, name);
	}
	
	/**
	 * @param properties
	 * @return DocumentManagerFactory
	 * @throws DatastoreConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(Properties properties) throws DatastoreConfigurationException {
		
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
	 * @throws DatastoreConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(DatastoreConfig config) throws DatastoreConfigurationException {
		
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
	 * @throws DatastoreConfigurationException
	 */
	
	public static DocumentManagerFactory createDocumentManagerFactory(InputStream xmlFile, String name) throws DatastoreConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Properties properties = null;
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
			throw new DatastoreConfigurationException(e);
		} 
		
		return createDocumentManagerFactory(properties);
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