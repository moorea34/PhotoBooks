package photobooks.presentation;

import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;



import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import photobooks.application.Globals;
import photobooks.business.BillManager;
import photobooks.business.ClientManager;
import photobooks.business.PaymentManager;
import photobooks.business.ProductManager;
import photobooks.business.ProductPackageManager;
import photobooks.objects.Bill;
import photobooks.objects.BillProduct;
import photobooks.objects.BillPackage;
import photobooks.objects.Client;
import photobooks.objects.ITransaction;
import photobooks.objects.ITransaction.TransactionType;
import photobooks.objects.Payment;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;

import acceptanceTests.Register; 

public class BillsPage extends Composite {
	private Shell shell;
	private ClientManager _clientManager;
	private BillManager _billManager;
	private PaymentManager _paymentManager;
	private ProductManager _productManager;
	private ProductPackageManager _packageManager;

	private Client _client;
	private Bill _bill;
	private Payment _payment = null;

	private TreeViewer treeViewer;
	private BillEditor _billEditor;
	private PaymentEditor _paymentEditor;
	private Composite composite;
	private Button btnModifyBill;

	private boolean _modifying = false;
	private Button btnAddBill;
	private Text txtSearchClients;
	private ViewerFilter filterViewer;

	public BillsPage(Composite parent, int style, Shell parentShell, ClientManager clientManager, BillManager billManager, PaymentManager paymentManager, ProductManager productManager, ProductPackageManager packageManager) {
		super(parent, style);
		setLayout(new FormLayout());
		Register.newWindow(this);
		shell = parentShell;

		_clientManager = clientManager;
		_productManager = productManager;
		_packageManager = packageManager;
		_billManager = billManager;
		_paymentManager = paymentManager;

		//if (_billManager.getAll().isEmpty())
		//	_billManager.insertStubData();

		setupClientBillList();

		FormData fd__billEditor = new FormData();
		fd__billEditor.right = new FormAttachment(100, -6);
		fd__billEditor.bottom = new FormAttachment(composite, -10, SWT.BOTTOM);
		fd__billEditor.top = new FormAttachment(composite, 0, SWT.TOP);
		fd__billEditor.left = new FormAttachment(composite, 6);

		_paymentEditor = new PaymentEditor(this, SWT.NONE);
		_paymentEditor.setModify(false);
		_paymentEditor.setVisible(false);
		_paymentEditor.setLayoutData(fd__billEditor);

		_billEditor = new BillEditor(this, SWT.NONE, shell, clientManager, _productManager, _packageManager);
		_billEditor.setLayoutData(fd__billEditor);

		_billEditor.setModify(false);
		_billEditor.enableClientSelection(false);
		_billEditor.getAddPaymentBtn().addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (_bill != null)
				{
					if(_modifying)
					{
						toggleModify();
					}

					Payment payment = new Payment();

					payment.setClient(_client);
					payment.setDate(Calendar.getInstance());//Set for display
					payment.setInvoiceId(_bill.getID());

					AddPaymentWindow paymentWnd = new AddPaymentWindow(shell, SWT.SHELL_TRIM, payment);
					Object result = paymentWnd.open();

					if (result != null)
					{
						payment.setDate(Calendar.getInstance());//Actual save time
						_paymentManager.insert(payment);

						_bill.getPayments().add(payment);
						treeViewer.add(_bill, payment);

						treeViewer.setSelection(new StructuredSelection(payment));
					}
				}
			}
		});

		_billEditor.getQuoteToInvoiceBtn().addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(_bill != null)
				{
					if(_modifying)
					{
						toggleModify();
					}

					Bill invoice = new Bill(_bill.getClient(), TransactionType.Invoice, _bill.getDescription(), Calendar.getInstance(), _bill.getGst(), _bill.getPst(), new ArrayList<BillProduct>(), new ArrayList<BillPackage>());
					Client parent = _bill.getClient();

					for (BillProduct product : _bill.getProducts())
					{
						invoice.getProducts().add(new BillProduct(product.getProduct(), 0, product.getPrice(), product.getAmount()));
					}

					for (BillPackage billPackage : _bill.getPackages())
					{
						invoice.getPackages().add(new BillPackage(billPackage.getPackage(), 0, billPackage.getPrice(), billPackage.getAmount()));
					}

					_billManager.insert(invoice);

					if (parent != null)
					{
						//Get actual tree item reference
						for (TreeItem item : treeViewer.getTree().getItems())
						{
							Object obj = item.getData();

							if (((Client)obj).getID() == parent.getID())
							{
								parent = (Client)obj;
								break;
							}
						}
					}

					treeViewer.add(parent, invoice);
					treeViewer.setSelection(new StructuredSelection(invoice));
				}
			}
		});

		Button btnRemoveBill = new Button(this, SWT.NONE);
		FormData fd_btnRemoveBill = new FormData();
		fd_btnRemoveBill.width = 140;
		fd_btnRemoveBill.height = 30;
		fd_btnRemoveBill.top = new FormAttachment(composite, 6);
		fd_btnRemoveBill.right = new FormAttachment(100, -5);
		btnRemoveBill.setLayoutData(fd_btnRemoveBill);
		btnRemoveBill.setText("Remove Bill");

		btnRemoveBill.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(_bill != null)
				{
					if(_modifying)
					{
						toggleModify();
					}

					if (_billEditor.getVisible())
					{
						_billManager.delete(_bill);
						treeViewer.remove(_bill);

						_billEditor.clearTransactionFields();

						_bill = null;
					}
					else
					{
						_paymentManager.delete(_payment);

						_bill.getPayments().remove(_payment);
						treeViewer.remove(_payment);

						selectBill(_bill);

						_payment = null;
					}
				}
			}
		});

		btnModifyBill = new Button(this, SWT.NONE);
		FormData fd_btnModifyBill = new FormData();
		fd_btnModifyBill.top = new FormAttachment(btnRemoveBill, 0, SWT.TOP);
		fd_btnModifyBill.width = 140;
		fd_btnModifyBill.bottom = new FormAttachment(btnRemoveBill, 0, SWT.BOTTOM);
		fd_btnModifyBill.right = new FormAttachment(btnRemoveBill, -6);
		btnModifyBill.setLayoutData(fd_btnModifyBill);
		btnModifyBill.setText("Modify Bill");
		btnModifyBill.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(_bill != null)
				{
					toggleModify();
				}
				else if (_modifying)
				{
					//Disable modifying
					toggleModify();
				}
			}
		});

		btnAddBill = new Button(this, SWT.NONE);
		btnAddBill.setText("Add Bill");
		FormData fd_btnAddBill = new FormData();
		fd_btnAddBill.width = 140;
		fd_btnAddBill.top = new FormAttachment(btnRemoveBill, 0, SWT.TOP);
		fd_btnAddBill.bottom = new FormAttachment(btnRemoveBill, 0, SWT.BOTTOM);
		fd_btnAddBill.right = new FormAttachment(btnModifyBill, -6);
		btnAddBill.setLayoutData(fd_btnAddBill);
		btnAddBill.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (_modifying)
				{
					//Disable modifying
					toggleModify();
				}

				AddBillWindow addBillWnd = new AddBillWindow(shell, SWT.SHELL_TRIM | SWT.RESIZE, _clientManager, _productManager, _packageManager);

				Object result = addBillWnd.open();

				if (result != null)
				{
					Bill bill = (Bill)result;
					Client parent = bill.getClient();

					_billManager.insert(bill);

					if (parent != null)
					{
						//Get actual reference
						for (TreeItem item : treeViewer.getTree().getItems())
						{
							Object obj = item.getData();

							if (((Client)obj).getID() == parent.getID())
							{
								parent = (Client)obj;
								break;
							}
						}
					}

					treeViewer.add(parent, bill);
					treeViewer.setSelection(new StructuredSelection(bill));
				}
			}
		});

		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
			}
		});

	}

	private void setupClientBillList()
	{
		composite = new Composite(this, SWT.NONE);
		//composite.setBounds(10, 10, 160, 470);
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 10);
		fd_composite.left = new FormAttachment(0, 10);
		fd_composite.bottom = new FormAttachment(100, -37);
		fd_composite.right = new FormAttachment(0, 170);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new TreeColumnLayout());

		treeViewer = new TreeViewer(composite, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 0);
		fd_composite.left = new FormAttachment(0, 0);
		fd_composite.bottom = new FormAttachment(100, 0);
		fd_composite.right = new FormAttachment(100, 0);
		//tree.setBounds(0, 0, 160, 470);
		tree.setLayoutData(fd_composite);
		tree.setFont( Globals.getFont() );

		treeViewer.setContentProvider(new ITreeContentProvider() 
		{
			public Object[] getElements(Object packages) 	
			{
				return _clientManager.getClientList().toArray();

			}

			public void dispose() 
			{
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
			{

			}

			@Override
			public Object[] getChildren(Object arg0) 
			{
				if(arg0 instanceof Client)
				{
					return _billManager.getByClientID(((Client)arg0).getID()).toArray();
				}
				else if (arg0 instanceof Bill)
				{
					return ((Bill)arg0).getPayments().toArray();
				}

				return null;
			}

			@Override
			public Object getParent(Object arg0) 
			{
				return null;
			}

			@Override
			public boolean hasChildren(Object arg0) 
			{
				if(arg0 instanceof Client)
				{
					if(!_billManager.getByClientID(((Client)arg0).getID()).isEmpty())
						return true;
				}
				else if (arg0 instanceof Bill)
				{
					if (((Bill)arg0).getPayments().size() > 0)
						return true;
				}

				return false;
			}

		});

		treeViewer.setLabelProvider(new ILabelProvider() 
		{

			public String getText(Object element) 
			{
				if (element instanceof Client)
					return ((Client) element).getFormattedName();
				else if (element instanceof Payment)
					return ((Payment)element).getDisplayName();
				else if (element instanceof ITransaction)
					return ((ITransaction)element).getDisplayName();

				return "";
			}

			@Override
			public void addListener(ILabelProviderListener arg0) 
			{
			}

			@Override
			public void dispose() 
			{
			}

			@Override
			public boolean isLabelProperty(Object arg0, String arg1)
			{
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener arg0) 
			{
			}

			@Override
			public Image getImage(Object arg0) 
			{
				return null;
			}
		});

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				Object selected = (Object)selection.getFirstElement();

				if (_modifying)
				{
					toggleModify();
				}

				if (selected instanceof Client)
				{
					selectClient((Client)selected);
				}
				else if (selected instanceof Payment)
				{
					selectPayment((Payment)selected);
				}
				else if (selected instanceof Bill)
				{
					selectBill((Bill)selected);
				}
			}

		});

		treeViewer.setInput(_clientManager);

		txtSearchClients = new Text(this, SWT.BORDER | SWT.SEARCH);
		FormData clientSearch = new FormData();
		clientSearch.top = new FormAttachment(composite, 6);
		clientSearch.left = new FormAttachment(composite, 0, SWT.LEFT);
		clientSearch.right = new FormAttachment(composite, 0, SWT.RIGHT);
		clientSearch.height = 18;
		txtSearchClients.setLayoutData(clientSearch);


		txtSearchClients.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent arg0) 
			{
				treeViewer.refresh();
			}

		});

		filterViewer = new ViewerFilter() 
		{
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) 
			{
				if(txtSearchClients.getText().equals("") || txtSearchClients.getText().length() < 3)
					return true;

				if (element instanceof Client)
				{
					return ((Client) element).searchAll(txtSearchClients.getText().toLowerCase());
				}
				else 
				{
					return true;
				}
			}
		};

		treeViewer.addFilter(filterViewer);
	}

	public void selectClient(Client selected)
	{
		if (_modifying)
			toggleModify();

		_client = selected;

		_billEditor.setClient(selected);
	}

	public void selectBill(Bill bill)
	{
		if (_modifying)
			toggleModify();

		_bill = bill;

		if (_bill != null)
		{
			selectClient(_bill.getClient());

			_billEditor.setVisible(true);
			_paymentEditor.setVisible(false);
		}

		_billEditor.setBill(_bill);
	}

	public void selectPayment(Payment payment)
	{
		if (_modifying)
			toggleModify();

		//Set _transaction first as a reference
		if (payment != null)
		{
			for (TreeItem clientItem : treeViewer.getTree().getItems())
			{
				Client client = (Client)clientItem.getData();

				if (client.getID() == payment.getClient().getID())
				{
					for (TreeItem billItem : clientItem.getItems())
					{
						Bill bill = (Bill)billItem.getData();

						if (bill.getID() == payment.getInvoiceId())
						{
							selectBill(bill);
							break;
						}
					}

					break;
				}
			}
		}

		_payment = payment;

		if (_payment != null)
		{
			_paymentEditor.setPayment(_payment);

			_paymentEditor.setVisible(true);
			_billEditor.setVisible(false);
		}
	}

	private void toggleModify()
	{
		if (_modifying)
		{
			if (_billEditor.getVisible())
			{
				_billEditor.setModify(false);

				if (_bill != null)
				{
					_billEditor.getBillFromFields(_bill);
					_billManager.update(_bill);
				}
			}
			else//PaymentEditor
			{
				if (_payment != null)
					_paymentEditor.getPayment(_payment);

				_paymentEditor.setModify(false);
				_paymentManager.update(_payment);
			}

			_modifying = !_modifying;

			btnModifyBill.setText("Modify Bill");
			_billEditor.updateTotals();

			//treeViewer.refresh();
		}
		else
		{
			if (_billEditor.getVisible())
			{
				_billEditor.setModify(true);
			}
			else
			{
				_paymentEditor.setModify(true);
			}

			btnModifyBill.setText("Done Modifying");

			_modifying = !_modifying;
		}
	}
	
	private TreeItem getClientFromTree(int id)
	{
		for (TreeItem item : treeViewer.getTree().getItems())
		{
			Client client = (Client)item.getData();
			
			if (client.getID() == id)
				return item;
		}
		
		return null;
	}
	
	public void addNewClient(Client newClient)
	{
		if (newClient != null)
		{
			treeViewer.add(treeViewer.getInput(), newClient);
			_billEditor.addClient(newClient);
		}
	}
	
	public void updateClient(Client client)
	{
		if (client != null)
		{
			TreeItem treeClient = getClientFromTree(client.getID());
			
			treeClient.setData(client);
			treeViewer.refresh();
			
			_billEditor.updateClient(client);
			
			if (_client != null && client.getID() == _client.getID())
			{
				if (_payment != null)
				{
					//Update client references
					_payment.setClient(client);
					_bill.setClient(client);
					
					selectPayment(_payment);
				}
				else if (_bill != null)
				{
					_bill.setClient(client);
					
					selectBill(_bill);
				}
				else
					selectClient(client);
			}
		}
	}
	
	public void removeClient(Client client)
	{
		if (client != null)
		{
			TreeItem treeClient = getClientFromTree(client.getID());
			
			treeViewer.remove(treeClient.getData());
			
			_billEditor.removeClient(client);
			
			if (_client != null && _client.getID() == client.getID())
			{
				_billEditor.clearTransactionFields();
				_paymentEditor.clearFields();
				
				_client = null;
				_bill = null;
				_payment = null;
			}
		}
	}

	public void refresh()
	{
		if (_modifying)
		{
			toggleModify();
		}

		treeViewer.refresh();
		_billEditor.clearTransactionFields();
	}
}
