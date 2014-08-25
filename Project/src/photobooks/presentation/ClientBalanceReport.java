package photobooks.presentation;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import photobooks.application.Utility;
import photobooks.business.ClientManager;
import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.jface.viewers.TreeViewerColumn;

public class ClientBalanceReport extends Composite implements IReport {
	
	private Shell shell;
	private ClientManager _clientManager;
	
	private boolean _refreshed = false;
	
	private Label lblClientBalanceAs;
	private TreeViewer clientTreeViewer;
	private Tree clientTree;
	
	private int sortColumn = 0;
	private int sortDir = 1;
	
	public ClientBalanceReport(Composite parent, int style, ClientManager clientManager) {
		super(parent, style);
		shell = parent.getShell();
		
		_clientManager = clientManager;
		
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
		
		lblClientBalanceAs = new Label(this, SWT.NONE);
		FormData fd_lblClientBalanceAs = new FormData();
		fd_lblClientBalanceAs.top = new FormAttachment(0, 5);
		fd_lblClientBalanceAs.left = new FormAttachment(btnSave, 0, SWT.LEFT);
		lblClientBalanceAs.setLayoutData(fd_lblClientBalanceAs);
		lblClientBalanceAs.setText("Client balances as of " + Utility.formatDateLong(Calendar.getInstance()) + ":");
		
		clientTreeViewer = new TreeViewer(this, SWT.BORDER);
		clientTree = clientTreeViewer.getTree();
		clientTree.setLinesVisible(true);
		clientTree.setHeaderVisible(true);
		FormData fd_clientTree = new FormData();
		fd_clientTree.top = new FormAttachment(lblClientBalanceAs, 10);
		fd_clientTree.bottom = new FormAttachment(btnSave, -10, SWT.TOP);
		fd_clientTree.left = new FormAttachment(btnSave, 0, SWT.LEFT);
		fd_clientTree.right = new FormAttachment(btnRefresh, 0, SWT.RIGHT);
		clientTree.setLayoutData(fd_clientTree);
		clientTreeViewer.setSorter(new ViewerSorter() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				return compareClients((Client)e1, (Client)e2);
			}
		});
		clientTreeViewer.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				return compareClients((Client)e1, (Client)e2);
			}
		});
		clientTreeViewer.setContentProvider(new ITreeContentProvider() {
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
		});
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(clientTreeViewer, SWT.NONE);
		TreeColumn trclmnClient = treeViewerColumn.getColumn();
		trclmnClient.setWidth(245);
		trclmnClient.setText("Client");
		treeViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((Client)element).getFullName();
			}
		});
		treeViewerColumn.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (sortColumn == 0) sortDir *= -1;
				else sortDir = 1;
				
				sortColumn = 0;
				clientTreeViewer.refresh();
			}
		});
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(clientTreeViewer, SWT.NONE);
		TreeColumn trclmnPhoneNumber = treeViewerColumn_1.getColumn();
		trclmnPhoneNumber.setWidth(145);
		trclmnPhoneNumber.setText("Phone number");
		treeViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				Client client = (Client)element;
				PhoneNumber number = client.getFirstNumber();
				
				if (number == null) return "";
				else return Utility.formatPhoneNumber(number.getNumber());
			}
		});
		treeViewerColumn_1.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (sortColumn == 1) sortDir *= -1;
				else sortDir = 1;
				
				sortColumn = 1;
				clientTreeViewer.refresh();
			}
		});
		
		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(clientTreeViewer, SWT.NONE);
		TreeColumn trclmnBalance = treeViewerColumn_2.getColumn();
		trclmnBalance.setWidth(125);
		trclmnBalance.setText("Balance");
		treeViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return Utility.formatMoneyExport(((Client)element).getAccountBalance());
			}
		});
		treeViewerColumn_2.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (sortColumn == 2) sortDir *= -1;
				else sortDir = 1;
				
				sortColumn = 2;
				clientTreeViewer.refresh();
			}
		});
	}
	
	private int compareClients(Client c1, Client c2) {
		int result = 0;
		
		if (sortColumn == 0)
			result = c1.getFullName().compareTo(c2.getFullName());
		else if (sortColumn == 1)
		{
			PhoneNumber p1 = c1.getFirstNumber(), p2 = c2.getFirstNumber();
			String s1 = "", s2 = "";
			
			if (p1 != null) s1 = Utility.formatPhoneNumber(p1.getNumber());
			if (p2 != null) s2 = Utility.formatPhoneNumber(p2.getNumber());
			
			result = s1.compareTo(s2);
		}
		else if (sortColumn == 2)
		{
			double t1 = c1.getAccountBalance(), t2 = c2.getAccountBalance();
			
			if (t1 < t2) result = -1;
			else if (t1 > t2) result = 1;
		}
		
		return result * sortDir;
	}
	
	public void refresh() {
		_refreshed = true;
		
		ArrayList<Client> clients = _clientManager.getClientList();
		clientTreeViewer.setInput(clients.toArray());
	}
	
	public void refreshIfNull() {
		if (!_refreshed)
			refresh();
	}
	
	public void save() {
		String loc = Utility.getSaveLocTXT(shell, "Client Balance Report");
		final String rowFormatString = "%-40s %-20s %15s%n";
		
		if (loc != null && loc.length() > 0) {
			try
			{
				FileWriter fileWriter = new FileWriter(loc);
				
				//Write title
				fileWriter.write(lblClientBalanceAs.getText() + String.format("%n%n%n"));
				
				//Write table header
				fileWriter.write(String.format(rowFormatString + "%n", "Client Name", "Phone Number", "Account Balance"));
				
				//Write table data
				for (TreeItem item : clientTree.getItems()) {
					fileWriter.write(String.format("%n" + rowFormatString, item.getText(0), item.getText(1), item.getText(2)));
				}
				
				fileWriter.close();
			}
			catch (Exception ex)
			{
				Utility.showErrorMessage(shell, "Error saving report: " + ex.toString());
			}
		}
	}
}
