package photobooks.presentation;

import java.util.*;

import photobooks.application.Utility;
import photobooks.objects.Address;
import photobooks.objects.Address.AddressType;
import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;
import photobooks.objects.PhoneNumber.PhoneNumberType;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.custom.CCombo;

import acceptanceTests.EventLoop;
import acceptanceTests.Register; 

public class AddClientWindow extends Dialog 
{

	private Client result;
	private Shell shell;
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
	private Button btnOkay;
	private Button btnCancel;

	public AddClientWindow(Shell parent, int style) 
	{
		super(parent, style);
		setText("New Client");
		
		Register.newWindow(this);
	}

	public Client open() 
	{
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		if(EventLoop.isEnabled())
		{
			while (!shell.isDisposed()) 
			{
				if (!display.readAndDispatch()) 
				{
					display.sleep();
				}
			}
		}
		return result;
	}

	private void createContents() 
	{
		shell = new Shell(getParent(), getStyle());
		shell.setSize(411, 451);
		shell.setText(getText());
		
		Group infoGroup = new Group(shell, SWT.NONE);
		infoGroup.setText("Personal Information");
		infoGroup.setBounds(10, 10, 390, 125);
		
		Label lblName = new Label(infoGroup, SWT.NONE);
		lblName.setText("Name");
		lblName.setBounds(10, 23, 93, 15);
		
		Label lblEmail = new Label(infoGroup, SWT.NONE);
		lblEmail.setText("Email");
		lblEmail.setBounds(10, 47, 93, 15);
		
		Label lblDOB = new Label(infoGroup, SWT.NONE);
		lblDOB.setText("DOB");
		lblDOB.setBounds(10, 71, 93, 15);
		
		Label lblAnniversary = new Label(infoGroup, SWT.NONE);
		lblAnniversary.setText("Anniversary");
		lblAnniversary.setBounds(10, 95, 93, 15);
		
		firstNameBox = new Text(infoGroup, SWT.BORDER);
		firstNameBox.setBounds(109, 20, 132, 21);
		
		lastNameBox = new Text(infoGroup, SWT.BORDER);
		lastNameBox.setBounds(248, 20, 132, 21);
		
		emailBox = new Text(infoGroup, SWT.BORDER);
		emailBox.setBounds(109, 44, 271, 21);
		
		dobDay = new CCombo(infoGroup, SWT.BORDER);
		dobDay.setText("Day");
		dobDay.setEditable(false);
		dobDay.setBounds(109, 68, 72, 21);
		
		dobMonth = new CCombo(infoGroup, SWT.BORDER);
		dobMonth.setText("Month");
		dobMonth.setEditable(false);
		dobMonth.setBounds(187, 68, 107, 21);
		
		dobYear = new CCombo(infoGroup, SWT.BORDER);
		dobYear.setText("Year");
		dobYear.setEditable(false);
		dobYear.setBounds(300, 68, 80, 21);
		
		annDay = new CCombo(infoGroup, SWT.BORDER);
		annDay.setText("Day");
		annDay.setEditable(false);
		annDay.setBounds(109, 92, 72, 21);
		
		annMonth = new CCombo(infoGroup, SWT.BORDER);
		annMonth.setText("Month");
		annMonth.setEditable(false);
		annMonth.setBounds(187, 92, 107, 21);
		
		annYear = new CCombo(infoGroup, SWT.BORDER);
		annYear.setText("Year");
		annYear.setEditable(false);
		annYear.setBounds(300, 92, 80, 21);
		
		Group numberGroup = new Group(shell, SWT.NONE);
		numberGroup.setText("Phone Numbers");
		numberGroup.setBounds(10, 141, 390, 125);
		
		Label lblNumHome = new Label(numberGroup, SWT.NONE);
		lblNumHome.setText("Home");
		lblNumHome.setBounds(10, 23, 93, 15);
		
		Label lblNumCellular = new Label(numberGroup, SWT.NONE);
		lblNumCellular.setText("Cellular");
		lblNumCellular.setBounds(10, 47, 93, 15);
		
		Label lblNumWork = new Label(numberGroup, SWT.NONE);
		lblNumWork.setText("Work");
		lblNumWork.setBounds(10, 71, 93, 15);
		
		Label lblNumAlt = new Label(numberGroup, SWT.NONE);
		lblNumAlt.setText("Alternative");
		lblNumAlt.setBounds(10, 95, 93, 15);
		
		numHomeBox = new Text(numberGroup, SWT.BORDER);
		numHomeBox.setBounds(109, 20, 271, 21);
		
		numCellularBox = new Text(numberGroup, SWT.BORDER);
		numCellularBox.setBounds(109, 44, 271, 21);
		
		numWorkBox = new Text(numberGroup, SWT.BORDER);
		numWorkBox.setBounds(109, 68, 271, 21);
		
		numAltBox = new Text(numberGroup, SWT.BORDER);
		numAltBox.setBounds(109, 92, 271, 21);
		
		Group addressGroup = new Group(shell, SWT.NONE);
		addressGroup.setText("Addresses");
		addressGroup.setBounds(10, 272, 390, 114);
		
		Label lblAddrHome = new Label(addressGroup, SWT.NONE);
		lblAddrHome.setText("Home");
		lblAddrHome.setBounds(10, 24, 93, 15);
		
		Label lblAddrAlt1 = new Label(addressGroup, SWT.NONE);
		lblAddrAlt1.setText("Alternative 1");
		lblAddrAlt1.setBounds(10, 48, 93, 15);
		
		Label lblAddrAlt2 = new Label(addressGroup, SWT.NONE);
		lblAddrAlt2.setText("Alternative 2");
		lblAddrAlt2.setBounds(10, 72, 93, 15);
		
		addrHomeBox = new Text(addressGroup, SWT.BORDER);
		addrHomeBox.setBounds(109, 21, 271, 21);
		
		addrAlt1Box = new Text(addressGroup, SWT.BORDER);
		addrAlt1Box.setBounds(109, 45, 271, 21);
		
		addrAlt2Box = new Text(addressGroup, SWT.BORDER);
		addrAlt2Box.setBounds(109, 69, 271, 21);
		
		btnOkay = new Button(shell, SWT.NONE);
		btnOkay.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				parseInput();
				shell.dispose();
			}
		});
		btnOkay.setBounds(325, 392, 75, 25);
		btnOkay.setText("Okay");
		
		btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shell.dispose();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(244, 392, 75, 25);
		
		initDateValues();

	}
	
	private void parseInput()
	{
		ArrayList<Address> addresses = new ArrayList<Address>();
		ArrayList<PhoneNumber> numbers = new ArrayList<PhoneNumber>();
		
		if(!addrHomeBox.getText().equals(""))
			addresses.add(new Address(AddressType.Home, addrHomeBox.getText()));
		if(!addrAlt1Box.getText().equals(""))
			addresses.add(new Address(AddressType.Alternative1, addrAlt1Box.getText()));
		if(!addrAlt2Box.getText().equals(""))
			addresses.add(new Address(AddressType.Alternative2, addrAlt2Box.getText()));
		
		if(!numHomeBox.getText().equals(""))
			numbers.add(new PhoneNumber(PhoneNumberType.Home, numHomeBox.getText()));
		if(!numCellularBox.getText().equals(""))
			numbers.add(new PhoneNumber(PhoneNumberType.Cellular, numCellularBox.getText()));
		if(!numWorkBox.getText().equals(""))
			numbers.add(new PhoneNumber(PhoneNumberType.Work, numWorkBox.getText()));
		if(!numAltBox.getText().equals(""))
			numbers.add(new PhoneNumber(PhoneNumberType.Alternative, numAltBox.getText()));

		Calendar newDob = null;
		if(dobMonth.getSelectionIndex() != -1 && dobYear.getSelectionIndex() != -1 && dobDay.getSelectionIndex() != -1)
		{
			newDob = Calendar.getInstance();
			newDob.set(Integer.parseInt(dobYear.getItem(dobYear.getSelectionIndex())), dobMonth.getSelectionIndex(), dobDay.getSelectionIndex() + 1);
		}
		
		Calendar newAnn = null;
		if(annMonth.getSelectionIndex() != -1 && annYear.getSelectionIndex() != -1 && annDay.getSelectionIndex() != -1)
		{
			newAnn = Calendar.getInstance();
			newAnn.set(Integer.parseInt(annYear.getItem(annYear.getSelectionIndex())), annMonth.getSelectionIndex(), annDay.getSelectionIndex() + 1);
		}
		
		result = new Client(
				firstNameBox.getText(),
				lastNameBox.getText(),
				emailBox.getText(),
				newDob,
				newAnn,
				numbers,
				addresses);
		
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
	
}
