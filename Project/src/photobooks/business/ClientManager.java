package photobooks.business;

import photobooks.gateways.*;

import java.util.ArrayList;
import java.util.Calendar;
import photobooks.objects.PhoneNumber.PhoneNumberType;
import photobooks.objects.Address.AddressType;
import photobooks.objects.Address;
import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;

public class ClientManager 
{
	private IGateway<Client> _gateway;
	private EventManager _eventManager;

	public ClientManager( IGateway<Client> gateway, EventManager eventManager )
	{
		_gateway = gateway;
		_eventManager = eventManager;
	}

	public void insertStubData()
	{

		Client newClient = new Client("Ryan", "Pope");
		_gateway.add(newClient);
		
		Calendar dob = Calendar.getInstance();
		dob.set(1992, 2, 22);
		
		Calendar ann = Calendar.getInstance();
		ann.set(1950, 11, 12);

		ArrayList<PhoneNumber> numbers = new ArrayList<PhoneNumber>();
		numbers.add(new PhoneNumber(PhoneNumberType.Cellular, "1-204-111-1111"));
		numbers.add(new PhoneNumber(PhoneNumberType.Home, "1-204-222-2222"));
		numbers.add(new PhoneNumber(PhoneNumberType.Work, "1-204-333-3333"));
		numbers.add(new PhoneNumber(PhoneNumberType.Alternative, "1-204-444-4444"));
		
		ArrayList<Address> addresses = new ArrayList<Address>();
		addresses.add(new Address(AddressType.Home, "Home Address"));
		addresses.add(new Address(AddressType.Alternative1, "Alt1 Address"));
		addresses.add(new Address(AddressType.Alternative2, "Alt2 Address"));

		newClient = new Client("Steven", "Morrison", "smore@fire.com", dob, ann, numbers, addresses);
		_gateway.add(newClient);
	}

	public ArrayList<Client> getClientList()
	{	
		return new ArrayList<Client>(_gateway.getAll());
	}
	
	public Client getClientByID( int id )
	{
		return _gateway.getByID( id );
	}

	public void insertClient(Client client)
	{
		_gateway.add(client);
		_eventManager.insertEventsForClient(client);
	}

	public void removeClient(Client client)
	{	
		_eventManager.removeEventsForClient(client);
		_gateway.delete(client);
	}

	public void updateClient(Client client)
	{
		_gateway.update(client);
		_eventManager.updateEventsForClient(client);
	}
}
