package tests.objectTests;

import junit.framework.TestCase;

import photobooks.objects.DBObject;

public class DBObjectTest extends TestCase {

	
	public void testConstructor() {
		DBObject obj1 = new DBObject();
		DBObject obj2 = new DBObject(10);
		
		assertTrue(obj1.getID() == 0);
		assertTrue(obj2.getID() == 10);
	}

	public void testSetAndGetID() {
		DBObject obj1 = new DBObject();
		DBObject obj2 = new DBObject(10);
		
		obj1.setID(4);
		obj2.setID(5);
		
		assertTrue(obj1.getID() == 4);
		assertTrue(obj2.getID() == 5);
	}
}
