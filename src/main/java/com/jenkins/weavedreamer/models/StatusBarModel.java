package com.jenkins.weavedreamer.models;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * A model class that aggregates status information into a simple string to be displayed.
 * If uses the Beans PropertyChanged notification classes but probably wrong.
 * Currently the only status information displayed is the cursor position in the grids.
 * @author pete
 *
 */
public class StatusBarModel implements TableModelListener {

	private String text = new String();
	private EventListenerList listeners = new EventListenerList();
	
	public String getText() {
		return text;
	}
	
	/**
	 * Observes the given grid model for interesting status changes.
	 * @param model
	 */
	public void listen(AbstractWeavingDraftModel model) {
		model.addTableModelListener(this);
	}

	public void tableChanged(TableModelEvent e) {
		if (e.getType() == AbstractWeavingDraftModel.CURSOR) {
			String oldText = text;
			AbstractWeavingDraftModel model = (AbstractWeavingDraftModel)e.getSource();
			Rectangle rect = model.getCurrentCell();
			if (model.getColumnCount() == 1) {
				text = format1d(rect.y, rect.height);
			} else if (model.getRowCount() == 1) {
				text = format1d(rect.x, rect.width);
			} else {
				text = format2d(rect);
			}
			for (PropertyChangeListener l : listeners.getListeners(PropertyChangeListener.class)) {
				l.propertyChange(new PropertyChangeEvent(this, "text", oldText, text));
			}
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(PropertyChangeListener.class, listener);
	}
	
	public void removePropertyChangeListener (PropertyChangeListener listener) {
		listeners.remove(PropertyChangeListener.class, listener);
	}

	/**
     * Returns a string that can be measured to find a suitable width for the container.
	 */
    public String getMetricsString() {
    	return format2d(new Rectangle(999, 999, 999, 999));
    }

    private String format2d(Rectangle rect) {
		return String.format("%d, %d (%dx%d)",
				rect.x + 1, rect.y + 1, rect.width + 1, rect.height + 1);
	}

	private String format1d(int start, int count) {
		return String.format("%d (%d)",
				start + 1, count + 1);
	}
}
