package photobooks.presentation;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import photobooks.application.Utility;
import photobooks.business.*;
import photobooks.objects.*;
import photobooks.objects.ITransaction.TransactionType;

class TreeArrayContentProvider implements ITreeContentProvider {
	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}
	
	@Override
	public void dispose() {
	}
	
	@Override
	public boolean hasChildren(Object arg0) {
		return false;
	}
	
	@Override
	public Object getParent(Object arg0) {
		return null;
	}
	
	@Override
	public Object[] getElements(Object arg0) {
		return (Object[])arg0;
	}
	
	@Override
	public Object[] getChildren(Object arg0) {
		return null;
	}
}

public class FinancialReport extends Composite implements IReport {

	private Shell shell;
	private BillManager _billManager;
	private PaymentManager _paymentManager;
	
	private boolean _refreshed = false;
	
	private Label lblDateRange;
	private DatePicker dpFrom, dpTo;
	
	private TreeViewer billTreeViewer;
	private Tree billTree;
	
	private TreeViewer paymentTreeViewer;
	private Tree paymentTree;
	
	private Label lblTotalIncomeValue, lblTotalPstValue, lblTotalGstValue, lblTotalSalesValue;
	
	private int sortColumn = 0, sortColumn2 = 0;
	private int sortDir = 1, sortDir2 = 1;
	
	//Totals
	private double totalSales = 0, totalGst = 0, totalPst = 0, totalIncome = 0;
	
	public FinancialReport(Composite parent, int style, BillManager billManager, PaymentManager paymentManager) {
		super(parent, style);
		shell = parent.getShell();
		
		_billManager = billManager;
		_paymentManager = paymentManager;
		
		init();
	}
	
	public void init() {
		setLayout(new FormLayout());
		
		Button btnSave = new Button(this, SWT.NONE);
		FormData fd_btnSave = new FormData();
		fd_btnSave.height = 30;
		fd_btnSave.width = 150;
		fd_btnSave.bottom = new FormAttachment(100);
		fd_btnSave.left = new FormAttachment(0);
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				save();
			}
		});
		
		Button btnRefresh = new Button(this, SWT.NONE);
		FormData fd_btnRefresh = new FormData();
		fd_btnRefresh.height = 30;
		fd_btnRefresh.width = 150;
		fd_btnRefresh.top = new FormAttachment(btnSave, 0, SWT.TOP);
		fd_btnRefresh.right = new FormAttachment(100);
		btnRefresh.setLayoutData(fd_btnRefresh);
		btnRefresh.setText("Refresh");
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				refresh();
			}
		});
		
		lblDateRange = new Label(this, SWT.NONE);
		FormData fd_lblDateRange = new FormData();
		fd_lblDateRange.top = new FormAttachment(0, 5);
		fd_lblDateRange.left = new FormAttachment(btnSave, 0, SWT.LEFT);
		lblDateRange.setLayoutData(fd_lblDateRange);
		lblDateRange.setText("Sales and income from ");
		
		dpFrom = new DatePicker(this, SWT.NONE);
		FormData fd_dpFrom = new FormData();
		fd_dpFrom.top = new FormAttachment(lblDateRange, -2, SWT.TOP);
		fd_dpFrom.left = new FormAttachment(lblDateRange, 6);
		fd_dpFrom.width = 300;
		dpFrom.setLayoutData(fd_dpFrom);
		Calendar date = Calendar.getInstance();
		date.set(1, 0, 1);
		dpFrom.setDate(date);
		
		Label lblTo = new Label(this, SWT.NONE);
		FormData fd_lblTo = new FormData();
		fd_lblTo.top = new FormAttachment(lblDateRange, 0, SWT.TOP);
		fd_lblTo.left = new FormAttachment(dpFrom, 6);
		lblTo.setLayoutData(fd_lblTo);
		lblTo.setText(" to ");
		
		dpTo = new DatePicker(this, SWT.NONE);
		FormData fd_dpTo = new FormData();
		fd_dpTo.top = new FormAttachment(dpFrom, 0, SWT.TOP);
		fd_dpTo.left = new FormAttachment(lblTo, 6);
		fd_dpTo.width = 300;
		dpTo.setLayoutData(fd_dpTo);
		date = Calendar.getInstance();
		date.set(99999, 11, 31);
		dpTo.setDate(date);
		
		Label lblTotalIncome = new Label(this, SWT.NONE);
		FormData fd_lblTotalIncome = new FormData();
		fd_lblTotalIncome.left = new FormAttachment(btnSave, 0, SWT.LEFT);
		fd_lblTotalIncome.bottom = new FormAttachment(btnSave, -10, SWT.TOP);
		lblTotalIncome.setLayoutData(fd_lblTotalIncome);
		lblTotalIncome.setText("Total income: ");
		
		Label lblTotalPst = new Label(this, SWT.NONE);
		FormData fd_lblTotalPst = new FormData();
		fd_lblTotalPst.left = new FormAttachment(btnSave, 0, SWT.LEFT);
		fd_lblTotalPst.bottom = new FormAttachment(lblTotalIncome, -10, SWT.TOP);
		lblTotalPst.setLayoutData(fd_lblTotalPst);
		lblTotalPst.setText("Total pst: ");
		
		Label lblTotalGst = new Label(this, SWT.NONE);
		FormData fd_lblTotalGst = new FormData();
		fd_lblTotalGst.left = new FormAttachment(btnSave, 0, SWT.LEFT);
		fd_lblTotalGst.bottom = new FormAttachment(lblTotalPst, -10, SWT.TOP);
		lblTotalGst.setLayoutData(fd_lblTotalGst);
		lblTotalGst.setText("Total gst: ");
		
		Label lblTotalSales = new Label(this, SWT.NONE);
		FormData fd_lblTotalSales = new FormData();
		fd_lblTotalSales.left = new FormAttachment(btnSave, 0, SWT.LEFT);
		fd_lblTotalSales.bottom = new FormAttachment(lblTotalGst, -10, SWT.TOP);
		lblTotalSales.setLayoutData(fd_lblTotalSales);
		lblTotalSales.setText("Total sales: ");
		
		lblTotalIncomeValue = new Label(this, SWT.NONE);
		lblTotalIncomeValue.setAlignment(SWT.RIGHT);
		FormData fd_lblTotalIncomeValue = new FormData();
		fd_lblTotalIncomeValue.width = 200;
		fd_lblTotalIncomeValue.bottom = new FormAttachment(lblTotalIncome, 0, SWT.BOTTOM);
		fd_lblTotalIncomeValue.left = new FormAttachment(lblTotalIncome, 6);
		lblTotalIncomeValue.setLayoutData(fd_lblTotalIncomeValue);
		lblTotalIncomeValue.setText("0.00");
		
		lblTotalPstValue = new Label(this, SWT.NONE);
		lblTotalPstValue.setText("0.00");
		lblTotalPstValue.setAlignment(SWT.RIGHT);
		FormData fd_lblTotalPstValue = new FormData();
		fd_lblTotalPstValue.left = new FormAttachment(lblTotalIncomeValue, 0, SWT.LEFT);
		fd_lblTotalPstValue.right = new FormAttachment(lblTotalIncomeValue, 0, SWT.RIGHT);
		fd_lblTotalPstValue.bottom = new FormAttachment(lblTotalPst, 0, SWT.BOTTOM);
		lblTotalPstValue.setLayoutData(fd_lblTotalPstValue);
		
		lblTotalGstValue = new Label(this, SWT.NONE);
		lblTotalGstValue.setText("0.00");
		lblTotalGstValue.setAlignment(SWT.RIGHT);
		FormData fd_lblTotalGstValue = new FormData();
		fd_lblTotalGstValue.left = new FormAttachment(lblTotalIncomeValue, 0, SWT.LEFT);
		fd_lblTotalGstValue.right = new FormAttachment(lblTotalIncomeValue, 0, SWT.RIGHT);
		fd_lblTotalGstValue.bottom = new FormAttachment(lblTotalGst, 0, SWT.BOTTOM);
		lblTotalGstValue.setLayoutData(fd_lblTotalGstValue);
		
		lblTotalSalesValue = new Label(this, SWT.NONE);
		lblTotalSalesValue.setText("0.00");
		lblTotalSalesValue.setAlignment(SWT.RIGHT);
		FormData fd_lblTotalSalesValue = new FormData();
		fd_lblTotalSalesValue.left = new FormAttachment(lblTotalIncomeValue, 0, SWT.LEFT);
		fd_lblTotalSalesValue.right = new FormAttachment(lblTotalIncomeValue, 0, SWT.RIGHT);
		fd_lblTotalSalesValue.bottom = new FormAttachment(lblTotalSales, 0, SWT.BOTTOM);
		lblTotalSalesValue.setLayoutData(fd_lblTotalSalesValue);
		
		billTreeViewer = new TreeViewer(this, SWT.BORDER);
		billTree = billTreeViewer.getTree();
		billTree.setLinesVisible(true);
		billTree.setHeaderVisible(true);
		FormData fd_billTree = new FormData();
		fd_billTree.bottom = new FormAttachment(50, -10 - (4 * 15));
		fd_billTree.top = new FormAttachment(lblDateRange, 10);
		fd_billTree.left = new FormAttachment(btnSave, 0, SWT.LEFT);
		fd_billTree.right = new FormAttachment(btnRefresh, 0, SWT.RIGHT);
		billTree.setLayoutData(fd_billTree);
		billTreeViewer.setSorter(new ViewerSorter() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				return compareBills((Bill)e1, (Bill)e2);
			}
		});
		billTreeViewer.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				return compareBills((Bill)e1, (Bill)e2);
			}
		});
		billTreeViewer.setContentProvider(new TreeArrayContentProvider());
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(billTreeViewer, SWT.NONE);
		TreeColumn trclmnInvoice = treeViewerColumn.getColumn();
		trclmnInvoice.setAlignment(SWT.CENTER);
		trclmnInvoice.setWidth(70);
		trclmnInvoice.setText("Invoice");
		treeViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return String.valueOf(((Bill)element).getID());
			}
		});
		treeViewerColumn.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (sortColumn == 0) sortDir *= -1;
				else sortDir = 1;
				
				sortColumn = 0;
				billTreeViewer.refresh();
			}
		});
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(billTreeViewer, SWT.NONE);
		TreeColumn trclmnDescription = treeViewerColumn_1.getColumn();
		trclmnDescription.setWidth(360);
		trclmnDescription.setText("Description");
		treeViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((Bill)element).getDescription();
			}
		});
		treeViewerColumn_1.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (sortColumn == 1) sortDir *= -1;
				else sortDir = 1;
				
				sortColumn = 1;
				billTreeViewer.refresh();
			}
		});
		
		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(billTreeViewer, SWT.NONE);
		TreeColumn trclmnSales = treeViewerColumn_2.getColumn();
		trclmnSales.setAlignment(SWT.RIGHT);
		trclmnSales.setWidth(100);
		trclmnSales.setText("Sales");
		treeViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return Utility.formatMoneyExport(((Bill)element).subtotal());
			}
		});
		treeViewerColumn_2.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (sortColumn == 2) sortDir *= -1;
				else sortDir = 1;
				
				sortColumn = 2;
				billTreeViewer.refresh();
			}
		});
		
		TreeViewerColumn treeViewerColumn_4 = new TreeViewerColumn(billTreeViewer, SWT.NONE);
		TreeColumn trclmnGst = treeViewerColumn_4.getColumn();
		trclmnGst.setAlignment(SWT.RIGHT);
		trclmnGst.setWidth(80);
		trclmnGst.setText("Gst");
		treeViewerColumn_4.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return Utility.formatMoneyExport(((Bill)element).getGst());
			}
		});
		treeViewerColumn_4.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (sortColumn == 4) sortDir *= -1;
				else sortDir = 1;
				
				sortColumn = 4;
				billTreeViewer.refresh();
			}
		});
		
		TreeViewerColumn treeViewerColumn_5 = new TreeViewerColumn(billTreeViewer, SWT.NONE);
		TreeColumn trclmnPst = treeViewerColumn_5.getColumn();
		trclmnPst.setAlignment(SWT.RIGHT);
		trclmnPst.setWidth(80);
		trclmnPst.setText("Pst");
		treeViewerColumn_5.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return Utility.formatMoneyExport(((Bill)element).getPst());
			}
		});
		treeViewerColumn_5.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (sortColumn == 5) sortDir *= -1;
				else sortDir = 1;
				
				sortColumn = 5;
				billTreeViewer.refresh();
			}
		});
		
		paymentTreeViewer = new TreeViewer(this, SWT.BORDER);
		paymentTree = paymentTreeViewer.getTree();
		paymentTree.setLinesVisible(true);
		paymentTree.setHeaderVisible(true);
		FormData fd_paymentTree = new FormData();
		fd_paymentTree.top = new FormAttachment(billTree, 10);
		fd_paymentTree.left = new FormAttachment(billTree, 0, SWT.LEFT);
		fd_paymentTree.right = new FormAttachment(billTree, 0, SWT.RIGHT);
		fd_paymentTree.bottom = new FormAttachment(lblTotalSales, -10);
		paymentTree.setLayoutData(fd_paymentTree);
		paymentTreeViewer.setContentProvider(new TreeArrayContentProvider());
		paymentTreeViewer.setSorter(new ViewerSorter() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				return comparePayments((Payment)e1, (Payment)e2);
			}
		});
		paymentTreeViewer.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				return comparePayments((Payment)e1, (Payment)e2);
			}
		});
		
		TreeViewerColumn treeViewerColumn_6 = new TreeViewerColumn(paymentTreeViewer, SWT.NONE);
		TreeColumn trclmnPayment = treeViewerColumn_6.getColumn();
		trclmnPayment.setAlignment(SWT.CENTER);
		trclmnPayment.setWidth(80);
		trclmnPayment.setText("Payment");
		treeViewerColumn_6.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return String.valueOf(((Payment)element).getID());
			}
		});
		treeViewerColumn_6.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (sortColumn2 == 0) sortDir2 *= -1;
				else sortDir2 = 1;
				
				sortColumn2 = 0;
				paymentTreeViewer.refresh();
			}
		});
		
		TreeViewerColumn treeViewerColumn_7 = new TreeViewerColumn(paymentTreeViewer, SWT.NONE);
		TreeColumn trclmnDescription_1 = treeViewerColumn_7.getColumn();
		trclmnDescription_1.setWidth(360);
		trclmnDescription_1.setText("Description");
		treeViewerColumn_7.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return String.valueOf(((Payment)element).getDescription());
			}
		});
		treeViewerColumn_7.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (sortColumn2 == 1) sortDir2 *= -1;
				else sortDir2 = 1;
				
				sortColumn2 = 1;
				paymentTreeViewer.refresh();
			}
		});
		
		TreeViewerColumn treeViewerColumn_8 = new TreeViewerColumn(paymentTreeViewer, SWT.NONE);
		TreeColumn trclmnAmount = treeViewerColumn_8.getColumn();
		trclmnAmount.setAlignment(SWT.RIGHT);
		trclmnAmount.setWidth(100);
		trclmnAmount.setText("Amount");
		treeViewerColumn_8.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return Utility.formatMoneyExport(((Payment)element).total());
			}
		});
		treeViewerColumn_8.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (sortColumn2 == 2) sortDir2 *= -1;
				else sortDir2 = 1;
				
				sortColumn2 = 2;
				paymentTreeViewer.refresh();
			}
		});
	}
	
	private int clamp(int value) {
		if (value > 0) return 1;
		else if (value < 0) return -1;
		else return 0;
	}
	
	private int compareBills(Bill b1, Bill b2) {
		int result = 0;
		
		switch (sortColumn)
		{
			case 0:
				result = clamp(Integer.compare(b1.getID(), b2.getID()));
				break;
			case 1:
				result = b1.getDescription().compareTo(b2.getDescription());
				break;
			case 2:
				result = clamp(Double.compare(b1.subtotal(), b2.subtotal()));
				break;
			/*case 3:
				result = clamp(Double.compare(b1.totalPayments(), b2.totalPayments()));
				break;*/
			case 4:
				result = clamp(Double.compare(b1.getGst(), b2.getGst()));
				break;
			case 5:
				result = clamp(Double.compare(b1.getPst(), b2.getPst()));
				break;
		}
		
		return result * sortDir;
	}
	
	private int comparePayments(Payment p1, Payment p2) {
		int result = 0;
		
		switch (sortColumn2)
		{
			case 0:
				result = clamp(Integer.compare(p1.getID(), p2.getID()));
				break;
			case 1:
				result = p1.getDescription().compareTo(p2.getDescription());
				break;
			case 2:
				result = clamp(Double.compare(p1.total(), p2.total()));
				break;
		}
		
		return result * sortDir2;
	}
	
	private boolean inRange(Calendar date, Calendar from, Calendar to) {
		boolean result = true;
		
		if (date != null) {
			if (from != null) {
				if (date.get(Calendar.YEAR) < from.get(Calendar.YEAR))
					result = false;
				else if (date.get(Calendar.YEAR) == from.get(Calendar.YEAR))
				{
					if (date.get(Calendar.MONTH) < from.get(Calendar.MONTH))
						result = false;
					else if (date.get(Calendar.MONTH) == from.get(Calendar.MONTH))
					{
						if (date.get(Calendar.DAY_OF_MONTH) < from.get(Calendar.DAY_OF_MONTH))
							result = false;
					}
				}
			}
			
			if (to != null) {
				if (date.get(Calendar.YEAR) > to.get(Calendar.YEAR))
					result = false;
				else if (date.get(Calendar.YEAR) == to.get(Calendar.YEAR))
				{
					if (date.get(Calendar.MONTH) > to.get(Calendar.MONTH))
						result = false;
					else if (date.get(Calendar.MONTH) == to.get(Calendar.MONTH))
					{
						if (date.get(Calendar.DAY_OF_MONTH) > to.get(Calendar.DAY_OF_MONTH))
							result = false;
					}
				}
			}
		}
		
		return result;
	}
	
	public void refresh() {
		_refreshed = true;
		
		totalSales = 0;
		totalGst = 0;
		totalPst = 0;
		totalIncome = 0;
		
		Calendar from = dpFrom.getDate(), to = dpTo.getDate();
		System.out.println("Refreshing finanical report from " + Utility.formatDateLong(from) + " to " + Utility.formatDateLong(to));
		
		Collection<Bill> billCollection = _billManager.getAll();
		ArrayList<ITransaction> bills = new ArrayList<ITransaction>();
		
		for (Bill bill : billCollection) {
			if (bill.getType() == TransactionType.Invoice && inRange(bill.getDate(), from, to))
			{
				bills.add(bill);
				
				totalSales += bill.subtotal();
				totalGst += bill.getGst();
				totalPst += bill.getPst();
			}
		}
		
		billTreeViewer.setInput(bills.toArray());
		
		Collection<Payment> paymentCollection = _paymentManager.getAll();
		ArrayList<ITransaction> payments = new ArrayList<ITransaction>();
		
		for (Payment payment : paymentCollection) {
			if (inRange(payment.getDate(), from, to))
			{
				payments.add(payment);
				
				totalIncome += payment.total();
			}
		}
		
		paymentTreeViewer.setInput(payments.toArray());
		
		lblTotalSalesValue.setText(Utility.formatMoneyExport(totalSales));
		lblTotalGstValue.setText(Utility.formatMoneyExport(totalGst));
		lblTotalPstValue.setText(Utility.formatMoneyExport(totalPst));
		lblTotalIncomeValue.setText(Utility.formatMoneyExport(totalIncome));
	}
	
	public void refreshIfNull() {
		if (!_refreshed)
			refresh();
	}
	
	public void save() {
		final String billRowFormatString = "%-11s %-30s %15s %10s %10s%n";
		final String paymentRowFormatString = "%-11s %-52s %15s%n";
		final String totalsFormatString = "%-15s %25s";
		
		String loc = Utility.getSaveLocTXT(shell, "Financial Report");
		String start = "beginning of time", end = "end of time";
		Calendar startDate = dpFrom.getDate(), endDate = dpTo.getDate();
		String regex = "\\r?\\n|\\r";
		
		if (startDate != null) start = Utility.formatDateLong(startDate);
		if (endDate != null) end = Utility.formatDateLong(endDate);
		
		if (loc != null && loc.length() > 0) {
			try
			{
				FileWriter fileWriter = new FileWriter(loc);
				
				//Write title
				fileWriter.write(String.format("Sales and income from %s to %s:", start, end) + String.format("%n%n%n"));
				
				//Write bill table header
				fileWriter.write(String.format(billRowFormatString + "%n", "Invoice", "Description", "Sales", "Gst", "Pst"));
				
				//Write bill table data
				for (TreeItem item : billTree.getItems()) {
					String[] split = item.getText(1).split(regex);
					
					if (split[0].length() > 29)
					{
						String[] split2 = new String[] { split[0].substring(0, 30), split[0].substring(30) };
						
						fileWriter.write(String.format("%n" + billRowFormatString, item.getText(0), split2[0], item.getText(2), item.getText(3), item.getText(4)));
						split[0] = split2[1];
						
						while (split[0].length() > 29)
						{
							split2 = new String[] { split[0].substring(0, 30), split[0].substring(30) };
							fileWriter.write(String.format(billRowFormatString, "", split2[0], "", "", ""));
							split[0] = split2[1];
						}
						
						if (split[0].length() > 0)
							fileWriter.write(String.format(billRowFormatString, "", split[0], "", "", ""));
					}
					else
						fileWriter.write(String.format("%n" + billRowFormatString, item.getText(0), split[0], item.getText(2), item.getText(3), item.getText(4)));
					
					for (int i = 1; i < split.length; ++i) {
						if (split[i].length() == 0)
							fileWriter.write(String.format("%n"));
						
						while (split[i].length() > 29)
						{
							String[] split2 = new String[] { split[i].substring(0, 30), split[i].substring(30) };
							
							fileWriter.write(String.format(billRowFormatString, "", split2[0], "", "", ""));
							split[i] = split2[1];
						}

						if (split[i].length() > 0)
							fileWriter.write(String.format(billRowFormatString, "", split[i], "", "", ""));
					}
				}
				
				//Spacing between tables
				fileWriter.write(String.format("%n%n%n%n"));
				
				//Payment table header
				fileWriter.write(String.format(paymentRowFormatString + "%n", "Payment", "Description", "Amount"));
				
				//Payment table data
				for (TreeItem item : paymentTree.getItems()) {
					String[] split = item.getText(1).split(regex);
					
					if (split[0].length() > 51)
					{
						String[] split2 = new String[] { split[0].substring(0, 52), split[0].substring(52) };
						
						fileWriter.write(String.format("%n" + paymentRowFormatString, item.getText(0), split2[0], item.getText(2)));
						split[0] = split2[1];
						
						while (split[0].length() > 51)
						{
							split2 = new String[] { split[0].substring(0, 52), split[0].substring(52) };
							fileWriter.write(String.format(paymentRowFormatString, "", split2[0], ""));
							split[0] = split2[1];
						}
						
						if (split[0].length() > 0)
							fileWriter.write(String.format(paymentRowFormatString, "", split[0], ""));
					}
					else
						fileWriter.write(String.format("%n" + paymentRowFormatString, item.getText(0), split[0], item.getText(2)));
					
					for (int i = 1; i < split.length; ++i) {
						if (split[i].length() == 0)
							fileWriter.write(String.format("%n"));
						
						while (split[i].length() > 51)
						{
							String[] split2 = new String[] { split[i].substring(0, 52), split[i].substring(52) };
							
							fileWriter.write(String.format(paymentRowFormatString, "", split2[0], ""));
							split[i] = split2[1];
						}

						if (split[i].length() > 0)
							fileWriter.write(String.format(paymentRowFormatString, "", split[i], ""));
					}
				}
				
				//Spacing after tables
				fileWriter.write(String.format("%n%n%n%n"));
				
				//Write out totals
				fileWriter.write(String.format(totalsFormatString + "%n%n", "Total sales:", Utility.formatMoneyExport(totalSales)));
				fileWriter.write(String.format(totalsFormatString + "%n%n", "Total gst:", Utility.formatMoneyExport(totalGst)));
				fileWriter.write(String.format(totalsFormatString + "%n%n", "Total pst:", Utility.formatMoneyExport(totalPst)));
				fileWriter.write(String.format(totalsFormatString, "Total income:", Utility.formatMoneyExport(totalIncome)));
				
				fileWriter.close();
			}
			catch (Exception ex)
			{
				Utility.showErrorMessage(shell, "Error saving report: " + ex.toString());
			}
		}
	}
}
