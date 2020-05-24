/*
 * StepColorModelTest.java
 * JUnit based test
 * 
 * Created on January 7, 2005, 3:43 PM
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

import com.jenkins.weavingsimulator.datatypes.WeftPick;
import com.jenkins.weavingsimulator.datatypes.Treadle;
import com.jenkins.weavingsimulator.datatypes.WarpEnd;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import javax.swing.table.TableModel;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author ajenkins
 */
public class StepColorModelTest extends TestCase {
    private WeavingDraft draft;
    private TableModel model;
    private TestTableModelListener listener;
    
    public StepColorModelTest(String testName) {
        super(testName);
    }

    protected void setUp() {
        draft = new WeavingDraft("TestDraft");
        draft.getTreadles().add(new Treadle());
        draft.getTreadles().add(new Treadle());
        
        draft.setNumHarnesses(2);
        
        List<WeftPick> picks = new LinkedList<WeftPick>();
        picks.add(new WeftPick(Color.BLACK, 2, 0));
        picks.add(new WeftPick(Color.WHITE, 2, 1));
        
        draft.setPicks(picks);

        model = new StepColorModel(new EditingSession(draft));
        
        listener = new TestTableModelListener();
        model.addTableModelListener(listener);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(StepColorModelTest.class);
        
        return suite;
    }

    public void testGetColumnCount() {
        assertEquals(1, model.getColumnCount());
    }

    public void testGetRowCount() {
        assertEquals(draft.getPicks().size(), model.getRowCount());
    }

    public void testGetValueAt() {
        assertEquals(draft.getPicks().get(1).getColor(),
                model.getValueAt(1, 0));
    }

    public void testSetValueAt() {
        model.setValueAt(Color.BLUE,  1, 0);
        assertEquals(Color.BLUE, draft.getPicks().get(1).getColor());
    }

    public void testIsCellEditable() {
        for (int row = 0; row < model.getRowCount(); row++)
            assertTrue("row "+row+" editable", model.isCellEditable(row, 0));
    }

    public void testGetColumnClass() {
        assertEquals(Color.class, model.getColumnClass(0));
    }
    
    /// Test if tableListener is notified when a setValueAt is called
    public void testNotifyListenerOnSet() {
        model.setValueAt(Color.BLUE, 1, 0);
        TableModelTestUtils.assertOneTableCellUpdateEvent(listener.event, 1, 0);
    }
    
    /// Test that listener is notifed when number of picks changes
    public void testNotifyListenerOnStepsChanged() {
        draft.getPicks().add(new WeftPick(Color.BLACK, 2, 0));        
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    /// Test that listener is not notified for properties other than picks
    public void testNotifyOnlyForSteps() {
        // change some other properties of draft
        draft.setName("Foo");
        draft.getEnds().add(new WarpEnd(Color.RED, 1));
        draft.setNumHarnesses(3);
        draft.getTreadles().set(1, new Treadle());
        
        assertNull(listener.event);
    }
}
