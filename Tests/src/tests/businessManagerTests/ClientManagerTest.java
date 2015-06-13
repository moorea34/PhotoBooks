package tests.businessManagerTests;

import java.io.File;

import photobooks.application.Utility;
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
		
		//Delete database so we can test with a clean one every time
		Utility.deleteRecursive(new File("database"));
		
		HSQLDBDao dao = new HSQLDBDao("database/Test");
		dao.initialize();
		dao.commitChanges();
		
		_dao = dao;
		_clientManager = new ClientManager(_dao);
		
		_dao.rollback();
		_dao.dispose();
	}
}
