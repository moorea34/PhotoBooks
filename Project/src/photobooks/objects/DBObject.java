package photobooks.objects;

public class DBObject
{
	private int _id;
	
	public DBObject()
	{
		//0 means unassigned, real id's will be > 0
		_id = 0;
	}
	
	public DBObject(int id)
	{
		_id = id;
	}
	
	public int getID()
	{
		return _id;
	}
	
	//We need to be able to fill out an object before taking the next ID from the DB
	public void setID(int id)
	{
		_id = id;
	}
	
	@Override
	public int hashCode() {
		return _id;
	}



	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		DBObject other = (DBObject) obj;
		
		if (other._id != _id)
			return false;
		
		return true;
	}
}
