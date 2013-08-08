package com.rhcloud.mongo;

import java.util.List;

public interface Query {

	public Query setCollectionName(String collectionName);
	public String getCollectionName();
	public Query put(String key);
	public Query is(Object value);
	public <T> T getSingleResult(Class<T> clazz);
	public <T> List<T> getResultList(Class<T> clazz);
}