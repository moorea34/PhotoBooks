package photobooks.application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class Utility {

	private static DecimalFormat moneyFormat = new DecimalFormat("0.00");
	
	public static final int YEAR_CONST = 1940;
	private static final String[] MONTHS = {
		"January",
		"February",
		"March",
		"April",
		"May",
		"June",
		"July",	
		"August",
		"September",
		"October",
		"November",
		"December"
		};
	
	public static int stringToMonth(String month)
	{
		int result = -1;
		
		switch (month.toLowerCase().trim())
		{
			case "january": result = 0;
				break;
			case "february": result = 1;
				break;
			case "march": result = 2;
				break;
			case "april": result = 3;
				break;
			case "may": result = 4;
				break;
			case "june": result = 5;
				break;
			case "july": result = 6;
				break;
			case "august": result = 7;
				break;
			case "september": result = 8;
				break;
			case "october": result = 9;
				break;
			case "november": result = 10;
				break;
			case "december": result = 11;
				break;
		}
		
		return result;
	}
	
	public static String monthToString( int dayOfMonth )
	{
		String result = "";
		
		switch (dayOfMonth)
		{
			case 0: result = "January";
					break;
			case 1: result = "February";
					break;
			case 2: result = "March";
					break;
			case 3: result = "April";
					break;
			case 4: result = "May";
					break;
			case 5: result = "June";
					break;
			case 6: result = "July";
					break;
			case 7: result = "August";
					break;
			case 8: result = "September";
					break;
			case 9: result = "October";
					break;
			case 10:result = "November";
					break;
			case 11:result = "December";
					break;
		}
		
		return result;
	}
	
	public static String dayOfWeekToString( int dayOfWeek )
	{
		String result = "";
		
		switch (dayOfWeek)
		{
			case 1: result = "Sunday";
					break;
			case 2: result = "Monday";
					break;
			case 3: result = "Tuesday";
					break;
			case 4: result = "Wednesday";
					break;
			case 5: result = "Thursday";
					break;
			case 6: result = "Friday";
					break;
			case 7: result = "Saturday";
					break;
		}
		
		return result;
	}
	
	public static String[] getDays()
	{
		String[] days = new String[31];
		
		for(int i = 0; i <= 30; i++)
		{
			days[i] = String.valueOf(i + 1);
		}
		
		
		return days;
	}
	
	public static String[] getMonths()
	{
		return MONTHS;
	}
	
	public static String[] getYears()
	{
		int yearDiff = Calendar.getInstance().get(Calendar.YEAR) - YEAR_CONST;
		String years[] = new String[yearDiff + 1];
		
		for(int i = 0; i <= yearDiff; i++)
		{
			years[i] = String.valueOf(i + YEAR_CONST);
		}
		
		return years;
	}
	
	public static DecimalFormat getMoneyFormatter() { return moneyFormat; }
	
	public static String formatDate(Calendar calendar)
	{
		return String.format("%d / %d / %d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
	}
	
	public static String formatMoney(double price)
	{
		if (Math.abs(price) < 0.01)
			return "0.00";
		else
			return moneyFormat.format(price);
	}
	
	public static String formatPhoneNumber(String number)
	{
		String result = "";
		char c;
		int numberCount = 0;
		
		if (number != null)
		{
			for (int i = number.length() - 1; i >= 0; --i)
			{
				c = number.charAt(i);
				
				if (c >= '0' && c <= '9')
				{
					if (numberCount == 4)
						result = " - " + result;
					else if (numberCount == 7)
						result = ") " + result;
					else if (numberCount == 10)
						result = " (" + result;
					
					result = c + result;
					numberCount++;
				}
			}
			
			if (numberCount == 10)
				result = "(" + result;
		}
		
		return result;
	}

	public static String getDir(Shell shell) 
	{
		DirectoryDialog dirDialog = new DirectoryDialog(shell, SWT.OPEN);
		dirDialog.setText("Open");
        return dirDialog.open();
	}
	
	public static String getSaveLoc(Shell shell, String defaultName)
	{
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		
		fileDialog.setText("Save");
		
		fileDialog.setFilterNames(new String[] { "Html Files" });
		fileDialog.setFilterExtensions(new String[] { "*.html" });
		
		if (defaultName != null)
			fileDialog.setFileName(defaultName + ".html");
		
		return fileDialog.open();
	}

	public static void openDir(Shell shell, String path)
	{
		File dir = new File(path);
		if (Desktop.isDesktopSupported()) 
		{
		    try 
		    {
				Desktop.getDesktop().open(dir);
			} catch (IllegalArgumentException e) 
			{
				MessageBox messageBox = new MessageBox( shell, SWT.OK );
				messageBox.setText("Error");
				messageBox.setMessage( "The path associated with this client no longer exists.\n" + path );
				messageBox.open();
			}
		    catch (IOException e)
		    {
		    	e.printStackTrace();
		    }
		}
		
	}
	
	public static void centerScreen(Shell shell)
	{
		Monitor primary = Display.getCurrent().getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    Rectangle rect = shell.getBounds();
	    
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
	    
	    shell.setLocation(x, y);
	}
	
	public static void setFont(Control ctrl) {
		if (ctrl != null) {
			ctrl.setFont(Globals.getFont());
			
			if (ctrl instanceof Composite)
			{
				Composite comp = (Composite)ctrl;
				
				for (Control child : comp.getChildren())
				{
					setFont(child);
				}
			}
		}
	}
}
