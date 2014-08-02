package photobooks.objects;

public class ProductPackage extends DBObject {
	private Product _product;
	private int _packageId;
	private int _amount;
	private int _order;//used for sorting
	
	//Constructors
	public ProductPackage(Product product, int packageId) {
		this(product, packageId, 1, 0);
	}
	
	public ProductPackage(Product product, int packageId, int amount, int order) {
		_product = product;
		_packageId = packageId;
		_amount = amount;
		_order = order;
	}
	
	//Getters
	public Product getProduct() { return _product; }
	public int getPackageId() { return _packageId; }
	public int getAmount() { return _amount; }
	public int getOrder() { return _order; }
	
	//Setters
	public void setProduct(Product product) { _product = product; }
	public void setPackageId(int id) { _packageId = id; }
	public void setAmount(int amount) { _amount = amount; }
	public void setOrder(int order) { _order = order; }
}
