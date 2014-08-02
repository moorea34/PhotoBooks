package photobooks.presentation;
import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import photobooks.application.Globals;
import photobooks.application.Utility;
import photobooks.business.ClientManager;
import photobooks.objects.Client;
import acceptanceTests.Register;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment; 

public class ClientsPage extends Composite
{
	private ClientEditor _clientEditor;
	
	private Composite _parent;
	private Text txtSearchClients;
	private int currClientID;
	private ListViewer listViewer;
	private boolean modifying;
	private ClientManager _clientManager;
	private ViewerFilter filterViewer;
	private Button btnChooseDirectory, btnViewPhotos, btnRemoveClient, btnModifyClient, btnAddClient;
	
	private BillsPage _billsPage;
	
	public ClientsPage(Composite parent, int style, ClientManager clientManager, BillsPage billsPage) 
	{
		super(parent, style);
		Register.newWindow(this);
		
		currClientID = -1;
		_parent = parent;
		_clientManager = clientManager;
		_billsPage = billsPage;
		modifying = false;
		
		setupUI();
		
		selectFirstClient();
		
	}

	private void setupUI() 
	{
		setLayout(new FormLayout());
		listViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		FormData fd_list = new FormData();
		fd_list.width = 160;
		fd_list.bottom = new FormAttachment(100, -37);
		fd_list.top = new FormAttachment(0, 10);
		fd_list.left = new FormAttachment(0, 10);
		list.setLayoutData(fd_list);
		list.setFont( Globals.getFont() );
		
				listViewer.setContentProvider(new IStructuredContentProvider() 
				{
					public Object[] getElements(Object clients) 
					{
						return _clientManager.getClientList().toArray();
					}
		
					public void dispose() 
					{
					}
		
					@Override
					public void inputChanged(Viewer arg0, Object arg1, Object arg2) 
					{				
					}
				});
				
						listViewer.setInput(_clientManager);
						
								listViewer.setLabelProvider(new LabelProvider() 
								{
						
									public String getText(Object element) 
									{
										String result = "";
										if (element instanceof Client)
										{
											result = ((Client) element).getFormattedName();
										}
										
										return result;
									}
								});
								
										listViewer.addSelectionChangedListener(new ISelectionChangedListener() 
										{
											public void selectionChanged(SelectionChangedEvent event) 
											{
												IStructuredSelection selection = (IStructuredSelection)event.getSelection();
												Client selected = (Client)selection.getFirstElement();
												if(selected != null)
												{
													System.out.println("Selected client id: " + selected.getID());
													
													if(modifying)
													{
														toggleModify();
													}
													
													currClientID = selected.getID();
													selectClient(selected);
													
												}
											}
										});
				
						txtSearchClients = new Text(this, SWT.BORDER | SWT.SEARCH);
						FormData fd_txtSearchClients = new FormData();
						fd_txtSearchClients.height = 18;
						fd_txtSearchClients.right = new FormAttachment(list, 0, SWT.RIGHT);
						fd_txtSearchClients.left = new FormAttachment(list, 0, SWT.LEFT);
						fd_txtSearchClients.top = new FormAttachment(list, 6);
						txtSearchClients.setLayoutData(fd_txtSearchClients);
						txtSearchClients.addModifyListener(new ModifyListener()
						{

							@Override
							public void modifyText(ModifyEvent arg0) 
							{
								listViewer.refresh();
							}
							
						});
				
				_clientEditor = new ClientEditor(this, SWT.NONE);
				_clientEditor.setModify(false);
				FormData fd = new FormData();
				fd.bottom = new FormAttachment(list, -40, SWT.BOTTOM);
				fd.left = new FormAttachment(listViewer.getList(), 6, SWT.RIGHT);
				fd.top = new FormAttachment(listViewer.getList(), 0, SWT.TOP);
				fd.right = new FormAttachment(100, -6);
				_clientEditor.setLayoutData(fd);
						
				btnRemoveClient = new Button(this, SWT.NONE);
				FormData fd_btnRemoveClient = new FormData();
				fd_btnRemoveClient.width = 150;
				fd_btnRemoveClient.height = 30;
				fd_btnRemoveClient.bottom = new FormAttachment(100, -6);
				fd_btnRemoveClient.right = new FormAttachment(100, -6);
				btnRemoveClient.setLayoutData(fd_btnRemoveClient);
				btnRemoveClient.addSelectionListener(new SelectionAdapter() 
				{
					@Override
					public void widgetSelected(SelectionEvent e) 
					{
						if(currClientID != -1)
						{
							if(modifying)
							{
								toggleModify();
							}
							
							Client oldClient = _clientManager.getClientByID(currClientID);
							_clientManager.removeClient(oldClient);
							
							if(_clientManager.getClientList().size() > 0)
								currClientID = _clientManager.getClientList().get(0).getID();
							else
								currClientID = -1;
							
							updateList();
							
							_billsPage.updateClientCB();
						}
					}
				});
				btnRemoveClient.setText("Remove Client");
				
						btnModifyClient = new Button(this, SWT.NONE);
						FormData fd_btnModifyClient = new FormData();
						fd_btnModifyClient.width = 150;
						fd_btnModifyClient.top = new FormAttachment(btnRemoveClient, 0, SWT.TOP);
						fd_btnModifyClient.bottom = new FormAttachment(btnRemoveClient, 0, SWT.BOTTOM);
						fd_btnModifyClient.right = new FormAttachment(btnRemoveClient, -6);
						btnModifyClient.setLayoutData(fd_btnModifyClient);
						btnModifyClient.addSelectionListener(new SelectionAdapter() 
						{
							@Override
							public void widgetSelected(SelectionEvent e) 
							{
								toggleModify();
							}
						});
						btnModifyClient.setText("Modify Client");
		
				btnAddClient = new Button(this, SWT.NONE);
				FormData fd_btnAddClient = new FormData();
				fd_btnAddClient.width = 150;
				fd_btnAddClient.top = new FormAttachment(btnRemoveClient, 0, SWT.TOP);
				fd_btnAddClient.bottom = new FormAttachment(btnRemoveClient, 0, SWT.BOTTOM);
				fd_btnAddClient.right = new FormAttachment(btnModifyClient, -6);
				btnAddClient.setLayoutData(fd_btnAddClient);
				btnAddClient.addSelectionListener(new SelectionAdapter() 
				{
					@Override
					public void widgetSelected(SelectionEvent e) 
					{
						if(modifying)
						{
							toggleModify();
						}
						
						AddClientWindow addClient = new AddClientWindow(_parent.getShell(), SWT.SHELL_TRIM | SWT.RESIZE);
						Client newClient = addClient.open();
						
						if(newClient != null)
						{
							_clientManager.insertClient(newClient);
							updateList();
							
							listViewer.setSelection(new StructuredSelection(newClient));
							
							_billsPage.updateClientCB();
						}

						
					}
				});
				btnAddClient.setText("Add Client");

		setupClientList(this);
		
		btnChooseDirectory = new Button(this, SWT.NONE);
		FormData fd_btnChooseDirectory = new FormData();
		fd_btnChooseDirectory.width = 150;
		fd_btnChooseDirectory.height = 30;
		fd_btnChooseDirectory.bottom = new FormAttachment(btnRemoveClient, -6, SWT.TOP);
		fd_btnChooseDirectory.right = new FormAttachment(btnRemoveClient, 0, SWT.RIGHT);
		btnChooseDirectory.setLayoutData(fd_btnChooseDirectory);
		btnChooseDirectory.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(currClientID != -1)
				{
					Client selected = _clientManager.getClientByID(currClientID);
			        String selectedDir = Utility.getDir(_parent.getShell());
			        
			        if(selectedDir != null)
			        {
			        	selected.setDirectory(selectedDir);
			        	_clientManager.updateClient(selected);
			        	updateList();
			        	
						btnViewPhotos.setEnabled(true);
			        }
			        else
			        	btnViewPhotos.setEnabled(false);
				}
			}
		});
		btnChooseDirectory.setText("Choose Directory");
		
		btnViewPhotos = new Button(this, SWT.NONE);
		FormData fd_btnViewPhotos = new FormData();
		fd_btnViewPhotos.width = 150;
		fd_btnViewPhotos.top = new FormAttachment(btnChooseDirectory, 0, SWT.TOP);
		fd_btnViewPhotos.bottom = new FormAttachment(btnChooseDirectory, 0, SWT.BOTTOM);
		fd_btnViewPhotos.right = new FormAttachment(btnChooseDirectory, -6, SWT.LEFT);
		btnViewPhotos.setLayoutData(fd_btnViewPhotos);
		btnViewPhotos.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(currClientID != -1)
				{
					Client selected = _clientManager.getClientByID(currClientID);
					if(selected.getDirectory() != null && !selected.getDirectory().equals(""))
					{
						String path = new File(selected.getDirectory()).getAbsolutePath();
						Utility.openDir(_parent.getShell(), path);
					}
				}
				
			}
		});
		btnViewPhotos.setText("View Photos");
		
		filterViewer = new ViewerFilter() 
		{
		    @Override
		    public boolean select(Viewer viewer, Object parentElement, Object element) 
		    {
		    	String[] split;
		    	
		    	if(txtSearchClients.getText().trim().length() < 1)
		    		return true;
		    	
		    	split = txtSearchClients.getText().trim().toLowerCase().split("\\|");
		    	
		    	for (String str : split)
		    	{
		    		String s = str.trim();
		    		
		    		if (s.length() > 0 && ((Client) element).searchAll(s))
		    			return true;
		    	}
		        
		        return false;
		    }
		};
		listViewer.addFilter(filterViewer);
	}

	private void setupClientList(Composite parent)
	{
	}
	
	private void selectFirstClient()
	{
		ArrayList<Client> clients = new ArrayList<Client>( _clientManager.getClientList() );
		
		if (clients != null && clients.size() > 0)
		{
			Client firstClient = clients.get(0);
			selectClient(firstClient);
		}
	}
	
	private void selectClient(Client client)
	{
		clearValues();
		
		if(client != null)
		{
			currClientID = client.getID();
			_clientEditor.setClient(client);
			
			if (client.getDirectory() != null && client.getDirectory().length() > 0) {
				btnViewPhotos.setEnabled(true);
			}
			else
				btnViewPhotos.setEnabled(false);
		}
	}

	private void updateList()
	{
		clearValues();
		listViewer.refresh();
		
		if(currClientID != -1)
		{
			Client selected = _clientManager.getClientByID(currClientID);
			listViewer.setSelection(new StructuredSelection(selected));
			selectClient(selected);
		}
	}
	
	public void refresh()
	{
		this.updateList();
	}

	private void clearValues()
	{
		_clientEditor.clearValues();
	}
	
	private void toggleModify()
	{
		if(currClientID != -1)
		{
			_clientEditor.setModify(!modifying);
			
			modifying = !modifying;
			
			if(modifying)
			{
				btnModifyClient.setText("Done Modifying");
			}
			else
			{
				btnModifyClient.setText("Modify Client");
				Client currClient = _clientManager.getClientByID(currClientID);
				_clientEditor.getClientFromFields(currClient);
				
				_clientManager.updateClient(currClient);
				
				updateList();

				_billsPage.updateClientCB();
			}
		}
	}
}