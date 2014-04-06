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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.beans.IndexedPropertyChangeEvent;
import com.jenkins.weavingsimulator.datatypes.WeftPick;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;

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
public class TreadlingDraftModel extends AbstractWeavingDraftModel {
    
    /** Creates a new instance of TreadlingDraftModel */
    public TreadlingDraftModel(WeavingDraft draft) {
        super(draft);
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
    
    /** Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param	rowIndex	the row whose value is to be queried
     * @param	columnIndex 	the column whose value is to be queried
     * @return	the value Object (color) at the specified cell
     *
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (draft.getPicks().get(rowIndex).isTreadleSelected(columnIndex))
            return Color.BLACK;
        else if (rowIndex >= selectionStart && rowIndex < selectionEnd)
            return Color.LIGHT_GRAY;
        else
        	return Color.WHITE;
    }
    
    /** Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>aValue</code>.
     *
     * @param	aValue		 the new value (boolean)
     * @param	rowIndex	 the row whose value is to be changed
     * @param	columnIndex 	 the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     *
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        WeftPick pick = draft.getPicks().get(rowIndex);
        pick.setTreadleId(columnIndex);
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
    
    private int selectionStart = 0;
    private int selectionEnd = 0;
    /** Sets the selection to be the rows [startRow..endRow)
     *  
     * @param startRow First row that is selected
     * @param startColumn Unused
     * @param endRow One-past-last row selected.
     * @param endColumn Unused
     */
    public void setSelection (int startRow, int startColumn, int endRow, int endColumn) {
    	selectionStart = startRow;
    	selectionEnd = endRow;
    	fireTableDataChanged();
    }
    
    public void pasteSelection (int rowIndex, int columnIndex) {
    	int offset = rowIndex - selectionStart;
    	for (int row = selectionStart; row != selectionEnd; row++) {
            WeftPick to = draft.getPicks().get(row + offset);
            WeftPick from = draft.getPicks().get(row);
            for (int col = 0; col < draft.getTreadles().size(); col++) {
            	if (from.isTreadleSelected(col)) to.setTreadleId(col);
            }
    	}
    }
}
