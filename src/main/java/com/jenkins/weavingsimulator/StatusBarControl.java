package com.jenkins.weavingsimulator;

import java.awt.*;
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
	private static final long serialVersionUID = 1L;
	StatusBarModel model;
	
	public StatusBarControl (StatusBarModel model) {
		this.model = model;
		this.model.addPropertyChangeListener(this);
		FontMetrics metrics = getFontMetrics(getFont());
		Dimension dim = new Dimension ((int)(metrics.stringWidth(model.getMetricsString()) * 1.1),
				(int)(metrics.getHeight() * 1.3));
		setPreferredSize(dim);
		setEditable(false);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		setText((String)evt.getNewValue());
	}
}
