package tests;

import photobooks.gateways.IGateway;
import photobooks.gateways.IDao;
import photobooks.gateways.StubGateway;
import photobooks.gateways.StubDao;

import junit.framework.TestCase;

import photobooks.objects.Bill;

public class StubGatewayTest extends TestCase
{
	class TestClass
	{
		public int id;
		
		public TestClass()
		{
			
		}
	}
	
	//Just test StubDBObjectGateway methods, since the StubGatewayTest already covers the others
	public void testAddAndGetByID()
	{
		IDao dao = new StubDao();
		IGateway<Bill> quoteGateway = dao.billGateway();
		IGateway<TestClass> testclassGateway = new StubGateway<TestClass>();
		
		TestClass tc = new TestClass();
		Bill bill = new Bill();
		
		assertTrue(quoteGateway.getAll().size() == 0);
		
		//Test DBObject
		assertTrue(quoteGateway.add(bill));
		
		assertFalse(quoteGateway.add(bill));
		assertTrue(bill.getID() == 1);
		
		
		assertTrue(quoteGateway.getByID(1) == bill);
		assertTrue(quoteGateway.getAll().contains(bill));
		assertTrue(quoteGateway.getAll().size() == 1);
		
		quoteGateway.delete(bill);
		
		assertTrue(quoteGateway.getByID(1) == null);
		assertFalse(quoteGateway.getAll().contains(bill));
		assertTrue(quoteGateway.getAll().size() == 0);
		
		//Test that non-DBObject fails
		assertFalse(testclassGateway.add(tc));
		
		assertFalse(testclassGateway.getAll().contains(tc));
		assertTrue(testclassGateway.getAll().size() == 0);
		assertTrue(testclassGateway.getByID(1) == null);
		
		dao.dispose();
	}

}
