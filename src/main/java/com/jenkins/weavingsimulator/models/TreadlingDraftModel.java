/*
 * TreadlingDraftModel.java
 * 
 * Created on April 11, 2003, 10:56 PM
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

import java.awt.Color;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jenkins.weavingsimulator.datatypes.WeftPick;

/** Represents the treadling draft display of a weaving draft.  The treadling
 * draft displays which treadle should be pushed for each pick in the pattern.
 * So this TableModel represents each treadle by a column, and pick by a row.
 * For each row, the column representing the treadle to push is black, and the
 * rest of the squares are white, except the selection is indicated with grey.
 * This means the getValueAt returns a Color , but setValueAt takes a boolean.
 * Odd but it works. 
 *
 * @author  ajenkins
 */
public class TreadlingDraftModel extends CopyableWeavingGridModel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of TreadlingDraftModel */
    public TreadlingDraftModel(EditingSession session) {
        super(session);
        setDraftListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                String propName = ev.getPropertyName();
                if (propName.equals("treadles")) {
                    // we only care if the *number* of treadles changes
                    if (!(ev instanceof IndexedPropertyChangeEvent)) {
                        fireTableStructureChanged();
                    }
                } else if (propName.equals("picks")) {
                    // each pick represents a row
                    if (ev instanceof IndexedPropertyChangeEvent) {
                        IndexedPropertyChangeEvent iev = (IndexedPropertyChangeEvent)ev;
                        fireTableRowsUpdated(iev.getIndex(), iev.getIndex());
                    } else {
                        fireTableDataChanged();
                    }
                }
            }
        });
    }
    
    /** Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     *
     */
    public int getColumnCount() {
        return draft.getTreadles().size();
    }
    
    /** Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     *
     */
    public int getRowCount() {
        return draft.getPicks().size();
    }    
    
	@Override
	protected Command getSetValueCommand(final Object aValue, final int row, final int column) {
		final WeftPick pick = draft.getPicks().get(row);
		
		if (draft.getIsLiftplan()) {
			return new Command () {
				boolean oldValue;
				public void execute() {
					oldValue = pick.getTreadles()[column];
					pick.setTreadle(column, (Boolean)aValue);
				}
				public void undo() {
					pick.setTreadle(column, oldValue);
				}
			};
		} else {
			return new Command () {
				int oldId;
				public void execute() {
					oldId = pick.getSelection();
		        	if ((Boolean)aValue) pick.setTreadleId(column);
				}
				public void undo() {
					pick.setTreadleId(oldId);
				}
			};
		}
	}
	
    public boolean getBooleanValueAt (int row, int column) {
    	return draft.getPicks().get(row).isTreadleSelected(column);
    }
    
    public void setBooleanValueAt (boolean value, int row, int column) {
		final WeftPick pick = draft.getPicks().get(row);
		if (draft.getIsLiftplan()) {
			pick.setTreadle(column, value);
		} else {
			if (value) pick.setTreadleId(column);
		}    	
    }
    
    protected PasteGrid getUndoSelection (PasteGrid selection) {
		PasteGrid grid = new PasteGrid (this, 
				new GridSelection(selection.getStartRow(), 
						0, 
						Math.min(selection.getStartRow() + selection.getRows(), this.getRowCount()),
						this.getColumnCount()));
		grid.setOrigin(selection.getStartRow(), 0);
		return grid;
    }
    
    /** Returns true if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code>
     * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param	rowIndex	the row whose value to be queried
     * @param	columnIndex	the column whose value to be queried
     * @return	true if the cell is editable
     * @see #setValueAt
     *
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    
    /** Returns the most specific superclass for all the cell values
     * in the column.  This is used by the <code>JTable</code> to set up a
     * default renderer and editor for the column.
     *
     * @param columnIndex  the index of the column
     * @return the common ancestor class of the object values in the model.
     *
     */
    public Class<?> getColumnClass(int columnIndex) {
        return Color.class;
    }
    
    public EditedValueProvider getEditedValueProvider () {
    	return new ToggleEditProvider(this);
    }
}
