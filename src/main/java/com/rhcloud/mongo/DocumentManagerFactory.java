package com.rhcloud.mongo;

import java.net.UnknownHostException;

import com.rhcloud.mongo.exception.MongoDBConfigurationException;

public interface DocumentManagerFactory {

	public DocumentManager createDocumentManager() throws MongoDBConfigurationException, UnknownHostException;
	public DocumentManager createDocumentManager(MongoDBConfig config) throws UnknownHostException;
}