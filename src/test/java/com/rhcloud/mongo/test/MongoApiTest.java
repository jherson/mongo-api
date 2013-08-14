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

import com.rhcloud.mongo.DocumentManager;
import com.rhcloud.mongo.MongoDBConfig;
import com.rhcloud.mongo.impl.DocumentManagerFactoryImpl;
import com.rhcloud.mongo.test.model.MongoTestObject;

public class MongoApiTest {
	
	private static DocumentManager documentManager;
	
	@Before
	public void initDB() {
		final String host = "ds037468.mongolab.com";
		final String port = "37468";
		final String database = "apitest"; 
		final String username = "test";
		final String password = "test";
		
		MongoDBConfig config = new MongoDBConfig();
		config.setHost(host);
		config.setPort(Integer.decode(port));
		config.setDatabase(database);
		config.setUsername(username);
		config.setPassword(password);
		
		try {
			documentManager = new DocumentManagerFactoryImpl().createDocumentManager(config);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After
	public void closeDB() {
		documentManager.close();
	}

	@Test
	public void testInsert() {		
		
		MongoTestObject testObject = new MongoTestObject();
		testObject.setName("Mongo Test Object");
		
		testObject = documentManager.insert(MongoTestObject.class, testObject);
						
		assertNotNull(testObject.getId());
		
		testObject = documentManager.find(MongoTestObject.class, testObject.getId());
		
		assertNotNull(testObject);
		
		List<MongoTestObject> testObjectList = documentManager.createQuery(MongoTestObject.class).getResultList();
		
		assertNotNull(testObjectList);
		assertNotEquals(testObjectList.size(), 0);
		
        testObject = documentManager.update(MongoTestObject.class, testObject);
		
		assertEquals(testObject.getName(), documentManager.find(MongoTestObject.class, testObject.getId()).getName());				
	}
	
	@Test
	public void testDelete() {
		MongoTestObject testObject = documentManager.createQuery(MongoTestObject.class).put("name").isEqual("Mongo Test Object").getSingleResult();
		
		assertNotNull(testObject);
		
		documentManager.delete(testObject);
		
		assertNull(documentManager.find(MongoTestObject.class, testObject.getId()));
	}
}