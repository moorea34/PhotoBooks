package photobooks.objects;

public class Address extends DBObject
{
	private AddressType _type;
	private String _address;
	private int _clientId;
	
	public enum AddressType
	{
		/* Allow multiple addresses */
		Home,
		Alternative1,
		Alternative2
	}
	
	public Address(AddressType type, String address)
	{
		_type = type;
		_address = address;
		//0 means unassigned, real id's will be > 0
		_clientId = 0;
	}
	
	public Address(AddressType type, String address, int clientId)
	{
		_type = type;
		_address = address;
		_clientId = clientId;
	}
	
	public AddressType getType()
	{
		return _type;
	}
	
	public String getAddress()
	{
		return _address;
	}
	
	public int getClientId()
	{
		return _clientId;
	}
	
	public void setType(AddressType type)
	{
		_type = type;
	}
	
	public void setAddress(String address)
	{
		_address = address;
	}
	
	public void setClientId(int clientId)
	{
		_clientId = clientId;
	}
}