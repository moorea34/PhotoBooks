package photobooks.objects;

import java.util.ArrayList;
import java.util.Calendar;

import photobooks.objects.PhoneNumber.PhoneNumberType;

public class Client extends DBObject
{
	private String _firstName;
	private String _lastName;
	private String _email;
	private String _directory;
	private Calendar _birthday;
	private Calendar _anniversary;
	private ArrayList<PhoneNumber> _phoneNumbers;
	
	private double _accountBalance;
	private String _address, _city, _province, _postalCode;
	
	public Client()
	{		
		this("", "", "", null, null, new ArrayList<PhoneNumber>(), "");
	}
	
	public Client(String firstName, String lastName)
	{		
		this( firstName, lastName, "", null, null, new ArrayList<PhoneNumber>(), "");
	}
	
	public Client(String firstName, String lastName, String email, Calendar birthday, Calendar anniversary, ArrayList<PhoneNumber> phoneNumbers)
	{
		this( firstName, lastName, email, birthday, anniversary, phoneNumbers, "");
	}
	
	public Client(String firstName, String lastName, String email, Calendar birthday, Calendar anniversary, ArrayList<PhoneNumber> phoneNumbers, String directory)
	{
		this(firstName, lastName, email, birthday, anniversary, phoneNumbers, "", "", "", "", 0, directory);
	}
	
	public Client(String firstName, String lastName, String email, Calendar birthday, Calendar anniversary, ArrayList<PhoneNumber> phoneNumbers, String address, String city, String province, String postalCode, double accountBalance, String directory)
	{
		_accountBalance = accountBalance;
		
		_address = address;
		_city = city;
		_province = province;
		_postalCode = postalCode;
		
		_firstName = firstName;
		_lastName = lastName;
		_email = email;
		_directory = directory;
		_birthday = birthday;
		_anniversary = anniversary;
		_phoneNumbers = phoneNumbers;
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
	
	//Returns the first number by priority from Home -> Cell -> Work -> Alternative
	public PhoneNumber getFirstNumber()
	{
		PhoneNumber first = null;
		
		for (PhoneNumber number : _phoneNumbers)
		{
			if (first == null)
				first = number;
			else if (first.getType() == PhoneNumberType.Alternative)
			{
				if (number.getType() != PhoneNumberType.Alternative)
					first = number;
			}
			else if (first.getType() == PhoneNumberType.Work)
			{
				if (number.getType() == PhoneNumberType.Home || number.getType() == PhoneNumberType.Cellular)
					first = number;
			}
			else if (first.getType() == PhoneNumberType.Cellular)
			{
				if (number.getType() == PhoneNumberType.Home)
					first = number;
			}
			
			if (first.getType() == PhoneNumberType.Home)
				break;
		}
		
		return first;
	}
	
	public ArrayList<PhoneNumber> getNumbers()
	{
		return _phoneNumbers;
	}
	
	public double getAccountBalance() { return _accountBalance; }
	
	public String getAddress() { return _address; }
	public String getCity() { return _city; }
	public String getProvince() { return _province; }
	public String getPostalCode() { return _postalCode; }
	
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
	
	public void setAccountBalance(double accountBalance) { _accountBalance = accountBalance; }
	
	public void setAddress(String address) { _address = address; }
	public void setCity(String city) { _city = city; }
	public void setProvince(String province) { _province = province; }
	public void setPostalCode(String postalCode) { _postalCode = postalCode; }

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
		
		if (_address.toLowerCase().contains(text))
			return true;
		
		if (_city.toLowerCase().contains(text))
			return true;
		
		if (_province.toLowerCase().contains(text))
			return true;
		
		if (_postalCode.toLowerCase().contains(text))
			return true;
		
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
