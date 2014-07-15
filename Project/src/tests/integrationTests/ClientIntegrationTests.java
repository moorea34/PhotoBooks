package tests.integrationTests;

import java.util.*;
import junit.framework.*;
import photobooks.business.*;
import photobooks.gateways.*;
import photobooks.objects.*;

public class ClientIntegrationTests extends TestCase
{	
	private IDao _dao;
	private boolean _isStub;
	private ClientManager _clientManager;
	private EventManager _eventManager;
	private final String FIRST_NAME = "Mark";
	private final String LAST_NAME = "Twain";
	private final String NEW_FIRST_NAME = "John";

	public void tearDown()
	{
		_dao.dispose();
	}
	
	private void restartDB()
	{
		if (_dao != null)
		{
			_dao.dispose();
		}
		
		_dao = getDao();
		IGateway<Client> clientGateway = _dao.clientGateway();
		_eventManager = new EventManager( null, _dao.eventGateway() ); 
		_clientManager = new ClientManager( clientGateway, _eventManager );
		
		if (_isStub)
		{
			_clientManager.insertStubData();
		}
	}
	
	private IDao getDao()
	{
		if (_isStub)
		{
			return new StubDao();
		} else
		{
			return new Dao();
		}
	}
	
	public void test_CRUD_stub()
	{
		_isStub = true;
		restartDB();
		CRUD();
	}
	
	public void test_CRUD_real()
	{
		_isStub = false;
		restartDB();
		CRUD();
	}
	
	private void CRUD()
	{
		// Create
		Client newClient = new Client( FIRST_NAME, LAST_NAME );
		_clientManager.insertClient( newClient );
		int clientID = newClient.getID();
		
		// Read
		ArrayList<Client> clients = _clientManager.getClientList();
		assertTrue( clients.size() == 3 );
		assertTrue( clients.get(2).getFirstName().equals( FIRST_NAME ));
		assertTrue( clients.get(2).getLastName().equals( LAST_NAME ));
		
		Client testClient = _clientManager.getClientByID( clientID );
		
		// Update
		testClient.setFirstName( NEW_FIRST_NAME );
		_clientManager.updateClient( testClient );
		
		testClient = _clientManager.getClientByID( clientID );
		
		assertTrue( testClient.getFirstName().equals( NEW_FIRST_NAME ));
		assertTrue( testClient.getLastName().equals( LAST_NAME ));
		
		// Delete
		_clientManager.removeClient( testClient );
		
		clients = _clientManager.getClientList();
		
		assertTrue( clients.size() == 2 );
		assertTrue( _clientManager.getClientByID( clientID ) == null );
	}
}
