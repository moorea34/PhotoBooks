package tests.objectTests;

import java.util.*;

import junit.framework.TestCase;
import photobooks.objects.*;
import org.mockito.*;

public class EventTest extends TestCase
{
	private Event _event;
	private final int ID = 10;
	private final String DESCRIPTION = "my desc";
	
	public void setUp()
	{
		Calendar mockDate = Mockito.mock( Calendar.class );
		
		_event = new Event(mockDate, DESCRIPTION, ID);
	}
	
	
	public void testConstructor()
	{
		Calendar mockDate = Mockito.mock( Calendar.class );
		
		// Act
		_event = new Event(mockDate, DESCRIPTION, ID);
		
		// Assert
		assertTrue(_event.getDescription().equals(DESCRIPTION));
		assertTrue(_event.getDate().equals(mockDate));
		assertTrue(_event.getID() == ID);
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
	
	public void testSetDescription()
	{
		_event = new Event("d");
		
		_event.setDescription(DESCRIPTION);
		
		// Assert
		assertTrue(_event.getDescription().equals(DESCRIPTION));
	}
	
}