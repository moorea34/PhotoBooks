package tests.objectTests;

import java.util.*;

import junit.framework.TestCase;
import photobooks.business.*;
import photobooks.objects.*;
import photobooks.objects.Event.EventType;
import org.mockito.*;

public class EventTest extends TestCase
{
	private Event _event;
	private final String NAME = "full name";
	
	public void setUp()
	{		
		EventType type = EventType.Birthday;
		Calendar mockDate = Mockito.mock( Calendar.class );
		
		_event = new Event( type, mockDate, 0 );
	}
	
	
	public void testConstructor_ValidEventObject()
	{
		// Arrange
		EventType type = EventType.Birthday;
		Calendar mockDate = Mockito.mock( Calendar.class );
		
		// Act
		_event = new Event( type, mockDate, 0 );
		
		// Assert
		assertTrue(_event.getClientID() == 0);
		assertTrue(_event.getDate().equals(mockDate));
		assertTrue(_event.getType() == type);
	}
	
	
	public void testSetDate_ReturnsCorrectDate()
	{
		// Arrange
		Calendar mockDate = Mockito.mock( Calendar.class );
		
		// Act
		_event.setDate( mockDate );
		
		// Assert
		assertTrue( _event.getDate().equals( mockDate ) );
	}
	
	public void testGetDescription_Birthday()
	{
		// Arrange
		Client fakeClient = Mockito.mock( Client.class );
		Mockito.when( fakeClient.getFullName() ).thenReturn( NAME );
		
		ClientManager fakeManager = Mockito.mock( ClientManager.class );
		Mockito.when( fakeManager.getClientByID(0) ).thenReturn( fakeClient );
		
		_event = new Event( EventType.Birthday, null, 0 );
		
		// Act
		String result = _event.getDescription( fakeManager );
		
		// Assert
		assertTrue( result.equals( "It is full name's birthday!" ) );
		Mockito.verify( fakeClient ).getFullName();
		Mockito.verify( fakeManager ).getClientByID(0);
	}
	
	public void testGetDescription_Meeting()
	{
		// Arrange
		Client fakeClient = Mockito.mock( Client.class );
		Mockito.when( fakeClient.getFullName() ).thenReturn( NAME );
		
		ClientManager fakeManager = Mockito.mock( ClientManager.class );
		Mockito.when( fakeManager.getClientByID(0) ).thenReturn( fakeClient );
		
		_event = new Event( EventType.Meeting, null, 0 );
		
		// Act
		String result = _event.getDescription( fakeManager );
		
		// Assert
		assertTrue( result.equals( "You have a meeting with full name." ) );
		Mockito.verify( fakeClient ).getFullName();
		Mockito.verify( fakeManager ).getClientByID(0);
	}
	
	public void testGetDescription_Photoshoot()
	{
		// Arrange
		Client fakeClient = Mockito.mock( Client.class );
		Mockito.when( fakeClient.getFullName() ).thenReturn( NAME );
		
		ClientManager fakeManager = Mockito.mock( ClientManager.class );
		Mockito.when( fakeManager.getClientByID(0) ).thenReturn( fakeClient );
		
		_event = new Event( EventType.Photoshoot, null, 0 );
		
		// Act
		String result = _event.getDescription( fakeManager );
		
		// Assert
		assertTrue( result.equals( "You have a photoshoot with full name." ) );
		Mockito.verify( fakeClient ).getFullName();
		Mockito.verify( fakeManager ).getClientByID(0);
	}
	
}