package photobooks.application;

import java.io.BufferedReader;
import java.io.FileReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import photobooks.gateways.Dao;
import photobooks.gateways.IDao;
import photobooks.objects.SWTResourceManager;

public class Globals
{
	//We need a global data access object since the stub stores lists
	private static final String settingsFileName = "settings.txt";
	
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
	
	public static void loadSettings()
	{
		String line, name, value;
		String[] split;
		FileReader file = null;
		BufferedReader fileIn = null;
		
		try
		{
			file = new FileReader(settingsFileName);
			fileIn = new BufferedReader(file);
			
			line = fileIn.readLine();
			
			while (line != null) {
				split = line.split(":");
				
				if (split.length == 2) {
					name = split[0].trim().toLowerCase();
					value = split[1].trim();
					
					switch (name)
					{
						case "gst":
							_gst = Double.parseDouble(value);
							break;
						case "pst":
							_pst = Double.parseDouble(value);
							break;
					}
				}
				
				line = fileIn.readLine();
			}
			
			fileIn.close();
			file.close();
		}
		catch (Exception e)
		{
			System.out.println("Error reading settings file!" + e.toString());
		}
	}
}
