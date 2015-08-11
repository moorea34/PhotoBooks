package tests;

import java.io.File;

import photobooks.application.Utility;
import photobooks.gateways2.IDao;
import photobooks.hsqldbgateways.HSQLDBDao;
import tests.businessManagerTests.ClientManagerTest;
import tests.objectTests.ClientTest;
import tests.objectTests.PhoneNumberTest;
import junit.framework.TestSuite;
import junit.framework.Test;

public class UnitTests {
	
	public static TestSuite suite;
	

	public static IDao openTestDao() {
		return HSQLDBDao.loadDB("TestDatabase");
	}
	
	public static IDao recreateTestDao() {
		//Delete database so we can test with a clean one every time
		Utility.deleteRecursive(new File("TestDatabase"));
		
		return HSQLDBDao.createDB("TestDatabase");
	}
	
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
