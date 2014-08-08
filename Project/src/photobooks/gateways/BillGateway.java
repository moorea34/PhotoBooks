package photobooks.gateways;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import photobooks.objects.Bill;
import photobooks.objects.BillPackage;
import photobooks.objects.BillProduct;
import photobooks.objects.Client;
import photobooks.objects.ITransaction;
import photobooks.objects.Payment;

public class BillGateway<T> implements IConditionalGateway<Bill>
{
	//table
	private static final String BILL_TABLE = "BILL";
	private static final String TRANSACTIONTYPE_TABLE = "TRANSACTION";
	//columns
	private static final String ID = "ID";
	private static final String CLIENT_ID = "CLIENT_ID";
	private static final String TRANSACTIONTYPE_ID = "TRANSACTIONTYPE_ID";
	private static final String DESCRIPTION = "DESCRIPTION";
	private static final String DATE = "DATE";
	private static final String GST = "GST";
	private static final String PST = "PST";
	
	private static String EOF = "  ";
	private ResultSet _resultSet;
	private Statement _statement;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public BillGateway(IDao dao)
	{
		_dao = dao;
		_statement = dao.getStatement();
	}	
	
	public Collection<Bill> getAll() 
	{
		Bill bill = null;
		ArrayList<Bill> bills = new ArrayList<Bill>();
		int id = 0, clientId = 0, typeId = 0;
		Client client = null;
		String description = EOF, typeValue = EOF;
		Calendar date = null;
		Timestamp tempDate;
		Double gst = 0.0, pst = 0.0;
		ArrayList<BillProduct> products = null;
		ArrayList<BillPackage> packages = null;
		ArrayList<Payment> payments = null;
		
		try
		{
			_commandString = "SELECT * FROM " + BILL_TABLE + " ORDER BY " + DATE + " DESC";
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
				clientId = _resultSet.getInt(CLIENT_ID);
				client = _dao.clientGateway().getByID(clientId);
				typeId = _resultSet.getInt(TRANSACTIONTYPE_ID);
				typeValue = _dao.typeGateway().getById(TRANSACTIONTYPE_TABLE, typeId);
				description = _resultSet.getString(DESCRIPTION);
				tempDate = _resultSet.getTimestamp(DATE);
				if (tempDate != null) {
					date = Calendar.getInstance();
					date.setTimeInMillis(tempDate.getTime());
				}
				gst = _resultSet.getDouble(GST);
				pst = _resultSet.getDouble(PST);
				products = new ArrayList<BillProduct>(_dao.billProductGateway().getAllWithId(id));
				packages = new ArrayList<BillPackage>(_dao.billPackageGateway().getAllWithId(id));
				payments = new ArrayList<Payment>(_dao.paymentGateway().getAllWithId(id));
				
				bill = new Bill(client, ITransaction.TransactionType.valueOf(typeValue), description, 
						(date != null) ? (Calendar) date.clone() : null, gst, pst, products, packages, payments);
				bill.setID(id);
				bills.add(bill);
				
				date = null;
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return bills;
	}

	public Bill getByID(int id) 
	{
		Bill bill = null;
		int clientId = 0, typeId = 0;
		Client client = null;
		String description = EOF, typeValue = EOF;
		Calendar date = null;
		Timestamp tempDate;
		Double gst = 0.0, pst = 0.0;
		ArrayList<BillProduct> products = null;
		ArrayList<BillPackage> packages = null;
		ArrayList<Payment> payments = null;
		
		try
		{
			_commandString = "SELECT * FROM " + BILL_TABLE + " WHERE " + ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);
			
			while (_resultSet.next())
			{
				clientId = _resultSet.getInt(CLIENT_ID);
				client = _dao.clientGateway().getByID(clientId);
				typeId = _resultSet.getInt(TRANSACTIONTYPE_ID);
				typeValue = _dao.typeGateway().getById(TRANSACTIONTYPE_TABLE, typeId);
				description = _resultSet.getString(DESCRIPTION);
				tempDate = _resultSet.getTimestamp(DATE);
				if (tempDate != null) {
					date = Calendar.getInstance();
					date.setTimeInMillis(tempDate.getTime());
				}
				gst = _resultSet.getDouble(GST);
				pst = _resultSet.getDouble(PST);
				products = new ArrayList<BillProduct>(_dao.billProductGateway().getAllWithId(id));
				packages = new ArrayList<BillPackage>(_dao.billPackageGateway().getAllWithId(id));
				payments = new ArrayList<Payment>(_dao.paymentGateway().getAllWithId(id));
				
				bill = new Bill(client, ITransaction.TransactionType.valueOf(typeValue), description, 
						(date != null) ? (Calendar) date.clone() : null, gst, pst, products, packages, payments);
				bill.setID(id);
				
				date = null;
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return bill;
	}
	
	public Collection<Bill> getAllWithId(int clientId) {
		Bill bill = null;
		ArrayList<Bill> bills = new ArrayList<Bill>();
		int id = 0, typeId = 0;
		Client client = null;
		String description = EOF, typeValue = EOF;
		Calendar date = null;
		Timestamp tempDate;
		Double gst = 0.0, pst = 0.0;
		ArrayList<BillProduct> products = null;
		ArrayList<BillPackage> packages = null;
		ArrayList<Payment> payments = null;
		
		try
		{
			_commandString = "SELECT * FROM " + BILL_TABLE + " WHERE " + CLIENT_ID + " = " + clientId + " ORDER BY " + DATE + " DESC";
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
				client = _dao.clientGateway().getByID(clientId);
				typeId = _resultSet.getInt(TRANSACTIONTYPE_ID);
				typeValue = _dao.typeGateway().getById(TRANSACTIONTYPE_TABLE, typeId);
				description = _resultSet.getString(DESCRIPTION);
				tempDate = _resultSet.getTimestamp(DATE);
				
				if (tempDate != null) {
					date = Calendar.getInstance();
					date.setTimeInMillis(tempDate.getTime());
				}
				
				gst = _resultSet.getDouble(GST);
				pst = _resultSet.getDouble(PST);
				products = new ArrayList<BillProduct>(_dao.billProductGateway().getAllWithId(id));
				packages = new ArrayList<BillPackage>(_dao.billPackageGateway().getAllWithId(id));
				payments = new ArrayList<Payment>(_dao.paymentGateway().getAllWithId(id));
				
				bill = new Bill(client, ITransaction.TransactionType.valueOf(typeValue), description, 
						(date != null) ? (Calendar) date.clone() : null, gst, pst, products, packages, payments);
				bill.setID(id);
				bills.add(bill);
				
				date = null;
			}
			
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return bills;
	}

	public boolean add(Bill newObj) 
	{
		String values = null;
		int id = 0, typeId = 0, clientId;
		Date date = null;
		ArrayList<BillProduct> products = null;
		ArrayList<BillPackage> packages = null;
		boolean result = false;
		
		try
		{
			typeId = _dao.typeGateway().getByType(TRANSACTIONTYPE_TABLE, newObj.getType().toString());
			clientId = newObj.getClient() != null ? newObj.getClient().getID() : 0;
			date = newObj.getDate() != null ? new Date(newObj.getDate().getTime().getTime()) : null;
		
			values = "NULL, " + clientId 
					+ ", " + typeId 
					+ ", '" + newObj.getDescription()
					+ "'";
			if (date != null)
				values += ", '" + date.toString() + "'";
			else
				values += ", NULL";
			values += ", " + newObj.getGst()
					+ ", " + newObj.getPst()
					+ "";
			
			_commandString = "INSERT INTO " + BILL_TABLE + " VALUES(" + values + ")";
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
				packages = newObj.getPackages();
				
				for (BillProduct product : products)
				{
					product.setBillID(id);
					_dao.billProductGateway().add(product);
				}
				
				for (BillPackage newPackage : packages)
				{
					newPackage.setBillID(id);
					_dao.billPackageGateway().add(newPackage);
				}
			}
//			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return result;
	}

	public void update(Bill obj) 
	{
		String values = null, where = null;
		int typeId = 0, clientId;
		Date date = null;
		ArrayList<BillProduct> productsOld = null;
		ArrayList<BillProduct> productsNew = null;
		ArrayList<BillPackage> packagesOld = null;
		ArrayList<BillPackage> packagesNew = null;
		boolean result = false;
		
		try
		{
			typeId = _dao.typeGateway().getByType(TRANSACTIONTYPE_TABLE, obj.getType().toString());
			clientId = obj.getClient() != null ? obj.getClient().getID() : 0;
			date = obj.getDate() != null ? new Date(obj.getDate().getTime().getTime()) : null;
		
			values = CLIENT_ID + " = " + clientId
					+ ", " + TRANSACTIONTYPE_ID + " = " + typeId
					+ ", " + DESCRIPTION + " = '" + obj.getDescription()
					+ "'";	
			if (date != null)
				values += ", " + DATE + " = '" + date.toString() + "'";
			else
				values += ", " + DATE + " = NULL";
			values += ", " + GST + " = " + obj.getGst()
					+ ", " + PST + " = " + obj.getPst()
					+ "";
			
			where  = "WHERE " + ID + " = " + obj.getID();
			_commandString = "UPDATE " + BILL_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);
			result = _dao.checkWarning(_statement, _updateCount);
			
			if (result)
			{			
				productsOld = new ArrayList<BillProduct>(_dao.billProductGateway().getAllWithId(obj.getID()));			
				productsNew = obj.getProducts();
				
				packagesOld = new ArrayList<BillPackage>(_dao.billPackageGateway().getAllWithId(obj.getID()));
				packagesNew = obj.getPackages();
				
				for (BillProduct product : productsNew)
				{
					product.setBillID(obj.getID());
					
					if (product.getID() == 0)
						_dao.billProductGateway().add(product);
					else
					{
						_dao.billProductGateway().update(product);
						
						for (int i = 0; i < productsOld.size(); ++i)
						{
							if (productsOld.get(i).getID() == product.getID())
							{
								productsOld.remove(i);
								break;
							}
						}
					}
				}
				
				for (BillProduct product : productsOld)
				{
					_dao.billProductGateway().delete(product);
				}				
				
				for (BillPackage newPackage : packagesNew)
				{
					newPackage.setBillID(obj.getID());
					
					if (newPackage.getID() == 0)
						_dao.billPackageGateway().add(newPackage);
					else
					{
						_dao.billPackageGateway().update(newPackage);
						
						for (int i = 0; i < packagesOld.size(); ++i)
						{
							if (packagesOld.get(i).getID() == newPackage.getID())
							{
								packagesOld.remove(i);
								break;
							}
						}
					}
				}
				
				for (BillPackage newPackage : packagesOld)
				{
					_dao.billPackageGateway().delete(newPackage);					
				}
			}
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}	
	}

	public void delete(Bill obj) 
	{
		_dao.globalGateway().delete(BILL_TABLE, obj.getID());		
	}
}
