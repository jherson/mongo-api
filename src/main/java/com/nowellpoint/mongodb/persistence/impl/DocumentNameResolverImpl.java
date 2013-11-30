package com.nowellpoint.mongodb.persistence.impl;

import java.io.Serializable;

import com.nowellpoint.mongodb.persistence.DocumentNameResolver;
import com.nowellpoint.mongodb.persistence.annotation.Collection;
import com.nowellpoint.mongodb.persistence.annotation.Document;

public class DocumentNameResolverImpl implements DocumentNameResolver, Serializable {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -1901363012010847413L;
	
	/**
	 * 
	 */
	
	public static final DocumentNameResolver INSTANCE = new DocumentNameResolverImpl(); 
	
	/**
	 * 
	 */
	
	protected DocumentNameResolverImpl() {
		
	}

	@Override
	public <T> String resolveDocumentName(Class<T> clazz) {
		if (! clazz.isAnnotationPresent(Document.class)) {
			throw new IllegalArgumentException("Class must be annotated with the Document annotation");
		}
		
		if (! clazz.isAnnotationPresent(Collection.class)) {
			throw new IllegalArgumentException("Class must be annotated with the Collection annotation");
		}
		
		String collectionName = null; 
				
		Collection collection = (Collection) clazz.getAnnotation(Collection.class);
		if (collection.name().trim().length() > 0) {
			collectionName = collection.name();
		} else {
			collectionName = clazz.getSimpleName();
		}
		return collectionName;
	}
	
	public DocumentNameResolver getInstance() {
		return INSTANCE;
	}
}