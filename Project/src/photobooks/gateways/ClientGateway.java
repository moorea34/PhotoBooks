package photobooks.gateways;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import photobooks.application.Utility;
import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;

public class ClientGateway<T> implements IGateway<Client> 
{
	// table
	private static final String CLIENT_TABLE = "CLIENT";
	
	// columns	
	private static final String ID = "ID";
	private static final String FIRST_NAME = "FIRSTNAME";
	private static final String LAST_NAME = "LASTNAME";
	private static final String EMAIL = "EMAIL";
	private static final String DIRECTORY = "DIRECTORY";
	private static final String BIRTHDAY = "BIRTHDAY";
	private static final String ANNIVERSARY = "ANNIVERSARY";
	
	private static final String ADDRESS = "ADDRESS";
	private static final String CITY = "CITY";
	private static final String PROVINCE = "PROVINCE";
	private static final String POSTALCODE = "POSTALCODE";
	private static final String ACCOUNTBALANCE = "ACCOUNTBALANCE";
	
	private static String EOF = "  ";
	private ResultSet _resultSet;
	private Statement _statement = null;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public ClientGateway(IDao dao)
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
	
	private Client resultSetToClient(ResultSet results) {
		Client client = null;
		int id = 0;
		String firstName = EOF, lastName = EOF, email = EOF, directory = EOF;
		Calendar birthday = null, anniversary = null;
		Timestamp tempDate;
		ArrayList<PhoneNumber> phoneNumbers = null;
		String address = "", city = "", province = "", postalCode = "";
		double accountBalance;
		
		try
		{
			id = results.getInt(ID);
			firstName = Utility.unformatSqlString(results.getString(FIRST_NAME));
			lastName = Utility.unformatSqlString(results.getString(LAST_NAME));
			email = Utility.unformatSqlString(results.getString(EMAIL));
			directory = Utility.unformatSqlString(results.getString(DIRECTORY));
			
			address = Utility.unformatSqlString(results.getString(ADDRESS));
			city = Utility.unformatSqlString(results.getString(CITY));
			province = Utility.unformatSqlString(results.getString(PROVINCE));
			postalCode = Utility.unformatSqlString(results.getString(POSTALCODE));
			accountBalance = results.getDouble(ACCOUNTBALANCE);
			
			tempDate = results.getTimestamp(BIRTHDAY);
			
			if (tempDate != null) {
				birthday = Calendar.getInstance();
				birthday.setTimeInMillis(tempDate.getTime());
			}
			
			tempDate = _resultSet.getTimestamp(ANNIVERSARY);
			
			if (tempDate != null) {
				anniversary = Calendar.getInstance();
				anniversary.setTimeInMillis(tempDate.getTime());
			}
			
			phoneNumbers = new ArrayList<PhoneNumber>(_dao.phoneNumberGateway().getAllWithId(id));
			
			client = new Client(firstName, lastName, email, (birthday != null) ? (Calendar) birthday.clone() : null, 
					(anniversary != null) ? (Calendar) anniversary.clone() : null, phoneNumbers, address, city,
							province, postalCode, accountBalance, directory);
			
			client.setID(id);
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return client;
	}
	
	public Collection<Client> getAll() 
	{
		Client client = null;
		ArrayList<Client> clients = new ArrayList<Client>();
		
		try
		{
			_commandString = "SELECT * FROM " + CLIENT_TABLE + " ORDER BY " + LAST_NAME + ", " + FIRST_NAME;
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
				client = resultSetToClient(_resultSet);
				clients.add(client);
			}
			
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return clients;
	}

	public Client getByID(int id) 
	{
		Client client = null;
		
		try
		{
			_commandString = "SELECT * FROM " + CLIENT_TABLE + " WHERE " + ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);
			
			while (_resultSet.next())
			{
				client = resultSetToClient(_resultSet);
			}
			
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return client;
	}

	public boolean add(Client newObj) 
	{
		String values = null;
		int id = 0;
		Date birthday = null;
		Date anniversary = null;
		ArrayList<PhoneNumber> phoneNumbers = null;
		boolean result = false;
		
		try
		{
			birthday = newObj.getBirthday() != null ? new Date(newObj.getBirthday().getTime().getTime()) : null;
			anniversary = newObj.getAnniversary() != null ? new Date(newObj.getAnniversary().getTime().getTime()) : null;
			
			values = "NULL, '" + Utility.formatSqlString(newObj.getFirstName()) 
					+ "', '" + Utility.formatSqlString(newObj.getLastName())
					+ "'";
			
			if (birthday != null)
				values += ", '" + birthday.toString() + "'";
			else
				values += ", NULL";
			
			if (anniversary != null)
				values += ", '" + anniversary.toString() + "'";
			else
				values += ", NULL";
			
			values += ", '" + Utility.formatSqlString(newObj.getEmail())
					+ "', '" + Utility.formatSqlString(newObj.getDirectory()) 
					+ "'";
			
			values += String.format(", '%s', '%s', '%s', '%s', %.6f", Utility.formatSqlString(newObj.getAddress()), Utility.formatSqlString(newObj.getCity()), Utility.formatSqlString(newObj.getProvince()), Utility.formatSqlString(newObj.getPostalCode()), newObj.getAccountBalance());
			
			_commandString = "INSERT INTO " + CLIENT_TABLE + " VALUES(" + values + ")";
			_updateCount = _statement.executeUpdate(_commandString);
			result = _dao.checkWarning(_statement, _updateCount);
			
			if (result)
			{
				_commandString = "CALL IDENTITY()";
				_resultSet = _statement.executeQuery(_commandString);
				
				while (_resultSet.next())
				{
					id = _resultSet.getInt(1);
				}
				
				newObj.setID(id);
								
				phoneNumbers = newObj.getNumbers();
				
				for (PhoneNumber phoneNumber : phoneNumbers)
				{
					phoneNumber.setClientId(id);
					_dao.phoneNumberGateway().add(phoneNumber);
				}
			}
			
			_resultSet.close();
				
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return result;
	}

	public void update(Client obj) 
	{
		String values = null, where = null;
		Date birthday = null;
		Date anniversary = null;
		ArrayList<PhoneNumber> phoneNumbersOld = null;
		ArrayList<PhoneNumber> phoneNumbersNew = null;
		boolean result = false;
		
		try
		{
			birthday = obj.getBirthday() != null ? new Date(obj.getBirthday().getTime().getTime()) : null;
			anniversary = obj.getAnniversary() != null ? new Date(obj.getAnniversary().getTime().getTime()) : null;
			
			values = FIRST_NAME + " = '" + Utility.formatSqlString(obj.getFirstName()) 
					+ "', " + LAST_NAME + " = '" + Utility.formatSqlString(obj.getLastName())
					+ "'";
			
			if (birthday != null)
				values += ", " + BIRTHDAY + " = '" + birthday.toString() + "'";
			else
				values += ", " + BIRTHDAY + " = NULL";
			
			if (anniversary != null)
				values += ", " + ANNIVERSARY + " = '" + anniversary.toString() + "'";
			else
				values += ", " + ANNIVERSARY + " = NULL";
			
			values += ", " + EMAIL + " = '" + Utility.formatSqlString(obj.getEmail())
					+ "', " + DIRECTORY + " = '" + Utility.formatSqlString(obj.getDirectory())
					+ "'";
			
			values += String.format(", %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = %.6f", ADDRESS, Utility.formatSqlString(obj.getAddress()), CITY, Utility.formatSqlString(obj.getCity()),
					PROVINCE, Utility.formatSqlString(obj.getProvince()), POSTALCODE, Utility.formatSqlString(obj.getPostalCode()), ACCOUNTBALANCE, obj.getAccountBalance());
			
			where = "WHERE " + ID + " = " + obj.getID();
			_commandString = "UPDATE " + CLIENT_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);
			result = _dao.checkWarning(_statement, _updateCount);
			
			if (result)
			{
				boolean exists;
				
				phoneNumbersOld = new ArrayList<PhoneNumber>(_dao.phoneNumberGateway().getAllWithId(obj.getID()));	
				phoneNumbersNew = obj.getNumbers();
				
				// delete all phone numbers then insert all, due to the fact that you cannot update a new obj
				for (PhoneNumber phoneNumber : phoneNumbersOld)
				{
					exists = false;
					
					for (PhoneNumber newNumber : phoneNumbersNew)
					{ 
						if (phoneNumber.getID() == newNumber.getID())
						{
							exists = true;
							break;
						}
					}
					
					if (!exists || phoneNumber.getNumber().trim().length() == 0)
						_dao.phoneNumberGateway().delete(phoneNumber);
				}
				
				for (PhoneNumber phoneNumber : phoneNumbersNew)
				{ 
					if (phoneNumber.getID() == 0 && phoneNumber.getNumber().trim().length() > 0)
					{
						phoneNumber.setClientId(obj.getID());
						_dao.phoneNumberGateway().add(phoneNumber);
					}
					else if (phoneNumber.getNumber().trim().length() > 0)
						_dao.phoneNumberGateway().update(phoneNumber);
				}
			}
				
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
	}

	public void delete(Client obj) 
	{		
		_dao.globalGateway().delete(CLIENT_TABLE, obj.getID());	
	}
}
