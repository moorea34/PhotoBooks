package photobooks.business;

import photobooks.gateways.IGateway;

import java.util.ArrayList;
import java.util.Collection;

import photobooks.objects.Product;
import photobooks.objects.Package;

public class ProductManager 
{
	private IGateway<Product> _gateway;
	private ProductPackageManager _productPackageManager; 
	
	public ProductManager( IGateway<Product> gateway, ProductPackageManager productPackageManager )
	{
		_gateway = gateway;
		_productPackageManager = productPackageManager;
	}
	
	public void insertStubData()
	{
		insertProduct( new Product() );
		insertProduct( new Product() );
	}
	
	public Collection<Product> getProductList()
	{
		return _gateway.getAll();
	}
	
	public Product getProduct(int id)
	{
		return _gateway.getByID(id);
	}
	
	public void insertProduct(Product product)
	{
		_gateway.add(product);
	}
	
	public void updateProduct(Product product)
	{
		_gateway.update(product);
	}
	
	public void deleteProduct(Product product)
	{

		ArrayList<Package> productPackages  = new ArrayList<Package>(_productPackageManager.getProductPackageList());
		
		for (Package productPackage : productPackages)
		{
			productPackage.removeProduct(product.getID());
			_productPackageManager.updateProductPackage(productPackage);
		}
		
		_gateway.delete(product);
	}
	
	public int getTotalPurchased()
	{
		Collection<Product> allProducts = this.getProductList();
		int total = 0;
		
		for(Product p : allProducts)
		{
			total += p.getTotalPurchased();
		}
		
		return total;
	}
}
