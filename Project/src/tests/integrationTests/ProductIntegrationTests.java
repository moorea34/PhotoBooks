package tests.integrationTests;

import java.util.*;

import junit.framework.*;
import photobooks.business.*;
import photobooks.gateways.*;
import photobooks.objects.*;
import photobooks.objects.Package;

public class ProductIntegrationTests extends TestCase
{	
	private IDao _dao;
	private boolean _isStub;
	private ClientManager _clientManager;
	private EventManager _eventManager;
	private ProductManager _productManager;
	private ProductPackageManager _productPackageManager;
	
	private final String PRODUCT_NAME = "Some product name";
	private final String PRODUCT_DESCRIPTION = "Some product description";
	private final Double PRODUCT_PRICE = 3.14;
	private final Double NEW_PRICE = 4.23;
	private final int PRODUCT_ID = 4;

	public void tearDown()
	{
		if (_dao != null)
		{
			_dao.dispose();
		}
	}
	
	private void restartDB()
	{
		if (_dao != null)
		{
			_dao.dispose();
		}
		
		_dao = getDao();
		IGateway<Client> clientGateway = _dao.clientGateway();
		IGateway<Product> productGateway = _dao.productGateway();
		IGateway<Package> packageGateway = _dao.packageGateway();
		
		_eventManager = new EventManager( null, _dao.eventGateway() ); 
		_clientManager = new ClientManager( clientGateway, _eventManager );
		_productPackageManager = new ProductPackageManager( packageGateway );
		_productManager = new ProductManager( productGateway, _productPackageManager );
		
		if (_isStub)
		{
			_clientManager.insertStubData();
			_productManager.insertStubData();
			_productPackageManager.insertStubData();
		}
	}
	
	private IDao getDao()
	{
		if (_isStub)
		{
			return new StubDao();
		} else
		{
			return new Dao();
		}
	}
	
	public void test_Product_CRUD_stub()
	{
		_isStub = true;
		restartDB();
		Product_CRUD();
	}
	
	public void test_Product_CRUD_real()
	{
		_isStub = false;
		restartDB();
		Product_CRUD();
	}
	
	private void Product_CRUD()
	{
		// Create
		Product newProduct = new Product( PRODUCT_NAME, PRODUCT_DESCRIPTION, PRODUCT_PRICE );
		_productManager.insertProduct( newProduct );
		
		// Read
		ArrayList<Product> products = new ArrayList<Product>( _productManager.getProductList() );
		assertTrue( products.size() == 3 );
		assertTrue( products.get(2).getName().equals( PRODUCT_NAME ) );
		
		Product testProduct = _productManager.getProduct( newProduct.getID() );
		assertTrue( testProduct.getName().equals( newProduct.getName() ) );
		assertTrue( testProduct.getDescription().equals( newProduct.getDescription() ) );
		assertTrue( testProduct.getPrice() == newProduct.getPrice() );
		
		// Update
		newProduct = _productManager.getProduct( newProduct.getID() );
		newProduct.setPrice( NEW_PRICE );
		_productManager.updateProduct( newProduct );
		
		testProduct = _productManager.getProduct( newProduct.getID() );
		assertTrue( testProduct.getPrice() == newProduct.getPrice() );
		
		// Delete
		_productManager.deleteProduct( newProduct );
		
		products = new ArrayList<Product>( _productManager.getProductList() );
		assertTrue( products.size() == 2 );
	}
	
	public void test_Package_CRUD_stub()
	{
		_isStub = true;
		restartDB();
		Package_CRUD();
	}
	
	public void test_Package_CRUD_real()
	{
		_isStub = false;
		restartDB();
		Package_CRUD();
	}
	
	private void Package_CRUD()
	{
		// Create
		Package newPackage = new Package( PRODUCT_NAME, PRODUCT_DESCRIPTION, PRODUCT_PRICE );
		_productPackageManager.insertProductPackage( newPackage );
		
		// Read
		ArrayList<Package> packages = new ArrayList<Package>( _productPackageManager.getProductPackageList() );
		assertTrue( packages.size() == 3 );
		assertTrue( packages.get(2).getName().equals( PRODUCT_NAME ) );
		
		Package testPackage = _productPackageManager.getProductPackage( newPackage.getID() );
		assertTrue( testPackage.getName().equals( newPackage.getName() ) );
		assertTrue( testPackage.getDescription().equals( newPackage.getDescription() ) );
		assertTrue( testPackage.getPrice() == newPackage.getPrice() );
		
		// Update
		newPackage = _productPackageManager.getProductPackage( newPackage.getID() );
		newPackage.setPrice( NEW_PRICE );
		_productPackageManager.updateProductPackage( newPackage );
		
		testPackage = _productPackageManager.getProductPackage( newPackage.getID() );
		assertTrue( testPackage.getPrice() == newPackage.getPrice() );
		
		// Delete
		_productPackageManager.deleteProductPackage( newPackage );
		
		packages = new ArrayList<Package>( _productPackageManager.getProductPackageList() );
		assertTrue( packages.size() == 2 );
	}
}
