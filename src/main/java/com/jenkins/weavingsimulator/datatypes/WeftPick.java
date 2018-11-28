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
import java.util.Arrays;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/** This bean represents a single weft thread in a weaving pattern.  It contains
 * the id of the treadle to push, and the color of the weft thread to use.
 *
 * @author  ajenkins
 */
public class WeftPick {
    
    /** Holds value of property treadleId. */
    private boolean[] treadles;
    
    /** Utility field used by bound properties. */
    private transient java.beans.PropertyChangeSupport propertyChangeSupport =  
        new java.beans.PropertyChangeSupport(this);
    
    /** Holds value of property color. */
    private Color color = Color.WHITE;
    
    /** Creates a new instance of WeftPick */
    public WeftPick() {
    }
    
    public WeftPick(int treadleCount) {
    	this.treadles = new boolean[treadleCount];
    }

    public WeftPick(Color color, int treadleCount, int... selected) {
    	if (treadleCount < 0) throw new IllegalArgumentException();
        this.color = color;
        this.treadles = new boolean[treadleCount];
        for (int t : selected) {
        	if (t >= treadleCount || t < 0) throw new IllegalArgumentException();
        	this.treadles[t] = true;
        }
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
        return treadleId < treadles.length && this.treadles[treadleId];
    }
    
    /** Returns the first selected treadle ID, or -1 for no selection
     * 
     */
    public int getSelection() {
    	for (int i=0; i != treadles.length; ++i) {
    		if (treadles[i]) return i;
    	}
    	return -1;
    }
    
    /** Getter for property treadles
     * 
     */
    public boolean[] getTreadles() {
    	return treadles;
    }
    
    /** Sets all the treadles. Mainly for persistence.
     */
    public void setTreadles(boolean[] t) {
    	boolean[] oldTreadles = treadles;
    	treadles = t;
    	propertyChangeSupport.firePropertyChange("treadles", oldTreadles, treadles);
    }
    
    /** Setter for property treadleId.
     * @param treadleId Treadle to set.
     * @param value value to set it to
     *
     */
    public void setTreadle(int treadleId, boolean value) {
        boolean[] oldTreadles = this.treadles.clone();
        this.treadles[treadleId] = value;
        propertyChangeSupport.firePropertyChange("treadles", oldTreadles, treadles);
    }
    
	/** Sets the given treadle to be selected, clearing any other selections.
	 *  It is used for non-liftplan drafts, and also for restoring
	 *  drafts from older persisted versions where the property was called treadleId.
	 * 
	 * @param i treadle to set
	 */
	public void setTreadleId(int i) {
		if (treadles == null) {
			treadles = new boolean[i+1];
		}
		if (i==-1 || !treadles[i]) {
			boolean[] oldTreadles = treadles;
			treadles = new boolean[oldTreadles.length];
			if (i != -1) treadles[i] = true;
			propertyChangeSupport.firePropertyChange("treadles", oldTreadles, treadles);			
		}
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
		if (this.treadles.length != treadles) throw new IllegalArgumentException ();
	}

	public void setTreadleCount(int i) {
		boolean[] oldTreadles = treadles;
		treadles = new boolean[i];
		System.arraycopy(oldTreadles, 0, treadles, 0, Math.min(i, oldTreadles.length));
		propertyChangeSupport.firePropertyChange("treadles", oldTreadles, treadles);
	}


	@Override
	public String toString () {
		return String.format("#%02x%02x%02x : %s", 
				color.getRed(), color.getGreen(),color.getBlue(),
				Arrays.toString(treadles));
	}
}
