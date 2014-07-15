package tests.integrationTests;

import java.util.*;

import junit.framework.*;
import photobooks.business.*;
import photobooks.gateways.*;
import photobooks.objects.*;
import photobooks.objects.Event.EventType;

public class EventIntegrationTests extends TestCase
{	
	private IDao _dao;
	private boolean _isStub;
	private ClientManager _clientManager;
	private EventManager _eventManager;
	private final int CLIENT_ID = 3;
	
	public void tearDown()
	{
		_dao.dispose();
	}
	
	private void setUpDB()
	{	
		_eventManager.removeAllEvents( _eventManager.getAllEvents() );
		_eventManager.insertStubData(_clientManager);
	}
	
	private void tearDownDB()
	{
		_eventManager.removeAllEvents( _eventManager.getAllEvents() );
		
		// Default DB data
		Calendar date = new GregorianCalendar();
		date.set(1993, 10 - 1, 18);
		Event newEvent = new Event( EventType.Birthday, date, 1 );
		_eventManager.insertEvent( newEvent );
		
		date = new GregorianCalendar();
		date.set(2006, 3 - 1, 20);
		newEvent = new Event( EventType.Anniversary, date, 2 );
		_eventManager.insertEvent( newEvent );
		
		date = new GregorianCalendar();
		date.set(2010, 1 - 1, 1);
		newEvent = new Event( EventType.Anniversary, date, 1 );
		_eventManager.insertEvent( newEvent );
		
		date = new GregorianCalendar();
		date.set(2014, 3 - 1, 15);
		newEvent = new Event( EventType.Birthday, date, 2 );
		_eventManager.insertEvent( newEvent );
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
			_eventManager.insertStubData(_clientManager);
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
		
		setUpDB();
		CRUD();
		tearDownDB();
	}
	
	private void CRUD()
	{
		// Create
		Client newClient = new Client();
		_clientManager.insertClient( newClient );
		
		Calendar date = GregorianCalendar.getInstance();
		date.add(Calendar.DAY_OF_MONTH, 20);
		Event newEvent = new Event( EventType.Photoshoot, date, newClient.getID() );
		_eventManager.insertEvent( newEvent );
		
		// Read
		ArrayList<Event> events = _eventManager.getAllEvents();
		assertTrue( events.size() == 4 );
		assertTrue( events.get(3).getClientID() == newClient.getID() );
		assertTrue( events.get(3).getType() == EventType.Photoshoot );
		
		events = _eventManager.getEventsByClientID( newClient.getID() );
		assertTrue( events.size() == 1);
		
		// Update
		Event testEvent = events.get(0);
		date = GregorianCalendar.getInstance();
		date.add(Calendar.YEAR, 3);
		testEvent.setDate( date );
		_eventManager.updateEvent( testEvent );
		
		testEvent = _eventManager.getEventsByClientID( newClient.getID() ).get(0);

		assertTrue( testEvent.getDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) );
		assertTrue( testEvent.getDate().get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH) );
		assertTrue( testEvent.getDate().get(Calendar.MONTH) == date.get(Calendar.MONTH) );
		
		// Delete
		_eventManager.removeEvent( testEvent );
		
		events = _eventManager.getAllEvents();
		
		assertTrue( events.size() == 3 );
		assertTrue( _eventManager.getEventsByClientID( newClient.getID() ).size() == 0 );
		
		_clientManager.removeClient( newClient );
	}
}
