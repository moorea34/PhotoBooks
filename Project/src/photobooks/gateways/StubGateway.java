package photobooks.gateways;

import java.util.*;

import photobooks.objects.DBObject;

public class StubGateway<T> implements IGateway<T>
{
	protected ArrayList<T> data;
	static int idCounter;
	
	public StubGateway()
	{
		data = new ArrayList<T>();
		idCounter = 0;
	}
	
	public void load()
	{
	}
	
	public Collection<T> getAll()
	{
		return data;
	}

	public T getByID(int id)
	{
		T result = null;
		
		for (T item : data)
		{
			DBObject obj = (DBObject)item;
			
			if (obj.getID() == id)
			{
				result = item;
				break;
			}
		}
		
		return result;
	}
	
	public boolean add(T newObj)
	{
		if (newObj instanceof DBObject)
		{
			DBObject obj = (DBObject)newObj;
			
			if (obj.getID() == 0)
			{
				idCounter++;
				obj.setID(idCounter);
			}
			else
			{
				return false;
			}
			
			data.add(newObj);
			return true;
		}
		else
		{
			System.out.println("Object " + newObj.getClass().getName() + " does not extend DBObject");
		}
		
		return false;
	}
	
	//Exists for data base use
	public void update(T obj)
	{
		
	}
	
	public void delete(T obj)
	{
		data.remove(obj);
	}
}
