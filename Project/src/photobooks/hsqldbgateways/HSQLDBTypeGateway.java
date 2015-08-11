package photobooks.hsqldbgateways;

import java.sql.ResultSet;
import java.sql.Statement;

import photobooks.gateways2.ITypeGateway;

public class HSQLDBTypeGateway implements ITypeGateway {
	
	//Table
	private static final String TYPE_TABLE = "TYPE";
	//Columns
	private static final String ID = "ID";
	private static final String TYPE = "TYPE";
		
	
	protected HSQLDBDao _dao;
	protected Statement _statement;
	
	public HSQLDBTypeGateway(HSQLDBDao dao) {
		_dao = dao;
		
		try {
			_statement = _dao.createStatement();
		}
		catch (Exception e) {
			HSQLDBDao.logException(e);
		}
	}

	//Gets type name by id
	@Override
	public String getById(String tableName, int id) {
		String typeValue = null, commandString;
		ResultSet resultSet;
		
		try
		{
			commandString = String.format("SELECT %s FROM %s%s WHERE %s = %d", TYPE, tableName, TYPE_TABLE, ID, id);
			resultSet = _statement.executeQuery(commandString);

			while (resultSet.next())
			{
				typeValue = resultSet.getString(TYPE);
			}
			
			resultSet.close();
		}
		catch (Exception e)
		{
			HSQLDBDao.logException(e);
		}
		
		return typeValue;
	}

	//Gets id from type name
	@Override
	public int getByType(String tableName, String type) {
		int typeId = 0;
		String commandString;
		ResultSet resultSet;
		
		try
		{
			commandString = String.format("SELECT %s FROM %s%s WHERE %s = %s", ID, tableName, TYPE_TABLE, TYPE, HSQLDBGateway.formatSqlString(type));
			resultSet = _statement.executeQuery(commandString);
	
			while (resultSet.next())
			{
				typeId = resultSet.getInt(ID);
			}
			
			resultSet.close();
		}
		catch (Exception e)
		{
			HSQLDBDao.logException(e);
		}
		
		return typeId;
	}

	//Cleans up statement object
	@Override
	public void close() {
		if (_statement != null) {
			try {
				_statement.close();
			}
			catch (Exception e) {
				HSQLDBDao.logException(e);
			}
			
			_statement = null;
		}
	}

}
