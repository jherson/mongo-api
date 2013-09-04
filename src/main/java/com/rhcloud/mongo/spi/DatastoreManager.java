package com.rhcloud.mongo.spi;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.rhcloud.mongo.DocumentManagerFactory;
import com.rhcloud.mongo.db.Datastore;
import com.rhcloud.mongo.exception.MongoDBConfigurationException;
import com.rhcloud.mongo.qualifier.MongoDBDatastore;

@ApplicationScoped
public class DatastoreManager implements Serializable {

	/**
	 * 
	 */
	
    private static final long serialVersionUID = -8960046845443427918L;
    
    /**
     * 
     */
    
    private static final Logger LOG = Logger.getLogger(DatastoreManager.class.getName());
    
    /**
     * 
     */
    
    private static Map<String, DocumentManagerFactory> documentManagerFactoryMap = new ConcurrentHashMap<String, DocumentManagerFactory>();
    
    @PostConstruct
    public void postConstruct() {
    	
    }
    
    public void onStartup(ServletContext context) {
    	LOG.info("searching WEB-INF for configuration file: mongodb.cfg.xml");
		
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();		
		String path = servletContext.getRealPath("/WEB-INF/classes");
		
		File configFile = new File(path + "/mongodb.cfg.xml");
		
		if (! configFile.exists()) {
			LOG.severe("unable to find the configuration file");
		}		
		
		try {
			documentManagerFactoryMap.put("mongolab", Datastore.createDocumentManagerFactory(configFile, "mongolab"));
        } catch (MongoDBConfigurationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
    }

    public void onShutdown(ServletContext context) {
    	
    }
    
    @Produces
    public DocumentManagerFactory getDocumentManagerFactory(InjectionPoint injectionPoint) {
    	
    	/**
		 * get the MongoDBDatastore annotation from the injected class
		 */
		
		MongoDBDatastore datastore = injectionPoint.getAnnotated().getAnnotation(MongoDBDatastore.class);
		
		return documentManagerFactoryMap.get(datastore.name());
    	
    }
}