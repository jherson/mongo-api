package com.rhcloud.mongo.impl;

import java.io.Serializable;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.rhcloud.mongo.DocumentManager;
import com.rhcloud.mongo.DocumentManagerFactory;

public class DocumentManagerFactoryImpl implements DocumentManagerFactory, Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 8406720002314923220L;
	
	/**
	 * 
	 */
	
	private MongoClient mongo;
	
	/**
	 * 
	 */
	
	private DB db;
	
	
	/**
	 * constructor
	 * 
	 * @param mongo
	 * @param db
	 */
	
	public DocumentManagerFactoryImpl(MongoClient mongo, DB db) {
		this.mongo = mongo;
		this.db = db;
	}


	/**
	 * createDocumentManager
	 */
	
	@Override
	public DocumentManager createDocumentManager() {
		return new DocumentManagerImpl(mongo, db);
	}
	
	@Override
	public void close() {
		mongo.close();
	}
}