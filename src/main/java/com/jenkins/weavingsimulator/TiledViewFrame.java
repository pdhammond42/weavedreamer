package com.jenkins.weavingsimulator;

import javax.swing.JFrame;

import com.jenkins.weavingsimulator.models.EditingSession;

/** Frame that is also an Editing Session.View, for the tiled view.
 * 
 * @author pete
 *
 */
public class TiledViewFrame extends JFrame implements EditingSession.View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TiledViewFrame(String name) {
		super(name);
	}

	@Override
	public void closeView() {
		setVisible(false);
	}

	@Override
	public String getViewName() {
		return "Tiled";
	}
}
