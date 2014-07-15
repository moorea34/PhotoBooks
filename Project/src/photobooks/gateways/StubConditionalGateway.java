package photobooks.gateways;

import java.util.ArrayList;
import java.util.Collection;

import photobooks.objects.DBObject;

public class StubConditionalGateway<T> implements IConditionalGateway<T> 
{
	protected ArrayList<T> data;
	static int idCounter;
	
	public StubConditionalGateway()
	{
		data = new ArrayList<T>();
		idCounter = 0;
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

	// will return data with specific id only
	public Collection<T> getAllWithId(int id) 
	{
		return data;
	}
}
