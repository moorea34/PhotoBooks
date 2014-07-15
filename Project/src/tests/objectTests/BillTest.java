package tests.objectTests;

import java.util.ArrayList;
import java.util.Calendar;

import photobooks.objects.Client;
import photobooks.objects.ITransaction.TransactionType;
import photobooks.objects.BillProduct;
import photobooks.objects.BillPackage;
import photobooks.objects.Bill;
import photobooks.objects.Package;
import photobooks.objects.Payment;
import photobooks.objects.Payment.TenderType;
import photobooks.objects.Product;
import junit.framework.TestCase;

public class BillTest extends TestCase {

	ArrayList<BillPackage> packages = new ArrayList<BillPackage>();
	ArrayList<BillProduct> products = new ArrayList<BillProduct>();
	ArrayList<Payment> payments = new ArrayList<Payment>();
	Client client = new Client("Alex", "Moore");

	BillProduct billProduct1, billProduct2;
	BillPackage billPackage1, billPackage2;
	Product product1 = new Product("P1", "Product 1", 10);
	Product product2 = new Product("P2", "Product 2", 5);
	Package package1 = new Package("Pk1", "Package 1", 4);
	Package package2 = new Package("Pk2", "Package 2", 6);
	Payment payment1, payment2;
	Calendar date = Calendar.getInstance();
	
	public BillTest()
	{
		billPackage1 = new BillPackage(package1, 0, package1.getPrice());
		billPackage2 = new BillPackage(package2, 0, package2.getPrice());
		
		packages.add(billPackage1);
		packages.add(billPackage2);
		
		billProduct1 = new BillProduct(product1, 0, product1.getPrice());
		billProduct2 = new BillProduct(product2, 0, product2.getPrice());
		
		products.add(billProduct1);
		products.add(billProduct2);
		
		payment1 = new Payment(TenderType.Cash, client, 0, 15, "Product payment", date);
		payment2 = new Payment(TenderType.Cash, client, 0, 10, "Package payment", date);
		
		payments.add(payment1);
		payments.add(payment2);
	}
	
	
	public void testQuote() {
		Bill quote1 = new Bill();
		int id = 4;
		
		quote1.setID(id);
		quote1.setClient(client);
		quote1.setPackages(packages);
		quote1.setProducts(products);
		
		quote1.purchase();

		assertEquals(TransactionType.Quote, quote1.getType());
		
		for (BillPackage p : packages)
		{
			assertEquals(p.getPackage().getTotalPurchased(), 1);
		}
		for (BillProduct p : products)
		{
			assertEquals(p.getProduct().getTotalPurchased(), 1);
		}
		
		quote1.cancel();
	}

	
	public void testSetProducts() {
		Bill quote1 = new Bill();
		int purchased1 = billProduct1.getProduct().getTotalPurchased() + 1, purchased2 = billProduct2.getProduct().getTotalPurchased() + 1;
		
		quote1.setProducts(null);
		
		assertNull(quote1.getProducts());
		
		quote1.setProducts(products);
		assertEquals(products, quote1.getProducts());
		quote1.purchase();
		
		assertEquals(purchased1, product1.getTotalPurchased());
		assertEquals(purchased2, product2.getTotalPurchased());
		
		quote1.cancel();
	}

	
	public void testSetPackages() {
		Bill quote1 = new Bill();
		int purchased1 = billPackage1.getPackage().getTotalPurchased() + 1, purchased2 = billPackage2.getPackage().getTotalPurchased() + 1;
		
		quote1.setPackages(null);
		
		assertNull(quote1.getPackages());
		
		quote1.setPackages(packages);
		assertEquals(packages, quote1.getPackages());
		quote1.purchase();
		
		assertEquals(purchased1, package1.getTotalPurchased());
		assertEquals(purchased2, package2.getTotalPurchased());
		
		quote1.cancel();
	}
	
	public void testSetPayments() {
		Bill invoice = new Bill();
		
		invoice.setPayments(payments);
		
		assertEquals(payments, invoice.getPayments());
	}
	
	
	public void testSetDate() {
		Bill quote1 = new Bill();
		Calendar date = Calendar.getInstance();
		
		date.set(2014, 1, 5);
		
		quote1.setDate(date);
		
		assertEquals(date, quote1.getDate());
	}
	
	
	public void testSetGst() {
		Bill quote1 = new Bill();
		double gst = 0.05;
		
		quote1.setGst(gst);
		
		assertEquals(gst, quote1.getGst());
	}
	
	
	public void testSetPst() {
		Bill quote1 = new Bill();
		double pst = 0.04;
		
		quote1.setPst(pst);
		
		assertEquals(pst, quote1.getPst());
	}

	
	public void testSetClient() {
		Bill quote1 = new Bill();
		
		quote1.setClient(client);
		
		assertEquals(client, quote1.getClient());
	}
	
	
	public void testSetDescription() {
		Bill quote1 = new Bill();
		String desc = "Quote # 1";
		
		quote1.setDescription(desc);
		
		assertEquals(desc, quote1.getDescription());
	}
	
	
	public void testPurchase() {
		Bill quote1 = new Bill();

		int prod1count = product1.getTotalPurchased();
		int prod2count = product2.getTotalPurchased();
		int pack1count = package1.getTotalPurchased();
		int pack2count = package2.getTotalPurchased();
		
		quote1.setPackages(packages);
		quote1.setProducts(products);
		quote1.purchase();
		
		assertEquals(prod1count+1, product1.getTotalPurchased());
		assertEquals(prod2count+1, product2.getTotalPurchased());
		assertEquals(pack1count+1, package1.getTotalPurchased());
		assertEquals(pack2count+1, package2.getTotalPurchased());

		quote1.cancel();
	}
	
	
	public void testCancel() {
		Bill quote1 = new Bill();

		quote1.setPackages(packages);
		quote1.setProducts(products);
		quote1.purchase();
		
		int prod1count = product1.getTotalPurchased();
		int prod2count = product2.getTotalPurchased();
		int pack1count = package1.getTotalPurchased();
		int pack2count = package2.getTotalPurchased();
		
		quote1.cancel();
		
		assertEquals(prod1count-1, product1.getTotalPurchased());
		assertEquals(prod2count-1, product2.getTotalPurchased());
		assertEquals(pack1count-1, package1.getTotalPurchased());
		assertEquals(pack2count-1, package2.getTotalPurchased());
	}
		

	
	public void testAffectToBalance() {
		Bill quote1 = new Bill();
		double totalValueProducts = 15;
		double totalValuePackages = 10;
		double totalValue = totalValueProducts + totalValuePackages;
		
		//Test products
		assertTrue(quote1.affectToBalance() == 0);
		assertTrue(quote1.total() == 0);
		
		quote1.getProducts().add(billProduct1);
		assertTrue(quote1.total() == product1.getPrice());
		
		quote1.getProducts().add(billProduct2);
		assertTrue(quote1.total() == totalValueProducts);
		
		assertTrue(quote1.affectToBalance() == 0);
		
		//Test packages
		quote1.getPackages().add(billPackage1);
		assertTrue(quote1.total() == (totalValueProducts + package1.getPrice()));
		
		quote1.getPackages().add(billPackage2);
		assertTrue(quote1.total() == totalValue);
		
		assertTrue(quote1.affectToBalance() == 0);
		
		quote1.setType(TransactionType.Invoice);
		assertTrue(quote1.affectToBalance() == -totalValue);
	}
	
	public void testTotalPayments() {
		Bill invoice = new Bill();
		double totalValueProducts = 15;
		double totalValuePackages = 10;
		double totalValue = totalValueProducts + totalValuePackages;
		
		invoice.setPayments(payments);
		
		assertEquals(totalValue, invoice.totalPayments());
	}

}
