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

import java.awt.MenuItem;
import java.awt.Rectangle;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenuItem;
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
    
    /**
     * The current selected set of cells.
     * This works in table cells, not pixels. 
     */
    private Rectangle cursorSelection = new Rectangle();
    
    /**
     * Constant that can be used for the type parameter of 
     * a TableModelEvent to indicate that the cursor position has changed.
     */
    public static int CURSOR = 4;
    
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
        this.draft = draft;
        if (draft != null) draft.addPropertyChangeListener(draftListener);
    }
    
    /** Getter for property draft.
     * @return Value of property draft.
     *
     */
    public WeavingDraft getDraft() {
        return this.draft;
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
    
    /** Sets a selection into the model, which can later be duplicated by calling pasteSelection.
     * The default implementation is a no-op. 
     * @param startRow Starting row of the selection
     * @param startColumn Starting column of the selection
     * @param endRow One-past-end row of the selection.
     * @param endColumn One-past-end column of the selection
     */
    public void showSelection (int startRow, int startColumn, int endRow, int endColumn){
    }
    
    /** If a selection has previously been set by showSelection, copies it to
     * a shared location that can be accessed later by copySelection. 
     * Default operation is a no-op. 
     * Groups of derived objects will co-operate about where the shared 
     * location is.
     */
    public void copySelection() {
    	
    }
    
    /** If a selection has previously been copied by any cooperating object,
     *  duplicates that selection
     * with its top left hand corner at rowIndex, columnIndex.
     * @param rowIndex Starting row
     * @param columnIndex Starting column
     */
    public void pasteSelection (int rowIndex, int columnIndex) {
    }
    
    /**
     * Sets the current cell cursor position and notifies any listeners.
     * @param rowIndex row that the cursor is over
     * @param columnIndex column that the cursor is over
     */
    public void setCurrentCell (int rowIndex, int columnIndex) {
    	cursorSelection = new Rectangle (columnIndex, rowIndex, -1, -1);
    	fireTableChanged (new TableModelEvent(this, rowIndex, rowIndex, columnIndex, CURSOR));
    }
    
    /**
     * Sets the current cell cursor position and notifies any listeners.
     * @param startRow Starting row of the selection
     * @param startColumn Starting column of the selection
     * @param endRow One-past-end row of the selection.
     * @param endColumn One-past-end column of the selection

     */
    public void setCurrentCell (int startRow, int startColumn, int endRow, int endColumn) {
    	cursorSelection = new Rectangle (startColumn, startRow, Math.abs(startColumn-endColumn), Math.abs(startRow-endRow));
    	fireTableChanged (new TableModelEvent(this, startRow, startRow, startColumn, CURSOR));
    }

    /**
     * Gets the current cursor location, as a starting cell and width & height of any drag in progress.
     * If no drag is in progress reports -1, -1 for the dimensions.
     * @return
     * 
     */
    public Rectangle getCurrentCell() {
    	return cursorSelection;
    }
    
    /** Returns a list of menu items that can be shown in a context menu.
     * The default is an empty list, in which case  no menu is displayed.
     * The items should have their Action listeners set.
     * @return A set of menu items.
     */
    public JMenuItem[] getMenuItems(int row, int column) {
    	return new JMenuItem[0];
    }
}
