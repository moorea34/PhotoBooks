package tests.objectTests;

import java.util.*;

import junit.framework.TestCase;
import photobooks.objects.Address;
import photobooks.objects.Address.AddressType;
import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;
import photobooks.objects.PhoneNumber.PhoneNumberType;

public class ClientTest extends TestCase
{
	private final String FIRST_NAME = "Ryan";
	private final String LAST_NAME = "POPE";
	private final String EMAIL = "rpope@pope.com";
	private final String CELL = "1-204-111-1111";
	private final String HOME_NUM = "1-204-222-2222";
	private final String WORK_NUM = "1-204-333-3333";
	private final String ALT_NUM = "1-204-444-4444";
	private final String HOME_ADDRESS = "Home Address";
	private final String ALT1_ADDRESS = "Alt1 Address";
	private final String ALT2_ADDRESS = "Alt2 Address";
	private final int DOB_YEAR = 1992;
	private final int DOB_MONTH = 11;
	private final int DOB_DAY = 22;
	private final int ANN_YEAR = 1992;
	private final int ANN_MONTH = 11;
	private final int ANN_DAY = 22;
	
	public ClientTest(String arg0)
	{
		super(arg0);
	}
	
	
	public void testClient_NullConstructor()
	{
		Client client;
		
		client = new Client();
		
		assertNotNull(client);
		assertTrue(client.getFirstName().equals(""));
		assertTrue(client.getLastName().equals(""));
	}
	
	
	public void testClient_Constructor()
	{
		Client client;
		
		client = new Client(FIRST_NAME, LAST_NAME);
		
		assertNotNull(client);
		assertTrue(client.getFirstName().equals(FIRST_NAME));
		assertTrue(client.getLastName().equals(LAST_NAME));
	}
	
	
	public void testClient_Constructor1()
	{
		Client client;
		Calendar dob = Calendar.getInstance();
		dob.set(DOB_YEAR, DOB_MONTH, DOB_DAY);
		
		Calendar ann = Calendar.getInstance();
		ann.set(ANN_YEAR, ANN_MONTH, ANN_DAY);
		ArrayList<PhoneNumber> numbers = new ArrayList<PhoneNumber>();
		numbers.add(new PhoneNumber(PhoneNumberType.Cellular, CELL));
		numbers.add(new PhoneNumber(PhoneNumberType.Home, HOME_NUM));
		numbers.add(new PhoneNumber(PhoneNumberType.Work, WORK_NUM));
		numbers.add(new PhoneNumber(PhoneNumberType.Alternative, ALT_NUM));
		
		ArrayList<Address> addresses = new ArrayList<Address>();
		addresses.add(new Address(AddressType.Home, HOME_ADDRESS));
		addresses.add(new Address(AddressType.Alternative1, ALT1_ADDRESS));
		addresses.add(new Address(AddressType.Alternative2, ALT2_ADDRESS));

		client = new Client(FIRST_NAME, LAST_NAME, EMAIL, dob, ann, numbers, addresses);
		
		assertNotNull(client);
		assertTrue(client.getFirstName().equals(FIRST_NAME));
		assertTrue(client.getLastName().equals(LAST_NAME));
		assertTrue(client.getEmail().equals(EMAIL));
		
		assertNotNull(client.getNumbers());
		assertNotNull(client.getNumbers().get(0));
		assertTrue(client.getNumbers().get(0).getType() == PhoneNumberType.Cellular);
		assertTrue(client.getNumbers().get(0).getNumber().equals(CELL));
		
		assertNotNull(client.getNumbers().get(1));
		assertTrue(client.getNumbers().get(1).getType() == PhoneNumberType.Home);
		assertTrue(client.getNumbers().get(1).getNumber().equals(HOME_NUM));
		
		assertNotNull(client.getNumbers().get(2));
		assertTrue(client.getNumbers().get(2).getType() == PhoneNumberType.Work);
		assertTrue(client.getNumbers().get(2).getNumber().equals(WORK_NUM));
		
		assertNotNull(client.getNumbers().get(3));
		assertTrue(client.getNumbers().get(3).getType() == PhoneNumberType.Alternative);
		assertTrue(client.getNumbers().get(3).getNumber().equals(ALT_NUM));
		
		assertNotNull(client.getAddresses());
		assertNotNull(client.getAddresses().get(0));
		assertTrue(client.getAddresses().get(0).getType() == AddressType.Home);
		assertTrue(client.getAddresses().get(0).getAddress().equals(HOME_ADDRESS));
		
		assertNotNull(client.getAddresses().get(1));
		assertTrue(client.getAddresses().get(1).getType() == AddressType.Alternative1);
		assertTrue(client.getAddresses().get(1).getAddress().equals(ALT1_ADDRESS));
		
		assertNotNull(client.getAddresses().get(2));
		assertTrue(client.getAddresses().get(2).getType() == AddressType.Alternative2);
		assertTrue(client.getAddresses().get(2).getAddress().equals(ALT2_ADDRESS));
	}
	
	
	public void testClient_Setters()
	{
		Client client;
		
		client = new Client();
		client.setFirstName(FIRST_NAME);
		client.setLastName(LAST_NAME);
		
		Calendar dob = Calendar.getInstance();
		dob.set(DOB_YEAR, DOB_MONTH, DOB_DAY);
		
		Calendar ann = Calendar.getInstance();
		ann.set(ANN_YEAR, ANN_MONTH, ANN_DAY);
		
		ArrayList<PhoneNumber> numbers = new ArrayList<PhoneNumber>();
		numbers.add(new PhoneNumber(PhoneNumberType.Cellular, CELL));
		client.setNumbers(numbers);
		
		ArrayList<Address> addresses = new ArrayList<Address>();
		addresses.add(new Address(AddressType.Home, HOME_ADDRESS));
		client.setAddresses(addresses);
		
		assertTrue(client.getFirstName().equals(FIRST_NAME));
		assertTrue(client.getLastName().equals(LAST_NAME));
		
		assertNotNull(client.getNumbers());
		assertNotNull(client.getNumbers().get(0));
		assertTrue(client.getNumbers().get(0).getType() == PhoneNumberType.Cellular);
		assertTrue(client.getNumbers().get(0).getNumber().equals(CELL));
		
		assertNotNull(client.getAddresses());
		assertNotNull(client.getAddresses().get(0));
		assertTrue(client.getAddresses().get(0).getType() == AddressType.Home);
		assertTrue(client.getAddresses().get(0).getAddress().equals(HOME_ADDRESS));
	}
}