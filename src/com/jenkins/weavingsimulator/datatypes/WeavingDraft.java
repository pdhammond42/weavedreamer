/*
 * WeavingDraft.java
 * 
 * Created on April 2, 2003, 8:46 PM
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
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 *
 * @author  ajenkins
 */
public class WeavingDraft {
    
    /** Utility field used by bound properties. */
    private PropertyChangeSupport propertyChangeSupport =
        new PropertyChangeSupport(this);

    /** will be registered with ends list */
    private EndsChangedListener endsChangeListener =
            new EndsChangedListener();
    
    /** will be registered with each end */
    private EndChangeListener endChangeListener =
        new EndChangeListener();

    /** will be registered with the treadles list */
    private TreadlesChangedListener treadlesChangedListener = 
            new TreadlesChangedListener();
    
    /** will be registered with each treadle */
    private TreadleChangedListener treadleChangedListener =
        new TreadleChangedListener();
    
    /** will be registered with the picks list */
    private PicksChangedListener picksChangedListner =
        new PicksChangedListener();
    
    /** will be registered with each pick */
    private PickChangedListener pickChangedListener =
        new PickChangedListener();
    
    /** List<WeftPick>: Holds value of property picks. */
    private ObservableList<WeftPick> picks;
    
    /** List<WarpEnd>: Holds value of property ends. */
    private ObservableList<WarpEnd> ends;
    
    /** List<List<Boolean> >: Holds value of property treadles. */
    private ObservableList<Treadle> treadles;
    
    /** Holds value of property name. */
    private String name;
    
    /** Holds value of property numHarnesses. */
    private int numHarnesses = 0;
    
    private Palette palette;
    
    /** Holds value of property doValidation.   If this property is false, then
     * certain validations in property set functions, that depend on the value
     * of other properties, won't be done.  This is to support XMLDecode, since
     * I can't control what order it sets properties in, so I don't want it to,
     * for instance, cause an error because it sets picks before numHarnesses.
     * Therefore, the object is initially created with doValidation off.  After
     * creating an instance, doValidation should be turned on.
     */
    private boolean doValidation = false;    
    
    /** Creates a new instance of WeavingDraft.  This constructor is meant to
     * be used by XMLDecode, since it creates the object with no name, and
     * with doValidation set to false.  To programmatically create an object,
     * use the WeavingDraft(String) constructor.
     */
    public WeavingDraft() {
        setName("");
        setPicks(new ObservableList<WeftPick>());
        setEnds(new ObservableList<WarpEnd>());
        setTreadles(new ObservableList<Treadle>());
    }
    
    /** Initializes object with name property set to name, and doValidation set
     * to true.  This is the constructor that should normally be used.
     */
    public WeavingDraft(String name) {
        this();
        setName(name);
        setDoValidation(true);
    }
    
    /** Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     *
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }
    
    /** Adds a PropertyChangeListener to the listener list for a specific property.
     * @param propertyName the name of the property to listen for changes on.
     * @param l The listener to add.
     *
     */
    public void addPropertyChangeListener(String propertyName, 
            java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, l);
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
    
    /** Getter for property picks.
     * @return Value of property picks.
     *
     */
    public List<WeftPick> getPicks() {
        return this.picks;
    }
    
    /** Setter for property picks.
     * @param picks New value of property picks.
     *
     */
    public void setPicks(List<WeftPick> picks) {
        for (WeftPick pick :  picks)
            validatePick( pick);
        
        ObservableList<WeftPick> oldPicks = this.picks;
        if (oldPicks != null) {
            oldPicks.removeListChangeListener(picksChangedListner);
            for (WeftPick pick : oldPicks)
                pick.removePropertyChangeListener(pickChangedListener);
        }
        
        this.picks = new ObservableList<WeftPick>( picks);
        this.picks.addListChangeListener(picksChangedListner);
        for (WeftPick pick : this.picks)
            pick.addPropertyChangeListener(pickChangedListener);
        
        propertyChangeSupport.firePropertyChange("picks", oldPicks, 
                this.picks);
    }
    
    /** Getter for property ends.
     * @return Value of property ends.
     *
     */
    public List<WarpEnd> getEnds() {
        return this.ends;
    }
    
    /** Setter for property ends.
     * @param ends New value of property ends.
     *
     */
    public void setEnds(List<WarpEnd> ends) {
        for (WarpEnd end : ends)
            validateEnd(end);
        
        ObservableList<WarpEnd> oldEnds = this.ends;
        if (oldEnds != null) {
            oldEnds.removeListChangeListener(endsChangeListener);
            for (WarpEnd end : oldEnds)
                end.removePropertyChangeListener(endChangeListener);
        }
        this.ends = new ObservableList<WarpEnd>(ends);
        this.ends.addListChangeListener(endsChangeListener);
        
        for (WarpEnd end : this.ends)
            end.addPropertyChangeListener(endChangeListener);

        propertyChangeSupport.firePropertyChange("ends", oldEnds, 
                this.ends);
    }
    
    /** Getter for property treadles.
     * @return Value of property treadles.
     *
     */
    public List<Treadle> getTreadles() {
        return this.treadles;
    }
    
    /** Setter for property treadles.
     * @param treadles New value of property treadles.
     *
     */
    public void setTreadles(List<Treadle> treadles) {
        for (Treadle treadle : treadles)
            validateTreadle(treadle);
        
        ObservableList<Treadle> oldTreadles = this.treadles;
        if (oldTreadles != null) {
            oldTreadles.removeListChangeListener(treadlesChangedListener);
            for (Treadle treadle : oldTreadles) {
                treadle.removeListChangeListener(treadleChangedListener);
            }
        }
        this.treadles = new ObservableList<Treadle>(treadles);
        this.treadles.addListChangeListener(treadlesChangedListener);
        if (oldTreadles != null) fixUpSteps(oldTreadles.size());
        for (Treadle treadle : this.treadles)
            treadle.addListChangeListener(treadleChangedListener);

        propertyChangeSupport.firePropertyChange("treadles", oldTreadles, 
                this.treadles);
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName() {
        return this.name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange("name", oldName, name);
    }
    
    /** Getter for property numHarnesses.
     * @return Value of property numHarnesses.
     *
     */
    public int getNumHarnesses() {
        return this.numHarnesses;
    }
    
    /** Setter for property numHarnesses.
     * @param numHarnesses New value of property numHarnesses.
     *
     */
    public void setNumHarnesses(int numHarnesses) {
        int oldNumHarnesses = this.numHarnesses;
        this.numHarnesses = numHarnesses;
        
        // if numHarneses decreased, unset any harnessIds or treadles in
        // ends if they're out of range
        if (numHarnesses < oldNumHarnesses) {
            // unset any harnessIds in ends if they're out of range
            for (WarpEnd end : ends) {
                if (end.getHarnessId() >= numHarnesses) {
                    end.setHarnessId(-1);
                }
            }
            
            // remove any invalid treadle tieups
            for (Treadle tr : treadles) {
                for (Iterator<Integer> it = tr.iterator(); it.hasNext(); ) {
                    int harnessId = it.next();
                    if (harnessId >= numHarnesses)
                        it.remove();
                }
            }
        }
        
        propertyChangeSupport.firePropertyChange("numHarnesses", 
                oldNumHarnesses, numHarnesses);
    }
    
    /**
     * Sets the palette used by this draft. In the present design this is simply
     * held and persisted by the draft; ends and picks still deal with actual colors
     * which are not directly tied to this palette.  In future this might be changed
     * to defien ends and picks in terms of palette index.
     * @param palette
     */
    public void setPalette (Palette palette) {
        Palette oldPalette = this.palette;
        this.palette = palette; 
        propertyChangeSupport.firePropertyChange("palette", oldPalette, palette);
    }
    
    public Palette getPalette() {
    	return this.palette;
    }
    
    /** Setter for property doValidation.
     * @param doValidation New value of property doValidation.
     *
     */
    public void setDoValidation(boolean doValidation) {
        this.doValidation = doValidation;
    }

    /**
     * Gets the color which would be visible in the woven fabric at the grid 
     * square indicated by {@code warpThread} and {@code weftThread}.
     */
    public Color getVisibleColor(int warpThread, int weftThread) {        
        WarpEnd end = getEnds().get(warpThread);
        WeftPick pick = getPicks().get(weftThread);

        return isWarpVisible(warpThread, weftThread) ? 
        		end.getColor() : pick.getColor();
    }
    
    /**
     * Returns true if the warp is visible at the given end, pick position, or
     * false if the weft is visible.
     * @param warpThread number of the warp thread
     * @param weftThread number of the weft thread
     * @return true if the warp is visible
     */
    public boolean isWarpVisible (int warpThread, int weftThread) {
        WarpEnd end = getEnds().get(warpThread);
        WeftPick pick = getPicks().get(weftThread);
        
        if (end.getHarnessId() == -1) {
                return false;
        } else {
        	// Return true if the end is attached to a harness that is selected
        	// by the current pick's treadling.
        	for (int treadle = 0; treadle < getTreadles().size(); treadle++) {
        		if (getTreadles().get(treadle).contains(end.getHarnessId()) &&
        			pick.isTreadleSelected(treadle)) {
        			return true;
        		}
        	}
        	return false;
        }	
    }
    
    /**
     * Attempt to create a new palette using the colors currently in use.
     * Intended for use after loading an old draft that does not have a palette.
     */
    public void createPalette() {
    	Set<Color> colors = new HashSet<Color>();
    	for (WarpEnd end : ends) {
    		colors.add(end.getColor());
    	}
    	for (WeftPick pick : picks) {
    		colors.add(pick.getColor());
    	}
    	palette = new Palette(new ArrayList<Color>(colors), "custom");
    }
    
    /** validates that newStep is in the correct range.
     * @exception IllegalArgumentException if newStep is not in correct range.
     */
    private void validatePick(WeftPick newPick)
        throws IllegalArgumentException 
    {
        if (doValidation) {
        	newPick.validate(treadles.size());
        }
    }
    
    private void validateEnd(WarpEnd newEnd)
        throws IllegalArgumentException
    {
        if (doValidation) {
            if (newEnd.getHarnessId() < -1 ||
                newEnd.getHarnessId() >= numHarnesses)
                throw new IllegalArgumentException("Illegal harness value in end " +
                    newEnd.getHarnessId());
        }
    }
    
    private void validateTreadle(Treadle newTreadle) 
        throws IllegalArgumentException
    {
        if (doValidation) {
            for (int harnessId : newTreadle) {
                if (harnessId < 0 || harnessId >= numHarnesses)
                    throw new IllegalArgumentException("Illegal treadle tie up value: " +
                        harnessId);
            }
        }
    }

    /** fix up picks that refer to out of range treadles. 
     * This should be called whenever the number of treadles changes.
     */
    private void fixUpSteps(int oldCount) {
        // unset any picks that use the removed treadle
    	if (oldCount > treadles.size()) {
    		for (WeftPick pick : picks) {
    			pick.setTreadleCount(treadles.size());
    		}
    	}
    }

    /** find the index of an object in a list, using identity instead of equals to compare. */
    private <T> int identityIndexOf(List<T> list, T element) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == element)
                return i;
        }
        return -1;
    }

    private class TreadlesChangedListener implements ListChangeListener<Treadle> {
        
        public void itemAdded(ListChangedEvent<Treadle> event) {
            Treadle treadle = event.getNewValue();
            try { validateTreadle(treadle); } 
            catch (IllegalArgumentException e) {
                treadles.remove(event.getItemIndex());
                throw e;
            }
            treadle.addListChangeListener(treadleChangedListener);
            propertyChangeSupport.firePropertyChange("treadles", null, treadles);
        }
        
        public void itemChanged(ListChangedEvent<Treadle> event) {
            Treadle treadle = event.getNewValue();
            try { validateTreadle(treadle); } 
            catch (IllegalArgumentException e) {
                treadles.set(event.getItemIndex(), event.getOldValue());
                throw e;
            }
            treadle.addListChangeListener(treadleChangedListener);
            propertyChangeSupport.fireIndexedPropertyChange("treadles", event.getItemIndex(), 
                event.getOldValue(), treadle);
        }
        
        public void itemRemoved(ListChangedEvent<Treadle> event) {
            fixUpSteps(treadles.size() + 1);
            propertyChangeSupport.firePropertyChange("treadles", null, treadles);
        }
    }

    /** listener for individual treadles */
    private class TreadleChangedListener implements ListChangeListener<Integer> {
        
        public void itemAdded(ListChangedEvent<Integer> event) {
            Treadle treadle = (Treadle)event.getSource();
            try { validateTreadle(treadle); } 
            catch (IllegalArgumentException e) {
                treadle.remove(event.getItemIndex());
                throw e;
            }
            propertyChangeSupport.fireIndexedPropertyChange("treadles", 
                identityIndexOf(treadles, treadle), null, treadle);
        }
        
        public void itemChanged(ListChangedEvent<Integer> event) {
            Treadle treadle = (Treadle)event.getSource();
            try { validateTreadle(treadle); } 
            catch (IllegalArgumentException e) {
                treadle.set(event.getItemIndex(), event.getOldValue());
                throw e;
            }
            propertyChangeSupport.fireIndexedPropertyChange("treadles", 
                identityIndexOf(treadles, treadle), 
                null, treadle);
        }
        
        public void itemRemoved(ListChangedEvent<Integer> event) {
            fixUpSteps(event.getOldValue());
            Treadle treadle = (Treadle)event.getSource();
            propertyChangeSupport.fireIndexedPropertyChange("treadles", identityIndexOf(treadles, treadle), 
                treadle, null);
        }
    }

    private class PicksChangedListener implements ListChangeListener<WeftPick> {
        
        public void itemAdded(ListChangedEvent<WeftPick> event) {
            WeftPick pick = event.getNewValue();
            try { validatePick(pick); }
            catch (IllegalArgumentException e) {
                picks.remove(event.getItemIndex());
                throw e;
            }
            pick.addPropertyChangeListener(pickChangedListener);
            propertyChangeSupport.firePropertyChange("picks", null, picks);
        }
        
        public void itemChanged(ListChangedEvent<WeftPick> event) {
            WeftPick pick = event.getNewValue();
            try { validatePick(pick); }
            catch (IllegalArgumentException e) {
                picks.set(event.getItemIndex(), event.getOldValue());
                throw e;
            }
            pick.addPropertyChangeListener(pickChangedListener);
            propertyChangeSupport.fireIndexedPropertyChange("picks", event.getItemIndex(), 
                event.getOldValue(), event.getNewValue());
        }
        
        public void itemRemoved(ListChangedEvent<WeftPick> event) {
            propertyChangeSupport.firePropertyChange("picks", null, picks);
        }
        
    }
    
    
    private class EndsChangedListener implements ListChangeListener<WarpEnd> {
        
        public void itemAdded(ListChangedEvent<WarpEnd> event) {
            WarpEnd end = event.getNewValue();
            try { validateEnd(end); }
            catch (IllegalArgumentException e) {
                ends.remove(event.getItemIndex());
                throw e;
            }
            end.addPropertyChangeListener(endChangeListener);
            propertyChangeSupport.firePropertyChange("ends", null, ends);
        }
        
        public void itemChanged(ListChangedEvent<WarpEnd> event) {
            WarpEnd end = event.getNewValue();
            try { validateEnd(end); }
            catch (IllegalArgumentException e) {
                ends.set(event.getItemIndex(), event.getOldValue());
                throw e;
            }
            end.addPropertyChangeListener(endChangeListener);
            propertyChangeSupport.fireIndexedPropertyChange("ends", event.getItemIndex(), 
                event.getOldValue(), event.getNewValue());
        }
        
        public void itemRemoved(ListChangedEvent<WarpEnd> event) {
            propertyChangeSupport.firePropertyChange("ends", null, ends);
        }
        
    }

    private class PickChangedListener implements java.beans.PropertyChangeListener {
        
        /** This method gets called when a bound property is changed.
         * @param evt A PropertyChangeEvent object describing the event source
         *   	and the property that has changed.
         *
         */
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            WeftPick pick = (WeftPick)evt.getSource();
            try { validatePick(pick); }
            catch (IllegalArgumentException e) {
                pick.setTreadle((Integer)evt.getOldValue(), true);
                throw e;
            }
            propertyChangeSupport.fireIndexedPropertyChange("picks", identityIndexOf(picks, pick),
                null, pick);
        }
        
    }

    /** This is a listener to register with each end, so if a end is
     * changed, we can still throw a propertyChangeEvent.
     */
    private class EndChangeListener implements java.beans.PropertyChangeListener {
        
        /** This method gets called when a bound property is changed.
         * @param evt A PropertyChangeEvent object describing the event source
         *   	and the property that has changed.
         *
         */
        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            WarpEnd end = (WarpEnd)evt.getSource();
            try { validateEnd(end); }
            catch (IllegalArgumentException e) {
                end.setHarnessId((Integer)evt.getOldValue());
                throw e;
            }
            propertyChangeSupport.fireIndexedPropertyChange("ends", identityIndexOf(ends, end),
                null, end);
        }
        
    }        
}
