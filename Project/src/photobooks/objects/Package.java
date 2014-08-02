package photobooks.objects;

import java.util.ArrayList;

public class Package extends ProductBase
{
	private ArrayList<ProductPackage> _products;
	
	public Package() 
	{
		super();
		_products = new ArrayList<ProductPackage>();
	}
	
	public Package(String name, String description, double price)
	{
		super(name, description, price);
		_products = new ArrayList<ProductPackage>();
	}
	
	public Package(String name, String description, double price, ArrayList<ProductPackage> products)
	{
		super(name, description, price);
		_products = products;		
	}	
	
	public Package(String name, String description, double price, int totalPurchased, ArrayList<ProductPackage> products)
	{
		super(name, description, price, totalPurchased);
		_products = products;
	}
	
	public ArrayList<ProductPackage> getProducts() {
		return _products;
	}
	
	public ArrayList<Product> getProductList() {
		ArrayList<Product> products = new ArrayList<Product>();
		
		for (ProductPackage product : _products) {
			products.add(product.getProduct());
		}
		
		return products;
	}
	
	public void setProducts(ArrayList<ProductPackage> _products) {
		this._products = _products;
	}
	
	public void updateOrder() {
		int order = 0;
		
		for (ProductPackage p : _products) {
			p.setOrder(order);
			++order;
		}
	}
	
	public void moveUp(int productId) {
		ArrayList<ProductPackage> prodList = new ArrayList<ProductPackage>(_products);
		ProductPackage temp;
		int i = 0;
		
		for (ProductPackage product : prodList) {
			if (product.getProduct().getID() == productId)
			{
				if (i > 0)
				{
					temp = _products.get(i);
					_products.set(i, _products.get(i - 1));
					_products.set(i - 1, temp);
				}
				
				break;
			}
			
			++i;
		}
		
		updateOrder();
	}
	
	public void moveDown(int productId) {
		ArrayList<ProductPackage> prodList = new ArrayList<ProductPackage>(_products);
		ProductPackage temp;
		int i = 0;
		
		for (ProductPackage product : prodList) {
			if (product.getProduct().getID() == productId)
			{
				if (i < _products.size() - 1)
				{
					temp = _products.get(i);
					_products.set(i, _products.get(i + 1));
					_products.set(i + 1, temp);
				}
				
				break;
			}
			
			++i;
		}
		
		updateOrder();
	}
	
	private int nextOrder() {
		int order = 0;
		
		for (ProductPackage p : _products) {
			if (p.getOrder() > order)
				order = p.getOrder();
		}
		
		if (_products.size() > 0)
			++order;
		
		return order;
	}
	
	public void insertProduct(Product _product) {
		insertProduct(_product, 1);
	}
	
	public void insertProduct(Product _product, int count)
	{
		for (ProductPackage p : _products) {
			if (p.getProduct().equals(_product)) {
				p.setAmount(p.getAmount() + count);
				return;
			}
		}
		
		_products.add(new ProductPackage(_product, getID(), count, nextOrder()));
	}
	
	public void removeProduct(int _id) {
		removeProduct(_id, 1);
	}
	
	public void removeProduct(int _id, int count)
	{
		ArrayList<ProductPackage> prodList = new ArrayList<ProductPackage>(_products);
		
		for(ProductPackage p : prodList)
		{
			if(p.getProduct().getID() == _id) {
				p.setAmount(p.getAmount() - count);
				
				if (p.getAmount() <= 0)
					_products.remove(p);
			}
		}
	}
	
	public boolean searchAll(String lowerCase) {
		boolean result = super.searchAll(lowerCase);
		
		if (!result)
		{
			for (ProductPackage p : _products) {
				result = result || p.getProduct().searchAll(lowerCase);
			}
		}
		
		return result;
	}
}
