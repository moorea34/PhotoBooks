package tests.objectTests;

import photobooks.objects.BillProduct;
import photobooks.objects.Product;
import junit.framework.TestCase;

public class BillProductTest extends TestCase {

	BillProduct _product;
	int _billID;
	Product _prod;
	double _price;

	protected void setUp() throws Exception {
		_product = new BillProduct();
		_billID = 10;
		_prod = new Product("P1", "Product 1", 6);
		_price = 5;
		
		super.setUp();
	}


	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testBillProduct() {
		assertTrue(0 == _product.getPrice());
		assertTrue(_product.getBillID() == 0);
		assertNull(_product.getProduct());
	}

	public void testBillProductProductBillDouble() {
		_product = new BillProduct(_prod, _billID, _price);
		
		assertEquals(_prod, _product.getProduct());
		assertEquals(_billID, _product.getBillID());
		assertEquals(_price, _product.getPrice());
	}

	public void testSetProduct() {
		_product.setProduct(_prod);
		
		assertEquals(_prod, _product.getProduct());
	}

	public void testSetBill() {
		_product.setBillID(_billID);
		
		assertEquals(_billID, _product.getBillID());
	}

	public void testSetPrice() {
		_product.setPrice(_price);
		
		assertEquals(_price, _product.getPrice());
	}

}
