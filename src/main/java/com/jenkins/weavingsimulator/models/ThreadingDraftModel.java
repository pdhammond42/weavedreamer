/*
 * ThreadingDraftModel.java
 * 
 * Created on April 11, 2003, 1:48 AM
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

import com.jenkins.weavingsimulator.datatypes.WarpEnd;


/** This TableModel represents the threading draft part of a weaving draft. 
 * Each column represents a warp thread in the pattern.  The rows each
 * represent one harness.  An element value of true means the warp thread
 * represented by that column is connected to the harness represented by that
 * row.
 * Each selected cell is coloured black on a white background, 
 * except the selection is indicated with grey.
 * This means the getValueAt returns a Color , but setValueAt takes a boolean.
 * Odd but it works. 
 * @author  ajenkins
 */
public class ThreadingDraftModel extends CopyableWeavingGridModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of ThreadingDraftModel */
    public ThreadingDraftModel(EditingSession session) {
        super(session);
        setDraftListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                String propName = ev.getPropertyName();
                if (propName.equals("ends")) {
                    if (ev instanceof IndexedPropertyChangeEvent) {
                        // Each end is a column, so a single table column has changed
                        IndexedPropertyChangeEvent iev = (IndexedPropertyChangeEvent)ev;
                        fireTableColumnUpdated(iev.getIndex());
                    } else {
                        fireTableStructureChanged();
                    }
                } else if (propName.equals("numHarnesses")) {
                    fireTableDataChanged();
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
        return draft.getEnds().size();
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
        return draft.getNumHarnesses();
    }
        
    public boolean getBooleanValueAt (int row, int column) {
    	return draft.getEnds().get(column).getHarnessId() == row;
    }

    public void setBooleanValueAt (boolean value, int row, int column) {
    	final WarpEnd end = draft.getEnds().get(column);
    	if (value && row != end.getHarnessId()) {
    		end.setHarnessId(row);
    	}    	
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    
    public Class<?> getColumnClass(int columnIndex) {
        return Color.class;
    }
    
    public EditedValueProvider getEditedValueProvider() {
    	return new SetValueProvider();
    }

	@Override
	protected Command getSetValueCommand(final Object aValue, final int row, final int column) {
		final WarpEnd end = draft.getEnds().get(column);
		final int oldHarness = end.getHarnessId();

		return new Command () {	        
			public void execute() {
		        if ((Boolean)aValue && row != end.getHarnessId()) {
		            end.setHarnessId(row);
		        }
			}
			public void undo() {
				end.setHarnessId(oldHarness);
			}
		};
	}

	@Override
	protected PasteGrid getUndoSelection(PasteGrid selection) {
		PasteGrid grid = new PasteGrid (this, 
				new GridSelection(0, 
						selection.getStartColumn(), 
						this.getRowCount(), 
						Math.min(selection.getStartColumn() + selection.getColumns(), this.getColumnCount())));
		grid.setOrigin(0,selection.getStartColumn());
		return grid;
	}
}
