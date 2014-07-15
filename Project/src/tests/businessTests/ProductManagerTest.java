package tests.businessTests;

import photobooks.objects.Product;
import photobooks.objects.Package;

import photobooks.business.ProductManager;
import photobooks.business.ProductPackageManager;
import junit.framework.TestCase;
import photobooks.gateways.*;

public class ProductManagerTest extends TestCase
{
	private ProductManager _productManager;
	
	public ProductManagerTest(String arg0)
	{
		super(arg0);
	}
	
	public void setUp()
	{
		IGateway<Product> stubGateway = new StubGateway<Product>();
		_productManager = new ProductManager( stubGateway, new ProductPackageManager( new StubGateway<Package>() ) );
	}
	
	
	public void testProductManager_Constructor()
	{		
		assertNotNull(_productManager.getProductList());		
	}
	
	
	public void testProductManager_GetProduct()
	{		
		Product newProduct = new Product();
		final int PROD_ID = 1234;
		
		newProduct.setID(PROD_ID);
		
		_productManager.insertProduct(newProduct);
		
		assertEquals(null, _productManager.getProduct(PROD_ID));
	}
	
	 
	public void testProductManager_Insert()
	{
		int count = _productManager.getProductList().size();
		
		_productManager.insertProduct(new Product());
		
		assertEquals(count+1, _productManager.getProductList().size());
	}
	
	
	public void testProductManager_Delete()
	{		
		Product newProduct = new Product();
		
		_productManager.insertProduct(newProduct);
		
		int count = _productManager.getProductList().size();
		
		_productManager.deleteProduct(newProduct);
		
		assertEquals(count-1, _productManager.getProductList().size());
	}
	
	
	public void testProductManager_Update()
	{
		//update not implemented
	}
	
	
	public void testProductManager_GetTotalPurchased_OneProduct()
	{
		int total = 0;
		
		Product product1 = new Product();
		product1.purchase(5);
		_productManager.insertProduct(product1);
		total = product1.getTotalPurchased();
		
		assertEquals(total, _productManager.getTotalPurchased());		
	}
	
	public void testProductManager_GetTotalPurchased_ManyProducts()
	{
		int total = 0;
		
		Product product1 = new Product();
		product1.purchase(5);
		_productManager.insertProduct(product1);
		total = product1.getTotalPurchased();
		
		Product product2 = new Product();
		product2.purchase(15);
		_productManager.insertProduct(product2);
		total += product2.getTotalPurchased();
		
		assertEquals(total, _productManager.getTotalPurchased());
	}
	
	public void testProductManager_GetTotalPurchased_Cancellation()
	{
		int total = 0;
		
		Product product1 = new Product();
		product1.purchase(5);
		_productManager.insertProduct(product1);
		total = product1.getTotalPurchased();
		
		Product product2 = new Product();
		product2.purchase(15);
		_productManager.insertProduct(product2);
		total += product2.getTotalPurchased();
		
		product1.cancel(3);
		total = product1.getTotalPurchased() + product2.getTotalPurchased();
		
		assertEquals(total, _productManager.getTotalPurchased());
	}
}
