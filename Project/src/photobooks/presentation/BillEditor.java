package photobooks.presentation;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import photobooks.application.Globals;
import photobooks.application.Utility;
import photobooks.business.ClientManager;
import photobooks.business.InvoiceExporter;
import photobooks.business.ProductManager;
import photobooks.business.ProductPackageManager;
import photobooks.objects.Bill;
import photobooks.objects.BillPackage;
import photobooks.objects.BillProduct;
import photobooks.objects.Client;
import photobooks.objects.ITransaction;
import photobooks.objects.ITransaction.TransactionType;
import photobooks.objects.PhoneNumber;
import photobooks.objects.ProductPackage;

import org.eclipse.swt.widgets.Button;

import acceptanceTests.Register; 

public class BillEditor extends Composite {
	final String ZERO = "0.00";
	
	private DecimalFormat format = new DecimalFormat("0.00");
	private boolean _clientSelection = false;
	private boolean _modifying = false;
	
	private TreeViewer packageViewer;
	private Text tbDescription;
	private Text tbGst;
	private Text tbPst;
	private Combo cbClient;
	private SimpleContentProposalProvider ppClient;
	private ContentProposalAdapter paClient;
	
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
	private Client _client = null;
	private Button btnAddPayment;
	private Label lblAmountDueValue;
	private Label lblAccountBalance;
	private Label lblAccountBalanceValue;
	private Button btnMoveUp;
	private Button btnMoveDown;

	public BillEditor(Composite parent, int style, Shell parentShell, ClientManager clientManager, ProductManager productManager, ProductPackageManager packageManager) {
		super(parent, style);
		setLayout(new FormLayout());
		Register.newWindow(this);
		
		shell = parentShell;
		
		_clientManager = clientManager;
		_productManager = productManager;
		_packageManager = packageManager;
		
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
		
		lblAccountBalance = new Label(this, SWT.NONE);
		FormData fd_lblAccountBalance = new FormData();
		fd_lblAccountBalance.top = new FormAttachment(lblClientPhone, 6);
		fd_lblAccountBalance.left = new FormAttachment(lblBillType, 0, SWT.LEFT);
		lblAccountBalance.setLayoutData(fd_lblAccountBalance);
		lblAccountBalance.setText("Account Balance:");
		
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
		fd_lblPhoneNumbers.left = new FormAttachment(lblAccountBalance, 6);
		lblPhoneNumbers.setLayoutData(fd_lblPhoneNumbers);
		
		lblAccountBalanceValue = new Label(this, SWT.RIGHT);
		lblAccountBalanceValue.setText("0.00");
		FormData fd_lblAccountBalanceValue = new FormData();
		fd_lblAccountBalanceValue.bottom = new FormAttachment(lblAccountBalance, 0, SWT.BOTTOM);
		fd_lblAccountBalanceValue.left = new FormAttachment(lblPhoneNumbers, 0, SWT.LEFT);
		fd_lblAccountBalanceValue.right = new FormAttachment(lblPhoneNumbers, 0, SWT.RIGHT);
		lblAccountBalanceValue.setLayoutData(fd_lblAccountBalanceValue);
		
		lblBillNumber = new Label(this, SWT.NONE);
		FormData fd_lblBillNumber = new FormData();
		fd_lblBillNumber.left = new FormAttachment(lblPhoneNumbers, 0, SWT.LEFT);
		fd_lblBillNumber.right = new FormAttachment(lblDate, -5, SWT.LEFT);
		fd_lblBillNumber.top = new FormAttachment(lblBillType, 0, SWT.TOP);
		lblBillNumber.setLayoutData(fd_lblBillNumber);
		
		cbClient = new Combo(this, SWT.NONE);
		FormData fd_cbClient = new FormData();
		fd_cbClient.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		fd_cbClient.top = new FormAttachment(lblClient, -3, SWT.TOP);
		fd_cbClient.left = new FormAttachment(lblPhoneNumbers, 0, SWT.LEFT);
		cbClient.setLayoutData(fd_cbClient);
		cbClient.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = cbClient.getSelectionIndex();
				
				if (index >= 0 && index < clientList.size())
					setClient(clientList.get(index));
			}
		});
		cbClient.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				String text = cbClient.getText();
				int index = 0;
				
				for (String str : clientNames) {
					if (str.equals(text))
						break;
					
					++index;
				}
				
				if (index >= 0 && index < clientList.size()) {
					cbClient.select(index);
					setClient(clientList.get(index));
				}
			}
		});
		
		ppClient = new SimpleContentProposalProvider(new String[] { });
		ppClient.setFiltering(true);
		
		paClient = new ContentProposalAdapter(cbClient, new ComboContentAdapter(), ppClient, null, Utility.autoActivationCharacters.toCharArray());
		paClient.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		
		lblAddress = new Label(this, SWT.NONE);
		FormData fd_lblAddress = new FormData();
		fd_lblAddress.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		fd_lblAddress.left = new FormAttachment(lblPhoneNumbers, 0, SWT.LEFT);
		fd_lblAddress.top = new FormAttachment(lblClientAddress, 0, SWT.TOP);
		lblAddress.setLayoutData(fd_lblAddress);
		
		Label lblDescription = new Label(this, SWT.NONE);
		FormData fd_lblDescription = new FormData();
		fd_lblDescription.top = new FormAttachment(lblAccountBalance, 6);
		fd_lblDescription.left = new FormAttachment(lblBillType, 0, SWT.LEFT);
		lblDescription.setLayoutData(fd_lblDescription);
		lblDescription.setText("Description:");
		
		tbDescription = new Text(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		FormData fd_tbDescription = new FormData();
		fd_tbDescription.top = new FormAttachment(lblDescription, -2, SWT.TOP);
		fd_tbDescription.height = 90;
		fd_tbDescription.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		fd_tbDescription.left = new FormAttachment(lblPhoneNumbers, 0, SWT.LEFT);
		tbDescription.setLayoutData(fd_tbDescription);
		
		lblAmountDueValue = new Label(this, SWT.NONE);
		lblAmountDueValue.setText("0.0");
		FormData fd_lblAmountDueValue = new FormData();
		fd_lblAmountDueValue.bottom = new FormAttachment(100);
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
		tbGst.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				updateTotals();
			}
		});
		
		tbPst = new Text(this, SWT.BORDER | SWT.RIGHT);
		FormData fd_tbPst = new FormData();
		fd_tbPst.left = new FormAttachment(lblTotalValue, 0, SWT.LEFT);
		fd_tbPst.top = new FormAttachment(lblPst, -3, SWT.TOP);
		fd_tbPst.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		tbPst.setLayoutData(fd_tbPst);
		tbPst.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				updateTotals();
			}
		});
		
		packageViewer = new TreeViewer(this, SWT.BORDER);
		Tree packageTree = packageViewer.getTree();
		FormData fd_packageTree = new FormData();
		fd_packageTree.bottom = new FormAttachment(lblSubtotal, -6);
		fd_packageTree.top = new FormAttachment(tbDescription, 6);
		fd_packageTree.right = new FormAttachment(lblDateValue, 0, SWT.RIGHT);
		fd_packageTree.left = new FormAttachment(lblBillType, 0, SWT.LEFT);
		packageTree.setLayoutData(fd_packageTree);
		packageTree.setHeaderVisible(true);
		packageTree.setLinesVisible(true);
		packageViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				String result = null;
				
				if (element != null) {
					result = element.toString();
				}
				
				return result;
			}
		});
		
		TreeColumn treeColumnItem = new TreeColumn(packageTree, SWT.NONE);
		treeColumnItem.setWidth(150);
		treeColumnItem.setText("Item");
		
		TreeColumn treeColumnDesc = new TreeColumn(packageTree, SWT.NONE);
		treeColumnDesc.setWidth(200);
		treeColumnDesc.setText("Description");
		
		TreeColumn treeColumnAmount = new TreeColumn(packageTree, SWT.NONE);
		treeColumnAmount.setWidth(70);
		treeColumnAmount.setText("Amount");
		
		TreeColumn treeColumnPrice = new TreeColumn(packageTree, SWT.NONE);
		treeColumnPrice.setWidth(70);
		treeColumnPrice.setText("Price");
		
		btnMoveUp = new Button(this, SWT.NONE);
		FormData fd_btnMoveUp = new FormData();
		fd_btnMoveUp.height = 30;
		fd_btnMoveUp.width = 120;
		fd_btnMoveUp.top = new FormAttachment(packageViewer.getTree(), 6, SWT.BOTTOM);
		fd_btnMoveUp.left = new FormAttachment(0, 0);
		btnMoveUp.setLayoutData(fd_btnMoveUp);
		btnMoveUp.setText("^");
		btnMoveUp.setEnabled(false);
		btnMoveUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				Tree tree = packageViewer.getTree();
				TreeItem[] items = tree.getSelection();
				int index = -1;
				
				if (items != null && items.length > 0) {
					for (TreeItem item : items) {
						if (item.getData() instanceof BillProduct || item.getData() instanceof BillPackage) {
							index = tree.indexOf(item);
							break;
						}
					}
					
					if (index > 0) {
						Object temp = tree.getItem(index).getData();
						setTreeItem(tree.getItem(index), tree.getItem(index - 1).getData());
						setTreeItem(tree.getItem(index - 1), temp);
						
						tree.setSelection(tree.getItem(index - 1));
					}
				}
			}
		});
		
		btnMoveDown = new Button(this, SWT.NONE);
		FormData fd_btnMoveDown = new FormData();
		fd_btnMoveDown.height = 30;
		fd_btnMoveDown.width = 120;
		fd_btnMoveDown.left = new FormAttachment(btnMoveUp, 6);
		fd_btnMoveDown.bottom = new FormAttachment(btnMoveUp, 0, SWT.BOTTOM);
		btnMoveDown.setLayoutData(fd_btnMoveDown);
		btnMoveDown.setText("v");
		btnMoveDown.setEnabled(false);
		btnMoveDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				Tree tree = packageViewer.getTree();
				TreeItem[] items = tree.getSelection();
				int index = -1;
				
				if (items != null && items.length > 0) {
					for (TreeItem item : items) {
						if (item.getData() instanceof BillProduct || item.getData() instanceof BillPackage) {
							index = tree.indexOf(item);
							break;
						}
					}
					
					if (index < tree.getItemCount() - 1) {
						Object temp = tree.getItem(index).getData();
						setTreeItem(tree.getItem(index), tree.getItem(index + 1).getData());
						setTreeItem(tree.getItem(index + 1), temp);
						
						tree.setSelection(tree.getItem(index + 1));
					}
				}
			}
		});
		
		btnAddItem = new Button(this, SWT.NONE);
		FormData fd_btnAddProductpackage = new FormData();
		fd_btnAddProductpackage.height = 30;
		fd_btnAddProductpackage.width = 120;
		fd_btnAddProductpackage.top = new FormAttachment(btnMoveUp, 6, SWT.BOTTOM);
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
					
					TreeItem item = new TreeItem(packageViewer.getTree(), SWT.NONE);
					setTreeItem(item, result);
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
				TreeItem[] treeItems = packageViewer.getTree().getSelection();
				
				if (treeItems.length > 0)
				{
					EditBillItemWindow editWnd = new EditBillItemWindow(shell, SWT.SHELL_TRIM | (~SWT.RESIZE), _productManager, _packageManager, treeItems[0].getData());
				
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
						
						setTreeItem(treeItems[0], result);
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
				TreeItem[] treeItems = packageViewer.getTree().getSelection();
				
				for (TreeItem item : treeItems)
				{
					if (item.getData() instanceof BillPackage || item.getData() instanceof BillProduct)
						item.dispose();
				}

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
					//String line;
					//String html = HtmlGenerator.createBill(_bill);
					String fileName = Utility.getSaveLoc(shell, String.format("%s %s %s %d", _bill.getClient().getLastName(), _bill.getClient().getFirstName(), _bill.getType().toString(), _bill.getID()));
					
					if (fileName != null)
					{
						/*String directory;
						int index = fileName.lastIndexOf('\\');
						int index2 = fileName.lastIndexOf('/');
						
						if (index2 > index)
							index = index2;
						
						if (index < 0)
							directory = "";
						else
							directory = fileName.substring(0, index + 1);*/
						
						try
						{
							InvoiceExporter.export(_bill, fileName);
						}
						catch (Exception ex)
						{
							MessageBox mb = new MessageBox(shell, SWT.OK);
							
							System.out.println("Error exporting invoice: " + ex.toString());
							
							mb.setMessage("Error exporting invoice.");
							mb.open();
						}
						
						/*try
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
						}*/
					}
				}
			}
		});
		
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
			}
		});
		
		updateCBClientList();
		
		clearTransactionFields();
	}
	
	public void updateCBClientList() {
		if (_modifying) setModify(!_modifying);
		
		clientList = _clientManager.getClientList();
		
		/*if (clientList.size() == 0)
		{
			_clientManager.insertStubData();
			clientList = clientManager.getClientList();
		}*/
		
		clientNames = new String[clientList.size()];
		
		for (int i = 0; i < clientNames.length; ++i)
		{
			clientNames[i] = clientList.get(i).getFullName();
		}
		
		cbClient.setItems(clientNames);
		ppClient.setProposals(clientNames);
		
		if (_bill != null)
			setBill(_bill);
		else if (_client != null)
			setClient(_client);
		else
			clearAllFields();
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
			
			output = String.format("%s %s %s %s", client.getAddress(), client.getCity(), client.getProvince(), client.getPostalCode());
			
			lblAddress.setText(output);
			lblAccountBalanceValue.setText(Utility.formatMoney(client.getAccountBalance()));
			
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
		
		_client = client;
	}
	
	public void updateTotals()
	{
		clearTotalFields();
		
		Bill bill = new Bill();
		double total = 0;
			
		getBillFromFields(bill);
		
		total = bill.total();
			
		lblSubtotalValue.setText(format.format(bill.subtotal()));
			
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
	
	private void setTreeItem(TreeItem item, Object obj) {
		if (obj instanceof BillPackage)
			setBillPackageInTreeItem(item, (BillPackage)obj);
		else if (obj instanceof BillProduct)
			setBillProductInTreeItem(item, (BillProduct)obj);
	}
	
	private void setBillPackageInTreeItem(TreeItem item, BillPackage pack) {
		item.setItemCount(0);
		item.setData(pack);
		item.setText(new String[] { pack.getPackage().getName(), pack.getPackage().getDescription(), String.valueOf(pack.getAmount()), Utility.formatMoney(pack.getPrice()) });
		
		for (ProductPackage product : pack.getPackage().getProducts()) {
			TreeItem childItem = new TreeItem(item, SWT.NONE);
			childItem.setData(product);
			childItem.setText(new String[] { product.getProduct().getName(), product.getProduct().getDescription(), String.valueOf(product.getAmount()) });
		}
	}
	
	private void setBillProductInTreeItem(TreeItem item, BillProduct prod) {
		item.setItemCount(0);
		item.setData(prod);
		item.setText(new String[] { prod.getProduct().getName(), prod.getProduct().getDescription(), String.valueOf(prod.getAmount()), Utility.formatMoney(prod.getPrice()) });
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

			packageViewer.getTree().setItemCount(0);
			
			if (bill.getType() == ITransaction.TransactionType.Quote || bill.getType() == ITransaction.TransactionType.Invoice)
			{
				int packageIndex = 0, packageCount = bill.getPackages().size(), productIndex = 0, productCount = bill.getProducts().size();
				int totalCount = packageCount + productCount;
				
				tbGst.setText("" + format.format(bill.getGst()));
				tbPst.setText("" + format.format(bill.getPst()));
				
				for (int i = 0; i < totalCount; ++i) {
					while (productIndex < productCount && bill.getProducts().get(productIndex) == null) ++productIndex;
					while (packageIndex < packageCount && bill.getPackages().get(packageIndex) == null) ++packageIndex;
					
					if (packageIndex < packageCount && productIndex < productCount) {
						if (bill.getProducts().get(productIndex).getOrder() < bill.getPackages().get(packageIndex).getOrder()) {
							TreeItem treeItem = new TreeItem(packageViewer.getTree(), SWT.NONE);
							setBillProductInTreeItem(treeItem, bill.getProducts().get(productIndex));
							
							++productIndex;
						}
						else {
							TreeItem treeItem = new TreeItem(packageViewer.getTree(), SWT.NONE);
							setBillPackageInTreeItem(treeItem, bill.getPackages().get(packageIndex));
							
							++packageIndex;
						}
					}
					else if (packageIndex >= packageCount && productIndex < productCount) {
						TreeItem treeItem = new TreeItem(packageViewer.getTree(), SWT.NONE);
						setBillProductInTreeItem(treeItem, bill.getProducts().get(productIndex));
						
						++productIndex;
					}
					else {
						TreeItem treeItem = new TreeItem(packageViewer.getTree(), SWT.NONE);
						setBillPackageInTreeItem(treeItem, bill.getPackages().get(packageIndex));
						
						++packageIndex;
					}
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
		lblAmountDueValue.setText(ZERO);
	}
	
	public void clearTransactionFields()
	{
		packageViewer.getTree().setItemCount(0);
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
		lblAccountBalanceValue.setText(ZERO);
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
		btnMoveUp.setEnabled(modify);
		btnMoveDown.setEnabled(modify);
		
		cbClient.setEnabled(_clientSelection && modify);
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
			String text = cbClient.getText();
			int index = 0;
			
			for (String str : clientNames) {
				if (str.equals(text))
					break;
				
				++index;
			}
			
			if (index >= 0 && index < clientList.size())
				out.setClient(clientList.get(index));
			
			out.setDescription(tbDescription.getText());
			
			if (out.getType() == ITransaction.TransactionType.Quote || out.getType() == ITransaction.TransactionType.Invoice)
			{
				double gst = 0;
				double pst = 0;
				int order = 0;
				
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
				
				for (TreeItem item : packageViewer.getTree().getItems())
				{
					Object obj = item.getData();
					
					if (obj instanceof BillProduct)
					{
						((BillProduct)obj).setOrder(order);
						out.getProducts().add((BillProduct)obj);
					}
					else if (obj instanceof BillPackage)
					{
						((BillPackage)obj).setOrder(order);
						out.getPackages().add((BillPackage)obj);
					}
					
					++order;
				}
			}
		}
	}
}
