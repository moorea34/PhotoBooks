package tests.businessTests;

import photobooks.objects.Payment;

import photobooks.business.PaymentManager;
import junit.framework.TestCase;
import photobooks.gateways.*;

public class PaymentManagerTest extends TestCase {
	
	private PaymentManager _manager;

	public void setUp()
	{
		_manager = new PaymentManager( new StubConditionalGateway<Payment>() );
	}
	
	
	public void testTransactionManager() {		
		assertNotNull(_manager.getAll());
	}

	
	public void testGetByID() {
		Payment payment = new Payment();
		
		_manager.insert(payment);
		
		assertNotNull(_manager.getByID(payment.getID()));
	}

	
	public void testInsert() {
		Payment payment = new Payment();
		
		_manager.insert(payment);
		
		assertNotNull(_manager.getByID(payment.getID()));
	}

	
	public void testDelete() {
		Payment payment = new Payment();
		int size;
		
		_manager.insert(payment);
		size = _manager.getAll().size();
		
		_manager.delete(payment);
		
		assertTrue(_manager.getAll().size() == (size - 1));
	}

	
	public void testUpdate() {
		//DBStub doesn't implement update
	}
}
