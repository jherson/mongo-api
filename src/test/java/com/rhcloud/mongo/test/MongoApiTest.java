package com.rhcloud.mongo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.UnknownHostException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rhcloud.mongo.Datastore;
import com.rhcloud.mongo.MongoDBConfig;
import com.rhcloud.mongo.DatastoreFactory;
import com.rhcloud.mongo.test.model.MongoTestObject;

public class MongoApiTest {
	
	private static final String host = "ds037468.mongolab.com";
	private static final String port = "37468";
	private static final String database = "apitest"; 
	private static final String username = "test";
	private static final String password = "test";
	
	private static Datastore datastore;
	
	@Before
	public void initDB() {
		MongoDBConfig config = new MongoDBConfig();
		config.setHost(host);
		config.setPort(Integer.decode(port));
		config.setDatabase(database);
		config.setUsername(username);
		config.setPassword(password);
		
		try {
			datastore = DatastoreFactory.createMongoDBDatastore(config);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After
	public void closeDB() {
		datastore.close();
	}

	@Test
	public void testInsert() {		
		
		MongoTestObject testObject = new MongoTestObject();
		testObject.setName("Mongo Test Object");
		
		testObject = datastore.insert(MongoTestObject.class, testObject);
		
		System.out.println("Id: " + testObject.getId());
						
		assertNotNull(testObject.getId());
		
		testObject = datastore.find(MongoTestObject.class, testObject.getId());
		
		System.out.println("Id: " + testObject.getId());
		
		assertNotNull(testObject);
		
		List<MongoTestObject> testObjectList = datastore.createQuery(MongoTestObject.class).getResultList();
		
		assertNotNull(testObjectList);
		assertNotEquals(testObjectList.size(), 0);
		
        testObject = datastore.update(MongoTestObject.class, testObject);
		
		assertEquals(testObject.getName(), datastore.find(MongoTestObject.class, testObject.getId()).getName());
		
		//datastore.delete(MongoTestObject.class, testObject);
				
		//assertNull(datastore.find(MongoTestObject.class, testObject.getId()));
	}
}