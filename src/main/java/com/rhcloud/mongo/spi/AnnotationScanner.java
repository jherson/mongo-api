package com.rhcloud.mongo.spi;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.bson.types.ObjectId;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.rhcloud.mongo.annotation.Document;
import com.rhcloud.mongo.annotation.Id;

public class AnnotationScanner {
	
	/**
	 * 
	 */
	
	private static final Logger LOG = Logger.getLogger(AnnotationScanner.class.getName());
	
	/**
	 * startScan
	 */

	public void startScan() {
		
		/**
		 * 
		 */
		
		new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath())
				.filterInputsBy(new FilterBuilder().excludePackage("java").excludePackage("javax"))
				.setScanners(new TypeAnnotationsScanner(), new MethodAnnotationsScanner(), new TypeElementsScanner())).save("src/test/resources/META-INF/reflections/document-reflections.xml");

		
		/**
		 * 
		 */

		//scanForDocument();

	}
	
	public void startScan(ServletContext servletContext) {
		
		/**
		 * 
		 */
		
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forWebInfLib(servletContext))
				.setUrls(ClasspathHelper.forWebInfClasses(servletContext))
				.filterInputsBy(new FilterBuilder().excludePackage("java").excludePackage("javax"))
				.setScanners(new TypeAnnotationsScanner(), new MethodAnnotationsScanner(), new TypeElementsScanner()));
		
		/**
		 * 
		 */
		
		//scanForDocument(reflections);
		
	}
	
	private void scanForDocument() {
		
		Reflections reflections = Reflections.collect();

		/**
		 * 
		 */
		
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Document.class);
        LOG.info("MongoDB @Document annotations: " + annotated.size());
        
	}
}
