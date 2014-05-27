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
import junit.framework.TestCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author ajenkins
 */
public class TreadlingDraftModelTest extends TestCase {
    private WeavingDraft draft;
    private TreadlingDraftModel model;
    private TestTableModelListener listener;
    private EditingSession session;
    
    public TreadlingDraftModelTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
        draft = new WeavingDraft("TestDraft");
        draft.setNumHarnesses(2);
        draft.setTreadles(Arrays.asList(new Treadle(), new Treadle()));
        draft.setPicks(Arrays.asList(
                new WeftPick(Color.BLACK, 2, 0), 
                new WeftPick(Color.WHITE, 2, 1),
                new WeftPick(Color.BLUE, 2, 0)));
        
        session = new EditingSession(draft);
        model = new TreadlingDraftModel(session);
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
    	draft.getPicks().get(0).setTreadle(0, true);
    	draft.getPicks().get(1).setTreadle(1, true);
    	draft.getPicks().get(2).setTreadle(0, true);
    	
    	assertThat((Color)model.getValueAt(0, 0), equalTo(Color.BLACK));
    	assertThat((Color)model.getValueAt(0, 1), equalTo(Color.WHITE));
    	assertThat((Color)model.getValueAt(1, 0), equalTo(Color.WHITE));
    	assertThat((Color)model.getValueAt(1, 1), equalTo(Color.BLACK));
    	assertThat((Color)model.getValueAt(2, 0), equalTo(Color.BLACK));
    	assertThat((Color)model.getValueAt(2, 1), equalTo(Color.WHITE));    	
    }
    
    public void testGetValueWithSelection() {
    	model.showSelection(0, 0, 2, 2);

    	draft.getPicks().get(0).setTreadle(0, true);
    	draft.getPicks().get(1).setTreadle(1, true);
    	draft.getPicks().get(2).setTreadle(0, true);

    	assertThat((Color)model.getValueAt(0, 0), equalTo(Color.BLACK));
    	assertThat((Color)model.getValueAt(0, 1), equalTo(Color.LIGHT_GRAY));
    	assertThat((Color)model.getValueAt(0, 2), equalTo(Color.WHITE));
    	assertThat((Color)model.getValueAt(1, 0), equalTo(Color.LIGHT_GRAY));
    	assertThat((Color)model.getValueAt(1, 1), equalTo(Color.BLACK));
    	assertThat((Color)model.getValueAt(1, 2), equalTo(Color.WHITE));
    	assertThat((Color)model.getValueAt(2, 0), equalTo(Color.BLACK));    	
    	assertThat((Color)model.getValueAt(2, 1), equalTo(Color.WHITE));    	
    	assertThat((Color)model.getValueAt(2, 2), equalTo(Color.WHITE));    	
    }

    public void testSetValueAt() {
        assertTrue(draft.getPicks().get(1).isTreadleSelected(1));
        model.setValueAt(true, 1, 0);
        assertTrue(draft.getPicks().get(1).isTreadleSelected(0));
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
            assertEquals("col="+col, Color.class, model.getColumnClass(col));
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
        draft.getPicks().add(new WeftPick(Color.RED, 2, 1));
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    public void testNoExtraNotifications() {
        draft.setNumHarnesses(5);
        draft.setName("somename");
        draft.getEnds().add(new WarpEnd(Color.BLACK, 1));
        assertNull(listener.event);
    }
        
    public void testCopySelectionGoesToSession() {
    	draft.setTreadles(Arrays.asList(new Treadle(), new Treadle(), 
    			new Treadle(), new Treadle()));
    	draft.setPicks(Arrays.asList(
                new WeftPick(Color.BLACK, 4, 0), 
                new WeftPick(Color.WHITE, 4, 1),
                new WeftPick(Color.WHITE, 4, 1),
                new WeftPick(Color.WHITE, 4, 1),
                new WeftPick(Color.WHITE, 4, 1),
                new WeftPick(Color.BLUE, 4, 0)));

    	model.setValueAt(true, 2, 1);
    	model.setValueAt(true, 3, 2);
    	model.setValueAt(true, 4, 3);
    	model.setValueAt(true, 5, 3);
    	
    	model.showSelection(2, 1, 5, 3);
    	assertThat (session.getSelectedCells().getRows(), is(0));
    	assertThat (session.getSelectedCells().getColumns(), is(0));
    	model.copySelection();
    	
    	SelectedCells selection = session.getSelectedCells();
    	assertThat(selection.getRows(), equalTo(3));
        assertThat(selection.getColumns(), equalTo(2));
        assertThat(selection.getValue(0,0), is(true));
        assertThat(selection.getValue(0,1), is(false));
        assertThat(selection.getValue(1,0), is(false));
        assertThat(selection.getValue(1,1), is(true));    
    	assertThat(selection.getValue(2,0), is(false));
    	assertThat(selection.getValue(2,1), is(false));
    }
    
    public void testSelectionisPastedFromSession() {
    	draft.setTreadles(Arrays.asList(new Treadle(), new Treadle(), 
    			new Treadle(), new Treadle()));
    	draft.setPicks(Arrays.asList(
                new WeftPick(Color.BLACK, 4, 0), 
                new WeftPick(Color.WHITE, 4, 1),
                new WeftPick(Color.WHITE, 4, 2),
                new WeftPick(Color.WHITE, 4, 3),
                new WeftPick(Color.WHITE, 4, 1),
                new WeftPick(Color.BLUE, 4, 0)));

    	/* Start with
    	 *  *...
    	 *  .*..
    	 *  ..*.
    	 *  ...*
    	 *  .*..
    	 *  *...
    	 *  copy from r,c 3, 1 .. 5, 4, paste to 1, 0, should get
    	 *  *...
    	 *  ..*.
    	 *  *...
    	 *  ...*
    	 *  .*..
    	 *  *... 
    	 */	
    	session.setSelectedCells(new SelectedCells(draft.getPicks(), new GridSelection(3, 1, 5, 4)));
    	
    	model.pasteSelection(1, 0);
    	assertThat((Color)model.getValueAt(0, 0), is(Color.BLACK));
    	assertThat((Color)model.getValueAt(0, 1), is(Color.WHITE));
    	assertThat((Color)model.getValueAt(0, 2), is(Color.WHITE));
    	assertThat((Color)model.getValueAt(0, 3), is(Color.WHITE));
    	
    	assertThat((Color)model.getValueAt(1, 0), is(Color.WHITE));
    	assertThat((Color)model.getValueAt(1, 1), is(Color.WHITE));
    	assertThat((Color)model.getValueAt(1, 2), is(Color.BLACK));
    	assertThat((Color)model.getValueAt(1, 3), is(Color.WHITE));
    	
    	assertThat((Color)model.getValueAt(2, 0), is(Color.BLACK));
    	assertThat((Color)model.getValueAt(2, 1), is(Color.WHITE));
    	assertThat((Color)model.getValueAt(2, 2), is(Color.WHITE));
    	assertThat((Color)model.getValueAt(2, 3), is(Color.WHITE));
    	
    	assertThat((Color)model.getValueAt(3, 0), is(Color.WHITE));
    	assertThat((Color)model.getValueAt(3, 1), is(Color.WHITE));
    	assertThat((Color)model.getValueAt(3, 2), is(Color.WHITE));
    	assertThat((Color)model.getValueAt(3, 3), is(Color.BLACK));
    	
    }
}
