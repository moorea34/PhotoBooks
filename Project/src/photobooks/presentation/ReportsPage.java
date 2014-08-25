package photobooks.presentation;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import photobooks.business.BillManager;
import photobooks.business.ClientManager;
import photobooks.business.PaymentManager;

import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.TabItem;

public class ReportsPage extends Composite {

	private ClientManager _clientManager;
	private BillManager _billManager;
	private PaymentManager _paymentManager;
	
	private ClientBalanceReport _clientBalanceReport;
	private FinancialReport _financialReport;
	
	public ReportsPage(Composite parent, int style, ClientManager clientManager, BillManager billManager, PaymentManager paymentManager) {
		super(parent, style);
		
		_clientManager = clientManager;
		_billManager = billManager;
		_paymentManager = paymentManager;
		
		init();
	}
	
	public void init() {
		setLayout(new FormLayout());
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.top = new FormAttachment(0, 0);
		fd_tabFolder.bottom = new FormAttachment(100, 0);
		fd_tabFolder.left = new FormAttachment(0, 0);
		fd_tabFolder.right = new FormAttachment(100, 0);
		tabFolder.setLayoutData(fd_tabFolder);
		
		_clientBalanceReport = new ClientBalanceReport(tabFolder, SWT.NONE, _clientManager);
		_financialReport = new FinancialReport(tabFolder, SWT.NONE, _billManager, _paymentManager);
		
		TabItem tbtmClientbalance = new TabItem(tabFolder, SWT.NONE);
		tbtmClientbalance.setText("Client Balance");
		tbtmClientbalance.setControl(_clientBalanceReport);
		
		TabItem tbtmFinancialReport = new TabItem(tabFolder, SWT.NONE);
		tbtmFinancialReport.setText("Financial Report");
		tbtmFinancialReport.setControl(_financialReport);
	}
}
