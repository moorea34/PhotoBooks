package photobooks.gateways;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLWarning;

import photobooks.application.Utility;
import photobooks.objects.*;
import photobooks.objects.Package;

public class Dao implements IDao 
{
	//Initialize gateways
	private GlobalGateway _globalGateway;
	private IGateway<Client> _clientGateway;
	private IGateway<Product> _productGateway;
	private IGateway<Package> _packageGateway;
	private IConditionalGateway<Bill> _billGateway;
	private IGateway<Event> _eventGateway;
	private IConditionalGateway<Payment> _paymentGateway;
	private IConditionalGateway<PhoneNumber> _phoneNumberGateway;
	private IConditionalGateway<BillProduct> _billProductGateway;
	private IConditionalGateway<BillPackage> _billPackageGateway;
	private ITypeGateway _typeGateway;
	
	private static final String databaseName = "PhotoBooks";
	private static final String databaseType = "HSQL";	

	private Statement statement;
	private Connection connection;
	private ResultSet resultSet;
	
	private String commandString;
	
	public Dao()
	{
		open();
		
		_globalGateway = new GlobalGateway(this);
		_clientGateway = new ClientGateway<Client>(this);
		_productGateway = new ProductGateway<Product>(this);
		_packageGateway = new PackageGateway<Package>(this);
		_billGateway = new BillGateway<Bill>(this);
		_eventGateway = new EventGateway<Event>(this);
		_paymentGateway = new PaymentGateway<Payment>(this);
		_phoneNumberGateway = new PhoneNumberGateway<PhoneNumber>(this);
		_billProductGateway = new BillProductGateway<BillProduct>(this);
		_billPackageGateway = new BillPackageGateway<BillPackage>(this);
		_typeGateway = new TypeGateway(this);
	}
	
	private void open()
	{
		String url;
		
		try
		{
			Class.forName("org.hsqldb.jdbcDriver").newInstance();
			url = "jdbc:hsqldb:database/" + databaseName ;			
			connection = DriverManager.getConnection(url, "SA", "");
			statement = connection.createStatement();
		}
		catch (Exception e) 
		{
			processSQLError(e);
		}
		
		System.out.println("Opened " + databaseType +" database " + databaseName);
	}

	public void commitChanges() 
	{
		dispose();
		open();
		
		reloadGateways();
	}
	
	public void reloadGateways()
	{
		_globalGateway.load();
		_clientGateway.load();
		_productGateway.load();
		_packageGateway.load();
		_billGateway.load();
		_eventGateway.load();
		_paymentGateway.load();
		_phoneNumberGateway.load();
		_billProductGateway.load();
		_billPackageGateway.load();
		_typeGateway.load();
	}
	
	public void restore(String src) throws Exception
	{
		dispose();
		
		Utility.copyDatabase(src, "./database");
		
		open();
		reloadGateways();
	}

	public void dispose() 
	{
		try
		{
			commandString = "SHUTDOWN COMPACT";
			resultSet = statement.executeQuery(commandString);
			resultSet.close();
		}
		catch (Exception e)
		{
			processSQLError(e);
		}
		
		try
		{
			connection.close();
		}
		catch (Exception e)
		{
			processSQLError(e);
		}
		
		System.out.println("Closed " + databaseType + " database " + databaseName);
	}

	public GlobalGateway globalGateway()
	{
		return _globalGateway;
	}
	
	public IGateway<Client> clientGateway()
	{
		return _clientGateway;
	}

	public IGateway<Product> productGateway()
	{
		return _productGateway;
	}

	public IGateway<Package> packageGateway()
	{
		return _packageGateway;
	}

	public IConditionalGateway<Bill> billGateway()
	{
		return _billGateway;
	}
	
	public IGateway<Event> eventGateway()
	{
		return _eventGateway;
	}

	public IConditionalGateway<Payment> paymentGateway()
	{
		return _paymentGateway;
	}
	
	public IConditionalGateway<PhoneNumber> phoneNumberGateway()
	{
		return _phoneNumberGateway;
	}	

	public IConditionalGateway<BillProduct> billProductGateway() 
	{
		return _billProductGateway;
	}

	public IConditionalGateway<BillPackage> billPackageGateway() 
	{
		return _billPackageGateway;
	}
	
	public ITypeGateway typeGateway()
	{
		return _typeGateway;
	}
	
	public Statement getStatement()
	{
		return statement;
	}
	
	public boolean checkWarning(Statement statement, int updateCount)
	{
		boolean result = true;
		
		try
		{
			SQLWarning warning = statement.getWarnings();
			if (warning != null)
			{
				result = false;
			}
		}
		catch (Exception e)
		{
			processSQLError(e);
			result = false;
		}
		if (updateCount != 1)
		{
			result = false;
		}
		
		return result;
	}
	
	public void processSQLError(Exception e)
	{
		String result;
		result = "SQL Error: " + e.getMessage();
		System.out.println(result);
//		e.printStackTrace();
	}


}
