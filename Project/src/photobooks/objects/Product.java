package photobooks.objects;

public class Product extends ProductBase
{
	public Product() 
	{
		super();
	}
	
	public Product(String name, String description, double price) 
	{
		super(name, description, price);
	}
	
	public Product(String name, String description, double price, int totalPurchased)
	{
		super(name, description, price, totalPurchased);
	}
}
