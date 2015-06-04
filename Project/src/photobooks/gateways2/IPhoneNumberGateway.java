package photobooks.gateways2;

import java.util.Collection;

import photobooks.objects.PhoneNumber;

public interface IPhoneNumberGateway extends IGateway<PhoneNumber> {
	public Collection<PhoneNumber> getByClientID(int clientID);
}
