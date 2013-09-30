/*
 * Palette.java
 * 
 * Created on January 13, 2005, 1:02 AM
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


package com.jenkins.weavingsimulator.datatypes;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Palette is a class representing a set of colors, along with a currently
 * selected color.
 *
 * @author ajenkins
 */
public class Palette {
    /**
     * Default color for palette entries, if a color hasn't been set
     */
    public static final Color DEFAULT_COLOR = Color.WHITE;
    
    private List<Color> colors;
    private int selection = -1; // index into colors
    
    private PropertyChangeSupport propertyChangeSupport =
            new PropertyChangeSupport(this);
    
    /**
     * Constructs a palette with <CODE>size</CODE> entries.  All entries will 
     * initially be set to <CODE>DEFAULT_COLOR</CODE>
     * @param size number of elements in palette
     */
    public Palette(int size) {
        colors = new ArrayList<Color>(size);
        for (int i = 0; i < size; i++)
            colors.add(DEFAULT_COLOR);
    }
    
    /**
     * Constructs a palette with the colors in <CODE>colors</CODE>.
     * @param colors The palette will be initialized with a copy of <CODE>colors</CODE>.
     */
    public Palette(List<Color> colors) {
        this.colors = new ArrayList<Color>(colors);
    }
    
    /**
     * Returns the number of entries in this palette
     * @return Number of entries in this palette.
     */
    public int getNumColors() {
        return colors.size();
    }
    
    /**
     * Set the number of entries in this palette.  If <CODE>size</CODE> is less than the current
     * size, entries will be removed from the high indexes.  If <CODE>size</CODE> is greather
     * than the current size, entries will be added to the end, with 
     * <CODE>DEFAULT_COLOR</CODE>.  Can cause a <CODE>PropertyChangeEvent</CODE>
     * for the <CODE>numColors</CODE> and <CODE>selection</CODE> properties.
     * @param size New number of entries in this palette.
     */
    public void setNumColors(int size) {
        if (size == colors.size())
            return;
        
        int oldSize = colors.size();
        if (size > colors.size()) {
            while (size > colors.size())
                colors.add(DEFAULT_COLOR);
        } else {
            while (colors.size() > size)
                colors.remove(colors.size() - 1);
            if (selection >= colors.size())
                setSelection(-1);
        }
        propertyChangeSupport.firePropertyChange("numColors", 
                oldSize, colors.size());
    }
    
    /**
     * Get a list of the colors in this palette.
     * @return An unmodifiable List of the colors in this palette.
     */
    public List<Color> getColors() {
        return Collections.unmodifiableList(colors);
    }
    
    /**
     * Sets the colors in this palette, replacing the existing set of colors.  Causes
     * a <CODE>PropertyChangeEvent</CODE> to be fired for property name "colors".
     * @param colors New list of colors to set this palette to.  The list will be copied.
     */
    public void setColors(List<Color> colors) {
        List<Color> oldColors = this.colors;
        this.colors = new ArrayList<Color>(colors);
        if (selection >= colors.size())
            setSelection(-1);
        propertyChangeSupport.firePropertyChange("colors", oldColors, colors);
    }
    
    /**
     * Get a color from the palette.
     * @param i index of the color to get.  Must be between 0 and <CODE>numColors</CODE>.
     * @return Returns the Color at index <CODE>i</CODE> from this Palette.
     * @throws IndexOutOfBoundsException if <CODE>i</CODE> is out of bounds.
     */
    public Color getColor(int i) {
        return colors.get(i);
    }
    
    /**
     * Change a color entry in the palette to a new value.  Causes an
     * <CODE>IndexedPropertyChangeEvent</CODE> to be fired for
     * property name "color".
     * @param i Index of the color to set.  Must be between 0 and <CODE>numColors.</CODE>
     * @param c New color to set entry to.
     * @throws IndexOutOfBoundsException if <CODE>i</CODE> is out of bounds.
     */
    public void setColor(int i, Color c) {
        Color oldValue = colors.get(i);
        colors.set(i, c);
        propertyChangeSupport.fireIndexedPropertyChange("color", i, oldValue, c);
    }
    
    /**
     * Add a property change listener.
     * @param l New listener
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }
    
    /**
     * Remove a listener.
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
    
    /**
     * Get the index of the currently selected color.
     * @return The index of the currently selected color, or -1 to indicate no
     * color is selected.
     */
    public int getSelection() {
        return selection;
    }
    
    /**
     * Set the index of the currently selected color.  Causes
     * a <CODE>PropertyChangeEvent</CODE> with
     * property name "selection".
     * @param i Index of color to select, or -1 to indicate nothing is selected.
     */
    public void setSelection(int i) {
        int oldSelection = selection;
        if (i < -1 || i >= colors.size())
            throw new IllegalArgumentException("Invalid index: "+i);
        selection = i;
        propertyChangeSupport.firePropertyChange("selection", 
                oldSelection, selection);
    }
}

