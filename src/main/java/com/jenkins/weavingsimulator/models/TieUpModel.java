/*
 * TieUpModel.java
 * 
 * Created on April 11, 2003, 9:27 PM
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

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jenkins.weavingsimulator.datatypes.Treadle;

/** A TableModel class for representing the treadle tie up part of the
 * weaving draft.
 *
 * @author  ajenkins
 */
public class TieUpModel extends AbstractWeavingDraftModel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of TieUpModel */
    public TieUpModel(EditingSession session) {
        super(session);
        setDraftListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                String propName = ev.getPropertyName();
                if (propName.equals("treadles")) {
                    if (ev instanceof IndexedPropertyChangeEvent) {
                        // Each treadle is a column, so a single table column has changed
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
    
    /** Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>aValue</code>.
     *
     * @param	aValue		 the new value
     * @param	rowIndex	 the row whose value is to be changed
     * @param	columnIndex 	 the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     *
     */
    private void doSetValueAt(Object aValue, int rowIndex, int columnIndex) {
    	if (isCellEditable(rowIndex, columnIndex)) {
	        Treadle treadle = draft.getTreadles().get(columnIndex);
	        int harnessId = rowIndex;
	        if (treadle.contains(harnessId))
	            // need to wrap harnessId in Integer, otherwise the remove(index) method is called 
	            // instead of the remove(Object) version.
	            treadle.remove(new Integer(harnessId));
	        else
	            treadle.add(harnessId);
    	}
    }
    
	@Override
	protected Command getSetValueCommand(final Object aValue, final int row, final int column) {
		return new Command (){
			public void execute() {
		        doSetValueAt(aValue, row, column);
			}

			public void undo() {
		        doSetValueAt(aValue, row, column);
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
        return !draft.getIsLiftplan();
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
        Treadle treadle = draft.getTreadles().get(columnIndex);
        if (treadle.contains(rowIndex))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
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
        return Boolean.class;
    }    
    
    public EditedValueProvider getEditedValueProvider() {
    	return new SetValueProvider();
    }
}
