package com.rhcloud.mongo.spi;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContextLifecycleNotifier implements ServletContextListener {
	
	/**
	 * 
	 */
	
	private static final Logger LOG = Logger.getLogger(ServletContextLifecycleNotifier.class.getName());
	
	@Inject
	private DatastoreManager datastoreManager;
	
	@Override
    public void contextInitialized(ServletContextEvent event) {
	    LOG.info("contextInitialized");
	    datastoreManager.onStartup(event.getServletContext());
    }

	@Override
    public void contextDestroyed(ServletContextEvent event) {
		 LOG.info("contextDestroyed");	    
    }
}