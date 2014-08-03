package photobooks.application;

import photobooks.presentation.MainWindow;

public class PhotoBooks 
{
	public static void main(String[] args)
	{
		Globals.loadSettings();
		
		new MainWindow();
		
		//Clean when we're using DB
		Globals.getDao().dispose();
		
		System.out.println("Exiting PhotoBooks...");
	}
}
