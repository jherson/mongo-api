package com.nowellpoint.mongodb.persistence.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nowellpoint.mongodb.persistence.DocumentManager;
import com.nowellpoint.mongodb.persistence.DocumentManagerFactory;
import com.nowellpoint.mongodb.persistence.datastore.Datastore;
import com.nowellpoint.mongodb.persistence.exception.DatastoreConfigurationException;
import com.nowellpoint.mongodb.persistence.test.model.MongoTestObject;
import com.nowellpoint.mongodb.persistence.test.model.TestListObject;
import com.nowellpoint.mongodb.persistence.test.model.TestObject;

public class MongoApiTest {
	
	private static DocumentManagerFactory documentManagerFactory;
	private static DocumentManager documentManager;
	private static MongoTestObject testObject;
	
	@BeforeClass
	public static void initDB() {
			
		try {			
			documentManagerFactory = Datastore.createDocumentManagerFactory();
			documentManager = documentManagerFactory.createDocumentManager();
		} catch (DatastoreConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void before() {
		Assume.assumeTrue(documentManagerFactory.isOpen());
		testObject = new MongoTestObject();
		testObject.setName("Mongo Test Object");
	}
	
	@After
	public void after() {
		Assume.assumeTrue(documentManagerFactory.isOpen());
		//documentManager.deleteAll(MongoTestObject.class);
	}
	
	@Test
	public void testCreateCollection() {
		Assume.assumeTrue(documentManagerFactory.isOpen());
		
		TestObject test = new TestObject();
		test.setName("testing collection name from class name");
		test.setNumber(new Integer(1000));
		
		List<TestListObject> testList = new ArrayList<TestListObject>();
		
		TestListObject testList1 = new TestListObject();
		testList1.setAmount(1000.00);
		testList1.setCount(2);
		testList1.setListName("Test List 1");
		
		testList.add(testList1);
		
		TestListObject testList2 = new TestListObject();
		testList2.setAmount(3000.00);
		testList2.setCount(30);
		testList2.setListName("Test List 2");
		
		testList.add(testList2);
		
		test.setTestLists(testList);
		
		test = documentManager.insert(TestObject.class, test);
		
		test = documentManager.find(TestObject.class, test.getId());
		
		assertNotNull(test);		
		assertNotNull(test.getName());
		assertNotNull(test.getCreationDate());
		
		List<TestObject> testObjects = documentManager.createQuery(TestObject.class).field("number").isEqual(new Integer(1000)).getResultList();
		
		assertEquals(testObjects.size(), 1);
		
		//documentManager.delete(test);
		
		//test = documentManager.find(TestObject.class, test.getId());
		
		//assertNull(test);
	}

	//@Test
	public void testInsert() {		
		Assume.assumeTrue(documentManagerFactory.isOpen());
		
		testObject = documentManager.insert(MongoTestObject.class, testObject);						
		assertNotNull(testObject.getId());		
	}
	
	//@Test
	public void testQuery() {
		Assume.assumeTrue(documentManagerFactory.isOpen());
		
		testObject = documentManager.insert(MongoTestObject.class, testObject);
		assertNotNull(testObject.getId());		
		
        testObject = documentManager.find(MongoTestObject.class, testObject.getId());
		assertNotNull(testObject);
		
		List<MongoTestObject> testObjectList = documentManager.createQuery(MongoTestObject.class).getResultList();		
		assertNotNull(testObjectList);
		
		assertNotEquals(testObjectList.size(), 0);
	}
	
	//@Test
	public void testUpdate() {
		Assume.assumeTrue(documentManagerFactory.isOpen());
		
		testObject = documentManager.insert(MongoTestObject.class, testObject);
		assertNotNull(testObject.getId());
		
		testObject.setName("Updated");
        testObject = documentManager.update(MongoTestObject.class, testObject);
		assertEquals(testObject.getName(), documentManager.find(MongoTestObject.class, testObject.getId()).getName());				
	}
	
	//@Test
	public void testDelete() {
		Assume.assumeTrue(documentManagerFactory.isOpen());
		
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