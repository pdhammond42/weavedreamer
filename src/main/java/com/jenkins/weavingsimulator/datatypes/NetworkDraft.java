package com.jenkins.weavingsimulator.datatypes;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * The NetworkDraft class holds the algorithms and data for Network drafting. An
 * instance of this class controls a WeavingDraft, building the peg plan and
 * threading according to the Network drafting rules. It holds the necessary
 * data to do that - pattern line, keys, initial etc - and exposes them to the
 * GUI via grid models.
 * 
 * See "Network Drafting - an Introduction", Alice Schlein, Bridgewater Press
 * 1994.
 * 
 * @author pete
 * 
 */
public class NetworkDraft {
	
	public NetworkDraft () {
		initial = new Vector<Integer>();
		key1 = new Vector<List<Boolean>>(); 
		key2 = new Vector<List<Boolean>>(); 
		patternLine = new Vector<Integer>();
		setInitialRows (4);
		setInitialCols(4);
		setPatternLineRows(16);
		setPatternLineCols(24);
		setRibbonWidth(4);
	}
	
    private PropertyChangeSupport propertyChangeSupport =
            new PropertyChangeSupport(this);
    
    public void addPropertyChangeListener(String propertyName, 
            java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, l);
    }

    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

	public List<Integer> getInitial() {
		return initial;
	}

	public int getInitial(int index) {
		return initial.get(index);
	}

	public void setInitialRow(int index, int row) {
		int oldValue = initial.get(index);
		this.initial.set(index, row);
		propertyChangeSupport.fireIndexedPropertyChange("initial", index, oldValue, row);
	}

	public List<Integer> getPatternLine() {
		return patternLine;
	}

	public int getPatternLine(int index) {
		return patternLine.get(index);
	}
	
	/**
	 * Set the selected row of the given column of the pattern line.
	 * @param index The column
	 * @param row The row
	 */
	public void setPatternLineRow(int index, int row) {
		int oldValue = patternLine.get(index);
		this.patternLine.set(index, row);
		propertyChangeSupport.fireIndexedPropertyChange("patternLine", index,  oldValue, row);
	}

	public List<List<Boolean>> getKey1() {
		return key1;
	}

	public void setKey1(int column, int row, boolean val) {
		Object oldValue = deepClone(key1);
		this.key1.get(column).set(row, val);
		propertyChangeSupport.firePropertyChange("key1", oldValue, key1);		
	}
	
	public List<List<Boolean>> getKey2() {
		return key2;
	}

	public void setKey2(int column, int row, boolean val) {
		Object oldValue = deepClone(key2);
		this.key2.get(column).set(row, val);
		propertyChangeSupport.firePropertyChange("key2", oldValue, key2);		
	}
	
	public boolean isTelescoped() {
		return isTelescoped;
	}

	public void setTelescoped(boolean isTelescoped) {
		boolean oldValue = this.isTelescoped;
		this.isTelescoped = isTelescoped;
		propertyChangeSupport.firePropertyChange("isTelescoped", oldValue, isTelescoped);		
	}

	public int getInitialRows() {
		return initialRows;
	}

	public void setInitialRows(int initialRows) {
		int oldValue= this.initialRows;
		this.initialRows = initialRows;
		for (List<Boolean> i: key1) {
			setSizeBool(((Vector<Boolean>)i), initialRows);
		}
		for (List<Boolean> i: key2) {
			setSizeBool(((Vector<Boolean>)i), initialRows);
		}
		propertyChangeSupport.firePropertyChange("initialRows", oldValue, initialRows);		
		calculateShaftLimit();
	}

	public int getInitialCols() {
		return initial.size();
	}

	public void setInitialCols(int initialCols) {
		int oldValue = initial.size();
		setSizeInt(initial, initialCols);
		setSizeVBool(key1, initialCols, initialRows);
		setSizeVBool(key2, initialCols, initialRows);
		propertyChangeSupport.firePropertyChange("initialCols", oldValue, initialCols);						
	}

	public int getPatternLineRows() {
		return patternLineRows;
	}

	public void setPatternLineRows(int patternLineRows) {
		int oldValue = this.patternLineRows;
		this.patternLineRows = patternLineRows;
		propertyChangeSupport.firePropertyChange("patternLineRows", oldValue, patternLineRows);
	}

	public int getPatternLineCols() {
		return patternLine.size();
	}

	public void setPatternLineCols(int patternLineCols) {
		int oldValue = patternLine.size();
		setSizeInt(patternLine, patternLineCols);
		propertyChangeSupport.firePropertyChange("patternLineCols", oldValue, patternLine.size());
	}

	public int getRibbonWidth() {
		return ribbonWidth;
	}
	
	public void setRibbonWidth(int ribbonWidth) {
		int oldValue = this.ribbonWidth;
		this.ribbonWidth = ribbonWidth;
		propertyChangeSupport.firePropertyChange("ribbonWidth", oldValue, ribbonWidth);		
	}
	
	public int getShaftLimit() {
		return shaftLimit;
	}
	
	public void setShaftLimit(int shaftLimit) {
		int oldValue = this.shaftLimit;
		this.shaftLimit = shaftLimit;
		propertyChangeSupport.firePropertyChange("shaftLimit", oldValue, shaftLimit);		
	}
	
	public int getLoomShafts() {
		return loomShafts;
	}

	public void setLoomShafts(int loomShafts) {
		int oldValue = this.loomShafts;
		this.loomShafts = loomShafts;
		propertyChangeSupport.firePropertyChange("loomShafts", oldValue, loomShafts);
		calculateShaftLimit();
	}
	
	private void calculateShaftLimit() {
		int limit= Math.max(getLoomShafts() - getInitialRows() + 1, 1);
		setShaftLimit(limit);
	}
	
	/** Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     *
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
    
    /** Removes a PropertyChangeListener from the listener list for a specific property.
     * @param propertyName The name of the property to remove listener for
     * @param l The listener to remove.
     *
     */
    public void removePropertyChangeListener(String propertyName, 
            java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, l);
    }	
	
    /** Property setter for persistence only
     * 
     */
    public void setInitial(List<Integer> init) {
    	initial = new Vector<Integer>(init);
    }
    
    /** Property setter for persistence only
     * 
     */
    public void setPatternLine(List<Integer> pattern) {
    	patternLine = new Vector<Integer>(pattern);
    }
 
    /** Property setter for persistence only
     * 
     */
    public void setKey1(List<List<Boolean>> key1) {
    	this.key1 = new Vector<List<Boolean>>();
    	for (List<Boolean> l : key1) {
    		this.key1.add(new Vector<Boolean>(l));
    	}
    }
    
    /** Property setter for persistence only
     * 
     */
    public void setKey2(List<List<Boolean>> key2) {
    	this.key2 = new Vector<List<Boolean>>();
    	for (List<Boolean> l : key2) {
    		this.key2.add(new Vector<Boolean>(l));
    	}
    }
    
    // Properties
	/**
	 * The selected column in each row of the Initial.
	 */
	private Vector<Integer> initial;
	
	/**
	 * The selected column in each row of the pattern line.
	 */
	private Vector<Integer> patternLine;
	
	/** 
	 * The first key pattern, row first.
	 */
	private  Vector<List<Boolean>> key1;
	
	/** 
	 * The first key pattern, row first.
	 */
	private Vector<List<Boolean>> key2;
	
	/**
	 * If true, the telescope algorithm is used when the patterns line
	 * has to be compressed. When false, the digitise algorithm is used.
	 */
	private boolean isTelescoped;
	
	/**
	 * Number of rows in the initial grid, and by implication the key grids.
	 */
	private int initialRows;
		
	/**
	 * Number of rows in the pattern line
	 */
	private int patternLineRows;
		
	/** 
	 * Width of the ribbon that the pattern line is expanded to.
	 */
	private int ribbonWidth;
	
	/** Number of shafts available	
	 */
	private int loomShafts;
	
	/** Number of shafts to be used for the pattern. Defaults to the 
	 * shaft rule (loomShafts - initialRows + 1)
	 */
	private int shaftLimit;
	
	/**
	 * Takes a pattern line and returns a copy of it reduced to fit in the given height
	 * by the Telescope approach (i.e. wrapping, but that is used to mean something else in 
	 * The Book). 
	 * @param line The line to fit 
	 * @param height The height to fit it to
	 * @return A new line such that 0 <= line[i] < height. 
	 */
	public static List<Integer> Telescope(List<Integer> line, int height) {
		List<Integer> ret = new ArrayList<Integer>();
		for (Integer i :line) {
			ret.add(i % height);
		}
		return ret;
	}

	/**
	 * Takes a pattern line and returns a copy of it reduced to fit in the given height
	 * by the Digitizing approach.
	 * A fairly odd name for it, it's just scaling, but that is what it is called 
	 * in The Book.
	 * @param line The line to fit 
	 * @param height The height to fit it to
	 * @return A new line such that 0 <= line[i] < height. 
	 */
	public static List<Integer> Digitize(List<Integer> line, int height) {
		int max = Collections.max(line) + 1;
		if (max > height) {
			List<Integer> ret = new ArrayList<Integer>();
			for (Integer i : line) {
				ret.add(i * height / max);
			}
			return ret;
		}
		else {
			return line;
		}
	}
		
	/**
	 * Creates a Threading line by applying
	 * the pattern line to a network formed by repeating the initial to fit
	 * the dimensions of the pattern. The pattern line will be 
	 * reduced to the appropriate height if necessary.
	 * @return A set of shaft threadings.
	 */
	public List<Integer> Threading() {
		List<Integer> pattern = compressPatternLine(shaftLimit);
		List<Integer> threading = new ArrayList<Integer>();
		int initialRows = Collections.max(initial) + 1;
		int patternRows = Collections.max(patternLine) + 1;
		for (int i = 0; i < pattern.size(); i++) {
			int networkRow = initial.get(i % initial.size());
			// I'm sure there is a mathsy way of doing this but this is all 
			// I can think of...
			int row = pattern.get(i);
			while (row > networkRow) {
				networkRow += initialRows;
			}
			threading.add(networkRow % loomShafts);
		}
		return threading;
	}

	/**
	 * Performs the "cut and paste" masking of the key liftplans with the
	 * pattern line, expanded by width. 
	 * @return A liftplan.
	 */
	public List<boolean[]> Liftplan () {
		List<Integer> pattern = compressPatternLine(shaftLimit);
		List<boolean[]> liftplan = new ArrayList<boolean[]>();
		
		// The pattern line has been rotated to the vertical for this part.
		// Set the liftplan to the corresponding value from key1 where
		// the pattern ribbon is, or the value from key2 where it isn't.
		int keyColumns = key1.size();
		int keyRows = key1.get(0).size();
		for (int irow = 0; irow < pattern.size(); irow++) {
			boolean[] row = new boolean[loomShafts];
			int ribbonLeft = pattern.get(irow);
			int ribbonRight = ribbonLeft + ribbonWidth;
			for (int icol = 0; icol < loomShafts; icol++) {
				boolean inRibbon = (icol >= ribbonLeft && icol < ribbonRight) ||
						(ribbonRight > loomShafts && icol < ribbonRight % loomShafts);
				List<List<Boolean>> key = inRibbon ? key1 : key2;
				row[icol] = key.get(icol % keyColumns).get(irow % keyRows);
			}
			liftplan.add(row);
		}
		return liftplan;
	}
	
	List<Integer> compressPatternLine (int shafts) {
		return isTelescoped ? 
				Telescope(patternLine, shafts) : Digitize(patternLine, shafts);
	}
	
	// Vector is not nearly as helpful as you might expect here...
	private static void setSizeInt(Vector<Integer> collection, int size) {
		int oldSize = collection.size();
		collection.setSize(size);
		for (int i = oldSize; i < size; ++i) {
			collection.set(i, 0);
		}
	}

	private static void setSizeBool(Vector<Boolean> collection, int size) {
		int oldSize = collection.size();
		collection.setSize(size);
		for (int i = oldSize; i < size; ++i) {
			collection.set(i, false);
		}
	}

	private static void setSizeVBool(Vector<List<Boolean>> collection, int size, int otherSize) {
		int oldSize = collection.size();
		collection.setSize(size);
		for (int i = oldSize; i < size; ++i) {
			Vector<Boolean>v = new Vector<Boolean>();
			setSizeBool(v,  otherSize);
			collection.set(i, v);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Vector<List<Boolean>> deepClone (Vector<List<Boolean>> collection) {
		Vector<List<Boolean>> clone = new Vector<List<Boolean>> ();
		for (List<Boolean> l : collection) {
			clone.add((Vector<Boolean>)((Vector<Boolean>)l).clone());
		}
		return clone;
	}
	
    public boolean equals(Object obj) {
    	if (obj == null) { return false; }
    	if (obj == this) { return true; }
    	if (obj.getClass() != getClass()) {
    		return false;
    	}
    	NetworkDraft n = (NetworkDraft)obj;
    	return new EqualsBuilder()
    	.append(initial, n.initial)
    	.append(patternLine, n.patternLine)
    	.append(key1, n.key1)
    	.append(key2, n.key2)
    	.append(isTelescoped, n.isTelescoped)
    	.append(initialRows, n.initialRows)
    	.append(patternLineRows, n.patternLineRows)
    	.append(ribbonWidth, n.ribbonWidth)
    	.append(shaftLimit, n.shaftLimit)
    	.append(loomShafts, n.loomShafts)
    	.isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder()
    	.append(initial)
    	.append(patternLine)
    	.append(key1)
    	.append(key2)
    	.append(isTelescoped)
    	.append(initialRows)
    	.append(patternLineRows)
    	.append(ribbonWidth)
    	.append(shaftLimit)
    	.append(loomShafts)
		.toHashCode();
    }

    public String toString() {
    	return new StringBuilder()
    	.append(initial)
    	.append(patternLine)
    	.append(key1)
    	.append(key2)
    	.append(isTelescoped)
    	.append(",")
    	.append(initialRows)
    	.append(",")
    	.append(patternLineRows)
    	.append(",")
    	.append(ribbonWidth)
    	.toString();
    }
}
