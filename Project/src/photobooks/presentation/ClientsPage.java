package photobooks.presentation;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import photobooks.application.Globals;
import photobooks.application.Utility;
import photobooks.business.ClientManager;
import photobooks.objects.Address;
import photobooks.objects.Address.AddressType;
import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;
import photobooks.objects.PhoneNumber.PhoneNumberType;
import acceptanceTests.Register; 

public class ClientsPage extends Composite
{
	private Shell shell;
	private static final int YEAR_CONST = 1940;
	private Composite _parent;
	private Text firstNameBox;
	private Text lastNameBox;
	private Text txtSearchClients;
	private int currClientID;
	private ListViewer listViewer;
	private boolean modifying;
	private Text numHomeBox;
	private Text numCellularBox;
	private Text numWorkBox;
	private Text numAltBox;
	private Text addrHomeBox;
	private Text addrAlt1Box;
	private Text addrAlt2Box;
	private CCombo dobDay, dobMonth, dobYear, annDay, annMonth, annYear;
	private ClientManager _clientManager;
	private ViewerFilter filterViewer;
	private Text emailBox;
	private Button btnChooseDirectory, btnViewPhotos, btnRemoveClient, btnModifyClient, btnAddClient;
	
	private BillsPage _billsPage;
	
	public ClientsPage(Composite parent, int style, ClientManager clientManager, BillsPage billsPage) 
	{
		super(parent, style);
		shell = parent.getShell();
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
		listViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		list.setFont( Globals.getFont() );
		list.setBounds(10, 10, 160, 465);
		
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
						txtSearchClients.setBounds(10, 481, 160, 23);
						txtSearchClients.addModifyListener(new ModifyListener()
						{

							@Override
							public void modifyText(ModifyEvent arg0) 
							{
								listViewer.refresh();
							}
							
						});
		
				Group infoGroup = new Group(this, SWT.NONE);
				infoGroup.setText("Personal Information");
				infoGroup.setBounds(176, 10, 390, 151);
				
						Label lblFirstName = new Label(infoGroup, SWT.NONE);
						lblFirstName.setBounds(10, 23, 93, 15);
						lblFirstName.setText("Name");
								
										Label lblLastName = new Label(infoGroup, SWT.NONE);
										lblLastName.setBounds(10, 47, 93, 15);
										lblLastName.setText("Email");
								
										Label lblDob = new Label(infoGroup, SWT.NONE);
										lblDob.setBounds(10, 71, 93, 15);
										lblDob.setText("DOB");
								
										Label lblAnniversary = new Label(infoGroup, SWT.NONE);
										lblAnniversary.setBounds(10, 95, 93, 15);
										lblAnniversary.setText("Anniversary");
						
								firstNameBox = new Text(infoGroup, SWT.BORDER);
								firstNameBox.setBounds(109, 20, 132, 21);
								firstNameBox.setEditable(false);
								
										lastNameBox = new Text(infoGroup, SWT.BORDER);
										lastNameBox.setBounds(248, 20, 132, 21);
										lastNameBox.setEditable(false);
																
																emailBox = new Text(infoGroup, SWT.BORDER);
																emailBox.setEditable(false);
																emailBox.setBounds(109, 44, 271, 21);
																
																dobDay = new CCombo(infoGroup, SWT.BORDER);
																dobDay.setEnabled(false);
																dobDay.setEditable(false);
																dobDay.setText("Day");
																dobDay.setBounds(109, 68, 72, 21);
																
																dobMonth = new CCombo(infoGroup, SWT.BORDER);
																dobMonth.setEnabled(false);
																dobMonth.setEditable(false);
																dobMonth.setText("Month");
																dobMonth.setBounds(187, 68, 107, 21);
																
																dobYear = new CCombo(infoGroup, SWT.BORDER);
																dobYear.setEnabled(false);
																dobYear.setEditable(false);
																dobYear.setText("Year");
																dobYear.setBounds(300, 68, 80, 21);
																
																annDay = new CCombo(infoGroup, SWT.BORDER);
																annDay.setEnabled(false);
																annDay.setEditable(false);
																annDay.setText("Day");
																annDay.setBounds(109, 92, 72, 21);
																
																annMonth = new CCombo(infoGroup, SWT.BORDER);
																annMonth.setEnabled(false);
																annMonth.setEditable(false);
																annMonth.setText("Month");
																annMonth.setBounds(187, 92, 107, 21);
																
																annYear = new CCombo(infoGroup, SWT.BORDER);
																annYear.setEnabled(false);
																annYear.setEditable(false);
																annYear.setText("Year");
																annYear.setBounds(300, 92, 80, 21);
		
				Group grpPhoneNumbers = new Group(this, SWT.NONE);
				grpPhoneNumbers.setText("Phone Numbers");
				grpPhoneNumbers.setBounds(176, 167, 390, 150);
				
						Label lblHome = new Label(grpPhoneNumbers, SWT.NONE);
						lblHome.setText("Home");
						lblHome.setBounds(10, 23, 93, 15);
								
										Label lblCellular = new Label(grpPhoneNumbers, SWT.NONE);
										lblCellular.setText("Cellular");
										lblCellular.setBounds(10, 47, 93, 15);
								
										Label lblWork = new Label(grpPhoneNumbers, SWT.NONE);
										lblWork.setText("Work");
										lblWork.setBounds(10, 71, 93, 15);
								
										Label lblAlternative = new Label(grpPhoneNumbers, SWT.NONE);
										lblAlternative.setText("Alternative");
										lblAlternative.setBounds(10, 95, 93, 15);
						
								numHomeBox = new Text(grpPhoneNumbers, SWT.BORDER);
								numHomeBox.setEditable(false);
								numHomeBox.setBounds(109, 20, 271, 21);
								
										numCellularBox = new Text(grpPhoneNumbers, SWT.BORDER);
										numCellularBox.setEditable(false);
										numCellularBox.setBounds(109, 44, 271, 21);
														
																numWorkBox = new Text(grpPhoneNumbers, SWT.BORDER);
																numWorkBox.setEditable(false);
																numWorkBox.setBounds(109, 68, 271, 21);
																
																		numAltBox = new Text(grpPhoneNumbers, SWT.BORDER);
																		numAltBox.setEditable(false);
																		numAltBox.setBounds(109, 92, 271, 21);
		
				Group grpAddresses = new Group(this, SWT.NONE);
				grpAddresses.setText("Addresses");
				grpAddresses.setBounds(176, 323, 390, 117);
						
								Label addrHomeLbl = new Label(grpAddresses, SWT.NONE);
								addrHomeLbl.setText("Home");
								addrHomeLbl.setBounds(10, 24, 93, 15);
				
						Label lblAlternative_1 = new Label(grpAddresses, SWT.NONE);
						lblAlternative_1.setText("Alternative 1");
						lblAlternative_1.setBounds(10, 48, 93, 15);
								
										Label lblAlternative_2 = new Label(grpAddresses, SWT.NONE);
										lblAlternative_2.setText("Alternative 2");
										lblAlternative_2.setBounds(10, 72, 93, 15);
										
												addrHomeBox = new Text(grpAddresses, SWT.BORDER);
												addrHomeBox.setEditable(false);
												addrHomeBox.setBounds(109, 21, 271, 21);
												
														addrAlt1Box = new Text(grpAddresses, SWT.BORDER);
														addrAlt1Box.setEditable(false);
														addrAlt1Box.setBounds(109, 45, 271, 21);
														
																addrAlt2Box = new Text(grpAddresses, SWT.BORDER);
																addrAlt2Box.setEditable(false);
																addrAlt2Box.setBounds(109, 69, 271, 21);
		
		btnViewPhotos = new Button(this, SWT.NONE);
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
		btnViewPhotos.setBounds(194, 448, 120, 25);
		
		btnChooseDirectory = new Button(this, SWT.NONE);
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
			        }
				}
			}
		});
		btnChooseDirectory.setText("Choose Directory");
		btnChooseDirectory.setBounds(320, 448, 150, 25);
		
				btnAddClient = new Button(this, SWT.NONE);
				btnAddClient.addSelectionListener(new SelectionAdapter() 
				{
					@Override
					public void widgetSelected(SelectionEvent e) 
					{
						if(modifying)
						{
							toggleModify();
						}
						
						AddClientWindow addClient = new AddClientWindow(_parent.getShell(), SWT.SHELL_TRIM & (~SWT.RESIZE));
						Client newClient = addClient.open();
						
						if(newClient != null)
						{
							_clientManager.insertClient(newClient);
							updateList();
							
							listViewer.setSelection(new StructuredSelection(newClient));
							
							_billsPage.addNewClient(newClient);
						}

						
					}
				});
				btnAddClient.setText("Add Client");
				btnAddClient.setBounds(194, 479, 120, 25);

		btnModifyClient = new Button(this, SWT.NONE);
		btnModifyClient.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				toggleModify();
			}
		});
		btnModifyClient.setText("Modify Client");
		btnModifyClient.setBounds(320, 479, 120, 25);

		setupClientList(this);
		btnRemoveClient = new Button(this, SWT.NONE);
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
					
					_billsPage.removeClient(oldClient);
				}
			}
		});
		btnRemoveClient.setBounds(446, 479, 120, 25);
		btnRemoveClient.setText("Remove Client");
		
		initDateValues();
		
		filterViewer = new ViewerFilter() 
		{
		    @Override
		    public boolean select(Viewer viewer, Object parentElement, Object element) 
		    {
		    	if(txtSearchClients.getText().equals("") || txtSearchClients.getText().length() < 3)
		    		return true;
		    	
		        if (((Client) element).searchAll(txtSearchClients.getText().toLowerCase()))
		        {
		            return true;
		        }
		        
		        return false;
		    }
		};
		listViewer.addFilter(filterViewer);
	}

	private void initDateValues() 
	{		
		dobDay.setItems( Utility.getDays() );
		annDay.setItems( Utility.getDays() );
		dobMonth.setItems( Utility.getMonths() );
		annMonth.setItems( Utility.getMonths() );
		dobYear.setItems( Utility.getYears() );
		annYear.setItems( Utility.getYears() );		
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
			firstNameBox.setText(client.getFirstName());
			lastNameBox.setText(client.getLastName());
			
			if (client.getEmail() == null)
				emailBox.setText("");
			else
				emailBox.setText(client.getEmail());
			
			if(client.getBirthday() != null)
			{
				dobDay.select(client.getBirthday().get(Calendar.DAY_OF_MONTH) - 1);
				dobMonth.select(client.getBirthday().get(Calendar.MONTH));
				dobYear.select(client.getBirthday().get(Calendar.YEAR) - YEAR_CONST);
			}
			
			if(client.getAnniversary() != null)
			{
				annDay.select(client.getAnniversary().get(Calendar.DAY_OF_MONTH) - 1);
				annMonth.select(client.getAnniversary().get(Calendar.MONTH));
				annYear.select(client.getAnniversary().get(Calendar.YEAR) - YEAR_CONST);
			}
			
			for(PhoneNumber num : client.getNumbers())
			{
				switch(num.getType())
				{
					case Home:
						numHomeBox.setText(num.getNumber());
						break;
					case Cellular:
						numCellularBox.setText(num.getNumber());
						break;
					case Work:
						numWorkBox.setText(num.getNumber());
						break;
					case Alternative:
						numAltBox.setText(num.getNumber());
						break;
				}
			}
			
			for(Address addr : client.getAddresses())
			{
				switch(addr.getType())
				{
					case Home:
						addrHomeBox.setText(addr.getAddress());
						break;
					case Alternative1:
						addrAlt1Box.setText(addr.getAddress());
						break;
					case Alternative2:
						addrAlt2Box.setText(addr.getAddress());
						break;
				}
			}
			
			if(client.getDirectory() != null && !client.getDirectory().equals(""))
			{
				btnViewPhotos.setEnabled(true);
			}
			else
			{
				btnViewPhotos.setEnabled(false);
			}
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
		/* Info group */
		firstNameBox.setText("");
		lastNameBox.setText("");
		emailBox.setText("");

		/* Phone number group */
		numHomeBox.setText("");
		numCellularBox.setText("");
		numWorkBox.setText("");
		numAltBox.setText("");

		/* Address group */
		addrHomeBox.setText("");
		addrAlt1Box.setText("");
		addrAlt2Box.setText("");
		
		dobDay.select(-1);
		dobMonth.select(-1);
		dobYear.select(-1);
		
		annDay.select(-1);
		annMonth.select(-1);
		annYear.select(-1);
	}
	
	private void toggleModify()
	{
		if(currClientID != -1)
		{
			/* Info group */
			firstNameBox.setEditable(!modifying);
			lastNameBox.setEditable(!modifying);
			emailBox.setEditable(!modifying);
			dobDay.setEnabled(!modifying);
			dobMonth.setEnabled(!modifying);
			dobYear.setEnabled(!modifying);
			annDay.setEnabled(!modifying);
			annMonth.setEnabled(!modifying);
			annYear.setEnabled(!modifying);

			/* Phone number group */
			numHomeBox.setEditable(!modifying);
			numCellularBox.setEditable(!modifying);
			numWorkBox.setEditable(!modifying);
			numAltBox.setEditable(!modifying);

			/* Address group */
			addrHomeBox.setEditable(!modifying);
			addrAlt1Box.setEditable(!modifying);
			addrAlt2Box.setEditable(!modifying);
			
			modifying = !modifying;
			
			if(modifying)
			{
				btnModifyClient.setText("Done Modifying");
			}
			else
			{
				btnModifyClient.setText("Modify Client");
				Client currClient = _clientManager.getClientByID(currClientID);
				currClient.setFirstName(firstNameBox.getText());
				currClient.setLastName(lastNameBox.getText());
				currClient.setEmail(emailBox.getText());

				if(dobMonth.getSelectionIndex() != -1 && dobYear.getSelectionIndex() != -1 && dobDay.getSelectionIndex() != -1)
				{
					Calendar newDob = Calendar.getInstance();
					newDob.set(Integer.parseInt(dobYear.getItem(dobYear.getSelectionIndex())), dobMonth.getSelectionIndex(), dobDay.getSelectionIndex() + 1);
					currClient.setBirthday(newDob);
				}
				
				if(annMonth.getSelectionIndex() != -1 && annYear.getSelectionIndex() != -1 && annDay.getSelectionIndex() != -1)
				{
					Calendar newAnn = Calendar.getInstance();
					newAnn.set(Integer.parseInt(annYear.getItem(annYear.getSelectionIndex())), annMonth.getSelectionIndex(), annDay.getSelectionIndex() + 1);
					currClient.setAnniversary(newAnn);
				}
				
				ArrayList<PhoneNumber> updatedNumbers = new ArrayList<PhoneNumber>();
				if(!numHomeBox.getText().equals(""))
					updatedNumbers.add(new PhoneNumber(PhoneNumberType.Home, numHomeBox.getText()));
				if(!numCellularBox.getText().equals(""))
					updatedNumbers.add(new PhoneNumber(PhoneNumberType.Cellular, numCellularBox.getText()));
				if(!numWorkBox.getText().equals(""))
					updatedNumbers.add(new PhoneNumber(PhoneNumberType.Work, numWorkBox.getText()));
				if(!numAltBox.getText().equals(""))
					updatedNumbers.add(new PhoneNumber(PhoneNumberType.Alternative, numAltBox.getText()));
				
				ArrayList<Address> updatedAddresses = new ArrayList<Address>();
				if(!addrHomeBox.getText().equals(""))
					updatedAddresses.add(new Address(AddressType.Home, addrHomeBox.getText()));
				if(!addrAlt1Box.getText().equals(""))
					updatedAddresses.add(new Address(AddressType.Alternative1, addrAlt1Box.getText()));
				if(!addrAlt2Box.getText().equals(""))
					updatedAddresses.add(new Address(AddressType.Alternative2, addrAlt2Box.getText()));
				
				currClient.setAddresses(updatedAddresses);
				currClient.setNumbers(updatedNumbers);
				
				_clientManager.updateClient(currClient);
				
				_billsPage.updateClient(currClient);
				
				updateList();
			}
		}
	}
}