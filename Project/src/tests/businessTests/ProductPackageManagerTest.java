package tests.businessTests;

import photobooks.objects.Package;

import photobooks.business.ProductPackageManager;
import photobooks.gateways.*;
import junit.framework.TestCase;

public class ProductPackageManagerTest extends TestCase
{
	private ProductPackageManager _manager;
	
	public ProductPackageManagerTest(String arg0)
	{
		super(arg0);
	}
	
	public void setUp()
	{
		IGateway<Package> stubGateway = new StubGateway<Package>();
		_manager = new ProductPackageManager( stubGateway );
	}

	
	public void testProductPackageManager_Constructor()
	{		
		assertNotNull(_manager.getProductPackageList());
	}
	
	
	public void testProductPackageManager_GetProductPackage()
	{		
		Package newProductPackage = new Package();
		
		newProductPackage.setID(1234);
		
		_manager.insertProductPackage(newProductPackage);
		
		assertEquals(null, _manager.getProductPackage(1234));
		
		newProductPackage.setID(0);
		_manager.insertProductPackage(newProductPackage);
		
		assertEquals(newProductPackage, _manager.getProductPackage(newProductPackage.getID()));
	}
	
	 
	public void testProductManager_Insert()
	{
		int count = _manager.getProductPackageList().size();
		
		_manager.insertProductPackage(new Package());
		
		assertEquals(count+1, _manager.getProductPackageList().size());
	}
	
	
	public void testProductManager_Delete()
	{		
		Package newProductPackage = new Package();
		
		_manager.insertProductPackage(newProductPackage);
		
		int count = _manager.getProductPackageList().size();
		
		_manager.deleteProductPackage(newProductPackage);
		
		assertEquals(count-1, _manager.getProductPackageList().size());
	}
	
	
	public void testProductManager_Update()
	{
		//update not implemented
	}
	
	
	public void testProductManager_GetTotalPurchased()
	{
		int total = 0;
	
		Package package1 = new Package();
		package1.purchase(5);
		_manager.insertProductPackage(package1);
		total = package1.getTotalPurchased();
		
		assertEquals(total, _manager.getTotalPurchased());
		
		Package package2 = new Package();
		package2.purchase(15);
		_manager.insertProductPackage(package2);
		total += package2.getTotalPurchased();
		
		assertEquals(total, _manager.getTotalPurchased());
		
		package1.cancel(3);
		total = package1.getTotalPurchased()+package2.getTotalPurchased();
		
		assertEquals(total, _manager.getTotalPurchased());
		
	}
}
