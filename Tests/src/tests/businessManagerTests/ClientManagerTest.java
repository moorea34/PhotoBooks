package tests.businessManagerTests;

import java.util.ArrayList;
import java.util.Calendar;

import photobooks.application.Utility;
import photobooks.business2.ClientManager;
import photobooks.gateways2.IDao;
import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;
import photobooks.objects.PhoneNumber.PhoneNumberType;
import junit.framework.TestCase;

public class ClientManagerTest extends TestCase {
	
	static String firstName1 = "'FirstName", firstName2 = "'FirstName2", firstName3 = "'FirstName3";
	static String lastName1 = "LastName'", lastName3 = "LastName3'";
	static String email1 = "hot'mail", email2 = "g'mail", email3 = "'email3";
	static Calendar dob1 = Calendar.getInstance(), dob2 = Calendar.getInstance(), dob3 = Calendar.getInstance();
	static Calendar ann1 = Calendar.getInstance(), ann3 = Calendar.getInstance();
	static ArrayList<PhoneNumber> phoneNumbers1 = new ArrayList<PhoneNumber>(), phoneNumbers2 = new ArrayList<PhoneNumber>();
	static String address1 = "Address'1", address3 = "Address'3";
	static String city1 = "City'1", city3 = "City'3";
	static String province1 = "Province'1", province3 = "Province'3";
	static String postalCode1 = "PostalCode'1", postalCode3 = "PostalCode'3";
	static double accountBalance1 = 5, accountBalance2 = 10, accountBalance3 = 20;
	static String dir1 = "Dir'1", dir2 = "Dir'2", dir3 = "Dir'3";
	
	
	public ClientManagerTest(String arg0) {
		super(arg0);
		
		dob1.set(1990, 4, 8);
		ann1.set(2011, 3, 2);
		
		dob2.set(1991,  7, 6);
		
		dob3.set(1989, 1, 15);
		ann3.set(2012, 12, 31);
		
		phoneNumbers1 = new ArrayList<PhoneNumber>();
		phoneNumbers1.add(new PhoneNumber(PhoneNumberType.Home, "258-'7894"));
		phoneNumbers1.add(new PhoneNumber(PhoneNumberType.Cellular, "381-'7894"));
		phoneNumbers1.add(new PhoneNumber(PhoneNumberType.Alternative, ""));

		phoneNumbers2 = new ArrayList<PhoneNumber>();
		phoneNumbers2.add(new PhoneNumber(PhoneNumberType.Home, "258-'7894"));
	}
	
	boolean arePhoneNumbersEqual(ArrayList<PhoneNumber> nums1, ArrayList<PhoneNumber> nums2) {
		if (nums1.size() == nums2.size()) {
			for (int i = 0; i < nums1.size(); ++i) {
				if ( nums1.get(i).getType() != nums2.get(i).getType() || !nums1.get(i).getNumber().equals(nums2.get(i).getNumber()) ) {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	boolean areClientsEqual(Client c1, Client c2) {
		return c1.getFirstName().equals(c2.getFirstName()) && c1.getLastName().equals(c2.getLastName()) &&
				c1.getEmail().equals(c2.getEmail()) && Utility.calendarsEqual(c1.getBirthday(), c2.getBirthday()) &&
				Utility.calendarsEqual(c1.getAnniversary(), c2.getAnniversary()) &&
				arePhoneNumbersEqual(c1.getNumbers(), c2.getNumbers()) && c1.getAddress().equals(c2.getAddress()) && c1.getCity().equals(c2.getCity()) &&
				c1.getProvince().equals(c2.getProvince()) && c1.getPostalCode().equals(c2.getPostalCode()) && c1.getAccountBalance() == c2.getAccountBalance() &&
				c1.getDirectory().equals(c2.getDirectory());
	}
	
	public void testClientManager() {
		IDao _dao = null;
		ClientManager _clientManager = null;
		Client client1 = new Client(firstName1, lastName1, email1, dob1, ann1, phoneNumbers1, address1, city1, province1, postalCode1, accountBalance1, dir1);
		Client client2 = new Client(firstName2, lastName1, email2, dob2, ann1, phoneNumbers2, address1, city1, province1, postalCode1, accountBalance2, dir2);
		Client clientDB;
		ArrayList<Client> clientsDB;
		
		//Initialize database
		_dao = tests.UnitTests.recreateTestDao();
		_clientManager = new ClientManager(_dao);
		
		assertNotNull(_dao);
		
		assertTrue(_clientManager.insertClient(client1));
		assertTrue(_clientManager.insertClient(client2));
		_dao.dispose();
		
		client1.getNumbers().remove(2);
		
		//Test persistence
		_dao = tests.UnitTests.openTestDao();
		_clientManager = new ClientManager(_dao);
		assertNotNull(_dao);
		
		//Test get by ID
		clientDB = _clientManager.getByID(client1.getID());
		assertNotNull(clientDB);
		assertTrue(areClientsEqual(client1, clientDB));

		clientDB = _clientManager.getByID(client2.getID());
		assertNotNull(clientDB);
		assertTrue(areClientsEqual(client2, clientDB));
		
		//Test select
		clientsDB = _clientManager.select(0, 2, "FirstName", "", true);
		assertTrue(clientsDB != null && clientsDB.size() == 2);
		
		clientsDB = _clientManager.select(0, 2, "LastName", "", true);
		assertTrue(clientsDB != null && clientsDB.size() == 2);

		clientsDB = _clientManager.select(0, 2, "mail", "", true);
		assertTrue(clientsDB != null && clientsDB.size() == 2);

		clientsDB = _clientManager.select(0, 2, "Address", "", true);
		assertTrue(clientsDB != null && clientsDB.size() == 2);

		clientsDB = _clientManager.select(0, 2, "City", "", true);
		assertTrue(clientsDB != null && clientsDB.size() == 2);

		clientsDB = _clientManager.select(0, 2, "Province", "", true);
		assertTrue(clientsDB != null && clientsDB.size() == 2);

		clientsDB = _clientManager.select(0, 2, "PostalCode", "", true);
		assertTrue(clientsDB != null && clientsDB.size() == 2);

		clientsDB = _clientManager.select(0, 2, "Dir", "", true);
		assertTrue(clientsDB != null && clientsDB.size() == 2);

		//Update and delete
		client1.setFirstName(firstName3);
		client1.setLastName(lastName3);
		client1.setEmail(email3);
		client1.setBirthday(dob3);
		client1.setAnniversary(ann1);
		client1.setAddress(address3);
		client1.setCity(city3);
		client1.setProvince(province3);
		client1.setPostalCode(postalCode3);
		client1.setAccountBalance(accountBalance3);
		client1.setDirectory(dir3);
		
		client1.getNumbers().get(1).setNumber("");
		client1.getNumbers().add(new PhoneNumber(PhoneNumberType.Work, "204-456-7863"));
		
		assertTrue(_clientManager.updateClient(client1));
		assertTrue(_clientManager.deleteClient(client2));
		_dao.dispose();
		
		client1.getNumbers().remove(1);
		
		//Test persistence
		_dao = tests.UnitTests.openTestDao();
		_clientManager = new ClientManager(_dao);
		assertNotNull(_dao);

		clientDB = _clientManager.getByID(client1.getID());
		assertNotNull(clientDB);
		assertTrue(areClientsEqual(client1, clientDB));
		
		clientDB = _clientManager.getByID(client2.getID());
		assertNull(clientDB);
		
		_dao.dispose();
	}
}
