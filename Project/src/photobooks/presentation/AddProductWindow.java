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
	private Text nameBox;
	private Text descripBox;
	private Text priceBox;
	private Shell _parent;

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
		shlNew = new Shell(getParent(), getStyle());
		shlNew.setSize(412, 280);
		shlNew.setLayout(new FormLayout());
		
		Button btnOkay = new Button(shlNew, SWT.NONE);
		FormData fd_btnOkay = new FormData();
		fd_btnOkay.width = 120;
		fd_btnOkay.height = 30;
		fd_btnOkay.right = new FormAttachment(100, -6);
		fd_btnOkay.bottom = new FormAttachment(100, -6);
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
		fd_btnCancel.right = new FormAttachment(btnOkay, -6);
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
		
		Group infoBox = new Group(shlNew, SWT.NONE);
		FormData fd_infoBox = new FormData();
		fd_infoBox.bottom = new FormAttachment(btnOkay, -6);
		fd_infoBox.right = new FormAttachment(100, -6);
		fd_infoBox.top = new FormAttachment(0, 6);
		fd_infoBox.left = new FormAttachment(0, 6);
		infoBox.setLayoutData(fd_infoBox);
		infoBox.setText("Product Information");
		
		Label nameLbl = new Label(infoBox, SWT.NONE);
		nameLbl.setText("Name");
		nameLbl.setBounds(10, 23, 93, 15);
		
		Label priceLbl = new Label(infoBox, SWT.NONE);
		priceLbl.setText("Price");
		priceLbl.setBounds(10, 47, 93, 15);
		
		Label descripLbl = new Label(infoBox, SWT.NONE);
		descripLbl.setText("Description");
		descripLbl.setBounds(10, 78, 93, 15);
		
		nameBox = new Text(infoBox, SWT.BORDER);
		nameBox.setBounds(109, 20, 269, 21);
		
		priceBox = new Text(infoBox, SWT.BORDER);
		priceBox.setText("0.00");
		priceBox.setBounds(109, 44, 269, 21);
		
		descripBox = new Text(infoBox, SWT.BORDER);
		descripBox.setBounds(10, 99, 368, 81);

		if(_type.equals("") || _type.equals("Package"))
		{
			shlNew.setText("New Package");
			infoBox.setText("Package Information");
		}
		else
		{
			shlNew.setText("New Product");
			infoBox.setText("Product Information");
		}
	}

	protected void parseInput() 
	{
		double price = 0;
		
		try
		{
			price = Double.parseDouble(priceBox.getText());
		}
		catch(Exception ex)
		{
			MessageBox messageBox = new MessageBox( _parent.getShell(), SWT.OK );
			messageBox.setText("Error!");
			messageBox.setMessage( "Could not parse price, setting to $0.00" );
			messageBox.open();
		}
		
		if(!nameBox.getText().trim().equals(""))
		{
			if(_type.equals("Product"))
			{
				result = new Product(nameBox.getText(), descripBox.getText(), price);
			}
			else
			{
				result = new Package(nameBox.getText(), descripBox.getText(), price);
			}
		}
		
	}
	
}
