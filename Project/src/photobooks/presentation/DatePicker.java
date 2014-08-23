package photobooks.presentation;

import java.util.Calendar;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.viewers.ComboViewer;

import photobooks.application.Utility;

public class DatePicker extends Composite {

	public Spinner nudDay, nudYear;
	public ComboViewer comboViewer;
	public Combo cbMonth;
	
	private SimpleContentProposalProvider ppMonths;
	private ContentProposalAdapter paMonths;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DatePicker(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		nudDay = new Spinner(this, SWT.BORDER);
		nudDay.setMaximum(31);
		FormData fd_nudDay = new FormData();
		fd_nudDay.top = new FormAttachment(0, 0);
		fd_nudDay.bottom = new FormAttachment(100, 0);
		fd_nudDay.left = new FormAttachment(0, 0);
		fd_nudDay.right = new FormAttachment(30, 0);
		nudDay.setLayoutData(fd_nudDay);
		
		comboViewer = new ComboViewer(this, SWT.NONE);
		cbMonth = comboViewer.getCombo();
		FormData fd_cbMonth = new FormData();
		fd_cbMonth.top = new FormAttachment(0, 0);
		fd_cbMonth.bottom = new FormAttachment(100, 0);
		fd_cbMonth.left = new FormAttachment(nudDay, 3);
		fd_cbMonth.right = new FormAttachment(70, -3);
		cbMonth.setLayoutData(fd_cbMonth);
		cbMonth.setItems(Utility.getMonths());
		comboViewer.getCombo().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				int index = Utility.stringToMonth(cbMonth.getText());
				
				if (index < 0)
					cbMonth.select(-1);
				else
					cbMonth.select(index);
			}
		});
		
		ppMonths = new SimpleContentProposalProvider(Utility.getMonths());
		ppMonths.setFiltering(true);
		
		paMonths = new ContentProposalAdapter(cbMonth, new ComboContentAdapter(), ppMonths, null, Utility.autoActivationCharacters.toCharArray());
		paMonths.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		
		nudYear = new Spinner(this, SWT.BORDER);
		nudYear.setMaximum(Integer.MAX_VALUE);
		nudYear.setSelection(Calendar.getInstance().get(Calendar.YEAR));
		FormData fd_nudYear = new FormData();
		fd_nudYear.top = new FormAttachment(0, 0);
		fd_nudYear.bottom = new FormAttachment(100, 0);
		fd_nudYear.left = new FormAttachment(comboViewer.getCombo(), 3, SWT.RIGHT);
		fd_nudYear.right = new FormAttachment(100, 0);
		nudYear.setLayoutData(fd_nudYear);
	}
	
	public void clear() {
		comboViewer.getCombo().setText("");
		nudYear.setSelection(0);
		nudDay.setSelection(0);
	}
	
	public Calendar getDate() {
		Calendar date = null;
		int year = nudYear.getSelection(), month = Utility.stringToMonth(comboViewer.getCombo().getText()), day = nudDay.getSelection();
		
		if ((day != 0 || year != 0) && month >= 0)
		{
			date = Calendar.getInstance();
			date.set(year, month, day);
			
			date = (Calendar)date.clone();
		}
		
		return date;
	}
	
	public void setDate(Calendar date) {
		clear();
		
		if (date != null) {
			nudYear.setSelection(date.get(Calendar.YEAR));
			nudDay.setSelection(date.get(Calendar.DAY_OF_MONTH));
			comboViewer.getCombo().setText(Utility.monthToString(date.get(Calendar.MONTH)));
		}
	}
	
	public void setModify(boolean modify) {
		nudYear.setEnabled(modify);
		nudDay.setEnabled(modify);
		comboViewer.getCombo().setEnabled(modify);
	}
}
