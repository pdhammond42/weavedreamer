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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTable;

import com.jenkins.weavingsimulator.models.AbstractWeavingDraftModel;
import com.jenkins.weavingsimulator.models.WeavingPatternCellModel;

/** GridControl is a specialized JTable in which all cells are square.  Instead
 * of setting setting the row height and column width, you set the squareWidth 
 * property.
 * 
 * This class implements drag behaviour to select and operate on multiple cells
 * at once. There are two drag modes: with and without shift, both on button 1.
 * Without shift, it displays a preview line of the drag path, then on end drag 
 * requests the model to set all cells crossed by the path. With shift, it updates 
 * the model on each drag with the selection. It is up to the model to implement
 * the selection. The reason for the difference is that setting the cells directly 
 * leaves cells changed even if the drag moves away from them. We probably could
 * provide some sort of restore, but I can't see how so this works.
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
    
    private boolean allowDrag = true;
    /** 
     * Sets whether dragging will be allowed in this grid (default is on).
     * @param allow Allow dragging if true.
     */
    public void setAllowDrag(boolean allow) {
    	allowDrag = allow;
    }
        
    private void init() {
        setDefaultRenderer(Color.class, new ColorRenderer());
        setDefaultRenderer(WeavingPatternCellModel.class, new DraftRenderer());
        
        setDefaultRenderer(Boolean.class, new BooleanRenderer());
        setDefaultEditor(Boolean.class, null);
        
        // Default is to do Boolean things. Caller can override this.
        editedValueProvider = new EditedValueProvider () {
        	public Object getValue() {
        		return true;
        	}
        };
        
        setCellSelectionEnabled(false);
        
        setRowHeight(squareWidth);
        setGridColor(Color.GRAY);
        
        addMouseMotionListener (new MouseMotionListener() {
			public void mouseDragged(MouseEvent arg0) {
				if (allowDrag) doDrag (arg0);
			}
			public void mouseMoved(MouseEvent arg0) {
				final RowColumn p = toCell(arg0.getPoint());
				if (getModel() instanceof AbstractWeavingDraftModel) { 
					((AbstractWeavingDraftModel)getModel()).setCurrentCell(p.row, p.column);
				}
			}
			});
        addMouseListener (new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				final RowColumn p = toCell(e.getPoint());	
				if (e.getClickCount() == 1 && 
						e.getButton() == MouseEvent.BUTTON1 && 
						editedValueProvider != null) {
					setValueAt (editedValueProvider.getValue(), p.row, p.column);
				} else if (e.getClickCount() == 1 && 
						e.getButton() == MouseEvent.BUTTON3) {
					((AbstractWeavingDraftModel)getModel()).pasteSelection(p.row, p.column);
				}
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
        
    Point dragStart = null;
    Point dragEnd = null;
    boolean isShiftDrag = false;
	private EditedValueProvider editedValueProvider;
    
	/** Overridden to draw the preview line during drag operations. 
	 * 
	 */
    public void paintComponent (Graphics g) {
    	super.paintComponent(g);
    	Graphics2D g2 = (Graphics2D) g;
    	if (dragStart != null && dragEnd != null && !isShiftDrag) {
    		g2.setColor(Color.darkGray);
    		g2.setStroke (new BasicStroke(2));
    		g2.drawLine(dragStart.x, dragStart.y, dragEnd.x, dragEnd.y);
    	}
    }
    
    public void doDrag (MouseEvent event) {
    	if ((event.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
        	isShiftDrag = (event.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != 0;
	    	if (dragStart == null) {
				dragStart = event.getPoint();
	    	} else {
	    		dragEnd = event.getPoint();
	    		final RowColumn start = toCell(dragStart);
	    		final RowColumn end = toCell(dragEnd);
	    		AbstractWeavingDraftModel model = null;
	    		if (getModel() instanceof AbstractWeavingDraftModel) 
	    			model = (AbstractWeavingDraftModel)getModel();
	    		if (model != null) 
	    			model.setCurrentCell(
	    				start.row, start.column, 
    					end.row, end.column);		
	    		if (isShiftDrag) {
	    			if (model != null) model.showSelection(
	    					start.row, start.column, 
	    					end.row, end.column);		
	    		} else {
	    			repaint();
	    		}
	    	}	
    	}
    }
    
    public void endDrag () {
    	if (dragStart != null && dragEnd != null) {
    		if (isShiftDrag) {
    			if (getModel() instanceof AbstractWeavingDraftModel) { 
    				((AbstractWeavingDraftModel)getModel()).copySelection();
    				((AbstractWeavingDraftModel)getModel()).showSelection(0,0,0,0);
    			}
    		} else {
    			final RowColumn start = toCell(dragStart);
    			final RowColumn end = toCell(dragEnd);
    			final int diffX = end.column - start.column;
    			final int diffY = end.row - start.row;
    			final int steps = Math.max(Math.abs(diffX), Math.abs(diffY))+1;
    			if (editedValueProvider != null) {
    				setValueAt (editedValueProvider.getValue(), start.row, start.column);
    				for (int i = 1; i < steps; i++) {
    					setValueAt (editedValueProvider.getValue(), start.row + i*diffY/(steps-1), start.column+i*diffX/(steps-1));
    				}	
    			}	
    		}
    	}
    	dragStart = null;
    	dragEnd = null;
    }
    
    /**	 
     * This is nothing to do with conventional JTable cell editing.
     * It returns a value to be set into a selected cell. The caller is 
     * responsible for setting up an object that returns the appropriate type for the
     * model's setValueAt method.
     * The GridControl holds a single instance of this interface. 
     * When a cell is clicked, or dragged over, its value is set according to the
     * value returned. 
     *  
     */
    public interface EditedValueProvider {
    	Object getValue();
    }
    
    /** Provide an object that will return the new value of edited cells.
     * Provide a null reference to disable editing.
     * @param p the provider of values.
     */
    public void setEditValueProvider (EditedValueProvider p) {
    	editedValueProvider = p;
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
    private class RowColumn {
    	public final int row;
    	public final int column;
    	public RowColumn (int row, int column) {
    		this.row = row;
    		this.column = column;
    	}
    }
    
    private RowColumn toCell (Point p) {
    	return new RowColumn (p.y / squareWidth, p.x / squareWidth);
    }
 }
