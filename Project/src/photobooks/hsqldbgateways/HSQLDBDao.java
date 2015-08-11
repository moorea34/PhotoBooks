package photobooks.hsqldbgateways;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import photobooks.gateways2.*;
import photobooks.objects.Client;

public class HSQLDBDao implements IDao {
	
	//Static variables
	private static Object jdbcDriver = null;
	
	//Instance variables
	private String databaseName = null;
	private Connection connection = null;
	
	//Gateways
	private IGateway<Client> _clientGateway = null;
	private IPhoneNumberGateway _phoneNumberGateway = null;
	private ITypeGateway _typeGateway = null;
	
	
	public static boolean dbExists(String dbFolder) {
		String dbFile = dbFolder + "/PhotoBooks.script";
		File file = new File(dbFile);
		
		if (file.exists() && !file.isDirectory()) {
			return true;
		}
		else {
			return false;
		}
	}

	//Loads an existing database (returns null if it does not exist or fails to open)
	public static HSQLDBDao loadDB(String dbFolder) {
		HSQLDBDao dao = null;
		
		if (dbExists(dbFolder)) {
			dao = new HSQLDBDao(dbFolder);
			
			if (!dao.open()) {
				dao.dispose();
				dao = null;
			}
		}
		
		return dao;
	}

	//Creates a new database (returns null if the folder is not empty or fails to create the database)
	public static HSQLDBDao createDB(String dbFolder) {
		HSQLDBDao dao = null;
		
		if (photobooks.application.Utility.isDirectoryEmpty(dbFolder)) {
			dao = new HSQLDBDao(dbFolder);
			
			if (!dao.open()) {
				dao.dispose();
				dao = null;
			}
			else if (!dao.commitChanges()) {
				dao.dispose();
				dao = null;
			}
		}
		
		return dao;
	}

	//Prints out the exception message
	public static void logException(Exception e) {
		System.out.println(e.getMessage());
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
	
	//Prints out the warning messages
	public static void logWarnings(Connection connection) {
		try {
			SQLWarning warning = connection.getWarnings();

			if (warning != null) {
				System.out.println("Connection warnings");
			}
			
			while (warning != null) {
				logException(warning);
				warning = warning.getNextWarning();
			}
		}
		catch (Exception e) {
			logException(e);
		}
	}

	//Prints out the warning messages
	public static void logWarnings(Statement statement) {
		try {
			try {
				SQLWarning warning = statement.getWarnings();

				if (warning != null) {
					System.out.println("Connection warnings");
				}
				
				while (warning != null) {
					logException(warning);
					warning = warning.getNextWarning();
				}
			}
			catch (Exception e) {
				logException(e);
			}
		}
		catch (Exception e) {
			logException(e);
		}
	}
	
	//Prints out the warning messages
	public static void logWarnings(ResultSet resultSet) {
		try {
			try {
				SQLWarning warning = resultSet.getWarnings();

				if (warning != null) {
					System.out.println("Connection warnings");
				}
				
				while (warning != null) {
					logException(warning);
					warning = warning.getNextWarning();
				}
			}
			catch (Exception e) {
				logException(e);
			}
		}
		catch (Exception e) {
			logException(e);
		}
	}
	
	
	private HSQLDBDao(String dbFolder) {
		databaseName = dbFolder + "/PhotoBooks";
	}
	
	//Opens the database connection and returns true on success
	private boolean open() {
		String url = "jdbc:hsqldb:" + databaseName;
		
		if (jdbcDriver == null) {
			try {
				jdbcDriver = Class.forName("org.hsqldb.jdbcDriver").newInstance();
			}
			catch (Exception e) {
				System.out.println("Failed to load HSQLDB JDBC driver:\n\t" + e.getMessage());
				return false;
			}
		}
		
		try
		{		
			connection = DriverManager.getConnection(url, "SA", "");
			logWarnings(connection);
			connection.setAutoCommit(false);
			logWarnings(connection);
		}
		catch (Exception e) 
		{
			System.out.println("Failed to open " + databaseName + ": \n\t" + e.getMessage());
			return false;
		}
		
		if (!createGateways()) {
			System.out.println("Failed to create and initialize gateways");
			return false;
		}
		
		System.out.println("Opened HSQL database " + databaseName);
		return true;
	}
	
	//Creates all gateway objects
	private boolean createGateways() {
		_clientGateway = new HSQLDBClientGateway(this);
		_phoneNumberGateway = new HSQLDBPhoneNumberGateway(this);
		_typeGateway = new HSQLDBTypeGateway(this);
		
		return initialize();
	}

	//Initialize gateways to ensure tables exist and were able to create a statement object
	private boolean initialize() {
		boolean result = true;
		
		//Initialize gateway objects one at a time to ensure order
		result = result && _clientGateway.initialize();
		result = result && _phoneNumberGateway.initialize();
		
		if (result == false) {
			rollback();
		}
		
		return result;
	}
	
	//Close the connection object with the given command
	private void close(String commandString) {
		if (connection != null) {
			ResultSet resultSet;
			Statement statement = null;
			
			closeGateways();
			
			try {
				statement = createStatement();
				resultSet = statement.executeQuery(commandString);
				resultSet.close();
			}
			catch (Exception e) {
				System.out.println("Failed to " + commandString + " " + databaseName + ": \n\t" + e.getMessage());
			}
			
			if (statement != null) {
				try {
					statement.close();
				}
				catch (Exception e) {
				}
			}
		
			try {
				connection.close();
				connection = null;
				
				System.out.println("Closed HSQL database " + databaseName);
			}
			catch (Exception e) {
				System.out.println("Failed to close " + databaseName + ":\n\t" + e.getMessage());
			}
		}
	}

	//Releases resources used by the gateways
	private void closeGateways() {
		if (_clientGateway != null) { _clientGateway.close(); _clientGateway = null; }
		if (_phoneNumberGateway != null) { _phoneNumberGateway.close(); _phoneNumberGateway = null; }
		if (_typeGateway != null) { _typeGateway.close(); _typeGateway = null; }
	}

	//Commits changes to the database
	@Override
	public boolean commitChanges() {
		if (connection != null) {
			try {
				connection.commit();
				logWarnings(connection);
			}
			catch (SQLException e) {
				System.out.println("Error committing changes: " + e.getMessage());
				return false;
			}
		}
		
		return true;
	}

	//Rollback to before the current transaction was started
	@Override
	public boolean rollback() {
		if (connection != null) {
			try {
				connection.rollback();
				logWarnings(connection);
			}
			catch (SQLException e) {
				System.out.println("Error during rollback: " + e.getMessage());
				return false;
			}
		}
		
		return true;
	}
	
	//Closes the database connection
	@Override
	public void dispose() {
		close("SHUTDOWN COMPACT");
	}
	
	//Creates a Statement object (fails if the connection is not open)
	public Statement createStatement() throws Exception {
		Statement statement = null;
		
		if (connection != null) {
			statement = connection.createStatement();
			logWarnings(connection);
		}
		else {
			throw new Exception("Cannot createStatement: database not open!");
		}
		
		return statement;
	}
	
	//Returns true if the table exists within the database (tableName is case sensitive)
	public boolean tableExists(String tableName) {
		Statement statement = null;
		String query = String.format("SELECT COUNT(*) as \"Count\" From INFORMATION_SCHEMA.SYSTEM_TABLES Where TABLE_NAME = '%s'", tableName);
		ResultSet resultSet = null;
		boolean result = false;
		
		try {
			statement = createStatement();
			resultSet = statement.executeQuery(query);
			logWarnings(connection);
			
			while (resultSet.next()) {
				if (resultSet.getInt("Count") > 0) {
					result = true;
				}

				logWarnings(resultSet);
			}
		}
		catch (Exception e) {
			System.out.println("Failed to check if table exists:\n\t" + e.getMessage());
		}

		
		//Close result set because the error might have happened after
		if (resultSet != null) {
			try {
				resultSet.close();
			}
			catch (Exception e) {
			}
		}
		
		//Close statement because the error might have been the query or result set
		if (statement != null) {
			try {
				statement.close();
			}
			catch (Exception e) {
			}
		}
		
		return result;
	}
	

	//Gets the client gateway
	@Override
	public IGateway<Client> clientGateway() { return _clientGateway; }

	//Gets the phone number gateway
	public IPhoneNumberGateway phoneNumberGateway() { return _phoneNumberGateway; }

	//Gets the type gateway
	public ITypeGateway typeGateway() { return _typeGateway; }
}
