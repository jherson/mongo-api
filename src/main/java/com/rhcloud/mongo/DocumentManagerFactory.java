package com.rhcloud.mongo;

public interface DocumentManagerFactory {

	/**
	 * createDocumentManager
	 * 
	 * @return
	 */
	
	public DocumentManager createDocumentManager();
	
	/**
	 * close
	 */
	
	public void close();
}