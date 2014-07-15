package photobooks.objects;

import java.util.*;

import photobooks.business.ClientManager;

// Holds all the information for any type of event that may occur
public class Event extends DBObject implements Comparable<Object>
{
	private EventType _type;
	private Calendar _date;
	private int _clientID;
	
	public enum EventType
	{
		Birthday,
		Anniversary,
		Meeting,
		Photoshoot
	}
	
	public Event(EventType type, Calendar date, int clientID)
	{
		_type = type;
		_clientID = clientID;
		_date = date;
	}
	
	public EventType getType()
	{
		return _type;
	}
	
	public Calendar getDate()
	{
		return _date;
	}
	
	// So we can reschedule if needed
	public void setDate(Calendar date)
	{
		_date = date;
	}
	
	public int getClientID()
	{
		return _clientID;
	}
	
	public boolean isAnnual()
	{
		return _type == EventType.Birthday ||
			   _type == EventType.Anniversary;
	}
	
	public String getFormattedString( ClientManager clientManager )
	{
		Client client = clientManager.getClientByID( _clientID );
		String clientName = client.getFullName();
		
		String dateString = String.format("%s/%s/%s", _date.get(Calendar.DAY_OF_MONTH), _date.get(Calendar.MONTH) + 1, _date.get(Calendar.YEAR));
		
		return String.format("%s - %s - %s", dateString, clientName, _type.toString() );		
	}
	
	public String getDescription( ClientManager clientManager )
	{		
		String description = "";
		Client client = clientManager.getClientByID( _clientID );
		String clientName = client.getFullName();
		
		switch (_type)
		{
			case Birthday: 	description = String.format("It is %s's birthday!", clientName);
							break;
			case Meeting: 	description = String.format("You have a meeting with %s.", clientName);
							break;
			case Photoshoot:description = String.format("You have a photoshoot with %s.", clientName);
							break;
			case Anniversary:description = String.format("%s has an anniversary!", clientName );
							break;
			default:		break;
		}
		
		return description;
	}
	
	public int compareTo( Object other )
	{
		if (!(other instanceof Event))
		{
			return 0;
		}
		
		int result = 0;
		Event that = (Event)other;
		
		result = this.getDate().compareTo( that.getDate() );
		
		if (result == 0)
		{
			result = this.getClientID() - that.getClientID();
		}
		
		if (result == 0)
		{
			result = this.getType().compareTo( that.getType() );
		}
		
		return result;
	}
}





