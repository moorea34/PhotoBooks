package tests.objectTests;

import photobooks.objects.Client;
import photobooks.objects.Payment;
import photobooks.objects.Payment.TenderType;

import java.util.Calendar;

import junit.framework.TestCase;

public class PaymentTest extends TestCase {

	Client client = new Client("Alex", "Moore");
	String description = "Payment 1";
	double total = 25;
	int invoiceId = 500;
	TenderType tenderType = TenderType.Cheque;
	Calendar date = Calendar.getInstance();
	
	public PaymentTest()
	{
	}
	
	
	public void testPayment() {
		Payment payment2 = new Payment(tenderType, client, invoiceId, total, description, date);
		
		assertEquals(client, payment2.getClient());
		assertEquals(description, payment2.getDescription());
		assertEquals(tenderType, payment2.getTenderType());
		assertEquals(invoiceId, payment2.getInvoiceId());
		assertEquals(total, payment2.getAmount());
		assertEquals(date, payment2.getDate());
	}
	
	public void testSetDate() {
		Payment payment = new Payment();
		
		payment.setDate(date);
		
		assertEquals(date, payment.getDate());
	}
	
	public void testSetAmount() {
		Payment payment = new Payment();
		
		payment.setAmount(total);
		
		assertEquals(total, payment.getAmount());
	}
	
	public void testSetInvoiceId() {
		Payment payment = new Payment();
		
		payment.setInvoiceId(invoiceId);
		
		assertEquals(invoiceId, payment.getInvoiceId());
	}
	
	public void testSetTenderType() {
		Payment payment = new Payment();
		
		payment.setTenderType(tenderType);
		
		assertEquals(tenderType, payment.getTenderType());
	}

	
	public void testSetClient() {
		Payment payment = new Payment();
		
		payment.setClient(client);
		
		assertEquals(client, payment.getClient());
	}

	
	public void testSetDescription() {
		Payment payment = new Payment();
		
		payment.setDescription(description);
		
		assertEquals(description, payment.getDescription());
	}

	
	public void testTotal() {
		Payment payment = new Payment();
		
		payment.setAmount(total);
		
		assertEquals(total, payment.total());
	}

	
	public void testAffectToBalance() {
		Payment payment = new Payment();
		
		payment.setAmount(total);
		
		assertEquals(total, payment.affectToBalance());
	}

}
