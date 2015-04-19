/*
 * ObservableList.java
 * 
 * Created on September 1, 2004, 9:52 PM
 *  
 * Copyright 2004 Adam P. Jenkins
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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** A List implementation which can notify observers of changes.  Using the 
 * {@link #addListChangeListener(ListChangeListener)} method, listeners can be
 * notified when items are added, removed, or replaced in this list.
 *
 * @author  ajenkins
 */
public class ObservableList<E> extends AbstractList<E> {
    private List<E> contents = new ArrayList<E>();
    
    /** Utility field holding list of ListChangeListeners. */
    private transient ArrayList<ListChangeListener<E>> listChangeListenerList;
    
    /** Creates a new instance of ObservableList */
    public ObservableList() {
    }
 
    public ObservableList(Collection<E> c) {
        contents.addAll(c);
    }
    
    public void add(int index, E element) {
        contents.add(index, element);
        fireItemAdded(new ListChangedEvent<E>(this, index, null, element));
    }
    
    public E get(int index) {
        return contents.get(index);
    }
    
    public E remove(int index) {
        E oldVal = contents.remove(index);
        fireItemRemoved(new ListChangedEvent<E>(this, index, oldVal, null));
        return oldVal;
    }
    
    public E set(int index, E element) {
        E oldVal = contents.set(index, element);
        fireItemChanged(new ListChangedEvent<E>(this, index, oldVal, element));
        return oldVal;
    }
    
    public int size() {
        return contents.size();
    }
    
    /** Registers ListChangeListener to receive events.
     * @param listener The listener to register.
     *
     */
    public synchronized void addListChangeListener(ListChangeListener<E> listener) {
        if (listChangeListenerList == null ) {
            listChangeListenerList = new ArrayList<ListChangeListener<E>>();
        }
        listChangeListenerList.add(listener);
    }
    
    /** Removes ListChangeListener from the list of listeners.
     * @param listener The listener to remove.
     *
     */
    public synchronized void removeListChangeListener(ListChangeListener<E> listener) {
        if (listChangeListenerList != null ) {
            listChangeListenerList.remove(listener);
        }
    }
    
    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    private void fireItemAdded(ListChangedEvent<E> event) {
        ArrayList<ListChangeListener<E>> list;
        synchronized (this) {
            if (listChangeListenerList == null) return;
            list = new ArrayList<ListChangeListener<E>>(listChangeListenerList);
        }
        for (ListChangeListener<E> l : list) {
            l.itemAdded(event);
        }
    }
    
    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    private void fireItemRemoved(ListChangedEvent<E> event) {
        ArrayList<ListChangeListener<E>> list;
        synchronized (this) {
            if (listChangeListenerList == null) return;
            list = new ArrayList<ListChangeListener<E>>(listChangeListenerList);
        }
        for (ListChangeListener<E> l : list) {
            l.itemRemoved(event);
        }
    }
    
    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    private void fireItemChanged(ListChangedEvent<E> event) {
        ArrayList<ListChangeListener<E>> list;
        synchronized (this) {
            if (listChangeListenerList == null) return;
            list = new ArrayList<ListChangeListener<E>>(listChangeListenerList);
        }
        for (ListChangeListener<E> l : list) {
            l.itemChanged(event);
        }
    }
    
}
