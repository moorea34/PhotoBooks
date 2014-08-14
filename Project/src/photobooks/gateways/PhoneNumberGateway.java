package photobooks.gateways;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import photobooks.objects.PhoneNumber;

public class PhoneNumberGateway<T> implements IConditionalGateway<PhoneNumber>
{
	// table
	private static final String PHONE_TABLE = "PHONE";
	// columns
	private static final String ID = "ID";
	private static final String CLIENT_ID = "CLIENT_ID";
	private static final String PHONE_NUMBER = "PHONENUMBER";
	private static final String PHONETYPE_ID = "PHONETYPE_ID";
	
	private static String EOF = "  ";
	private ResultSet _resultSet;
	private Statement _statement = null;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public PhoneNumberGateway(IDao dao)
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

	public Collection<PhoneNumber> getAll() 
	{
		PhoneNumber phoneNumber = null;
		ArrayList<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
		int phoneNumberId = 0, typeId = 0, clientId = 0;
		String phoneNumberValue = EOF, typeValue = EOF;
		
		try
		{
			_commandString = "SELECT * FROM " + PHONE_TABLE  + "";
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
				phoneNumberId = _resultSet.getInt(ID);
				clientId = _resultSet.getInt(CLIENT_ID);
				phoneNumberValue = _resultSet.getString(PHONE_NUMBER);
				typeId = _resultSet.getInt(PHONETYPE_ID);
				typeValue = _dao.typeGateway().getById(PHONE_TABLE, typeId);
				
				phoneNumber = new PhoneNumber(PhoneNumber.PhoneNumberType.valueOf(typeValue), phoneNumberValue, clientId);
				phoneNumber.setID(phoneNumberId);
				phoneNumbers.add(phoneNumber);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return phoneNumbers;
	}

	public PhoneNumber getByID(int id) 
	{
		PhoneNumber phoneNumber = null;
		int phoneNumberId = 0, typeId = 0;
		String phoneNumberValue = EOF, typeValue = EOF;
		
		try
		{
			_commandString = "SELECT * FROM " + PHONE_TABLE + " WHERE " + ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);
	
			while (_resultSet.next())
			{
				phoneNumberId = _resultSet.getInt(ID);
				phoneNumberValue = _resultSet.getString(PHONE_NUMBER);
				typeId = _resultSet.getInt(PHONETYPE_ID);
				typeValue = _dao.typeGateway().getById(PHONE_TABLE, typeId);	
				
				phoneNumber = new PhoneNumber(PhoneNumber.PhoneNumberType.valueOf(typeValue), phoneNumberValue, id);
				phoneNumber.setID(phoneNumberId);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return phoneNumber;
	}

	public boolean add(PhoneNumber newObj) 
	{		
		String values = null;
		int id = 0, typeId = 0;
		boolean result = false;
		
		try
		{
			typeId = _dao.typeGateway().getByType(PHONE_TABLE, newObj.getType().toString());
			
			values = "NULL, " + newObj.getClientId() 
					+ ", '" + newObj.getNumber()
					+ "', " + typeId
					+ "";			
			_commandString = "INSERT INTO " + PHONE_TABLE + " VALUES(" + values + ")";
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

	public void update(PhoneNumber obj) 
	{
		String values = null, where = null;
		int typeId = 0;
		
		try
		{
			typeId = _dao.typeGateway().getByType(PHONE_TABLE, obj.getType().toString());
			
			values = CLIENT_ID + " = " + obj.getClientId() 
					+ ", " + PHONE_NUMBER + " = '" + obj.getNumber()
					+ "', " + PHONETYPE_ID + " = " + typeId
					+ "";	
			where = "WHERE " + ID + " = " + obj.getID();
			_commandString = "UPDATE " + PHONE_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);			
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}			
	}

	public void delete(PhoneNumber obj) 
	{
		_dao.globalGateway().delete(PHONE_TABLE, obj.getID());			
	}

	public Collection<PhoneNumber> getAllWithId(int id) 
	{
		PhoneNumber phoneNumber = null;
		ArrayList<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
		int phoneNumberId = 0, typeId = 0;
		String phoneNumberValue = EOF, typeValue = EOF;
		
		try
		{
			_commandString = "SELECT * FROM " + PHONE_TABLE + " WHERE " + CLIENT_ID + " = " + id + "";
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
				phoneNumberId = _resultSet.getInt(ID);
				phoneNumberValue = _resultSet.getString(PHONE_NUMBER);
				typeId = _resultSet.getInt(PHONETYPE_ID);
				typeValue = _dao.typeGateway().getById(PHONE_TABLE, typeId);
				
				phoneNumber = new PhoneNumber(PhoneNumber.PhoneNumberType.valueOf(typeValue), phoneNumberValue, id);
				phoneNumber.setID(phoneNumberId);
				phoneNumbers.add(phoneNumber);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return phoneNumbers;
	}

}
