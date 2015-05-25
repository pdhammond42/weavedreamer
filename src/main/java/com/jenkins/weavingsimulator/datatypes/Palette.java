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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * A Palette is a class representing a set of colors, along with a currently
 * selected color.
 *
 * @author ajenkins
 */
public class Palette implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Default color for palette entries, if a color hasn't been set
     */
    public static final Color DEFAULT_COLOR = Color.WHITE;
    
    private List<Color> colors;
    private int selection = -1; // index into colors
    private String name;
    
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
    
    public Palette() {
    	colors = new ArrayList<Color>();
    }
    
    /**
     * Constructs a palette with the colors in <CODE>colors</CODE>.
     * @param colors The palette will be initialized with a copy of <CODE>colors</CODE>.
     */
    public Palette(List<Color> colors, String name) {
    	if (colors != null) {
    		this.colors = new ArrayList<Color>(colors);
    	}
        this.name = name;
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
    
    public void writeObject(ObjectOutputStream os) throws IOException {
    	os.writeObject(colors);
    }
    
    @SuppressWarnings("unchecked")
	public void readObject (ObjectInputStream os) throws IOException, ClassNotFoundException {
    	colors = (List<Color>)(os.readObject());
    }
    
    public String toString() {
    	return name;
    }
    

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colors == null) ? 0 : colors.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Palette other = (Palette) obj;
		if (colors == null) {
			if (other.colors != null)
				return false;
		} else if (!colors.equals(other.colors))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
     * Saves the given palettes into a preferences node, replacing any rpevious content.
     * Adding a layer of indirection between here and preferences would be more extensible, 
     * but I don't want to extend it right now.
     * @param palettes A list of palettes to store.
     * @param prefs Where to store them
     * @throws BackingStoreException 
     * @throws IOException 
     */
    public static void savePalettes(List<Palette> palettes, Preferences prefs) throws BackingStoreException, IOException {
    	prefs.clear();
    	int key = 0;
    	for(Palette p: palettes) {
        	ByteArrayOutputStream os = new ByteArrayOutputStream();
        	ObjectOutputStream oos = new ObjectOutputStream(os);
        	oos.writeObject(p);    		
        	prefs.putByteArray(String.format("%04d", key++), os.toByteArray());
    	}
    }
    
    /**
     * Loads palettes from the given preferences node. 
     * @param prefs Where to load from
     * @return The palettes
     * @throws BackingStoreException 
     * @throws IOException 
     */
    public static List<Palette> loadPalettes (Preferences prefs) throws BackingStoreException, IOException {
		List<Palette> palettes = new ArrayList<Palette>();
		String[] keys = prefs.keys();
		Arrays.sort(keys);
		for (String key: keys) {
	    	ByteArrayInputStream is = new ByteArrayInputStream(prefs.getByteArray(key, null));
	    	ObjectInputStream ois = new ObjectInputStream(is);			
	    	try {
	    		palettes.add((Palette)(ois.readObject()));
	    	} catch (ClassNotFoundException e) {
	    		// This really should never happen...
	    	}
		}
		return palettes;
    }
    
    /**
     * Returns a default set of palettes. This is what the user gets offered in a new setup,
     * where no palettes are saved, and also if palette storage gets corrupted.
     * @return
     */
    public static List<Palette> getDefaultPalettes() {
		List<Palette> palettes = new ArrayList<Palette>();
		palettes.add(new Palette(Arrays.asList(
				Color.black, Color.darkGray, Color.lightGray, Color.white), 
				"Monochrome"));
		palettes.add(new Palette(Arrays.asList(
				new Color(78, 146, 88), new Color( 48, 103, 84), new Color(108, 196, 23), new Color(204, 251, 93), 
				new Color( 0, 128, 128), new Color(237, 218, 116), new Color(178, 194, 72), new Color(120, 199, 199)
				), "Spring"));
		palettes.add(new Palette(Arrays.asList(
				new Color(255, 166, 47), new Color(197, 137, 23), new Color(248, 128, 23), new Color(192, 64, 0), 
				new Color(243, 229, 171), new Color(255, 243, 128), new Color(248, 114, 23), new Color(153, 0, 18)
				), "Summer"));
		palettes.add(new Palette(Arrays.asList(
				new Color(228,228,149), new Color(204,128,51), new Color(205, 127, 50), new Color(175, 120, 23),
				new Color(127, 70, 44), new Color(128, 101, 23), new Color(73, 61, 38), new Color(193, 154, 107)
				), "Autumn"));
		palettes.add(new Palette(Arrays.asList(
				new Color(0, 255, 255), new Color( 62, 169, 159), new Color(59, 185, 255), new Color(43, 96, 222 ), 
				new Color( 43, 56, 86), new Color(109, 123, 141), new Color(102, 99, 98 ), new Color(94, 125, 126)
				), "Winter"));
		palettes.add(new Palette(Arrays.asList(
				Color.white, Color.red, Color.black, Color.green, Color.yellow, Color.blue
				), "80s"));    	
		return palettes;
    }
}

