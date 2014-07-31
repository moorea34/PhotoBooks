package photobooks.presentation;

import photobooks.objects.*;
import photobooks.application.Globals;
import photobooks.business.*;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import acceptanceTests.EventLoop;
import acceptanceTests.Register; 

public class ManageEventsWindow extends Dialog 
{
	protected Event result;
	private Shell shell;
	private EventManager _eventManager;
	private ListViewer listViewer;
	private Button btnRemoveEvent, btnAddEvent, btnClose;
	private ClientManager _clientManager;
	/*final private String[] EVENT_TYPES = {
			EventType.Meeting.toString(),
			EventType.Photoshoot.toString()
	};*/

	public ManageEventsWindow( Shell parent, int style, EventManager manager, ClientManager clientManager ) 
	{
		super(parent, style);
		this.setText("Manage Events");
		_eventManager = manager;
		_clientManager = clientManager;
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setSize(414, 293);
		shell.setText(getText());
		
		setupEventList();
		setupButtons();
	}
	
	private void setupButtons()
	{
		btnRemoveEvent = new Button( shell, SWT.None );
		btnRemoveEvent.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				IStructuredSelection selection = (IStructuredSelection)listViewer.getSelection();
				Event selected = (Event)selection.getFirstElement();
				
				if(selected != null)
				{
					_eventManager.removeEvent(selected);
					listViewer.refresh();
					btnRemoveEvent.setEnabled(false);
				}
			}
		});
		btnRemoveEvent.setBounds(140, 220, 120, 25);
		btnRemoveEvent.setText("Remove Event");
		btnRemoveEvent.setEnabled(false);
		
		btnAddEvent = new Button( shell, SWT.None );
		btnAddEvent.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
	    		AddEventWindow window = new AddEventWindow( shell, SWT.SHELL_TRIM, _clientManager );
	    		Event newEvent = window.open();
	    		
	    		if (newEvent != null)
	    		{
	    			_eventManager.insertEvent( newEvent );
	    			listViewer.refresh();
	    		}
			}
		});
		btnAddEvent.setBounds(10, 220, 120, 25);
		btnAddEvent.setText("Add Event");
		
		btnClose = new Button( shell, SWT.None );
		btnClose.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.dispose();
			}
		});
		btnClose.setBounds(270, 220, 120, 25);
		btnClose.setText("OK");
	}
	
	private void setupEventList()
	{
		listViewer = new ListViewer(shell, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		list.setFont( Globals.getFont() );
		list.setBounds(10, 10, 380, 200);

		listViewer.setContentProvider(new IStructuredContentProvider() 
		{
			public Object[] getElements(Object clients) 
			{
				return _eventManager.getAllEvents().toArray();
			}

			public void dispose() 
			{
			}

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) 
			{				
			}
		});

		listViewer.setInput( _eventManager );

		listViewer.setLabelProvider( new LabelProvider() 
		{

			public String getText(Object element) 
			{
				String result = "";
				if (element instanceof Event)
				{
					result = ((Event) element).getFormattedString( _clientManager );
				}
				
				return result;
			}
		});

		listViewer.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				btnRemoveEvent.setEnabled(true);
			}
		});
	}
}
