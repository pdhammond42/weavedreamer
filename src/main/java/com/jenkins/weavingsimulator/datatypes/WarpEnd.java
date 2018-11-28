/*
 * WarpEnd.java
 * 
 * Created on March 30, 2003, 7:57 PM
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

import java.awt.Color;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/** A class representing one end in a warp.
 * @author ajenkins
 */
public class WarpEnd {
    private Color color;
    private int harnessId;
    
    /** Utility field used by bound properties. */
    private transient java.beans.PropertyChangeSupport propertyChangeSupport =  
        new java.beans.PropertyChangeSupport(this);
    
    public WarpEnd() { }
    
    /** Creates a new instance of WarpEnd
     * @param c Color for this end
     * @param h harness id which will lift this end.
     */
    public WarpEnd(Color c, int h) {
        color = c;
        harnessId = h;
    }
    
    /**
     * @return The color for this end
     */    
    public Color getColor() { return color; }
    public void setColor(Color newVal) {
        Color oldVal = color;
        color = newVal;
        propertyChangeSupport.firePropertyChange("color", oldVal, newVal);
    }
    
    /**
     * @return The id of the harness that lifts this end
     */    
    public int getHarnessId() { return harnessId; }
    public void setHarnessId(int newVal) {
        int oldVal = harnessId;
        harnessId = newVal;
        propertyChangeSupport.firePropertyChange("harnessId", oldVal, newVal);
    }
    
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    /** Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     *
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }
    
    /** Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     *
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
    
}
