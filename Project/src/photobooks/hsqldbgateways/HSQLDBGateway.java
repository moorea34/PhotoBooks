package photobooks.hsqldbgateways;

import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import photobooks.gateways2.IGateway;
import photobooks.objects.DBObject;

//Used to represent columns and values in a database
class KeyValuePair<T, U> {
	public T key;
	public U value;
	
	public KeyValuePair(T _key, U _value) {
		key = _key;
		value = _value;
	}
}

//Base HSQLDB gateway class
public class HSQLDBGateway<T extends DBObject> implements IGateway<T> {
	
	//Luckily all tables id fields are spelled the same way
	static final String ID = "ID";

	protected HSQLDBDao _dao = null;
	protected Statement _statement = null;
	
	private String _tableName;
	
	
	public HSQLDBGateway(HSQLDBDao dao, String tableName) {
		_dao = dao;
		
		try {
			_statement = _dao.createStatement();
		}
		catch (Exception e) {
			logException(e);
		}
		
		_tableName = tableName;
	}
	
	//Ensures the table exists in the database
	public boolean initialize() {
		return false;
	}
	
	//Wraps strings in quotation marks and handles apostrophes appropriately (returns the string "NULL" if str is null)
	public static String formatSqlString(String str) {
		if (str != null) {
			return String.format("'%s'", str.replace("'", "\\apostrophe"));
		}
		else {
			return "NULL";
		}
	}
	
	//Unhandles apostrophes stored in the database string
	public static String unformatSqlString(String str){
		String result = str;
		
		if (result != null){
			result = result.replace("\\apostrophe", "'");
		}
		
		return result;
	}
	
	//Prints out the exception message
	public static void logException(Exception e) {
		System.out.println(e.getMessage());
	}
	
	//Prints out the warning messages
	protected void logWarnings() {
		try {
			SQLWarning warning = _statement.getWarnings();
			Exception e = warning.getNextWarning();
			
			while (e != null) {
				logException(e);
				e = warning.getNextWarning();
			}
		}
		catch (Exception e) {
			logException(e);
		}
	}
	
	//Returns true if their are no warnings in the statement
	protected static boolean noWarnings(Statement statement)
	{
		boolean result = true;
		
		try
		{
			SQLWarning warning = statement.getWarnings();
			
			if (warning != null) {
				result = false;
			}
		}
		catch (Exception e)
		{
			logException(e);
			result = false;
		}
		
		return result;
	}
	
	//Creates a collection of objects from a result set
	protected Collection<T> parseCollection(ResultSet resultSet) throws Exception {
		ArrayList<T> objs = new ArrayList<T>();
		T currentObj;
		
		while (resultSet.next()) {
			currentObj = fromResultSet(resultSet);
			
			if (currentObj != null) {
				objs.add(currentObj);
			}
		}
		
		return objs;
	}
	
	/*Selects a subset of objects from the table
	 * 
	 * offset: Number of objects to skip
	 * count: Number of objects to get
	 * orderBy: Comma separated list of columns to order by
	 * orderDesc: True to return collection in desc order otherwise ascending (only if orderBy parameter is specified)
	 * where: Where clause of an SQL statement
	 * */
	protected Collection<T> select(int offset, int count, String orderBy, boolean orderDesc, String where) {
		ResultSet resultSet;
		Collection<T> objs = null;
		String _order = "", dir = "ASC", _where = "", limit = String.format("LIMIT %d %d", offset, count);
		String commandString;
		
		if (where != null && !where.isEmpty()) {
			_where = String.format("WHERE %s", where);
		}
		
		if (orderBy != null && !orderBy.isEmpty()) {
			if (orderDesc) {
				dir = "DESC";
			}
			
			_order = String.format("ORDER BY %s %s", orderBy, dir);
		}
		
		commandString = String.format("SELECT * FROM %s %s %s %s", _tableName, _where, _order, limit);
		
		try {
			resultSet = _statement.executeQuery(commandString);
			
			objs = parseCollection(resultSet);
			
			resultSet.close();
		}
		catch (Exception e) {
			logException(e);
		}
		
		return objs;
	}

	/*Selects a subset of objects from the table
	 * 
	 * offset: Number of objects to skip
	 * count: Number of objects to get
	 * orderBy: Column to order by
	 * orderDesc: True to return collection in desc order otherwise ascending (only if orderBy parameter is specified)
	 * */
	@Override
	public Collection<T> select(int offset, int count, String orderBy, boolean orderDesc) {
		return select(offset, count, orderBy, orderDesc, "");
	}

	//Gets a single object by id
	@Override
	public T getByID(int id) {
		T obj = null;
		ResultSet resultSet;
		String commandString = String.format("SELECT * FROM %s WHERE %s = %d", _tableName, ID, id);
		
		try {
			resultSet = _statement.executeQuery(commandString);
			
			if (resultSet.next()) {
				obj = fromResultSet(resultSet);
			}
			
			resultSet.close();
		}
		catch (Exception e) {
			logException(e);
		}
		
		return obj;
	}
		
	//Adds a new object to the table, newObj's id will be set to the new objects id
	@Override
	public boolean add(T newObj) {
		String values = toInsertString(newObj);
		int updateCount;
		int id = 0;
		ResultSet resultSet;
		String commandString = String.format("INSERT INTO %s VALUES(%s)", _tableName, values);
		
		try {
			updateCount = _statement.executeUpdate(commandString);
			
			if (updateCount == 1 && noWarnings(_statement)) {
				commandString = "CALL IDENTITY()";
				resultSet = _statement.executeQuery(commandString);
				
				while (resultSet.next())
				{
					id = resultSet.getInt(1);
				}
				
				newObj.setID(id);

				resultSet.close();
			}
			else {
				//Print out errors and warnings
				logWarnings();
				
				if (updateCount != 1) {
					throw new Exception(String.format("HSQLDB add method updated %d objects!", updateCount));
				}
			}
		}
		catch (Exception e) {
			logException(e);
			return false;
		}
		
		return true;
	}

	//Updates an existing object in the table
	@Override
	public boolean update(T obj) {
		String updateString = toUpdateString(obj);
		int updateCount;
		String commandString = String.format("UPDATE %s SET %s where %s = %d", _tableName, updateString, ID, obj.getID());
		
		try {
			updateCount = _statement.executeUpdate(commandString);
			
			if (updateCount != 1 || !noWarnings(_statement)) {
				//Print out errors and warnings
				logWarnings();
				
				if (updateCount != 1) {
					throw new Exception(String.format("HSQLDB update method updated %d objects!", updateCount));
				}
			}
		}
		catch (Exception e) {
			logException(e);
			return false;
		}
		
		return true;
	}

	//Removes an object from the table
	@Override
	public boolean delete(T obj) {
		String commandString;
		
		try
		{
			commandString = String.format("DELETE FROM %s WHERE %s = %d", _tableName, ID, obj.getID());
			_statement.executeUpdate(commandString);		
		}
		catch (Exception e)
		{
			logException(e);
			return false;
		}
		
		return true;
	}
	
	//Closes the statement object
	@Override
	public void close() {
		if (_statement != null) {
			try {
				_statement.close();
			} catch (Exception e) {
				logException(e);
			}
			
			_statement = null;
		}
	}
	
	//Converts T object to insert string parameters
	protected String toInsertString(T newObj) {
		//Important: Luckily all tables start with ID which is NULL on insert
		String insertString = "NULL";
		Collection<KeyValuePair<String, String>> pairs = toKeyValuePairs(newObj);
		Iterator<KeyValuePair<String, String>> it;
		KeyValuePair<String, String> next;
		
		it = pairs.iterator();
			
		while (it.hasNext()) {
			next = it.next();
			insertString += ", " + next.value;
		}
		
		return insertString;
	}
	
	//Converts T object to update string parameters
	protected String toUpdateString(T obj) {
		String updateString = "";
		Collection<KeyValuePair<String, String>> pairs = toKeyValuePairs(obj);
		Iterator<KeyValuePair<String, String>> it;
		KeyValuePair<String, String> next;
		
		if (pairs.size() > 0) {
			it = pairs.iterator();
			
			next = it.next();
			updateString += next.key + " = " + next.value;
			
			while (it.hasNext()) {
				next = it.next();
				updateString += ", " + next.key + " = " + next.value;
			}
		}
		
		return updateString;
	}
	
	//Methods to be overridden in a sub class
	
	//Creates T object from result set
	protected T fromResultSet(ResultSet resultSet) {
		return null;
	}
	
	//Creates collection of key value pairs representing the T object
	//Key value pairs should be in order that inserting into the database expects them to be
	protected Collection<KeyValuePair<String, String>> toKeyValuePairs(T obj) {
		return null;
	}
}
