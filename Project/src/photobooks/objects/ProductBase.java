package photobooks.objects;

public class ProductBase extends DBObject
{	
	private String _name;
	private String _description;
	private double _price;
	private int _totalPurchased;
	
	public ProductBase() 
	{
		_name = null;
		_description = null;
		_price = 0;
		_totalPurchased = 0;
	}
	
	public ProductBase(String name, String description, double price) 
	{
		_name = name;
		_description = description;
		_price = price;
		_totalPurchased = 0;
	}
	
	public ProductBase(String name, String description, double price, int totalPurchased)
	{
		_name = name;
		_description = description;
		_price = price;
		_totalPurchased = totalPurchased;
	}
	
	public String getName() 
	{
		return _name;
	}
	
	public void setName(String _name) 
	{
		this._name = _name;
	}
	
	public String getDescription() 
	{
		return _description;
	}
	
	public void setDescription(String _description) 
	{
		this._description = _description;
	}
	
	public double getPrice() 
	{
		return _price;
	}
	
	public void setPrice(double _price) 
	{
		this._price = _price;
	}
	
	public int getTotalPurchased()
	{
		return _totalPurchased;
	}
	
	public void purchase(int _count)
	{
		this._totalPurchased = this._totalPurchased + _count;
	}
	
	public void purchase()
	{
		this._totalPurchased++;
	}
	
	public void cancel(int _count)
	{
		this._totalPurchased = this._totalPurchased - _count;
	}
	
	public void cancel()
	{
		this._totalPurchased--;
	}

	public boolean searchAll(String lowerCase)
	{
		if(_name.toLowerCase().contains(lowerCase) || _description.toLowerCase().contains(lowerCase))
			return true;
		
		return false;
	}
}
