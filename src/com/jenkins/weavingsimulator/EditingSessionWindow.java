package com.jenkins.weavingsimulator;

import javax.swing.JInternalFrame;

import com.jenkins.weavingsimulator.models.EditingSession;

/**
 * Base class for windows that interact with an editing session.
 * @author pete
 *
 */
public abstract class EditingSessionWindow extends JInternalFrame {
	private EditingSession session;
	
	public EditingSessionWindow (EditingSession session) {
		this.session = session;
	}
	
	/**
	 * Getter for property session.
	 * 
	 * @return Value of property session.
	 */
	public EditingSession getSession() {
		return this.session;
	}
}
