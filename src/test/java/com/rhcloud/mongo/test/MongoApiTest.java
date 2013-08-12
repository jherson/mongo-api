package com.rhcloud.mongo.test;

import java.net.UnknownHostException;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

import com.rhcloud.mongo.dao.MongoDBDao;
import com.rhcloud.mongo.dao.impl.MongoDBDaoImpl;
import com.rhcloud.mongo.test.model.MongoTestObject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MongoApiTest {
	
	private static final String host = "ds037468.mongolab.com";
	private static final String port = "37468";
	private static final String database = "apitest"; 
	private static final String username = "test";
	private static final String password = "test";
	
	private static MongoClient mongo;
	private static DB db;
	private static MongoDBDao mongoDBDao;
	
	@Before
	public void initDB() {
		mongo = null;
		try {
			mongo = new MongoClient(new ServerAddress(host, Integer.decode(port)));
		} catch (NumberFormatException | UnknownHostException e) {
			e.printStackTrace();
		}
		
		db = mongo.getDB(database);
		
		if (!db.authenticate(username, password.toCharArray())) {
			throw new MongoException(String.format("Failed to authenticate against db: %s", db));
		}
		
		mongoDBDao = new MongoDBDaoImpl();
		mongoDBDao.setDB(db);
	}
	
	@After
	public void closeDB() {
		mongo.close();
	}

	@Test
	public void testInsert() {		
		
		MongoTestObject testObject = new MongoTestObject();
		testObject.setName("Mongo Test Object");
		
		testObject = mongoDBDao.insert(MongoTestObject.class, testObject);
		
		System.out.println("Id: " + testObject.getId());
						
		assertNotNull(testObject.getId());
		
		List<MongoTestObject> testObjectList = mongoDBDao.createQuery(MongoTestObject.class).getResultList();
		
		assertNotEquals(testObjectList.size(), 0);
		
        testObject = mongoDBDao.update(MongoTestObject.class, testObject);
		
		assertEquals(testObject.getName(), findById(testObject.getId()).getName());
		
        mongoDBDao.delete(MongoTestObject.class, testObject);
				
		assertNull(findById(testObject.getId()));
	}
	
	private MongoTestObject findById(ObjectId id) {
		return mongoDBDao.find(MongoTestObject.class, id);
	}
}