package com.rhcloud.mongo;

import java.net.UnknownHostException;

public interface DocumentManagerFactory {

	public DocumentManager createDocumentManager(MongoDBConfig config) throws UnknownHostException;
}