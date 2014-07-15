package photobooks.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import photobooks.gateways.Dao;
import photobooks.gateways.IDao;
import photobooks.objects.SWTResourceManager;

public class Globals
{
	//We need a global data access object since the stub stores lists
	private static IDao dao = new Dao();
	private static double _gst = 0.12;
	private static double _pst = 0.10;
	
	public static IDao getDao()
	{
		return dao;
	}
	
	public static double getGst()
	{
		return _gst;
	}
	
	public static double getPst()
	{
		return _pst;
	}
	
	public static Font getFont()
	{
		return SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL);
	}
}
