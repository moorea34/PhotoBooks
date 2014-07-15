package photobooks.gateways;

import java.sql.Statement;


public class GlobalGateway 
{
	private static final String ID = "ID";
	
	private Statement _statement;
	private IDao _dao;
	private String _commandString;
	
	public GlobalGateway(IDao dao)
	{
		_dao = dao;
		_statement = dao.getStatement();
	}
	
	public void delete(String tableName, int id) 
	{		
		try
		{			
			_commandString = "DELETE FROM " + tableName + " WHERE " + ID + " = " + id + "";
			_statement.executeUpdate(_commandString);		
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}			
	}
}
