package com.rhcloud.mongodb.persistence.spi;

import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class AnnotationScanner {
	
	/**
	 * 
	 */
	
	private static final Logger LOG = Logger.getLogger(AnnotationScanner.class.getName());
	
	
	
	public void startScan(ServletContext servletContext) {
		
		/**
		 * 
		 */
		
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forWebInfLib(servletContext))
				.setUrls(ClasspathHelper.forWebInfClasses(servletContext))
				.filterInputsBy(new FilterBuilder().excludePackage("java").excludePackage("javax"))
				.setScanners(new TypeAnnotationsScanner(), new MethodAnnotationsScanner(), new TypeElementsScanner()));
		
	}
}