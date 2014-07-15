package tests;

import junit.framework.*;
import tests.integrationTests.*;

public class IntegrationTests
{
	public static TestSuite suite;
	
	public static Test suite()
	{
		suite = new TestSuite("Integration Tests");
		
		suite.addTestSuite( BillIntegrationTests.class );
		suite.addTestSuite( ClientIntegrationTests.class );
		suite.addTestSuite( EventIntegrationTests.class );
		suite.addTestSuite( ProductIntegrationTests.class );
		
		return suite;
	}

}
