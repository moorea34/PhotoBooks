package photobooks.business;

import java.util.*;

import photobooks.gateways.*;
import photobooks.objects.*;

public class EventManager 
{
	private IGateway<Event> _gateway;

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
	
	public Event getById(int id)
	{
		return _gateway.getByID(id);
	}
	
	public void removeAllEvents( ArrayList<Event> list )
	{
		for (Event event : list)
		{
			this.removeEvent( event );
		}
	}
}