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

package com.jenkins.weavedreamer;

import java.awt.*;
import java.awt.event.MouseEvent;

import junit.framework.TestCase;

import com.jenkins.weavedreamer.datatypes.WeavingDraft;
import com.jenkins.weavedreamer.models.AbstractWeavingDraftModel;
import com.jenkins.weavedreamer.models.Command;
import com.jenkins.weavedreamer.models.EditingSession;

public class WeavingGridControlTest extends TestCase {
	
	private MouseEvent leftDragEvent (Component source, Point pos) {
		return new MouseEvent (source, 
				MouseEvent.MOUSE_DRAGGED,
				0,
				MouseEvent.BUTTON1_DOWN_MASK,
				pos.x, pos.y,
				0, false);
	}

	private MouseEvent shiftLeftDragEvent (Component source, Point pos) {
		return new MouseEvent (source,
				MouseEvent.MOUSE_DRAGGED,
				0,
				MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.SHIFT_DOWN_MASK,
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
		AbstractWeavingDraftModel model = new TestDraftModel();
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r5c0 = new Point (5, 55);
		WeavingGridControl grid = new WeavingGridControl(model);
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
		AbstractWeavingDraftModel model = new TestDraftModel();
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r5c0 = new Point (5, 55);
		WeavingGridControl grid = new WeavingGridControl(model);
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
		AbstractWeavingDraftModel model = new TestDraftModel();
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r1c2 = new Point (25, 15);
		WeavingGridControl grid = new WeavingGridControl(model);
		grid.doDrag(leftDragEvent(grid, cell_r1c0));
		grid.doDrag(leftDragEvent(grid, cell_r1c2));
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,0));
		assertTrue((Boolean) model.getValueAt(1,1));
		assertTrue((Boolean) model.getValueAt(1,2));
	}
	
	public void testLeftDragLeft () {
		AbstractWeavingDraftModel model = new TestDraftModel();
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r1c2 = new Point (25, 15);
		WeavingGridControl grid = new WeavingGridControl(model);
		grid.doDrag(leftDragEvent(grid, cell_r1c2));
		grid.doDrag(leftDragEvent(grid, cell_r1c0));
		grid.endDrag();
		assertFalse((Boolean) model.getValueAt(0,0));
		assertTrue((Boolean) model.getValueAt(1,0));
		assertTrue((Boolean) model.getValueAt(1,1));
		assertTrue((Boolean) model.getValueAt(1,2));
	}
	
	public void testLeftDragLeftDown () {
		AbstractWeavingDraftModel model = new TestDraftModel();
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c2 = new Point (25, 15);
		Point cell_r3c0 = new Point (5, 35);
		WeavingGridControl grid = new WeavingGridControl(model);
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
		AbstractWeavingDraftModel model = new TestDraftModel();
		// Each is by default 10 pixels square.
		// Cell pixel positions 
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r5c0 = new Point (5, 55);
		WeavingGridControl grid = new WeavingGridControl(model);
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

	public void testLeftShiftDragDownRight() {
		TestDraftModel model = new TestDraftModel();
		// Each is by default 10 pixels square.
		// Cell pixel positions
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r5c2 = new Point (25, 55);
		WeavingGridControl grid = new WeavingGridControl(model);
		grid.doDrag(shiftLeftDragEvent(grid, cell_r1c0));
		grid.doDrag(shiftLeftDragEvent(grid, cell_r5c2));
		grid.endDrag();
		assertTrue (model.isCopied);
		assertEquals(0, model.startColumn);
		assertEquals(2, model.endColumn);
		assertEquals(1, model.startRow);
		assertEquals(5, model.endRow);
	}

	public void testLeftShiftDragUpLeft() {
		TestDraftModel model = new TestDraftModel();
		// Each is by default 10 pixels square.
		// Cell pixel positions
		Point cell_r1c0 = new Point (5, 15);
		Point cell_r5c2 = new Point (25, 55);
		WeavingGridControl grid = new WeavingGridControl(model);
		grid.doDrag(shiftLeftDragEvent(grid, cell_r5c2));
		grid.doDrag(shiftLeftDragEvent(grid, cell_r1c0));
		grid.endDrag();
		assertTrue (model.isCopied);
		assertEquals(2, model.startColumn);
		assertEquals(0, model.endColumn);
		assertEquals(5, model.startRow);
		assertEquals(1, model.endRow);
	}

	private class TestDraftModel extends AbstractWeavingDraftModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Object[][] model = new Object[][] {
				{false,false,false},
				{false,false,false},
				{false,false,false},
				{false,false,false},
				{false,false,false},
				{false,false,false},
				{false,false,false},
		};

		int startRow, startColumn, endRow, endColumn;
		boolean isCopied;

		public TestDraftModel() {
			super(new EditingSession(new WeavingDraft()));
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return model[rowIndex][columnIndex];
		}

		public int getRowCount() {
			return model.length;
		}

		public int getColumnCount() {
			return model[0].length;
		}
		
	    public EditedValueProvider getEditedValueProvider() {
	    	return new SetValueProvider();
	    }

		@Override
		protected Command getSetValueCommand(final Object aValue, final int row, final int column) {
			return new Command() {
				public void execute() {
					model[row][column] = aValue;
				}
				public void undo() {
				}
			};
		}

		@Override
		public void showSelection (int startRow, int startColumn, int endRow, int endColumn){
			this.startRow = startRow;
			this.startColumn = startColumn;
			this.endRow = endRow;
			this.endColumn = endColumn;
		}

		@Override
		public void copySelection() {
			isCopied = true;
		}
	}
}

	
	
	
	