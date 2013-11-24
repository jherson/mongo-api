package com.nowellpoint.mongodb.persistence.test.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nowellpoint.mongodb.persistence.BaseDocument;
import com.nowellpoint.mongodb.persistence.annotation.Document;
import com.nowellpoint.mongodb.persistence.annotation.Collection;
import com.nowellpoint.mongodb.persistence.annotation.EmbedMany;
import com.nowellpoint.mongodb.persistence.annotation.PostPersist;
import com.nowellpoint.mongodb.persistence.annotation.PrePersist;
import com.nowellpoint.mongodb.persistence.annotation.Property;
import com.nowellpoint.mongodb.persistence.annotation.Index;

@Document
@Collection
@Index(name="testindex", key = "name")
public class TestObject extends BaseDocument implements Serializable {
		
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -1913878606286172065L;
	
	@Property
	private String name;
	
	@Property
	private Integer number;
	
	@Property
	private Date creationDate;
	
	@EmbedMany(name = "listoftestobjects")
	private List<TestListObject> testLists;
	
	public TestObject() {
		super();
		creationDate = new Date();
	}
	
	@PrePersist
	public void prePersist() {
		System.out.println("prePersist");
	}
	
	@PostPersist
	public void postPersist() {
		System.out.println("postPersist");
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getNumber() {
		return number;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
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