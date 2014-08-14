package photobooks.gateways;

import java.sql.ResultSet;
import java.sql.Statement;

public class TypeGateway implements ITypeGateway
{
	// table
	private static final String TYPE_TABLE = "TYPE";
	// columns
	private static final String ID = "ID";
	private static final String TYPE = "TYPE";
	
	private static String EOF = "  ";
	private ResultSet _resultSet;
	private Statement _statement = null;
	private IDao _dao;
	private String _commandString;

	public TypeGateway(IDao dao)
	{
		this._dao = dao;
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
	
	public String getById(String tableName, int id)
	{
		String typeValue = EOF;
		
		try
		{
			_commandString = "SELECT " + TYPE + " FROM " + tableName + TYPE_TABLE + " WHERE " + ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);

			while (_resultSet.next())
			{
				typeValue = _resultSet.getString(TYPE);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return typeValue;
	}

	public int getByType(String tableName, String type) 
	{		
		int typeId = 0;
	
		try
		{
			_commandString = "SELECT " + ID + " FROM " + tableName + TYPE_TABLE + " WHERE " + TYPE + " = '" + type + "'";
			_resultSet = _statement.executeQuery(_commandString);
	
			while (_resultSet.next())
			{
				typeId = _resultSet.getInt(ID);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return typeId;
	}
}