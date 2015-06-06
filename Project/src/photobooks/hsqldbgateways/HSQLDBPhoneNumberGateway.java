package photobooks.hsqldbgateways;

import java.sql.ResultSet;
import java.util.Collection;

import photobooks.gateways2.IPhoneNumberGateway;
import photobooks.objects.PhoneNumber;

public class HSQLDBPhoneNumberGateway extends HSQLDBGateway<PhoneNumber> implements IPhoneNumberGateway {
	
	//Table
	private static final String PHONE_TABLE = "PHONE";
	//Columns
	private static final String ID = "ID";
	private static final String CLIENT_ID = "CLIENT_ID";
	private static final String PHONE_NUMBER = "PHONENUMBER";
	private static final String PHONETYPE_ID = "PHONETYPE_ID";
	

	//Constructor
	public HSQLDBPhoneNumberGateway(HSQLDBDao dao) {
		super(dao, PHONE_TABLE);
	}

	/*Selects a subset of phone numbers from the table
	 * 
	 * clientID: The client whose phone numbers to get
	 * offset: Number of objects to skip
	 * count: Number of objects to get
	 * orderBy: Comma separated list of columns to order by
	 * orderDesc: True to return collection in descending order otherwise ascending (only if orderBy parameter is specified)
	 * */
	@Override
	public Collection<PhoneNumber> getByClientID(int clientID, int offset, int count, String orderBy, boolean orderDesc) {
		return select(offset, count, orderBy, orderDesc, String.format("%s = %d", CLIENT_ID, clientID));
	}
	
	//Creates Phone Number object from result set
	@Override
	protected PhoneNumber fromResultSet(ResultSet resultSet) {
		PhoneNumber pn = null;
		int id, typeID;
		String number, typeValue = null;
		
		try {
			id = resultSet.getInt(ID);
			number = unformatSqlString(resultSet.getString(PHONE_NUMBER));
			typeID = resultSet.getInt(PHONETYPE_ID);
			
			//TODO: get phonetype value from type gateway
			
			pn = new PhoneNumber(PhoneNumber.PhoneNumberType.valueOf(typeValue), number);
		}
		catch (Exception e) {
			logException(e);
		}
		
		return pn;
	}
	
	//Creates collection of key value pairs representing the PhoneNumber object
	@Override
	protected Collection<KeyValuePair<String, String>> toKeyValuePairs(PhoneNumber obj) {
		return null;
	}
}
