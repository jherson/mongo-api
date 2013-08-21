package com.rhcloud.mongo;

public interface DocumentManagerFactory {

	/**
	 * createDocumentManager
	 * 
	 * @return
	 */
	
	public DocumentManager createDocumentManager();
	
	/**
	 * isOpen
	 * 
	 * @return boolean
	 */
	
	public boolean isOpen();
	
	/**
	 * close
	 */
	
	public void close();
}