package photobooks.gateways2;

import photobooks.objects.Client;

public interface IDao {
	
	//Save changes to the database
	public void commitChanges();
	//Close the connection to the database (connection is automatically re-opened if necessary)
	public void dispose();
	
	//Gets the client gateway
	public IGateway<Client> clientGateway();

	//Gets the phone number gateway
	public IPhoneNumberGateway phoneNumberGateway();
	
	//Gets the type gateway
	public ITypeGateway typeGateway();
}
