package photobooks.objects;

import java.util.*;

import photobooks.application.Utility;

// Holds all the information for any type of event that may occur
public class Event extends DBObject
{
	private Calendar _date;
	private String _description = "";
	
	public Event(String description)
	{
		this((Calendar)Calendar.getInstance().clone(), description);
	}
	public Event(Calendar date, String description, int id)
	{
		this(date, description);
		
		setID(id);
	}
	
	public Event(Calendar date, String description)
	{
		_date = date;
		_description = description;
	}
	
	public Calendar getDate()
	{
		return _date;
	}
	
	public void setDate(Calendar date)
	{
		_date = date;
	}
	
	public String getDescription()
	{
		return _description;
	}
	
	public void setDescription(String description)
	{
		_description = description;
	}
	
	public int compareDateTo(Calendar date) {
		return Utility.compareDates(_date, date);
	}
	
	public int compareDateTo(Event other) {
		return compareDateTo(other._date);
	}
}





