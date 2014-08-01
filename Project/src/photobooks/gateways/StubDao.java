package photobooks.gateways;

import java.sql.Statement;

import photobooks.objects.*;
import photobooks.objects.Package;

public class StubDao implements IDao
{
	//Initialize gateways
	public GlobalGateway _globalGateway = null;
	public IGateway<Client> _clientGateway = new StubGateway<Client>();
	public IGateway<Product> _productGateway = new StubGateway<Product>();
	public IGateway<Package> _packageGateway = new StubGateway<Package>();
	public IGateway<Bill> _billGateway = new StubGateway<Bill>();
	public IGateway<Event> _eventGateway = new StubGateway<Event>();
	public IConditionalGateway<Payment> _paymentGateway = new StubConditionalGateway<Payment>();
	public IConditionalGateway<PhoneNumber> _phoneNumberGateway = new StubConditionalGateway<PhoneNumber>();
	public IConditionalGateway<BillProduct> _billProductGateway = new StubConditionalGateway<BillProduct>();
	public IConditionalGateway<BillPackage> _billPackageGateway = new StubConditionalGateway<BillPackage>();
	public ITypeGateway _typeGateway = new StubTypeGateway();

	public StubDao()
	{
		
	}

	//Used by DB to commit changes
	public void commitChanges()
	{

	}

	//Used to close connection to DB
	public void dispose()
	{

	}	
	
	//global gateway will be used for gateway functions that are similar for all gateways
	public GlobalGateway globalGateway() 
	{
		return _globalGateway;
	}
	
	//Gateways for each table in the DB
	//Make sure to add them to the IDao as well
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

	public IGateway<Bill> billGateway()
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
	
	// will return a jdbc statement for a gateway to use
	public Statement getStatement() 
	{
		return null;
	}

	// will process an exception that was raised
	public void processSQLError(Exception e) 
	{
	}
	
	// will check to see if insert/update properly
	public boolean checkWarning(Statement _statement, int _updateCount) 
	{
		return false;
	}
}
