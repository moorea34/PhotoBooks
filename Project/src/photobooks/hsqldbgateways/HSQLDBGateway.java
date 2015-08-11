package photobooks.hsqldbgateways;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import photobooks.gateways2.IGateway;
import photobooks.objects.DBObject;

//Base HSQLDB gateway class
public class HSQLDBGateway<T extends DBObject> implements IGateway<T> {
	
	//Luckily all tables id fields are spelled the same way
	static final String ID = "ID";

	protected HSQLDBDao _dao = null;
	protected Statement _statement = null;
	
	private String _tableName;
	

	//Executes the update on the statement and returns the number of records modified
	public static int ExecuteUpdate(String query, Statement statement) {
		int count = -1;
		
		try {
			count = statement.executeUpdate(query);
			HSQLDBDao.logWarnings(statement);
		}
		catch (Exception e) {
			System.out.println(String.format("Error executing update: %s", query));
			HSQLDBDao.logException(e);
		}
		
		return count;
	}
	
	//Executes the query on the statement and returns the result set on success otherwise null
	public static ResultSet ExecuteQuery(String query, Statement statement) {
		ResultSet resultSet = null;
		
		try {
			resultSet = statement.executeQuery(query);
			HSQLDBDao.logWarnings(statement);
		}
		catch (Exception e) {
			System.out.println(String.format("Error executing query: %s", query));
			HSQLDBDao.logException(e);
		}
		
		return resultSet;
	}
	

	//Replaces the character ' with a suitable substitute for the DB
	public static String formatApostrophe(String str) {
		return str.replace("'", "\\apostrophe");
	}
	
	//Wraps strings in quotation marks and handles apostrophes appropriately (returns the string "NULL" if str is null)
	public static String formatSqlString(String str) {
		if (str != null) {
			return String.format("'%s'", formatApostrophe(str));
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
	
	
	public HSQLDBGateway(HSQLDBDao dao, String tableName) {
		_dao = dao;
		_tableName = tableName;
		
		try {
			_statement = _dao.createStatement();
			HSQLDBDao.logWarnings(_statement);
		}
		catch (Exception e) {
			HSQLDBDao.logException(e);
		}
	}
	
	//Ensures the table exists in the database
	@Override
	public boolean initialize() {
		return false;
	}

	//Closes the statement object
	@Override
	public void close() {
		if (_statement != null) {
			try {
				_statement.close();
			} catch (Exception e) {
				HSQLDBDao.logException(e);
			}
			
			_statement = null;
		}
	}
	
	//Creates a collection of objects from a result set
	protected ArrayList<T> parseCollection(ResultSet resultSet) throws Exception {
		ArrayList<T> objs = new ArrayList<T>();
		T currentObj;

		while (resultSet.next()) {
			currentObj = fromResultSet(resultSet);
			HSQLDBDao.logWarnings(resultSet);
			
			if (currentObj != null) {
				objs.add(currentObj);
			}
			else {
				throw new Exception("Error parsing object from result set!");
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
	protected ArrayList<T> select(int offset, int count, String orderBy, boolean orderDesc, String where) {
		ResultSet resultSet;
		ArrayList<T> objs = null;
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
		
		commandString = String.format("SELECT %s * FROM %s %s %s", limit, _tableName, _where, _order);
		
		try {
			resultSet = _statement.executeQuery(commandString);
			HSQLDBDao.logWarnings(_statement);
			
			objs = parseCollection(resultSet);
			
			resultSet.close();
		}
		catch (Exception e) {
			System.out.println(String.format("Error executing: %s", commandString));
			HSQLDBDao.logException(e);
		}
		
		return objs;
	}

	/* Selects a subset of objects from the table
	 * 
	 * offset: Number of objects to skip
	 * count: Number of objects to get
	 * filter: String to filter objects by (gateway implementation specific)
	 * orderBy: Comma separated list of columns to order by
	 * orderDesc: True to return collection in descending order otherwise ascending (only if orderBy parameter is specified)
	 * 
	 * Returns the list of items found. On error returns null.
	 * */
	@Override
	public ArrayList<T> select(int offset, int count, String filter, String orderBy, boolean orderDesc) {
		return null;
	}

	//Gets a single object by id
	@Override
	public T getByID(int id) {
		T obj = null;
		ResultSet resultSet;
		String commandString = String.format("SELECT * FROM %s WHERE %s = %d", _tableName, ID, id);
		
		try {
			resultSet = _statement.executeQuery(commandString);
			HSQLDBDao.logWarnings(_statement);
			
			if (resultSet.next()) {
				obj = fromResultSet(resultSet);
				HSQLDBDao.logWarnings(resultSet);
			}
			
			resultSet.close();
		}
		catch (Exception e) {
			System.out.println(String.format("Error executing get by id statement: %s", commandString));
			HSQLDBDao.logException(e);
		}
		
		return obj;
	}
	
	protected boolean add(String query, T newObj) {
		String commandString;
		int updateCount;
		int id = 0;
		ResultSet resultSet;
		
		try {
			updateCount = _statement.executeUpdate(query);
			HSQLDBDao.logWarnings(_statement);
			
			if (updateCount == 1) {
				commandString = "CALL IDENTITY()";
				
				resultSet = _statement.executeQuery(commandString);
				HSQLDBDao.logWarnings(_statement);
				
				while (resultSet.next())
				{
					id = resultSet.getInt(1);
					HSQLDBDao.logWarnings(resultSet);
				}
				
				if (id == 0) {
					throw new Exception("Failed to obtain new object ID!");
				}
				
				newObj.setID(id);

				resultSet.close();
			}
			else {
				throw new Exception(String.format("HSQLDB add method updated %d objects!", updateCount));
			}
		}
		catch (Exception e) {
			System.out.println(String.format("Error executing add statement: %s", query));
			HSQLDBDao.logException(e);
			return false;
		}
		
		return true;
	}
	
	//Adds a new object to the table, newObj's id will be set to the new objects id
	@Override
	public boolean add(T newObj) {
		return false;
	}

	//Updates an existing object in the table
	protected boolean update(String query) {
		int updateCount;
		
		try {
			updateCount = _statement.executeUpdate(query);
			HSQLDBDao.logWarnings(_statement);
			
			if (updateCount != 1) {
				throw new Exception(String.format("HSQLDB update method updated %d objects!", updateCount));
			}
		}
		catch (Exception e) {
			System.out.println(String.format("Error executing update statement: %s", query));
			HSQLDBDao.logException(e);
			return false;
		}
		
		return true;
	}

	//Updates an existing object in the table
	@Override
	public boolean update(T obj) {
		return false;
	}

	//Removes an object from the table
	@Override
	public boolean delete(T obj) {
		if (obj != null) {
			String commandString = String.format("DELETE FROM %s WHERE %s = %d", _tableName, ID, obj.getID());
			return ExecuteUpdate(commandString, _statement) == 1;
		}
		
		return false;
	}
	
	//Creates T object from result set
	protected T fromResultSet(ResultSet resultSet) {
		return null;
	}
}
