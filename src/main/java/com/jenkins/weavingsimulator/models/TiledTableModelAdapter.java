/*
 * TiledTableModelAdapter.java
 * 
 * Created on April 11, 2003, 7:09 PM
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

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

/** This class is a wrapper around another TableModel, which creates a tiled
 * view of the wrapped TableModel.  That is, the table represented by the
 * wrapped TableModel is tiled repeatedly horizontally and vertically, as 
 * many times as indicated by the tiledColumnCount and tiledRowCount properties.
 * All cell access functions are translated to calls on the underlying model.
 *
 * @author  ajenkins
 */
public class TiledTableModelAdapter extends javax.swing.table.AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TableModel model;
    
    /** Holds value of property tiledRowCount. */
    private int tiledRowCount;
    
    /** Holds value of property tiledColumnCount. */
    private int tiledColumnCount;
    
    /** Creates a new instance of TiledTableModelAdapter.  tiledRowCount 
     * and tiledColumnCount default to 1. */
    public TiledTableModelAdapter(TableModel model) {
        this.model = model;
        model.addTableModelListener(new DelegatingTableListener());
        tiledRowCount = tiledColumnCount = 1;
    }
    
    /** Creates a new instance of TiledTableModel, which wraps model, and
     * with tiledRows for tiledRowCount, and tiledColumns for tiledColumnCount.
     */
    public TiledTableModelAdapter(TableModel model, int tiledRows, int tiledColumns) {
        this.model = model;
        model.addTableModelListener(new DelegatingTableListener());
        tiledRowCount = tiledRows;
        tiledColumnCount = tiledColumns;
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
        return model.getColumnClass(getRealColumnIndex(columnIndex));
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
        return model.getColumnCount() * getTiledColumnCount();
    }
    
    /** Returns the name of the column at <code>columnIndex</code>.  This is used
     * to initialize the table's column header name.  Note: this name does
     * not need to be unique; two columns in a table can have the same name.
     *
     * @param	columnIndex	the index of the column
     * @return  the name of the column
     *
     */
    public String getColumnName(int columnIndex) {
        return model.getColumnName(getRealColumnIndex(columnIndex));
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
        return model.getRowCount() * getTiledRowCount();
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
        return model.getValueAt(getRealRowIndex(rowIndex),
            getRealColumnIndex(columnIndex));
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
        return model.isCellEditable(getRealRowIndex(rowIndex),
            getRealColumnIndex(columnIndex));
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
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        model.setValueAt(aValue, getRealRowIndex(rowIndex),
            getRealColumnIndex(columnIndex));
    }
    
    /** 
     * Getter for property tiledRowCount. tiledRowCount specifies the number of
     * times the underlying TableModel will be tiled vertically.  So, the 
     * rowCount of this TiledTableModel will be tiledRowCount * number of rows
     * in the underlying TableModel.
     * @return Value of property tiledRowCount.
     *
     */
    public int getTiledRowCount() {
        return this.tiledRowCount;
    }
    
    /** Setter for property tiledRowCount.
     * @param tiledRowCount New value of property tiledRowCount.
     * @see #getTiledRowCount
     */
    public void setTiledRowCount(int tiledRowCount) {
        this.tiledRowCount = tiledRowCount;
        fireTableStructureChanged();
    }
    
    /** Getter for property tiledColumnCount.
     * @return Value of property tiledColumnCount.
     *
     */
    public int getTiledColumnCount() {
        return this.tiledColumnCount;
    }
    
    /** Setter for property tiledColumnCount.
     * @param tiledColumnCount New value of property tiledColumnCount.
     *
     */
    public void setTiledColumnCount(int tiledColumnCount) {
        this.tiledColumnCount = tiledColumnCount;
        fireTableStructureChanged();
    }
    
    /** Computes which column col corresponds to in the underlying 
     * TableModel. */
    private int getRealColumnIndex(int col) {
        return col % model.getColumnCount();
    }
    
    /** Computes which row row corresponds to in the underlying
     * TableModel. */
    private int getRealRowIndex(int row) {
        return row % model.getRowCount();
    }
    
    /** Getter for property model.
     * @return The underlying TableModel
     *
     */
    public TableModel getModel() {
        return model;
    }
    
    private class DelegatingTableListener implements javax.swing.event.TableModelListener {
        
        /** This fine grain notification tells listeners the exact range
         * of cells, rows, or columns that changed.
         *
         */
        public void tableChanged(TableModelEvent e) {
            fireTableStructureChanged();
        }
        
    }    
    
}
