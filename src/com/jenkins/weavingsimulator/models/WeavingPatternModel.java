/*
 * WeavingPatternModel.java
 * 
 * Created on April 11, 2003, 12:59 AM
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
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;

/**
 * TableModel representing the fabric resulting from a weaving pattern.
 * Each row represents a weft thread, and each column represents a warp thread.
 * Each element represents the color of the warp or weft thread that would
 * show when viewing the fabric from the top.
 *
 * @author  ajenkins
 */
public class WeavingPatternModel extends AbstractWeavingDraftModel {
    
    /** Creates a new instance of WeavingPatternModel */
    public WeavingPatternModel(WeavingDraft draft) {
        super(draft);
        setDraftListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                String propName = ev.getPropertyName();
                
                IndexedPropertyChangeEvent iev = null;
                if (ev instanceof IndexedPropertyChangeEvent)
                    iev = (IndexedPropertyChangeEvent)ev;
                
                if (propName.equals("ends")) {
                    if (iev == null) {
                        // number of columns may have changed
                        fireTableStructureChanged();
                    } else { // else just end column changed
                        fireTableColumnUpdated(iev.getIndex());
                    }
                } else if (propName.equals("picks")) {
                    if (iev == null) {
                        // number of rows changed
                        fireTableDataChanged();
                    } else {
                        // just pick row changed
                        fireTableRowsUpdated(iev.getIndex(), iev.getIndex());
                    }
                } else if (propName.equals("treadles")) {
                    // a treadle changing can change all rows whose pick uses this treadle,
                    // so just update all rows
                    fireTableRowsUpdated(0, getRowCount());
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
        return draft.getPicks().size();
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
        return draft.getVisibleColor(columnIndex, rowIndex);
    }
    
    public Class<?> getColumnClass(int col) { 
        return Color.class; 
    }    
}