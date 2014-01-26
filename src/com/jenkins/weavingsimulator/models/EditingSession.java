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

import com.jenkins.weavingsimulator.datatypes.Palette;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;


/**
 * An EditingSession holds the state for editing a WeavingDraft.
 * @author ajenkins
 */
public class EditingSession {
    public static final String DRAFT_PROPERTY = "draft";
    public static final String DRAFT_MODIFIED_PROPERTY = "draftModified";
    public static final String FILE_PROPERTY = "file";
    public static final String PALETTE_PROPERTY = "palette";
    
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
    
    /**
     * The palette may get changed directly on the draft. The session
     * needs to propagate this.
     */
    private PropertyChangeListener draftPaletteChangedListener;
    
    
    public EditingSession() {
        propertySupport = new PropertyChangeSupport(this);
        draftPaletteChangedListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
            	if (e.getPropertyName() == "palette") 
            		propertySupport.firePropertyChange (PALETTE_PROPERTY, 
            				e.getOldValue(), e.getNewValue());
            }
        };
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
     * Setter for property draft.
     * @param draft New value of property draft.
     */
    public void setDraft(WeavingDraft draft) {

        WeavingDraft oldDraft = this.draft;
        this.draft = draft;
        propertySupport.firePropertyChange (DRAFT_PROPERTY, oldDraft, draft);
        this.draft.addPropertyChangeListener("palette", draftPaletteChangedListener);
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
}
