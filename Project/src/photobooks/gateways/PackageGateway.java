package photobooks.gateways;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import photobooks.objects.Package;
import photobooks.objects.Product;

public class PackageGateway<T> implements IGateway<Package> 
{
	//table 
	private static final String PACKAGE_TABLE = "PACKAGE";
	private static final String PRODUCT_PACKAGE_TABLE = "PRODUCTPACKAGE";
	//columns
	//shared 
	private static final String ID = "ID";
	//package table
	private static final String NAME = "NAME";
	private static final String DESCRIPTION = "DESCRIPTION";
	private static final String PRICE = "PRICE";
	private static final String TOTAL_PURCHASED = "TOTALPURCHASED";
	//product package table
	private static final String PRODUCT_ID = "PRODUCT_ID";
	private static final String PACKAGE_ID = "PACKAGE_ID";
	
	private static String EOF = "  ";
	private ResultSet _resultSet;
	private ResultSet _resultSet2;
	private Statement _statement;
	private Statement _statement2;
	private Statement _statement3;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public PackageGateway(IDao dao)
	{
		_dao = dao;
		_statement = _dao.getStatement();
		_statement2 = _dao.getStatement();
		_statement3 = _dao.getStatement();
	}

	public Collection<Package> getAll() 
	{
		Package newPackage = null;
		ArrayList<Package> packages = new ArrayList<Package>();
		int id = 0, totalPurchased = 0, productId = 0;
		String name = EOF, description = EOF;
		double price = 0;
		ArrayList<Product> products = null;
		
		try
		{
			_commandString = "SELECT * FROM " + PACKAGE_TABLE;
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
				name = _resultSet.getString(NAME);
				description = _resultSet.getString(DESCRIPTION);
				price = _resultSet.getDouble(PRICE);
				totalPurchased = _resultSet.getInt(TOTAL_PURCHASED);
				
				// get all products associated with a package
				products = new ArrayList<Product>();
				try
				{
					_commandString = "SELECT * FROM " + PRODUCT_PACKAGE_TABLE + " WHERE " + PACKAGE_ID + " = " + id + "";
					_resultSet2 = _statement2.executeQuery(_commandString);
				}
				catch (Exception e)
				{
					_dao.processSQLError(e);
				}
				try
				{
					while (_resultSet2.next())
					{
						productId = _resultSet2.getInt(PRODUCT_ID);
						products.add(_dao.productGateway().getByID(productId));
					}
					_resultSet2.close();
				}
				catch (Exception e)
				{
					_dao.processSQLError(e);
				}
				
				newPackage = new Package(name, description, price, totalPurchased, products);
				newPackage.setID(id);
				packages.add(newPackage);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return packages;
	}

	public Package getByID(int id) 
	{
		Package newPackage = null;
		int totalPurchased = 0, productId = 0;
		String name = EOF, description = EOF;
		double price = 0;
		ArrayList<Product> products = null;
		
		try
		{
			_commandString = "SELECT * FROM " + PACKAGE_TABLE + " WHERE " + ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);
			
			while (_resultSet.next())
			{
				id = _resultSet.getInt(ID);
				name = _resultSet.getString(NAME);
				description = _resultSet.getString(DESCRIPTION);
				price = _resultSet.getDouble(PRICE);
				totalPurchased = _resultSet.getInt(TOTAL_PURCHASED);
				
				// get all products associated with a package
				products = new ArrayList<Product>();
				try
				{
					_commandString = "SELECT * FROM " + PRODUCT_PACKAGE_TABLE + " WHERE " + PACKAGE_ID + " = " + id + "";
					_resultSet2 = _statement2.executeQuery(_commandString);
				}
				catch (Exception e)
				{
					_dao.processSQLError(e);
				}
				try
				{
					while (_resultSet2.next())
					{
						productId = _resultSet2.getInt(PRODUCT_ID);
						products.add(_dao.productGateway().getByID(productId));
					}
					_resultSet2.close();
				}
				catch (Exception e)
				{
					_dao.processSQLError(e);
				}
				
				newPackage = new Package(name, description, price, totalPurchased, products);
				newPackage.setID(id);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return newPackage;
	}

	public boolean add(Package newObj) 
	{
		String values = null, values2 = null;
		int id = 0;
		ArrayList<Product> products = null;
		boolean result = false;
		
		try
		{
			values = "NULL, '" + newObj.getName()
					+ "', '" + newObj.getDescription()
					+ "', " + newObj.getPrice()
					+ ", " + newObj.getTotalPurchased();
			
			_commandString = "INSERT INTO " + PACKAGE_TABLE + " VALUES(" + values +")";
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
				
				products = newObj.getProducts();
				
				for (Product product : products)
				{
					try
					{
						values2 = "NULL, " + product.getID() + ", " + id;
						
						_commandString = "INSERT INTO " + PRODUCT_PACKAGE_TABLE + " VALUES(" + values2 + ")";
						_updateCount = _statement2.executeUpdate(_commandString);
					}
					catch (Exception e)
					{
						_dao.processSQLError(e);
					}
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

	public void update(Package obj) 
	{
		String values = null, values2 = null, where = null;
		ArrayList<Product> products = null;
		boolean result = false;
		
		try
		{
			values = NAME + " = '" + obj.getName() 
					+ "', " + DESCRIPTION + " = '" + obj.getDescription()
					+ "', " + PRICE + " = " + obj.getPrice()
					+ ", " + TOTAL_PURCHASED +  " = " + obj.getTotalPurchased() + "";
			where = "WHERE " + ID + " = " + obj.getID();
			
			_commandString = "UPDATE " + PACKAGE_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);
			result = _dao.checkWarning(_statement, _updateCount);
			
			if (result)
			{
				//delete all product packages with package id
				try
				{
					_commandString = "DELETE FROM " + PRODUCT_PACKAGE_TABLE + " WHERE " + PACKAGE_ID + " = " + obj.getID() + "";
					_updateCount = _statement2.executeUpdate(_commandString);
				}
				catch (Exception e)
				{
					_dao.processSQLError(e);
				}
				
				products = obj.getProducts();
				
				for (Product product : products)
				{
					try
					{
						values2 = "NULL, " + product.getID() + ", " + obj.getID();
						
						_commandString = "INSERT INTO " + PRODUCT_PACKAGE_TABLE + " VALUES(" + values2 + ")";
						_updateCount = _statement3.executeUpdate(_commandString);
					}
					catch (Exception e)
					{
						_dao.processSQLError(e);
					}
				}
			}
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
	}

	public void delete(Package obj) 
	{
		_dao.globalGateway().delete(PACKAGE_TABLE, obj.getID());	
	}

}
