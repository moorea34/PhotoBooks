package photobooks.presentation;

import java.util.*;

import photobooks.application.Utility;
import photobooks.objects.Address;
import photobooks.objects.Address.AddressType;
import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;
import photobooks.objects.PhoneNumber.PhoneNumberType;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.custom.CCombo;

import acceptanceTests.EventLoop;
import acceptanceTests.Register; 

public class AddClientWindow extends Dialog 
{
	private Client result = null;
	private Shell shell = null;
	private ClientEditor _editor;

	public AddClientWindow(Shell parent, int style) 
	{
		super(parent, style);
		setText("New Client");
		
		Register.newWindow(this);
	}

	public Client open() 
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
		shell = new Shell(getParent(), getStyle() | SWT.APPLICATION_MODAL);
		shell.setSize(411, 451);
		shell.setMinimumSize(500, 600);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		Button btnCancel = new Button(shell, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.right = new FormAttachment(100, -10);
		fd_btnCancel.width = 120;
		fd_btnCancel.height = 30;
		fd_btnCancel.bottom = new FormAttachment(100, -10);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.dispose();
			}
		});
		
		Button btnSave = new Button(shell, SWT.NONE);
		FormData fd_btnSave = new FormData();
		fd_btnSave.left = new FormAttachment(0, 10);
		fd_btnSave.top = new FormAttachment(btnCancel, 0, SWT.TOP);
		fd_btnSave.width = 120;
		fd_btnSave.height = 30;
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				Client _client = new Client();
				
				_editor.getClientFromFields(_client);
				result = _client;
				
				shell.dispose();
			}
		});
		
		_editor = new ClientEditor(shell, SWT.NONE);
		FormData fd = new FormData();
		fd.right = new FormAttachment(100, -10);
		fd.top = new FormAttachment(0, 10);
		fd.bottom = new FormAttachment(btnCancel, -6);
		fd.left = new FormAttachment(0, 10);
		_editor.setLayoutData(fd);
		
		Utility.setFont(shell);
		Utility.centerScreen(shell);
	}
}
