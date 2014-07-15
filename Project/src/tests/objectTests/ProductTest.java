package tests.objectTests;

import junit.framework.TestCase;
import photobooks.objects.Product;

public class ProductTest extends TestCase
{
	public ProductTest(String arg0)
	{
		super(arg0);
	}
	
	
	public void testProduct_NullConstructor()
	{
		Product product;
		
		product = new Product();
		
		assertNotNull(product);
		assertNull(product.getName());
		assertNull(product.getDescription());
		assertTrue(product.getPrice() == 0);
	}
	
	
	public void testProduct_Constructor()
	{
		Product product;
		
		product = new Product("Name", "Description", 12.34);
		
		assertNotNull(product);
		assertTrue(product.getName().equals("Name"));
		assertTrue(product.getDescription().equals("Description"));
		assertTrue(product.getPrice() == 12.34);
	}
	
	
	public void testProduct_Setters()
	{
		Product product;
		
		product = new Product();
		
		product.setName("Name");
		product.setDescription("Description");
		product.setPrice(12.34);
		
		assertTrue(product.getName().equals("Name"));
		assertTrue(product.getDescription().equals("Description"));
		assertTrue(product.getPrice() == 12.34);
	}
	
	
	public void testProduct_PurchaseCancel()
	{
		Product product;
		
		product = new Product();
		int purchased = 0;
		
		product.purchase();
		purchased++;
		assertTrue(product.getTotalPurchased() == purchased);
		
		product.purchase(1000);
		purchased += 1000;
		assertTrue(product.getTotalPurchased() == purchased);
		
		product.cancel(1000);
		purchased -= 1000;
		assertTrue(product.getTotalPurchased() == purchased);
		
		product.cancel();
		purchased --;
		assertTrue(product.getTotalPurchased() == purchased);
	}
}
