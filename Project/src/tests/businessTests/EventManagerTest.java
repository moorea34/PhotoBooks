package tests.businessTests;

import java.util.*;

import junit.framework.*;
import photobooks.objects.*;
import photobooks.business.*;
import photobooks.gateways.*;

import org.mockito.*;

public class EventManagerTest extends TestCase
{
	EventManager _manager;
	final String DESCRIPTION = "My description";
	
	public EventManagerTest( String arg0 )
	{
		super(arg0);
	}
	
	public void setUp()
	{
		IGateway<Event> gateway = new StubGateway<Event>();
		_manager = new EventManager(gateway);
	}
	
	public void clearStubDB()
	{
		/* Clear stub db */
		for( Event e : _manager.getAllEvents() )
		{
			_manager.removeEvent(e);
		}
	}
	
	public void test_InsertEvent()
	{
		// Arrange
		@SuppressWarnings("unchecked")
		IGateway<Event> fakeGateway = Mockito.mock( IGateway.class );	
		Event fakeEvent = Mockito.mock( Event.class );
		_manager = new EventManager( fakeGateway );
		
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
		_manager = new EventManager( fakeGateway );
		
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
		_manager = new EventManager( fakeGateway );
		
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
		_manager = new EventManager( fakeGateway );
		
		// Act
		_manager.getAllEvents();
		
		// Assert
		Mockito.verify( fakeGateway ).getAll();
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
		
		_manager = new EventManager( fakeGateway );
		
		// Act
		_manager.removeAllEvents( events );
		
		// Assert
//		Mockito.verify( fakeGateway ).delete( event1 );
//		Mockito.verify( fakeGateway ).delete( event2 );
//		Mockito.verify( fakeGateway ).delete( event3 );
	}
}
