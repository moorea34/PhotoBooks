package photobooks.gateways;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import photobooks.objects.Product;
import photobooks.objects.ProductPackage;

public class ProductPackageGateway {
	//table 
	private static final String PRODUCT_PACKAGE_TABLE = "PRODUCTPACKAGE";
	
	//columns
	//product package table
	private static final String ID = "ID";
	private static final String PRODUCT_ID = "PRODUCT_ID";
	private static final String PACKAGE_ID = "PACKAGE_ID";
	private static final String AMOUNT = "AMOUNT";
	private static final String ORDER = "IORDER";
	
	private ResultSet _resultSet;
	private Statement _statement;
	private IDao _dao;
	private String _commandString;
	private int _updateCount;
	
	public ProductPackageGateway(IDao dao) {
		_dao = dao;
		_statement = _dao.getStatement();
	}
	
	private ProductPackage resultSetToProduct(ResultSet results) throws SQLException {
		ProductPackage product = null;
		int id, productId, packageId, amount, order;
		Product prod = null;
		
		id = results.getInt(ID);
		productId = results.getInt(PRODUCT_ID);
		packageId = results.getInt(PACKAGE_ID);
		amount = results.getInt(AMOUNT);
		order = results.getInt(ORDER);
		
		prod = _dao.productGateway().getByID(productId);
		
		product = new ProductPackage(prod, packageId, amount, order);
		product.setID(id);
		
		return product;
	}
	
	public ArrayList<ProductPackage> getByPackageID(int packageId) {
		ArrayList<ProductPackage> products = new ArrayList<ProductPackage>();
		ProductPackage productPackage;
		
		try
		{
			_commandString = String.format("SELECT * FROM %s WHERE %s = %d ORDER BY %s", PRODUCT_PACKAGE_TABLE, PACKAGE_ID, packageId, ORDER);
			_resultSet = _statement.executeQuery(_commandString);
			
			while (_resultSet.next())
			{
				productPackage = resultSetToProduct(_resultSet);
				products.add(productPackage);
			}
			
			_resultSet.close();
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return products;
	}
	
	public boolean add(ProductPackage newObj) {
		boolean result = false;
		String values;
		int id = 0;
		
		try
		{
			values = String.format("NULL, %d, %d, %d, %d", newObj.getProduct().getID(), newObj.getPackageId(), newObj.getAmount(), newObj.getOrder());
			
			_commandString = String.format("INSERT INTO %s VALUES(%s)", PRODUCT_PACKAGE_TABLE, values);
			_updateCount = _statement.executeUpdate(_commandString);
			result = _dao.checkWarning(_statement, _updateCount);
			
			if (result)
			{
				_commandString = "CALL IDENTITY()";
				_resultSet = _statement.executeQuery(_commandString);
				
				while (_resultSet.next())
				{
					id = _resultSet.getInt(1);
				}
				
				newObj.setID(id);
				_resultSet.close();
			}
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
		
		return result;
	}
	
	public void update(ProductPackage obj) {
		//boolean result = false;
		String values, where;
		
		try
		{
			values = String.format("%s = %d, %s = %d, %s = %d, %s = %d", PRODUCT_ID, obj.getProduct().getID(), PACKAGE_ID, obj.getPackageId(),
					AMOUNT, obj.getAmount(), ORDER, obj.getOrder());
			
			where = String.format("%s = %d", ID, obj.getID());
			
			_commandString = String.format("UPDATE %s SET %s WHERE %s", PRODUCT_PACKAGE_TABLE, values, where);
			_updateCount = _statement.executeUpdate(_commandString);
			/*result = */_dao.checkWarning(_statement, _updateCount);
		}
		catch (Exception e)
		{
			_dao.processSQLError(e);
		}
	}
	
	public void delete(ProductPackage obj) 
	{
		_dao.globalGateway().delete(PRODUCT_PACKAGE_TABLE, obj.getID());	
	}
}
