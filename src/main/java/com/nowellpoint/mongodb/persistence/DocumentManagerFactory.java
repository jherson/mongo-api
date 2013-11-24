package com.nowellpoint.mongodb.persistence;

import com.mongodb.DB;

public interface DocumentManagerFactory {
	
	/**
	 * createDocumentManager
	 * 
	 * @return DocumentManager
	 */
	
	DocumentManager createDocumentManager();
	
	/**
	 * 
	 * @return
	 */
	
	DB getDB();
	
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
