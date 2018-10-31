/*
 * WeavingDraftTest.java
 * NetBeans JUnit based test
 * 
 * Created on April 3, 2003, 2:51 AM
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


package com.jenkins.weavedreamer.datatypes;

import java.awt.Color;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.apache.commons.lang.ArrayUtils.*;

/**
 *
 * @author ajenkins
 */
public class WeavingDraftTest extends TestCase {
    private WeavingDraft draft;
    private TestPropertyChangeListener listener;
    
    public WeavingDraftTest(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(WeavingDraftTest.class);        
        return suite;
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    protected void setUp() throws java.lang.Exception {
        draft = new WeavingDraft("Test Draft");
        listener = new TestPropertyChangeListener();
        draft.addPropertyChangeListener(listener);
    }
    
    /** Test of addPropertyChangeListener method, of class 
     * com.jenkins.weavedreamer.WeavingDraft.
     */
    public void testAddPropertyChangeListener() {
        draft.addPropertyChangeListener(listener);
        draft.setName("test");
        assertEquals("test", 
            listener.events.get("name").getNewValue());
    }
    
    /** Test of removePropertyChangeListener method, of class com.jenkins.weavedreamer.WeavingDraft. */
    public void testRemovePropertyChangeListener() {
        draft.removePropertyChangeListener(listener);
        draft.setName("test");
        assertTrue(listener.events.isEmpty());
    }
    
    
    /** Test of setPicks method, of class com.jenkins.weavedreamer.WeavingDraft. */
    public void testSetSteps() {
        draft.getTreadles().add(new Treadle());
        draft.getTreadles().add(new Treadle());
        
        List<WeftPick> picks = new LinkedList<WeftPick>();
        picks.add(new WeftPick(Color.BLACK, 2, 0));
        picks.add(new WeftPick(Color.BLACK, 2, 1));
        
        List<WeftPick> oldValue = draft.getPicks();
        draft.setPicks(picks);
        assertEquals(picks, draft.getPicks());
        
        // make sure propertyChange was fired
        BeanTestUtils.assertPropertyChangeFired(listener.events, "picks", oldValue, picks, -1);
        
        // make sure it throws exception for out of bounds pick values
        WeftPick two = new WeftPick(Color.WHITE, 3, 2);
        boolean gotException = false;
        try {
            draft.getPicks().add(two);
        } catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertTrue(gotException);
        // make sure illegal element didn't get added
        assertFalse(draft.getPicks().contains(two));
        
        // verify lower bound
        gotException = false;
        try {
            draft.getPicks().add(new WeftPick(Color.WHITE, -2, -2));
        } catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertTrue(gotException);
        
        // verify that bounds are also checked for list set
        picks.add(two);
        gotException = false;
        try {
            draft.setPicks(picks);
        } catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertTrue(gotException);
    }
        
    /** Test of setEnds method, of class com.jenkins.weavedreamer.WeavingDraft. */
    public void testSetEnds() {
        draft.setNumHarnesses(2); 
        
        draft.getTreadles().add(new Treadle());
        draft.getTreadles().add(new Treadle());
        
        List<WarpEnd> oldValue = draft.getEnds();
        List<WarpEnd> ends = new LinkedList<WarpEnd>();
        ends.add(new WarpEnd(Color.BLACK, 1));
        ends.add(new WarpEnd(Color.RED, 0));
        draft.setEnds(ends);
        assertEquals(ends, draft.getEnds());
        BeanTestUtils.assertPropertyChangeFired(listener.events, "ends",  oldValue, ends, -1);
        
        // make sure it doesn't allow out of bounds harnessIds in ends.
        WarpEnd badEnd = new WarpEnd(Color.BLACK, 2);
        boolean gotException = false;
        try { draft.getEnds().add(badEnd); }
        catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertTrue(gotException);
        // make sure illegal element didn't get added
        assertFalse(draft.getEnds().contains(badEnd));
        
        gotException = false;
        try { draft.getEnds().add(new WarpEnd(Color.BLACK, -2)); }
        catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertTrue(gotException);
        
        // verify list set also
        ends.add(new WarpEnd(Color.BLUE, 2));
        gotException = false;
        try { draft.setEnds(ends); }
        catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertTrue(gotException);
        
        // verify that setting individual end
        ends = new ObservableList<WarpEnd>();
        ends.add(new WarpEnd(Color.BLUE, 1));
        ends.add(new WarpEnd(Color.BLUE, 0));
        draft.setEnds(ends);
        listener.events.clear();
        draft.getEnds().get(0).setHarnessId(0);
        assertFalse(listener.events.isEmpty());
        BeanTestUtils.assertPropertyChangeFired(listener.events, "ends", null, draft.getEnds().get(0), 0);
        
        // make sure exception thrown for setting invalid individual end
        gotException = false;
        try {
            WarpEnd end = draft.getEnds().get(1);
            end.setHarnessId(5);
        } catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertTrue(gotException);
    }
    
    /** Test of setTreadles method, of class com.jenkins.weavedreamer.WeavingDraft. */
    public void testSetTreadles() {
        draft.setNumHarnesses(4);
        
        List<Treadle> treadles = new LinkedList<Treadle>();
        Treadle treadle = new Treadle();
        
        // test adding list of treadles
        List<Treadle> oldValue = draft.getTreadles();
        treadle.add(1);
        treadle.add(2);
        treadles.add(treadle);
        
        treadle = new Treadle();
        treadle.add(0);
        treadle.add(3);
        treadles.add(treadle);
        
        draft.setTreadles(treadles);
        assertEquals(treadle,  draft.getTreadles().get(1));
        BeanTestUtils.assertPropertyChangeFired(listener.events, "treadles", oldValue,  treadles, -1);

        // test adding single value
        treadle = new Treadle();
        treadle.add(Integer.valueOf(1));
        listener.events.clear();
        int numTreadles = draft.getTreadles().size();
        draft.getTreadles().add(treadle);
        assertTrue(draft.getTreadles().contains(treadle));
        BeanTestUtils.assertPropertyChangeFired(listener.events, "treadles", null, draft.getTreadles(), -1);
        
        // test modifying a treadle
        treadle = draft.getTreadles().get(0);
        listener.events.clear();
        treadle.add(0);
        BeanTestUtils.assertPropertyChangeFired(listener.events, "treadles", null, treadle, 0);
        
        // test adding list with invalid value
        oldValue = draft.getTreadles();
        treadle = new Treadle();
        treadle.add(4);
        treadles.add(treadle);
        boolean gotException = false;
        try { draft.setTreadles(treadles); }
        catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertTrue(gotException);
        // make sure invalid list didn't get set
        assertEquals(oldValue,  draft.getTreadles());
        
        // test adding single invalid value
        gotException = false;
        oldValue = draft.getTreadles();
        try { draft.getTreadles().add(treadle); }
        catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertTrue(gotException);
        assertEquals(oldValue,  draft.getTreadles());
        
        // make sure that removing a treadle updates any picks which reference it
        numTreadles = draft.getTreadles().size();
        draft.getPicks().add(new WeftPick(Color.WHITE, numTreadles, numTreadles - 1));
        draft.getTreadles().remove(numTreadles - 1);
        assertFalse(draft.getPicks().get(draft.getPicks().size() - 1).isTreadleSelected(numTreadles-2));
    }

        
    /** Test of setName method, of class com.jenkins.weavedreamer.WeavingDraft. */
    public void testSetName() {
        String oldVal = draft.getName();
        String name = "Test Name";
        draft.setName(name);
        assertEquals(name, draft.getName());
        BeanTestUtils.assertPropertyChangeFired(listener.events, "name", oldVal, draft.getName(), -1);
    }
    
    /** Test of setNumHarnesses method, of class com.jenkins.weavedreamer.WeavingDraft. */
    public void testSetNumHarnesses() {
        draft.setNumHarnesses(4);
        BeanTestUtils.assertPropertyChangeFired(listener.events, "numHarnesses",
                Integer.valueOf(0), Integer.valueOf(4), -1);
        
        draft.getEnds().add(new WarpEnd(Color.BLACK, 0));
        draft.getEnds().add(new WarpEnd(Color.BLACK, 1));
        draft.getEnds().add(new WarpEnd(Color.BLACK, 3));
        
        Treadle treadle = new Treadle();
        treadle.add(0);
        treadle.add(3);
        treadle.add(2);
        draft.getTreadles().add(treadle);
        
        // decrease number of harnesses
        draft.setNumHarnesses(3);
        assertEquals(3, draft.getNumHarnesses());
        
        // make sure it unsets out of range harnessIds in ends
        assertEquals(-1, draft.getEnds().get(2).getHarnessId());
        
        // make sure it removes out of range harnessIds from treadles
        assertFalse(draft.getTreadles().contains(3));
        assertEquals(2, draft.getTreadles().get(0).size());
    }

    /** Test of setDoValidation method, of class com.jenkins.weavedreamer.WeavingDraft. */
    public void testSetDoValidation() {
        boolean gotException = false;
        draft.setDoValidation(true);
        try { draft.getPicks().add(new WeftPick(Color.WHITE, 2)); }
        catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertTrue(gotException);
        
        gotException = false;
        draft.setDoValidation(false);
        try { draft.getPicks().add(new WeftPick(Color.WHITE, 2)); }
        catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertFalse(gotException);
    }    

    
    public void testGetVisibleColor() {
        draft.setNumHarnesses(2);
        draft.getEnds().add(new WarpEnd(Color.WHITE, -1));
        draft.getEnds().add(new WarpEnd(Color.RED, 0));
        draft.getTreadles().add(new Treadle(Arrays.asList(0)));
        draft.getTreadles().add(new Treadle(Arrays.asList(1)));
        draft.getPicks().add(new WeftPick(Color.GREEN, 2));
        draft.getPicks().add(new WeftPick(Color.PINK, 2, 0));
        draft.getPicks().add(new WeftPick(Color.BLUE, 2, 1));
        
        boolean gotException = false;
        
        // case 1: out of range weft end, should throw exception
        try { draft.getVisibleColor(0, draft.getPicks().size()); }
        catch (IndexOutOfBoundsException e) {
            gotException = true;
        }
        assertTrue(gotException);
        
        // case 2: out of range column, should throw exception
        gotException = false;
        try { draft.getVisibleColor(draft.getEnds().size(), 0); }
        catch (IndexOutOfBoundsException e) {
            gotException = true;
        }
        assertTrue(gotException);
        
        // case 3: warp end is not connected to a harness, and no treadle is
        // specified for pick. The latter factor is irrelevant, a warp not
        // attached to a harness will never be lifted.
        assertEquals(Color.GREEN, draft.getVisibleColor(0, 0));
        
        // case 4: warp end is connected to a harness, no
        // treadle is specified for pick
        assertEquals(Color.GREEN, draft.getVisibleColor(1, 0));
                
        // case 5: warp end not connected to harness, treadle specified
        assertEquals(Color.PINK, draft.getVisibleColor(0, 1));        
        
        // case 6: warp connected to harness which is lifted
        assertEquals(Color.RED, draft.getVisibleColor(1, 1));
        
        // case 7: warp connected to harness which is not lifted
        assertEquals(Color.BLUE, draft.getVisibleColor(1, 2));
    }
    
    public void testIsWarpVisible() {
    	// Simplest weave: 2 harnesses, 2 ends, 2 picks.
    	// Pick 0 uses treadle 0 which lifts harness 0,
    	// so end 0 is visible and end 1 is hidden.
    	draft.setNumHarnesses(2);
        draft.getEnds().add(new WarpEnd(Color.WHITE, 0));
        draft.getEnds().add(new WarpEnd(Color.RED, 1));
        draft.getTreadles().add(new Treadle(Arrays.asList(0)));
        draft.getTreadles().add(new Treadle(Arrays.asList(1)));
        draft.getPicks().add(new WeftPick(Color.PINK, 2, 0));
        draft.getPicks().add(new WeftPick(Color.BLUE, 2, 1));
        
        assertTrue (draft.isWarpVisible(0,0));
        assertFalse (draft.isWarpVisible(1,0));
        
        assertFalse (draft.isWarpVisible(0,1));
        assertTrue (draft.isWarpVisible(1,1));
    }
    
    public void testPaletteCanBeCreated() {
    	draft.setNumHarnesses(2);
        draft.getTreadles().add(new Treadle(Arrays.asList(0)));
        draft.getTreadles().add(new Treadle(Arrays.asList(1)));
        draft.getEnds().add(new WarpEnd(Color.WHITE, 0));
        draft.getEnds().add(new WarpEnd(Color.RED, 1));
        draft.getPicks().add(new WeftPick(Color.PINK, 2, 0));
        draft.getPicks().add(new WeftPick(Color.BLUE, 2, 1));    
        
        draft.createPalette();
        assertThat(draft.getPalette().getColors(), containsInAnyOrder(Color.white, Color.red, Color.pink, Color.blue));
    }
    
    public void testPropertyUpdate() {
    	draft.setProperties (4, 6, 18, 22, false, false);
    	assertThat(draft.getIsLiftplan(), is(false));
    	assertThat(draft.getEnds().size(), is(18));
    	assertThat(draft.getPicks().size(), is(22));
    	assertThat(draft.getNumHarnesses(), is(4));
    	assertThat(draft.getTreadles().size(), is(6));
    }

    public void testPropertyChangesTreadlesConsistently() {
    	draft.setProperties (4, 6, 18, 12, false, false);
    	draft.setProperties (4, 8, 18, 16, false, false);
    	assertThat(draft.getPicks().size(), is(16));
    	assertThat(draft.getTreadles().size(), is(8));
    	for (WeftPick p : draft.getPicks()) {
    		assertThat (p.getTreadles().length, is(8));
    	}
    }
    
    public void testPropertyChangePreservesDraftToLiftplan() {
    	draft.setProperties (3, 2, 8, 8, false, false);
    	/* Set up a draft like this:
    	 *  .*
    	 *  **  treadle
    	 *  *.
    	 * 
    	 *  *.
    	 *  .*  pick
    	 *  *.
    	 *  
    	 *  Convert to liftplan and check it looks like
    	 *  ..*
    	 *  .*.  "treadle"
    	 *  *..
    	 *  
    	 *  **.
    	 *  .**  pick
    	 *  **.
    	*/
    	draft.getTreadles().get(0).add(0);
    	draft.getTreadles().get(0).add(1);
    	draft.getTreadles().get(1).add(1);
    	draft.getTreadles().get(1).add(2);
    	draft.getPicks().get(0).setTreadleId(0);
    	draft.getPicks().get(1).setTreadleId(1);
    	draft.getPicks().get(2).setTreadleId(0);
    	
    	draft.setProperties(3, 2, 8, 8, true, false);
    	assertThat(draft.getIsLiftplan(), is(true));
    	assertThat(draft.getTreadles().get(0), contains(2));
    	assertThat(draft.getTreadles().get(1), contains(1));
    	assertThat(draft.getTreadles().get(2), contains(0));
    	assertThat(toObject(draft.getPicks().get(0).getTreadles()), 
    			arrayContaining(true, true, false));
    	assertThat(toObject(draft.getPicks().get(1).getTreadles()), 
    			arrayContaining(false, true, true));
    	assertThat(toObject(draft.getPicks().get(2).getTreadles()), 
    			arrayContaining(true, true, false));
    }
    
    public void testPersistence() throws IOException {
        draft.setNumHarnesses(2);
        draft.getEnds().add(new WarpEnd(Color.WHITE, -1));
        draft.getEnds().add(new WarpEnd(Color.RED, 0));
        draft.getTreadles().add(new Treadle(Arrays.asList(0)));
        draft.getTreadles().add(new Treadle(Arrays.asList(1)));
        draft.getPicks().add(new WeftPick(Color.GREEN, 2));
        draft.getPicks().add(new WeftPick(Color.PINK, 2, 0));
        draft.getPicks().add(new WeftPick(Color.BLUE, 2, 1));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLEncoder enc = new XMLEncoder(os);
        enc.writeObject(draft);
        enc.close();
        
        FileOutputStream out = new FileOutputStream("out.xml");
        out.write(os.toByteArray());
        out.close();
        
        ByteArrayInputStream ins = new ByteArrayInputStream(os.toByteArray());
        XMLDecoder dec = new XMLDecoder(ins);
        WeavingDraft newDraft = (WeavingDraft)dec.readObject();
        dec.close();
        
        assertThat (newDraft, is(equalTo (draft)));
    }
    
    public void testLpPersistence() throws IOException {
        draft.setNumHarnesses(2);
        draft.getEnds().add(new WarpEnd(Color.WHITE, -1));
        draft.getEnds().add(new WarpEnd(Color.RED, 0));
        draft.getTreadles().add(new Treadle(Arrays.asList(0)));
        draft.getTreadles().add(new Treadle(Arrays.asList(1)));
        draft.getPicks().add(new WeftPick(Color.GREEN, 2));
        draft.getPicks().add(new WeftPick(Color.PINK, 2, 0));
        draft.getPicks().add(new WeftPick(Color.BLUE, 2, 1));
        draft.setIsLiftplan(true);
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLEncoder enc = new XMLEncoder(os);
        enc.writeObject(draft);
        enc.close();
        
        FileOutputStream out = new FileOutputStream("out.xml");
        out.write(os.toByteArray());
        out.close();
        
        ByteArrayInputStream ins = new ByteArrayInputStream(os.toByteArray());
        XMLDecoder dec = new XMLDecoder(ins);
        WeavingDraft newDraft = (WeavingDraft)dec.readObject();
        dec.close();
        assertThat(newDraft, is(equalTo(draft)));
    }
    
    public void testNetworkPersistence() throws IOException {
        draft.setProperties(6, 4, 8, 8, true, true);
        NetworkDraft net = draft.getNetwork();
        net.setInitialRows(2);
        net.setInitialCols(2);
        net.setPatternLineRows(8);
        net.setPatternLineCols(8);
        net.setPatternLineRow(1, 2);
        net.setPatternLineRow(2, 2);
        net.setKey1(1, 1, true);
        net.setKey2(0, 0, true);
        net.setRibbonWidth(3);
        net.setInitialRow(1, 1);
                
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLEncoder enc = new XMLEncoder(os);
        enc.writeObject(draft);
        enc.close();
        
        FileOutputStream out = new FileOutputStream("out.xml");
        out.write(os.toByteArray());
        out.close();
        
        ByteArrayInputStream ins = new ByteArrayInputStream(os.toByteArray());
        XMLDecoder dec = new XMLDecoder(ins);
        WeavingDraft newDraft = (WeavingDraft)dec.readObject();
        dec.close();
        assertThat(newDraft.getNetwork(), is(equalTo(draft.getNetwork())));
    }   
    
    public void testEquality() {
    	WeavingDraft d1 = new WeavingDraft();
    	WeavingDraft d2 = new WeavingDraft();   
    	assertThat(d1, is(d2));
    	d1.setName("something");
    	assertThat(d1, is(not(d2)));
    	d2.setName("something");
    	assertThat(d1, is(d2));
    }
}
