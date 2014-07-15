package photobooks.objects;

public class PhoneNumber extends DBObject
{
	private PhoneNumberType _type;
	private String _number;
	private int _clientId;
	
	public enum PhoneNumberType
	{
		Home,
		Cellular,
		Work,
		Alternative
	}
	
	public PhoneNumber(PhoneNumberType type, String number)
	{
		_type = type;
		_number = number;
		//0 means unassigned, real id's will be > 0
		_clientId = 0;
	}
	
	public PhoneNumber(PhoneNumberType type, String number, int clientId)
	{
		_type = type;
		_number = number;
		_clientId = clientId;
	}
	
	public PhoneNumberType getType()
	{
		return _type;
	}
	
	public String getNumber()
	{
		return _number;
	}
	
	public int getClientId()
	{
		return _clientId;
	}
	
	public void setType(PhoneNumberType type)
	{
		_type = type;
	}
	
	public void setNumber(String number)
	{
		_number = number;
	}
	
	public void setClientId(int clientId)
	{
		_clientId = clientId;
	}
}
