package tests;

import tests.objectTests.ClientTest;

import junit.framework.TestSuite;
import junit.framework.Test;

public class UnitTests {
	
	public static TestSuite suite;
	
	public static Test suite()
	{
		suite = new TestSuite("Unit Tests");
		
		testObjects();
		
		return suite;
	}
	
	private static void testObjects()
	{
		suite.addTestSuite(ClientTest.class);
	}
}
