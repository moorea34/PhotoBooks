package photobooks.presentation;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import photobooks.application.Utility;
import photobooks.business.ClientManager;
import photobooks.business.ProductManager;
import photobooks.business.ProductPackageManager;
import photobooks.objects.Bill;
import photobooks.objects.Client;
import photobooks.objects.ITransaction.TransactionType;

import org.eclipse.swt.widgets.Button;

import acceptanceTests.EventLoop;
import acceptanceTests.Register; 

public class AddBillWindow extends Dialog {

	protected Object result = null;
	protected Shell shell;
	private ClientManager _clientManager;
	private ProductManager _productManager;
	private ProductPackageManager _packageManager;
	private Composite _composite = null;
	private BillEditor billEditor;
	private Button btnCancel;
	private Button btnSaveAsInvoice;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AddBillWindow(Shell parent, int style, ClientManager clientManager, ProductManager productManager, ProductPackageManager packageManager) {
		super(parent, style);
		setText("Add Bill");
		
		_clientManager = clientManager;
		_productManager = productManager;
		_packageManager = packageManager;
		
		Register.newWindow(this);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open(Client client) {
		createContents();
		
		if (client != null)
			billEditor.setClient(client);
		
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

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle() | SWT.APPLICATION_MODAL);
		shell.setSize(470, 400);
		shell.setMinimumSize(700, 700);
		shell.setText(getText());
		shell.addListener(SWT.Resize, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				Rectangle rect = shell.getClientArea();
				
				if (_composite != null)
				{
					_composite.setSize(rect.width - 20, rect.height - 20);
				}
			}
		});
		
		_composite = new Composite(shell, SWT.NONE);
		_composite.setLocation(10, 10);
		_composite.setSize(450 - 20, 400 - 20);
		_composite.setLayout(new FormLayout());
		
		billEditor = new BillEditor(_composite, SWT.NONE, shell, _clientManager, _productManager, _packageManager);
		billEditor.enableClientSelection(true);
		billEditor.setModify(true);
		
		FormData fd = new FormData();
		fd.left = new FormAttachment(0);
		fd.top = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100, -45);
		billEditor.setLayoutData(fd);
		
		btnCancel = new Button(_composite, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.width = 150;
		fd_btnCancel.height = 30;
		fd_btnCancel.bottom = new FormAttachment(100);
		fd_btnCancel.right = new FormAttachment(100);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.dispose();
			}
		});
		
		btnSaveAsInvoice = new Button(_composite, SWT.NONE);
		FormData fd_btnSaveAsInvoice = new FormData();
		fd_btnSaveAsInvoice.width = 150;
		fd_btnSaveAsInvoice.height = 30;
		fd_btnSaveAsInvoice.bottom = new FormAttachment(btnCancel, 0, SWT.BOTTOM);
		fd_btnSaveAsInvoice.left = new FormAttachment(0, 0);
		btnSaveAsInvoice.setLayoutData(fd_btnSaveAsInvoice);
		btnSaveAsInvoice.setText("Save as Invoice");
		btnSaveAsInvoice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				Bill bill = new Bill();
				bill.setType(TransactionType.Invoice);
				
				billEditor.getBillFromFields(bill);
				bill.setDate(Calendar.getInstance());
				
				if (bill.getClient() != null)
				{
					result = bill;
					shell.dispose();
				}
				else
				{
					MessageBox mb = new MessageBox(shell, SWT.OK);
					
					mb.setMessage("Please select a client.");
					mb.open();
				}
			}
		});
		
		Button btnSaveAsQuote = new Button(_composite, SWT.NONE);
		FormData fd_btnSaveAsQuote = new FormData();
		fd_btnSaveAsQuote.width = 150;
		fd_btnSaveAsQuote.height = 30;
		fd_btnSaveAsQuote.bottom = new FormAttachment(btnCancel, 0, SWT.BOTTOM);
		fd_btnSaveAsQuote.left = new FormAttachment(btnSaveAsInvoice, 6, SWT.RIGHT);
		btnSaveAsQuote.setLayoutData(fd_btnSaveAsQuote);
		btnSaveAsQuote.setText("Save as Quote");
		btnSaveAsQuote.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				Bill bill = new Bill();
				bill.setType(TransactionType.Quote);
				
				billEditor.getBillFromFields(bill);
				bill.setDate(Calendar.getInstance());
				
				if (bill.getClient() != null)
				{
					result = bill;
					shell.dispose();
				}
				else
				{
					MessageBox mb = new MessageBox(shell, SWT.OK);
					
					mb.setMessage("Please select a client.");
					mb.open();
				}
			}
		});
		
		Utility.setFont(shell);
		Utility.centerScreen(shell);
	}
}
