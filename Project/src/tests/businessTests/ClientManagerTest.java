package tests.businessTests;

import java.util.ArrayList;
import java.util.Calendar;

import photobooks.business.*;
import photobooks.gateways.*;
import photobooks.objects.*;
import photobooks.objects.PhoneNumber.PhoneNumberType;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;

public class ClientManagerTest extends TestCase
{
	private ClientManager _clientManager;
	
	public ClientManagerTest(String arg0)
	{
		super(arg0);
	}
	
	// Will create a new client manager and stub gateway for each test
	public void setUp()
	{
		IGateway<Client> stubGateway = new StubGateway<Client>();
		EventManager eventManager = mock( EventManager.class );
		_clientManager = new ClientManager( stubGateway, eventManager );
		
		clearStubDB();
	}
	
	public void clearStubDB()
	{
		for( Client c : _clientManager.getClientList() )
		{
			_clientManager.removeClient(c);
		}
	}

	
	public void testClientManager_Constructor()
	{
		assertNotNull( _clientManager.getClientList() );		
	}
	
	
	public void testClientManager_Insertion()
	{
		Client newClient = new Client("Ryan", "Pope");
		_clientManager.insertClient(newClient);
		
		Calendar dob = Calendar.getInstance();
		dob.set(1992, 2, 22);
		
		Calendar ann = Calendar.getInstance();
		ann.set(1950, 11, 12);

		ArrayList<PhoneNumber> numbers = new ArrayList<PhoneNumber>();
		numbers.add(new PhoneNumber(PhoneNumberType.Cellular, "1-204-111-1111"));
		numbers.add(new PhoneNumber(PhoneNumberType.Home, "1-204-222-2222"));
		numbers.add(new PhoneNumber(PhoneNumberType.Work, "1-204-333-3333"));
		numbers.add(new PhoneNumber(PhoneNumberType.Alternative, "1-204-444-4444"));

		newClient = new Client("Steven", "Morrison", "smore@fire.com", dob, ann, numbers);
		_clientManager.insertClient(newClient);
		
		assertEquals(_clientManager.getClientList().get(0).getFirstName(),"Ryan");
		assertEquals(_clientManager.getClientList().get(0).getLastName(),"Pope");
		
		assertEquals(_clientManager.getClientList().get(1).getFirstName(),"Steven");
		assertEquals(_clientManager.getClientList().get(1).getLastName(),"Morrison");
		
		assertEquals(_clientManager.getClientList().get(1).getNumbers().get(0).getNumber(),"1-204-111-1111");
		
		assertEquals(_clientManager.getClientList().get(1).getBirthday().get(Calendar.YEAR), 1992);
		assertEquals(_clientManager.getClientList().get(1).getBirthday().get(Calendar.MONTH), 2);
		assertEquals(_clientManager.getClientList().get(1).getBirthday().get(Calendar.DAY_OF_MONTH), 22);
		
		assertEquals(_clientManager.getClientList().get(1).getAnniversary().get(Calendar.YEAR), 1950);
		assertEquals(_clientManager.getClientList().get(1).getAnniversary().get(Calendar.MONTH), 11);
		assertEquals(_clientManager.getClientList().get(1).getAnniversary().get(Calendar.DAY_OF_MONTH), 12);

	}
}