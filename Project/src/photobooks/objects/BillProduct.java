package photobooks.objects;

public class BillProduct extends DBObject
{
	private Product _product;
	private int _billID;
	private double _purchasePrice;//Store price so it doesn't change after its been bought
	private int _amount;
	private int _order;
	
	public BillProduct()
	{
		_product = null;
		_billID = 0;
		_purchasePrice = 0;
		_amount = 1;
		_order = 0;
	}
	
	public BillProduct(Product product, int billID, double purchasePrice)
	{
		setProduct(product);
		setBillID(billID);
		setPrice(purchasePrice);
		_amount = 1;
		_order = 0;
	}
	
	public BillProduct(Product product, int billID, double purchasePrice, int amount)
	{
		setProduct(product);
		setBillID(billID);
		setPrice(purchasePrice);
		setAmount(amount);
		_order = 0;
	}
	
	public BillProduct(Product product, int billID, double purchasePrice, int amount, int order)
	{
		setProduct(product);
		setBillID(billID);
		setPrice(purchasePrice);
		setAmount(amount);
		setOrder(order);
	}
	
	public Product getProduct()
	{
		return _product;
	}
	
	public void setProduct(Product product)
	{
		_product = product;
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
