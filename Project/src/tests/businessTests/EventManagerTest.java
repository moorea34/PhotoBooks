package tests.businessTests;

import java.util.*;

import junit.framework.*;
import photobooks.objects.*;
import photobooks.objects.Event.EventType;
import photobooks.business.*;
import photobooks.gateways.*;

import org.mockito.*;

public class EventManagerTest extends TestCase
{
	EventManager _manager;
	
	public EventManagerTest( String arg0 )
	{
		super(arg0);
	}
	
	public void setUp()
	{
		IGateway<Event> gateway = new StubGateway<Event>();
		_manager = new EventManager( null, gateway );
	}
	
	public void clearStubDB()
	{
		/* Clear stub db */
		for( Event e : _manager.getAllEvents() )
		{
			_manager.removeEvent(e);
		}
	}
	
	public void test_GetEventsToDisplay_DeletesOldEvents()
	{
		// Arrange
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DATE, -1);
		
		_manager.insertEvent( new Event( EventType.Meeting, date, 0 ) );
		
		// Act
		ArrayList<Event> events = _manager.getEventsToDisplay();
		
		// Assert
		assertTrue( events.size() == 0 );
	}
	
	public void test_GetEventsToDisplay_DoesNotDeleteAnnualEvents()
	{
		// Arrange
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DATE, -1);
		
		Event event = new Event( EventType.Birthday, date, 0 );
		Event event2 = new Event ( EventType.Anniversary, date, 0 );
		
		_manager.insertEvent( event );
		_manager.insertEvent( event2 );
		
		// Act
		_manager.getEventsToDisplay();
		
		// Assert
		ArrayList<Event> events = _manager.getAllEvents();
		assertTrue( events.size() == 2 );
		assertTrue( events.get(0).equals( event ) );
	}
	
	public void test_GetEventsToDisplay_DoesNotDisplayAnnualEventsBeforeRange()
	{
		// Arrange
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DATE, -1);
		
		Event event = new Event( EventType.Birthday, date, 0 );
		_manager.insertEvent( event );
		
		// Act
		ArrayList<Event> events = _manager.getEventsToDisplay();
		
		// Assert
		assertTrue( events.size() == 0 );
	}
	
	
	public void test_GetEventsToDisplay_DisplaysEventsInRange()
	{
		// Arrange
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DATE, 1);
		
		Event event = new Event( EventType.Meeting, date, 0 );
		_manager.insertEvent( event );
		
		// Act
		ArrayList<Event> events = _manager.getEventsToDisplay();
		
		// Assert
		assertTrue( events.size() == 1 );
		assertTrue( events.get(0).equals( event ) );
	}
	
	
	public void test_GetEventsToDisplay_DoesNotDisplayEventsPastRange()
	{
		// Arrange
		Calendar date = new GregorianCalendar();
		date.add( Calendar.DATE, EventManager.DISPLAY_DATE_RANGE + 1 );
		
		Event event = new Event( EventType.Meeting, date, 0 );
		_manager.insertEvent( event );
		
		// Act
		ArrayList<Event> events = _manager.getEventsToDisplay();
		
		// Assert
		assertTrue( events.size() == 0 );
	}
	
	public void test_InsertEvent()
	{
		// Arrange
		@SuppressWarnings("unchecked")
		IGateway<Event> fakeGateway = Mockito.mock( IGateway.class );	
		Event fakeEvent = Mockito.mock( Event.class );
		_manager = new EventManager( null, fakeGateway );
		
		// Act
		_manager.insertEvent( fakeEvent );
		
		// Assert
		Mockito.verify( fakeGateway ).add( fakeEvent );
	}
	
	public void test_removeEvent()
	{
		// Arrange
		@SuppressWarnings("unchecked")
		IGateway<Event> fakeGateway = Mockito.mock( IGateway.class );	
		Event fakeEvent = Mockito.mock( Event.class );
		_manager = new EventManager( null, fakeGateway );
		
		// Act
		_manager.removeEvent( fakeEvent );
		
		// Assert
		Mockito.verify( fakeGateway ).delete( fakeEvent );
	}
	
	public void test_updateEvent()
	{
		// Arrange
		@SuppressWarnings("unchecked")
		IGateway<Event> fakeGateway = Mockito.mock( IGateway.class );	
		Event fakeEvent = Mockito.mock( Event.class );
		_manager = new EventManager( null, fakeGateway );
		
		// Act
		_manager.updateEvent( fakeEvent );
		
		// Assert
		Mockito.verify( fakeGateway ).update( fakeEvent );
	}
	
	public void test_getAllEvents()
	{
		// Arrange
		@SuppressWarnings("unchecked")
		IGateway<Event> fakeGateway = Mockito.mock( IGateway.class );
		_manager = new EventManager( null, fakeGateway );
		
		// Act
		_manager.getAllEvents();
		
		// Assert
		Mockito.verify( fakeGateway ).getAll();
	}
	
	public void test_getEventsByClientID_FoundOne()
	{
		// Arrange
		Calendar date = GregorianCalendar.getInstance();
		Event event1 = new Event( EventType.Birthday, date, 0 );
		Event event2 = new Event( EventType.Birthday, date, 1 );
		Event event3 = new Event( EventType.Birthday, date, 2 );

		_manager.insertEvent(event1);
		_manager.insertEvent(event2);
		_manager.insertEvent(event3);
		
		// Act
		ArrayList<Event> result = _manager.getEventsByClientID( 1 );
		
		// Assert
		assertTrue( result.size() == 1 );
		assertTrue( result.get(0).equals( event2 ));
	}
	
	public void test_getEventsByClientID_FoundZero()
	{
		// Arrange
		Calendar date = GregorianCalendar.getInstance();
		Event event1 = new Event( EventType.Birthday, date, 0 );
		Event event2 = new Event( EventType.Birthday, date, 1 );
		Event event3 = new Event( EventType.Birthday, date, 2 );

		_manager.insertEvent(event1);
		_manager.insertEvent(event2);
		_manager.insertEvent(event3);
		
		// Act
		ArrayList<Event> result = _manager.getEventsByClientID( 3 );
		
		// Assert
		assertTrue( result.size() == 0 );
	}
	
	public void test_getEventsByClientID_FoundMultiple()
	{
		// Arrange
		Calendar date = GregorianCalendar.getInstance();
		Event event1 = new Event( EventType.Birthday, date, 0 );
		Event event2 = new Event( EventType.Birthday, date, 1 );
		Event event3 = new Event( EventType.Birthday, date, 1 );
		Event event4 = new Event( EventType.Birthday, date, 2 );
		Event event5 = new Event( EventType.Birthday, date, 1 );

		_manager.insertEvent(event1);
		_manager.insertEvent(event2);
		_manager.insertEvent(event3);
		_manager.insertEvent(event4);
		_manager.insertEvent(event5);
		
		// Act
		ArrayList<Event> result = _manager.getEventsByClientID( 1 );
		
		// Assert
		assertTrue( result.size() == 3 );
		assertTrue( result.get(0).equals( event2 ));
		assertTrue( result.get(1).equals( event3 ));
		assertTrue( result.get(2).equals( event5 ));
	}
	
	public void test_removeAllEvents()
	{
		// Arrange
		Event event1 = new Event( null, null, 0 );
		Event event2 = new Event( null, null, 1 );
		Event event3 = new Event( null, null, 2 );
		
		ArrayList<Event> events = new ArrayList<Event>();
		events.add(event1);
		events.add(event2);
		events.add(event3);
		
		@SuppressWarnings("unchecked")
		IGateway<Event> fakeGateway = Mockito.mock( StubGateway.class );
		
		_manager = new EventManager( null, fakeGateway );
		
		// Act
		_manager.removeAllEvents( events );
		
		// Assert
//		Mockito.verify( fakeGateway ).delete( event1 );
//		Mockito.verify( fakeGateway ).delete( event2 );
//		Mockito.verify( fakeGateway ).delete( event3 );
	}
}
