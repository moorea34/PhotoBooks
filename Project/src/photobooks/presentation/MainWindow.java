package photobooks.presentation;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import photobooks.application.Globals;
import photobooks.application.Utility;
import photobooks.business.*;
import photobooks.gateways.Dao;
import photobooks.gateways.IDao;
import photobooks.gateways.StubDao;
import photobooks.objects.Package;
import photobooks.objects.Product;
import acceptanceTests.Register; 
import acceptanceTests.EventLoop;

public class MainWindow {
	protected Shell shell;
	private EventManager _eventManager;
	private ClientManager _clientManager;
	private BillManager _billManager;
	private PaymentManager _paymentManager;
	private ProductPackageManager _packageManager;
	private ProductManager _productManager;
	private IDao _dao;
	
	private ClientsPage clientsPage;
	private PackagesPage packagesPage;
	private BillsPage billingPage;
	private SettingsEditor settingsEditor;
	
	private TabFolder tabLayout;
	private TabItem packagesTab, clientTab, billTab, settingsTab;
	
	final private String PRODUCT_TYPE = "Product";
	final private String PACKAGE_TYPE = "Package";
	private MenuItem fileItem, backupDBItem, restoreDBItem, exit;
	//private MenuItem manageEvents, eventItem, checkForEventsItem;

	public MainWindow() 
	{
		Register.newWindow(this);
		System.out.println("Opening Main Window...");

		_dao = Globals.getDao();
		
		_eventManager = new EventManager(_dao.eventGateway());
		_clientManager = new ClientManager( _dao );
		_billManager = new BillManager(_dao);
		_paymentManager = new PaymentManager(_dao.paymentGateway());
		
		_packageManager = new ProductPackageManager( _dao.packageGateway() );
		_productManager = new ProductManager( _dao.productGateway(), _packageManager );
		
		if (_dao instanceof StubDao)
		{
			_clientManager.insertStubData();
			//_eventManager.insertStubData( _clientManager );
		}
		
		open();
	}

	public void open() {
		final Display display = Display.getDefault();
		final int timer = 300000;
		
		createContents();	
		
		shell.open();
		shell.layout();
		
		display.timerExec(timer, new Runnable() {
		    public void run() {
		        System.out.println("Autosaving database.");
		        Globals.getDao().commitChanges();

		        display.timerExec(timer, this);
		    }

		});
		
		if(EventLoop.isEnabled())
		{
			while (!shell.isDisposed()) 
			{
				if (!display.readAndDispatch()) 
				{
					display.sleep();
				}
			}

			display.dispose(); // Clean up cache
		}

		System.out.println("Closing Main Window...");
	}

	protected void createContents() {
		
		shell = new Shell(SWT.SHELL_TRIM | SWT.RESIZE);
		shell.setMinimumSize(new Point(750, 750));
		shell.setSize(600, 600);
		shell.setText("PhotoBooks");
		shell.setLayout(new FormLayout());
		
		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
	    
		setupFileMenu(menuBar);
	    //setupEventMenu( menuBar );
	    setupProductMenu( menuBar );
	    setupPackageMenu( menuBar );

		tabLayout = new TabFolder(shell, SWT.NONE);
		FormData fd = new FormData();
		fd.left = new FormAttachment(0, 0);
		fd.top = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100,  0);
		fd.bottom = new FormAttachment(100,  0);
		tabLayout.setLayoutData(fd);

		clientTab = new TabItem(tabLayout, SWT.NONE);
		clientTab.setText("Clients");
		
		billingPage = new BillsPage(tabLayout, SWT.NONE, shell, _clientManager, _billManager, _paymentManager, _productManager, _packageManager);

		clientsPage = new ClientsPage(tabLayout, SWT.NONE, _clientManager, (BillsPage)billingPage);
		clientTab.setControl(clientsPage);

		packagesTab = new TabItem(tabLayout, SWT.NONE);
		packagesTab.setText("Packages");
		
		packagesPage = new PackagesPage(tabLayout, SWT.NONE, _packageManager, _productManager);
		packagesTab.setControl(packagesPage);

		billTab = new TabItem(tabLayout, SWT.NONE);
		billTab.setText("Billing");
		
		billTab.setControl(billingPage);
		
		settingsTab = new TabItem(tabLayout, SWT.NONE);
		settingsEditor = new SettingsEditor(tabLayout, SWT.NONE);
		
		settingsTab.setText("Settings");
		settingsTab.setControl(settingsEditor);
		
		Utility.setFont(shell);
		Utility.centerScreen(shell);
	}
	
	private void setupFileMenu(Menu menuBar)
	{
		Menu fileMenu = new Menu(menuBar);

	    fileItem = new MenuItem(menuBar, SWT.CASCADE);
	    fileItem.setText("File");
	    fileItem.setMenu(fileMenu);
	    
	    backupDBItem = new MenuItem(fileMenu, SWT.NONE);
	    backupDBItem.setText("Backup Database");
	    backupDBItem.addSelectionListener(new SelectionAdapter()
	    {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) 
	    	{
	    		if (Globals.getDao() instanceof Dao)
	    		{
	    			Dao dao = (Dao)Globals.getDao();
		    		String dir = Utility.getDir(shell);
		    		
		    		if (dir != null)
		    		{
		    			dao.commitChanges();
		    		
		    			try
		    			{
		    				Utility.copyDatabase("./database", dir);
		    			}
		    			catch (Exception ex)
		    			{
		    				String msg = "Error backing up database: " + ex.toString();
		    			
		    				Utility.showErrorMessage(shell, msg);
		    			}
		    		}
	    		}
	    	}
	    });
	    
	    restoreDBItem = new MenuItem(fileMenu, SWT.NONE);
	    restoreDBItem.setText("Restore Database");
	    restoreDBItem.addSelectionListener(new SelectionAdapter() 
	    {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) 
	    	{
	    		if (Globals.getDao() instanceof Dao)
	    		{
	    			Dao dao = (Dao)Globals.getDao();
	    			String src = Utility.getDir(shell);
	    			
	    			if (src != null)
	    			{
	    				try
	    				{
	    					dao.restore(src);
	    					
	    					clientsPage.refresh();
	    					packagesPage.refresh();
	    					billingPage.updateClientCB();
	    				}
	    				catch (Exception ex)
	    				{
	    					String msg = "Error backing up database: " + ex.toString();
			    			
		    				Utility.showErrorMessage(shell, msg);
	    				}
	    			}
	    		}
	    	}
	    });
	    
	    exit = new MenuItem(fileMenu, SWT.NONE);
	    exit.setText("Exit");
	    exit.addSelectionListener(new SelectionAdapter() 
	    {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) 
	    	{
	    		shell.dispose();
	    	}
	    });
	}

	/*private void setupEventMenu( Menu menuBar )
	{
	    Menu eventMenu = new Menu(menuBar);
	    
	    eventItem = new MenuItem(menuBar, SWT.CASCADE);
	    eventItem.setText("Events");
	    eventItem.setMenu(eventMenu);
	    
	    checkForEventsItem = new MenuItem(eventMenu, SWT.NONE);
	    checkForEventsItem.setText("Check for events");
	    checkForEventsItem.addSelectionListener( new SelectionAdapter()
	    {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0)
	    	{
	    		boolean result = _eventManager.checkForEvents( _clientManager );
	    		
	    		// No results were found, so we still want to respond to the button click
	    		if (!result)
	    		{
	    			MessageBox messageBox = new MessageBox( shell, SWT.OK );
	    			messageBox.setText("No new events.");
	    			messageBox.setMessage( "No events are coming up." );
	    			messageBox.open();
	    		}
	    	}
	    });
	    
	    manageEvents = new MenuItem( eventMenu, SWT.NONE );
	    manageEvents.setText("Manage Events");
	    manageEvents.addSelectionListener( new SelectionAdapter()
	    {
	    	public void widgetSelected( SelectionEvent arg0 )
	    	{
	    		ManageEventsWindow window = new ManageEventsWindow( shell, SWT.SHELL_TRIM, _eventManager, _clientManager );
	    		window.open();
	    	}
	    });
	    
	}*/
	
	private void setupProductMenu( Menu menuBar )
	{
	    Menu productMenu = new Menu(menuBar);
	    
	    MenuItem product = new MenuItem(menuBar, SWT.CASCADE);
	    product.setText("Products");
	    product.setMenu(productMenu);
	    
	    MenuItem addProd = new MenuItem(productMenu, SWT.NONE);
	    addProd.setText("Add Product");
	    addProd.addSelectionListener( new SelectionAdapter()
	    {
	    	public void widgetSelected( SelectionEvent arg0 )
	    	{
	    		AddProductWindow window = new AddProductWindow( shell, SWT.SHELL_TRIM & (~SWT.RESIZE), PRODUCT_TYPE );
	    		Product newProduct = (Product)window.open();
	    		
	    		if(newProduct != null)
	    		{
		    		_productManager.insertProduct( newProduct );
		    		
		    		((PackagesPage)packagesPage).refresh();
	    		}
	    	}
	    });
	    
	    MenuItem viewProducts = new MenuItem(productMenu, SWT.NONE);
	    viewProducts.setText("View Products");
	    viewProducts.addSelectionListener( new SelectionAdapter()
	    {
	    	public void widgetSelected( SelectionEvent arg0 )
	    	{
	    		tabLayout.setSelection(packagesTab);
	    	}
	    });
	}
	
	private void setupPackageMenu( Menu menuBar )
	{
	    Menu packageMenu = new Menu(menuBar);
	    
	    MenuItem packageItem = new MenuItem(menuBar, SWT.CASCADE);
	    packageItem.setText("Packages");
	    packageItem.setMenu(packageMenu);
	    
	    MenuItem addPackage = new MenuItem(packageMenu, SWT.NONE);
	    addPackage.setText("Add Package");
	    addPackage.addSelectionListener( new SelectionAdapter()
	    {
	    	public void widgetSelected( SelectionEvent arg0 )
	    	{
	    		AddProductWindow window = new AddProductWindow( shell, SWT.SHELL_TRIM & (~SWT.RESIZE), PACKAGE_TYPE );
	    		Package newPackage = (Package)window.open();
	    		
	    		if(newPackage != null)
	    		{
		    		_packageManager.insertProductPackage( newPackage );
		    		
		    		((PackagesPage)packagesPage).refresh();
	    		}
	    	}
	    });
	    
	    MenuItem viewPackages = new MenuItem(packageMenu, SWT.NONE);
	    viewPackages.setText("View Packages");
	    viewPackages.addSelectionListener( new SelectionAdapter()
	    {
	    	public void widgetSelected( SelectionEvent arg0 )
	    	{
	    		tabLayout.setSelection(packagesTab);
	    	}
	    });
	}
}
