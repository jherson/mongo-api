package com.rhcloud.mongo.spi;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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
		
		/**
		 * initizlize the AnnotationScanner
		 */
		
		AnnotationScanner scanner = new AnnotationScanner();			
		scanner.startScan(contextEvent.getServletContext());
		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		
	}
}