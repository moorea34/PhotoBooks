package photobooks.business2;

import java.util.ArrayList;

import photobooks.gateways2.IDao;
import photobooks.objects.Client;

public class ClientManager {
	
	IDao _dao = null;

	public ClientManager(IDao dao) {
		_dao = dao;
	}
	
	//Gets a set of clients from the database ordered by a list of columns in orderBy, skipping offset clients and returning no more than count
	//Returns null on error
	public ArrayList<Client> select(int offset, int count, String filter, String orderBy, boolean orderDesc) {
		return _dao.clientGateway().select(offset, count, filter, orderBy, orderDesc);
	}
	
	//Gets a client from the database by id
	//Returns null on error
	public Client getByID(int id) { return _dao.clientGateway().getByID(id); }
	
	//Adds a new client to the database
	//Returns true on success and sets the clients ID
	public boolean insertClient(Client client) {
		if (_dao.clientGateway().add(client)) {
			_dao.commitChanges();
			return true;
		}
		else {
			_dao.rollback();
			return false;
		}
	}
	
	//Updates an existing client in the database
	//Returns true on success
	public boolean updateClient(Client client) {
		if (_dao.clientGateway().update(client)) {
			_dao.commitChanges();
			return true;
		}
		else {
			_dao.rollback();
			return false;
		}
	}
	
	//Removes an existing client object from the database
	//Returns true on success
	public boolean deleteClient(Client client) {
		if (_dao.clientGateway().delete(client)) {
			_dao.commitChanges();
			return true;
		}
		else {
			_dao.rollback();
			return false;
		}
	}
}
