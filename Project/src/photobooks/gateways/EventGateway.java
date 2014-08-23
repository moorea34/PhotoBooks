package photobooks.gateways;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import photobooks.objects.Event;

public class EventGateway<T> implements IGateway<Event>
{
	// table
	private static final String EVENT_TABLE = "EVENT";
	// columns
	private static final String ID = "ID";
	private static final String DATE = "DATE";
	private static final String DESCRIPTION = "DESCRIPTION";
	
	private ResultSet _resultSet;
	private Statement _statement = null;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public EventGateway(IDao dao)
	{
		_dao = dao;
		load();
	}
	
	public void load()
	{
		if (_statement != null)
		{
			try
			{
				_statement.close();
			}
			catch (Exception e)
			{
				_dao.processSQLError(e);
			}
		}
		
		_statement = _dao.getStatement();
	}
	
	private Event resultSetToEvent(ResultSet results) throws SQLException
	{
		Event event = null;
		int eventId = 0;
		String description;
		Calendar dateValue = null;
		Timestamp tempDate;
		
		eventId = _resultSet.getInt(ID);
		description = _resultSet.getString(DESCRIPTION);
		tempDate = _resultSet.getTimestamp(DATE);
		
		if (tempDate != null) {
			dateValue = Calendar.getInstance();
			dateValue.setTimeInMillis(tempDate.getTime());
			dateValue = (Calendar)dateValue.clone();
		}
		
		event = new Event(dateValue, description, eventId);
		
		return event;
	}

	public Collection<Event> getAll() 
	{
		ArrayList<Event> events = new ArrayList<Event>();
		
		try
		{
			_commandString = "SELECT * FROM " + EVENT_TABLE + " ORDER BY " + DATE;
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
				events.add(resultSetToEvent(_resultSet));
			}
			
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return events;
	}
	
	public Collection<Event> getAllInRange(Calendar start, Calendar end, boolean keepNull) 
	{
		ArrayList<Event> events = new ArrayList<Event>();
		String strKeepNull = "";
		
		if (keepNull)
			strKeepNull = String.format("%s IS NULL OR", DATE);
		
		try
		{
			_commandString = String.format("SELECT * FROM %s WHERE (%s (convert(%s, DATE) >= '%s' AND convert(%s, DATE) <= '%s')) ORDER BY %s", EVENT_TABLE, strKeepNull, DATE, new Date(start.getTime().getTime()).toString(), DATE, new Date(end.getTime().getTime()).toString(), DATE);
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
				events.add(resultSetToEvent(_resultSet));
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
		
		try
		{
			_commandString = "SELECT * FROM " + EVENT_TABLE + " WHERE " + ID + " = " + id;
			_resultSet = _statement.executeQuery(_commandString);
	
			while (_resultSet.next())
			{
				event = resultSetToEvent(_resultSet);
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
		int id = 0;
		Date date = null;
		boolean result = false;
		
		try
		{
			date = newObj.getDate() != null ? new Date(newObj.getDate().getTime().getTime()) : null;
			
			values = "NULL";
			
			if (date != null)
				values += ", '" + date.toString() + "'";
			else
				values += ", NULL";
			
			values += ", '" + newObj.getDescription() + "'";
			
			_commandString = String.format("INSERT INTO %s VALUES(%s)", EVENT_TABLE, values);
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
		Date date = null;
		
		try
		{
			date = obj.getDate() != null ? new Date(obj.getDate().getTime().getTime()) : null;
			
			if (date != null)
				values = DATE + " = '" + date.toString() + "'";
			else
				values = DATE + " = NULL";
			
			values += ", " + DESCRIPTION + " = '" + obj.getDescription() + "'";
			
			where = String.format("%s = %d", ID, obj.getID());
			
			_commandString = String.format("UPDATE %s SET %s WHERE %s", EVENT_TABLE, values, where);
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
}
