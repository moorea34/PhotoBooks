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
import photobooks.application.Utility;
import photobooks.business.ProductManager;
import photobooks.business.ProductPackageManager;
import photobooks.gateways.IDao;
import photobooks.objects.Product;
import photobooks.objects.ProductBase;
import photobooks.objects.Package;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import acceptanceTests.Register;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class PackagesPage extends Composite
{
	@SuppressWarnings("unused")
	private IDao _dao;
	private Composite _parent;
	//private Shell shell;
	private ProductPackageManager _packageManager;
	private ProductManager _productManager;
	private TreeViewer treeViewer;
	private Tree tree;
	private Button addProdButton, modifyButton, newPackageButton, removeButton, rmProdButton;
	private Group addProductBox, removeProductBox;
	private ListViewer addProdListViewer, rmProdListViewer, viewProdListViewer;
	private List addProdList, removeProdList, viewProdList;
	private int currSelectedID;
	private String currSelectedType;
	private boolean modifying = false;
	private Button btnNewProduct;
	private ViewerFilter filterViewer;
	private Text packageSearch;
	private ViewerFilter prodFilterViewer;
	
	private PackageInfoEditor _packageInfo;
	
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
		setLayout(new FormLayout());
		treeViewer = new TreeViewer(this, SWT.BORDER);
		tree = treeViewer.getTree();
		FormData fd_tree = new FormData();
		fd_tree.width = 163;
		fd_tree.bottom = new FormAttachment(50, -28);
		fd_tree.top = new FormAttachment(0, 10);
		fd_tree.left = new FormAttachment(0, 10);
		tree.setLayoutData(fd_tree);
		tree.setFont( Globals.getFont() );
		
		viewProdListViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
		viewProdList = viewProdListViewer.getList();
		FormData fd_viewProdList = new FormData();
		fd_viewProdList.right = new FormAttachment(tree, 0, SWT.RIGHT);
		fd_viewProdList.left = new FormAttachment(tree, 0, SWT.LEFT);
		fd_viewProdList.top = new FormAttachment(tree, 6);
		fd_viewProdList.bottom = new FormAttachment(100, -37);
		viewProdList.setLayoutData(fd_viewProdList);
		viewProdList.setFont(Globals.getFont());
		
		packageSearch = new Text(this, SWT.BORDER | SWT.SEARCH);
		FormData fd_packageSearch = new FormData();
		fd_packageSearch.height = 18;
		fd_packageSearch.right = new FormAttachment(tree, 0, SWT.RIGHT);
		fd_packageSearch.left = new FormAttachment(tree, 0, SWT.LEFT);
		fd_packageSearch.top = new FormAttachment(viewProdList, 6);
		packageSearch.setLayoutData(fd_packageSearch);
		
		packageSearch.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent arg0) 
			{
				treeViewer.refresh();
				viewProdListViewer.refresh();
			}
			
		});
		
		//TODO
		_packageInfo = new PackageInfoEditor(this, SWT.NONE);
		FormData fd = new FormData();
		fd.left = new FormAttachment(tree, 6, SWT.RIGHT);
		fd.right = new FormAttachment(100, -6);
		fd.top = new FormAttachment(tree, 0, SWT.TOP);
		fd.bottom = new FormAttachment(35, 0);
		_packageInfo.setLayoutData(fd);
		
		addProductBox = new Group(this, SWT.NONE);
		addProductBox.setLayout(new FormLayout());
		FormData fd_addProductBox = new FormData();
		fd_addProductBox.left = new FormAttachment(_packageInfo, 0, SWT.LEFT);
		fd_addProductBox.right = new FormAttachment(_packageInfo, 0, SWT.RIGHT);
		fd_addProductBox.top = new FormAttachment(_packageInfo, 6);
		fd_addProductBox.bottom = new FormAttachment(60, 0);
		addProductBox.setLayoutData(fd_addProductBox);
		addProductBox.setText("Add Products to Package");
		
		addProdListViewer = new ListViewer(addProductBox, SWT.BORDER | SWT.V_SCROLL);
		addProdList = addProdListViewer.getList();
		FormData fd_addProdList = new FormData();
		fd_addProdList.bottom = new FormAttachment(100, -6);
		fd_addProdList.right = new FormAttachment(60, 0);
		fd_addProdList.top = new FormAttachment(0, 6);
		fd_addProdList.left = new FormAttachment(0, 6);
		addProdList.setLayoutData(fd_addProdList);
		addProdListViewer.getList().addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent evt)
			{
				if (evt.keyCode == SWT.TRAVERSE_RETURN || evt.keyCode == SWT.CR || evt.keyCode == SWT.LF || evt.character == '\r' || evt.character == '\n')
				{
					if (modifying) {
						addProdButton.notifyListeners(SWT.Selection, new Event());
					}
				}
			}

			public void keyReleased(KeyEvent evt) {}
		});
		addProdListViewer.getList().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				if (modifying) {
					addProdButton.notifyListeners(SWT.Selection, new Event());
				}
			}
		});
		
		addProdButton = new Button(addProductBox, SWT.NONE);
		FormData fd_addProdButton = new FormData();
		fd_addProdButton.width = 150;
		fd_addProdButton.height = 30;
		fd_addProdButton.bottom = new FormAttachment(addProdList, 0, SWT.BOTTOM);
		fd_addProdButton.left = new FormAttachment(addProdList, 6);
		addProdButton.setLayoutData(fd_addProdButton);
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
		
		removeProductBox = new Group(this, SWT.NONE);
		removeProductBox.setLayout(new FormLayout());
		FormData fd_removeProductBox = new FormData();
		fd_removeProductBox.right = new FormAttachment(_packageInfo, 0, SWT.RIGHT);
		fd_removeProductBox.left = new FormAttachment(_packageInfo, 0, SWT.LEFT);
		fd_removeProductBox.top = new FormAttachment(addProductBox, 6);
		fd_removeProductBox.bottom = new FormAttachment(87, 0);
		removeProductBox.setLayoutData(fd_removeProductBox);
		removeProductBox.setText("Remove Products from Package");
		
		rmProdListViewer = new ListViewer(removeProductBox, SWT.BORDER | SWT.V_SCROLL);
		removeProdList = rmProdListViewer.getList();
		FormData fd_removeProdList = new FormData();
		fd_removeProdList.bottom = new FormAttachment(100, -6);
		fd_removeProdList.right = new FormAttachment(60);
		fd_removeProdList.top = new FormAttachment(0, 6);
		fd_removeProdList.left = new FormAttachment(0, 6);
		removeProdList.setLayoutData(fd_removeProdList);
		removeProdList.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent evt)
			{
				if (evt.keyCode == SWT.TRAVERSE_RETURN || evt.keyCode == SWT.BS || evt.keyCode == SWT.CR || evt.keyCode == SWT.LF || evt.character == '\r' || evt.character == '\n' || evt.character == '\b')
				{
					if (modifying) {
						rmProdButton.notifyListeners(SWT.Selection, new Event());
					}
				}
			}

			public void keyReleased(KeyEvent evt) {}
		});
		removeProdList.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				if (modifying) {
					rmProdButton.notifyListeners(SWT.Selection, new Event());
				}
			}
		});
		
		rmProdButton = new Button(removeProductBox, SWT.NONE);
		FormData fd_rmProdButton = new FormData();
		fd_rmProdButton.bottom = new FormAttachment(removeProdList, 0, SWT.BOTTOM);
		fd_rmProdButton.left = new FormAttachment(removeProdList, 6);
		fd_rmProdButton.height = 30;
		fd_rmProdButton.width = 150;
		rmProdButton.setLayoutData(fd_rmProdButton);
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
		
		removeButton = new Button(this, SWT.NONE);
		FormData fd_removeButton = new FormData();
		fd_removeButton.right = new FormAttachment(_packageInfo, 0, SWT.RIGHT);
		fd_removeButton.width = 150;
		fd_removeButton.height = 30;
		fd_removeButton.bottom = new FormAttachment(100, -6);
		removeButton.setLayoutData(fd_removeButton);
		removeButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				removeSelected();
			}
		});
		removeButton.setText("Remove Package");
		
		modifyButton = new Button(this, SWT.NONE);
		FormData fd_modifyButton = new FormData();
		fd_modifyButton.width = 150;
		fd_modifyButton.top = new FormAttachment(removeButton, 0, SWT.TOP);
		fd_modifyButton.bottom = new FormAttachment(removeButton, 0, SWT.BOTTOM);
		fd_modifyButton.right = new FormAttachment(removeButton, -6);
		modifyButton.setLayoutData(fd_modifyButton);
		modifyButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				toggleModify();
			}
		});
		modifyButton.setText("Modify Package");
		
		newPackageButton = new Button(this, SWT.NONE);
		FormData fd_newPackageButton = new FormData();
		fd_newPackageButton.bottom = new FormAttachment(removeButton, -6, SWT.TOP);
		fd_newPackageButton.right = new FormAttachment(removeButton, 0, SWT.RIGHT);
		fd_newPackageButton.left = new FormAttachment(removeButton, 0, SWT.LEFT);
		fd_newPackageButton.height = 30;
		newPackageButton.setLayoutData(fd_newPackageButton);
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
		
		btnNewProduct = new Button(this, SWT.NONE);
		FormData fd_btnNewProduct = new FormData();
		fd_btnNewProduct.top = new FormAttachment(newPackageButton, 0, SWT.TOP);
		fd_btnNewProduct.left = new FormAttachment(modifyButton, 0, SWT.LEFT);
		fd_btnNewProduct.bottom = new FormAttachment(newPackageButton, 0, SWT.BOTTOM);
		fd_btnNewProduct.right = new FormAttachment(modifyButton, 0, SWT.RIGHT);
		btnNewProduct.setLayoutData(fd_btnNewProduct);
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
		
		setupTree();
		setupListViewers();
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
			
			_packageInfo.nameBox.setEditable(modifying);
			_packageInfo.priceBox.setEditable(modifying);
			_packageInfo.descripBox.setEditable(modifying);
			
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
				
				((ProductBase)_selected).setName(_packageInfo.nameBox.getText());
				((ProductBase)_selected).setDescription(_packageInfo.descripBox.getText());
				
				double price = 0;
				
				try
				{
					price = Double.parseDouble(_packageInfo.priceBox.getText());
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
			_packageInfo.nameBox.setText(((ProductBase)selected).getName());
			_packageInfo.descripBox.setText(((ProductBase)selected).getDescription());
			_packageInfo.priceBox.setText(Utility.formatMoney(((ProductBase)selected).getPrice()));
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
		_packageInfo.nameBox.setText("");
		_packageInfo.descripBox.setText("");
		_packageInfo.priceBox.setText("0.00");

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
			_packageInfo.infoBox.setText("Product Information");
			modifyButton.setText("Modify Product");
			removeButton.setText("Remove Product");
		}
		else if (item instanceof Package)
		{
			addProductBox.setVisible(true);
			removeProductBox.setVisible(true);
			_packageInfo.infoBox.setText("Package Information");
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
