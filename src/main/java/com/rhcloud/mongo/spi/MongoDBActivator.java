package com.rhcloud.mongo.spi;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.rhcloud.mongo.Datastore;
import com.rhcloud.mongo.exception.MongoDBConfigurationException;

@WebListener
public class MongoDBActivator implements ServletContextListener  {
	
	/**
	 * 
	 */
	
	private static final Logger log = Logger.getLogger(MongoDBActivator.class.getName());
	
	/**
	 * 
	 */

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		log.info("Starting MongoDB...Scanning for Documents");
		AnnotationScanner scanner = new AnnotationScanner();
		scanner.startScan(contextEvent.getServletContext());
		
		try {
			Datastore.createDocumentManagerFactory();

		} catch (MongoDBConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		log.info("Stopping MongoDB...");	
	}
}