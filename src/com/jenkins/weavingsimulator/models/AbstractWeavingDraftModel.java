/*
 * AbstractWeavingDraftModel.java
 * 
 * Created on April 11, 2003, 10:58 PM
 *  
 * Copyright 2003 Adam P. Jenkins
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

import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author  ajenkins
 */
public abstract class AbstractWeavingDraftModel 
    extends AbstractTableModel 
{
    /** Holds value of property draft. */
    protected WeavingDraft draft;
    private PropertyChangeListener draftListener;
    

    /** Creates a new instance of AbstractWeavingDraftModel */
    public AbstractWeavingDraftModel(WeavingDraft draft) {
        draftListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                // if one of the num* properties changed, number of columns
                // may have changed
                if (e instanceof IndexedPropertyChangeEvent)
                    fireTableDataChanged();
                else
                    fireTableStructureChanged();
            }
        };
        setDraft(draft);
    }
    
    /** Getter for property draft.
     * @return Value of property draft.
     *
     */
    public WeavingDraft getDraft() {
        return this.draft;
    }
    
    /** Setter for property draft.
     * @param draft New value of property draft.
     *
     */
    public void setDraft(WeavingDraft draft) {
        if (this.draft != null)
            this.draft.removePropertyChangeListener(draftListener);
        this.draft = draft;
        draft.addPropertyChangeListener(draftListener);
        fireTableStructureChanged();
    }
    
    protected void setDraftListener(PropertyChangeListener listener) {
        draft.removePropertyChangeListener(draftListener);
        draftListener = listener;
        draft.addPropertyChangeListener(draftListener);
    }
    
    public void fireTableColumnUpdated(int column) {
        fireTableChanged(new TableModelEvent(this, 
            0, getRowCount() - 1, column));
    }
}
