/*
 * ListChangedEvent.java
 * 
 * Created on April 3, 2003, 12:06 AM
 *  
 * Copyright 2003 Adam P. Jenkins
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


package com.jenkins.weavingsimulator.datatypes;

import java.util.List;

/**
 * Event passed to ListChangeListener by Observable list
 * to describe changes to the list.
 * @author ajenkins
 */
public class ListChangedEvent<E> extends java.util.EventObject {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Holds value of property itemIndex. */
    private int itemIndex;
    
    /** Holds value of property oldValue. */
    private E oldValue;
    
    /** Holds value of property newValue. */
    private E newValue;
    
    /**
     * Creates a new instance of ListChangedEvent
     * @param source Reference to the ObservableList which was changed.
     * @param index Index of the item which was changed.  If an item was
     * added or removed, the index of the new item or removed
     * item.
     * @param oldValue The value which was at <CODE>index</CODE> before the 
     * item changed.  If an item was added, this parameter is
     * null.  If an item was deleted, this is the object which
     * was removed from the list.
     * @param newValue If item was changed, this is the new value at 
     * <CODE>index</CODE>.  If item was inserted, this is the
     * newly inserted item.  If item was removed, this is null.
     */
    public ListChangedEvent(List<E> source, int index, E oldValue, E newValue) {
        super(source);
        itemIndex = index;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    /**
     * Gets the index of the item which changed.
     * @return The index of the item which was changed, added, or deleted.
     */
    public int getItemIndex() {
        return this.itemIndex;
    }    
    
    /**
     * Getter for property oldValue.
     * @return Old value of the changed property, null if a new item
     * was added.
     */
    public E getOldValue() {
        return this.oldValue;
    }
    
    /**
     * Getter for property newValue.
     * @return New value of item changed, null if an item was deleted.
     */
    public E getNewValue() {
        return this.newValue;
    }
    
}
