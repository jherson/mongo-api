package com.nowellpoint.mongodb.persistence.test.model;

import java.io.Serializable;

public class Organization implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 4347536133780744954L;

	/**
	 * 
	 */
	
	private String id;
	
	/**
	 * 
	 */
	
	private String name;
	
	/**
	 * 
	 */
	
	private String division;
	
	/**
	 * 
	 */
	
	private String street;
	
	/**
	 * 
	 */
	
	private String city;
	
	/**
	 * 
	 */
	
	private String state;
	
	/**
	 * 
	 */
	
	private String postalCode;
	
	/**
	 * 
	 */
	
	private String country;
	
	/**
	 * 
	 */
	
	private String primaryContact;
	
	/**
	 * 
	 */
	
	private String defaultLocaleSidKey;
	
	/**
	 * 
	 */
	
	private String languageLocaleKey;
	
	/**
	 * 
	 */
	
	private String fiscalYearStartMonth;
	
	/**
	 * 
	 */

	private Boolean isSandbox;
	
	
	public Organization() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPrimaryContact() {
		return primaryContact;
	}

	public void setPrimaryContact(String primaryContact) {
		this.primaryContact = primaryContact;
	}

	public String getDefaultLocaleSidKey() {
		return defaultLocaleSidKey;
	}

	public void setDefaultLocaleSidKey(String defaultLocaleSidKey) {
		this.defaultLocaleSidKey = defaultLocaleSidKey;
	}

	public String getLanguageLocaleKey() {
		return languageLocaleKey;
	}

	public void setLanguageLocaleKey(String languageLocaleKey) {
		this.languageLocaleKey = languageLocaleKey;
	}

	public String getFiscalYearStartMonth() {
		return fiscalYearStartMonth;
	}

	public void setFiscalYearStartMonth(String fiscalYearStartMonth) {
		this.fiscalYearStartMonth = fiscalYearStartMonth;
	}
	
	public Boolean getIsSandbox() {
		return isSandbox;
	}
	
	public void setIsSandbox(Boolean isSandbox) {
		this.isSandbox = isSandbox;
	}
}