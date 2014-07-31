package photobooks.presentation;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import photobooks.application.Utility;
import photobooks.objects.Product;
import photobooks.objects.Package;
import acceptanceTests.Register;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment; 

public class AddProductWindow extends Dialog 
{

	protected Object result;
	protected Shell shlNew;
	private String _type;
	private Shell _parent;
	private PackageInfoEditor _packageInfoEditor;

	public AddProductWindow(Shell parent, int style, String type) 
	{
		super(parent, style);
		_parent = parent;
		_type = type;

		Register.newWindow(this);
	}

	public Object open() 
	{
		createContents();
		
		shlNew.open();
		shlNew.layout();
		
		Display display = getParent().getDisplay();
		
		while (!shlNew.isDisposed()) 
		{
			if (!display.readAndDispatch()) 
			{
				display.sleep();
			}
		}
		
		return result;
	}

	private void createContents() 
	{
		shlNew = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlNew.setSize(412, 280);
		shlNew.setMinimumSize(600, 300);
		shlNew.setLayout(new FormLayout());
		
		Button btnOkay = new Button(shlNew, SWT.NONE);
		FormData fd_btnOkay = new FormData();
		fd_btnOkay.bottom = new FormAttachment(100, -6);
		fd_btnOkay.left = new FormAttachment(0, 6);
		fd_btnOkay.width = 120;
		fd_btnOkay.height = 30;
		btnOkay.setLayoutData(fd_btnOkay);
		btnOkay.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				parseInput();
				shlNew.dispose();
			}
		});
		btnOkay.setText("Save");
		
		Button btnCancel = new Button(shlNew, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(btnOkay, 0, SWT.TOP);
		fd_btnCancel.right = new FormAttachment(100, -6);
		fd_btnCancel.height = 30;
		fd_btnCancel.width = 120;
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shlNew.dispose();
			}
		});
		btnCancel.setText("Cancel");
		
		_packageInfoEditor = new PackageInfoEditor(shlNew, SWT.NONE);
		FormData fd_infoBox = new FormData();
		fd_infoBox.bottom = new FormAttachment(btnOkay, -6, SWT.TOP);
		fd_infoBox.top = new FormAttachment(0, 6);
		fd_infoBox.right = new FormAttachment(100, -6);
		fd_infoBox.left = new FormAttachment(0, 6);
		_packageInfoEditor.setLayoutData(fd_infoBox);
		_packageInfoEditor.descripBox.setEditable(true);
		_packageInfoEditor.nameBox.setEditable(true);
		_packageInfoEditor.priceBox.setEditable(true);

		if(_type.equals("") || _type.equals("Package"))
		{
			shlNew.setText("New Package");
			_packageInfoEditor.infoBox.setText("Package Information");
		}
		else
		{
			shlNew.setText("New Product");
			_packageInfoEditor.infoBox.setText("Product Information");
		}
		
		Utility.centerScreen(shlNew);
		Utility.setFont(shlNew);
	}

	protected void parseInput() 
	{
		double price = 0;
		
		try
		{
			price = Double.parseDouble(_packageInfoEditor.priceBox.getText());
		}
		catch(Exception ex)
		{
			MessageBox messageBox = new MessageBox( _parent.getShell(), SWT.OK );
			messageBox.setText("Error!");
			messageBox.setMessage( "Could not parse price, setting to $0.00" );
			messageBox.open();
		}
		
		if(!_packageInfoEditor.nameBox.getText().trim().equals(""))
		{
			if(_type.equals("Product"))
			{
				result = new Product(_packageInfoEditor.nameBox.getText(), _packageInfoEditor.descripBox.getText(), price);
			}
			else
			{
				result = new Package(_packageInfoEditor.nameBox.getText(), _packageInfoEditor.descripBox.getText(), price);
			}
		}
		
	}
	
}
