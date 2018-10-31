/*
 * TieUpModelTest.java
 * JUnit based test
 * 
 * Created on January 11, 2005, 3:53 PM
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

package com.jenkins.weavedreamer.models;

import com.jenkins.weavedreamer.datatypes.WeftPick;
import com.jenkins.weavedreamer.datatypes.Treadle;
import com.jenkins.weavedreamer.datatypes.WarpEnd;
import com.jenkins.weavedreamer.datatypes.WeavingDraft;
import java.awt.Color;
import java.util.Arrays;
import javax.swing.table.TableModel;
import junit.framework.TestCase;

/**
 * 
 * @author ajenkins
 */
public class TieUpModelTest extends TestCase {
    private WeavingDraft draft;

    private TableModel model;

    private TestTableModelListener listener;

    public TieUpModelTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
        draft = new WeavingDraft("TestDraft");
        draft.setNumHarnesses(3);
        draft.getTreadles().add(new Treadle(Arrays.asList(0, 2)));
        draft.getTreadles().add(new Treadle(Arrays.asList(1)));

        model = new TieUpModel(new EditingSession(draft));
        listener = new TestTableModelListener();
        model.addTableModelListener(listener);
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(
                TieUpModelTest.class);

        return suite;
    }

    public void testGetColumnCount() {
        assertEquals(draft.getTreadles().size(), model.getColumnCount());
    }

    public void testGetRowCount() {
        assertEquals(draft.getNumHarnesses(), model.getRowCount());
    }

    /**
     * getValueAt should return true if the harness corresponding to the row, is
     * connected to the treadle corresponding to the column
     */
    public void testGetValueAt() {
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                assertEquals("row=" + row + ",col=" + col, draft.getTreadles()
                        .get(col).contains(row), model.getValueAt(row, col));
            }
        }
    }

    public void testSetValueAt() {
        assertFalse(draft.getTreadles().get(0).contains(1));
        model.setValueAt(true, 1, 0);
        assertTrue(draft.getTreadles().get(0).contains(1));
    }

    public void testIsCellEditable() {
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                assertTrue("row=" + row + ",col=" + col, model.isCellEditable(
                        row, col));
            }
        }
    }

    public void testGetColumnClass() {
        for (int col = 0; col < model.getColumnCount(); col++) {
            assertEquals("class for column " + col, Boolean.class, model
                    .getColumnClass(col));
        }
    }

    public void testNotifyListenerOnSet() {
        draft.getTreadles().get(0).add(1);
        TableModelTestUtils.assertTableColumnUpdateEvent(listener.event, model,
                0);
    }

    public void testNotifyOnTreadlesChanged() {
        draft.getTreadles().add(new Treadle());
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event,
                model);
    }

    public void testNotifyOnShuttlesChanged() {
        draft.setNumHarnesses(4);
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event,
                model);
    }

    public void testNoExtraNotifications() {
        draft.setName("NewName");
        draft.getPicks().add(new WeftPick(Color.RED, 2));
        draft.getEnds().add(new WarpEnd(Color.BLUE, 1));
        assertNull(listener.event);
    }
}
