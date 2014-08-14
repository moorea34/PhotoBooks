package photobooks.objects;

public interface IBillItem {
	public double getPrice();
	public int getAmount();
	public double total();
	public ProductBase getItem();
	public boolean isPackage();
}
