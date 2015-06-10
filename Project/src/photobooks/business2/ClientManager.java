package photobooks.business2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import photobooks.gateways2.IDao;
import photobooks.gateways2.IGateway;
import photobooks.gateways2.IPhoneNumberGateway;
import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;

public class ClientManager {
	
	IDao _dao = null;

	public ClientManager(IDao dao) {
		_dao = dao;
	}
	
	//Gets a set of clients from the database ordered by a list of columns in orderBy, skipping offset clients and returning no more than count
	//Returns null on error
	public ArrayList<Client> select(int offset, int count, String orderBy, boolean orderDesc) {
		Collection<Client> clientCollection = null;
		Collection<PhoneNumber> numbers = null;
		Iterator<Client> it;
		
		ArrayList<Client> clients = null;
		Client client;
		
		IGateway<Client> clientGateway;
		IPhoneNumberGateway phoneNumberGateway;
		
		if (_dao != null) {
			clientGateway = _dao.clientGateway();
			phoneNumberGateway = _dao.phoneNumberGateway();
			
			if (clientGateway != null && phoneNumberGateway != null) {
				//Get the clients if the gateways and dao object exist
				clientCollection = clientGateway.select(offset, count, orderBy, orderDesc);
				
				if (clientCollection != null) {
					clients = new ArrayList<Client>(clientCollection);
					
					it = clients.iterator();
					
					while (it.hasNext()) {
						client = it.next();
						
						numbers = phoneNumberGateway.getByClientID(client.getID(), 0, 4, null, true);
						
						if (numbers != null) {
							client.setNumbers(new ArrayList<PhoneNumber>(numbers));
						}
						else {
							//Return null if their is a problem
							return null;
						}
					}
				}
			}
		}
		
		return clients;
	}
	
	//Gets a client from the database by id
	//Returns null on error
	public Client getByID(int id) {
		Client client = null;
		Collection<PhoneNumber> numbers = null;
		
		IGateway<Client> clientGateway;
		IPhoneNumberGateway phoneNumberGateway;
		
		if (_dao != null) {
			clientGateway = _dao.clientGateway();
			phoneNumberGateway = _dao.phoneNumberGateway();
			
			if (clientGateway != null && phoneNumberGateway != null) {
				//Get the client if the gateways and dao object exist
				client = clientGateway.getByID(id);
				
				if (client != null) {
					//Get the phone numbers if the client exists
					numbers = phoneNumberGateway.getByClientID(id, 0, 4, null, true);
					
					if (numbers != null) {
						client.setNumbers(new ArrayList<PhoneNumber>(numbers));
					}
					else {
						//If we fail to get the phone numbers there is a problem
						client = null;
					}
				}
			}
		}
		
		return client;
	}
	
	//Adds a new client to the database
	//Returns true on success and sets the clients ID
	public boolean insertClient(Client client) {
		IGateway<Client> clientGateway;
		IPhoneNumberGateway phoneNumberGateway;
		Iterator<PhoneNumber> it;
		boolean result = false;
		
		if (_dao != null && client != null) {
			clientGateway = _dao.clientGateway();
			phoneNumberGateway = _dao.phoneNumberGateway();
			
			if (clientGateway != null && phoneNumberGateway != null) {
				//Insert client if dao and gateways exist
				if (clientGateway.add(client)) {
					result = true;
					
					//Insert client phone numbers on success
					if (client.getNumbers() != null) {
						it = client.getNumbers().iterator();
						
						while (it.hasNext()) {
							result = result && phoneNumberGateway.add(it.next());
						}
					}
					
					//Commit changes on success, otherwise rollback
					if (result) {
						_dao.commitChanges();
					}
					else {
						_dao.rollback();
					}
				}
			}
		}
		
		return result;
	}
	
	//Updates an existing client in the database
	//Returns true on success
	public boolean updateClient(Client client) {
		IGateway<Client> clientGateway;
		IPhoneNumberGateway phoneNumberGateway;
		Iterator<PhoneNumber> it;
		PhoneNumber pn;
		boolean result = false;
		
		if (_dao != null && client != null) {
			clientGateway = _dao.clientGateway();
			phoneNumberGateway = _dao.phoneNumberGateway();
			
			if (clientGateway != null && phoneNumberGateway != null) {
				//Update client if dao and gateways exist
				if (clientGateway.update(client)) {
					result = true;
					
					//Update client phone numbers on success
					if (client.getNumbers() != null) {
						it = client.getNumbers().iterator();
						
						//Add and update numbers (only delete when the client is deleted so we don't burn through id's)
						while (it.hasNext()) {
							pn = it.next();
							
							if (pn.getID() == 0) {
								result = result && phoneNumberGateway.add(pn);
							}
							else {
								result = result && phoneNumberGateway.update(pn);
							}
						}
					}
					
					//Commit changes on success, otherwise rollback
					if (result) {
						_dao.commitChanges();
					}
					else {
						_dao.rollback();
					}
				}
			}
		}
		
		return result;
	}
	
	//Removes an existing client object from the database
	//Returns true on success
	public boolean deleteClient(Client client) {
		IGateway<Client> clientGateway;
		IPhoneNumberGateway phoneNumberGateway;
		Iterator<PhoneNumber> it;
		boolean result = false;
		
		if (_dao != null && client != null) {
			clientGateway = _dao.clientGateway();
			phoneNumberGateway = _dao.phoneNumberGateway();
			
			if (clientGateway != null && phoneNumberGateway != null) {
				//Delete client if dao and gateways exist
				if (clientGateway.delete(client)) {
					result = true;
					
					//Delete client phone numbers on success
					if (client.getNumbers() != null) {
						it = client.getNumbers().iterator();
						
						while (it.hasNext()) {
							result = result && phoneNumberGateway.delete(it.next());
						}
					}
					
					//Commit changes on success, otherwise rollback
					if (result) {
						_dao.commitChanges();
					}
					else {
						_dao.rollback();
					}
				}
			}
		}
		
		return result;
	}
}
