package photobooks.hsqldbgateways;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
	private IGateway<Client> _clientGateway = null;
	private IPhoneNumberGateway _phoneNumberGateway = null;
	
	//Constructor taking the database name to open
	//The database is lazy loaded
	public HSQLDBDao(String dbName) {
		databaseName = dbName;
	}
	
	//Opens the database connection and returns true on success
	private boolean open() {
		String url = "jdbc:hsqldb:database/" + databaseName;
		
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
	}
	
	//Close the connection object with the given command
	private void close(String commandString) {
		if (connection != null) {
			ResultSet resultSet;
			Statement statement;
			
			closeGateways();
			
			try {
				statement = createStatement();
				resultSet = statement.executeQuery(commandString);
				resultSet.close();
			}
			catch (Exception e) {
				System.out.println("Failed to " + commandString + " " + databaseName + ": \n\t" + e.getMessage());
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
	public void commitChanges() {
		close("SHUTDOWN COMPACT");
	}

	//Closes the connection to the database without saving changes
	@Override
	public void dispose() {
		close("SHUTDOWN");
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

}
