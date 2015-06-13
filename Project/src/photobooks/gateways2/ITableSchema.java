package photobooks.gateways2;

import java.sql.ResultSet;
import java.util.ArrayList;

import photobooks.hsqldbgateways.HSQLDBDao;

public interface ITableSchema<T> {
	
	//Gets the name of the table
	public String Name();
	//Gets the string to create the table
	public String Create();
	//Gets the initial values in the table which may be null
	public String InitialValues();
	
	public String select(int offset, int count, String filter, String orderBy, boolean orderDesc);
	public String GetByIDStatement(T obj);
	public String InsertionStatement(T obj);
	public String UpdateStatement(T obj);
	public String DeleteStatement(T obj);
	
	public ArrayList<T> Parse(ResultSet resultSet, HSQLDBDao dao) throws Exception;
}
