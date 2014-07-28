/*
 * AbstractWeavingDraftModelTest.java
 * JUnit based test
 * 
 * Created on January 8, 2005, 2:19 AM
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

import com.jenkins.weavingsimulator.datatypes.WeavingDraft;

import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import junit.framework.TestCase;
import junit.framework.TestSuite;



/**
 *
 * @author ajenkins
 */
public class AbstractWeavingDraftModelTest extends TestCase {
    private AbstractWeavingDraftModel model;
    private WeavingDraft draft;
    private TestTableModelListener listener;
    
    public AbstractWeavingDraftModelTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
        draft = new WeavingDraft("TestDraft");
        model = new AbstractWeavingDraftModelImpl(draft);
        listener = new TestTableModelListener();
    }

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite(AbstractWeavingDraftModelTest.class);
        
        return suite;
    }

    public void testGetDraft() {
        assertSame(draft, model.getDraft());
    }

    /**
     * Make sure when a AbstractWeavingModel is created with a draft
     * passed in the constructor, the model adds a listener to the draft
     * to be notified of changes.
     */
    public void testDefaultDraftListener() {
        model.addTableModelListener(listener);
        draft.setNumHarnesses(3);
        // assert that table model noticed the change
        assertNotNull(listener.event);
    }
    
    private class TestPropertyChangeListener implements PropertyChangeListener {
        java.beans.PropertyChangeEvent event = null;
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            event = evt;
        }
        
    }
    
    public void testSetDraftListener() {
        // make sure adding a new draft listener replaces the old listener
        TestPropertyChangeListener draftListener = new TestPropertyChangeListener();
        model.setDraftListener(draftListener);
        model.addTableModelListener(listener);
        draft.setNumHarnesses(3);
        
        // assert that the new draftListener was added to draft
        assertNotNull(draftListener.event);
        
        // assert that the original draft listener was replaced; if the original
        // was still in place then a TableModelEvent would have been raised
        assertNull(listener.event);
    }

    public void testFireTableColumnUpdated() {
        model.addTableModelListener(listener);
        model.fireTableColumnUpdated(0);
        
        assertNotNull(listener.event);
        assertEquals(0, listener.event.getColumn());
        assertEquals(0, listener.event.getFirstRow());
        assertTrue(listener.event.getLastRow() >= model.getRowCount() - 1);
    }

    public void testFireCurrentCellUpdated() {
        model.addTableModelListener(listener);
        model.setCurrentCell(2, 3);
        
        assertEquals(new Rectangle (3, 2, -1, -1), model.getCurrentCell());
    	assertEquals(AbstractWeavingDraftModel.CURSOR, listener.event.getType());
    	assertEquals(3, listener.event.getColumn());
    	assertEquals(2, listener.event.getFirstRow());
    }
    
    public void testFireCurrentCellUpdatedForDrag() {
        model.addTableModelListener(listener);
        model.setCurrentCell(2, 3, 1, 5);
        
        assertEquals(new Rectangle (3, 2, 2, 1), model.getCurrentCell());
    	assertEquals(AbstractWeavingDraftModel.CURSOR, listener.event.getType());
    	assertEquals(3, listener.event.getColumn());
    	assertEquals(2, listener.event.getFirstRow());
    }
        
    private class AbstractWeavingDraftModelImpl extends AbstractWeavingDraftModel {

        AbstractWeavingDraftModelImpl(WeavingDraft draft) {
            super(draft);
        }

        public int getRowCount() {
            return 1;
        }

        public int getColumnCount() {
            return 1;
        }

        public Object getValueAt(int row, int column) {
            return null;
        }
        public EditedValueProvider getEditedValueProvider() {
        	return null;
        }
    }
}
