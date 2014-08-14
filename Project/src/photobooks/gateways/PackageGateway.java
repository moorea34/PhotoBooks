package photobooks.gateways;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import photobooks.objects.Package;
import photobooks.objects.ProductPackage;

public class PackageGateway<T> implements IGateway<Package> 
{
	//table 
	private static final String PACKAGE_TABLE = "PACKAGE";
	//private static final String PRODUCT_PACKAGE_TABLE = "PRODUCTPACKAGE";
	//columns
	//shared 
	private static final String ID = "ID";
	//package table
	private static final String NAME = "NAME";
	private static final String DESCRIPTION = "DESCRIPTION";
	private static final String PRICE = "PRICE";
	//product package table
	//private static final String PRODUCT_ID = "PRODUCT_ID";
	//private static final String PACKAGE_ID = "PACKAGE_ID";
	
	private static String EOF = "  ";
	private ResultSet _resultSet;
	//private ResultSet _resultSet2;
	private Statement _statement = null;
	//private Statement _statement2;
	//private Statement _statement3;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	private ProductPackageGateway _productPackageGateway = null;
	
	public PackageGateway(IDao dao)
	{
		_dao = dao;
		load();
		//_statement2 = _dao.getStatement();
		//_statement3 = _dao.getStatement();
		
		_productPackageGateway = new ProductPackageGateway(dao);
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
		
		if (_productPackageGateway != null)
			_productPackageGateway.load();
	}

	public Collection<Package> getAll() 
	{
		Package newPackage = null;
		ArrayList<Package> packages = new ArrayList<Package>();
		ArrayList<ProductPackage> productPackages;
		int id = 0;
		String name = EOF, description = EOF;
		double price = 0;
		//ArrayList<Product> products = null;
		
		try
		{
			_commandString = "SELECT * FROM " + PACKAGE_TABLE + " ORDER BY " + NAME;
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

				productPackages = _productPackageGateway.getByPackageID(id);
				
				/*// get all products associated with a package
				products = new ArrayList<Product>();
				
				for (ProductPackage pp : productPackages) {
					products.add(pp.getProduct());
				}*/
				
				newPackage = new Package(name, description, price, productPackages);
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
		ArrayList<ProductPackage> productPackages;
		String name = EOF, description = EOF;
		double price = 0;
		//ArrayList<Product> products = null;
		
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
				
				productPackages = _productPackageGateway.getByPackageID(id);
				
				/*// get all products associated with a package
				products = new ArrayList<Product>();
				
				for (ProductPackage pp : productPackages) {
					products.add(pp.getProduct());
				}*/
				
				newPackage = new Package(name, description, price, productPackages);
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
		String values = null;//, values2 = null;
		int id = 0;
		ArrayList<ProductPackage> products = null;
		boolean result = false;
		
		try
		{
			values = "NULL, '" + newObj.getName()
					+ "', '" + newObj.getDescription()
					+ "', " + newObj.getPrice();
			
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
				
				for (ProductPackage product : products)
				{
					_productPackageGateway.add(product);
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
		String values = null, where = null;
		ArrayList<ProductPackage> newProducts = null, oldProducts = null;
		boolean result = false;
		
		try
		{
			values = NAME + " = '" + obj.getName() 
					+ "', " + DESCRIPTION + " = '" + obj.getDescription()
					+ "', " + PRICE + " = " + obj.getPrice();
			where = "WHERE " + ID + " = " + obj.getID();
			
			_commandString = "UPDATE " + PACKAGE_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);
			result = _dao.checkWarning(_statement, _updateCount);
			
			if (result)
			{
				oldProducts = _productPackageGateway.getByPackageID(obj.getID());
				newProducts = obj.getProducts();
				
				for (ProductPackage product : oldProducts) {
					if (!newProducts.contains(product))
						_productPackageGateway.delete(product);
				}
				
				for (ProductPackage product : newProducts)
				{
					if (product.getID() == 0)
						_productPackageGateway.add(product);
					else
						_productPackageGateway.update(product);
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
