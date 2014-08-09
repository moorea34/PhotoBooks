package photobooks.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import photobooks.objects.*;
import photobooks.objects.PhoneNumber.PhoneNumberType;

public class ClientEditor extends Composite {
	
	private Text firstNameBox;
	private Text lastNameBox;
	private Text numHomeBox;
	private Text numCellularBox;
	private Text numWorkBox;
	private Text numAltBox;
	private Text emailBox;
	private Label lblAddress;
	private Text tbAddress;
	private Label lblCity;
	private Text tbCity;
	private Label lblProvince;
	private Text tbProvince;
	private Label lblPostalCode;
	private Text tbPostalCode;
	private DatePicker dpBirthday, dpAnniversary;

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
														
														dpBirthday = new DatePicker(infoGroup, SWT.NONE);
														FormData fd_dob = new FormData();
														fd_dob.left = new FormAttachment(firstNameBox, 0, SWT.LEFT);
														fd_dob.right = new FormAttachment(100, -6);
														fd_dob.top = new FormAttachment(lblDob, boxOffsetY, SWT.TOP);
														fd_dob.height = nudHeight + 4;
														dpBirthday.setLayoutData(fd_dob);
														
														dpAnniversary = new DatePicker(infoGroup, SWT.NONE);
														FormData fd_ann = new FormData();
														fd_ann.top = new FormAttachment(lblAnniversary, boxOffsetY, SWT.TOP);
														fd_ann.left = new FormAttachment(dpBirthday, 0, SWT.LEFT);
														fd_ann.right = new FormAttachment(dpBirthday, 0, SWT.RIGHT);
														fd_ann.height = nudHeight + 4;
														dpAnniversary.setLayoutData(fd_ann);

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
		grpAddresses.setText("Address");
										
										lblAddress = new Label(grpAddresses, SWT.NONE);
										lblAddress.setText("Address");
										FormData fd_lblAddress = new FormData();
										fd_lblAddress.top = new FormAttachment(0, 6);
										fd_lblAddress.left = new FormAttachment(0, 6);
										fd_lblAddress.width = labelWidth;
										fd_lblAddress.height = labelHeight;
										lblAddress.setLayoutData(fd_lblAddress);
										
										lblCity = new Label(grpAddresses, SWT.NONE);
										lblCity.setText("City");
										FormData fd_lblCity = new FormData();
										fd_lblCity.top = new FormAttachment(lblAddress, 10);
										fd_lblCity.left = new FormAttachment(lblAddress, 0, SWT.LEFT);
										fd_lblCity.width = labelWidth;
										fd_lblCity.height = labelHeight;
										lblCity.setLayoutData(fd_lblCity);
										
										lblProvince = new Label(grpAddresses, SWT.NONE);
										lblProvince.setText("Province");
										FormData fd_lblProvince = new FormData();
										fd_lblProvince.top = new FormAttachment(lblCity, 10);
										fd_lblProvince.left = new FormAttachment(lblAddress, 0, SWT.LEFT);
										fd_lblProvince.width = labelWidth;
										fd_lblProvince.height = labelHeight;
										lblProvince.setLayoutData(fd_lblProvince);
										
										lblPostalCode = new Label(grpAddresses, SWT.NONE);
										lblPostalCode.setText("Postal Code");
										FormData fd_lblPostalCode = new FormData();
										fd_lblPostalCode.top = new FormAttachment(lblProvince, 10);
										fd_lblPostalCode.left = new FormAttachment(lblAddress, 0, SWT.LEFT);
										fd_lblPostalCode.width = labelWidth;
										fd_lblPostalCode.height = labelHeight;
										lblPostalCode.setLayoutData(fd_lblPostalCode);
														
														tbAddress = new Text(grpAddresses, SWT.BORDER);
														tbAddress.setText("");
														FormData fd_tbAddress = new FormData();
														fd_tbAddress.top = new FormAttachment(lblAddress, boxOffsetY, SWT.TOP);
														fd_tbAddress.left = new FormAttachment(lblAddress, 6);
														fd_tbAddress.right = new FormAttachment(100, -6);
														fd_tbAddress.height = boxHeight;
														tbAddress.setLayoutData(fd_tbAddress);
														
														tbCity = new Text(grpAddresses, SWT.BORDER);
														tbCity.setText("");
														FormData fd_tbCity = new FormData();
														fd_tbCity.top = new FormAttachment(lblCity, boxOffsetY, SWT.TOP);
														fd_tbCity.left = new FormAttachment(tbAddress, 0, SWT.LEFT);
														fd_tbCity.right = new FormAttachment(tbAddress, 0, SWT.RIGHT);
														fd_tbCity.height = boxHeight;
														tbCity.setLayoutData(fd_tbCity);
														
														tbProvince = new Text(grpAddresses, SWT.BORDER);
														tbProvince.setText("");
														FormData fd_tbProvince = new FormData();
														fd_tbProvince.top = new FormAttachment(lblProvince, boxOffsetY, SWT.TOP);
														fd_tbProvince.left = new FormAttachment(tbAddress, 0, SWT.LEFT);
														fd_tbProvince.right = new FormAttachment(tbAddress, 0, SWT.RIGHT);
														fd_tbProvince.height = boxHeight;
														tbProvince.setLayoutData(fd_tbProvince);
														
														tbPostalCode = new Text(grpAddresses, SWT.BORDER);
														tbPostalCode.setText("");
														FormData fd_tbPostalCode = new FormData();
														fd_tbPostalCode.top = new FormAttachment(lblPostalCode, boxOffsetY, SWT.TOP);
														fd_tbPostalCode.left = new FormAttachment(tbAddress, 0, SWT.LEFT);
														fd_tbPostalCode.right = new FormAttachment(tbAddress, 0, SWT.RIGHT);
														fd_tbPostalCode.height = boxHeight;
														tbPostalCode.setLayoutData(fd_tbPostalCode);
							
		clearValues();
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
		
		dpBirthday.clear();
		dpAnniversary.clear();
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
			
			dpBirthday.setDate(client.getBirthday());
			dpAnniversary.setDate(client.getAnniversary());
			
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
			
			tbAddress.setText(client.getAddress());
			tbCity.setText(client.getCity());
			tbProvince.setText(client.getProvince());
			tbPostalCode.setText(client.getPostalCode());
		}
	}
	
	public void getClientFromFields(Client out)
	{
		out.setFirstName(firstNameBox.getText());
		out.setLastName(lastNameBox.getText());
		out.setEmail(emailBox.getText());
		
		out.setBirthday(dpBirthday.getDate());
		
		out.setAnniversary(dpAnniversary.getDate());
		
		updatePhoneNumber(PhoneNumberType.Home, numHomeBox, out);
		updatePhoneNumber(PhoneNumberType.Cellular, numCellularBox, out);
		updatePhoneNumber(PhoneNumberType.Work, numWorkBox, out);
		updatePhoneNumber(PhoneNumberType.Alternative, numAltBox, out);
		
		out.setAddress(tbAddress.getText());
		out.setCity(tbCity.getText());
		out.setProvince(tbProvince.getText());
		out.setPostalCode(tbPostalCode.getText());
	}
	
	private void updatePhoneNumber(PhoneNumberType type, Text tb, Client out)
	{
		for (PhoneNumber number : out.getNumbers())
		{
			if (number.getType() == type)
			{
				number.setNumber(tb.getText());
				return;
			}
		}
		
		out.getNumbers().add(new PhoneNumber(type, tb.getText()));
	}
	
	public void setModify(boolean modify)
	{
		firstNameBox.setEnabled(modify);
		lastNameBox.setEnabled(modify);
		emailBox.setEnabled(modify);
		
		dpBirthday.setModify(modify);
		dpAnniversary.setModify(modify);
		
		numHomeBox.setEnabled(modify);
		numCellularBox.setEnabled(modify);
		numWorkBox.setEnabled(modify);
		numAltBox.setEnabled(modify);
		
		tbAddress.setEnabled(modify);
		tbCity.setEnabled(modify);
		tbProvince.setEnabled(modify);
		tbPostalCode.setEnabled(modify);
	}

	/*@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}*/

}
