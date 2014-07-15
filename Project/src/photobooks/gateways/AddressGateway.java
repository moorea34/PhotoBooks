package photobooks.gateways;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import photobooks.objects.Address;

public class AddressGateway<T> implements IConditionalGateway<Address>  
{	
	// table
	private static final String ADDRESS_TABLE = "ADDRESS";
	// columns
	private static final String ID = "ID";
	private static final String CLIENT_ID = "CLIENT_ID";
	private static final String ADDRESS = "ADDRESS";
	private static final String ADDRESSTYPE_ID = "ADDRESSTYPE_ID";
	
	private static String EOF = "  ";
	private ResultSet _resultSet;
	private Statement _statement;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public AddressGateway(IDao dao)
	{
		_dao = dao;
		_statement = dao.getStatement();
	}

	public Collection<Address> getAll() 
	{
		Address address = null;
		ArrayList<Address> addresses = new ArrayList<Address>();
		int addressId = 0, typeId = 0, clientId = 0;
		String addressValue = EOF, typeValue = EOF;
		
		try
		{
			_commandString = "SELECT * FROM " + ADDRESS_TABLE + "";
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
				addressId = _resultSet.getInt(ID);
				clientId = _resultSet.getInt(CLIENT_ID);
				addressValue = _resultSet.getString(ADDRESS);
				typeId = _resultSet.getInt(ADDRESSTYPE_ID);
				typeValue = _dao.typeGateway().getById(ADDRESS_TABLE, typeId);	
				
				address = new Address(Address.AddressType.valueOf(typeValue), addressValue, clientId);
				address.setID(addressId);
				addresses.add(address);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return addresses;
	}

	public Address getByID(int id) 
	{
		Address address = null;
		int addressId = 0, typeId = 0;
		String addressValue = EOF, typeValue = EOF;
		
		try
		{
			_commandString = "SELECT * FROM " + ADDRESS_TABLE + " WHERE " + ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);
	
			while (_resultSet.next())
			{
				addressId = _resultSet.getInt(ID);
				addressValue = _resultSet.getString(ADDRESS);
				typeId = _resultSet.getInt(ADDRESSTYPE_ID);
				typeValue = _dao.typeGateway().getById(ADDRESS_TABLE, typeId);	
				
				address = new Address(Address.AddressType.valueOf(typeValue), addressValue, id);
				address.setID(addressId);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return address;
	}

	public boolean add(Address newObj) 
	{
		String values = null;
		int id = 0, typeId = 0;
		boolean result = false;
		
		try
		{
			typeId = _dao.typeGateway().getByType(ADDRESS_TABLE, newObj.getType().toString());
			
			values = "NULL, " + newObj.getClientId() 
					+ ", '" + newObj.getAddress()
					+ "', " + typeId
					+ "";			
			_commandString = "INSERT INTO " + ADDRESS_TABLE + " VALUES(" + values + ")";
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

	public void update(Address obj) 
	{
		String values = null, where = null;
		int typeId = 0;
		
		try
		{
			typeId = _dao.typeGateway().getByType(ADDRESS_TABLE, obj.getType().toString());
			
			values = CLIENT_ID + " = " + obj.getClientId() 
					+ ", " + ADDRESS + " = '" + obj.getAddress()
					+ "', " + ADDRESSTYPE_ID + " = " + typeId
					+ "";	
			where = "WHERE " + ID + " = " + obj.getID();
			_commandString = "UPDATE " + ADDRESS_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);			
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}		
	}

	public void delete(Address obj) 
	{	
		_dao.globalGateway().delete(ADDRESS_TABLE, obj.getID());			
	}

	public Collection<Address> getAllWithId(int id) 
	{
		Address address = null;
		ArrayList<Address> addresses = new ArrayList<Address>();
		int addressId = 0, typeId = 0;
		String addressValue = EOF, typeValue = EOF;
		
		try
		{
			_commandString = "SELECT * FROM " + ADDRESS_TABLE + " WHERE " + CLIENT_ID + " = " + id + "";
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
				addressId = _resultSet.getInt(ID);
				addressValue = _resultSet.getString(ADDRESS);
				typeId = _resultSet.getInt(ADDRESSTYPE_ID);
				typeValue = _dao.typeGateway().getById(ADDRESS_TABLE, typeId);		
				
				address = new Address(Address.AddressType.valueOf(typeValue), addressValue, id);
				address.setID(addressId);
				addresses.add(address);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return addresses;
	}
}
