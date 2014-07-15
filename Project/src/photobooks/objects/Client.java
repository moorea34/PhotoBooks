package photobooks.objects;

import java.util.ArrayList;
import java.util.Calendar;

public class Client extends DBObject
{
	private String _firstName;
	private String _lastName;
	private String _email;
	private String _directory;
	private Calendar _birthday;
	private Calendar _anniversary;
	private ArrayList<PhoneNumber> _phoneNumbers;
	private ArrayList<Address> _addresses;
	
	public Client()
	{		
		this("", "", "", null, null, new ArrayList<PhoneNumber>(), new ArrayList<Address>(), "");
	}
	
	public Client(String firstName, String lastName)
	{		
		this( firstName, lastName, "", null, null, new ArrayList<PhoneNumber>(), new ArrayList<Address>(), "");
	}
	
	public Client(String firstName, String lastName, String email, Calendar birthday, Calendar anniversary, ArrayList<PhoneNumber> phoneNumbers, ArrayList<Address> addresses)
	{
		this( firstName, lastName, email, birthday, anniversary, phoneNumbers, addresses, "");
	}
	
	public Client(String firstName, String lastName, String email, Calendar birthday, Calendar anniversary, ArrayList<PhoneNumber> phoneNumbers, ArrayList<Address> addresses, String directory)
	{
		_firstName = firstName;
		_lastName = lastName;
		_email = email;
		_directory = directory;
		_birthday = birthday;
		_anniversary = anniversary;
		_phoneNumbers = phoneNumbers;
		_addresses = addresses;
	}
	
	
	public String getFormattedName()
	{
		return String.format("%s, %s", _lastName, _firstName );
	}
	
	public String getFirstName()
	{
		return _firstName;
	}
	
	public String getLastName()
	{
		return _lastName;
	}
	
	public Calendar getBirthday()
	{
		return _birthday;
	}
	
	public Calendar getAnniversary()
	{
		return _anniversary;
	}
	
	public String getFullName()
	{
		return _firstName + " " + _lastName;
	}
	
	public ArrayList<PhoneNumber> getNumbers()
	{
		return _phoneNumbers;
	}
	
	public ArrayList<Address> getAddresses()
	{
		return _addresses;
	}
	
	public void setFirstName(String firstName)
	{
		_firstName = firstName;
	}
	
	public void setLastName(String lastName)
	{
		_lastName = lastName;
	}
	
	public void setBirthday(Calendar birthday)
	{
		_birthday = birthday;
	}
	
	public void setAnniversary(Calendar anniversary)
	{
		_anniversary = anniversary;
	}
	
	public void setNumbers(ArrayList<PhoneNumber> numbers)
	{
		_phoneNumbers = numbers;
	}
	
	public void setAddresses(ArrayList<Address> addresses)
	{
		_addresses = addresses;
	}

	public boolean searchAll(String text) 
	{
		if(_firstName.toLowerCase().contains(text))
			return true;
		
		if(_lastName.toLowerCase().contains(text))
			return true;
		
		for(PhoneNumber n : _phoneNumbers)
		{
			if(n.getNumber().toLowerCase().contains(text))
				return true;
		}
		
		for(Address a : _addresses)
		{
			if(a.getAddress().toLowerCase().contains(text))
				return true;
		}
		
		return false;
	}

	public String getEmail() 
	{
		return _email;
	}

	public String getDirectory() 
	{
		return _directory;
	}

	public void setDirectory(String directory) 
	{
		_directory = directory;
	}

	public void setEmail(String email) 
	{
		_email = email;
	}
}
