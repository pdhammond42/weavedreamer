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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;


/** GridControl is a specialized JTable in which all cells are square.  Instead
 * of setting setting the row height and column width, you set the squareWidth 
 * property.
 *
 * @author  ajenkins
 */
public class GridControl extends JTable {
    
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
        
        setDefaultRenderer(Boolean.class, new BooleanRenderer());
        setDefaultEditor(Boolean.class, new BooleanEditor());
        
        setRowHeight(squareWidth);
        setGridColor(Color.GRAY);
        
        addMouseMotionListener (new MouseMotionListener() {
			public void mouseDragged(MouseEvent arg0) {
				doDrag (arg0.getPoint());
			}
			public void mouseMoved(MouseEvent arg0) {
			}
			});
        addMouseListener (new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
				endDrag();
			}
        
        });
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
        this.squareWidth = squareWidth;
        for (int i = 0; i < getModel().getColumnCount(); i++) {
            javax.swing.table.TableColumn col = getColumnModel().getColumn(i);
            col.setMinWidth(squareWidth);
            col.setMaxWidth(squareWidth);
        }
        setRowHeight(squareWidth);
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
    
    /** Invoked when editing is finished. The changes are saved and the
     * editor is discarded.  This implementation is present so that if 
     * a block of cells are selected, then when editing finishes, all the 
     * selected cells get set to edit value instead of just the edited cell.
     *
     * @param  e  the event received
     * @see javax.swing.event.CellEditorListener
     *
     */
    public void editingStopped(javax.swing.event.ChangeEvent e) {
        javax.swing.table.TableCellEditor editor = getCellEditor();
        Object value = null;
        if (editor != null)
            value = editor.getCellEditorValue();
        
        super.editingStopped(e);
        
        if (getCellSelectionEnabled()) {
            for (int row = 0; row < getRowCount(); row++) {
                for (int col = 0; col < getColumnCount(); col++) {
                    if (isCellSelected(row, col))
                        setValueAt(value, row, col);
                }
            }
        }
    }
    
    Point dragStart = null;
    Point dragEnd = null;
    
    public void paintComponent (Graphics g) {
    	super.paintComponent(g);
    	if (dragStart != null && dragEnd != null) {
    		g.setColor(Color.black);
    		g.drawLine(dragStart.x, dragStart.y, dragEnd.x, dragEnd.y);
    	}
    }
    public void doDrag (Point point) {
    	if (dragStart == null) {
			dragStart = point;
    	} else {
    		dragEnd = point;
    		repaint();
    	}
    }
    
    public void endDrag () {
    	if (dragStart != null && dragEnd != null) {
    		final int cellX0 = dragStart.x / squareWidth;
    		final int cellY0 = dragStart.y / squareWidth;
    		final int cellX1 = dragEnd.x / squareWidth;
    		final int cellY1 = dragEnd.y / squareWidth;
    		final int diffX = cellX1 - cellX0;
    		final int diffY = cellY1 - cellY0;
    		final int steps = Math.max(Math.abs(diffX), Math.abs(diffY))+1;
    		for (int i = 0; i < steps; i++) {
    			setValueAt (true, cellY0 + i*diffY/(steps-1), cellX0+i*diffX/(steps-1));
    		}	
    	}
    	dragStart = null;
    	dragEnd = null;
    }
    
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
    
    private static class BooleanEditor 
            extends AbstractCellEditor
            implements TableCellEditor
    {
        private JComponent editorComponent;
        private boolean currentValue;
        
        BooleanEditor() {
            JButton b = new JButton("");
            editorComponent = b;
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    currentValue = !currentValue;
                    fireEditingStopped();
                }
            });
        }
        
        public Object getCellEditorValue() {
            return currentValue;
        }

        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) 
        {
            currentValue = (Boolean)value;
            if (currentValue)
                editorComponent.setBackground(Color.BLACK);
            else
                editorComponent.setBackground(Color.WHITE);
            return editorComponent;
        }
       
    }
}
