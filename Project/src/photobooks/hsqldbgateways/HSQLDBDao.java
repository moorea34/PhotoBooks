package photobooks.hsqldbgateways;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import photobooks.gateways2.*;
import photobooks.objects.Client;

public class HSQLDBDao implements IDao {
	
	//Static variables
	private static final String databaseType = "HSQL";	
	
	private static Object jdbcDriver = null;
	
	//Instance variables
	private String databaseName;
	private Connection connection = null;
	
	//Gateways
	private HSQLDBClientGateway _clientGateway = null;
	private HSQLDBPhoneNumberGateway _phoneNumberGateway = null;
	private HSQLDBTypeGateway _typeGateway = null;
	
	//Constructor taking the database name to open
	//The database is lazy loaded
	public HSQLDBDao(String dbName) {
		databaseName = dbName;
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
			}
		}
		
		try
		{		
			connection = DriverManager.getConnection(url, "SA", "");
			connection.setAutoCommit(false);
		}
		catch (Exception e) 
		{
			System.out.println("Failed to open " + databaseName + ": \n\t" + e.getMessage());
			return false;
		}
		
		System.out.println("Opened " + databaseType +" database " + databaseName);
		return true;
	}
	
	//Releases resources used by the gateways
	private void closeGateways() {
		if (_clientGateway != null) { _clientGateway.close(); _clientGateway = null; }
		if (_phoneNumberGateway != null) { _phoneNumberGateway.close(); _phoneNumberGateway = null; }
		if (_typeGateway != null) { _typeGateway.close(); _typeGateway = null; }
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
				
				System.out.println("Closed " + databaseType + " database " + databaseName);
			}
			catch (Exception e) {
				System.out.println("Failed to close " + databaseName + ":\n\t" + e.getMessage());
			}
		}
	}

	//Commits changes to the database
	@Override
	public boolean commitChanges() {
		if (connection != null) {
			try {
				connection.commit();
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
	
	//Opens the database if it isn't already and creates a Statement object
	public Statement createStatement() throws Exception {
		if (connection != null || open()) {
			return connection.createStatement();
		}
		else {
			throw new Exception("Cannot createStatement: failed to open database!");
		}
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
			
			while (resultSet.next()) {
				if (resultSet.getInt("Count") > 0) {
					result = true;
				}
			}
		}
		catch (Exception e) {
			System.out.println("Failed to open " + databaseName + " to check if table exists:\n\t" + e.getMessage());
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

	//Initialize gateways to ensure tables exist
	public boolean initialize() {
		boolean result = true;
		
		//Create lazy loaded gateway objects
		clientGateway();
		phoneNumberGateway();
		typeGateway();
		
		//Initialize gateway objects one at a time to ensure order
		result = result && _clientGateway.initialize();
		result = result && _phoneNumberGateway.initialize();
		
		if (result == false) {
			rollback();
		}
		
		return result;
	}

	//Gets the client gateway
	@Override
	public IGateway<Client> clientGateway() {
		if (_clientGateway == null) _clientGateway = new HSQLDBClientGateway(this);
		return _clientGateway;
	}

	//Gets the phone number gateway
	@Override
	public IPhoneNumberGateway phoneNumberGateway() {
		if (_phoneNumberGateway == null) _phoneNumberGateway = new HSQLDBPhoneNumberGateway(this);
		return _phoneNumberGateway;
	}

	//Gets the type gateway
	@Override
	public ITypeGateway typeGateway() {
		if (_typeGateway == null) _typeGateway = new HSQLDBTypeGateway(this);
		return _typeGateway;
	}

}
