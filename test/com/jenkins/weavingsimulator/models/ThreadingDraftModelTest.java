/*
 * ThreadingDraftModelTest.java
 * JUnit based test
 * 
 * Created on January 11, 2005, 10:48 AM
 *  
 * Copyright 2005 Adam P. Jenkins
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
import java.util.Arrays;

import javax.swing.table.TableModel;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.jenkins.weavingsimulator.datatypes.WeftPick;
import com.jenkins.weavingsimulator.datatypes.Treadle;
import com.jenkins.weavingsimulator.datatypes.WarpEnd;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;

/**
 *
 * @author ajenkins
 */
public class ThreadingDraftModelTest extends TestCase {
    private WeavingDraft draft;
    private ThreadingDraftModel model;
    private TestTableModelListener listener;
    
    public ThreadingDraftModelTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
        draft = new WeavingDraft("TestDraft");
        draft.setNumHarnesses(2);
        draft.getEnds().add(new WarpEnd(Color.BLUE, 0));
        draft.getEnds().add(new WarpEnd(Color.GREEN, 1));
        
        model = new ThreadingDraftModel(draft);
        
        listener = new TestTableModelListener();
        model.addTableModelListener(listener);
    }

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite(ThreadingDraftModelTest.class);
        
        return suite;
    }

    public void testGetColumnCount() {
        assertEquals(draft.getEnds().size(), model.getColumnCount());
    }

    public void testGetRowCount() {
        assertEquals(draft.getNumHarnesses(), model.getRowCount());
    }

    public void testGetValueAt() {
        for (int harness = 0; harness < model.getRowCount(); harness++) {
            for (int end = 0; end < model.getColumnCount(); end++) {
                boolean expected = 
                        draft.getEnds().get(end).getHarnessId() == harness;
                assertEquals("harness="+harness+", end="+end, 
                        Boolean.valueOf(expected), model.getValueAt(harness, end));
            }
        }
    }

    public void testSetValueAt() {
        assertEquals(0, draft.getEnds().get(0).getHarnessId());
        model.setValueAt(true, 1, 0);
        assertEquals(1, draft.getEnds().get(0).getHarnessId());
    }

    public void testIsCellEditable() {
        for (int harness = 0; harness < model.getRowCount(); harness++) {
            for (int end = 0; end < model.getColumnCount(); end++) {
                assertTrue("Cell "+harness+","+end, model.isCellEditable(harness, end));
            }
        }
    }

    public void testGetColumnClass() {
        for (int col = 0; col < model.getColumnCount(); col++)
            assertEquals("class for column "+col, Boolean.class, model.getColumnClass(col));
    }
    
    /// Test if tableListener is notified when a setValueAt is called
    public void testNotifyListenerOnSet() {
        model.setValueAt(true, 1, 0);
        TableModelTestUtils.assertTableColumnUpdateEvent(listener.event, model, 0);
    }
    
    /// Test that listener is notifed when number of harnesses changes
    public void testNotifyListenerOnHarnessesChanged() {
        draft.setNumHarnesses(4);
        
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    /// Test that listener is notifed when number of warp ends changes
    public void testNotifyListenerOnWarpChanged() {
        draft.getEnds().add(new WarpEnd(Color.RED, 1));
        
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    /// Test that listener is not notified unnecessarily
    public void testNotifyOnlyForHarnessesOrWarp() {
        // change some other properties of draft
        draft.setName("Foo");
        draft.getTreadles().add(new Treadle());
        draft.getPicks().add(new WeftPick(Color.BLUE, 0));
        
        assertNull(listener.event);
    }
    
    public void testSetAndPasteSelection() {
        draft.setEnds(Arrays.asList(
        		new WarpEnd(Color.BLUE, 0), 
        		new WarpEnd(Color.BLUE, 1), 
        		new WarpEnd(Color.BLUE, 0), 
        		new WarpEnd(Color.BLUE, 1), 
        		new WarpEnd(Color.BLUE, 0), 
        		new WarpEnd(Color.BLUE, 1)));

    	model.setValueAt(true, 0, 0);
    	model.setValueAt(true, 1, 1);
    	model.setValueAt(true, 1, 2);
    	
    	model.setSelection(0, 0, 0, 3);
    	model.pasteSelection (0, 3);
    	assertTrue((Boolean) model.getValueAt(0, 3));
    	assertTrue((Boolean) model.getValueAt(1, 4));
    	assertTrue((Boolean) model.getValueAt(1, 5));
    }
}
