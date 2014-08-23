package photobooks.presentation;

import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import photobooks.application.Utility;
import photobooks.listeners.DoubleClickEventListener;
import photobooks.listeners.KeyEventListener;
import photobooks.listeners.SelectionEventListener;
import photobooks.objects.Event;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Label;

public class Day extends Composite {
	
	private Calendar _date;
	private ArrayList<Event> _events = new ArrayList<Event>();
	
	private ListViewer listViewer;
	private List list;
	private Label lblDate, lblDayOfWeek;
	
	public Day(Composite parent, int style, DoubleClickEventListener doubleClickEventListener, SelectionEventListener selectionEventListener, KeyEventListener keyEventListener) {
		super(parent, SWT.NONE);
		init(doubleClickEventListener, selectionEventListener, keyEventListener);
	}
	
	public void init(DoubleClickEventListener doubleClickEventListener, SelectionEventListener selectionEventListener, KeyEventListener keyEventListener) {
		setLayout(new FormLayout());
		
		lblDayOfWeek = new Label(this, SWT.CENTER);
		FormData fd_lblDayOfWeek = new FormData();
		fd_lblDayOfWeek.top = new FormAttachment(0);
		fd_lblDayOfWeek.left = new FormAttachment(0);
		fd_lblDayOfWeek.right = new FormAttachment(100);
		fd_lblDayOfWeek.height = 30;
		lblDayOfWeek.setLayoutData(fd_lblDayOfWeek);
		lblDayOfWeek.setText("New Label");
		
		lblDate = new Label(this, SWT.CENTER);
		FormData fd_lblDate = new FormData();
		fd_lblDate.top = new FormAttachment(lblDayOfWeek, 0, SWT.BOTTOM);
		fd_lblDate.left = new FormAttachment(0);
		fd_lblDate.right = new FormAttachment(100);
		fd_lblDate.height = 30;
		lblDate.setLayoutData(fd_lblDate);
		lblDate.setText("New Label");
		
		listViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		list = listViewer.getList();
		FormData fd_list = new FormData();
		fd_list.top = new FormAttachment(lblDate, 6);
		fd_list.bottom = new FormAttachment(100);
		fd_list.left = new FormAttachment(0);
		fd_list.right = new FormAttachment(100);
		list.setLayoutData(fd_list);
		listViewer.addDoubleClickListener(doubleClickEventListener);
		listViewer.addSelectionChangedListener(selectionEventListener);
		list.addKeyListener(keyEventListener);
		
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setLabelProvider(new ILabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener arg0) {
			}
			
			@Override
			public boolean isLabelProperty(Object arg0, String arg1) {
				return false;
			}
			
			@Override
			public void dispose() {
			}
			
			@Override
			public void addListener(ILabelProviderListener arg0) {
			}
			
			@Override
			public String getText(Object arg0) {
				Event e = (Event)arg0;
				
				return e.getDescription();
			}
			
			@Override
			public Image getImage(Object arg0) {
				return null;
			}
		});
		
		listViewer.setInput(_events.toArray());
	}
	
	public void refresh() {
		listViewer.setInput(_events.toArray());
	}
	
	public ArrayList<Event> getEvents() { return _events; }
	
	public void clearEvents() { _events.clear(); }
	
	public void addEvent(Event event) { _events.add(event); }
	
	public void removeEvent(Event event)
	{
		int index = 0;
		
		for (Event e : _events)
		{
			if (e.getID() == event.getID())
				break;
			
			++index;
		}
		
		if (index < _events.size())
		{
			_events.remove(index);
		}
	}
	
	public Calendar getDate() { return _date; }
	
	public void setDate(Calendar date)
	{
		_date = date;
		
		lblDayOfWeek.setText(Utility.dayOfWeekToString(Utility.dayOfWeek(date) + 1));
		lblDate.setText(Utility.formatDateLong(_date));
	}
}
