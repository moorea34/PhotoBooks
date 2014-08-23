package photobooks.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

import photobooks.presentation.EventsPage;

public class KeyEventListener implements KeyListener {

	private EventsPage _eventsPage;
	
	public KeyEventListener(EventsPage eventsPage) {
		_eventsPage = eventsPage;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (_eventsPage != null && arg0.keyCode == SWT.DEL)
			_eventsPage.removeEvent();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

}
