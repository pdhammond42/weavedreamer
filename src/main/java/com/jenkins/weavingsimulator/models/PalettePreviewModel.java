package com.jenkins.weavingsimulator.models;

import java.awt.Color;

import com.jenkins.weavingsimulator.datatypes.Palette;

/**
 * Similar to the PaletteModel used for editing, but provides a Read-only view 
 * of a palette and shows it row-oriented.
 * @author pete
 *
 */
public class PalettePreviewModel  extends javax.swing.table.AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Palette palette;
    
    /**
     * Creates a new instance of PalettePreviewModel
     * @param palette The palette displayed by this model. 
     */
    public PalettePreviewModel(Palette palette) {
        this.palette = palette;
    }
    
    public Object getValueAt(int row, int col) {
        if (row != 0)
            throw new IndexOutOfBoundsException("row out of bounds: " + row);
        return palette.getColor(col);
    }

    public int getColumnCount() {
        return palette.getNumColors();
    }

    public int getRowCount() {
        return 1;
    }

    public void setValueAt(Object value, int row, int col) {
        if (row != 0)
            throw new IndexOutOfBoundsException("row out of bounds: " + row);
        palette.setColor(col, (Color)value);
    }

    public Class<?> getColumnClass(int columnIndex) {
        return Color.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}

