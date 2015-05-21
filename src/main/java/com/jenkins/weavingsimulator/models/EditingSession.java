/*
 * EditingSession.java
 * 
 * Created on January 16, 2005, 10:36 AM
 *  
 * Copyright 2005 Adam P. Jenkins
 * 
 * This file is part of WeavingSimulator
 * 
 * WeavingSimulator is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * WeavingSimulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WeavingSimulator; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


package com.jenkins.weavingsimulator.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.jenkins.weavingsimulator.datatypes.Palette;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;


/**
 * An EditingSession holds the state for editing a WeavingDraft.
 * It binds together various aspects of the editing into one place to
 * allow navigation between them.
 * @author ajenkins
 */
public class EditingSession {
    public static final String DRAFT_PROPERTY = "draft";
    public static final String DRAFT_MODIFIED_PROPERTY = "draftModified";
    public static final String FILE_PROPERTY = "file";
    public static final String PALETTE_PROPERTY = "palette";
    public static final String SELECTION_PROPERTY = "selection";
    
    private Deque<Command> undoList = new ArrayDeque<Command>();
    private Deque<Command> redoList = new ArrayDeque<Command>();
    
    private PropertyChangeSupport propertySupport;

    /**
     * Holds value of property draft.
     */
    private WeavingDraft draft;

    /**
     * Holds value of property file.
     */
    private File file;

    /**
     * Holds value of property draftModified.
     */
    private boolean draftModified;
    
    private PasteGrid selection;
	private List<View> views = new ArrayList<View>();
	
    public EditingSession(WeavingDraft draft) {
        propertySupport = new PropertyChangeSupport(this);

        this.draft = draft;
        // The palette may get changed directly on the draft. The session
        // needs to propagate this.
        this.draft.addPropertyChangeListener("palette", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
            	if (e.getPropertyName() == "palette") 
            		propertySupport.firePropertyChange (PALETTE_PROPERTY, 
            				e.getOldValue(), e.getNewValue());
            }
        });
        
		draft.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent ev) {
				if (!isDraftModified())
					setDraftModified(true);
			}
		});
		selection = new PasteGrid();
    }

    /** Interface to a view of the editing session. The session's idea 
     * of the view is very limited, basically it can enumerate them and 
     * close them.
     * @author pete
     *
     */
    public interface View {
    	public abstract void closeView();
    	public abstract String getViewName();
    }
    
	public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propName, 
            PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(propName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(String propName, 
            PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(propName, listener);
    }

    public void execute (Command command) {
    	command.execute();
    	undoList.push(command);
    }
    
	public void undo() {
		if (!undoList.isEmpty()) {
			Command command = undoList.pop();
			command.undo();
			redoList.push(command);
		}
	}
	
	public void redo() {
		if (!redoList.isEmpty()) {
			Command command = redoList.pop();
			command.execute();
			undoList.push(command);
		}		
	}
	
	/** Mainly for testing, query if there is anything in the
	 * undo buffer.
	 * @return True if there is at least one command to undo.
	 */
	public boolean canUndo() {
		return (!undoList.isEmpty());
	}
	
    /**
     * Getter for property palette.
     * @return Value of property palette.
     */
    public Palette getPalette() {

        return this.draft.getPalette();
    }

    /**
     * Setter for property palette.
     * @param palette New value of property palette.
     */
    public void setPalette(Palette palette) {

        Palette oldPalette = this.draft.getPalette();
        this.draft.setPalette(palette);
        propertySupport.firePropertyChange (PALETTE_PROPERTY, oldPalette, palette);
    }

    /**
     * Getter for property draft.
     * @return Value of property draft.
     */
    public WeavingDraft getDraft() {
        return this.draft;
    }

    /**
     * Getter for property file.
     * @return Value of property file.
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Setter for property file.
     * @param file New value of property file.
     */
    public void setFile(File file) {
        File oldFile = this.file;
        this.file = file;
        draft.setName(file.getName());
        propertySupport.firePropertyChange (FILE_PROPERTY, oldFile, file);
    }

    /**
     * Getter for property modified.
     * @return Value of property modified.
     */
    public boolean isDraftModified()  {
        return this.draftModified;
    }

    /**
     * Setter for property modified.
     * @param draftModified New value of property modified.
     */
    public void setDraftModified(boolean draftModified) {
        boolean oldDraftModified = this.draftModified;
        this.draftModified = draftModified;
        propertySupport.firePropertyChange (DRAFT_MODIFIED_PROPERTY, 
                oldDraftModified, draftModified);
    }

    /** Returns the current selection. If nothing is selected, returns an object 
     * with zero size.
     * @return
     */
    public PasteGrid getSelectedCells() {
		return selection;
	}


	public void setSelectedCells(PasteGrid selectedCells) {
		PasteGrid oldSelection = selection;
		selection = selectedCells;		
        propertySupport.firePropertyChange (SELECTION_PROPERTY, 
        		oldSelection, selection);
	}
	
	public List<View> getViews() {
		return views;
	}
}
