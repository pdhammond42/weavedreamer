/*
 * WarpThreadColorModelTest.java
 * JUnit based test
 * 
 * Created on January 14, 2005, 1:45 PM
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
public class WarpThreadColorModelTest extends TestCase {
    private WeavingDraft draft;
    private TableModel model;
    private TestTableModelListener listener;
    
    public WarpThreadColorModelTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
        draft = new WeavingDraft("TestDraft");
        draft.setNumHarnesses(2);
        draft.setEnds(Arrays.asList(
                new WarpEnd(Color.RED, 0),
                new WarpEnd(Color.GREEN, 1),
                new WarpEnd(Color.BLUE, 0)));
        
        model = new WarpEndColorModel(new EditingSession(draft));
        
        listener = new TestTableModelListener();
        model.addTableModelListener(listener);
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(WarpThreadColorModelTest.class);
        
        return suite;
    }

    public void testGetColumnCount() {
        assertEquals(draft.getEnds().size(), model.getColumnCount());
    }

    public void testGetRowCount() {
        assertEquals(1, model.getRowCount());
    }

    public void testGetValueAt() {
        for (int c = 0; c < model.getColumnCount(); c++) {
            assertEquals("col="+c, 
                    draft.getEnds().get(c).getColor(), model.getValueAt(0, c));
        }
    }

    public void testSetValueAt() {
        model.setValueAt(Color.YELLOW, 0, 1);
        assertEquals(Color.YELLOW, draft.getEnds().get(1).getColor());
    }

    public void testIsCellEditable() {
        for (int c = 0; c < model.getColumnCount(); c++) {
            assertTrue("col="+c, model.isCellEditable(0, c));
        }
    }

    public void testGetColumnClass() {
        for (int c = 0; c < model.getColumnCount(); c++) {
            assertEquals("col="+c, Color.class, model.getColumnClass(c));
        }
    }
    
    public void testNotifyOnThreadColorSet() {
        draft.getEnds().get(1).setColor(Color.YELLOW);
        TableModelTestUtils.assertOneTableCellUpdateEvent(listener.event, 0, 1);
    }
    
    public void testNotifyOnThreadsChanged() {
        draft.getEnds().add(new WarpEnd(Color.YELLOW, 0));
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
        
    public void testNoExtraNotifications() {
        draft.setName("NewName");
        draft.setNumHarnesses(3);
        draft.getTreadles().add(new Treadle());
        draft.setPicks(Arrays.asList(new WeftPick(Color.BLUE, 1, 0)));
        
        assertNull(listener.event);
    }
}
