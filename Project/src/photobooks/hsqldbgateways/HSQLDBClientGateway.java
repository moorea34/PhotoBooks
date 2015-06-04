package photobooks.hsqldbgateways;

import photobooks.gateways2.IGateway;
import photobooks.objects.Client;

public class HSQLDBClientGateway extends HSQLDBGateway<Client> implements IGateway<Client> {
	
	//Table
	private static final String CLIENT_TABLE = "CLIENT";
	
	//Columns	
	private static final String ID = "ID";
	private static final String FIRST_NAME = "FIRSTNAME";
	private static final String LAST_NAME = "LASTNAME";
	private static final String EMAIL = "EMAIL";
	private static final String DIRECTORY = "DIRECTORY";
	private static final String BIRTHDAY = "BIRTHDAY";
	private static final String ANNIVERSARY = "ANNIVERSARY";
	
	private static final String ADDRESS = "ADDRESS";
	private static final String CITY = "CITY";
	private static final String PROVINCE = "PROVINCE";
	private static final String POSTALCODE = "POSTALCODE";
	private static final String ACCOUNTBALANCE = "ACCOUNTBALANCE";

	
	public HSQLDBClientGateway(HSQLDBDao dao) {
		super(dao, CLIENT_TABLE);
	}
}
