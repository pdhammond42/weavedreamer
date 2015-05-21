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

import java.awt.Rectangle;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import com.jenkins.weavingsimulator.datatypes.WeavingDraft;

/**
 *
 * @author  ajenkins
 */
public abstract class AbstractWeavingDraftModel 
    extends AbstractTableModel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EditingSession session;
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
    public AbstractWeavingDraftModel(EditingSession session) {
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
        this.draft = session.getDraft();
        this.session = session;
        if (draft != null) draft.addPropertyChangeListener(draftListener);
    }
    
    /** Factory method that returns a command object to set the model's value
     * This object is passed to the session to be actually executed with
     * undo/redo support.
     * 
     * @param aValue Value to set
     * @param row row to set 
     * @param column column to set.
     * @return
     */
	protected abstract Command getSetValueCommand(Object aValue, int row, int column);
	
	/** Override the AbstractTableModel method to set the value, 
	 * working in terms of a command object that is passed to the session.
	 * 
	 * @see #getValueAt
	 * @see #getSetValueCommand
	 */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	session.execute(getSetValueCommand(aValue, rowIndex, columnIndex));
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
    public void pasteSelection (int rowIndex, int columnIndex, CellSelectionTransform transform) {
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
    
    /** Returns true if the model supports copy/paste behaviour.
     * Default is false.
     */
    public boolean supportsPaste() {
    	return false;
    }
    
    /** Returns true if the model can do a paste now.
     * 
     * @return
     */
    public boolean canPaste() {
    	return false;
    }
    
    /**	 
     * This is nothing to do with conventional JTable cell editing.
     * It returns a value to be set into a selected cell. 
     * The GridControl holds a single instance of this interface. 
     * When a cell is clicked, or dragged over, its value is set according to the
     * value returned. 
     *  
     */
    public interface EditedValueProvider {
    	Object getValue(int row, int column);
    }
    
    public abstract EditedValueProvider getEditedValueProvider();
    
    // An edited value provider that always returns true.
    protected class SetValueProvider implements EditedValueProvider {
		public SetValueProvider() {
		}

		public Object getValue(int row, int column) {
			return true;
		}
    }
    
    protected class ColorEditProvider implements EditedValueProvider {
    	public ColorEditProvider (EditingSession session) {
    		this.session = session;
    	}
    	
		public Object getValue(int row, int column) {
			int selection = session.getPalette().getSelection();
			if (selection != -1) {
				return session.getPalette().getColor(selection);
			} else {
				return null;
			}
		}
		
		private EditingSession session;
	};

}
