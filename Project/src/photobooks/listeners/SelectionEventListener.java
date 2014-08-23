package photobooks.listeners;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import photobooks.objects.Event;
import photobooks.presentation.EventsPage;

public class SelectionEventListener implements ISelectionChangedListener {

	private EventsPage _eventsPage;
	
	public SelectionEventListener(EventsPage eventsPage) {
		_eventsPage = eventsPage;
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		StructuredSelection selection = (StructuredSelection)arg0.getSelection();
		
		if (_eventsPage != null && !selection.isEmpty() && selection.getFirstElement() instanceof Event) {
			_eventsPage.selectedEvent = (Event)selection.getFirstElement();
			System.out.println("Event selected: " + _eventsPage.selectedEvent.getDescription());
		}
		else if (_eventsPage != null)
			_eventsPage.selectedEvent = null;
	}

}
