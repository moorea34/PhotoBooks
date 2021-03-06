package photobooks.application;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Hashtable;

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
	private static DecimalFormat exportMoneyFormat = new DecimalFormat("###,###,###,##0.00");
	
	public static final int YEAR_CONST = 1940;
	public static final String autoActivationCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz,";
	private static final String[] MONTHS = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	private static final String[] DAYSOFWEEK = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
	private static Hashtable<String, Integer> _monthMap = null;
	private static final Object lock = new Object();
	
	public static int stringToMonth(String month)
	{
		int result = -1;
		Integer value = null;
		
		if (_monthMap == null)
		{
			synchronized (lock) {
				if (_monthMap == null)
				{
					_monthMap = new Hashtable<String, Integer>();
					
					for (int i = 0; i < MONTHS.length; ++i)
					{
						_monthMap.put(MONTHS[i].trim().toLowerCase(), i);
					}
				}
			}
		}
		
		value = _monthMap.get(month.trim().toLowerCase());
		
		if (value != null)
			result = value;
		
		return result;
	}
	
	public static String monthToString( int dayOfMonth )
	{
		String result = "";
		
		if (dayOfMonth < MONTHS.length && dayOfMonth >= 0)
			result = MONTHS[dayOfMonth];
		
		return result;
	}
	
	public static int dayOfWeek(Calendar day) {
		int dayOfWeek = -1;
		
		switch (day.get(Calendar.DAY_OF_WEEK))
		{
			case Calendar.SUNDAY: dayOfWeek = 0;
				break;
			case Calendar.MONDAY: dayOfWeek = 1;
				break;
			case Calendar.TUESDAY: dayOfWeek = 2;
				break;
			case Calendar.WEDNESDAY: dayOfWeek = 3;
				break;
			case Calendar.THURSDAY: dayOfWeek = 4;
				break;
			case Calendar.FRIDAY: dayOfWeek = 5;
				break;
			case Calendar.SATURDAY: dayOfWeek = 6;
				break;
		}
		
		return dayOfWeek;
	}
	
	public static String dayOfWeekToString( int dayOfWeek )
	{
		String result = "";
		
		if (dayOfWeek < 8 && dayOfWeek > 0)
			result = DAYSOFWEEK[dayOfWeek - 1];
		
		return result;
	}
	
	private static int getNextField(int calendarField) {
		switch (calendarField)
		{
			case Calendar.YEAR:
				calendarField = Calendar.MONTH;
				break;
			case Calendar.MONTH:
				calendarField = Calendar.DAY_OF_MONTH;
				break;
			case Calendar.DAY_OF_MONTH:
				calendarField = Calendar.HOUR_OF_DAY;
				break;
			case Calendar.HOUR_OF_DAY:
				calendarField = Calendar.MINUTE;
				break;
			case Calendar.MINUTE:
				calendarField = Calendar.SECOND;
				break;
			case Calendar.SECOND:
				calendarField = Calendar.MILLISECOND;
				break;
			default:
				calendarField = Calendar.WEEK_OF_YEAR;
				break;
		}
		
		return calendarField;
	}
	
	public static int compareDates(Calendar _date, Calendar date) {
		int result = 0;
		int field = Calendar.YEAR;
		
		if (_date == null && date == null) result = 0;
		else if (_date == null && date != null) result = -1;
		else if (_date != null && date == null) result = 1;
		else
		{
			while (result == 0 && field != Calendar.WEEK_OF_YEAR)
			{
				result = Integer.compare(_date.get(field), date.get(field));
				field = getNextField(field);
			}
			
			if (result < 0) result = -1;
			else if (result > 0) result = 1;
		}
		
		return result;
	}
	
	public static int compareBirthdays(Calendar _date, Calendar date) {
		int result = 0;
		int field = Calendar.MONTH;
		
		if (_date == null && date == null) result = 0;
		else if (_date == null && date != null) result = -1;
		else if (_date != null && date == null) result = 1;
		else
		{
			while (result == 0 && field != Calendar.HOUR_OF_DAY)
			{
				result = Integer.compare(_date.get(field), date.get(field));
				field = getNextField(field);
			}
			
			if (result < 0) result = -1;
			else if (result > 0) result = 1;
		}
		
		return result;
	}
	
	public static boolean calendarsEqual(Calendar c1, Calendar c2)
	{
		return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
	}
	
	public static boolean withinDayOfYearRange( Calendar target, Calendar start, Calendar end )
	{
		return target.get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.DAY_OF_YEAR) &&
			   target.get(Calendar.DAY_OF_YEAR) < end.get(Calendar.DAY_OF_YEAR);
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
	
	public static String getOsName() {
		String name = null;
		
		try
		{
			name = System.getProperty("os.name");
		}
		catch (Exception ex)
		{
			System.out.println("Error getting OS name: " + ex.toString());
		}
		
		return name;
	}
	
	public static DecimalFormat getMoneyFormatter() { return moneyFormat; }
	
	public static String formatDate(Calendar calendar)
	{
		return String.format("%d / %d / %d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
	}
	
	public static String formatDateLong(Calendar calendar)
	{
		if (calendar == null) return "";
		
		return String.format("%s %d, %d", monthToString(calendar.get(Calendar.MONTH)), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
	}
	
	public static String formatTime(Calendar calendar)
	{
		int hour = calendar.get(Calendar.HOUR);
		String amPm = "AM";
		
		if (hour == 0) hour = 12;
		if (calendar.get(Calendar.AM_PM) == Calendar.PM) amPm = "PM";
		
		return String.format("%d:%d %s", hour, calendar.get(Calendar.MINUTE), amPm);
	}
	
	public static String formatMoney(double price)
	{
		if (Math.abs(price) < 0.01)
			return "0.00";
		else
			return moneyFormat.format(price);
	}
	
	public static String formatMoneyExport(double price)
	{
		if (Math.abs(price) < 0.01)
			return "0.00";
		else if (price < 0.00)
			return String.format("(%s)", formatMoneyExport(-price));
		else
			return exportMoneyFormat.format(price);
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
	
	public static String formatSqlString(String str){
		String result = str;
		
		if (result != null) {
			result = result.replace("'", "\\apostrophe");
		}
		
		return result;
	}
	
	public static String unformatSqlString(String str){
		String result = str;
		
		if (result != null){
			result = result.replace("\\apostrophe", "'");
		}
		
		return result;
	}
	
	public static boolean confirmDelete(Shell shell, String item) {
		MessageBox mb = new MessageBox(shell, SWT.YES | SWT.NO);
		
		mb.setText("Confirm delete...");
		mb.setMessage("Are you sure you want to delete " + item + "?");
		
		return mb.open() == SWT.YES;
	}
	
	public static void showErrorMessage(Shell shell, String msg) {
		MessageBox mb = new MessageBox(shell, SWT.OK);
		
		System.out.println(msg);
		
		mb.setMessage(msg);
		mb.open();
	}
	
	public static void showMessage(Shell shell, String msg, String title) {
		MessageBox mb = new MessageBox(shell, SWT.OK);
		
		mb.setMessage(msg);
		mb.setText(title);
		
		mb.open();
	}
	
	public static void copyDatabase(String srcFolder, String destFolder) throws Exception
	{
		copyFile(srcFolder + "/PhotoBooks.properties", destFolder + "/PhotoBooks.properties");
		copyFile(srcFolder + "/PhotoBooks.script", destFolder + "/PhotoBooks.script");
	}
	
	public static void copyFile(String src, String dest) throws Exception
	{
		FileReader reader = new FileReader(src);
		BufferedReader fileIn = new BufferedReader(reader);
		
		FileWriter writer = new FileWriter(dest);
		
		String line = fileIn.readLine();
		
		while (line != null)
		{
			writer.write(line + "\n");
			line = fileIn.readLine();
		}
		
		writer.close();
		
		fileIn.close();
		reader.close();
	}

	public static String getDir(Shell shell) 
	{
		DirectoryDialog dirDialog = new DirectoryDialog(shell, SWT.OPEN);
		dirDialog.setText("Open");
        return dirDialog.open();
	}
	
	public static String getSaveLoc(Shell shell, String defaultName, String[] filterNames, String[] filterExtensions)
	{
		FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		
		fileDialog.setText("Save");
		
		fileDialog.setFilterNames(filterNames);
		fileDialog.setFilterExtensions(filterExtensions);
		
		if (defaultName != null)
			fileDialog.setFileName(defaultName);
		
		return fileDialog.open();
	}
	
	public static String getSaveLocPDF(Shell shell, String defaultName)
	{
		if (defaultName == null) defaultName = "";
		
		return getSaveLoc(shell, defaultName + ".pdf", new String[] { "PDF Files" }, new String[] { "*.pdf" });
	}
	
	public static String getSaveLocTXT(Shell shell, String defaultName)
	{
		if (defaultName == null) defaultName = "";
		
		return getSaveLoc(shell, defaultName + ".txt", new String[] { "txt Files" }, new String[] { "*.txt" });
	}

	public static void openDir(Shell shell, String path)
	{
		File dir = new File(path);
		
		if (Desktop.isDesktopSupported()) 
		{
		    try 
		    {
				Desktop.getDesktop().open(dir);
			}
		    catch (IllegalArgumentException e) 
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
