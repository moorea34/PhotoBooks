package photobooks.gateways;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import photobooks.application.Utility;
import photobooks.objects.Product;

public class ProductGateway<T> implements IGateway<Product>
{
	//table 
	private static final String PRODUCT_TABLE = "PRODUCT";
	//columns
	private static final String ID = "ID";
	private static final String NAME = "NAME";
	private static final String DESCRIPTION = "DESCRIPTION";
	private static final String PRICE = "PRICE";
	
	private static String EOF = "  ";
	private ResultSet _resultSet;
	private Statement _statement = null;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public ProductGateway(IDao dao)
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

	public Collection<Product> getAll() 
	{
		Product product = null;
		ArrayList<Product> products = new ArrayList<Product>();
		int id = 0;
		String name = EOF, description = EOF;
		double price = 0;
		
		try
		{
			_commandString = "SELECT * FROM " + PRODUCT_TABLE + " ORDER BY " + NAME;
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
				id = _resultSet.getInt(ID);
				name = Utility.unformatSqlString(_resultSet.getString(NAME));
				description = Utility.unformatSqlString(_resultSet.getString(DESCRIPTION));
				price = _resultSet.getDouble(PRICE);
				
				product = new Product(name, description, price);
				product.setID(id);
				products.add(product);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return products;
	}

	public Product getByID(int id) 
	{
		Product product = null;
		String name = EOF, description = EOF;
		double price = 0;
		
		try
		{
			_commandString = "SELECT * FROM " + PRODUCT_TABLE + " WHERE " + ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);
			
			while (_resultSet.next())
			{
				id = _resultSet.getInt(ID);
				name = Utility.unformatSqlString(_resultSet.getString(NAME));
				description = Utility.unformatSqlString(_resultSet.getString(DESCRIPTION));
				price = _resultSet.getDouble(PRICE);
				
				product = new Product(name, description, price);
				product.setID(id);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return product;
	}

	public boolean add(Product newObj) 
	{
		String values = null;
		int id = 0;
		boolean result = false;
		
		try
		{
			values = "NULL, '" + Utility.formatSqlString(newObj.getName()) 
					+ "', '" + Utility.formatSqlString(newObj.getDescription())
					+ "', " + newObj.getPrice();
			
			_commandString = "INSERT INTO " + PRODUCT_TABLE + " VALUES(" + values +")";
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

	public void update(Product obj) 
	{
		String values = null, where = null;
		
		try
		{
			values = NAME + " = '" + Utility.formatSqlString(obj.getName()) 
					+ "', " + DESCRIPTION + " = '" + Utility.formatSqlString(obj.getDescription())
					+ "', " + PRICE + " = " + obj.getPrice();
			
			where = "WHERE " + ID + " = " + obj.getID();
			
			_commandString = "UPDATE " + PRODUCT_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
	}

	public void delete(Product obj) 
	{
		_dao.globalGateway().delete(PRODUCT_TABLE, obj.getID());				
	}

}
