package photobooks.gateways;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import photobooks.objects.BillProduct;

public class BillProductGateway<T> implements IConditionalGateway<BillProduct>
{
	//table
	private static final String BILLPRODUCT_TABLE = "BILLPRODUCT";
	//columns
	private static final String ID = "ID";
	private static final String PRODUCT_ID = "PRODUCT_ID";
	private static final String BILL_ID = "BILL_ID";
	private static final String PURCHASEPRICE = "PURCHASEPRICE";
	private static final String AMOUNT = "AMOUNT";
	
	private ResultSet _resultSet;
	private Statement _statement;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public BillProductGateway(IDao dao)
	{
		_dao = dao;
		_statement = dao.getStatement();
	}
	
	public Collection<BillProduct> getAll() 
	{
		BillProduct billProduct = null;
		ArrayList<BillProduct> billProducts = new ArrayList<BillProduct>();
		int billProductId = 0, productId = 0, billId = 0, amount;
		double purchasePrice = 0.0;
		
		try
		{
			_commandString = "SELECT * FROM " + BILLPRODUCT_TABLE + "";
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
				billProductId = _resultSet.getInt(ID);
				productId = _resultSet.getInt(PRODUCT_ID);
				billId = _resultSet.getInt(BILL_ID);
				purchasePrice = _resultSet.getDouble(PURCHASEPRICE);
				amount = _resultSet.getInt(AMOUNT);
				
				billProduct = new BillProduct(_dao.productGateway().getByID(productId), billId, purchasePrice, amount);
				billProduct.setID(billProductId);
				billProducts.add(billProduct);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return billProducts;
	}

	public BillProduct getByID(int id) 
	{
		BillProduct billProduct = null;
		int productId = 0, billId = 0, amount = 0;
		double purchasePrice = 0.0;
		
		try
		{
			_commandString = "SELECT * FROM " + BILLPRODUCT_TABLE + " WHERE " + ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);
			
			while (_resultSet.next())
			{
				productId = _resultSet.getInt(PRODUCT_ID);
				billId = _resultSet.getInt(BILL_ID);
				purchasePrice = _resultSet.getDouble(PURCHASEPRICE);
				amount = _resultSet.getInt(AMOUNT);
				
				billProduct = new BillProduct(_dao.productGateway().getByID(productId), billId, purchasePrice, amount);
				billProduct.setID(id);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return billProduct;
	}

	public boolean add(BillProduct newObj) 
	{
		String values = null;
		int id = 0, billId, productId;
		boolean result = false;
		
		try
		{
			billId = newObj.getBillID();
			productId = newObj.getProduct() != null ? newObj.getProduct().getID() : 0;
			
			values = "NULL, " + productId 
					+ ", " + billId
					+ ", " + newObj.getPrice()
					+ ", " + newObj.getAmount()
					+ "";
			
			_commandString = "INSERT INTO " + BILLPRODUCT_TABLE + " VALUES(" + values + ")";
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

	public void update(BillProduct obj) 
	{
		String values = null, where = null;
		int billId, productId;
		
		try
		{
			billId = obj.getBillID();
			productId = obj.getProduct() != null ? obj.getProduct().getID() : 0;
						
			values = PRODUCT_ID + " = " + productId
					+ ", " + BILL_ID + " = " + billId
					+ ", " + PURCHASEPRICE + " = " + obj.getPrice()
					+ ", " + AMOUNT + " = " + obj.getAmount()
					+ "";
			where = "WHERE " + ID + " = " + obj.getID();
			
			_commandString = "UPDATE " + BILLPRODUCT_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
	}

	public void delete(BillProduct obj) 
	{

		_dao.globalGateway().delete(BILLPRODUCT_TABLE, obj.getID());		
	}

	public Collection<BillProduct> getAllWithId(int id) 
	{
		BillProduct billProduct = null;
		ArrayList<BillProduct> billProducts = new ArrayList<BillProduct>();
		int billProductId = 0, productId = 0, billId = 0, amount = 0;
		double purchasePrice = 0.0;
		
		try
		{
			_commandString = "SELECT * FROM " + BILLPRODUCT_TABLE + " WHERE " + BILL_ID + " = " + id + "";
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
				billProductId = _resultSet.getInt(ID);
				productId = _resultSet.getInt(PRODUCT_ID);
				billId = _resultSet.getInt(BILL_ID);
				purchasePrice = _resultSet.getDouble(PURCHASEPRICE);
				amount = _resultSet.getInt(AMOUNT);
				
				//cant set bill otherwise infinite loop will occur
				billProduct = new BillProduct(_dao.productGateway().getByID(productId), billId, purchasePrice, amount);
				billProduct.setID(billProductId);
				billProducts.add(billProduct);
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return billProducts;
	}

}
