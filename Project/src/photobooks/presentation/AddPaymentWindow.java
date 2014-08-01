package photobooks.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;

import photobooks.application.Utility;
import photobooks.objects.Payment;

public class AddPaymentWindow extends Dialog {

	protected Object result = null;
	protected Shell shlAddPayment;
	
	private PaymentEditor _paymentEditor;
	private Payment _payment;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AddPaymentWindow(Shell parent, int style, Payment payment) {
		super(parent, style);
		setText("SWT Dialog");
		
		_payment = payment;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		
		shlAddPayment.open();
		shlAddPayment.layout();
		
		Display display = getParent().getDisplay();
		
		while (!shlAddPayment.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlAddPayment = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlAddPayment.setSize(480, 310);
		shlAddPayment.setMinimumSize(700, 410);
		shlAddPayment.setText("Add Payment");
		shlAddPayment.setLayout(new FormLayout());

		_paymentEditor = new PaymentEditor(shlAddPayment, SWT.NONE);
		FormData fd__paymentEditor = new FormData();
		fd__paymentEditor.right = new FormAttachment(100, -10);
		fd__paymentEditor.top = new FormAttachment(0, 10);
		fd__paymentEditor.left = new FormAttachment(0, 10);
		_paymentEditor.setLayoutData(fd__paymentEditor);
		
		Button btnCancel = new Button(shlAddPayment, SWT.NONE);
		fd__paymentEditor.bottom = new FormAttachment(btnCancel, -6);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.right = new FormAttachment(100, -10);
		fd_btnCancel.width = 120;
		fd_btnCancel.height = 30;
		fd_btnCancel.bottom = new FormAttachment(100, -10);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shlAddPayment.dispose();
			}
		});
		
		Button btnSave = new Button(shlAddPayment, SWT.NONE);
		FormData fd_btnSave = new FormData();
		fd_btnSave.left = new FormAttachment(0, 10);
		fd_btnSave.top = new FormAttachment(btnCancel, 0, SWT.TOP);
		fd_btnSave.width = 120;
		fd_btnSave.height = 30;
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				_paymentEditor.getPayment(_payment);
				result = _payment;
				
				shlAddPayment.dispose();
			}
		});
		
		if (_payment != null)
		{
			_paymentEditor.setPayment(_payment);
		}
		
		Utility.setFont(shlAddPayment);
		Utility.centerScreen(shlAddPayment);
	}
}
