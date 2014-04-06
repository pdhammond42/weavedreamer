/*
 * WeftPick.java
 * 
 * Created on April 23, 2003, 10:49 PM
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

/** This bean represents a single weft thread in a weaving pattern.  It contains
 * the id of the treadle to push, and the color of the weft thread to use.
 *
 * @author  ajenkins
 */
public class WeftPick {
    
    /** Holds value of property treadleId. */
    private int treadleId = -1;
    
    /** Utility field used by bound properties. */
    private transient java.beans.PropertyChangeSupport propertyChangeSupport =  
        new java.beans.PropertyChangeSupport(this);
    
    /** Holds value of property color. */
    private Color color = Color.WHITE;
    
    /** Creates a new instance of WeftPick */
    public WeftPick() {
    }
    
    public WeftPick(Color color, int treadleId) {
        this.color = color;
        this.treadleId = treadleId;
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
    
    /** Getter for property treadleId.
     * @return Value of property treadleId.
     *
     */
    public boolean isTreadleSelected (int treadleId) {
        return this.treadleId == treadleId;
    }
    
    /** Setter for property treadleId.
     * @param treadleId New value of property treadleId.
     *
     */
    public void setTreadleId(int treadleId) {
        int oldTreadleId = this.treadleId;
        this.treadleId = treadleId;
        propertyChangeSupport.firePropertyChange("treadleId", oldTreadleId, treadleId);
    }
    
    /** Getter for property color.
     * @return Value of property color.
     *
     */
    public Color getColor() {
        return this.color;
    }
    
    /** Setter for property color.
     * @param color New value of property color.
     *
     */
    public void setColor(Color color) {
        Color oldColor = this.color;
        this.color = color;
        propertyChangeSupport.firePropertyChange("color", oldColor, color);
    }
    
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Validates the object against the number of treadles in the draft.
     * @param treadles
     */
	public void validate(int treadles) throws IllegalArgumentException {
		if (treadleId >= treadles || treadleId < -1) throw new IllegalArgumentException ();
	}
}
