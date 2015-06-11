package tests.businessManagerTests;

import photobooks.business2.ClientManager;
import photobooks.gateways2.IDao;
import photobooks.hsqldbgateways.HSQLDBDao;
import junit.framework.TestCase;

public class ClientManagerTest extends TestCase {
	
	public ClientManagerTest(String arg0) {
		super(arg0);
	}
	
	public void testClientManager() {
		IDao _dao = null;
		ClientManager _clientManager = null;
		
		HSQLDBDao dao = new HSQLDBDao("Test");
		dao.initialize();
		dao.commitChanges();
		
		_dao = dao;
		_clientManager = new ClientManager(_dao);
		
		_dao.rollback();
		_dao.dispose();
	}
}
