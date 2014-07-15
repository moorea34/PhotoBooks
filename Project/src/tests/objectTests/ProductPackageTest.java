package tests.objectTests;

import java.util.ArrayList;

import junit.framework.TestCase;
import photobooks.objects.Package;

public class ProductPackageTest extends TestCase
{
	public ProductPackageTest(String arg0)
	{
		super(arg0);
	}
	
	
	public void testProductPackage_NullConstructor()
	{
		Package productPackage;
		
		productPackage = new Package();
				
		assertNotNull(productPackage);
		assertNull(productPackage.getName());
		assertNull(productPackage.getDescription());
		assertTrue(productPackage.getPrice() == 0);
		assertNotNull(productPackage.getProducts());
		assertTrue(productPackage.getProducts().isEmpty());
	}
	
	
	public void testProductPackage_Constructor1()
	{
		Package productPackage;
		
		productPackage = new Package("Name", "Description", 12.34);
		
		assertNotNull(productPackage);
		assertTrue(productPackage.getName().equals("Name"));
		assertTrue(productPackage.getDescription().equals("Description"));
		assertTrue(productPackage.getPrice() == 12.34);
		assertNotNull(productPackage.getProducts());
		assertTrue(productPackage.getProducts().isEmpty());	
	}
	
	
	public void testProductPackage_Constructor2()
	{
		Package productPackage;
		
		productPackage = new Package("Name", "Description", 12.34, new ArrayList<photobooks.objects.Product>());
		assertNotNull(productPackage);
		assertTrue(productPackage.getName().equals("Name"));
		assertTrue(productPackage.getDescription().equals("Description"));
		assertTrue(productPackage.getPrice() == 12.34);
		assertNotNull(productPackage.getProducts());
		assertTrue(productPackage.getProducts().isEmpty());
	}
	
	
	public void testProductPackage_Setters()
	{
		Package productPackage;
		
		productPackage = new Package();
		
		productPackage.setName("Name");
		productPackage.setDescription("Description");
		productPackage.setPrice(12.34);
		productPackage.setProducts(new ArrayList<photobooks.objects.Product>());
		
		assertTrue(productPackage.getName().equals("Name"));
		assertTrue(productPackage.getDescription().equals("Description"));
		assertTrue(productPackage.getPrice() == 12.34);
		assertNotNull(productPackage.getProducts());
		assertTrue(productPackage.getProducts().isEmpty());
	}
	
	
	public void testProductPackage_PurchaseCancel()
	{
		Package productPackage;
		
		productPackage = new Package();
		int purchased = 0;
		
		productPackage.purchase();
		purchased++;
		assertTrue(productPackage.getTotalPurchased() == purchased);
		
		productPackage.purchase(1000);
		purchased += 1000;
		assertTrue(productPackage.getTotalPurchased() == purchased);
		
		productPackage.cancel(1000);
		purchased -= 1000;
		assertTrue(productPackage.getTotalPurchased() == purchased);
		
		productPackage.cancel();
		purchased --;
		assertTrue(productPackage.getTotalPurchased() == purchased);
	}
}
