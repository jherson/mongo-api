package com.nowellpoint.mongodb.persistence.test.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nowellpoint.mongodb.persistence.annotation.Document;
import com.nowellpoint.mongodb.persistence.annotation.Collection;
import com.nowellpoint.mongodb.persistence.annotation.EmbedMany;
import com.nowellpoint.mongodb.persistence.annotation.Property;
import com.nowellpoint.mongodb.persistence.annotation.Id;
import com.nowellpoint.mongodb.persistence.annotation.Index;

@Document
@Collection
@Index(name="testindex", key = "name")
public class TestObject implements Serializable {
		
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -1913878606286172065L;
	
	@Id
	private Long id;
	
	@Property
	private String name;
	
	@Property
	private Date creationDate;
	
	@EmbedMany(name = "listoftestobjects")
	private List<TestListObject> testLists;
	
	public TestObject() {
		super();
		creationDate = new Date();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<TestListObject> getTestLists() {
		return testLists;
	}

	public void setTestLists(List<TestListObject> testLists) {
		this.testLists = testLists;
	}
}