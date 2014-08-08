package tests.businessTests;

import photobooks.objects.Client;
import photobooks.objects.Bill;

import photobooks.business.ClientManager;
import photobooks.business.BillManager;
import photobooks.gateways.*;
import junit.framework.TestCase;

public class BillManagerTest extends TestCase {
	
	private BillManager _manager;
	
	public void setUp()
	{
		_manager = new BillManager( new StubDao() );
	}

	public void testQuoteManager() {
		assertNotNull(_manager.getAll());
	}

	public void testGetByClientID() {
		IGateway<Client> stubClientGateway = new StubGateway<Client>();
		ClientManager clientManager = new ClientManager(stubClientGateway);
		Bill bill = new Bill();
		Client client = new Client("Alex", "Moore");
		
		clientManager.insertClient(client);
		bill.setClient(client);
		
		_manager.insert(bill);
		
		assertNotNull(_manager.getByClientID(client.getID()));
		assertTrue(_manager.getByClientID(client.getID()).size() > 0);
		
		_manager.delete(bill);
		clientManager.removeClient(client);
	}

	public void testGetByID() {
		Bill bill = new Bill();
		
		_manager.insert(bill);
		
		assertNotNull(_manager.getByID(bill.getID()));
		
		_manager.delete(bill);
	}

	public void testInsert() {
		Bill bill = new Bill();
		
		_manager.insert(bill);
		
		assertNotNull(_manager.getByID(bill.getID()));
		
		_manager.delete(bill);
	}

	public void testDelete() {
		Bill bill = new Bill();
		int size;
		
		_manager.insert(bill);
		size = _manager.getAll().size();
		
		_manager.delete(bill);
		
		assertTrue(_manager.getAll().size() == (size - 1));
	}

	public void testUpdate() {
		//DBStub doesn't implement update
	}

}
