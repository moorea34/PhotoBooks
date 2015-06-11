package photobooks.hsqldbgateways;

import java.sql.ResultSet;
import java.util.ArrayList;
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
	
	//Ensures the phone number table exists in the database
	@Override
	public boolean initialize() {
		if (!_dao.tableExists(PHONE_TABLE + "TYPE")) {
			String query = "CREATE MEMORY TABLE PHONETYPE(ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,TYPE VARCHAR(75))";
			String insert = "INSERT INTO PHONETYPE VALUES(1,'Home'); INSERT INTO PHONETYPE VALUES(2,'Cellular'); INSERT INTO PHONETYPE VALUES(3,'Work'); INSERT INTO PHONETYPE VALUES(4,'Alternative')";
			
			try {
				_statement.executeUpdate(query);
				_statement.executeUpdate(insert);
			}
			catch (Exception e) {
				System.out.println("Failed to create phone type table: " + e.getMessage());
				return false;
			}
		}
		
		if (!_dao.tableExists(PHONE_TABLE)) {
			String query = "CREATE MEMORY TABLE PHONE(ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,CLIENT_ID INTEGER,PHONENUMBER VARCHAR(60),PHONETYPE_ID INTEGER,CONSTRAINT SYS_FK_58 FOREIGN KEY(CLIENT_ID) REFERENCES CLIENT(ID) ON DELETE CASCADE,CONSTRAINT SYS_FK_59 FOREIGN KEY(PHONETYPE_ID) REFERENCES PHONETYPE(ID) ON DELETE CASCADE)";
			
			try {
				_statement.executeUpdate(query);
			}
			catch (Exception e) {
				System.out.println("Failed to create phone table: " + e.getMessage());
				return false;
			}
		}
		
		return true;
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
		int id, typeID, clientID;
		String number, typeValue = null;
		
		try {
			id = resultSet.getInt(ID);
			number = unformatSqlString(resultSet.getString(PHONE_NUMBER));
			typeID = resultSet.getInt(PHONETYPE_ID);
			typeValue = _dao.typeGateway().getById(PHONE_TABLE, typeID);
			clientID = resultSet.getInt(CLIENT_ID);
			
			pn = new PhoneNumber(PhoneNumber.PhoneNumberType.valueOf(typeValue), number, clientID);
			pn.setID(id);
		}
		catch (Exception e) {
			logException(e);
		}
		
		return pn;
	}
	
	//Creates collection of key value pairs representing the PhoneNumber object
	@Override
	protected Collection<KeyValuePair<String, String>> toKeyValuePairs(PhoneNumber obj) {
		ArrayList<KeyValuePair<String, String>> pairs = new ArrayList<KeyValuePair<String,String>>();
		int typeID;
		
		if (obj != null) {
			typeID = _dao.typeGateway().getByType(PHONE_TABLE, obj.getType().toString());
			
			pairs.add(new KeyValuePair<String, String>(CLIENT_ID, String.valueOf(obj.getClientId())));
			pairs.add(new KeyValuePair<String, String>(PHONE_NUMBER, formatSqlString(obj.getNumber())));
			pairs.add(new KeyValuePair<String, String>(PHONETYPE_ID, String.valueOf(typeID)));
		}
		
		return pairs;
	}
}
