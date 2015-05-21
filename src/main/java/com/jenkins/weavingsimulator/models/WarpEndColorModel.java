/*
 * ThreadColorModel.java
 * 
 * Created on April 21, 2003, 11:11 AM
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

/** A TableModel for editing warp thread colors.  It has a single row, and 
 * a column for each thread.
 *
 * @author  ajenkins
 */
public class WarpEndColorModel extends AbstractWeavingDraftModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EditingSession session;
	
    /** Creates a new instance of WarpEndColorModel */
    public WarpEndColorModel(EditingSession session) {
        super(session);
        this.session = session;
        setDraftListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                if (ev.getPropertyName().equals("ends")) {
                    if (ev instanceof IndexedPropertyChangeEvent) {
                        IndexedPropertyChangeEvent iev = (IndexedPropertyChangeEvent)ev;
                        fireTableCellUpdated(0, iev.getIndex());
                    } else {
                        fireTableStructureChanged();
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
        return 1;
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
        return end.getColor();
    }

    @Override
    protected Command getSetValueCommand(final Object aValue, final int row, final int column) {
    	return new Command (){
    		WarpEnd end = draft.getEnds().get(column);
    		Color oldColor = end.getColor();
    		public void execute() {

    			end.setColor((Color)aValue);
    		}

    		public void undo() {
    			end.setColor(oldColor);				
    		}
    	};
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
    
    public EditedValueProvider getEditedValueProvider() {
    	return new ColorEditProvider(session);
    }    
}
