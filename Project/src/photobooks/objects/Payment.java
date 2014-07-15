package photobooks.objects;

import java.util.Calendar;

public class Payment extends DBObject implements ITransaction
{
	public enum TenderType { Cheque, Cash, Gift, Other }
	
	private TenderType _type;
	private Client _client;
	private int _invoiceId;
	private double _amount;
	private String _description;
	private Calendar _date;
	
	public Payment()
	{
		_type = TenderType.Cash;
		_client = null;
		_invoiceId = 0;
		_description = "";
	}
	
	public Payment(TenderType tenderType, Client client, int invoiceId, double amount, String description, Calendar date)
	{
		setTenderType(tenderType);
		setClient(client);
		setInvoiceId(invoiceId);
		setAmount(amount);
		setDescription(description);
		setDate(date);
	}
	
	public String getDisplayName()
	{
		return "Payment " + getID();
	}
	
	public TenderType getTenderType()
	{
		return _type;
	}
	
	public void setTenderType(TenderType type)
	{
		_type = type;
	}
	
	public Client getClient()
	{
		return _client;
	}

	public void setClient(Client client)
	{
		_client = client;
	}
	
	public int getInvoiceId()
	{
		return _invoiceId;
	}
	
	public void setInvoiceId(int id)
	{
		_invoiceId = id;
	}
	
	public double getAmount()
	{
		return _amount;
	}
	
	public void setAmount(double amount)
	{
		_amount = amount;
	}

	public String getDescription()
	{
		return _description;
	}

	public void setDescription(String description)
	{
		_description = description;
	}
	
	public Calendar getDate()
	{
		return _date;
	}
	
	public void setDate(Calendar date)
	{
		_date = date;
	}

	public double total()
	{
		return _amount;
	}

	public double affectToBalance()
	{
		return total();
	}

}
