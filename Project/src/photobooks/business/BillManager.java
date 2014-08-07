package photobooks.business;

import photobooks.objects.BillProduct;
import photobooks.objects.Client;
import photobooks.objects.Bill;
import photobooks.objects.ITransaction.TransactionType;
import photobooks.objects.Product;
import photobooks.gateways.IConditionalGateway;
import photobooks.gateways.IDao;

import java.util.ArrayList;
import java.util.Collection;

public class BillManager
{
	private IDao _dao;
	private IConditionalGateway<Bill> _gateway;
	
	public BillManager( IDao dao )
	{
		_dao = dao;
		_gateway = dao.billGateway();
	}
	
	public void insertStubData()
	{
		for (Client client : _dao.clientGateway().getAll())
		{
			Bill bill = new Bill();
			
			bill.setClient(client);
			bill.setDescription("Test Quote ");
			bill.getProducts().add(new BillProduct(new Product("Product 1", "Test", 10), bill.getID(), 9));
			insert(bill);
		}
		
		Bill bill = new Bill();
		
		bill.setType(TransactionType.Invoice);
		ArrayList<Client> clients = new ArrayList<Client>( _dao.clientGateway().getAll() );
		Client client = clients.get(0);
		bill.setClient(client);
		bill.setDescription("Test Invoice");
		bill.getProducts().add(new BillProduct(new Product("Product 2", "Test 2", 8), bill.getID(), 8));
		insert(bill);
	}
	
	public Collection<Bill> getAll()
	{
		return _gateway.getAll();
	}
	
	public Collection<Bill> getByClientID(int clientID)
	{
		return _gateway.getAllWithId(clientID);
	}
	
	public Bill getByID(int id)
	{
		return _gateway.getByID(id);
	}
	
	public boolean insert(Bill bill)
	{
		return _gateway.add(bill);
	}
	
	public void delete(Bill bill)
	{
		if (bill.getType() == TransactionType.Invoice)
			bill.cancel();
		
		_gateway.delete(bill);
	}
	
	public void update(Bill bill)
	{
		_gateway.update(bill);
	}
}
