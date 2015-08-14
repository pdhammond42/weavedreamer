/*
 * GridControl.java
 * 
 * Created on April 10, 2003, 11:09 PM
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


package com.jenkins.weavingsimulator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.KeyStroke;

import com.jenkins.weavingsimulator.models.AbstractWeavingDraftModel;
import com.jenkins.weavingsimulator.models.WeavingPatternCellModel;

/** 
 * GridControl is a specialized JTable in which all cells are square.  Instead
 * of setting setting the row height and column width, you set the squareWidth 
 * property.
 * 
 */
public class GridControl extends JTable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Holds value of property squareWidth. */
    private int squareWidth = 10;
    
    /** Creates a new instance of GridControl */
    public GridControl() {
        init();
    }
    
    public GridControl(javax.swing.table.TableModel model) {
        super(model);
        init();
    }
        
    private void init() {
        setDefaultRenderer(Color.class, new ColorRenderer());
        setDefaultRenderer(WeavingPatternCellModel.class, new DraftRenderer());
        
        setDefaultRenderer(Boolean.class, new BooleanRenderer());
        setDefaultEditor(Boolean.class, null);
                
        setCellSelectionEnabled(false);
        
        setRowHeight(squareWidth);
        setGridColor(Color.GRAY);
        
        addMouseListener (new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				final RowColumn p = toCell(e.getPoint());	
				if (e.getClickCount() == 1 && 
						e.getButton() == MouseEvent.BUTTON1 && 
						editedValueProvider != null) {
					setValueAt (editedValueProvider.getValue(p.row, p.column), p.row, p.column);
				} 
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}    
        });
        
        // Prevent the grid from handling arrow events, otherwise the scroll panel behaviour get really confusing.
        for (String key: new String[] {"UP","DOWN","LEFT","RIGHT"}){
        	getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(key), "none");
        }
    }
    
    
    /** Getter for property squareWidth.
     * @return Value of property squareWidth.
     *
     */
    public int getSquareWidth() {
        return this.squareWidth;
    }
    
    /** Setter for property squareWidth.
     * @param squareWidth New value of property squareWidth.
     *
     */
    public void setSquareWidth(int squareWidth) {
        for (int i = 0; i < getModel().getColumnCount(); i++) {
            javax.swing.table.TableColumn col = getColumnModel().getColumn(i);
            // Order is important here...
            if (squareWidth < this.squareWidth) {
            	col.setMinWidth(squareWidth);
            	col.setMaxWidth(squareWidth);
            } else {
                col.setMaxWidth(squareWidth);
                col.setMinWidth(squareWidth);
            }
            col.setWidth(squareWidth);
        }
        setRowHeight(squareWidth);
        this.squareWidth = squareWidth;
    }
    
    /** Invoked when a column is added to the table column model.
     *
     * @see javax.swing.event.TableColumnModelListener
     *
     */
    public void columnAdded(javax.swing.event.TableColumnModelEvent e) {
        super.columnAdded(e);
        javax.swing.table.TableColumn column = 
            getColumnModel().getColumn(e.getToIndex());
        column.setMinWidth(squareWidth);
        column.setMaxWidth(squareWidth);
    }    
        
	protected AbstractWeavingDraftModel.EditedValueProvider editedValueProvider = null;
    
    private static class ColorRenderer implements javax.swing.table.TableCellRenderer 
    {
        javax.swing.JLabel label = new javax.swing.JLabel();
        
        ColorRenderer() {
            label.setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) 
        {
            label.setBackground((Color)value);
            if (isSelected)
                label.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            else
                label.setBorder(null);
            return label;
        }        
    }
    
    private static class DraftRenderer implements javax.swing.table.TableCellRenderer 
    {
        javax.swing.JLabel label = new javax.swing.JLabel();
        
        DraftRenderer() {
            label.setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) 
        {
        	WeavingPatternCellModel cell = (WeavingPatternCellModel)value;
            label.setBackground(cell.color());
            label.setBorder(javax.swing.BorderFactory.createMatteBorder(
            		0, 
            		0,	
            		cell.nextRowBorder() ? 1 : 0,
               		cell.nextColumnBorder() ? 1 : 0,
            		Color.black));
            return label;
        }        
    }
    
    private static class BooleanRenderer implements javax.swing.table.TableCellRenderer 
    {
        javax.swing.JLabel label = new javax.swing.JLabel();
        
        BooleanRenderer() {
            label.setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, 
            Object value, boolean isSelected, boolean hasFocus, int row, int column) 
        {
            if ((Boolean)value)
                label.setBackground(Color.BLACK);
            else
                label.setBackground(Color.WHITE);
            return label;
        }        
    }
    
    // Helpers for dealing with point to cell conversions.
    protected class RowColumn {
    	public final int row;
    	public final int column;
    	public RowColumn (int row, int column) {
    		this.row = row;
    		this.column = column;
    	}
    }
    
    protected RowColumn toCell (Point p) {
    	return new RowColumn (p.y / squareWidth, p.x / squareWidth);
    }
    
 }
