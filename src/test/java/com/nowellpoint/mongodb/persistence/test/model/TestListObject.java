package com.nowellpoint.mongodb.persistence.test.model;

import com.nowellpoint.mongodb.persistence.annotation.EmbeddedDocument;
import com.nowellpoint.mongodb.persistence.annotation.Property;

@EmbeddedDocument
public class TestListObject {

	@Property
	private String listName;
	
	@Property
	private Integer count;
	
    @Property
    private Double amount;
    
    public TestListObject() {
    	
    }
	
	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}