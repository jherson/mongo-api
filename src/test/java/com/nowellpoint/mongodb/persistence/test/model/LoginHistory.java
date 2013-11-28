package com.nowellpoint.mongodb.persistence.test.model;

import java.util.Date;

import com.nowellpoint.mongodb.persistence.BaseDocument;
import com.nowellpoint.mongodb.persistence.annotation.Collection;
import com.nowellpoint.mongodb.persistence.annotation.Document;
import com.nowellpoint.mongodb.persistence.annotation.Property;
/**
 * @author jherson
 *
 */

@Document
@Collection(name = "login.history")
public class LoginHistory extends BaseDocument {
	
	/**
	 * 
	 */

	private static final long serialVersionUID = 5782511567214678838L;
	
	/**
	 * 
	 */
	
	@Property(name = "remoteAddress")
	private String remoteAddress;
	
	/**
	 * 
	 */
	
	@Property
	private String name;
	
	/**
	 * 
	 */
	
	@Property
	private Date loginTime;
	
	/**
	 * 
	 */
	
	@Property
	private String userAgent;
	
	/**
	 * 
	 */
	
	@Property
	private String browswer;
	
	/**
	 * 
	 */
	
	@Property
	private String browserVersion;
	
	/**
	 * 
	 */
	
	@Property
	private String operatingSystem;
	

    public LoginHistory() {
    	
    }
	
	public String getRemoteAddress() {
		return this.remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Date getLoginTime() {
		return loginTime;
	}
	
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	
	public String getUserAgent() {
		return userAgent;
	}
	
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
		
	public String getBrowser() {
		return browswer;
	}
	
	public void setBrowser(String browser) {
		this.browswer = browser;
	}
	
	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}
	
	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
}
