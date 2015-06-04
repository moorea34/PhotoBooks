package photobooks.hsqldbgateways;

import java.util.Collection;

import photobooks.gateways2.IPhoneNumberGateway;
import photobooks.objects.PhoneNumber;

public class HSQLDBPhoneNumberGateway extends HSQLDBGateway<PhoneNumber> implements IPhoneNumberGateway {

	//Constructor
	public HSQLDBPhoneNumberGateway(HSQLDBDao dao) {
		super(dao, "PHONE");
	}

	//Gets all phone numbers for the given client ID
	@Override
	public Collection<PhoneNumber> getByClientID(int clientID) {
		// TODO Auto-generated method stub
		return null;
	}

}
