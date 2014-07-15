package tests;

import photobooks.gateways.*;
import photobooks.objects.Client;

import junit.framework.TestCase;

public class StubDaoTest extends TestCase
{
	
	public void testClientGateway()
	{
		IDao dao = new StubDao();
		Client client1 = new Client("Alex", "Moore");
		Client client2 = new Client("Chris", "Krahn");
		
		assertTrue(dao.clientGateway().add(client1));
		
		assertTrue(dao.clientGateway().getAll().size() == 1);
		assertTrue(dao.clientGateway().getAll().contains(client1));
		assertTrue(dao.clientGateway().getByID(client1.getID()) == client1);
		
		assertTrue(dao.clientGateway().add(client2));
		
		assertTrue(dao.clientGateway().getAll().size() == 2);
		assertTrue(dao.clientGateway().getAll().contains(client2));
		assertTrue(dao.clientGateway().getByID(client2.getID()) == client2);
		
		dao.clientGateway().delete(client1);
		
		assertFalse(dao.clientGateway().getAll().contains(client1));
		assertTrue(dao.clientGateway().getAll().contains(client2));
		assertTrue(dao.clientGateway().getAll().size() == 1);
		
		//If delete is called twice, there should be no change
		dao.clientGateway().delete(client1);
		
		assertFalse(dao.clientGateway().getAll().contains(client1));
		assertTrue(dao.clientGateway().getAll().contains(client2));
		assertTrue(dao.clientGateway().getAll().size() == 1);
		
		dao.clientGateway().delete(client2);
		
		assertTrue(dao.clientGateway().getAll().size() == 0);
		
		//StubGateway does not implement Update
	}
}
