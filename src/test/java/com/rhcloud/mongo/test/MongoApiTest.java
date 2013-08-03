package com.rhcloud.mongo.test;

import java.net.UnknownHostException;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

import com.rhcloud.mongo.dao.MongoDBDao;
import com.rhcloud.mongo.dao.impl.MongoDBDaoImpl;
import com.rhcloud.mongo.test.model.MongoTestObject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

public class MongoApiTest {

	private static final String host = "ds037468.mongolab.com";
	private static final String port = "37468";
	private static final String database = "apitest"; 
	private static final String username = "test";
	private static final String password = "test";
	

	@Test
	public void testInsert() {
		MongoClient mongo = null;
		try {
			mongo = new MongoClient(new ServerAddress(host, Integer.decode(port)));
		} catch (NumberFormatException | UnknownHostException e) {
			e.printStackTrace();
		}
		
		DB db = mongo.getDB(database);
		
		if (!db.authenticate(username, password.toCharArray())) {
			throw new MongoException(String.format("Failed to authenticate against db: %s", db));
		}
		
		MongoDBDao mongoDBDao = new MongoDBDaoImpl();
		mongoDBDao.setDB(db);
		
		MongoTestObject testObject = new MongoTestObject();
		testObject.setName("Mongo Test Object");
		
		testObject = mongoDBDao.insert("TestObjects", testObject, MongoTestObject.class);
		
		System.out.println("Id: " + testObject.getId());
					
		assertNotNull(testObject.getId());
		
		testObject.setName("Name change");
		testObject = mongoDBDao.update("TestObjects", testObject, MongoTestObject.class);
		
		
		
		//mongoDBDao.remove("TestObjects", testObject, MongoTestObject.class);
		
		//DBObject dbObject = mongoDBDao.findOne("TestObjects", new BasicDBObject("_id", testObject.getId()));
		
		//assertNull(dbObject);
	}
}