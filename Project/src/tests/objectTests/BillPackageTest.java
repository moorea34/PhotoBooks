package tests.objectTests;

import photobooks.objects.BillPackage;
import photobooks.objects.Package;
import junit.framework.TestCase;

public class BillPackageTest extends TestCase {

	BillPackage _package;
	int _billID;
	Package _pack;
	double _price;
	
	protected void setUp() throws Exception {
		_package = new BillPackage();
		_billID = 10;
		_pack = new Package("P1", "Package 1", 6);
		_price = 5;
		
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testBillPackage() {
		assertTrue(0 == _package.getPrice());
		assertTrue(_package.getBillID() == 0);
		assertNull(_package.getPackage());
	}

	public void testBillPackagePackageBillDouble() {
		_package = new BillPackage(_pack, _billID, _price);
		
		assertEquals(_pack, _package.getPackage());
		assertEquals(_billID, _package.getBillID());
		assertEquals(_price, _package.getPrice());
	}

	public void testSetPackage() {
		_package.setPackage(_pack);
		
		assertEquals(_pack, _package.getPackage());
	}

	public void testSetBill() {
		_package.setBillID(_billID);
		
		assertEquals(_billID, _package.getBillID());
	}

	public void testSetPrice() {
		_package.setPrice(_price);
		
		assertEquals(_price, _package.getPrice());
	}

}
