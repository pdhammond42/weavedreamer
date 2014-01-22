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

import com.jenkins.weavingsimulator.datatypes.WarpEnd;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;

import java.awt.Color;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


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
public class ThreadingDraftModel extends AbstractWeavingDraftModel {
    /** Creates a new instance of ThreadingDraftModel */
    public ThreadingDraftModel(WeavingDraft draft) {
        super(draft);
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
    
    /** Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param	rowIndex	the row whose value is to be queried
     * @param	columnIndex 	the column whose value is to be queried
     * @return	the value Object at the specified cell
     *
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        WarpEnd end = draft.getEnds().get(columnIndex);
        if (end.getHarnessId() == rowIndex) {
        	return Color.BLACK;
        } else if (columnIndex >= selectionStart && columnIndex < selectionEnd) {
        	return Color.LIGHT_GRAY;
        } else {
        	return Color.WHITE;
        }
    }
        
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        WarpEnd end = draft.getEnds().get(columnIndex);
        if (rowIndex != end.getHarnessId()) {
            end.setHarnessId(rowIndex);
        }        
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    
    public Class<?> getColumnClass(int columnIndex) {
        return Color.class;
    }
    
    private int selectionStart = 0;
    private int selectionEnd = 0;
    /** Sets the selection to be the columns [startColumn..endColumn)
     *  
     * @param startRow Unused 
     * @param startColumn First column that is selected 
     * @param endRow Unused
     * @param endColumn One-past-last column selected.
     */
    public void setSelection (int startRow, int startColumn, int endRow, int endColumn) {
    	selectionStart = startColumn;
    	selectionEnd = endColumn;
    }
    
    public void pasteSelection (int rowIndex, int columnIndex) {
    	int offset = columnIndex - selectionStart;
    	for (int column = selectionStart; column != selectionEnd; column++) {
            WarpEnd to = draft.getEnds().get(column + offset);
            WarpEnd from = draft.getEnds().get(column);
            to.setHarnessId(from.getHarnessId());   	
    	}
    }
}
