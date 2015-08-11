package photobooks.gateways2;

import photobooks.objects.Client;

public interface IDao {
	
	//Save changes to the database
	public boolean commitChanges();
	//Roll back changes to before beginning of transaction
	public boolean rollback();
	//Close database connection
	public void dispose();
	
	//Gets the client gateway
	public IGateway<Client> clientGateway();
}
