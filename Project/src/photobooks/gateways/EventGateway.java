package photobooks.gateways;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import photobooks.objects.Event;

public class EventGateway<T> implements IConditionalGateway<Event>
{
	// table
	private static final String EVENT_TABLE = "EVENT";
	// columns
	private static final String ID = "ID";
	private static final String CLIENT_ID = "CLIENT_ID";
	private static final String DATE = "DATE";
	private static final String EVENTTYPE_ID = "EVENTTYPE_ID";
	
	private static String EOF = "  ";
	private ResultSet _resultSet;
	private Statement _statement;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public EventGateway(IDao dao)
	{
		_dao = dao;
		_statement = dao.getStatement();
	}

	public Collection<Event> getAll() 
	{
		Event event = null;
		ArrayList<Event> events = new ArrayList<Event>();
		int eventId = 0, typeId = 0, clientId = 0;
		String typeValue = EOF;
		Calendar dateValue = null;
		Timestamp tempDate;
		
		try
		{
			_commandString = "SELECT * FROM " + EVENT_TABLE  + "";
			_resultSet = _statement.executeQuery(_commandString);
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		try
		{
			while (_resultSet.next())
			{
				eventId = _resultSet.getInt(ID);
				clientId = _resultSet.getInt(CLIENT_ID);
				tempDate = _resultSet.getTimestamp(DATE);
				if (tempDate != null) {
					dateValue = Calendar.getInstance();
					dateValue.setTimeInMillis(tempDate.getTime());
				}
				typeId = _resultSet.getInt(EVENTTYPE_ID);
				typeValue = _dao.typeGateway().getById(EVENT_TABLE, typeId);	
				
				event = new Event(Event.EventType.valueOf(typeValue), (dateValue != null) ? (Calendar) dateValue.clone() : null, clientId);
				event.setID(eventId);
				events.add(event);
				
				dateValue = null;
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return events;
	}

	public Event getByID(int id) 
	{
		Event event = null;
		int eventId = 0, typeId = 0;
		String typeValue = EOF;
		Calendar dateValue = null;
		Timestamp tempDate;
		
		try
		{
			_commandString = "SELECT * FROM " + EVENT_TABLE + " WHERE " + CLIENT_ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);
	
			while (_resultSet.next())
			{
				eventId = _resultSet.getInt(ID);
				tempDate = _resultSet.getTimestamp(DATE);
				if (tempDate != null) {
					dateValue = Calendar.getInstance();
					dateValue.setTimeInMillis(tempDate.getTime());
				}
				typeId = _resultSet.getInt(EVENTTYPE_ID);
				typeValue = _dao.typeGateway().getById(EVENT_TABLE, typeId);	
				
				event = new Event(Event.EventType.valueOf(typeValue), (dateValue != null) ? (Calendar) dateValue.clone() : null, id);
				event.setID(eventId);
				
				dateValue = null;
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return event;
	}

	public boolean add(Event newObj) 
	{		
		String values = null;
		int id = 0, typeId = 0;
		Date date = null;
		boolean result = false;
		
		try
		{
			typeId = _dao.typeGateway().getByType(EVENT_TABLE, newObj.getType().toString());
			date = newObj.getDate() != null ? new Date(newObj.getDate().getTime().getTime()) : null;
			
			values = "NULL, " + newObj.getClientID() 
					+ "";
			if (date != null)
				values += ", '" + date.toString() + "'";
			else
				values += ", NULL";
			values += ", " + typeId + "";
			
			_commandString = "INSERT INTO " + EVENT_TABLE + " VALUES(" + values + ")";
			_updateCount = _statement.executeUpdate(_commandString);
			result = _dao.checkWarning(_statement, _updateCount);
			
			//set id
			if (result)
			{
				_commandString = "CALL IDENTITY()";
				_resultSet = _statement.executeQuery(_commandString);
				
				while (_resultSet.next())
				{
					id = _resultSet.getInt(1);
				}
				newObj.setID(id);
				_resultSet.close();
			}
				
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return result;
	}

	public void update(Event obj) 
	{
		String values = null, where = null;
		int typeId = 0;
		Date date = null;
		
		try
		{
			typeId = _dao.typeGateway().getByType(EVENT_TABLE, obj.getType().toString());
			date = obj.getDate() != null ? new Date(obj.getDate().getTime().getTime()) : null;
			
			values = CLIENT_ID + " = " + obj.getClientID() 
					+ "";
			if (date != null)
				values += ", " + DATE + " = '" + date.toString() + "'";
			else
				values += ", " + DATE + " = NULL";
			values += ", " + EVENTTYPE_ID + " = " + typeId
					+ "";	
			where = "WHERE " + ID + " = " + obj.getID();
			_commandString = "UPDATE " + EVENT_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);			
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}			
	}

	public void delete(Event obj) 
	{		

		_dao.globalGateway().delete(EVENT_TABLE, obj.getID());		
	}

	public Collection<Event> getAllWithId(int id) 
	{
		Event event = null;
		ArrayList<Event> events = new ArrayList<Event>();
		int eventId = 0, typeId = 0;
		String typeValue = EOF;
		Calendar dateValue = null;
		Timestamp tempDate;
		
		try
		{
			_commandString = "SELECT * FROM " + EVENT_TABLE + " WHERE " + CLIENT_ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		try
		{
			while (_resultSet.next())
			{
				eventId = _resultSet.getInt(ID);
				tempDate = _resultSet.getTimestamp(DATE);
				if (tempDate != null) {
					dateValue = Calendar.getInstance();
					dateValue.setTimeInMillis(tempDate.getTime());
				}
				typeId = _resultSet.getInt(EVENTTYPE_ID);
				typeValue = _dao.typeGateway().getById(EVENT_TABLE, typeId);	
				
				event = new Event(Event.EventType.valueOf(typeValue), (dateValue != null) ? (Calendar) dateValue.clone() : null, id);
				event.setID(eventId);
				events.add(event);
				
				dateValue = null;
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return events;
	}

}
