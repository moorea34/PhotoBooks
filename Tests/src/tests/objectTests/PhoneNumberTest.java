package tests.objectTests;

import photobooks.objects.PhoneNumber;
import photobooks.objects.PhoneNumber.PhoneNumberType;
import junit.framework.TestCase;

public class PhoneNumberTest extends TestCase {
	
	private final PhoneNumberType PHONE_NUM_TYPE = PhoneNumberType.Work;
	private final String PHONE_NUM = "(381) 123-7894";
	private final int CLIENT_ID = 25;
	private final int ID = 4;

	private final PhoneNumberType PHONE_NUM_TYPE2 = PhoneNumberType.Cellular;
	private final String PHONE_NUM2 = "(789) 222-2222";
	private final int CLIENT_ID2 = 47;

	public PhoneNumberTest(String arg0) {
		super(arg0);
	}

	public void testPhoneNumber_Constructor1() {
		PhoneNumber pn = new PhoneNumber(PHONE_NUM_TYPE, PHONE_NUM);
		
		assertNotNull(pn);
		assertEquals(pn.getID(), 0);
		
		assertEquals(pn.getClientId(), 0);
		assertTrue(pn.getNumber().equals(PHONE_NUM));
		assertTrue(pn.getType() == PHONE_NUM_TYPE);
	}
	
	public void testPhoneNumber_Constructor2() {
		PhoneNumber pn = new PhoneNumber(PHONE_NUM_TYPE, PHONE_NUM, CLIENT_ID);
		
		assertNotNull(pn);
		assertEquals(pn.getID(), 0);
		
		assertEquals(pn.getClientId(), CLIENT_ID);
		assertTrue(pn.getNumber().equals(PHONE_NUM));
		assertTrue(pn.getType() == PHONE_NUM_TYPE);
	}
	
	public void testPhoneNumber_Setters() {
		PhoneNumber pn = new PhoneNumber(PHONE_NUM_TYPE, PHONE_NUM, CLIENT_ID);
		
		assertNotNull(pn);
		
		pn.setID(ID);
		assertEquals(pn.getID(), ID);
		
		pn.setClientId(CLIENT_ID2);
		assertEquals(pn.getClientId(), CLIENT_ID2);
		
		pn.setNumber(PHONE_NUM2);
		assertTrue(pn.getNumber().equals(PHONE_NUM2));
		
		pn.setType(PHONE_NUM_TYPE2);
		assertTrue(pn.getType() == PHONE_NUM_TYPE2);
	}
}
