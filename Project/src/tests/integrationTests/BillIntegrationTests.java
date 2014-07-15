package tests.integrationTests;

import java.util.*;

import junit.framework.*;
import photobooks.application.Globals;
import photobooks.business.*;
import photobooks.gateways.*;
import photobooks.objects.*;
import photobooks.objects.Package;
import photobooks.objects.Payment.TenderType;

public class BillIntegrationTests extends TestCase
{	
	private IDao _dao;
	private boolean _isStub;
	private PaymentManager _paymentManager;
	private ClientManager _clientManager;
	private EventManager _eventManager;
	private BillManager _billManager;
	
	private final String DESCRIPTION = "Some description thing.";
	private final String NEW_DESCRIPTION = "Totally new description";
	private final int BILL_ID = 34;

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
		IConditionalGateway<Payment> gateway = _dao.paymentGateway();
		_paymentManager = new PaymentManager( gateway );
		_billManager = new BillManager( _dao );
		IGateway<Client> clientGateway = _dao.clientGateway();
		_eventManager = new EventManager( null, _dao.eventGateway() );
		_clientManager = new ClientManager( clientGateway, _eventManager );
		
		if (_isStub)
		{
			_clientManager.insertStubData();
			_billManager.insertStubData();
			_paymentManager.insertStubData();			
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
	
	public void test_Bill_CRUD_stub()
	{
		_isStub = true;
		restartDB();
		Bill_CRUD();
	}
	
	public void test_Bill_CRUD_real()
	{
		_isStub = false;
		restartDB();
		Bill_CRUD();
	}
	
	private void Bill_CRUD()
	{
		// Create
		Client newClient = new Client();
		_clientManager.insertClient( newClient );
		
		Bill newBill = new Bill();
		newBill.setClient( newClient );
		_billManager.insert( newBill );
		
		// Read
		ArrayList<Bill> bills = new ArrayList<Bill>( _billManager.getAll() );
		assertTrue( bills.size() == 4 );
		assertTrue( bills.get(3).getID() == newBill.getID() );
		
		Bill testBill = _billManager.getByID( newBill.getID() );
		assertTrue( testBill.getClient().getID() == newBill.getClient().getID() );
		
		bills = new ArrayList<Bill>( _billManager.getByClientID( newClient.getID() ) );
		assertTrue( bills.size() == 1 );
		assertTrue( bills.get(0).getID() == newBill.getID() );
		
		// Update
		newBill = bills.get(0);
		newBill.setDescription( DESCRIPTION );
		_billManager.update( newBill );
		
		testBill = _billManager.getByID( newBill.getID() );
		assertTrue( testBill.getDescription().equals( DESCRIPTION ) );
		
		// Delete
		_billManager.delete( newBill );
		_clientManager.removeClient( newClient );
		
		bills = new ArrayList<Bill>( _billManager.getAll() );
		assertTrue( bills.size() == 3 );
	}
	
	public void test_Payment_CRUD_stub()
	{
		_isStub = true;
		restartDB();
		Payment_CRUD();
	}
	
	public void test_Payment_CRUD_real()
	{
		_isStub = false;
		restartDB();
		Payment_CRUD();
	}
	
	private void Payment_CRUD()
	{
		// Create				
		Client newClient = new Client();
		_clientManager.insertClient( newClient );
		
		Bill newBill = new Bill();
		newBill.setClient( newClient );
		_billManager.insert( newBill );
		
		Payment newPayment = new Payment();
		newPayment.setDescription( DESCRIPTION );
		newPayment.setInvoiceId( newBill.getID() );
		newPayment.setClient( newClient );
		_paymentManager.insert( newPayment );
		
		// Read
		ArrayList<Payment> payments = new ArrayList<Payment>( _paymentManager.getAll() );
		assertTrue( payments.size() == 3 );
		assertTrue( payments.get(2).getID() == newPayment.getID() );
		
		payments = new ArrayList<Payment>( _paymentManager.getByInvoiceId( newBill.getID() ) );
		assertTrue( payments.size() == 1 );
		assertTrue( payments.get(0).getDescription().equals( DESCRIPTION ) );
		assertTrue( payments.get(0).getInvoiceId() == newBill.getID() );
		
		Payment testPayment = _paymentManager.getByID( newPayment.getID() );
		assertTrue( testPayment.getDescription().equals( DESCRIPTION ) );
		assertTrue( testPayment.getInvoiceId() == newBill.getID() );
		
		// Update
		payments = new ArrayList<Payment>( _paymentManager.getByInvoiceId( newBill.getID() ) );
		newPayment = payments.get(0);
		newPayment.setDescription( NEW_DESCRIPTION );
		_paymentManager.update( newPayment );
		
		testPayment = _paymentManager.getByID( newPayment.getID() );
		assertTrue( testPayment.getDescription().equals( NEW_DESCRIPTION ) );
		
		// Delete
		_paymentManager.delete( newPayment );
		_billManager.delete( newBill );
		_clientManager.removeClient( newClient );
		
		payments = new ArrayList<Payment>( _paymentManager.getAll() );
		assertTrue( payments.size() == 2 );
	}
	
	public void test_Bill()
	{
		Bill bill1 = new Bill();
		bill1.setID( BILL_ID );
		
		Package package1 = new Package();
		Product product1 = new Product();
		Product product2 = new Product();
		package1.insertProduct(product1);
		package1.insertProduct(product2);
		
		Product product3 = new Product();
		
		BillPackage bPackage1 = new BillPackage( package1, bill1.getID(), 10 );
		BillProduct bProduct1 = new BillProduct( product3, bill1.getID(), 2 );
		
		ArrayList<BillPackage> packages = new ArrayList<BillPackage>();
		packages.add( bPackage1 );
		bill1.setPackages( packages );
		
		assertTrue( bill1.total() == 10 );
		
		ArrayList<BillProduct> products = new ArrayList<BillProduct>();
		products.add( bProduct1 );
		bill1.setProducts( products );
		
		assertTrue( bill1.total() == 12 );
		
		Payment p1 = new Payment( TenderType.Cash, null, bill1.getID(), 5, null, null );
		Payment p2 = new Payment( TenderType.Cheque, null, bill1.getID(), 7, null, null );
		
		ArrayList<Payment> payments = new ArrayList<Payment>();
		payments.add( p1 );
		bill1.setPayments( payments );
		
		assertTrue( bill1.totalPayments() == 5 );
		
		payments.add(p2);
		assertTrue( bill1.totalPayments() == 12 );
		
		assertTrue( bill1.getTaxes() == (Globals.getGst() * 12) + (Globals.getPst() * 12) );
	}
}
