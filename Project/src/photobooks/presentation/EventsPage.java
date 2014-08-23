package photobooks.presentation;

import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.DateTime;

import photobooks.application.Utility;
import photobooks.business.EventManager;
import photobooks.listeners.DoubleClickEventListener;
import photobooks.listeners.KeyEventListener;
import photobooks.listeners.SelectionEventListener;
import photobooks.objects.Event;

import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;

public class EventsPage extends Composite {
	private final static int DAYSINMONTH = 31;
	private final static int NUMBEROFDAYS = DAYSINMONTH * 2;
	
	private Shell shell;
	
	private DateTime dtWeek;
	private Week week;
	
	private ListViewer eventListViewer;
	private List eventList;
	
	private Calendar _startDate, _endDate;
	private ArrayList<Event> _events;
	
	public Event selectedEvent = null;
	
	private EventManager _eventManager;
	
	private DoubleClickEventListener _doubleClickEventListener;
	private SelectionEventListener _selectionEventListener;
	private KeyEventListener _keyEventListener;
	private Label lblNextDays;
	
	public EventsPage(Composite parent, int style, EventManager eventManager) {
		super(parent, style);
		
		shell = parent.getShell();
		_eventManager = eventManager;
		
		init();
		refresh();
	}
	
	public void init() {
		_doubleClickEventListener = new DoubleClickEventListener(this);
		_selectionEventListener = new SelectionEventListener(this);
		_keyEventListener = new KeyEventListener(this);
		
		setLayout(new FormLayout());
		
		dtWeek = new DateTime(this, SWT.CALENDAR);
		FormData fd_dtWeek = new FormData();
		fd_dtWeek.top = new FormAttachment(0, 6);
		fd_dtWeek.left = new FormAttachment(0, 6);
		dtWeek.setLayoutData(fd_dtWeek);
		dtWeek.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				week.setDate(getDate());
			}
		});
		
		lblNextDays = new Label(this, SWT.NONE);
		FormData fd_lblNextDays = new FormData();
		fd_lblNextDays.left = new FormAttachment(dtWeek, 0, SWT.LEFT);
		fd_lblNextDays.right = new FormAttachment(dtWeek, 0, SWT.RIGHT);
		fd_lblNextDays.top = new FormAttachment(dtWeek, 6);
		fd_lblNextDays.height = 25;
		lblNextDays.setLayoutData(fd_lblNextDays);
		lblNextDays.setText("Events for the next " + NUMBEROFDAYS + " days:");
		
		eventListViewer = new ListViewer(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		eventList = eventListViewer.getList();
		FormData fd_eventList = new FormData();
		fd_eventList.bottom = new FormAttachment(100, -6 - 42);
		fd_eventList.left = new FormAttachment(dtWeek, 0, SWT.LEFT);
		fd_eventList.right = new FormAttachment(dtWeek, 0, SWT.RIGHT);
		fd_eventList.top = new FormAttachment(lblNextDays, 6);
		eventList.setLayoutData(fd_eventList);
		
		week = new Week(this, SWT.NONE, _eventManager, _doubleClickEventListener, _selectionEventListener, _keyEventListener);
		FormData fd_week = new FormData();
		fd_week.top = new FormAttachment(dtWeek, 0, SWT.TOP);
		fd_week.left = new FormAttachment(dtWeek, 6);
		fd_week.right = new FormAttachment(100, -6);
		fd_week.bottom = new FormAttachment(eventList, 0, SWT.BOTTOM);
		week.setLayoutData(fd_week);
		
		Button btnRemoveEvent = new Button(this, SWT.NONE);
		FormData fd_btnRemoveEvent = new FormData();
		fd_btnRemoveEvent.height = 30;
		fd_btnRemoveEvent.width = 150;
		fd_btnRemoveEvent.bottom = new FormAttachment(100, -10);
		fd_btnRemoveEvent.right = new FormAttachment(week, 0, SWT.RIGHT);
		btnRemoveEvent.setLayoutData(fd_btnRemoveEvent);
		btnRemoveEvent.setText("Remove Event");
		btnRemoveEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				removeEvent();
			}
		});
		
		Button btnModifyEvent = new Button(this, SWT.NONE);
		FormData fd_btnModifyEvent = new FormData();
		fd_btnModifyEvent.height = 30;
		fd_btnModifyEvent.width = 150;
		fd_btnModifyEvent.bottom = new FormAttachment(btnRemoveEvent, 0, SWT.BOTTOM);
		fd_btnModifyEvent.right = new FormAttachment(btnRemoveEvent, -6);
		btnModifyEvent.setLayoutData(fd_btnModifyEvent);
		btnModifyEvent.setText("Modify Event");
		btnModifyEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				editEvent();
			}
		});
		
		Button btnAddEvent = new Button(this, SWT.NONE);
		FormData fd_btnAddEvent = new FormData();
		fd_btnAddEvent.height = 30;
		fd_btnAddEvent.width = 150;
		fd_btnAddEvent.bottom = new FormAttachment(btnRemoveEvent, 0, SWT.BOTTOM);
		fd_btnAddEvent.right = new FormAttachment(btnModifyEvent, -6);
		btnAddEvent.setLayoutData(fd_btnAddEvent);
		btnAddEvent.setText("Add Event");
		btnAddEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newEvent();
			}
		});
		
		eventListViewer.addDoubleClickListener(_doubleClickEventListener);
		eventListViewer.addSelectionChangedListener(_selectionEventListener);
		eventList.addKeyListener(_keyEventListener);
		
		eventListViewer.setContentProvider(new ArrayContentProvider());
		eventListViewer.setLabelProvider(new ILabelProvider() {
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
				
				if (e.getDate() != null)
					return String.format("%s: %s", Utility.formatDateLong(e.getDate()), e.getDescription());
				else
					return e.getDescription();
			}
			
			@Override
			public Image getImage(Object arg0) {
				return null;
			}
		});
	}
	
	public Calendar getDate() {
		Calendar date = (Calendar)Calendar.getInstance().clone();
		
		date.set(Calendar.YEAR, dtWeek.getYear());
		date.set(Calendar.MONTH, dtWeek.getMonth());
		date.set(Calendar.DAY_OF_MONTH, dtWeek.getDay());
		
		return date;
	}
	
	public Week getWeek() { return week; }
	
	private int compareBirthdays(Calendar date1, Calendar date2) {
		int result = 0;
		int offset1 = 0, offset2 = 0;
		
		if (Utility.compareBirthdays(date1, _startDate) < 0) offset1 = 12;
		if (Utility.compareBirthdays(date2, _startDate) < 0) offset2 = 12;
		
		if (date1.get(Calendar.MONTH) + offset1 < date2.get(Calendar.MONTH) + offset2)
			result = -1;
		else if (date1.get(Calendar.MONTH) + offset1 > date2.get(Calendar.MONTH) + offset2)
			result = 1;
		else
		{
			if (date1.get(Calendar.DAY_OF_MONTH) < date2.get(Calendar.DAY_OF_MONTH))
				result = -1;
			else if (date1.get(Calendar.DAY_OF_MONTH) > date2.get(Calendar.DAY_OF_MONTH))
				result = 1;
			else
				result = 0;
		}
		
		return result;
	}
	
	private void swap(ArrayList<Event> events, int index1, int index2) {
		Event temp = events.get(index1);
		events.set(index1, events.get(index2));
		events.set(index2, temp);
	}
	
	private void mergeSort(ArrayList<Event> birthdays) {
		int index, index1, index2, blockSize = 1, max, max1, max2, i, count;
		
		if (birthdays != null)
		{
			max = birthdays.size();
			
			while (blockSize < max)
			{
				for (index = 0; index < max; index += (blockSize * 2))
				{
					index1 = index;
					index2 = index + blockSize;
					
					max1 = Math.min(index1 + blockSize, max);
					max2 = Math.min(index2 + blockSize, max);
					
					for (; index1 < max1 && index2 < max2;) {
						if (compareBirthdays(birthdays.get(index2).getDate(), birthdays.get(index1).getDate()) < 0) {
							for (i = 0, count = index2 - index1; i < count; ++i) {
								swap(birthdays, index2 - i, index2 - (i + 1));
							}
							
							++max1;
							++index1;
							++index2;
						}
						else {
							++index1;
						}
					}
				}
				
				blockSize *= 2;
			}
		}
	}
	
	public void refresh() {
		int i = 0, maxI, j = 0, maxJ;
		ArrayList<Event> birthdays;
		
		selectedEvent = null;
		
		_startDate = (Calendar)Calendar.getInstance().clone();
		
		_startDate.set(Calendar.HOUR_OF_DAY, 0);
		_startDate.set(Calendar.MINUTE, 0);
		_startDate.set(Calendar.SECOND, 0);
		_startDate.set(Calendar.MILLISECOND, 0);
		
		_endDate = (Calendar)_startDate.clone();
		_endDate.add(Calendar.DAY_OF_MONTH, NUMBEROFDAYS);
		
		_events = _eventManager.getAllEventsInRange(_startDate, _endDate, true);
		birthdays = _eventManager.getBirthdaysInRange(_eventManager.getClientEvents(), _startDate, _endDate, false);
		mergeSort(birthdays);
		
		for (maxI = _events.size(), maxJ = birthdays.size(); i < maxI && j < maxJ; ++i) {
			if (_events.get(i).getDate() != null && compareBirthdays(birthdays.get(j).getDate(), _events.get(i).getDate()) < 0) {
				_events.add(i, birthdays.get(j));
				
				++j;
			}
		}
		
		for (; j < maxJ; ++j) {
			_events.add(birthdays.get(j));
		}
		
		eventListViewer.setInput(_events.toArray());
		week.setDate(getDate());
	}
	
	public void newEvent() {
		EditEventWindow editWnd = new EditEventWindow(shell, SWT.NONE, null);
		Object result = editWnd.open();
		
		if (result != null) {
			Event e = (Event)result;
			
			_eventManager.insertEvent(e);
			
			refresh();
			selectedEvent = e;
		}
	}
	
	public void editEvent() {
		if (selectedEvent != null && selectedEvent.getID() != 0) {
			System.out.println("Edit event: " + selectedEvent.getDescription());
			
			EditEventWindow editWnd = new EditEventWindow(shell, SWT.NONE, selectedEvent);
			Object result = editWnd.open();
			
			if (result != null) {
				Event e = (Event)result;
				
				_eventManager.updateEvent(e);
				
				refresh();
				selectedEvent = e;
			}
		}
	}
	
	public void removeEvent() {
		if (selectedEvent != null && selectedEvent.getID() != 0) {
			if (Utility.confirmDelete(shell, selectedEvent.getDescription()))
			{
				_eventManager.removeEvent(selectedEvent);
				refresh();
			}
		}
	}
}
