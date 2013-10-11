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

import java.awt.Point;

import junit.framework.Assert;
import junit.framework.TestCase;

public class GridControlTest extends TestCase {
	public void testDragDown () {
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
		grid.doDrag(cell_r1c0);
		grid.doDrag(cell_r5c0);
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,0));
		assertTrue((Boolean) model.getValueAt(2,0));
		assertTrue((Boolean) model.getValueAt(3,0));
		assertTrue((Boolean) model.getValueAt(4,0));
		assertTrue((Boolean) model.getValueAt(5,0));
		assertFalse((Boolean) model.getValueAt(6,0));
	}

	public void testDragUp () {
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
		grid.doDrag(cell_r5c0);
		grid.doDrag(cell_r1c0);
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,0));
		assertTrue((Boolean) model.getValueAt(2,0));
		assertTrue((Boolean) model.getValueAt(3,0));
		assertTrue((Boolean) model.getValueAt(4,0));
		assertTrue((Boolean) model.getValueAt(5,0));
		assertFalse((Boolean) model.getValueAt(6,0));
	}
	
	public void testDragRight () {
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
		grid.doDrag(cell_r1c0);
		grid.doDrag(cell_r1c2);
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,0));
		assertTrue((Boolean) model.getValueAt(1,1));
		assertTrue((Boolean) model.getValueAt(1,2));
	}
	
	public void testDragLeft () {
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
		grid.doDrag(cell_r1c2);
		grid.doDrag(cell_r1c0);
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,0));
		assertTrue((Boolean) model.getValueAt(1,1));
		assertTrue((Boolean) model.getValueAt(1,2));
	}
	public void testDragLeftDown () {
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
		grid.doDrag(cell_r1c2);
		grid.doDrag(cell_r3c0);
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,2));
		assertTrue((Boolean) model.getValueAt(2,1));
		assertTrue((Boolean) model.getValueAt(3,0));
	}
	
}
