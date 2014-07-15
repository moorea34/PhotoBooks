package photobooks.objects;

import java.util.Calendar;

public interface ITransaction
{
	public enum TransactionType
	{
		Quote(0), Invoice(1);
		
		private final int _value;
		
		public static TransactionType fromInt(int value)
		{
			switch (value)
			{
				case 0:
					return Quote;
				case 1:
					return Invoice;
				default:
					return Quote;
			}
		}
		
		private TransactionType(int value)
		{
			_value = value;
		}
		
		public int getValue()
		{
			return _value;
		}
	}
	
	public int getID();
	
	public String getDisplayName();
	
	public Client getClient();
	public void setClient(Client client);
	
	public String getDescription();
	public void setDescription(String description);
	
	public Calendar getDate();
	public void setDate(Calendar date);
	
	public double total();
	public double affectToBalance();
}
