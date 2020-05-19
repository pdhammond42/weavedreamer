/*
 * ObservableListTest.java
 * JUnit based test
 * 
 * Created on January 7, 2005, 12:08 AM
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


package com.jenkins.weavedreamer.datatypes;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author ajenkins
 */
public class ObservableListTest extends TestCase {
    private ObservableList<String> theList;
    private TestListChangeListener listener;
    
    public ObservableListTest(String testName) {
        super(testName);
    }

    protected void setUp() {
        theList = new ObservableList<String>();
        listener = new TestListChangeListener();
        theList.addListChangeListener(listener);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ObservableListTest.class);
        
        return suite;
    }

    private class TestListChangeListener implements ListChangeListener<String>
    {
        int nItemsRemoved = 0;
        int nItemsChanged = 0;
        int nItemsAdded = 0;
        
        ListChangedEvent<String> event = null;

        void resetCounts() {
            nItemsRemoved = nItemsChanged = nItemsAdded = 0;
            event = null;
        }
        
        public void itemRemoved(ListChangedEvent<String> event) {
            nItemsRemoved++;
            this.event = event;
        }

        public void itemChanged(ListChangedEvent<String> event) {
            nItemsChanged++;
            this.event = event;
        }

        public void itemAdded(ListChangedEvent<String> event) {
            nItemsAdded++;
            this.event = event;
        }
        
    }
    
    /**
     * Test of add method, of class com.jenkins.weavedreamer.datatypes.ObservableList.
     */
    public void testAdd() {
        theList.add("Hello");
        assertEquals("Hello", theList.get(0));
        assertEquals(1, theList.size());
        
        assertEquals(1, listener.nItemsAdded);
        assertEquals(0, listener.nItemsChanged);
        assertEquals(0, listener.nItemsRemoved);
    }


    /**
     * Test of remove method, of class com.jenkins.weavedreamer.datatypes.ObservableList.
     */
    public void testRemove() {
        theList.add("Hello");
        theList.remove(0);
        
        assertEquals(0, theList.size());
        assertEquals(1, listener.nItemsAdded);
        assertEquals(1, listener.nItemsRemoved);
        assertEquals(0, listener.nItemsChanged);
        
        // make sure the remove(Object) version works too
        listener.resetCounts();
        String hello = "Hello";
        theList.add(hello);
        theList.remove(hello);
        
        assertEquals(0, theList.size());
        assertEquals(1, listener.nItemsAdded);
        assertEquals(1, listener.nItemsRemoved);
        assertEquals(0, listener.nItemsChanged);
    }

    /**
     * Test of get method
     */
    public void testGet() {
        theList.add("Hello");
        listener.resetCounts();
        
        assertEquals("Hello", theList.get(0));
        // make sure get doesn't cause any listeners to be notified
        assertEquals(0, listener.nItemsAdded);
        assertEquals(0, listener.nItemsRemoved);
        assertEquals(0, listener.nItemsChanged);
    }
    
    /**
     * Test of set method, of class com.jenkins.weavedreamer.datatypes.ObservableList.
     */
    public void testSet() {
        theList.add("Hello");
        theList.set(0, "Bye");
        
        assertEquals(1, theList.size());
        assertEquals("Bye", theList.get(0));
        assertEquals(1, listener.nItemsAdded);
        assertEquals(0, listener.nItemsRemoved);
        assertEquals(1, listener.nItemsChanged);
    }

    /**
     * Test of removeListChangeListener method, of class com.jenkins.weavedreamer.datatypes.ObservableList.
     */
    public void testRemoveListChangeListener() {
        theList.removeListChangeListener(listener);
        theList.add("Hello");
        theList.set(0, "Bye");
        theList.remove(0);
        
        // make sure the listener wasn't notified
        assertEquals(0, listener.nItemsAdded);
        assertEquals(0, listener.nItemsRemoved);
        assertEquals(0, listener.nItemsChanged);
    }
    
    public void testMultipleListeners() {
        TestListChangeListener listener2 = new TestListChangeListener();
        theList.addListChangeListener(listener2);
        
        theList.add("Hello");
        theList.set(0, "Bye");
        theList.remove(0);
        
        // make sure listener was notified
        assertEquals(1, listener.nItemsAdded);
        assertEquals(1, listener.nItemsRemoved);
        assertEquals(1, listener.nItemsChanged);
        // make sure listener2 was notified
        assertEquals(1, listener2.nItemsAdded);
        assertEquals(1, listener2.nItemsRemoved);
        assertEquals(1, listener2.nItemsChanged);
    }

    public void testEventValues() {
        theList.add("Hello");
        assertEquals(0, listener.event.getItemIndex());
        assertEquals("Hello", listener.event.getNewValue());
        assertNull(listener.event.getOldValue());
        
        theList.set(0, "Bye");
        assertEquals(0, listener.event.getItemIndex());
        assertEquals("Bye", listener.event.getNewValue());
        assertEquals("Hello", listener.event.getOldValue());
        
        // try adding another element to make sure itemIndex isn't always 0
        theList.add("Two");
        assertEquals(1, listener.event.getItemIndex());
        assertEquals("Two", listener.event.getNewValue());
        assertNull(listener.event.getOldValue());
        
        theList.remove(1);
        assertEquals(1, listener.event.getItemIndex());
        assertNull(listener.event.getNewValue());
        assertEquals("Two", listener.event.getOldValue());
    }
}
