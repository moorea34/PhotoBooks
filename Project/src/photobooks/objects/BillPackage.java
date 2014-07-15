package photobooks.objects;

public class BillPackage extends DBObject
{
	private Package _package;
	private int _billID;
	private double _purchasePrice;//Store price so it doesn't change after its been bought
	private int _amount;
	
	public BillPackage()
	{
		_package = null;
		_billID = 0;
		_purchasePrice = 0;
		_amount = 1;
	}
	
	public BillPackage(Package newPackage, int billID, double purchasePrice)
	{
		setPackage(newPackage);
		setBillID(billID);
		setPrice(purchasePrice);
		_amount = 1;
	}
	
	public BillPackage(Package newPackage, int billID, double purchasePrice, int amount)
	{
		setPackage(newPackage);
		setBillID(billID);
		setPrice(purchasePrice);
		setAmount(amount);
	}
	
	public Package getPackage()
	{
		return _package;
	}
	
	public void setPackage(Package newPackage)
	{
		_package = newPackage;
	}
	
	public int getBillID()
	{
		return _billID;
	}
	
	public void setBillID(int billID)
	{
		_billID = billID;
	}
	
	public double getPrice()
	{
		return _purchasePrice;
	}
	
	public void setPrice(double purchasePrice)
	{
		_purchasePrice = purchasePrice;
	}
	
	public int getAmount()
	{
		return _amount;
	}
	
	public void setAmount(int amount)
	{
		_amount = amount;
	}
	
	public double total()
	{
		return _amount * _purchasePrice;
	}
}
