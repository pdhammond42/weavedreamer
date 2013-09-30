/*
 * TreadlingDraftModelTest.java
 * JUnit based test
 * 
 * Created on January 12, 2005, 1:17 AM
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

import com.jenkins.weavingsimulator.datatypes.WeftPick;
import com.jenkins.weavingsimulator.datatypes.Treadle;
import com.jenkins.weavingsimulator.datatypes.WarpEnd;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import java.awt.Color;
import java.util.Arrays;
import javax.swing.table.TableModel;
import junit.framework.TestCase;

/**
 *
 * @author ajenkins
 */
public class TreadlingDraftModelTest extends TestCase {
    private WeavingDraft draft;
    private TableModel model;
    private TestTableModelListener listener;
    
    public TreadlingDraftModelTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
        draft = new WeavingDraft("TestDraft");
        draft.setNumHarnesses(2);
        draft.setTreadles(Arrays.asList(new Treadle(), new Treadle()));
        draft.setPicks(Arrays.asList(
                new WeftPick(Color.BLACK, 0), 
                new WeftPick(Color.WHITE, 1),
                new WeftPick(Color.BLUE, 0)));
        
        model = new TreadlingDraftModel(draft);
        listener = new TestTableModelListener();
        model.addTableModelListener(listener);
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(TreadlingDraftModelTest.class);
        
        return suite;
    }

    public void testGetColumnCount() {
        assertEquals(draft.getTreadles().size(), model.getColumnCount());
    }

    public void testGetRowCount() {
        assertEquals(draft.getPicks().size(), model.getRowCount());
    }

    public void testGetValueAt() {
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                assertEquals("row="+row+",col="+col,
                        draft.getPicks().get(row).getTreadleId() == col,
                        model.getValueAt(row, col));
            }
        }
    }

    public void testSetValueAt() {
        assertEquals(1, draft.getPicks().get(1).getTreadleId());
        model.setValueAt(true, 1, 0);
        assertEquals(0, draft.getPicks().get(1).getTreadleId());
    }

    public void testIsCellEditable() {
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                assertTrue("row="+row+",col="+col, model.isCellEditable(row, col));
            }
        }
    }

    public void testGetColumnClass() {
        for (int col = 0; col < model.getColumnCount(); col++) {
            assertEquals("col="+col, Boolean.class, model.getColumnClass(col));
        }
    }
    
    public void testNotifyListenerOnSet() {
        model.setValueAt(true, 1, 0);
        TableModelTestUtils.assertTableRowUpdateEvent(listener.event, model, 1);
    }
    
    public void testNotifyOnTreadlesChanged() {
        draft.getTreadles().add(new Treadle());
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    public void testNotifyOnStepsChanged() {
        draft.getPicks().add(new WeftPick(Color.RED, 1));
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    public void testNoExtraNotifications() {
        draft.setNumHarnesses(5);
        draft.setName("somename");
        draft.getEnds().add(new WarpEnd(Color.BLACK, 1));
        assertNull(listener.event);
    }
}
