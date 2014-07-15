package tests;

import junit.framework.*;
import tests.businessTests.*;
import tests.objectTests.*;

public class UnitTests 
{
	public static TestSuite suite;
	
	public static Test suite()
	{
		suite = new TestSuite("Unit Tests");
		testGateways();
		testObjects();
		testBusiness();
		return suite;
	}
	
	private static void testGateways()
	{
		suite.addTestSuite(StubDaoTest.class);
		suite.addTestSuite(StubGatewayTest.class);
	}
	
	private static void testObjects()
	{
		suite.addTestSuite(DBObjectTest.class);
		suite.addTestSuite(ProductPackageTest.class);
		suite.addTestSuite(ProductTest.class);
		suite.addTestSuite(ClientTest.class);
		suite.addTestSuite(EventTest.class);
		suite.addTestSuite(BillTest.class);
		suite.addTestSuite(PaymentTest.class);
		suite.addTestSuite(BillPackageTest.class);
		suite.addTestSuite(BillProductTest.class);
	}
	
	private static void testBusiness()
	{
		suite.addTestSuite(ProductPackageManagerTest.class);
		suite.addTestSuite(ProductManagerTest.class);
		suite.addTestSuite(ClientManagerTest.class);
		suite.addTestSuite(BillManagerTest.class);
		suite.addTestSuite(PaymentManagerTest.class);
		suite.addTestSuite(EventManagerTest.class);
	}
}
