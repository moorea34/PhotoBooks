package photobooks.presentation;

import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;

import photobooks.application.Utility;
import photobooks.business.EventManager;
import photobooks.listeners.DoubleClickEventListener;
import photobooks.listeners.KeyEventListener;
import photobooks.listeners.SelectionEventListener;
import photobooks.objects.Event;

public class Week extends Composite {
	
	private static final int DAYSINWEEK = 7;
	private static final int DAYWIDTH = 300;
	
	private Day[] days = new Day[DAYSINWEEK];
	private Composite composite;
	
	private EventManager _eventManager;
	private ArrayList<Event> _events = null;
	
	private ScrollBar hBar;
	
	private DoubleClickEventListener _doubleClickEventListener;
	private SelectionEventListener _selectionEventListener;
	private KeyEventListener _keyEventListener;
	
	public Week(Composite parent, int style, EventManager eventManager, DoubleClickEventListener doubleClickEventListener, SelectionEventListener selectionEventListener, KeyEventListener keyEventListener) {
		super(parent, style | SWT.H_SCROLL);
		
		_eventManager = eventManager;
		_selectionEventListener = selectionEventListener;
		_keyEventListener = keyEventListener;
		_doubleClickEventListener = doubleClickEventListener;
		
		init();
		setDate(Calendar.getInstance());
	}
	
	public void init() {
		composite = new Composite(this, SWT.NONE);
		composite.setLocation(0, 0);
		composite.setSize(DAYWIDTH * 7, getSize().y);
		composite.setLayout(new FormLayout());
		
		hBar = getHorizontalBar();
		
		for (int i = 0; i < days.length; ++i) {
			days[i] = new Day(composite, SWT.NONE, _doubleClickEventListener, _selectionEventListener, _keyEventListener);
			
			FormData fd = new FormData();
			
			if (i == 0)
				fd.left = new FormAttachment(0, 0);
			else
				fd.left = new FormAttachment(days[i - 1], 0, SWT.RIGHT);
			
			fd.top = new FormAttachment(0, 0);
			fd.bottom = new FormAttachment(100, -hBar.getSize().y);
			fd.width = DAYWIDTH;
			
			days[i].setLayoutData(fd);
		}
		
		this.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event arg0) {
				Point size = composite.getSize();
				Rectangle rect = getClientArea();
				
				size.y = getSize().y;
				
				hBar.setMaximum(size.x);
				hBar.setThumb(Math.min(size.x, rect.width));
				
				int hPage = size.x - rect.width;
				int hSelection = hBar.getSelection();
				Point location = composite.getLocation();
				
				if (hSelection >= hPage) {
					if (hPage <= 0) hSelection = 0;
					location.x = -hSelection;
				}
				
				composite.setLocation(location);
				composite.setSize(size);
			}
		});
		
		hBar.addListener (SWT.Selection, new Listener () {
			@Override
			public void handleEvent (org.eclipse.swt.widgets.Event e) {
				Point location = composite.getLocation();
				location.x = -hBar.getSelection();
				composite.setLocation(location);
			}
		});
	}
	
	public void refresh() {
		for (Day day : days) {
			day.refresh();
		}
	}
	
	public Day[] getDays() { return days; }
	
	public void setDate(Calendar week) {
		int offset = 0, dayOfWeek;
		Calendar end;

		week = (Calendar)week.clone();
		
		week.set(Calendar.HOUR_OF_DAY, 0);
		week.set(Calendar.MINUTE, 0);
		week.set(Calendar.SECOND, 0);
		week.set(Calendar.MILLISECOND, 0);
		
		offset = Utility.dayOfWeek(week);
		week.add(Calendar.DAY_OF_MONTH, -offset);
		
		end = (Calendar)week.clone();
		end.add(Calendar.DAY_OF_MONTH, 7);
		end.add(Calendar.MILLISECOND, -1);
		
		_events = _eventManager.getAllEventsInRange(week, end, false);
		_events.addAll(_eventManager.getBirthdaysInRange(_eventManager.getClientEvents(), week, end, false));
		
		System.out.println("Selected week starting on " + Utility.monthToString(week.get(Calendar.MONTH)) + " " + week.get(Calendar.DAY_OF_MONTH));
		
		for (Day day : days) {
			day.clearEvents();
			day.setDate((Calendar)week.clone());
			week.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		for (Event event : _events)
		{
			Calendar cd = (Calendar)event.getDate().clone();
			
			if (event.getID() == 0)
			{
				if (week.get(Calendar.MONTH) == Calendar.DECEMBER && cd.get(Calendar.MONTH) == Calendar.JANUARY)
					cd.set(Calendar.YEAR, week.get(Calendar.YEAR) + 1);
				else
					cd.set(Calendar.YEAR, week.get(Calendar.YEAR));
			}
			
			dayOfWeek = Utility.dayOfWeek(cd);
			
			if (dayOfWeek >= 0)
				days[dayOfWeek].addEvent(event);
		}
		
		refresh();
	}
}
