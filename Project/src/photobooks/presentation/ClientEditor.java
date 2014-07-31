package photobooks.presentation;

import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import photobooks.application.Utility;
import photobooks.objects.*;
import photobooks.objects.Address.AddressType;
import photobooks.objects.PhoneNumber.PhoneNumberType;

public class ClientEditor extends Composite {
	
	private Text firstNameBox;
	private Text lastNameBox;
	private Text numHomeBox;
	private Text numCellularBox;
	private Text numWorkBox;
	private Text numAltBox;
	private Text addrHomeBox;
	private Text addrAlt1Box;
	private Text addrAlt2Box;
	private CCombo dobDay, dobMonth, dobYear, annDay, annMonth, annYear;
	private Text emailBox;

	public ClientEditor(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new FormLayout());

		init();
	}
	
	private void init() {
		int groupHeight = 130;
		int labelDistance = 10;
		
		int labelWidth = 90;
		int labelHeight = 20;
		
		int boxOffsetX = 3;
		int boxOffsetY = -2;
		int boxHeight = 22;
		
		int nudHeight = boxHeight + 2;
		
		Group infoGroup = new Group(this, SWT.NONE);
		infoGroup.setLayout(new FormLayout());
		FormData fd_infoGroup = new FormData();
		fd_infoGroup.top = new FormAttachment(0, 0);
		fd_infoGroup.left = new FormAttachment(0, 0);
		fd_infoGroup.right = new FormAttachment(100, 0);
		fd_infoGroup.height = groupHeight;
		infoGroup.setLayoutData(fd_infoGroup);
		infoGroup.setText("Personal Information");
		
				Label lblFirstName = new Label(infoGroup, SWT.NONE);
				FormData fd_lblFirstName = new FormData();
				fd_lblFirstName.left = new FormAttachment(0, 6);
				fd_lblFirstName.top = new FormAttachment(0, 6);
				fd_lblFirstName.width = labelWidth;
				fd_lblFirstName.height = labelHeight;
				lblFirstName.setLayoutData(fd_lblFirstName);
				lblFirstName.setText("Name");
						
								Label lblEmail = new Label(infoGroup, SWT.NONE);
								FormData fd_lblEmail = new FormData();
								fd_lblEmail.top = new FormAttachment(lblFirstName, labelDistance, SWT.BOTTOM);
								fd_lblEmail.left = new FormAttachment(lblFirstName, 0, SWT.LEFT);
								fd_lblEmail.width = labelWidth;
								fd_lblEmail.height = labelHeight;
								lblEmail.setLayoutData(fd_lblEmail);
								lblEmail.setText("Email");
						
								Label lblDob = new Label(infoGroup, SWT.NONE);
								FormData fd_lblDob = new FormData();
								fd_lblDob.top = new FormAttachment(lblEmail, labelDistance, SWT.BOTTOM);
								fd_lblDob.left = new FormAttachment(lblFirstName, 0, SWT.LEFT);
								fd_lblDob.width = labelWidth;
								fd_lblDob.height = labelHeight;
								lblDob.setLayoutData(fd_lblDob);
								lblDob.setText("DOB");
						
								Label lblAnniversary = new Label(infoGroup, SWT.NONE);
								FormData fd_lblAnniversary = new FormData();
								fd_lblAnniversary.top = new FormAttachment(lblDob, labelDistance, SWT.BOTTOM);
								fd_lblAnniversary.left = new FormAttachment(lblFirstName, 0, SWT.LEFT);
								fd_lblAnniversary.width = labelWidth;
								fd_lblAnniversary.height = labelHeight;
								lblAnniversary.setLayoutData(fd_lblAnniversary);
								lblAnniversary.setText("Anniversary");
				
						firstNameBox = new Text(infoGroup, SWT.BORDER);
						FormData fd_firstNameBox = new FormData();
						fd_firstNameBox.top = new FormAttachment(lblFirstName, boxOffsetY, SWT.TOP);
						fd_firstNameBox.left = new FormAttachment(lblAnniversary, 6);
						fd_firstNameBox.right = new FormAttachment(59, 1);
						fd_firstNameBox.height = boxHeight;
						firstNameBox.setLayoutData(fd_firstNameBox);
						
								lastNameBox = new Text(infoGroup, SWT.BORDER);
								FormData fd_lastNameBox = new FormData();
								fd_lastNameBox.top = new FormAttachment(firstNameBox, 0, SWT.TOP);
								fd_lastNameBox.bottom = new FormAttachment(firstNameBox, 0, SWT.BOTTOM);
								fd_lastNameBox.left = new FormAttachment(firstNameBox, boxOffsetX, SWT.RIGHT);
								fd_lastNameBox.right = new FormAttachment(100, -6);
								lastNameBox.setLayoutData(fd_lastNameBox);
														
														emailBox = new Text(infoGroup, SWT.BORDER);
														FormData fd_emailBox = new FormData();
														fd_emailBox.top = new FormAttachment(lblEmail, boxOffsetY, SWT.TOP);
														fd_emailBox.left = new FormAttachment(firstNameBox, 0, SWT.LEFT);
														fd_emailBox.right = new FormAttachment(100, -6);
														fd_emailBox.height = boxHeight;
														emailBox.setLayoutData(fd_emailBox);
														
														dobDay = new CCombo(infoGroup, SWT.BORDER);
														FormData fd_dobDay = new FormData();
														fd_dobDay.right = new FormAttachment(45, 0);
														fd_dobDay.top = new FormAttachment(lblDob, boxOffsetY, SWT.TOP);
														fd_dobDay.left = new FormAttachment(firstNameBox, 0, SWT.LEFT);
														fd_dobDay.height = nudHeight;
														dobDay.setLayoutData(fd_dobDay);
														dobDay.setText("Day");
														
														dobMonth = new CCombo(infoGroup, SWT.BORDER);
														FormData fd_dobMonth = new FormData();
														fd_dobMonth.right = new FormAttachment(75, 0);
														fd_dobMonth.top = new FormAttachment(dobDay, 0, SWT.TOP);
														fd_dobMonth.bottom = new FormAttachment(dobDay, 0, SWT.BOTTOM);
														fd_dobMonth.left = new FormAttachment(dobDay, boxOffsetX, SWT.RIGHT);
														dobMonth.setLayoutData(fd_dobMonth);
														dobMonth.setText("Month");
														
														dobYear = new CCombo(infoGroup, SWT.BORDER);
														FormData fd_dobYear = new FormData();
														fd_dobYear.right = new FormAttachment(100, -6);
														fd_dobYear.top = new FormAttachment(dobDay, 0, SWT.TOP);
														fd_dobYear.bottom = new FormAttachment(dobDay, 0, SWT.BOTTOM);
														fd_dobYear.left = new FormAttachment(dobMonth, boxOffsetX, SWT.RIGHT);
														dobYear.setLayoutData(fd_dobYear);
														dobYear.setText("Year");
														
														annDay = new CCombo(infoGroup, SWT.BORDER);
														FormData fd_annDay = new FormData();
														fd_annDay.top = new FormAttachment(lblAnniversary, boxOffsetY, SWT.TOP);
														fd_annDay.left = new FormAttachment(dobDay, 0, SWT.LEFT);
														fd_annDay.right = new FormAttachment(dobDay, 0, SWT.RIGHT);
														fd_annDay.height = nudHeight;
														annDay.setLayoutData(fd_annDay);
														annDay.setText("Day");
														
														annMonth = new CCombo(infoGroup, SWT.BORDER);
														FormData fd_annMonth = new FormData();
														fd_annMonth.top = new FormAttachment(annDay, 0, SWT.TOP);
														fd_annMonth.bottom = new FormAttachment(annDay, 0, SWT.BOTTOM);
														fd_annMonth.left = new FormAttachment(dobMonth, 0, SWT.LEFT);
														fd_annMonth.right = new FormAttachment(dobMonth, 0, SWT.RIGHT);
														annMonth.setLayoutData(fd_annMonth);
														annMonth.setText("Month");
														
														annYear = new CCombo(infoGroup, SWT.BORDER);
														FormData fd_annYear = new FormData();
														fd_annYear.top = new FormAttachment(annDay, 0, SWT.TOP);
														fd_annYear.bottom = new FormAttachment(annDay, 0, SWT.BOTTOM);
														fd_annYear.left = new FormAttachment(dobYear, 0, SWT.LEFT);
														fd_annYear.right = new FormAttachment(dobYear, 0, SWT.RIGHT);
														annYear.setLayoutData(fd_annYear);
														annYear.setText("Year");

		Group grpPhoneNumbers = new Group(this, SWT.NONE);
		grpPhoneNumbers.setLayout(new FormLayout());
		FormData fd_grpPhoneNumbers = new FormData();
		fd_grpPhoneNumbers.top = new FormAttachment(infoGroup, 6, SWT.BOTTOM);
		fd_grpPhoneNumbers.right = new FormAttachment(infoGroup, 0, SWT.RIGHT);
		fd_grpPhoneNumbers.left = new FormAttachment(infoGroup, 0, SWT.LEFT);
		fd_grpPhoneNumbers.height = groupHeight;
		grpPhoneNumbers.setLayoutData(fd_grpPhoneNumbers);
		grpPhoneNumbers.setText("Phone Numbers");
		
				Label lblHome = new Label(grpPhoneNumbers, SWT.NONE);
				FormData fd_lblHome = new FormData();
				fd_lblHome.left = new FormAttachment(0, 6);
				fd_lblHome.top = new FormAttachment(0, 6);
				fd_lblHome.width = labelWidth;
				fd_lblHome.height = labelHeight;
				lblHome.setLayoutData(fd_lblHome);
				lblHome.setText("Home");
						
								Label lblCellular = new Label(grpPhoneNumbers, SWT.NONE);
								FormData fd_lblCellular = new FormData();
								fd_lblCellular.top = new FormAttachment(lblHome, labelDistance, SWT.BOTTOM);
								fd_lblCellular.left = new FormAttachment(lblHome, 0, SWT.LEFT);
								fd_lblCellular.width = labelWidth;
								fd_lblCellular.height = labelHeight;
								lblCellular.setLayoutData(fd_lblCellular);
								lblCellular.setText("Cellular");
						
								Label lblWork = new Label(grpPhoneNumbers, SWT.NONE);
								FormData fd_lblWork = new FormData();
								fd_lblWork.top = new FormAttachment(lblCellular, labelDistance, SWT.BOTTOM);
								fd_lblWork.left = new FormAttachment(lblHome, 0, SWT.LEFT);
								fd_lblWork.width = labelWidth;
								fd_lblWork.height = labelHeight;
								lblWork.setLayoutData(fd_lblWork);
								lblWork.setText("Work");
						
								Label lblAlternative = new Label(grpPhoneNumbers, SWT.NONE);
								FormData fd_lblAlternative = new FormData();
								fd_lblAlternative.top = new FormAttachment(lblWork, labelDistance, SWT.BOTTOM);
								fd_lblAlternative.left = new FormAttachment(lblHome, 0, SWT.LEFT);
								fd_lblAlternative.width = labelWidth;
								fd_lblAlternative.height = labelHeight;
								lblAlternative.setLayoutData(fd_lblAlternative);
								lblAlternative.setText("Alternative");
				
						numHomeBox = new Text(grpPhoneNumbers, SWT.BORDER);
						FormData fd_numHomeBox = new FormData();
						fd_numHomeBox.top = new FormAttachment(lblHome, boxOffsetY, SWT.TOP);
						fd_numHomeBox.left = new FormAttachment(lblHome, 6, SWT.RIGHT);
						fd_numHomeBox.right = new FormAttachment(100, -6);
						fd_numHomeBox.height = boxHeight;
						numHomeBox.setLayoutData(fd_numHomeBox);
						
								numCellularBox = new Text(grpPhoneNumbers, SWT.BORDER);
								FormData fd_numCellularBox = new FormData();
								fd_numCellularBox.top = new FormAttachment(lblCellular, boxOffsetY, SWT.TOP);
								fd_numCellularBox.left = new FormAttachment(numHomeBox, 0, SWT.LEFT);
								fd_numCellularBox.right = new FormAttachment(100, -6);
								fd_numCellularBox.height = boxHeight;
								numCellularBox.setLayoutData(fd_numCellularBox);
												
														numWorkBox = new Text(grpPhoneNumbers, SWT.BORDER);
														FormData fd_numWorkBox = new FormData();
														fd_numWorkBox.top = new FormAttachment(lblWork, boxOffsetY, SWT.TOP);
														fd_numWorkBox.left = new FormAttachment(numHomeBox, 0, SWT.LEFT);
														fd_numWorkBox.right = new FormAttachment(100, -6);
														fd_numWorkBox.height = boxHeight;
														numWorkBox.setLayoutData(fd_numWorkBox);
														
																numAltBox = new Text(grpPhoneNumbers, SWT.BORDER);
																FormData fd_numAltBox = new FormData();
																fd_numAltBox.top = new FormAttachment(lblAlternative, boxOffsetY, SWT.TOP);
																fd_numAltBox.left = new FormAttachment(numHomeBox, 0, SWT.LEFT);
																fd_numAltBox.right = new FormAttachment(100, -6);
																fd_numWorkBox.height = boxHeight;
																numAltBox.setLayoutData(fd_numAltBox);

		Group grpAddresses = new Group(this, SWT.NONE);
		grpAddresses.setLayout(new FormLayout());
		FormData fd_grpAddresses = new FormData();
		fd_grpAddresses.left = new FormAttachment(infoGroup, 0, SWT.LEFT);
		fd_grpAddresses.right = new FormAttachment(infoGroup, 0, SWT.RIGHT);
		fd_grpAddresses.top = new FormAttachment(grpPhoneNumbers, 6, SWT.BOTTOM);
		fd_grpAddresses.height = groupHeight;
		grpAddresses.setLayoutData(fd_grpAddresses);
		grpAddresses.setText("Addresses");
				
						Label addrHomeLbl = new Label(grpAddresses, SWT.NONE);
						FormData fd_addrHomeLbl = new FormData();
						fd_addrHomeLbl.left = new FormAttachment(0, 6);
						fd_addrHomeLbl.top = new FormAttachment(0, 6);
						fd_addrHomeLbl.width = labelWidth;
						fd_addrHomeLbl.height = labelHeight;
						addrHomeLbl.setLayoutData(fd_addrHomeLbl);
						addrHomeLbl.setText("Home");
		
				Label lblAlternative_1 = new Label(grpAddresses, SWT.NONE);
				FormData fd_lblAlternative_1 = new FormData();
				fd_lblAlternative_1.top = new FormAttachment(addrHomeLbl, labelDistance, SWT.BOTTOM);
				fd_lblAlternative_1.left = new FormAttachment(addrHomeLbl, 0, SWT.LEFT);
				fd_lblAlternative_1.width = labelWidth;
				fd_lblAlternative_1.height = labelHeight;
				lblAlternative_1.setLayoutData(fd_lblAlternative_1);
				lblAlternative_1.setText("Alternative 1");
						
								Label lblAlternative_2 = new Label(grpAddresses, SWT.NONE);
								FormData fd_lblAlternative_2 = new FormData();
								fd_lblAlternative_2.top = new FormAttachment(lblAlternative_1, labelDistance, SWT.BOTTOM);
								fd_lblAlternative_2.left = new FormAttachment(addrHomeLbl, 0, SWT.LEFT);
								fd_lblAlternative_2.width = labelWidth;
								fd_lblAlternative_2.height = labelHeight;
								lblAlternative_2.setLayoutData(fd_lblAlternative_2);
								lblAlternative_2.setText("Alternative 2");
								
										addrHomeBox = new Text(grpAddresses, SWT.BORDER);
										FormData fd_addrHomeBox = new FormData();
										fd_addrHomeBox.top = new FormAttachment(addrHomeLbl, boxOffsetY, SWT.TOP);
										fd_addrHomeBox.left = new FormAttachment(addrHomeLbl, 6, SWT.RIGHT);
										fd_addrHomeBox.right = new FormAttachment(100, -6);
										fd_addrHomeBox.height = boxHeight;
										addrHomeBox.setLayoutData(fd_addrHomeBox);
										
												addrAlt1Box = new Text(grpAddresses, SWT.BORDER);
												FormData fd_addrAlt1Box = new FormData();
												fd_addrAlt1Box.top = new FormAttachment(lblAlternative_1, boxOffsetY, SWT.TOP);
												fd_addrAlt1Box.left = new FormAttachment(addrHomeBox, 0, SWT.LEFT);
												fd_addrAlt1Box.right = new FormAttachment(100, -6);
												fd_addrAlt1Box.height = boxHeight;
												addrAlt1Box.setLayoutData(fd_addrAlt1Box);
												
														addrAlt2Box = new Text(grpAddresses, SWT.BORDER);
														FormData fd_addrAlt2Box = new FormData();
														fd_addrAlt2Box.top = new FormAttachment(lblAlternative_2, boxOffsetY, SWT.TOP);
														fd_addrAlt2Box.left = new FormAttachment(addrHomeBox, 0, SWT.LEFT);
														fd_addrAlt2Box.right = new FormAttachment(100, -6);
														fd_addrAlt2Box.height = boxHeight;
														addrAlt2Box.setLayoutData(fd_addrAlt2Box);
														
		initDateValues();
		clearValues();
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
	
	public void clearValues()
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
	
	public void setClient(Client client)
	{
		clearValues();
		
		if(client != null)
		{
			firstNameBox.setText(client.getFirstName());
			lastNameBox.setText(client.getLastName());
			
			if (client.getEmail() == null)
				emailBox.setText("");
			else
				emailBox.setText(client.getEmail());
			
			if(client.getBirthday() != null)
			{
				dobDay.setText("" + client.getBirthday().get(Calendar.DAY_OF_MONTH));
				dobMonth.select(client.getBirthday().get(Calendar.MONTH));
				dobYear.setText("" + client.getBirthday().get(Calendar.YEAR));
			}
			
			if(client.getAnniversary() != null)
			{
				annDay.setText("" + client.getAnniversary().get(Calendar.DAY_OF_MONTH));
				annMonth.select(client.getAnniversary().get(Calendar.MONTH));
				annYear.setText("" + client.getAnniversary().get(Calendar.YEAR));
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
		}
	}
	
	public void getClientFromFields(Client out)
	{
		int year, month, day;
		
		out.setFirstName(firstNameBox.getText());
		out.setLastName(lastNameBox.getText());
		out.setEmail(emailBox.getText());
		
		try
		{
			year = Integer.parseInt(dobYear.getText());
			month = Utility.stringToMonth(dobMonth.getText());
			day = Integer.parseInt(dobDay.getText());
			
			if (month >= 0)
			{
				Calendar newDob = Calendar.getInstance();
				newDob.set(year, month, day);
				out.setBirthday(newDob);
			}
		}
		catch (Exception e)
		{
			
		}
		
		try
		{
			year = Integer.parseInt(annYear.getText());
			month = Utility.stringToMonth(annMonth.getText());
			day = Integer.parseInt(annDay.getText());
			
			if (month >= 0)
			{
				Calendar newAnn = Calendar.getInstance();
				newAnn.set(year, month, day);
				out.setAnniversary(newAnn);
			}
		}
		catch (Exception e)
		{
			
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
		
		out.setAddresses(updatedAddresses);
		out.setNumbers(updatedNumbers);
	}
	
	public void setModify(boolean modify)
	{
		firstNameBox.setEnabled(modify);
		lastNameBox.setEnabled(modify);
		emailBox.setEnabled(modify);
		
		dobDay.setEnabled(modify);
		dobMonth.setEnabled(modify);
		dobYear.setEnabled(modify);
		
		annDay.setEnabled(modify);
		annMonth.setEnabled(modify);
		annYear.setEnabled(modify);
		
		numHomeBox.setEnabled(modify);
		numCellularBox.setEnabled(modify);
		numWorkBox.setEnabled(modify);
		numAltBox.setEnabled(modify);
		
		addrHomeBox.setEnabled(modify);
		addrAlt1Box.setEnabled(modify);
		addrAlt2Box.setEnabled(modify);
	}

	/*@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}*/

}
