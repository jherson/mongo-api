package com.rhcloud.mongodb.persistence;

public interface DocumentManagerFactory {
	
	/**
	 * createDocumentManager
	 * 
	 * @return DocumentManager
	 */
	
	DocumentManager createDocumentManager();
	
	/**
	 * isOpen
	 * 
	 * @return boolean
	 */
	
	boolean isOpen();
	
	/**
	 * close
	 */
	
	void close();
}
