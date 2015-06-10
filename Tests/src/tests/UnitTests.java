package tests;

import tests.businessManagerTests.ClientManagerTest;

import tests.objectTests.ClientTest;
import tests.objectTests.PhoneNumberTest;

import junit.framework.TestSuite;
import junit.framework.Test;

public class UnitTests {
	
	public static TestSuite suite;
	
	public static Test suite() {
		suite = new TestSuite("Unit Tests");
		
		testObjects();
		testBusinessManagers();
		
		return suite;
	}
	
	//Gets all object unit tests
	private static void testObjects() {
		suite.addTestSuite(ClientTest.class);
		suite.addTestSuite(PhoneNumberTest.class);
	}
	
	//Gets all gateway unit tests
	private static void testBusinessManagers() {
		suite.addTestSuite(ClientManagerTest.class);
	}
}
