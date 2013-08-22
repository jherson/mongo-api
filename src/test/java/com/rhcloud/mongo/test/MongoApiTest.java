package com.rhcloud.mongo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
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
	private static MongoTestObject testObject;
	
	@BeforeClass
	public static void initDB() {
		
		final String host = System.getenv("MONGOLAB_MONGODB_DB_HOST");
		final String port = System.getenv("MONGOLAB_MONGODB_DB_PORT");
		final String database = System.getenv("MONGOLAB_MONGODB_DB_NAME"); 
		final String username = System.getenv("MONGOLAB_MONGODB_DB_USERNAME");
		final String password = System.getenv("MONGOLAB_MONGODB_DB_PASSWORD");
		
		AnnotationScanner scanner = new AnnotationScanner();
		scanner.startScan();
		
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
	
	@Before
	public void before() {
		testObject = new MongoTestObject();
		testObject.setName("Mongo Test Object");
	}
	
	@After
	public void after() {
		documentManager.deleteAll(MongoTestObject.class);
	}

	@Test
	public void testInsert() {		
		
		testObject = documentManager.insert(MongoTestObject.class, testObject);
						
		assertNotNull(testObject.getId());		
	}
	
	@Test
	public void testQuery() {
		
		testObject = documentManager.insert(MongoTestObject.class, testObject);
		
		assertNotNull(testObject.getId());		
		
        testObject = documentManager.find(MongoTestObject.class, testObject.getId());
		
		assertNotNull(testObject);
		
		List<MongoTestObject> testObjectList = documentManager.createQuery(MongoTestObject.class).getResultList();
		
		assertNotNull(testObjectList);
		assertNotEquals(testObjectList.size(), 0);
	}
	
	@Test
	public void testUpdate() {

		testObject = documentManager.insert(MongoTestObject.class, testObject);
		
		assertNotNull(testObject.getId());
		
		testObject.setName("Updated");
		
        testObject = documentManager.update(MongoTestObject.class, testObject);
		
		assertEquals(testObject.getName(), documentManager.find(MongoTestObject.class, testObject.getId()).getName());				
	}
	
	@Test
	public void testDelete() {
		
		testObject = documentManager.insert(MongoTestObject.class, testObject);
		
		assertNotNull(testObject.getId());		
		
		testObject = documentManager.createQuery(MongoTestObject.class).field("name").isEqual("Mongo Test Object").getSingleResult();
		
		assertNotNull(testObject);
		
		documentManager.delete(testObject);
		
		assertNull(documentManager.find(MongoTestObject.class, testObject.getId()));
	}
	
	@AfterClass
	public static void closeDB() {		
		documentManagerFactory.close();
	}
}