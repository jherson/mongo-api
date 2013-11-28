package com.nowellpoint.mongodb.persistence.test.model;

import java.io.Serializable;

import com.nowellpoint.mongodb.persistence.annotation.Collection;
import com.nowellpoint.mongodb.persistence.annotation.Document;

@Document
@Collection(name = "users")
public class User implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -8608331954991392574L;

	public User() {
		
	}
}