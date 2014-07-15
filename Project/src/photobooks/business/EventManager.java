package photobooks.business;

import java.util.*;

import photobooks.gateways.*;
import photobooks.objects.*;
import photobooks.objects.Event.EventType;
import photobooks.presentation.*;
import photobooks.application.*;

public class EventManager 
{
	private IGateway<Event> _gateway;
	private MainWindow _main;
	
	public static final int DISPLAY_DATE_RANGE = 7;

	public EventManager( MainWindow main, IGateway<Event> gateway )
	{
		_gateway = gateway;
		_main = main;
	}

	// This stub data is somewhat dependent on the client stub data
	public void insertStubData( ClientManager clientManager )
	{
		if (this.getAllEvents().size() > 0) {
			return;
		}
		
		ArrayList<Client> clients = new ArrayList<Client>( clientManager.getClientList() );
		
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DATE, 1);
		if(clients.size() > 0)
			insertEvent( new Event( EventType.Birthday, date, clients.get(0).getID() ) );
		
		Calendar date2 = new GregorianCalendar();
		date2.add(Calendar.DATE, 2);
		if(clients.size() > 1)
			insertEvent( new Event( EventType.Meeting, date2, clients.get(1).getID() ) );
		
		// Next week, won't be alerted to it
		Calendar date3 = new GregorianCalendar();
		date3.add(Calendar.DATE, 6);
		if(clients.size() > 1)
			insertEvent( new Event( EventType.Photoshoot, date3, clients.get(1).getID() ) );
	}

	public void insertEvent(Event event)
	{
		_gateway.add(event);
	}

	public void removeEvent(Event event)
	{
		_gateway.delete(event);
	}

	public void updateEvent(Event event)
	{
		_gateway.update(event);
	}
	
	public ArrayList<Event> getAllEvents()
	{
		ArrayList<Event> events = new ArrayList<Event>( _gateway.getAll() );
		Collections.sort( events );
		return events;
	}
	
	public ArrayList<Event> getEventsByClientID( int id )
	{
		ArrayList<Event> list = this.getAllEvents();
		ArrayList<Event> found = new ArrayList<Event>();
		
		for ( Event event : list )
		{
			if ( event.getClientID() != id )
			{
				found.add( event );
			}
		}
		
		list.removeAll(found);
		
		return list;
	}
	
	public void removeAllEvents( ArrayList<Event> list )
	{
		for (Event event : list)
		{
			this.removeEvent( event );
		}
	}
	
	public void insertEventsForClient(Client client)
	{
		if (client.getBirthday() != null)
		{
			Event birthday = new Event( EventType.Birthday, client.getBirthday(), client.getID() );
			this.insertEvent( birthday );
		}
		
		if (client.getAnniversary() != null)
		{
			Event anniversary = new Event( EventType.Anniversary, client.getAnniversary(), client.getID() );
			this.insertEvent( anniversary );
		}
	}
	
	public void removeEventsForClient(Client client)
	{
		ArrayList<Event> eventList = this.getEventsByClientID( client.getID() );
		this.removeAllEvents( eventList );
	}
	
	public void updateEventsForClient(Client client)
	{
		ArrayList<Event> events = this.getEventsByClientID(client.getID());
		for (Event event : events)
		{
			switch ( event.getType() )
			{
				case Birthday:
					event.setDate( client.getBirthday() );
					this.updateEvent(event);
					break;
				case Anniversary:
					event.setDate( client.getAnniversary() );
					this.updateEvent(event);
					break;
				default:
					break;
			}
		}
	}
	
	// Goes through events and lists the ones that are happening within a week.
	// Also deletes any old ones.
	// NOTE: A week is defined as 7 days into the future, not a business week
	public ArrayList<Event> getEventsToDisplay()
	{
		ArrayList<Event> events = getAllEvents();
		ArrayList<Event> eventsToDisplay = new ArrayList<Event>();

		// Get today and next week
		Calendar today = new GregorianCalendar();
		Calendar endOfPeriod = new GregorianCalendar();
		endOfPeriod.add(Calendar.DAY_OF_MONTH, DISPLAY_DATE_RANGE);	
		
		// If an event is older than today get rid of it
		// If it is within 7 days display it
		for (Event event : events)
		{
			if ( event.getDate().before(today) && !event.isAnnual() )
			{
				removeEvent( event );
			}
			else if ( (!event.isAnnual() && event.getDate().before(endOfPeriod) ) ||
					  (event.isAnnual() && withinDayOfYearRange( event.getDate(), today, endOfPeriod ) ) )
			{				
				eventsToDisplay.add( event );
			}
		}
		
		return eventsToDisplay;
	}
	
	private boolean withinDayOfYearRange( Calendar target, Calendar start, Calendar end )
	{
		return target.get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.DAY_OF_YEAR) &&
			   target.get(Calendar.DAY_OF_YEAR) < end.get(Calendar.DAY_OF_YEAR);
	}

	// Returns all of the event notifications in a formatted string
	public String getEventNotifications( ClientManager clientManager )
	{
		ArrayList<Event> events = getEventsToDisplay();
		String message = "";
		
		for (Event event : events)
		{
			Calendar date = event.getDate();
			
			// Get the day of the week
			Calendar newDate = Calendar.getInstance();
			newDate.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
			String dayOfWeek = Utility.dayOfWeekToString( newDate.get(Calendar.DAY_OF_WEEK) );
			
			message += String.format("%s, %s %s\n",
					dayOfWeek,
					Utility.monthToString( date.get( Calendar.MONTH ) ),
					date.get( Calendar.DAY_OF_MONTH) );
			message += event.getDescription( clientManager );
			message += "\n\n";
		}
		
		return message;
	}
	
	public boolean checkForEvents( ClientManager clientManager )
	{
		// Call back to the UI
		String notifications = this.getEventNotifications( clientManager );
		if ( !notifications.equals("") )
		{
			_main.alertToEvents( notifications );
			return true;
		}
		
		return false;
	}
}