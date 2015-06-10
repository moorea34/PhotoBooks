package tests.objectTests;

import java.util.ArrayList;
import java.util.Calendar;

import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;
import photobooks.objects.PhoneNumber.PhoneNumberType;

import junit.framework.TestCase;

//Client object unit test
public class ClientTest extends TestCase {
	
	private final String FIRST_NAME = "Ryan";
	private final String LAST_NAME = "POPE";
	
	private final String EMAIL = "rpope@pope.com";
	
	private final String CELL = "1-204-111-1111";
	private final String HOME_NUM = "1-204-555-5555";
	private final String WORK_NUM = "1-204-333-3333";
	private final String ALT_NUM = "1-204-444-4444";
	
	private final String ADDRESS = "ADDR";
	private final String CITY = "Gotham City";
	private final String PROVINCE = "Berg";
	private final String POSTAL_CODE = "T6W 7I9";
	
	private final String DIRECTORY = "MyPhotos";

	private final int DOB_YEAR = 1992;
	private final int DOB_MONTH = 11;
	private final int DOB_DAY = 22;
	
	private final int ANN_YEAR = 1993;
	private final int ANN_MONTH = 10;
	private final int ANN_DAY = 19;
	
	private final double ACCOUNT_BALANCE = 50.55;
	
	
	public ClientTest(String arg0)
	{
		super(arg0);
	}
	
	public void testClient_NullConstructor()
	{
		Client client;
		
		client = new Client();
		
		assertNotNull(client);
		assertTrue(client.getID() == 0);
		
		assertTrue(client.getFirstName().equals(""));
		assertTrue(client.getLastName().equals(""));
		assertTrue(client.getEmail().equals(""));
		assertNull(client.getBirthday());
		assertNull(client.getAnniversary());
		assertTrue(client.getNumbers().size() == 0);
		assertTrue(client.getAddress().equals(""));
		assertTrue(client.getCity().equals(""));
		assertTrue(client.getProvince().equals(""));
		assertTrue(client.getPostalCode().equals(""));
		assertTrue(client.getAccountBalance() == 0);
		assertTrue(client.getDirectory().equals(""));
	}
	
	
	public void testClient_Constructor()
	{
		Client client;
		
		client = new Client(FIRST_NAME, LAST_NAME);
		
		assertNotNull(client);
		assertTrue(client.getID() == 0);
		
		assertTrue(client.getFirstName().equals(FIRST_NAME));
		assertTrue(client.getLastName().equals(LAST_NAME));
		assertTrue(client.getEmail().equals(""));
		assertNull(client.getBirthday());
		assertNull(client.getAnniversary());
		assertTrue(client.getNumbers().size() == 0);
		assertTrue(client.getAddress().equals(""));
		assertTrue(client.getCity().equals(""));
		assertTrue(client.getProvince().equals(""));
		assertTrue(client.getPostalCode().equals(""));
		assertTrue(client.getAccountBalance() == 0);
		assertTrue(client.getDirectory().equals(""));
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

		client = new Client(FIRST_NAME, LAST_NAME, EMAIL, dob, ann, numbers);
		
		assertNotNull(client);
		assertTrue(client.getID() == 0);
		
		assertTrue(client.getFirstName().equals(FIRST_NAME));
		assertTrue(client.getLastName().equals(LAST_NAME));
		assertTrue(client.getEmail().equals(EMAIL));
		
		assertNotNull(client.getNumbers());
		assertTrue(client.getNumbers().size() == 4);
		
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

		assertTrue(client.getAddress().equals(""));
		assertTrue(client.getCity().equals(""));
		assertTrue(client.getProvince().equals(""));
		assertTrue(client.getPostalCode().equals(""));
		assertTrue(client.getAccountBalance() == 0);
		assertTrue(client.getDirectory().equals(""));
	}
	
	
	public void testClient_Constructor2()
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

		client = new Client(FIRST_NAME, LAST_NAME, EMAIL, dob, ann, numbers, DIRECTORY);
		
		assertNotNull(client);
		assertTrue(client.getID() == 0);
		
		assertTrue(client.getFirstName().equals(FIRST_NAME));
		assertTrue(client.getLastName().equals(LAST_NAME));
		assertTrue(client.getEmail().equals(EMAIL));
		
		assertNotNull(client.getNumbers());
		assertTrue(client.getNumbers().size() == 4);
		
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

		assertTrue(client.getAddress().equals(""));
		assertTrue(client.getCity().equals(""));
		assertTrue(client.getProvince().equals(""));
		assertTrue(client.getPostalCode().equals(""));
		assertTrue(client.getAccountBalance() == 0);
		
		assertTrue(client.getDirectory().equals(DIRECTORY));
	}
	
	
	public void testClient_Constructor3()
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

		client = new Client(FIRST_NAME, LAST_NAME, EMAIL, dob, ann, numbers, ADDRESS, CITY, PROVINCE, POSTAL_CODE, ACCOUNT_BALANCE, DIRECTORY);
		
		assertNotNull(client);
		assertTrue(client.getID() == 0);
		
		assertTrue(client.getFirstName().equals(FIRST_NAME));
		assertTrue(client.getLastName().equals(LAST_NAME));
		assertTrue(client.getEmail().equals(EMAIL));
		
		assertNotNull(client.getNumbers());
		assertTrue(client.getNumbers().size() == 4);
		
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

		assertTrue(client.getAddress().equals(ADDRESS));
		assertTrue(client.getCity().equals(CITY));
		assertTrue(client.getProvince().equals(PROVINCE));
		assertTrue(client.getPostalCode().equals(POSTAL_CODE));
		assertTrue(client.getAccountBalance() == ACCOUNT_BALANCE);
		
		assertTrue(client.getDirectory().equals(DIRECTORY));
	}
	
	
	public void testClient_Setters()
	{
		Client client;
		
		client = new Client();
		client.setID(5);
		assertTrue(client.getID() == 5);
		
		client.setFirstName(FIRST_NAME);
		client.setLastName(LAST_NAME);
		
		Calendar dob = Calendar.getInstance();
		dob.set(DOB_YEAR, DOB_MONTH, DOB_DAY);
		client.setBirthday(dob);
		
		Calendar ann = Calendar.getInstance();
		ann.set(ANN_YEAR, ANN_MONTH, ANN_DAY);
		client.setAnniversary(ann);
		
		ArrayList<PhoneNumber> numbers = new ArrayList<PhoneNumber>();
		numbers.add(new PhoneNumber(PhoneNumberType.Cellular, CELL));
		client.setNumbers(numbers);
		
		client.setAccountBalance(ACCOUNT_BALANCE);
		
		client.setAddress(ADDRESS);
		client.setCity(CITY);
		client.setProvince(PROVINCE);
		client.setPostalCode(POSTAL_CODE);
		
		client.setEmail(EMAIL);
		client.setDirectory(DIRECTORY);
		
		
		assertTrue(client.getFirstName().equals(FIRST_NAME));
		assertTrue(client.getLastName().equals(LAST_NAME));
		
		assertNotNull(client.getNumbers());
		assertTrue(client.getNumbers().size() == 1);
		assertNotNull(client.getNumbers().get(0));
		
		assertTrue(client.getNumbers().get(0).getType() == PhoneNumberType.Cellular);
		assertTrue(client.getNumbers().get(0).getNumber().equals(CELL));
		
		assertTrue(client.getBirthday() == dob);
		assertTrue(client.getAnniversary() == ann);
		
		assertTrue(client.getAccountBalance() == ACCOUNT_BALANCE);
		
		assertTrue(client.getAddress().equals(ADDRESS));
		assertTrue(client.getCity().equals(CITY));
		assertTrue(client.getProvince().equals(PROVINCE));
		assertTrue(client.getPostalCode().equals(POSTAL_CODE));
		
		assertTrue(client.getEmail().equals(EMAIL));
		assertTrue(client.getDirectory().equals(DIRECTORY));
	}
	
	public void testClient_GetFirstNumber()
	{
		Client client;
		
		client = new Client();

		
		ArrayList<PhoneNumber> numbers = client.getNumbers();
		assertNull(client.getFirstNumber());
		
		numbers.add(new PhoneNumber(PhoneNumberType.Alternative, ALT_NUM));
		assertTrue(client.getFirstNumber() == numbers.get(0));

		numbers.add(new PhoneNumber(PhoneNumberType.Work, WORK_NUM));
		assertTrue(client.getFirstNumber() == numbers.get(1));
		
		numbers.add(new PhoneNumber(PhoneNumberType.Cellular, CELL));
		assertTrue(client.getFirstNumber() == numbers.get(2));

		numbers.add(new PhoneNumber(PhoneNumberType.Home, HOME_NUM));
		assertTrue(client.getFirstNumber() == numbers.get(3));
	}
	
	
	public void testClient_SearchAll()
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

		client = new Client(FIRST_NAME, LAST_NAME, EMAIL, dob, ann, numbers, ADDRESS, CITY, PROVINCE, POSTAL_CODE, ACCOUNT_BALANCE, DIRECTORY);
		
		assertTrue(client.searchAll(FIRST_NAME));
		assertTrue(client.searchAll(LAST_NAME));
		
		assertTrue(client.searchAll(EMAIL));
		
		assertTrue(client.searchAll(String.valueOf(DOB_YEAR)));
		assertTrue(client.searchAll("December"));
		assertTrue(client.searchAll(String.valueOf(DOB_DAY)));

		assertTrue(client.searchAll(String.valueOf(ANN_YEAR)));
		assertTrue(client.searchAll("November"));
		assertTrue(client.searchAll(String.valueOf(ANN_DAY)));

		assertTrue(client.searchAll(CELL));
		assertTrue(client.searchAll(HOME_NUM));
		assertTrue(client.searchAll(WORK_NUM));
		assertTrue(client.searchAll(ALT_NUM));

		assertTrue(client.searchAll(ADDRESS));
		assertTrue(client.searchAll(CITY));
		assertTrue(client.searchAll(PROVINCE));
		assertTrue(client.searchAll(POSTAL_CODE));

		assertTrue(client.searchAll(DIRECTORY));
	}
}
