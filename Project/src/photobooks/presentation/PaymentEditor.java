package photobooks.presentation;

import java.text.DecimalFormat;
import java.util.Calendar;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Text;

import photobooks.objects.Payment;
import photobooks.objects.Payment.TenderType;

public class PaymentEditor extends Composite {
	private DecimalFormat format = new DecimalFormat("0.00");
	
	private Text tbAmount;
	private Text tbDescription;
	
	private Label lblDateValue;
	private Label lblUser;
	private Label lblInvoiceRefValue;
	private Label lblPaymentValue;
	
	private CCombo cbFormOfPayment;
	
	private String[] paymentTypes = new String[] { "Cash", "Cheque", "Gift", "Other" };

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PaymentEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		Label lblPayment = new Label(this, SWT.NONE);
		FormData fd_lblPayment = new FormData();
		fd_lblPayment.top = new FormAttachment(0, 0);
		lblPayment.setLayoutData(fd_lblPayment);
		lblPayment.setText("Payment #:");
		
		Label lblDate = new Label(this, SWT.NONE);
		FormData fd_lblDate = new FormData();
		fd_lblDate.top = new FormAttachment(lblPayment, 15);
		fd_lblDate.left = new FormAttachment(0, 0);
		lblDate.setLayoutData(fd_lblDate);
		lblDate.setText("Date:");
		
		Label lblAccount = new Label(this, SWT.NONE);
		FormData fd_lblAccount = new FormData();
		fd_lblAccount.top = new FormAttachment(lblDate, 15);
		fd_lblAccount.left = new FormAttachment(lblDate, 0, SWT.LEFT);
		lblAccount.setLayoutData(fd_lblAccount);
		lblAccount.setText("Account:");
		
		Label lblAmount = new Label(this, SWT.NONE);
		FormData fd_lblAmount = new FormData();
		fd_lblAmount.top = new FormAttachment(lblAccount, 15);
		fd_lblAmount.left = new FormAttachment(lblDate, 0, SWT.LEFT);
		lblAmount.setLayoutData(fd_lblAmount);
		lblAmount.setText("Amount:");
		
		Label lblFormOfPayment = new Label(this, SWT.NONE);
		FormData fd_lblFormOfPayment = new FormData();
		fd_lblFormOfPayment.top = new FormAttachment(lblAmount, 15);
		fd_lblFormOfPayment.left = new FormAttachment(lblDate, 0, SWT.LEFT);
		lblFormOfPayment.setLayoutData(fd_lblFormOfPayment);
		lblFormOfPayment.setText("Form of Payment:");
		
		Label lblInvoiceRef = new Label(this, SWT.NONE);
		FormData fd_lblInvoiceRef = new FormData();
		fd_lblInvoiceRef.top = new FormAttachment(lblFormOfPayment, 15);
		fd_lblInvoiceRef.left = new FormAttachment(lblDate, 0, SWT.LEFT);
		lblInvoiceRef.setLayoutData(fd_lblInvoiceRef);
		lblInvoiceRef.setText("Invoice Ref #:");
		
		cbFormOfPayment = new CCombo(this, SWT.BORDER);
		cbFormOfPayment.setText("Cash");
		FormData fd_cbFormOfPayment = new FormData();
		fd_cbFormOfPayment.right = new FormAttachment(100, 0);
		fd_cbFormOfPayment.bottom = new FormAttachment(lblFormOfPayment, 0, SWT.BOTTOM);
		fd_cbFormOfPayment.left = new FormAttachment(lblFormOfPayment, 6);
		cbFormOfPayment.setLayoutData(fd_cbFormOfPayment);
		cbFormOfPayment.setItems(paymentTypes);
		
		lblDateValue = new Label(this, SWT.NONE);
		FormData fd_lblDateValue = new FormData();
		fd_lblDateValue.bottom = new FormAttachment(lblDate, 0, SWT.BOTTOM);
		fd_lblDateValue.left = new FormAttachment(cbFormOfPayment, 0, SWT.LEFT);
		fd_lblDateValue.right = new FormAttachment(100, 0);
		lblDateValue.setLayoutData(fd_lblDateValue);
		
		lblUser = new Label(this, SWT.NONE);
		FormData fd_lblUser = new FormData();
		fd_lblUser.bottom = new FormAttachment(lblAccount, 0, SWT.BOTTOM);
		fd_lblUser.left = new FormAttachment(cbFormOfPayment, 0, SWT.LEFT);
		fd_lblUser.right = new FormAttachment(100, 0);
		lblUser.setLayoutData(fd_lblUser);
		
		tbAmount = new Text(this, SWT.BORDER);
		tbAmount.setText("0");
		FormData fd_tbAmount = new FormData();
		fd_tbAmount.right = new FormAttachment(100, 0);
		fd_tbAmount.bottom = new FormAttachment(lblAmount, 0, SWT.BOTTOM);
		fd_tbAmount.left = new FormAttachment(cbFormOfPayment, 0, SWT.LEFT);
		tbAmount.setLayoutData(fd_tbAmount);
		
		lblInvoiceRefValue = new Label(this, SWT.NONE);
		FormData fd_lblInvoiceRefValue = new FormData();
		fd_lblInvoiceRefValue.bottom = new FormAttachment(lblInvoiceRef, 0, SWT.BOTTOM);
		fd_lblInvoiceRefValue.left = new FormAttachment(cbFormOfPayment, 0, SWT.LEFT);
		fd_lblInvoiceRefValue.right = new FormAttachment(100, 0);
		lblInvoiceRefValue.setLayoutData(fd_lblInvoiceRefValue);
		
		Label lblDescription = new Label(this, SWT.NONE);
		FormData fd_lblDescription = new FormData();
		fd_lblDescription.top = new FormAttachment(lblInvoiceRef, 15);
		fd_lblDescription.left = new FormAttachment(lblDate, 0, SWT.LEFT);
		lblDescription.setLayoutData(fd_lblDescription);
		lblDescription.setText("Description:");
		
		tbDescription = new Text(this, SWT.BORDER);
		FormData fd_tbDescription = new FormData();
		fd_tbDescription.bottom = new FormAttachment(lblDescription, 0, SWT.BOTTOM);
		fd_tbDescription.left = new FormAttachment(cbFormOfPayment, 0, SWT.LEFT);
		fd_tbDescription.right = new FormAttachment(100, 0);
		tbDescription.setLayoutData(fd_tbDescription);
		
		lblPaymentValue = new Label(this, SWT.NONE);
		lblPaymentValue.setText("0");
		FormData fd_lblPaymentValue = new FormData();
		fd_lblPaymentValue.bottom = new FormAttachment(lblPayment, 0, SWT.BOTTOM);
		fd_lblPaymentValue.left = new FormAttachment(cbFormOfPayment, 0, SWT.LEFT);
		fd_lblPaymentValue.right = new FormAttachment(100, 0);
		lblPaymentValue.setLayoutData(fd_lblPaymentValue);

	}
	
	public void clearFields()
	{
		tbDescription.setText("");
		tbAmount.setText("0.00");
		cbFormOfPayment.setText("Cash");
		
		lblInvoiceRefValue.setText("");
		lblUser.setText("");
		lblDateValue.setText("");
		lblPaymentValue.setText("0");
	}
	
	public void setPayment(Payment payment)
	{
		if (payment != null)
		{
			tbDescription.setText(payment.getDescription());
			tbAmount.setText(format.format(payment.getAmount()));
			cbFormOfPayment.setText(payment.getTenderType().toString());
			
			lblInvoiceRefValue.setText("" + payment.getInvoiceId());
			lblUser.setText(payment.getClient().getFormattedName());
			lblDateValue.setText(payment.getDate().get(Calendar.YEAR) + " " + (payment.getDate().get(Calendar.MONTH) + 1) + " " + payment.getDate().get(Calendar.DAY_OF_MONTH));
			
			lblPaymentValue.setText("" + payment.getID());
		}
		else
			clearFields();
	}
	
	public void getPayment(Payment payment)
	{
		if (payment != null)
		{
			Double amount = Double.valueOf(tbAmount.getText());
			TenderType tenderType = TenderType.valueOf(cbFormOfPayment.getText());
			
			if (amount == null)
				amount = Double.valueOf(0);
			
			payment.setDescription(tbDescription.getText());
			payment.setAmount(amount);
			payment.setTenderType(tenderType);
			
		}
	}
	
	public void setModify(boolean enabled)
	{
		tbDescription.setEnabled(enabled);
		tbAmount.setEnabled(enabled);
		cbFormOfPayment.setEnabled(enabled);
	}
}
