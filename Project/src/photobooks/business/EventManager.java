package photobooks.business;

import java.util.*;

import photobooks.application.Utility;
import photobooks.gateways.*;
import photobooks.objects.*;

public class EventManager 
{
	private Calendar endOfDecember;
	private Calendar startOfJanuary = (Calendar)Calendar.getInstance().clone();
	
	private IDao _dao;
	private IGateway<Event> _gateway;
	private IGateway<Client> _clientGateway = null;
	
	public EventManager(IDao dao) {
		startOfJanuary.set(startOfJanuary.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1, 0, 0, 0);
		startOfJanuary.set(Calendar.MILLISECOND, 0);
		
		endOfDecember = (Calendar)startOfJanuary.clone();
		endOfDecember.add(Calendar.MILLISECOND, -1);
		
		_dao = dao;
		_gateway = _dao.eventGateway();
		_clientGateway = _dao.clientGateway();
	}

	public EventManager(IGateway<Event> gateway)
	{
		_gateway = gateway;
	}

	// This stub data is somewhat dependent on the client stub data
	/*public void insertStubData( ClientManager clientManager )
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
	}*/
	
	//Gets all Events from events where date >= start and <= end
	//keepNull will keep events whose 
	public ArrayList<Event> getEventsInRange(Collection<Event> events, Calendar start, Calendar end, boolean keepNull)
	{
		ArrayList<Event> results = new ArrayList<Event>();
		
		for (Event event : events)
		{
			if ((keepNull && event.getDate() == null) || (event.compareDateTo(start) >= 0 && event.compareDateTo(end) <= 0))
			{
				results.add(event);
			}
		}
		
		return results;
	}
	
	//Gets all Birthdays from events where date >= start and <= end (compares only month and day)
	//keepNull will keep events whose 
	public ArrayList<Event> getBirthdaysInRange(Collection<Event> events, Calendar start, Calendar end, boolean keepNull)
	{
		ArrayList<Event> results = new ArrayList<Event>();
		boolean startLTEnd = true;
		
		if (Utility.compareBirthdays(start, end) >= 0)
			startLTEnd = false;
		
		for (Event event : events)
		{
			if ((keepNull && event.getDate() == null) || (startLTEnd && Utility.compareBirthdays(event.getDate(), start) >= 0 && Utility.compareBirthdays(event.getDate(), end) <= 0) ||
					(!startLTEnd && !(Utility.compareBirthdays(event.getDate(), start) < 0 && Utility.compareBirthdays(event.getDate(), end) > 0)))
			{
				results.add(event);
			}
		}
		
		return results;
	}
	
	private Event calendarToEvent(Calendar date, String description) {
		Event result = null;
		
		if (date != null)
			result = new Event((Calendar)date.clone(), description);
		
		return result;
	}
	
	//Gets the list of birthday's and anniversary's
	public ArrayList<Event> getClientEvents()
	{
		ArrayList<Event> events = new ArrayList<Event>();
		Collection<Client> clients = _clientGateway.getAll();
		Event temp;
		
		for (Client client : clients) {
			temp = calendarToEvent(client.getBirthday(), String.format("%s's birthday!", client.getFullName()));
			if (temp != null) events.add(temp);
			
			temp = calendarToEvent(client.getAnniversary(), String.format("%s's anniversary!", client.getFullName()));
			if (temp != null) events.add(temp);
		}
		
		return events;
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
		return new ArrayList<Event>(_gateway.getAll());
	}
	
	public ArrayList<Event> getAllEventsInRange(Calendar start, Calendar end, boolean keepNull)
	{
		if (_gateway instanceof EventGateway)
			return new ArrayList<Event>(((EventGateway<Event>)_gateway).getAllInRange(start, end, keepNull));
		else
			return getEventsInRange(_gateway.getAll(), start, end, keepNull);
	}
	
	public Event getById(int id)
	{
		return _gateway.getByID(id);
	}
	
	public void removeAllEvents(ArrayList<Event> list)
	{
		for (Event event : list)
		{
			this.removeEvent(event);
		}
	}
}