package photobooks.gateways;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import photobooks.objects.BillPackage;

public class BillPackageGateway<T> implements IConditionalGateway<BillPackage> 
{
	//table
	private static final String BILLPACKAGE_TABLE = "BILLPACKAGE";
	//columns
	private static final String ID = "ID";
	private static final String PACKAGE_ID = "PACKAGE_ID";
	private static final String BILL_ID = "BILL_ID";
	private static final String PURCHASEPRICE = "PURCHASEPRICE";
	private static final String AMOUNT = "AMOUNT";
	
	private ResultSet _resultSet;
	private Statement _statement;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public BillPackageGateway(IDao dao)
	{
		_dao = dao;
		_statement = dao.getStatement();
	}
	
	public Collection<BillPackage> getAll() 
	{
		BillPackage billPackage = null;
		ArrayList<BillPackage> billPackages = new ArrayList<BillPackage>();
		int billPackageId = 0, packageId = 0, billId = 0, amount = 0;
		double purchasePrice = 0.0;
		
		try
		{
			_commandString = "SELECT * FROM " + BILLPACKAGE_TABLE + "";
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
				billPackageId = _resultSet.getInt(ID);
				packageId = _resultSet.getInt(PACKAGE_ID);
				billId = _resultSet.getInt(BILL_ID);
				purchasePrice = _resultSet.getDouble(PURCHASEPRICE);
				amount = _resultSet.getInt(AMOUNT);
				
				
				billPackage = new BillPackage(_dao.packageGateway().getByID(packageId), billId, purchasePrice, amount);
				billPackage.setID(billPackageId);
				billPackages.add(billPackage);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return billPackages;
	}

	public BillPackage getByID(int id) 
	{
		BillPackage billPackage = null;
		int packageId = 0, billId = 0, amount = 0;
		double purchasePrice = 0.0;
		
		try
		{
			_commandString = "SELECT * FROM " + BILLPACKAGE_TABLE + " WHERE " + ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);

			while (_resultSet.next())
			{
				packageId = _resultSet.getInt(PACKAGE_ID);
				billId = _resultSet.getInt(BILL_ID);
				purchasePrice = _resultSet.getDouble(PURCHASEPRICE);
				amount = _resultSet.getInt(AMOUNT);
				
				billPackage = new BillPackage(_dao.packageGateway().getByID(packageId), billId, purchasePrice, amount);
				billPackage.setID(id);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return billPackage;
	}

	public boolean add(BillPackage newObj) 
	{
		String values = null;
		int id = 0, billId, packageId;
		boolean result = false;
		
		try
		{
			billId = newObj.getBillID();
			packageId = newObj.getPackage() != null ? newObj.getPackage().getID() : 0;
			
			values = "NULL, " + packageId 
					+ ", " + billId
					+ ", " + newObj.getPrice()
					+ ", " + newObj.getAmount()
					+ "";
			
			_commandString = "INSERT INTO " + BILLPACKAGE_TABLE + " VALUES(" + values + ")";
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

	public void update(BillPackage obj) 
	{
		String values = null, where = null;
		int billId, packageId;
		
		try
		{
			billId = obj.getBillID();
			packageId = obj.getPackage() != null ? obj.getPackage().getID() : 0;
						
			values = PACKAGE_ID + " = " + packageId
					+ ", " + BILL_ID + " = " + billId
					+ ", " + PURCHASEPRICE + " = " + obj.getPrice()
					+ ", " + AMOUNT + " = " + obj.getAmount()
					+ "";
			where = "WHERE " + ID + " = " + obj.getID();
			
			_commandString = "UPDATE " + BILLPACKAGE_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
	}

	public void delete(BillPackage obj) 
	{
		_dao.globalGateway().delete(BILLPACKAGE_TABLE, obj.getID());			
	}

	public Collection<BillPackage> getAllWithId(int id) 
	{
		BillPackage billPackage = null;
		ArrayList<BillPackage> billPackages = new ArrayList<BillPackage>();
		int billPackageId = 0, packageId = 0, billId = 0, amount = 0;
		double purchasePrice = 0.0;
		
		try
		{
			_commandString = "SELECT * FROM " + BILLPACKAGE_TABLE + " WHERE " + BILL_ID + " = " + id + "";
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
				billPackageId = _resultSet.getInt(ID);
				packageId = _resultSet.getInt(PACKAGE_ID);
				billId = _resultSet.getInt(BILL_ID);
				purchasePrice = _resultSet.getDouble(PURCHASEPRICE);
				amount = _resultSet.getInt(AMOUNT);
				
				//cant set bill otherwise infinite loop will occur
				billPackage = new BillPackage(_dao.packageGateway().getByID(packageId), billId, purchasePrice, amount);
				billPackage.setID(billPackageId);
				billPackages.add(billPackage);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return billPackages;
	}
}
