package photobooks.gateways;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import photobooks.objects.Address;
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
	private Statement _statement;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public ClientGateway(IDao dao)
	{
		_dao = dao;
		_statement = dao.getStatement();
	}
	
	private Client resultSetToClient(ResultSet results) {
		Client client = null;
		int id = 0;
		String firstName = EOF, lastName = EOF, email = EOF, directory = EOF;
		Calendar birthday = null, anniversary = null;
		Timestamp tempDate;
		ArrayList<PhoneNumber> phoneNumbers = null;
		ArrayList<Address> addresses = null;
		String address = "", city = "", province = "", postalCode = "";
		double accountBalance;
		
		try
		{
			id = results.getInt(ID);
			firstName = results.getString(FIRST_NAME);
			lastName = results.getString(LAST_NAME);
			email = results.getString(EMAIL);
			directory = results.getString(DIRECTORY);
			
			address = results.getString(ADDRESS);
			city = results.getString(CITY);
			province = results.getString(PROVINCE);
			postalCode = results.getString(POSTALCODE);
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
			addresses = new ArrayList<Address>(_dao.addressGateway().getAllWithId(id));		
			
			client = new Client(firstName, lastName, email, (birthday != null) ? (Calendar) birthday.clone() : null, 
					(anniversary != null) ? (Calendar) anniversary.clone() : null, phoneNumbers, address, city,
							province, postalCode, accountBalance, directory, addresses);
			
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
			_commandString = "SELECT * FROM " + CLIENT_TABLE;
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
		ArrayList<Address> addresses = null;
		boolean result = false;
		
		try
		{
			birthday = newObj.getBirthday() != null ? new Date(newObj.getBirthday().getTime().getTime()) : null;
			anniversary = newObj.getAnniversary() != null ? new Date(newObj.getAnniversary().getTime().getTime()) : null;
			
			values = "NULL, '" + newObj.getFirstName() 
					+ "', '" + newObj.getLastName()
					+ "'";
			
			if (birthday != null)
				values += ", '" + birthday.toString() + "'";
			else
				values += ", NULL";
			
			if (anniversary != null)
				values += ", '" + anniversary.toString() + "'";
			else
				values += ", NULL";
			
			values += ", '" + newObj.getEmail()
					+ "', '" + newObj.getDirectory() 
					+ "'";
			
			values += String.format(", '%s', '%s', '%s', '%s', %.6f", newObj.getAddress(), newObj.getCity(), newObj.getProvince(), newObj.getPostalCode(), newObj.getAccountBalance());
			
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
				addresses = newObj.getAddresses();
				
				for (PhoneNumber phoneNumber : phoneNumbers)
				{
					phoneNumber.setClientId(id);
					_dao.phoneNumberGateway().add(phoneNumber);
				}
				
				for (Address address : addresses)
				{
					address.setClientId(id);
					_dao.addressGateway().add(address);
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
		ArrayList<Address> addressesOld = null;
		ArrayList<Address> addressesNew = null;
		boolean result = false;
		
		try
		{
			birthday = obj.getBirthday() != null ? new Date(obj.getBirthday().getTime().getTime()) : null;
			anniversary = obj.getAnniversary() != null ? new Date(obj.getAnniversary().getTime().getTime()) : null;
			
			values = FIRST_NAME + " = '" + obj.getFirstName() 
					+ "', " + LAST_NAME + " = '" + obj.getLastName()
					+ "'";
			
			if (birthday != null)
				values += ", " + BIRTHDAY + " = '" + birthday.toString() + "'";
			else
				values += ", " + BIRTHDAY + " = NULL";
			
			if (anniversary != null)
				values += ", " + ANNIVERSARY + " = '" + anniversary.toString() + "'";
			else
				values += ", " + ANNIVERSARY + " = NULL";
			
			values += ", " + EMAIL + " = '" + obj.getEmail()
					+ "', " + DIRECTORY + " = '" + obj.getDirectory()
					+ "'";
			
			values += String.format(", %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = %.6f", ADDRESS, obj.getAddress(), CITY, obj.getCity(),
					PROVINCE, obj.getProvince(), POSTALCODE, obj.getPostalCode(), ACCOUNTBALANCE, obj.getAccountBalance());
			
			where = "WHERE " + ID + " = " + obj.getID();
			_commandString = "UPDATE " + CLIENT_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);
			result = _dao.checkWarning(_statement, _updateCount);
			
			if (result)
			{							
				phoneNumbersOld = new ArrayList<PhoneNumber>(_dao.phoneNumberGateway().getAllWithId(obj.getID()));	
				phoneNumbersNew = obj.getNumbers();
				addressesOld = new ArrayList<Address>(_dao.addressGateway().getAllWithId(obj.getID()));
				addressesNew = obj.getAddresses();
				
				// delete all phone numbers then insert all, due to the fact that you cannot update a new obj
				for (PhoneNumber phoneNumber : phoneNumbersOld)
				{
					_dao.phoneNumberGateway().delete(phoneNumber);
				}				
				for (PhoneNumber phoneNumber : phoneNumbersNew)
				{ 
					phoneNumber.setClientId(obj.getID());
					_dao.phoneNumberGateway().add(phoneNumber);
				}
				
				 // delete all addresses then insert all, due to the fact that you cannot update a new obj
				for (Address address : addressesOld)
				{
					_dao.addressGateway().delete(address);
				}				
				for (Address address : addressesNew)
				{
					address.setClientId(obj.getID());
					_dao.addressGateway().add(address);
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
