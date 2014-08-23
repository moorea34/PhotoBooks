package photobooks.presentation;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import photobooks.application.Utility;
import photobooks.objects.Event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class EditEventWindow extends Dialog {

	protected Object result = null;
	protected Shell shell;
	
	private EventEditor _editor;
	private Event _event;

	public EditEventWindow(Shell parent, int style, Event event) {
		super(parent, style);
		setText("Event Editor");
		
		_event = event;
		
		if (_event == null)
			_event = new Event(null, "");
	}

	public Object open() {
		createContents();
		
		shell.open();
		shell.layout();
		
		Display display = getParent().getDisplay();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		return result;
	}

	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE | SWT.APPLICATION_MODAL);
		shell.setMinimumSize(700, 300);
		shell.setSize(490, 344);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		Button btnSave = new Button(shell, SWT.NONE);
		FormData fd_btnSave = new FormData();
		fd_btnSave.height = 30;
		fd_btnSave.width = 120;
		fd_btnSave.bottom = new FormAttachment(100, -10);
		fd_btnSave.left = new FormAttachment(0, 10);
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				_editor.getEvent(_event);
				
				result = _event;
				
				shell.close();
			}
		});
		
		Button btnCancel = new Button(shell, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.height = 30;
		fd_btnCancel.width = 120;
		fd_btnCancel.bottom = new FormAttachment(btnSave, 0, SWT.BOTTOM);
		fd_btnCancel.right = new FormAttachment(100, -10);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		
		_editor = new EventEditor(shell, SWT.NONE);
		FormData fd_editor = new FormData();
		fd_editor.top = new FormAttachment(0, 10);
		fd_editor.bottom = new FormAttachment(btnSave, -6, SWT.TOP);
		fd_editor.left = new FormAttachment(btnSave, 0, SWT.LEFT);
		fd_editor.right = new FormAttachment(btnCancel, 0, SWT.RIGHT);
		_editor.setLayoutData(fd_editor);
		_editor.setEvent(_event);
		
		Utility.setFont(shell);
		Utility.centerScreen(shell);
	}

}
