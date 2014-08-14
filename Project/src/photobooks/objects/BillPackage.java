package photobooks.objects;

public class BillPackage extends DBObject implements IBillItem
{
	private Package _package;
	private int _billID;
	private double _purchasePrice;//Store price so it doesn't change after its been bought
	private int _amount;
	private int _order;
	
	public BillPackage()
	{
		_package = null;
		_billID = 0;
		_purchasePrice = 0;
		_amount = 1;
		_order = 0;
	}
	
	public BillPackage(Package newPackage, int billID, double purchasePrice)
	{
		setPackage(newPackage);
		setBillID(billID);
		setPrice(purchasePrice);
		_amount = 1;
		_order = 0;
	}
	
	public BillPackage(Package newPackage, int billID, double purchasePrice, int amount)
	{
		setPackage(newPackage);
		setBillID(billID);
		setPrice(purchasePrice);
		setAmount(amount);
		_order = 0;
	}
	
	public BillPackage(Package newPackage, int billID, double purchasePrice, int amount, int order)
	{
		setPackage(newPackage);
		setBillID(billID);
		setPrice(purchasePrice);
		setAmount(amount);
		setOrder(order);
	}

	public boolean isPackage()
	{
		return true;
	}
	
	public ProductBase getItem()
	{
		return _package;
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
	
	public int getOrder() { return _order; }
	public void setOrder(int order) { _order = order; }
}
