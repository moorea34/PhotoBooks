package photobooks.presentation;

import java.util.*;

import photobooks.objects.*;
import photobooks.objects.Event.EventType;
import photobooks.application.Utility;
import photobooks.business.*;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.CCombo;

import acceptanceTests.EventLoop;
import acceptanceTests.Register; 

public class AddEventWindow extends Dialog 
{
	protected Event result;
	protected Shell shell;
	private CCombo day, month, year, clientCombo, eventType;
	private ClientManager _clientManager;
	final private String[] EVENT_TYPES = {
			EventType.Meeting.toString(),
			EventType.Photoshoot.toString()
	};
	private Button btnOkay;

	public AddEventWindow(Shell parent, int style, ClientManager clientManager ) 
	{
		super(parent, style);
		_clientManager = clientManager;
		this.setText("Add Event");
		Register.newWindow(this);
	}

	public Event open() 
	{
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		if(EventLoop.isEnabled())
		{
			while (!shell.isDisposed()) 
			{
				if (!display.readAndDispatch()) 
				{
					display.sleep();
				}
			}
		}
		return result;
	}

	private void createContents() 
	{
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setSize(411, 173);
		shell.setText(getText());
	
		setupButtons();
		setupClientCombo();
		setupEventTypeCombo();
		setupDateCombo();
	}
	
	private void setupDateCombo()
	{
		Label lblDate = new Label(shell, SWT.NONE);
		lblDate.setText("Date");
		lblDate.setBounds(10, 71, 93, 15);
		month = new CCombo(shell, SWT.BORDER);
		month.setEditable(false);
		month.setBounds(187, 68, 107, 21);
		
		year = new CCombo(shell, SWT.BORDER);
		year.setEditable(false);
		year.setBounds(300, 68, 80, 21);
		
		day = new CCombo(shell, SWT.BORDER);
		day.setEditable(false);
		day.setBounds(109, 68, 72, 21);
		
		initDateValues();
	}
	
	private void setupEventTypeCombo()
	{
		eventType = new CCombo(shell, SWT.BORDER);
		eventType.setBounds(109, 44, 271, 21);
		
		Label lblEventType = new Label(shell, SWT.NONE);
		lblEventType.setText("Event Type:");
		lblEventType.setBounds(10, 47, 93, 15);
		
		eventType.setItems( EVENT_TYPES );
		eventType.select(0);
	}
	
	private void setupClientCombo()
	{
		Label lblClient = new Label(shell, SWT.NONE);
		lblClient.setText("Client");
		lblClient.setBounds(10, 23, 93, 15);
		
		clientCombo = new CCombo(shell, SWT.BORDER);
		clientCombo.setBounds(109, 20, 271, 21);
		
		initClientValues();
	}
	
	private void setupButtons()
	{
		btnOkay = new Button(shell, SWT.NONE);
		btnOkay.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				parseInput();
				shell.dispose();
			}
		});
		btnOkay.setBounds(305, 100, 75, 25);
		btnOkay.setText("OK");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.dispose();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(224, 100, 75, 25);
	}
	
	private void parseInput()
	{
		if (month.getSelectionIndex() == -1 || year.getSelectionIndex() == -1 || day.getSelectionIndex() == -1 ||
			clientCombo.getSelectionIndex() == -1 || eventType.getSelectionIndex() == -1 )
		{
			return;
		}
		
		Calendar date = new GregorianCalendar();
		date.set(Integer.parseInt( year.getItem(year.getSelectionIndex())), month.getSelectionIndex(), day.getSelectionIndex() + 1);
		
		EventType type = EventType.valueOf( eventType.getItem( eventType.getSelectionIndex() ) );
		
		Client client = _clientManager.getClientList().get( clientCombo.getSelectionIndex() );
		
		result = new Event( type, date, client.getID() );
	}
	
	private void initDateValues() 
	{
		
		day.setItems( Utility.getDays() );
		month.setItems( Utility.getMonths() );
		year.setItems( Utility.getYears() );
		
		day.select(0);
		month.select(0);
		year.select(year.getItemCount() - 1);
	}
	
	private void initClientValues()
	{
		ArrayList<Client> clients = _clientManager.getClientList();
		String[] values = new String[clients.size()];
		
		for (int i = 0; i < clients.size(); i++)
		{
			values[i] = clients.get(i).getFormattedName();
		}
		
		clientCombo.setItems( values );
		clientCombo.select(0);
	}	
}
