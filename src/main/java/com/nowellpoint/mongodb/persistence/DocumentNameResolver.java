package com.nowellpoint.mongodb.persistence;

public interface DocumentNameResolver {

	public <T> String resolveDocumentName(Class<T> clazz);	
}