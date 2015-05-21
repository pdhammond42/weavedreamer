/*
 * PaletteModel.java
 * 
 * Created on October 7, 2004, 10:45 PM
 *  
 * Copyright 2004 Adam P. Jenkins
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

import com.jenkins.weavingsimulator.datatypes.Palette;

/**
 * TableModel implementation which represents a color palette.
 * @author ajenkins
 */
public class PaletteModel extends javax.swing.table.AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Palette palette;
    
    /**
     * Creates a new instance of PaletteModel
     * @param palette The palette modified by this model.  The model will
     * add its own listener to the palette so it will be notified of changes
     * to the palette.
     */
    public PaletteModel(Palette palette) {
        this.palette = palette;
        this.palette.addPropertyChangeListener(new PaletteChangeListener());
    }
    
    public Object getValueAt(int row, int col) {
        if (col != 0)
            throw new IndexOutOfBoundsException("col out of bounds: " + col);
        return palette.getColor(row);
    }

    public int getRowCount() {
        return palette.getNumColors();
    }

    public int getColumnCount() {
        return 1;
    }

    public void setValueAt(Object value, int row, int col) {
        if (col != 0)
            throw new IndexOutOfBoundsException("col out of bounds: " + col);
        palette.setColor(row, (Color)value);
    }

    public Class<?> getColumnClass(int columnIndex) {
        return Color.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    
    private class PaletteChangeListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("selection"))
                return;
            
            if (evt instanceof IndexedPropertyChangeEvent) {
                fireTableCellUpdated(
                        ((IndexedPropertyChangeEvent)evt).getIndex(), 0);
            } else
                fireTableDataChanged();
        }
    }
}
