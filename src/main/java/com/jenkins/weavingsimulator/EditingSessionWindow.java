package com.jenkins.weavingsimulator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;

import javax.swing.JInternalFrame;

import com.jenkins.weavingsimulator.models.EditingSession;

/**
 * Base class for windows that interact with an editing session.
 * @author pete
 *
 */
public abstract class EditingSessionWindow extends JInternalFrame implements EditingSession.View {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EditingSession session;
	
	public EditingSessionWindow (EditingSession session) {
		this.session = session;
	
		session.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						EditingSession.FILE_PROPERTY))
					fileChangedHandler((File) evt.getNewValue());
				else if (evt.getPropertyName().equals(
						EditingSession.DRAFT_MODIFIED_PROPERTY))
					draftModifiedChangedHandler((Boolean) evt.getNewValue());
			}
		});
		fileChangedHandler(getSession().getFile());
		draftModifiedChangedHandler(getSession().isDraftModified());
		session.getViews().add(this);
	}
	
	/**
	 * Getter for property session.
	 * 
	 * @return Value of property session.
	 */
	public EditingSession getSession() {
		return this.session;
	}

	private TiledViewFrame findTiledWindow() {
		for (EditingSession.View v: getSession().getViews()) {
			if (v.getViewName() == "Tiled") {
				return (TiledViewFrame)v;
			}
		}
		return null;
	}

	public void displayTiledView() {
		TiledViewFrame theTiledViewFrame = findTiledWindow(); 
		if (theTiledViewFrame == null) {
			WeavingPatternPanel wpanel = new WeavingPatternPanel(getSession().getDraft());
			wpanel.setName("draftPanel");
			final TiledViewFrame tiledViewFrame = new TiledViewFrame(session.getDraft().getName());
			tiledViewFrame.getContentPane().add(wpanel);
			tiledViewFrame.pack();
			tiledViewFrame.setSize(400, 400);
			addPropertyChangeListener(
					javax.swing.JInternalFrame.TITLE_PROPERTY,
					new java.beans.PropertyChangeListener() {
						public void propertyChange(
								java.beans.PropertyChangeEvent e) {
							tiledViewFrame.setTitle((String) e.getNewValue());
						}
					});
			theTiledViewFrame = tiledViewFrame;
			session.getViews().add(theTiledViewFrame);
		}
		theTiledViewFrame.setVisible(true);
	}

	private void fileChangedHandler(File file) {
		setTitle(session.getDraft().getName() + getWindowTitleSuffix());
	}

	/** Returns a string to be added to the file name to give the
	 * window title. Default is empty.
	 * 
	 * @return A string
	 */
	protected String getWindowTitleSuffix() {
		return "";
	}
	
	private void draftModifiedChangedHandler(boolean draftModified) {
		if (draftModified) {
			setTitle(getTitle() + " *");
		} else {
			String oldTitle = getTitle();
			if (oldTitle.endsWith(" *"))
				setTitle(oldTitle.substring(0, oldTitle.length() - 2));
		}
	}
	
	public void closeView() {
		try {
			setClosed(true);
		} catch (PropertyVetoException e) {
		}
	}
}
