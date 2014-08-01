package photobooks.gateways;

import java.sql.Statement;

import photobooks.objects.*;
import photobooks.objects.Package;

public interface IDao
{
	public void commitChanges();
	public void dispose();
	
	public Statement getStatement();
	public void processSQLError(Exception e);
	public boolean checkWarning(Statement _statement, int _updateCount);
	
	public GlobalGateway globalGateway();
	public IGateway<Client> clientGateway();
	public IGateway<Product> productGateway();
	public IGateway<Package> packageGateway();
	public IGateway<Bill> billGateway();
	public IGateway<Event> eventGateway();
	public IConditionalGateway<Payment> paymentGateway();
	public IConditionalGateway<PhoneNumber> phoneNumberGateway();
	public IConditionalGateway<BillProduct> billProductGateway();
	public IConditionalGateway<BillPackage> billPackageGateway();
	public ITypeGateway typeGateway();
}
