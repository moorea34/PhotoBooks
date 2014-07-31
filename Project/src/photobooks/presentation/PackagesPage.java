package photobooks.presentation;
import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import photobooks.application.Globals;
import photobooks.business.ProductManager;
import photobooks.business.ProductPackageManager;
import photobooks.gateways.IDao;
import photobooks.objects.Product;
import photobooks.objects.ProductBase;
import photobooks.objects.Package;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import acceptanceTests.Register;

public class PackagesPage extends Composite
{
	@SuppressWarnings("unused")
	private IDao _dao;
	private Composite _parent;
	//private Shell shell;
	private ProductPackageManager _packageManager;
	private ProductManager _productManager;
	private TreeViewer treeViewer;
	private Text nameBox;
	private Text descripBox;
	private Text priceBox;
	private Tree tree;
	private Button addProdButton, modifyButton, newPackageButton, removeButton, rmProdButton;
	private Group addProductBox, removeProductBox, infoBox;
	private Label descripLbl, nameLbl, priceLbl;
	private ListViewer addProdListViewer, rmProdListViewer, viewProdListViewer;
	private List addProdList, removeProdList, viewProdList;
	private int currSelectedID;
	private String currSelectedType;
	private boolean modifying = false;
	private Button btnNewProduct;
	private ViewerFilter filterViewer;
	private Text packageSearch;
	private ViewerFilter prodFilterViewer;
	
	public PackagesPage(Composite parent, int style, ProductPackageManager packageManager, ProductManager productManager) 
	{
		super(parent, style);
		//shell = parent.getShell();
		Register.newWindow(this);
		_parent = parent;
		currSelectedID = -1;
		currSelectedType = "";
		_packageManager = packageManager;
		_productManager = productManager;
		//insertStubData();
		setupPackageList();
		selectFirstPackage();

	}
	
	private void selectFirstPackage() 
	{
		ArrayList<Package> packs = new ArrayList<Package>( _packageManager.getProductPackageList() );
		if (packs != null && packs.size() > 0)
		{
			Object selected = packs.get(0);
			select(selected);
		}
		else
		{
			currSelectedID = -1;
			currSelectedType = "";
		}
	}

	private void setupPackageList() 
	{
		treeViewer = new TreeViewer(this, SWT.BORDER);
		tree = treeViewer.getTree();
		tree.setBounds(10, 10, 160, 200);
		tree.setFont( Globals.getFont() );
		
		infoBox = new Group(this, SWT.NONE);
		infoBox.setText("Package Information");
		infoBox.setBounds(176, 10, 388, 200);
		
		nameLbl = new Label(infoBox, SWT.NONE);
		nameLbl.setText("Name");
		nameLbl.setBounds(10, 23, 93, 15);
		
		nameBox = new Text(infoBox, SWT.BORDER);
		nameBox.setEditable(false);
		nameBox.setBounds(109, 20, 269, 21);
		
		descripLbl = new Label(infoBox, SWT.NONE);
		descripLbl.setText("Description");
		descripLbl.setBounds(10, 78, 93, 15);
		
		descripBox = new Text(infoBox, SWT.BORDER);
		descripBox.setEditable(false);
		descripBox.setBounds(10, 99, 368, 81);
		
		priceLbl = new Label(infoBox, SWT.NONE);
		priceLbl.setText("Price");
		priceLbl.setBounds(10, 47, 93, 15);
		
		priceBox = new Text(infoBox, SWT.BORDER);
		priceBox.setEditable(false);
		priceBox.setBounds(109, 44, 269, 21);
		
		removeButton = new Button(this, SWT.NONE);
		removeButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				removeSelected();
			}
		});
		removeButton.setBounds(424, 481, 140, 25);
		removeButton.setText("Remove Package");
		
		modifyButton = new Button(this, SWT.NONE);
		modifyButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				toggleModify();
			}
		});
		modifyButton.setText("Modify Package");
		modifyButton.setBounds(278, 481, 140, 25);
		
		newPackageButton = new Button(this, SWT.NONE);
		newPackageButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(modifying)
				{
					toggleModify();
				}
				
				AddProductWindow addItem = new AddProductWindow(_parent.getShell(), SWT.SHELL_TRIM & (~SWT.RESIZE), "Package");
				Object newItem = addItem.open();
				
				if(newItem != null)
						_packageManager.insertProductPackage((Package)newItem);
				
				refreshList();
				
			}
		});
		
		newPackageButton.setText("New Package");
		newPackageButton.setBounds(424, 450, 140, 25);
		
		addProductBox = new Group(this, SWT.NONE);
		addProductBox.setText("Add Products to Package");
		addProductBox.setBounds(176, 216, 388, 111);
		
		addProdButton = new Button(addProductBox, SWT.NONE);
		addProdButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(currSelectedType != "")
				{
					if(currSelectedType.equals("Package"))
					{
						IStructuredSelection selection = (IStructuredSelection)addProdListViewer.getSelection();
						Product selectedProduct = (Product)selection.getFirstElement();
						if(selectedProduct != null)
						{
							Package p = _packageManager.getProductPackage(currSelectedID);
							p.insertProduct(selectedProduct);
							_packageManager.updateProductPackage(p);
							
							setupRemoveProdListViewer(p);
						}
					}
					
				}
			}
		});
		addProdButton.setEnabled(false);
		addProdButton.setText("Add Selected");
		addProdButton.setBounds(238, 63, 140, 25);
		
		removeProductBox = new Group(this, SWT.NONE);
		removeProductBox.setText("Remove Products from Package");
		removeProductBox.setBounds(176, 333, 388, 111);
		
		rmProdButton = new Button(removeProductBox, SWT.NONE);
		rmProdButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				
				if(currSelectedType.equals("Package"))
				{
						IStructuredSelection selection = (IStructuredSelection)rmProdListViewer.getSelection();
						Product selectedProduct = (Product)selection.getFirstElement();
						if(selectedProduct != null)
						{			
							Package p = _packageManager.getProductPackage(currSelectedID);
							p.removeProduct(selectedProduct.getID());
							_packageManager.updateProductPackage(p);
							
							setupRemoveProdListViewer(p);
						}
					
				}
				
			}
		});
		
		rmProdButton.setEnabled(false);
		rmProdButton.setText("Remove Selected");
		rmProdButton.setBounds(238, 63, 140, 25);
		
		rmProdListViewer = new ListViewer(removeProductBox, SWT.BORDER | SWT.V_SCROLL);
		removeProdList = rmProdListViewer.getList();
		removeProdList.setBounds(10, 20, 200, 68);
		
		addProdListViewer = new ListViewer(addProductBox, SWT.BORDER | SWT.V_SCROLL);
		addProdList = addProdListViewer.getList();
		addProdList.setBounds(10, 20, 200, 68);
		
		viewProdListViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
		viewProdList = viewProdListViewer.getList();
		viewProdList.setFont(Globals.getFont());
		viewProdList.setBounds(10, 216, 160, 257);
		
		btnNewProduct = new Button(this, SWT.NONE);
		btnNewProduct.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(modifying)
				{
					toggleModify();
				}
				
				AddProductWindow addItem = new AddProductWindow(_parent.getShell(), SWT.SHELL_TRIM & (~SWT.RESIZE), "Product");
				Object newItem = addItem.open();
				
				if(newItem != null)
						_productManager.insertProduct((Product)newItem);
				
				refreshList();
			}
		});
		btnNewProduct.setText("New Product");
		btnNewProduct.setBounds(278, 450, 140, 25);
		
		packageSearch = new Text(this, SWT.BORDER | SWT.SEARCH);
		packageSearch.setBounds(10, 481, 160, 23);
		
		setupTree();
		setupListViewers();
		
		packageSearch.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent arg0) 
			{
				treeViewer.refresh();
				viewProdListViewer.refresh();
			}
			
		});
	}
	
	private void setupTree() 
	{
		treeViewer.setContentProvider(new ITreeContentProvider() 
		{
			public Object[] getElements(Object packages) 	
			{
				return _packageManager.getProductPackageList().toArray();

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
				return ((Package)arg0).getProducts().toArray();
			}

			@Override
			public Object getParent(Object arg0) 
			{
				return null;
			}

			@Override
			public boolean hasChildren(Object arg0) 
			{
				if(arg0 instanceof Package)
				{
					if(((Package) arg0).getProducts().size() > 0)
						return true;
				}
				
				return false;
			}

		});
		treeViewer.setLabelProvider(new ILabelProvider() 
		{

			public String getText(Object element) 
			{
				return ((ProductBase) element).getName();
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
		
		treeViewer.setInput(_packageManager);
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				Object selected = (Object)selection.getFirstElement();
				setLabels(selected);
				select(selected);
			}

		});
		
		filterViewer = new ViewerFilter() 
		{
		    @Override
		    public boolean select(Viewer viewer, Object parentElement, Object element) 
		    {
		    	if(packageSearch.getText().equals("") || packageSearch.getText().length() < 3)
		    		return true;
		    	
				if (element instanceof Package)
				{
					return ((Package) element).searchAll(packageSearch.getText().toLowerCase());
				}
				else 
				{
					return true;
				}
		    }
		};
		
		treeViewer.addFilter(filterViewer);
		
	}

	protected void toggleModify() 
	{
		if(currSelectedType != "")
		{
			modifying = !modifying;
			nameBox.setEditable(modifying);
			priceBox.setEditable(modifying);
			descripBox.setEditable(modifying);
			addProdButton.setEnabled(modifying);
			rmProdButton.setEnabled(modifying);
			
			if(modifying)
			{
				modifyButton.setText("Done Modifying");
			}
			else
			{
				ProductBase _selected = null;
				if(currSelectedType.equals("Package"))
				{
					modifyButton.setText("Modify Package");
					_selected = _packageManager.getProductPackage(currSelectedID);
				}
				else
				{
					modifyButton.setText("Modify Product");
					_selected = _productManager.getProduct(currSelectedID);
				}
				
				((ProductBase)_selected).setName(nameBox.getText());
				((ProductBase)_selected).setDescription(descripBox.getText());
				
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
				
				((ProductBase)_selected).setPrice(price);
				
				if(currSelectedType.equals("Package"))
				{
					_packageManager.updateProductPackage((Package)_selected);
				}
				else
				{

					_productManager.updateProduct((Product)_selected);
				}
				refreshList();
			}
		}
		
	}

	protected void removeSelected() 
	{
		if(modifying)
		{
			toggleModify();
		}
		
		if(currSelectedType != "")
		{
			
			if(currSelectedType.equals("Package"))
			{
				_packageManager.deleteProductPackage(_packageManager.getProductPackage(currSelectedID));
			}
			else
			{
				_productManager.deleteProduct(_productManager.getProduct(currSelectedID));
			}
		}
		
		
		selectFirstPackage();
		refreshList();
		
	}

	protected void refreshList() 
	{
		String selectedType = currSelectedType;
		int id = currSelectedID;
		
		treeViewer.refresh();
		addProdListViewer.refresh();
		rmProdListViewer.refresh();
		viewProdListViewer.refresh();
		
		if(selectedType.equals("Package"))
		{
			Package p = _packageManager.getProductPackage(id);
			
			if (p != null)
				select(p);
		}
		else if (selectedType.equals("Product"))
		{
			Product p = _productManager.getProduct(id);
			
			if (p != null)
				select(p);
		}
	}
	
	public void refresh()
	{
		this.refreshList();
	}

	private void select(Object selected) 
	{
		if(modifying)
		{
			toggleModify();
		}
		
		clearValues();
		if(selected != null)
		{
			System.out.println("Selected " + selected.getClass().getSimpleName() + " id: " + ((ProductBase)selected).getID());
			nameBox.setText(((ProductBase)selected).getName());
			descripBox.setText(((ProductBase)selected).getDescription());
			priceBox.setText(String.valueOf(((ProductBase)selected).getPrice()));
			currSelectedType = selected.getClass().getSimpleName();
			currSelectedID = ((ProductBase)selected).getID();
			
			if(currSelectedType.equals("Package"))
				setupRemoveProdListViewer((Package)selected);
		}
		else
		{
			currSelectedType = "";
			currSelectedID = -1;
		}
		
	}
	
	private void clearValues() 
	{
		nameBox.setText("");
		descripBox.setText("");
		priceBox.setText("");

		if(rmProdListViewer.getContentProvider() != null)
			rmProdListViewer.setInput(null);
	}

	private void setupListViewers() 
	{
		addProdListViewer.setContentProvider(new IStructuredContentProvider() 
		{
			public Object[] getElements(Object clients) 
			{
				return _productManager.getProductList().toArray();
			}

			public void dispose() 
			{
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
			{

			}
		});

		addProdListViewer.setInput(_productManager);

		addProdListViewer.setLabelProvider(new LabelProvider() 
		{

			public String getText(Object element) 
			{
				return ((Product) element).getName();
			}
		});
		
		viewProdListViewer.setContentProvider(new IStructuredContentProvider() 
		{
			public Object[] getElements(Object clients) 
			{
				return _productManager.getProductList().toArray();
			}

			public void dispose() 
			{
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
			{

			}
		});

		viewProdListViewer.setInput(_productManager);

		viewProdListViewer.setLabelProvider(new LabelProvider() 
		{

			public String getText(Object element) 
			{
				return ((Product) element).getName();
			}
		});
		
		viewProdListViewer.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				Object selected = (Object)selection.getFirstElement();
				setLabels(selected);
				select(selected);
			}

		});
		
		prodFilterViewer = new ViewerFilter() 
		{
		    @Override
		    public boolean select(Viewer viewer, Object parentElement, Object element) 
		    {
		    	if(packageSearch.getText().equals("") || packageSearch.getText().length() < 3)
		    		return true;
		    	
		        if (((Product) element).searchAll(packageSearch.getText().toLowerCase()))
		        {
		            return true;
		        }
		        
		        return false;
		    }
		};
		
		viewProdListViewer.addFilter(prodFilterViewer);
	}

	private void setLabels(Object item)
	{
		
		if(item instanceof Product)
		{
			addProductBox.setVisible(false);
			removeProductBox.setVisible(false);
			infoBox.setText("Product Information");
			modifyButton.setText("Modify Product");
			removeButton.setText("Remove Product");
		}
		else if (item instanceof Package)
		{
			addProductBox.setVisible(true);
			removeProductBox.setVisible(true);
			infoBox.setText("Package Information");
			modifyButton.setText("Modify Package");
			removeButton.setText("Remove Package");
			addProdListViewer.refresh();
			
			setupRemoveProdListViewer((Package)item);
		}
	}

	private void setupRemoveProdListViewer(final Package selected) 
	{
		rmProdListViewer.setContentProvider(new IStructuredContentProvider() 
		{
			public Object[] getElements(Object clients) 
			{
				return selected.getProducts().toArray();
			}

			public void dispose() 
			{
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
			{

			}
		});

		rmProdListViewer.setInput(selected);

		rmProdListViewer.setLabelProvider(new LabelProvider() 
		{

			public String getText(Object element) 
			{
				return ((Product) element).getName();
			}
		});
		
	}
}
