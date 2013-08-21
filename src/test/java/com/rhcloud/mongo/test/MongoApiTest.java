package com.rhcloud.mongo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rhcloud.mongo.Datastore;
import com.rhcloud.mongo.DocumentManager;
import com.rhcloud.mongo.DatastoreConfig;
import com.rhcloud.mongo.DocumentManagerFactory;
import com.rhcloud.mongo.exception.MongoDBConfigurationException;
import com.rhcloud.mongo.spi.AnnotationScanner;
import com.rhcloud.mongo.test.model.MongoTestObject;

public class MongoApiTest {
	
	private static DocumentManagerFactory documentManagerFactory;
	private static DocumentManager documentManager;
	
	@BeforeClass
	public static void initDB() {
		final String host = "ds037468.mongolab.com";
		final String port = "37468";
		final String database = "apitest"; 
		final String username = "test";
		final String password = "test";
		
		DatastoreConfig config = new DatastoreConfig();
		config.setHost(host);
		config.setPort(Integer.decode(port));
		config.setDatabase(database);
		config.setUsername(username);
		config.setPassword(password);
		
		try {
			documentManagerFactory = Datastore.createDocumentManagerFactory(config);
			documentManager = documentManagerFactory.createDocumentManager();

		} catch (MongoDBConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testScan() {
		AnnotationScanner scanner = new AnnotationScanner();
		Set<Class<?>> annotatedClasses = scanner.startScan();
		assertEquals(annotatedClasses.size(), 1);
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
		
		MongoTestObject testObject = documentManager.createQuery(MongoTestObject.class).field("name").isEqual("Mongo Test Object").getSingleResult();
		
		assertNotNull(testObject);
		
		documentManager.delete(testObject);
		
		assertNull(documentManager.find(MongoTestObject.class, testObject.getId()));
	}
	
	@AfterClass
	public static void closeDB() {
		System.out.println("closing");
		
		documentManagerFactory.close();
	}
}