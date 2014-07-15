package photobooks.gateways;

public class StubTypeGateway implements ITypeGateway
{
	// will return the name of a type based on an id
	public String getById(String tableName, int id) 
	{
		return null;
	}

	// will return the id of a type based on a name
	public int getByType(String tableName, String type) 
	{
		return 0;
	}

}
