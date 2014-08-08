package tests.integrationTests;

import java.util.*;

import junit.framework.*;
import photobooks.application.Utility;
import photobooks.business.*;
import photobooks.gateways.*;
import photobooks.objects.*;

public class EventIntegrationTests extends TestCase
{	
	private IDao _dao;
	private boolean _isStub = true;
	private EventManager _eventManager;
	private final String DESCRIPTION = "My description";
	//private final int CLIENT_ID = 3;
	
	public void tearDown()
	{
		_dao.dispose();
	}
	
	private void setUpDB()
	{
	}
	
	private void tearDownDB()
	{
	}
	
	private void restartDB()
	{
		if (_dao != null)
		{
			_dao.dispose();
		}
		
		_dao = getDao();
		_eventManager = new EventManager(_dao.eventGateway()); 
		
		if (_isStub)
		{
			//_eventManager.insertStubData(_clientManager);
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
		Calendar date = GregorianCalendar.getInstance();
		date.add(Calendar.DAY_OF_MONTH, 20);
		
		Event newEvent = new Event(date, "");
		_eventManager.insertEvent( newEvent );
		
		// Read
		ArrayList<Event> events = _eventManager.getAllEvents();
		int eventCount = events.size();
		assertTrue( eventCount > 0 );
		
		// Update
		Event testEvent = _eventManager.getById(newEvent.getID());
		assertEquals(newEvent.getID(), testEvent.getID());
		assertEquals(newEvent.getDescription(), testEvent.getDescription());
		assertTrue(Utility.calendarsEqual(testEvent.getDate(), newEvent.getDate()));
		
		date = GregorianCalendar.getInstance();
		date.add(Calendar.YEAR, 3);
		testEvent.setDate( date );
		testEvent.setDescription(DESCRIPTION);
		_eventManager.updateEvent( testEvent );
		
		testEvent = _eventManager.getById(testEvent.getID());

		assertEquals(testEvent.getDescription(), DESCRIPTION);
		assertTrue(Utility.calendarsEqual(testEvent.getDate(), date));
		
		// Delete
		_eventManager.removeEvent( testEvent );
		
		events = _eventManager.getAllEvents();
		
		assertTrue( events.size() == eventCount - 1 );
	}
}
