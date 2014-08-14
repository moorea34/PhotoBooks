package photobooks.presentation;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;

import photobooks.application.Globals;
import photobooks.application.Utility;

import org.eclipse.swt.widgets.Button;

public class SettingsEditor extends Composite {
	private Text tbGst;
	private Text tbPst;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SettingsEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		Label lblGst = new Label(this, SWT.NONE);
		FormData fd_lblGst = new FormData();
		fd_lblGst.top = new FormAttachment(0, 8);
		fd_lblGst.left = new FormAttachment(0, 6);
		lblGst.setLayoutData(fd_lblGst);
		lblGst.setText("Gst:");
		
		Label lblPst = new Label(this, SWT.NONE);
		FormData fd_lblPst = new FormData();
		fd_lblPst.top = new FormAttachment(lblGst, 10);
		fd_lblPst.left = new FormAttachment(lblGst, 0, SWT.LEFT);
		lblPst.setLayoutData(fd_lblPst);
		lblPst.setText("Pst:");
		
		tbGst = new Text(this, SWT.BORDER);
		FormData fd_tbGst = new FormData();
		fd_tbGst.width = 130;
		fd_tbGst.top = new FormAttachment(0, 6);
		fd_tbGst.left = new FormAttachment(lblGst, 6);
		tbGst.setLayoutData(fd_tbGst);
		
		tbPst = new Text(this, SWT.BORDER);
		FormData fd_tbPst = new FormData();
		fd_tbPst.left = new FormAttachment(tbGst, 0, SWT.LEFT);
		fd_tbPst.right = new FormAttachment(tbGst, 0, SWT.RIGHT);
		fd_tbPst.top = new FormAttachment(tbGst, 4);
		tbPst.setLayoutData(fd_tbPst);
		
		Button btnReload = new Button(this, SWT.NONE);
		FormData fd_btnReload = new FormData();
		fd_btnReload.width = 150;
		fd_btnReload.height = 30;
		fd_btnReload.bottom = new FormAttachment(100, -6);
		fd_btnReload.right = new FormAttachment(100, -6);
		btnReload.setLayoutData(fd_btnReload);
		btnReload.setText("Reload");
		btnReload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				reload();
			}
		});
		
		Button btnSave = new Button(this, SWT.NONE);
		FormData fd_btnSave = new FormData();
		fd_btnSave.width = 150;
		fd_btnSave.height = 30;
		fd_btnSave.bottom = new FormAttachment(btnReload, 0, SWT.BOTTOM);
		fd_btnSave.left = new FormAttachment(0, 6);
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				save();
				reload();
			}
		});
		
		reload();
	}
	
	public void reload() {
		tbGst.setText(String.valueOf(Globals.getGst()));
		tbPst.setText(String.valueOf(Globals.getPst()));
	}
	
	public void save() {
		double gst = Globals.getGst();
		double pst = Globals.getPst();
		Shell shell = getParent().getShell();
		
		try
		{
			gst = Double.parseDouble(tbGst.getText());
		}
		catch (Exception ex)
		{
			Utility.showErrorMessage(shell, "Invalid gst value!");
			return;
		}
		
		try
		{
			pst = Double.parseDouble(tbPst.getText());
		}
		catch (Exception ex)
		{
			Utility.showErrorMessage(shell, "Invalid pst value!");
			return;
		}
		
		Globals.setGst(gst);
		Globals.setPst(pst);
		
		Globals.saveSettings();
	}
}
