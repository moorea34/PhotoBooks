package photobooks.business;

import photobooks.gateways.IGateway;
import java.util.Collection;
import photobooks.objects.Package;

public class ProductPackageManager 
{
	private IGateway<Package> _gateway;
	
	public ProductPackageManager( IGateway<Package> gateway )
	{
		_gateway = gateway;
	}
	
	public void insertStubData()
	{
		insertProductPackage( new Package() );
		insertProductPackage( new Package() );
	}

	public Collection<Package> getProductPackageList()
	{
		return _gateway.getAll();
	}
	
	public Package getProductPackage(int id)
	{
		return _gateway.getByID(id);
	}
	
	public void insertProductPackage(Package productPackage)
	{
		_gateway.add(productPackage);
	}
	
	public void updateProductPackage(Package productPackage)
	{
		_gateway.update(productPackage);
	}
	
	public void deleteProductPackage(Package productPackage)
	{
		_gateway.delete(productPackage);
	}
	
	public int getTotalPurchased()
	{
		Collection<Package> allPackages = this.getProductPackageList();
		int total = 0;
		
		for(Package p : allPackages)
		{
			total += p.getTotalPurchased();
		}
		
		return total;
	}

}
