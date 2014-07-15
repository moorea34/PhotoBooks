package photobooks.presentation;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

import photobooks.business.ProductManager;
import photobooks.business.ProductPackageManager;
import photobooks.objects.BillPackage;
import photobooks.objects.BillProduct;
import photobooks.objects.Package;
import photobooks.objects.Product;
import acceptanceTests.EventLoop;
import acceptanceTests.Register; 

public class EditBillItemWindow extends Dialog {

	protected Object result = null;
	protected Shell shell;
	private Text tbAmount;
	private Text tbPrice;
	private Label lblDescValue;
	private CCombo cbItem;
	
	private ProductManager _productManager;
	private ProductPackageManager _packageManager;
	
	private Object _billItem;
	private String[] items = null;
	private ArrayList<Object> objects = null;
	private Button btnSave;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EditBillItemWindow(Shell parent, int style, ProductManager productManager, ProductPackageManager packageManager, Object billItem) {
		super(parent, style);
		Register.newWindow(this);
		setText("SWT Dialog");
		
		_productManager = productManager;
		_packageManager = packageManager;
		
		_billItem = billItem;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		
		shell.open();
		shell.layout();
		
		Display display = getParent().getDisplay();
		if(EventLoop.isEnabled())
		{
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(450, 192);
		shell.setText("Edit Item");
		shell.setLayout(new FormLayout());
		
		Label lblItem = new Label(shell, SWT.NONE);
		FormData fd_lblItem = new FormData();
		fd_lblItem.top = new FormAttachment(0, 10);
		fd_lblItem.left = new FormAttachment(0, 10);
		lblItem.setLayoutData(fd_lblItem);
		lblItem.setText("Item:");
		
		Label lblDescription = new Label(shell, SWT.NONE);
		FormData fd_lblDescription = new FormData();
		fd_lblDescription.right = new FormAttachment(0, 81);
		fd_lblDescription.top = new FormAttachment(lblItem, 15);
		fd_lblDescription.left = new FormAttachment(0, 10);
		lblDescription.setLayoutData(fd_lblDescription);
		lblDescription.setText("Description:");
		
		Label lblAmount = new Label(shell, SWT.NONE);
		FormData fd_lblAmount = new FormData();
		fd_lblAmount.top = new FormAttachment(lblDescription, 15);
		fd_lblAmount.left = new FormAttachment(0, 10);
		lblAmount.setLayoutData(fd_lblAmount);
		lblAmount.setText("Amount:");
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(lblAmount, 15);
		fd_lblNewLabel.left = new FormAttachment(lblItem, 0, SWT.LEFT);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("Price:");
		
		lblDescValue = new Label(shell, SWT.NONE);
		FormData fd_lblDescValue = new FormData();
		fd_lblDescValue.right = new FormAttachment(100, -10);
		fd_lblDescValue.bottom = new FormAttachment(lblDescription, 0, SWT.BOTTOM);
		fd_lblDescValue.left = new FormAttachment(lblDescription, 6);
		lblDescValue.setLayoutData(fd_lblDescValue);
		
		cbItem = new CCombo(shell, SWT.BORDER);
		FormData fd_cbItem = new FormData();
		fd_cbItem.bottom = new FormAttachment(lblItem, 0, SWT.BOTTOM);
		fd_cbItem.left = new FormAttachment(lblDescValue, 0, SWT.LEFT);
		fd_cbItem.right = new FormAttachment(lblDescValue, 0, SWT.RIGHT);
		cbItem.setLayoutData(fd_cbItem);
		cbItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				Object item = objects.get(cbItem.getSelectionIndex());
				
				if (item instanceof Product)
				{
					Product prod = (Product)item;
					
					lblDescValue.setText(prod.getDescription());
					tbPrice.setText("" + prod.getPrice());
				}
				else if (item instanceof Package)
				{
					Package pack = (Package)item;
					
					lblDescValue.setText(pack.getDescription());
					tbPrice.setText("" + pack.getPrice());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		objects = new ArrayList<Object>(_packageManager.getProductPackageList());
		objects.addAll(_productManager.getProductList());
		
		items = new String[objects.size()];
		int i = 0;
		
		for (Object obj : objects)
		{
			if (obj instanceof Product)
			{
				items[i] = ((Product)obj).getName();
			}
			else if (obj instanceof Package)
			{
				items[i] = ((Package)obj).getName();
			}
			
			++i;
		}
		
		cbItem.setItems(items);
		
		tbAmount = new Text(shell, SWT.BORDER | SWT.RIGHT);
		FormData fd_tbAmount = new FormData();
		fd_tbAmount.left = new FormAttachment(lblDescValue, 0, SWT.LEFT);
		fd_tbAmount.right = new FormAttachment(lblDescValue, 0, SWT.RIGHT);
		fd_tbAmount.top = new FormAttachment(lblDescValue, 9);
		tbAmount.setLayoutData(fd_tbAmount);
		tbAmount.setText("1");
		
		tbPrice = new Text(shell, SWT.BORDER | SWT.RIGHT);
		FormData fd_tbPrice = new FormData();
		fd_tbPrice.bottom = new FormAttachment(lblNewLabel, 0, SWT.BOTTOM);
		fd_tbPrice.left = new FormAttachment(lblDescValue, 0, SWT.LEFT);
		fd_tbPrice.right = new FormAttachment(lblDescValue, 0, SWT.RIGHT);
		tbPrice.setLayoutData(fd_tbPrice);
		
		Button btnCancel = new Button(shell, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.width = 80;
		fd_btnCancel.bottom = new FormAttachment(100, -10);
		fd_btnCancel.right = new FormAttachment(lblDescValue, 0, SWT.RIGHT);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.dispose();
			}
		});
		
		btnSave = new Button(shell, SWT.NONE);
		FormData fd_btnSave = new FormData();
		fd_btnSave.top = new FormAttachment(btnCancel, 0, SWT.TOP);
		fd_btnSave.left = new FormAttachment(lblItem, 0, SWT.LEFT);
		fd_btnSave.width = 80;
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (cbItem.getSelectionIndex() < 0 || cbItem.getSelectionIndex() >= objects.size())
				{
					MessageBox mb = new MessageBox(shell, SWT.OK);
					
					mb.setMessage("Please select a product or package.");
					mb.open();
				}
				else
				{
					Object obj = objects.get(cbItem.getSelectionIndex());
					Double price = Double.parseDouble(tbPrice.getText());
					Double amount = Double.parseDouble(tbAmount.getText());
					
					if (price == null)
						price = Double.valueOf(0);
					
					if (amount == null)
						amount = Double.valueOf(1);
					
					if (obj instanceof Product)
					{
						BillProduct prod = new BillProduct();
						
						prod.setAmount(amount.intValue());
						prod.setPrice(price);
						prod.setProduct((Product)obj);
						
						if (_billItem instanceof BillProduct)
						{
							prod.setBillID(((BillProduct)_billItem).getBillID());
							prod.setID(((BillProduct)_billItem).getID());
						}
						
						result = prod;
					}
					else if (obj instanceof Package)
					{
						BillPackage prod = new BillPackage();
						
						prod.setAmount(amount.intValue());
						prod.setPrice(price);
						prod.setPackage((Package)obj);
						
						if (_billItem instanceof BillPackage)
						{
							prod.setBillID(((BillPackage)_billItem).getBillID());
							prod.setID(((BillPackage)_billItem).getID());
						}
						
						result = prod;
					}

					shell.dispose();
				}
			}
		});

		setItem();
	}
	
	private void setItem()
	{
		if (_billItem != null)
		{
			if (_billItem instanceof BillPackage)
			{
				BillPackage pack = (BillPackage)_billItem;
				
				if (pack.getPackage() != null)
				{
					lblDescValue.setText(pack.getPackage().getDescription());
					
					for (int i = 0; i < items.length; ++i)
					{
						if (items[i].compareToIgnoreCase(pack.getPackage().getName()) == 0)
						{
							cbItem.setText(items[i]);
							break;
						}
					}
				}
				
				tbAmount.setText("" + pack.getAmount());
				tbPrice.setText("" + pack.getPrice());
			}
			else if (_billItem instanceof BillProduct)
			{
				BillProduct prod = (BillProduct)_billItem;
				
				if (prod.getProduct() != null)
				{
					lblDescValue.setText(prod.getProduct().getDescription());
					
					for (int i = 0; i < items.length; ++i)
					{
						if (items[i].compareToIgnoreCase(prod.getProduct().getName()) == 0)
						{
							cbItem.setText(items[i]);
							break;
						}
					}
				}
				
				tbAmount.setText("" + prod.getAmount());
				tbPrice.setText("" + prod.getPrice());
			}
		}
	}
}
