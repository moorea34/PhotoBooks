package photobooks.presentation;

import org.eclipse.swt.widgets.Composite;

import photobooks.objects.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;

public class EventEditor extends Composite {
	private Text tbDesc;
	private DatePicker dateTime;
	
	public EventEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		int labelWidth = 100;
		
		Label lblDate = new Label(this, SWT.NONE);
		FormData fd_lblDate = new FormData();
		fd_lblDate.top = new FormAttachment(0, 0);
		fd_lblDate.left = new FormAttachment(0, 0);
		fd_lblDate.width = labelWidth;
		lblDate.setLayoutData(fd_lblDate);
		lblDate.setText("Date: ");
		
		Label lblDescription = new Label(this, SWT.NONE);
		FormData fd_lblDescription = new FormData();
		fd_lblDescription.top = new FormAttachment(lblDate, 10);
		fd_lblDescription.left = new FormAttachment(lblDate, 0, SWT.LEFT);
		fd_lblDescription.width = labelWidth;
		lblDescription.setLayoutData(fd_lblDescription);
		lblDescription.setText("Description:");
		
		dateTime = new DatePicker(this, SWT.NONE);
		FormData fd_dateTime = new FormData();
		fd_dateTime.width = 150;
		fd_dateTime.top = new FormAttachment(lblDate, 0, SWT.TOP);
		fd_dateTime.left = new FormAttachment(lblDescription, 6);
		fd_dateTime.right = new FormAttachment(100, 0);
		dateTime.setLayoutData(fd_dateTime);
		
		tbDesc = new Text(this, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		FormData fd_tbDesc = new FormData();
		fd_tbDesc.top = new FormAttachment(lblDescription, 0, SWT.TOP);
		fd_tbDesc.bottom = new FormAttachment(100, 0);
		fd_tbDesc.left = new FormAttachment(lblDescription, 6);
		fd_tbDesc.right = new FormAttachment(dateTime, 0, SWT.RIGHT);
		tbDesc.setLayoutData(fd_tbDesc);
	}
	
	public void clear() {
		dateTime.clear();
		tbDesc.setText("");
	}
	
	public void setEvent(Event event) {
		clear();
		
		if (event != null) {
			dateTime.setDate(event.getDate());
			tbDesc.setText(event.getDescription());
		}
	}
	
	public void getEvent(Event out) {
		if (out != null) {
			out.setDate(dateTime.getDate());
			out.setDescription(tbDesc.getText());
		}
	}
}
