package photobooks.gateways;

public interface ITypeGateway 
{
	public String getById(String tableName, int id);
	public int getByType(String tableName, String type);
}
