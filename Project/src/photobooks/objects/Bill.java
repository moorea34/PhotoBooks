package photobooks.objects;

import java.util.ArrayList;
import java.util.Calendar;

import photobooks.application.Globals;

public class Bill extends DBObject implements ITransaction
{
	private ArrayList<BillProduct> _products;
	private ArrayList<BillPackage> _packages;
	private ArrayList<Payment> _payments;
	private Client _client;
	private TransactionType _type;
	private String _description;
	private Calendar _date;
	private double _gst;
	private double _pst;
	
	public Bill()
	{
		_products = new ArrayList<BillProduct>();
		_packages = new ArrayList<BillPackage>();
		_payments = new ArrayList<Payment>();
		_client = null;
		_type = TransactionType.Quote;
		_description = "";
		_date = Calendar.getInstance();
		_gst = Globals.getGst();
		_pst = Globals.getPst();
	}
	
	public Bill(Client client, TransactionType type, String description, Calendar date, double gst, double pst, ArrayList<BillProduct> products, ArrayList<BillPackage> packages)
	{
		_products = products;
		_packages = packages;
		_payments = new ArrayList<Payment>();
		_client = client;
		_type = type;
		_description = description;
		_date = date;
		_gst = gst;
		_pst = pst;
	}
	
	public Bill(Client client, TransactionType type, String description, Calendar date, double gst, double pst, ArrayList<BillProduct> products, ArrayList<BillPackage> packages, ArrayList<Payment> payments)
	{
		_products = products;
		_packages = packages;
		_payments = payments;
		_client = client;
		_type = type;
		_description = description;
		_date = date;
		_gst = gst;
		_pst = pst;
	}
	
	public String getDisplayName()
	{
		String name = "";
		
		if (_type == TransactionType.Quote)
			name = "Quote ";
		else if (_type == TransactionType.Invoice)
			name = "Invoice ";
		
		name += getID();
		
		return name;
	}
	
	public ArrayList<IBillItem> getItems()
	{
		ArrayList<IBillItem> objs = new ArrayList<IBillItem>();
		int packageIndex = 0, packageCount = _packages.size(), productIndex = 0, productCount = _products.size();
		int totalCount = packageCount + productCount;
		
		for (int i = 0; i < totalCount; ++i) {
			while (productIndex < productCount && _products.get(productIndex) == null) ++productIndex;
			while (packageIndex < packageCount && _packages.get(packageIndex) == null) ++packageIndex;
			
			if (packageIndex < packageCount && productIndex < productCount) {
				if (_products.get(productIndex).getOrder() < _packages.get(packageIndex).getOrder()) {
					objs.add(_products.get(productIndex));
					
					++productIndex;
				}
				else {
					objs.add(_packages.get(packageIndex));
					
					++packageIndex;
				}
			}
			else if (packageIndex >= packageCount && productIndex < productCount) {
				objs.add(_products.get(productIndex));
				
				++productIndex;
			}
			else {
				objs.add(_packages.get(packageIndex));
				
				++packageIndex;
			}
		}
		
		return objs;
	}
	
	public ArrayList<BillProduct> getProducts()
	{
		return _products;
	}
	
	public void setProducts(ArrayList<BillProduct> products)
	{
		// Replace the list
		_products = products;
	}
	
	public ArrayList<BillPackage> getPackages()
	{
		return _packages;
	}
	
	public void setPackages(ArrayList<BillPackage> _packages)
	{
		//Replace the list
		this._packages = _packages;
	}
	
	public ArrayList<Payment> getPayments()
	{
		return _payments;
	}
	
	public void setPayments(ArrayList<Payment> payments)
	{
		// Replace the list
		_payments = payments;
	}
	
	public Client getClient()
	{
		return _client;
	}
	
	public void setClient(Client client)
	{
		_client = client;
	}
	
	public TransactionType getType()
	{
		return _type;
	}
	
	public void setType(TransactionType type)
	{
		_type = type;
	}
	
	public String getDescription()
	{
		return _description;
	}
	
	public void setDescription(String description)
	{
		_description = description;
	}
	
	public Calendar getDate()
	{
		return _date;
	}
	
	public void setDate(Calendar date)
	{
		_date = date;
	}
	
	public double getGst()
	{
		return _gst;
	}
	
	public void setGst(double gst)
	{
		_gst = gst;
	}
	
	public double getPst()
	{
		return _pst;
	}
	
	public void setPst(double pst)
	{
		_pst = pst;
	}
	
	public double getTaxes()
	{
		/*double _total =  subtotal();
		
		return (_total * _gst) + (_total * _pst);*/
		return _gst + _pst;
	}
	
	public void cancel()
	{
		// "Cancel" these products and packages
		for ( BillProduct bp : _products)
		{
			Product p = bp.getProduct();
							
			if (p != null)
				p.cancel(bp.getAmount());
		}
		
		for ( BillPackage bp : _packages)
		{
			Package p = bp.getPackage();
					
			if (p != null)
				p.cancel(bp.getAmount());
		}
	}
	
	public void purchase()
	{
		// "Purchase" these products and packages
		for ( BillProduct bp : _products)
		{
			Product p = bp.getProduct();
									
			if (p != null)
				p.purchase(bp.getAmount());
		}
		
		for ( BillPackage bp : _packages)
		{
			Package p = bp.getPackage();
							
			if (p != null)
				p.purchase(bp.getAmount());
		}
	}
	
	public double subtotal()
	{
		double sum = 0;
		
		for (BillProduct product : _products)
		{
			sum += product.total();
		}
		
		for (BillPackage productPackage : _packages)
		{
			sum += productPackage.total();
		}
		
		return sum;
	}
	
	public double total()
	{
		return subtotal() + getTaxes();
	}
	
	public double affectToBalance()
	{
		switch (_type)
		{
			case Invoice:
				return totalPayments() - total();
			default://Quote
				return 0;
		}
		
	}
	
	public double totalPayments()
	{
		double sum = 0;
		
		for (Payment payment : _payments)
		{
			sum += payment.getAmount();
		}
		
		return sum;
	}
	
	public boolean searchAll(String lowerCase) {
		return getDisplayName().toLowerCase().contains(lowerCase) || _description.toLowerCase().contains(lowerCase);
	}
}
