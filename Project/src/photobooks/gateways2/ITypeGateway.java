package photobooks.gateways2;

public interface ITypeGateway {

	//Gets type name by id
	public String getById(String tableName, int id);
	//Gets id of type name
	public int getByType(String tableName, String type);
	
	//Releases resources used by the gateway (Closed by dao object)
	public void close();
}
