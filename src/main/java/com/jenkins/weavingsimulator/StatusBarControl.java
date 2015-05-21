package com.jenkins.weavingsimulator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;

import com.jenkins.weavingsimulator.models.StatusBarModel;

/**
 * A simple view of a StatusBarModel, only implemented as a custom
 * control to ease tying up the notifications, without having to use the
 * whole Document architecture which seems a bit over the top for the job in hand.
 * @author PDHammond
 *
 */
public class StatusBarControl extends JTextField implements PropertyChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	StatusBarModel model;
	
	public StatusBarControl (StatusBarModel model) {
		this.model = model;
		this.model.addPropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		setText((String)evt.getNewValue());
	}
}
