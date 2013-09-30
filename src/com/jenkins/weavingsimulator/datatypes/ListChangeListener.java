/*
 * ListChangeListener.java
 * 
 * Created on April 3, 2003, 12:02 AM
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

/**
 *
 * @author  ajenkins
 */
public interface ListChangeListener<E> extends java.util.EventListener {
    
    public void itemAdded(ListChangedEvent<E> event);
    
    public void itemRemoved(ListChangedEvent<E> event);
    
    public void itemChanged(ListChangedEvent<E> event);
    
}
