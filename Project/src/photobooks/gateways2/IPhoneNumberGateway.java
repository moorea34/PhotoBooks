package photobooks.gateways2;

import java.util.ArrayList;

import photobooks.objects.PhoneNumber;

public interface IPhoneNumberGateway extends IGateway<PhoneNumber> {
	
	/*Selects a subset of phone numbers from the table
	 * 
	 * clientID: The client whose phone numbers to get
	 * offset: Number of objects to skip
	 * count: Number of objects to get
	 * orderBy: Comma separated list of columns to order by
	 * orderDesc: True to return collection in descending order otherwise ascending (only if orderBy parameter is specified)
	 * */
	public ArrayList<PhoneNumber> getByClientID(int clientID, int offset, int count, String orderBy, boolean orderDesc);
}
