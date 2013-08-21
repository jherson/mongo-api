package com.rhcloud.mongo;

import java.io.Serializable;

import com.mongodb.ReadPreference;

public class DatastoreConfig implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 447030075733870658L;
	
	/**
	 * 
	 */
	
	private String host;
	
	/**
	 * 
	 */
	
	private String database;
	
	/**
	 * 
	 */
	
	private int port;
	
	/**
	 * 
	 */
	
	private String username;
	
	/**
	 * 
	 */
	
	private String password;
	
	/**
	 * 
	 */
	
	private ReadPreference readPreference;
	
	
	/**
	 * constructor
	 */
	
	public DatastoreConfig() {
		setReadPreference(ReadPreference.primaryPreferred());
	}
	
	/**
	 * getHostname
	 * 
	 * @return hostname
	 */
	
	public String getHost() {
		return host;
	}
	
	/**
	 * setHostname
	 * 
	 * @param hostname
	 */
	
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * getDatabase
	 * 
	 * @return database
	 */
	
	public String getDatabase() {
		return database;
	}
	
	/**
	 * setDatabase
	 * 
	 * @param database
	 */
	
	public void setDatabase(String database) {
		this.database = database;
	}
	
	/**
	 * getPort
	 * @return port
	 */
	
	public int getPort() {
		return port;
	}
	
	/**
	 * setPort
	 * 
	 * @param port
	 */
	
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * getUsername
	 * 
	 * @return username
	 */
	
	public String getUsername() {
		return username;
	}
	
	/**
	 * setUsername
	 * 
	 * @param username
	 */
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * getPassword
	 * 
	 * @return password
	 */
	
	public String getPassword() {
		return password;
	}
	
	/**
	 * setPassword
	 * 
	 * @param password
	 */
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * getReadPreference
	 * 
	 * @return ReadPreference
	 */
	
	public ReadPreference getReadPreference() {
		return readPreference;
	}
	
	/**
	 * setReadPreference
	 * 
	 * @param readPreference
	 */
	
	public void setReadPreference(ReadPreference readPreference) {
		this.readPreference = readPreference;
	}
}