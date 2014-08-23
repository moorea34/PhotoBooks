package photobooks.listeners;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;

import photobooks.objects.Event;
import photobooks.presentation.EventsPage;

public class DoubleClickEventListener implements IDoubleClickListener {
	
	private EventsPage _eventsPage;
	
	public DoubleClickEventListener(EventsPage eventsPage) {
		_eventsPage = eventsPage;
	}
	
	@Override
	public void doubleClick(DoubleClickEvent arg0) {
		StructuredSelection selection = (StructuredSelection)arg0.getSelection();
		
		if (_eventsPage != null && !selection.isEmpty() && selection.getFirstElement() instanceof Event) {
			_eventsPage.selectedEvent = (Event)selection.getFirstElement();
			_eventsPage.editEvent();
		}
	}

}
