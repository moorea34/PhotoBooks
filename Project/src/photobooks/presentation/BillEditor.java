package photobooks.presentation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EventObject;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import photobooks.application.Globals;
import photobooks.application.Utility;
import photobooks.business.ClientManager;
import photobooks.business.HtmlGenerator;
import photobooks.business.ProductManager;
import photobooks.business.ProductPackageManager;
import photobooks.objects.Address;
import photobooks.objects.Bill;
import photobooks.objects.BillPackage;
import photobooks.objects.BillProduct;
import photobooks.objects.Client;
import photobooks.objects.ITransaction;
import photobooks.objects.ITransaction.TransactionType;
import photobooks.objects.PhoneNumber;

import org.eclipse.swt.widgets.Button;

import acceptanceTests.Register; 

public class BillEditor extends Composite {
	final String ZERO = "0.0";
	
	private DecimalFormat format = new DecimalFormat("0.00");
	private boolean _clientSelection = false;
	private boolean _modifying = false;
	
	private TableViewer tableViewer;
	private Text tbDescription;
	private Text tbGst;
	private Text tbPst;
	private CCombo cbClient;
	
	private Button btnAddItem;
	private Button btnEditItem;
	private Button btnRemoveItem;
	private Button btnQuoteToInvoice;
	
	private Label lblBillType;
	private Label lblBillNumber;
	private Label lblDateValue;
	private Label lblPhoneNumbers;
	private Label lblAddress;
	private Label lblSubtotalValue;
	private Label lblTaxesValue;
	private Label lblTotalValue;
	private Label lblPst;
	private Label lblGst;
	
	private Shell shell;
	private ClientManager _clientManager;
	private ProductManager _productManager;
	private ProductPackageManager _packageManager;
	private ArrayList<Client> clientList;
	private String[] clientNames;
	
	private Bill _bill = null;
	private Button btnAddPayment;
	private Label lblAmountDueValue;

	public BillEditor(Composite parent, int style, Shell parentShell, ClientManager clientManager, ProductManager productManager, ProductPackageManager packageManager) {
		super(parent, style);
		setLayout(new FormLayout());
		Register.newWindow(this);
		
		shell = parentShell;
		
		_clientManager = clientManager;
		_productManager = productManager;
		_packageManager = packageManager;
		clientList = clientManager.getClientList();
		
		if (clientList.size() == 0)
		{
			_clientManager.insertStubData();
			clientList = clientManager.getClientList();
		}
		
		clientNames = new String[clientList.size()];
		
		for (int i = 0; i < clientNames.length; ++i)
		{
			clientNames[i] = clientList.get(i).getFullName();
		}
		
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0, 5);
		
		lblBillType = new Label(this, SWT.NONE);
		FormData fd_lblBillType = new FormData();
		fd_lblBillType.width = 70;
		lblBillType.setLayoutData(fd_lblBillType);
		//lblBillType.setLayoutData(fd_composite);
		lblBillType.setText("Quote #:");
		
		Label lblClient = new Label(this, SWT.NONE);
		fd_composite.top = new FormAttachment(lblClient, 0, SWT.TOP);
		fd_composite.left = new FormAttachment(lblClient, 99);
		FormData fd_lblClient = new FormData();
		fd_lblClient.top = new FormAttachment(lblBillType, 6);
		fd_lblClient.left = new FormAttachment(lblBillType, 0, SWT.LEFT);
		lblClient.setLayoutData(fd_lblClient);
		lblClient.setText("Client:");
		
		Label lblClientAddress = new Label(this, SWT.NONE);
		FormData fd_lblClientAddress = new FormData();
		fd_lblClientAddress.top = new FormAttachment(lblClient, 6);
		fd_lblClientAddress.left = new FormAttachment(lblBillType, 0, SWT.LEFT);
		lblClientAddress.setLayoutData(fd_lblClientAddress);
		lblClientAddress.setText("Client Address:");
		
		Label lblClientPhone = new Label(this, SWT.NONE);
		FormData fd_lblClientPhone = new FormData();
		fd_lblClientPhone.top = new FormAttachment(lblClientAddress, 6);
		fd_lblClientPhone.left = new FormAttachment(lblBillType, 0, SWT.LEFT);
		lblClientPhone.setLayoutData(fd_lblClientPhone);
		lblClientPhone.setText("Client Phone #'s:");
		
		lblDateValue = new Label(this, SWT.NONE);
		FormData fd_lblDateValue = new FormData();
		fd_lblDateValue.right = new FormAttachment(100);
		fd_lblDateValue.width = 120;
		lblDateValue.setLayoutData(fd_lblDateValue);
		lblDateValue.setAlignment(SWT.RIGHT);
		
		Label lblDate = new Label(this, SWT.NONE);
		FormData fd_lblDate = new FormData();
		fd_lblDate.right = new FormAttachment(lblDateValue, 0, SWT.LEFT);
		lblDate.setLayoutData(fd_lblDate);
		lblDate.setText("Date: ");
		
		lblPhoneNumbers = new Label(this, SWT.NONE);
		FormData fd_lblPhoneNumbers = new FormData();
		fd_lblPhoneNumbers.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		fd_lblPhoneNumbers.top = new FormAttachment(lblClientPhone, 0, SWT.TOP);
		fd_lblPhoneNumbers.left = new FormAttachment(lblClientPhone, 6);
		lblPhoneNumbers.setLayoutData(fd_lblPhoneNumbers);
		
		lblBillNumber = new Label(this, SWT.NONE);
		FormData fd_lblBillNumber = new FormData();
		fd_lblBillNumber.left = new FormAttachment(lblPhoneNumbers, 0, SWT.LEFT);
		fd_lblBillNumber.right = new FormAttachment(lblDate, -5, SWT.LEFT);
		fd_lblBillNumber.top = new FormAttachment(lblBillType, 0, SWT.TOP);
		lblBillNumber.setLayoutData(fd_lblBillNumber);
		
		cbClient = new CCombo(this, SWT.NONE);
		FormData fd_cbClient = new FormData();
		fd_cbClient.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		fd_cbClient.bottom = new FormAttachment(lblClient, 0, SWT.BOTTOM);
		fd_cbClient.left = new FormAttachment(lblPhoneNumbers, 0, SWT.LEFT);
		cbClient.setLayoutData(fd_cbClient);
		cbClient.setItems(clientNames);
		
		Label lblDescription = new Label(this, SWT.NONE);
		FormData fd_lblDescription = new FormData();
		fd_lblDescription.top = new FormAttachment(lblClientPhone, 6);
		fd_lblDescription.left = new FormAttachment(lblBillType, 0, SWT.LEFT);
		lblDescription.setLayoutData(fd_lblDescription);
		lblDescription.setText("Description:");
		
		tbDescription = new Text(this, SWT.BORDER);
		FormData fd_tbDescription = new FormData();
		fd_tbDescription.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		fd_tbDescription.top = new FormAttachment(lblDescription, -3, SWT.TOP);
		fd_tbDescription.left = new FormAttachment(lblPhoneNumbers, 0, SWT.LEFT);
		tbDescription.setLayoutData(fd_tbDescription);
		
		lblAmountDueValue = new Label(this, SWT.NONE);
		lblAmountDueValue.setText("0.0");
		FormData fd_lblAmountDueValue = new FormData();
		fd_lblAmountDueValue.bottom = new FormAttachment(100, 0);
		fd_lblAmountDueValue.right = new FormAttachment(100, 0);
		fd_lblAmountDueValue.width = 100;
		lblAmountDueValue.setLayoutData(fd_lblAmountDueValue);
		lblAmountDueValue.setAlignment(SWT.RIGHT);
		
		Label lblAmountDue = new Label(this, SWT.NONE);
		FormData fd_lblAmountDue = new FormData();
		fd_lblAmountDue.bottom = new FormAttachment(lblAmountDueValue, 0, SWT.BOTTOM);
		fd_lblAmountDue.right = new FormAttachment(lblAmountDueValue, -6, SWT.LEFT);
		lblAmountDue.setLayoutData(fd_lblAmountDue);
		lblAmountDue.setText("Amount Due:");
		
		lblTotalValue = new Label(this, SWT.NONE);
		FormData fd_lblTotalValue = new FormData();
		fd_lblTotalValue.bottom = new FormAttachment(lblAmountDueValue, -6);
		fd_lblTotalValue.right = new FormAttachment(lblAmountDueValue, 0, SWT.RIGHT);
		fd_lblTotalValue.left = new FormAttachment(lblAmountDueValue, 0, SWT.LEFT);
		lblTotalValue.setLayoutData(fd_lblTotalValue);
		lblTotalValue.setAlignment(SWT.RIGHT);
		
		Label lblTotal = new Label(this, SWT.NONE);
		FormData fd_lblTotal = new FormData();
		fd_lblTotal.bottom = new FormAttachment(lblTotalValue, 0, SWT.BOTTOM);
		fd_lblTotal.left = new FormAttachment(lblAmountDue, 0, SWT.LEFT);
		lblTotal.setLayoutData(fd_lblTotal);
		lblTotal.setText("Total:");
		
		Label lblTaxes = new Label(this, SWT.NONE);
		FormData fd_lblTaxes = new FormData();
		fd_lblTaxes.bottom = new FormAttachment(lblTotal, -6);
		fd_lblTaxes.left = new FormAttachment(lblTotal, 0, SWT.LEFT);
		lblTaxes.setLayoutData(fd_lblTaxes);
		lblTaxes.setText("Taxes:");
		
		Label lblSubtotal = new Label(this, SWT.NONE);
		FormData fd_lblSubtotal = new FormData();
		fd_lblSubtotal.left = new FormAttachment(lblTotal, 0, SWT.LEFT);
		lblSubtotal.setLayoutData(fd_lblSubtotal);
		lblSubtotal.setText("SubTotal:");
		
		lblSubtotalValue = new Label(this, SWT.RIGHT);
		FormData fd_lblSubtotalValue = new FormData();
		fd_lblSubtotalValue.left = new FormAttachment(lblTotalValue, 0, SWT.LEFT);
		fd_lblSubtotalValue.top = new FormAttachment(lblSubtotal, 0, SWT.TOP);
		fd_lblSubtotalValue.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		lblSubtotalValue.setLayoutData(fd_lblSubtotalValue);
		
		lblTaxesValue = new Label(this, SWT.RIGHT);
		FormData fd_lblTaxesValue = new FormData();
		fd_lblTaxesValue.left = new FormAttachment(lblTotalValue, 0, SWT.LEFT);
		fd_lblTaxesValue.bottom = new FormAttachment(lblTaxes, 0, SWT.BOTTOM);
		fd_lblTaxesValue.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		lblTaxesValue.setLayoutData(fd_lblTaxesValue);
		
		lblPst = new Label(this, SWT.NONE);
		FormData fd_lblPst = new FormData();
		fd_lblPst.bottom = new FormAttachment(lblTaxes, -6);
		fd_lblPst.left = new FormAttachment(lblTotal, 0, SWT.LEFT);
		lblPst.setLayoutData(fd_lblPst);
		lblPst.setText("PST:");
		
		lblGst = new Label(this, SWT.NONE);
		fd_lblSubtotal.bottom = new FormAttachment(lblGst, -6);
		FormData fd_lblGst = new FormData();
		fd_lblGst.bottom = new FormAttachment(lblPst, -6);
		fd_lblGst.left = new FormAttachment(lblTotal, 0, SWT.LEFT);
		lblGst.setLayoutData(fd_lblGst);
		lblGst.setText("GST:");
		
		tbGst = new Text(this, SWT.BORDER | SWT.RIGHT);
		FormData fd_tbGst = new FormData();
		fd_tbGst.left = new FormAttachment(lblTotalValue, 0, SWT.LEFT);
		fd_tbGst.top = new FormAttachment(lblGst, -3, SWT.TOP);
		fd_tbGst.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		tbGst.setLayoutData(fd_tbGst);
		
		tbPst = new Text(this, SWT.BORDER | SWT.RIGHT);
		FormData fd_tbPst = new FormData();
		fd_tbPst.left = new FormAttachment(lblTotalValue, 0, SWT.LEFT);
		fd_tbPst.top = new FormAttachment(lblPst, -3, SWT.TOP);
		fd_tbPst.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		tbPst.setLayoutData(fd_tbPst);
		
		lblAddress = new Label(this, SWT.NONE);
		FormData fd_lblAddress = new FormData();
		fd_lblAddress.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		fd_lblAddress.left = new FormAttachment(lblPhoneNumbers, 0, SWT.LEFT);
		fd_lblAddress.top = new FormAttachment(lblClientAddress, 0, SWT.TOP);
		lblAddress.setLayoutData(fd_lblAddress);
		
		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(lblSubtotal, -6);
		fd_table.top = new FormAttachment(tbDescription, 6);
		fd_table.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		fd_table.left = new FormAttachment(lblBillType, 0, SWT.LEFT);
		tableViewer.getTable().setLayoutData(fd_table);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
		TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(tableViewer, new FocusCellOwnerDrawHighlighter(tableViewer));

		ColumnViewerEditorActivationStrategy activationSupport = new ColumnViewerEditorActivationStrategy(tableViewer) {
		    protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
		        // Enable editor only with mouse double click
		        if (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION) {
		            EventObject source = event.sourceEvent;
		            if (source instanceof MouseEvent && ((MouseEvent)source).button == 1)
		                return false;

		            return true;
		        }

		        return false;
		    }
		};

		TableViewerEditor.create(tableViewer, focusCellManager, activationSupport, ColumnViewerEditor.TABBING_HORIZONTAL | 
		    ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | 
		    ColumnViewerEditor.TABBING_VERTICAL |
		    ColumnViewerEditor.KEYBOARD_ACTIVATION);
		
		TableViewerColumn tblclmnItem = new TableViewerColumn(tableViewer, SWT.NONE);
		tblclmnItem.getColumn().setWidth(150);
		tblclmnItem.getColumn().setText("Item");
		
		tblclmnItem.setLabelProvider(new ColumnLabelProvider() {
			  @Override
			  public String getText(Object element) {
				  String result = "";
				  
			      if (element != null)
			      {
			    	  if (element instanceof BillProduct)
			    	  {
			    		  if (((BillProduct)element).getProduct() != null)
			    		  {
			    			  result = ((BillProduct)element).getProduct().getName();
			    		  }
			    	  }
			    	  else if (element instanceof BillPackage)
			    	  {
			    		  if (((BillPackage)element).getPackage() != null)
			    		  {
			    			  result = ((BillPackage)element).getPackage().getName();
			    		  }
			    	  }
			      }
			     
			      return result;
			  }
			});
		
		TableViewerColumn tblclmnDescription = new TableViewerColumn(tableViewer, SWT.NONE);
		tblclmnDescription.getColumn().setWidth(200);
		tblclmnDescription.getColumn().setText("Description");
		tblclmnDescription.setLabelProvider(new ColumnLabelProvider() {
			@Override
			  public String getText(Object element) {
				  String result = "";
				  
			      if (element != null)
			      {
			    	  if (element instanceof BillProduct)
			    	  {
			    		  if (((BillProduct)element).getProduct() != null)
			    		  {
			    			  result = ((BillProduct)element).getProduct().getDescription();
			    		  }
			    	  }
			    	  else if (element instanceof BillPackage)
			    	  {
			    		  if (((BillPackage)element).getPackage() != null)
			    		  {
			    			  result = ((BillPackage)element).getPackage().getDescription();
			    		  }
			    	  }
			      }
			      
			      return result;
			}
		});
		
		TableViewerColumn tblclmnAmount = new TableViewerColumn(tableViewer, SWT.NONE);
		tblclmnAmount.getColumn().setWidth(70);
		tblclmnAmount.getColumn().setText("Amount");
		tblclmnAmount.setLabelProvider(new ColumnLabelProvider() {
			@Override
			  public String getText(Object element) {
				  String result = "";
				  
			      if (element != null)
			      {
			    	  if (element instanceof BillProduct)
			    	  {
			    		  if (((BillProduct)element).getProduct() != null)
			    		  {
			    			  result = "" + ((BillProduct)element).getAmount();
			    		  }
			    	  }
			    	  else if (element instanceof BillPackage)
			    	  {
			    		  if (((BillPackage)element).getPackage() != null)
			    		  {
			    			  result = "" + ((BillPackage)element).getAmount();
			    		  }
			    	  }
			      }
			      
			      return result;
			}
		});
		
		TableViewerColumn tblclmnPrice = new TableViewerColumn(tableViewer, SWT.NONE);
		tblclmnPrice.getColumn().setWidth(70);
		tblclmnPrice.getColumn().setText("Price");
		tblclmnPrice.setLabelProvider(new ColumnLabelProvider() {
			@Override
			  public String getText(Object element) {
				  String result = "";
				  
			      if (element != null)
			      {
			    	  if (element instanceof BillProduct)
			    	  {
			    		  if (((BillProduct)element).getProduct() != null)
			    		  {
			    			  result = format.format(((BillProduct)element).getPrice());
			    		  }
			    	  }
			    	  else if (element instanceof BillPackage)
			    	  {
			    		  if (((BillPackage)element).getPackage() != null)
			    		  {
			    			  result = format.format(((BillPackage)element).getPrice());
			    		  }
			    	  }
			      }
			      
			      return result;
			}
		});
		
		btnAddItem = new Button(this, SWT.NONE);
		FormData fd_btnAddProductpackage = new FormData();
		fd_btnAddProductpackage.height = 30;
		fd_btnAddProductpackage.width = 120;
		fd_btnAddProductpackage.top = new FormAttachment(tableViewer.getTable(), 6, SWT.BOTTOM);
		fd_btnAddProductpackage.left = new FormAttachment(0, 0);
		btnAddItem.setLayoutData(fd_btnAddProductpackage);
		btnAddItem.setText("Add Item");
		btnAddItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				EditBillItemWindow editWnd = new EditBillItemWindow(shell, SWT.SHELL_TRIM | (~SWT.RESIZE), _productManager, _packageManager, null);
				
				Object result = editWnd.open();
				
				if (result != null)
				{
					if (_bill != null)
					{
						if (result instanceof BillProduct)
							((BillProduct)result).setBillID(_bill.getID());
						else if (result instanceof BillPackage)
							((BillPackage)result).setBillID(_bill.getID());
					}
					
					tableViewer.add(result);
				}

				updateTotals();
			}
		});
		
		btnEditItem = new Button(this, SWT.NONE);
		FormData fd_btnRemoveProductpackage = new FormData();
		fd_btnRemoveProductpackage.height = 30;
		fd_btnRemoveProductpackage.left = new FormAttachment(0, 0);
		fd_btnRemoveProductpackage.right = new FormAttachment(btnAddItem, 0, SWT.RIGHT);
		fd_btnRemoveProductpackage.top = new FormAttachment(btnAddItem, 6, SWT.BOTTOM);
		btnEditItem.setLayoutData(fd_btnRemoveProductpackage);
		btnEditItem.setText("Edit Item");
		btnEditItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				TableItem[] items = tableViewer.getTable().getSelection();
				
				if (items.length > 0)
				{
					EditBillItemWindow editWnd = new EditBillItemWindow(shell, SWT.SHELL_TRIM | (~SWT.RESIZE), _productManager, _packageManager, items[0].getData());
				
					Object result = editWnd.open();
				
					if (result != null)
					{
						if (_bill != null)
						{
							if (result instanceof BillProduct)
								((BillProduct)result).setBillID(_bill.getID());
							else if (result instanceof BillPackage)
								((BillPackage)result).setBillID(_bill.getID());
						}
						
						items[0].setData(result);
						tableViewer.refresh(items[0].getData());
					}

					updateTotals();
				}
			}
		});
		
		btnRemoveItem = new Button(this, SWT.NONE);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.height = 30;
		fd_btnNewButton.top = new FormAttachment(btnEditItem, 6, SWT.BOTTOM);
		fd_btnNewButton.left = new FormAttachment(0, 0);
		fd_btnNewButton.right = new FormAttachment(btnAddItem, 0, SWT.RIGHT);
		btnRemoveItem.setLayoutData(fd_btnNewButton);
		btnRemoveItem.setText("Remove Item");
		btnRemoveItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				TableItem[] items = tableViewer.getTable().getSelection();
				
				for (TableItem item : items)
				{
					Object obj = item.getData();
					
					tableViewer.remove(obj);
				}
				
				tableViewer.remove(items);

				updateTotals();
			}
		});
		
		btnQuoteToInvoice = new Button(this, SWT.NONE);
		FormData fd_btnQuoteToInvoice = new FormData();
		fd_btnQuoteToInvoice.height = 30;
		fd_btnQuoteToInvoice.width = 120;
		fd_btnQuoteToInvoice.left = new FormAttachment(btnAddItem, 6);
		fd_btnQuoteToInvoice.bottom = new FormAttachment(btnAddItem, 0, SWT.BOTTOM);
		btnQuoteToInvoice.setLayoutData(fd_btnQuoteToInvoice);
		btnQuoteToInvoice.setText("Q -> I");
		btnQuoteToInvoice.setEnabled(false);
		
		btnAddPayment = new Button(this, SWT.NONE);
		FormData fd_btnAddPayment = new FormData();
		fd_btnAddPayment.height = 30;
		fd_btnAddPayment.bottom = new FormAttachment(btnEditItem, 0, SWT.BOTTOM);
		fd_btnAddPayment.left = new FormAttachment(btnEditItem, 6);
		fd_btnAddPayment.right = new FormAttachment(btnQuoteToInvoice, 0, SWT.RIGHT);
		btnAddPayment.setLayoutData(fd_btnAddPayment);
		btnAddPayment.setText("Add Payment");
		btnAddPayment.setEnabled(false);
		
		Button btnExportInvoice = new Button(this, SWT.NONE);
		FormData fd_btnExportInvoice = new FormData();
		fd_btnExportInvoice.height = 30;
		fd_btnExportInvoice.bottom = new FormAttachment(btnRemoveItem, 0, SWT.BOTTOM);
		fd_btnExportInvoice.left = new FormAttachment(btnRemoveItem, 6);
		fd_btnExportInvoice.right = new FormAttachment(btnQuoteToInvoice, 0, SWT.RIGHT);
		btnExportInvoice.setLayoutData(fd_btnExportInvoice);
		btnExportInvoice.setText("Export Bill");
		btnExportInvoice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (_bill != null)
				{
					String line;
					String html = HtmlGenerator.createBill(_bill);
					String fileName = Utility.getSaveLoc(shell, String.format("%s %s %s %d", _bill.getClient().getLastName(), _bill.getClient().getFirstName(), _bill.getType().toString(), _bill.getID()));
					
					if (fileName != null)
					{
						String directory;
						int index = fileName.lastIndexOf('\\');
						int index2 = fileName.lastIndexOf('/');
						
						if (index2 > index)
							index = index2;
						
						if (index < 0)
							directory = "";
						else
							directory = fileName.substring(0, index + 1);
						
						try
						{
							String styleLoc = directory + "style.css";
							FileWriter fileWriter = new FileWriter(fileName);
							FileWriter styleWriter = new FileWriter(styleLoc);
							FileReader styleReader = new FileReader("templates/style.css");
							BufferedReader bufferedReader = new BufferedReader(styleReader);
						
							fileWriter.write(html);
						
							fileWriter.close();
							
							//Copy style sheet
							line = bufferedReader.readLine();
							
							while (line != null)
							{
								styleWriter.write(line);
								line = bufferedReader.readLine();
							}

							styleWriter.close();
							
							bufferedReader.close();
							styleReader.close();
						}
						catch (Exception ex)
						{
							System.out.println("Error exporting bill: " + ex.toString());
						}
					}
				}
			}
		});
		
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
			}
		});
		
		clearTransactionFields();
	}
	
	public void setClient(Client client)
	{
		int i, size;
		
		clearTransactionFields();
		
		if (client != null)
		{
			String output = "";
			
			for (i = 0, size = clientList.size(); i < size; ++i)
			{
				if (client.getID() == clientList.get(i).getID())
				{
					cbClient.select(i);
					break;
				}
			}
			
			for (Address address : client.getAddresses())
			{
				if (address != null && address.getAddress() != null && address.getAddress().length() > 0)
				{
					if (output.length() == 0)
					{
						output = address.getAddress();
					}
					else
					{
						output += ", " + address.getAddress();
					}
				}
			}
			
			lblAddress.setText(output);
			
			output = "";
			
			for (PhoneNumber number : client.getNumbers())
			{
				if (number != null && number.getNumber() != null && number.getNumber().length() > 0)
				{
					if (output.length() == 0)
					{
						output = Utility.formatPhoneNumber(number.getNumber());
					}
					else
					{
						output += ", " + Utility.formatPhoneNumber(number.getNumber());
					}
				}
			}
			
			lblPhoneNumbers.setText(output);
		}
	}
	
	public void updateTotals()
	{
		clearTotalFields();
		
		Bill bill = new Bill();
		double total = 0;
			
		getBillFromFields(bill);
		
		total = bill.total() + bill.getTaxes();
			
		lblSubtotalValue.setText(format.format(bill.total()));
			
		lblTaxesValue.setText(format.format(bill.getTaxes()));
		lblTotalValue.setText(format.format(total));
		
		if (_bill != null)
		{
			total -= _bill.totalPayments();
		}
		
		if (_bill != null && _bill.getType() == TransactionType.Quote)
			lblAmountDueValue.setText("0.00");
		else
		{
			if (Math.abs(total) < 0.01)
				lblAmountDueValue.setText("0.00");
			else
				lblAmountDueValue.setText(format.format(total));
		}
	}
	
	public void setBill(Bill bill)
	{
		_bill = bill;
		
		if (bill != null)
		{
			if (bill.getType() == TransactionType.Quote)
				btnQuoteToInvoice.setEnabled(true);
			else
				btnQuoteToInvoice.setEnabled(false);
			
			if (bill.getType() == TransactionType.Invoice)
				btnAddPayment.setEnabled(true);
			else
				btnAddPayment.setEnabled(false);
			
			setClient(bill.getClient());
			
			switch (bill.getType())
			{
				case Quote:
					lblBillType.setText("Quote #:");
					break;
				case Invoice:
					lblBillType.setText("Invoice #:");
					break;
			}
			
			lblBillNumber.setText("" + bill.getID());
			lblDateValue.setText(Utility.formatDate(bill.getDate()));
			tbDescription.setText(bill.getDescription());

			tableViewer.getTable().setItemCount(0);
			
			if (bill.getType() == ITransaction.TransactionType.Quote || bill.getType() == ITransaction.TransactionType.Invoice)
			{
				tbGst.setText("" + format.format(bill.getGst()));
				tbPst.setText("" + format.format(bill.getPst()));
				
				for (int i = 0; i < bill.getPackages().size(); ++i)
				{
					if (bill.getPackages().get(i) != null)
						tableViewer.add(bill.getPackages().get(i));
				}
				
				for (int i = 0; i < bill.getProducts().size(); ++i)
				{
					if (bill.getProducts().get(i) != null)
						tableViewer.add(bill.getProducts().get(i));
				}
			}
			
			updateTotals();
		}
	}
	
	private void clearTotalFields()
	{
		lblSubtotalValue.setText(ZERO);
		lblTaxesValue.setText(ZERO);
		lblTotalValue.setText(ZERO);
	}
	
	public void clearTransactionFields()
	{
		tableViewer.setItemCount(0);
		lblBillNumber.setText("-");
		lblDateValue.setText("-");
		tbDescription.setText("");
		tbGst.setText(format.format(Globals.getGst()));
		tbPst.setText(format.format(Globals.getPst()));
		
		clearTotalFields();
	}
	
	public void clearAllFields()
	{
		clearTransactionFields();
		
		cbClient.setText("");
		lblAddress.setText("");
		lblPhoneNumbers.setText("");
	}
	
	public void enableClientSelection(boolean enabled)
	{
		_clientSelection = enabled;
		
		cbClient.setEnabled(_clientSelection && _modifying);
	}
	
	public void setModify(boolean modify)
	{
		_modifying = modify;
		
		tbGst.setEnabled(modify);
		tbPst.setEnabled(modify);
		tbDescription.setEnabled(modify);
		
		btnAddItem.setEnabled(modify);
		btnEditItem.setEnabled(modify);
		btnRemoveItem.setEnabled(modify);
		
		cbClient.setEnabled(_clientSelection && modify);
	}
	
	public void addClient(Client newClient)
	{
		int i = 0;

		clientNames = new String[clientNames.length + 1];
		clientList.add(newClient);
		
		for (Client client : clientList)
		{
			clientNames[i] = client.getFormattedName();
			++i;
		}
		
		cbClient.setItems(clientNames);
	}
	
	public void updateClient(Client newClient)
	{
		int i = 0;
		
		if (newClient != null)
		{
			for (Client client : clientList)
			{
				if (client.getID() == newClient.getID())
					break;
			
				++i;
			}
			
			if (i < clientList.size())
			{
				clientList.set(i, newClient);
				clientNames[i] = newClient.getFormattedName();
		
				cbClient.setItems(clientNames);
			}
		}
	}
	
	public void removeClient(Client oldClient)
	{
		int i = 0;

		if (oldClient != null)
		{
			for (Client client : clientList)
			{
				if (client.getID() == oldClient.getID())
					break;
			
				++i;
			}
			
			if (i < clientList.size())
			{
				clientList.remove(i);
				
				clientNames = new String[clientList.size()];
				
				i = 0;
				
				for (Client client : clientList)
				{
					clientNames[i] = client.getFormattedName();
					++i;
				}
			
				cbClient.setItems(clientNames);
			}
		}
	}
	
	public Button getQuoteToInvoiceBtn()
	{
		return btnQuoteToInvoice;
	}
	
	public Button getAddPaymentBtn()
	{
		return btnAddPayment;
	}
	
	public void getBillFromFields(Bill out)
	{
		if (out != null)
		{
			int index = cbClient.getSelectionIndex();
			
			if (index >= 0 && index < clientList.size())
				out.setClient(clientList.get(index));
			
			out.setDescription(tbDescription.getText());
			
			if (out.getType() == ITransaction.TransactionType.Quote || out.getType() == ITransaction.TransactionType.Invoice)
			{
				double gst = 0;
				double pst = 0;
				
				try
				{
					gst = Double.parseDouble(tbGst.getText());
				}
				catch (Exception e)
				{
					
				}
				
				out.setGst(gst);
				
				try
				{
					pst = Double.parseDouble(tbPst.getText());
				}
				catch (Exception e)
				{
					
				}
				
				out.setPst(pst);
				
				out.getPackages().clear();
				out.getProducts().clear();
				
				for (TableItem item : tableViewer.getTable().getItems())
				{
					Object obj = item.getData();
					
					if (obj instanceof BillProduct)
					{
						out.getProducts().add((BillProduct)obj);
					}
					else if (obj instanceof BillPackage)
					{
						out.getPackages().add((BillPackage)obj);
					}
				}
			}
		}
	}
}
