package com.jenkins.weavingsimulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jenkins.weavingsimulator.models.AbstractWeavingDraftModel;

/**
 * This class is a specialization of GridControl that implements editing
 * behaviours for AbstractWeavingDraftModel, including drag  behaviour to select 
 * and operate on multiple cells at once. 
 * There are two drag modes: with and without shift, both on button 1.
 * Without shift, it displays a preview line of the drag path, then on end drag 
 * requests the model to set all cells crossed by the path. With shift, it updates 
 * the model on each drag with the selection. It is up to the model to implement
 * the selection. The reason for the difference is that setting the cells directly 
 * leaves cells changed even if the drag moves away from them. We probably could
 * provide some sort of restore, but I can't see how so this works.
 *
 * @author  ajenkins
 */
public class WeavingGridControl extends GridControl {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** To save lots of downcasting from TableModel. */
    private AbstractWeavingDraftModel model;
    Point dragStart = null;
    Point dragEnd = null;
    boolean isShiftDrag = false;
    
    public WeavingGridControl() {
    	super();
    	init();
    }
    
    public WeavingGridControl(AbstractWeavingDraftModel model) {
    	super(model);
    	this.model = model;
    	this.editedValueProvider = model.getEditedValueProvider();
    	init();
    }
    
    private void init () {
        addMouseMotionListener (new MouseMotionListener() {
			public void mouseDragged(MouseEvent arg0) {
				if (allowDrag) doDrag (arg0);
			}
			public void mouseMoved(MouseEvent arg0) {
				final RowColumn p = toCell(arg0.getPoint());
				model.setCurrentCell(p.row, p.column);
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
				if (e.isPopupTrigger()) {
					showContextMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				endDrag();
				if (e.isPopupTrigger()) {
					showContextMenu(e);
				}
			}
        
        });        
    }
    
    private boolean allowDrag = true;
    /** 
     * Sets whether dragging will be allowed in this grid (default is on).
     * @param allow Allow dragging if true.
     */
    public void setAllowDrag(boolean allow) {
    	allowDrag = allow;
    }
    
    /** We would like to constrain the model to be instanceof AbstractWeavingDraftModel,
     * but JTable creates a default model so we can't.
     * 
     */
    public void setModel (javax.swing.table.TableModel model) {
		super.setModel(model);
    	if (model instanceof AbstractWeavingDraftModel) {
    		this.model = (AbstractWeavingDraftModel)model;
    		this.editedValueProvider = ((AbstractWeavingDraftModel) model).getEditedValueProvider();
    	} else {
    		this.model = null;
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
				model.setCurrentCell(
						start.row, start.column, 
						end.row, end.column);		
				if (isShiftDrag) {
					model.showSelection(
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
    			model.copySelection();
    		} else {
    			final RowColumn start = toCell(dragStart);
    			final RowColumn end = toCell(dragEnd);
    			final int diffX = end.column - start.column;
    			final int diffY = end.row - start.row;
    			final int steps = Math.max(Math.abs(diffX), Math.abs(diffY))+1;
    			if (editedValueProvider != null) {
    				setValueAt (editedValueProvider.getValue(start.row, start.column), start.row, start.column);
    				try {
    					for (int i = 1; i < steps; i++) {
    						setValueAt (editedValueProvider.getValue(start.row + i*diffY/(steps-1), start.column+i*diffX/(steps-1)), start.row + i*diffY/(steps-1), start.column+i*diffX/(steps-1));
    					}	
    				} catch (IndexOutOfBoundsException e) {
    					// Must have been released outside the grid. 
    					// Ignore, we've done all the cells we can.
    				}	
    			}	
    		}
    	}
    	dragStart = null;
    	dragEnd = null;
    }

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

    private void showContextMenu(MouseEvent e) {
    	final RowColumn p = toCell(e.getPoint());	
    	new PasteMenu(model, p.row, p.column)
    		.show(this, e.getX(), e.getY());	
    }	
}
