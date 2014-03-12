/*
 * GridControlTest.java
 * JUnit based test
 *
 *  
 * Copyright 2013 Peter Hammond
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

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import junit.framework.TestCase;

public class GridControlTest extends TestCase {
	
	private MouseEvent leftDragEvent (Component source, Point pos) {
		return new MouseEvent (source, 
				MouseEvent.MOUSE_DRAGGED,
				0,
				MouseEvent.BUTTON1_DOWN_MASK,
				pos.x, pos.y,
				0, false);
	}
	
	private MouseEvent rightDragEvent (Component source, Point pos) {
		return new MouseEvent (source, 
				MouseEvent.MOUSE_DRAGGED,
				0,
				MouseEvent.BUTTON3_DOWN_MASK,
				pos.x, pos.y,
				0, false);
	}
	
	public void testLeftDragDown () {
		javax.swing.table.TableModel model = new javax.swing.table.DefaultTableModel(
				new Object[][] {
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						}, 
				new Object[]{0,0,0});
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r5c0 = new Point (5, 55);
		GridControl grid = new GridControl(model);
		grid.doDrag(leftDragEvent(grid, cell_r1c0));
		grid.doDrag(leftDragEvent(grid, cell_r5c0));
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,0));
		assertTrue((Boolean) model.getValueAt(2,0));
		assertTrue((Boolean) model.getValueAt(3,0));
		assertTrue((Boolean) model.getValueAt(4,0));
		assertTrue((Boolean) model.getValueAt(5,0));
		assertFalse((Boolean) model.getValueAt(6,0));
	}

	public void testLeftDragUp () {
		javax.swing.table.TableModel model = new javax.swing.table.DefaultTableModel(
				new Object[][] {
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						}, 
				new Object[]{0,0,0});
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r5c0 = new Point (5, 55);
		GridControl grid = new GridControl(model);
		grid.doDrag(leftDragEvent(grid, cell_r5c0));
		grid.doDrag(leftDragEvent(grid, cell_r1c0));
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,0));
		assertTrue((Boolean) model.getValueAt(2,0));
		assertTrue((Boolean) model.getValueAt(3,0));
		assertTrue((Boolean) model.getValueAt(4,0));
		assertTrue((Boolean) model.getValueAt(5,0));
		assertFalse((Boolean) model.getValueAt(6,0));
	}
	
	public void testLeftDragRight () {
		javax.swing.table.TableModel model = new javax.swing.table.DefaultTableModel(
				new Object[][] {
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						}, 
				new Object[]{0,0,0});
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r1c2 = new Point (25, 15);
		GridControl grid = new GridControl(model);
		grid.doDrag(leftDragEvent(grid, cell_r1c0));
		grid.doDrag(leftDragEvent(grid, cell_r1c2));
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,0));
		assertTrue((Boolean) model.getValueAt(1,1));
		assertTrue((Boolean) model.getValueAt(1,2));
	}
	
	public void testLeftDragLeft () {
		javax.swing.table.TableModel model = new javax.swing.table.DefaultTableModel(
				new Object[][] {
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						}, 
				new Object[]{0,0,0});
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r1c2 = new Point (25, 15);
		GridControl grid = new GridControl(model);
		grid.doDrag(leftDragEvent(grid, cell_r1c2));
		grid.doDrag(leftDragEvent(grid, cell_r1c0));
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,0));
		assertTrue((Boolean) model.getValueAt(1,1));
		assertTrue((Boolean) model.getValueAt(1,2));
	}
	
	public void testLeftDragLeftDown () {
		javax.swing.table.TableModel model = new javax.swing.table.DefaultTableModel(
				new Object[][] {
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						}, 
				new Object[]{0,0,0});
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c2 = new Point (25, 15);
		Point cell_r3c0 = new Point (5, 35);
		GridControl grid = new GridControl(model);
		grid.doDrag(leftDragEvent(grid, cell_r1c2));
		grid.doDrag(leftDragEvent(grid, cell_r3c0));
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,2));
		assertTrue((Boolean) model.getValueAt(2,1));
		assertTrue((Boolean) model.getValueAt(3,0));
	}
	
	public void testRightDragDown () {
		// Currently right drag does nothing.
		javax.swing.table.TableModel model = new javax.swing.table.DefaultTableModel(
				new Object[][] {
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						{false,false,false},
						}, 
				new Object[]{0,0,0});
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r5c0 = new Point (5, 55);
		GridControl grid = new GridControl(model);
		grid.doDrag(rightDragEvent(grid, cell_r1c0));
		grid.doDrag(rightDragEvent(grid, cell_r5c0));
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertFalse((Boolean) model.getValueAt(1,0));
		assertFalse((Boolean) model.getValueAt(2,0));
		assertFalse((Boolean) model.getValueAt(3,0));
		assertFalse((Boolean) model.getValueAt(4,0));
		assertFalse((Boolean) model.getValueAt(5,0));
		assertFalse((Boolean) model.getValueAt(6,0));
	}

}