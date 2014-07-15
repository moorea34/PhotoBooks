package photobooks.objects;

import java.util.ArrayList;

public class Package extends ProductBase
{
	private ArrayList<Product> _products;
	
	public Package() 
	{
		super();
		_products = new ArrayList<Product>();
	}
	
	public Package(String name, String description, double price)
	{
		super(name, description, price);
		_products = new ArrayList<Product>();
	}
	
	public Package(String name, String description, double price, ArrayList<Product> products)
	{
		super(name, description, price);
		_products = products;		
	}	
	
	public Package(String name, String description, double price, int totalPurchased, ArrayList<Product> products)
	{
		super(name, description, price, totalPurchased);
		_products = products;
	}
	
	public ArrayList<Product> getProducts() {
		return _products;
	}
	
	public void setProducts(ArrayList<Product> _products) {
		this._products = _products;
	}
	
	public void insertProduct(Product _product)
	{
		if(!_products.contains(_product))
			_products.add(_product);
	}
	
	public void removeProduct(int _id)
	{
		ArrayList<Product> prodList = new ArrayList<Product>(_products);
		for(Product p : prodList)
		{
			if(p.getID() == _id)
				_products.remove(p);
		}
	}
	
}
