package photobooks.gateways;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import photobooks.objects.Payment;

public class PaymentGateway<T> implements IConditionalGateway<Payment>
{
	//table
	private static final String PAYMENT_TABLE = "PAYMENT";
	private static final String TENDERTYPE_TABLE = "TENDER";
	//columns
	private static final String ID = "ID";
	private static final String CLIENT_ID = "CLIENT_ID";
	private static final String BILL_ID = "BILL_ID";
	private static final String AMOUNT = "AMOUNT";
	private static final String DESCRIPTION = "DESCRIPTION";
	private static final String DATE = "DATE";
	private static final String TENDERTYPE_ID = "TENDERTYPE_ID";
	
	private static String EOF = "  ";
	private ResultSet _resultSet;
	private Statement _statement;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public PaymentGateway(IDao dao)
	{
		_dao = dao;
		_statement = _dao.getStatement();
	}
	
	public Collection<Payment> getAll() 
	{
		Payment payment = null;
		ArrayList<Payment> payments = new ArrayList<Payment>();
		int id = 0, clientId = 0, billId = 0, typeId = 0;
		double amount = 0.0;
		String description = EOF, typeValue = EOF;
		Calendar date = null;
		Timestamp tempDate = null;
		
		try
		{
			_commandString = "SELECT * FROM " + PAYMENT_TABLE + " ORDER BY " + DATE + " DESC";
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
				billId = _resultSet.getInt(BILL_ID);
				amount = _resultSet.getDouble(AMOUNT);
				description = _resultSet.getString(DESCRIPTION);
				tempDate = _resultSet.getTimestamp(DATE);
				if (tempDate != null) {
					date = Calendar.getInstance();
					date.setTimeInMillis(tempDate.getTime());
				}
				typeId = _resultSet.getInt(TENDERTYPE_ID);
				typeValue = _dao.typeGateway().getById(TENDERTYPE_TABLE, typeId);
				
				payment = new Payment(Payment.TenderType.valueOf(typeValue), _dao.clientGateway().getByID(clientId), billId, amount, description, 
						(date != null) ? (Calendar) date.clone() : null);
				payment.setID(id);
				payments.add(payment);
				
				date = null;
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return payments;
	}

	public Payment getByID(int id) 
	{
		Payment payment = null;
		int clientId = 0, billId = 0, typeId = 0;
		double amount = 0.0;
		String description = EOF, typeValue = EOF;
		Calendar date = null;
		Timestamp tempDate = null;
		
		try
		{
			_commandString = "SELECT * FROM " + PAYMENT_TABLE + " WHERE " + ID + " = " + id + "";
			_resultSet = _statement.executeQuery(_commandString);

			while (_resultSet.next())
			{
				clientId = _resultSet.getInt(CLIENT_ID);
				billId = _resultSet.getInt(BILL_ID);
				amount = _resultSet.getDouble(AMOUNT);
				description = _resultSet.getString(DESCRIPTION);
				tempDate = _resultSet.getTimestamp(DATE);
				if (tempDate != null) {
					date = Calendar.getInstance();
					date.setTimeInMillis(tempDate.getTime());
				}
				typeId = _resultSet.getInt(TENDERTYPE_ID);
				typeValue = _dao.typeGateway().getById(TENDERTYPE_TABLE, typeId);

				payment = new Payment(Payment.TenderType.valueOf(typeValue), _dao.clientGateway().getByID(clientId), billId, amount, description, 
						(date != null) ? (Calendar) date.clone() : null);
				payment.setID(id);
				
				date = null;
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return payment;
	}

	public boolean add(Payment newObj) 
	{
		String values = null;
		int id = 0, clientId = 0, typeId = 0;
		Date date = null;
		boolean result = false;
		
		try
		{
			clientId = newObj.getClass() != null ? newObj.getClient().getID() : 0;
			date = newObj.getDate() != null ? new Date(newObj.getDate().getTime().getTime()) : null;
			typeId = _dao.typeGateway().getByType(TENDERTYPE_TABLE, newObj.getTenderType().toString());
		
			values = "NULL, " + clientId
					+ ", " + newObj.getInvoiceId() //invoice id is the same as bill id
					+ ", " + newObj.getAmount()
					+ ", '" + newObj.getDescription()
					+ "'";
			if (date != null)
				values += ", '" + date.toString() + "'";
			else 
				values += ", NULL";
			values += ", " + typeId
					+ "";
			
			_commandString = "INSERT INTO " + PAYMENT_TABLE + " VALUES(" + values + ")";
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
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return result;
	}

	public void update(Payment obj) 
	{
		String values = null, where = null;
		int clientId = 0, typeId = 0;
		Date date = null;
		
		try
		{
			clientId = obj.getClass() != null ? obj.getClient().getID() : 0;
			date = obj.getDate() != null ? new Date(obj.getDate().getTime().getTime()) : null;
			typeId = _dao.typeGateway().getByType(TENDERTYPE_TABLE, obj.getTenderType().toString());
		
			values = CLIENT_ID + " = " + clientId
					+ ", " + BILL_ID + " = " + obj.getInvoiceId() //invoice id is the same as bill id
					+ ", " + AMOUNT + " = " + obj.getAmount()
					+ ", " + DESCRIPTION + " = '" + obj.getDescription()
					+ "'";
			if (date != null)
				values += ", " + DATE + " = '" + date.toString() + "'";
			else 
				values += ", " + DATE + " = NULL";
			values += ", " + TENDERTYPE_ID + " = " + typeId 
					+ "";
			where = "WHERE " + ID + " = " + obj.getID();
			
			_commandString = "UPDATE " + PAYMENT_TABLE + " SET " + values + " " + where;
			_updateCount = _statement.executeUpdate(_commandString);
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
	}

	public void delete(Payment obj) 
	{
		_dao.globalGateway().delete(PAYMENT_TABLE, obj.getID());	
	}

	public Collection<Payment> getAllWithId(int id) 
	{
		Payment payment = null;
		ArrayList<Payment> payments = new ArrayList<Payment>();
		int paymentId = 0, clientId = 0, typeId = 0;
		double amount = 0.0;
		String description = EOF, typeValue = EOF;
		Calendar date = null;
		Timestamp tempDate = null;
		
		try
		{
			_commandString = "SELECT * FROM " + PAYMENT_TABLE + " WHERE " + BILL_ID + " = " + id + "" + " ORDER BY " + DATE + " DESC";
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
				paymentId = _resultSet.getInt(ID);
				clientId = _resultSet.getInt(CLIENT_ID);
				amount = _resultSet.getDouble(AMOUNT);
				description = _resultSet.getString(DESCRIPTION);
				tempDate = _resultSet.getTimestamp(DATE);
				if (tempDate != null) {
					date = Calendar.getInstance();
					date.setTimeInMillis(tempDate.getTime());
				}
				typeId = _resultSet.getInt(TENDERTYPE_ID);
				typeValue = _dao.typeGateway().getById(TENDERTYPE_TABLE, typeId);
				
				payment = new Payment(Payment.TenderType.valueOf(typeValue), _dao.clientGateway().getByID(clientId), id, amount, description, 
						(date != null) ? (Calendar) date.clone() : null);
				payment.setID(paymentId);
				payments.add(payment);
				
				date = null;
			}
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return payments;
	}

}
