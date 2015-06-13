package photobooks.gateways2;

import java.util.ArrayList;

import photobooks.objects.DBObject;


public interface IGateway<T extends DBObject> {
	
	/*Selects a subset of objects from the table
	 * 
	 * offset: Number of objects to skip
	 * count: Number of objects to get
	 * orderBy: Comma separated list of columns to order by
	 * orderDesc: True to return collection in descending order otherwise ascending (only if orderBy parameter is specified)
	 * 
	 * Returns the list of items found. On error returns null.
	 * */
	public ArrayList<T> select(int offset, int count, String orderBy, boolean orderDesc);//TODO: add filter parameter
	//Gets a specific object from the table
	public T getByID(int id);
	//Adds a new object to the table
	public boolean add(T newObj);
	//Updates an existing object in the table
	public boolean update(T obj);
	//Removes an object from the table
	public boolean delete(T obj);
	
	//Releases resources used by the gateway (Closed by dao object)
	public void close();
}
