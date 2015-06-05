package photobooks.hsqldbgateways;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import photobooks.application.Utility;
import photobooks.gateways2.IGateway;
import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;

public class HSQLDBClientGateway extends HSQLDBGateway<Client> implements IGateway<Client> {
	
	//Table
	private static final String CLIENT_TABLE = "CLIENT";
	
	//Columns	
	private static final String ID = "ID";
	private static final String FIRST_NAME = "FIRSTNAME";
	private static final String LAST_NAME = "LASTNAME";
	private static final String BIRTHDAY = "BIRTHDAY";
	private static final String ANNIVERSARY = "ANNIVERSARY";
	private static final String EMAIL = "EMAIL";
	private static final String DIRECTORY = "DIRECTORY";
	
	private static final String ADDRESS = "ADDRESS";
	private static final String CITY = "CITY";
	private static final String PROVINCE = "PROVINCE";
	private static final String POSTALCODE = "POSTALCODE";
	private static final String ACCOUNTBALANCE = "ACCOUNTBALANCE";

	
	public HSQLDBClientGateway(HSQLDBDao dao) {
		super(dao, CLIENT_TABLE);
	}
	
	//Creates Client object from result set
	@Override
	protected Client fromResultSet(ResultSet resultSet) {
		Client client = null;
		int id;
		String firstName, lastName, email, directory, address, city, province, postalCode;
		double accountBalance;
		Timestamp tempDate;
		Calendar birthday = null, anniversary = null;
		
		try {
			id = resultSet.getInt(ID);
			firstName = Utility.unformatSqlString(resultSet.getString(FIRST_NAME));
			lastName = Utility.unformatSqlString(resultSet.getString(LAST_NAME));
			email = Utility.unformatSqlString(resultSet.getString(EMAIL));
			directory = Utility.unformatSqlString(resultSet.getString(DIRECTORY));
			
			address = Utility.unformatSqlString(resultSet.getString(ADDRESS));
			city = Utility.unformatSqlString(resultSet.getString(CITY));
			province = Utility.unformatSqlString(resultSet.getString(PROVINCE));
			postalCode = Utility.unformatSqlString(resultSet.getString(POSTALCODE));
			accountBalance = resultSet.getDouble(ACCOUNTBALANCE);
			
			tempDate = resultSet.getTimestamp(BIRTHDAY);
			
			if (tempDate != null) {
				birthday = Calendar.getInstance();
				birthday.setTimeInMillis(tempDate.getTime());
			}
			
			tempDate = resultSet.getTimestamp(ANNIVERSARY);
			
			if (tempDate != null) {
				anniversary = Calendar.getInstance();
				anniversary.setTimeInMillis(tempDate.getTime());
			}
			
			client = new Client(firstName, lastName, email, (birthday != null) ? (Calendar) birthday.clone() : null, 
					(anniversary != null) ? (Calendar) anniversary.clone() : null, new ArrayList<PhoneNumber>(), address, city,
							province, postalCode, accountBalance, directory);
			
			client.setID(id);
		}
		catch (Exception e) {
			logException(e);
		}
		
		return client;
	}
	
	//Creates collection of key value pairs representing the Client object
	@Override
	protected Collection<KeyValuePair<String, String>> toKeyValuePairs(Client obj) {
		ArrayList<KeyValuePair<String, String>> pairs = new ArrayList<KeyValuePair<String,String>>();
		String birthday = null;
		String anniversary = null;
		
		if (obj != null) {
			birthday = obj.getBirthday() != null ? new Date(obj.getBirthday().getTime().getTime()).toString() : null;
			anniversary = obj.getAnniversary() != null ? new Date(obj.getAnniversary().getTime().getTime()).toString() : null;
			
			pairs.add(new KeyValuePair<String, String>(FIRST_NAME, formatSqlString(obj.getFirstName())));
			pairs.add(new KeyValuePair<String, String>(LAST_NAME, formatSqlString(obj.getLastName())));
			pairs.add(new KeyValuePair<String, String>(BIRTHDAY, formatSqlString(birthday.toString())));
			pairs.add(new KeyValuePair<String, String>(ANNIVERSARY, formatSqlString(anniversary.toString())));
			pairs.add(new KeyValuePair<String, String>(EMAIL, formatSqlString(obj.getEmail())));
			pairs.add(new KeyValuePair<String, String>(DIRECTORY, formatSqlString(obj.getDirectory())));
			pairs.add(new KeyValuePair<String, String>(ADDRESS, formatSqlString(obj.getAddress())));
			pairs.add(new KeyValuePair<String, String>(CITY, formatSqlString(obj.getCity())));
			pairs.add(new KeyValuePair<String, String>(PROVINCE, formatSqlString(obj.getProvince())));
			pairs.add(new KeyValuePair<String, String>(POSTALCODE, formatSqlString(obj.getPostalCode())));
			pairs.add(new KeyValuePair<String, String>(ACCOUNTBALANCE, String.valueOf(obj.getAccountBalance())));
		}
		
		return pairs;
	}
}
