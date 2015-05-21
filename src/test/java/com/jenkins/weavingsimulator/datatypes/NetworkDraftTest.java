package com.jenkins.weavingsimulator.datatypes;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.lang.ArrayUtils;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import junit.framework.TestCase;

public class NetworkDraftTest extends TestCase {
	private WeavingDraft draft;
	
	public void setUp() {
        draft = new WeavingDraft("TestDraft");
        draft.setNumHarnesses(2);
        draft.getEnds().add(new WarpEnd(Color.BLUE, 0));
        draft.getEnds().add(new WarpEnd(Color.GREEN, 1));
	}
	
	public void testPatternLineCanBeTelescoped() {
		List<Integer> pl = Arrays.asList(1, 2, 3, 4, 5, 4, 5, 8, 14);
		
		assertThat(NetworkDraft.Telescope(pl, 4), 
				contains(1, 2, 3, 0, 1, 0, 1, 0, 2));
	}
		
	public void testPatternLineCanBeDigitized() {
		List<Integer> pl = Arrays.asList(0, 1, 2, 3, 4, 5, 8, 9, 10, 11);
		
		assertThat(NetworkDraft.Digitize(pl, 4), 
				contains(0, 0, 0, 1, 1, 1, 2, 3, 3, 3));
	}

	public void testPatternLineCanBeDigitizedOnOddBoundaries() {
		List<Integer> pl = Arrays.asList(0, 1, 12);
		
		assertThat(NetworkDraft.Digitize(pl, 4), 
				contains(0, 0, 3));
	}
	
	public void testPatternLineCanProjectToNetwork() {
		// Test case taken from Schlein p28.
		List<Integer> initial = Arrays.asList(3, 2, 1, 0);
		List<Integer> pattern = Arrays.asList
				(14, 14, 15, 15, 15, 15, 15,  0, 
				  1,  2,  3,  4,  5,  6,  7,  8, 
				  8,  9, 10, 11, 12, 13, 14, 15);
		assertThat(NetworkDraft.Threading (pattern, initial), 
				contains(15, 14,  1,  0, 15,  2, 1, 0, 
						  3,  2,  5,  4,  7,  6, 9, 8, 
					  	 11, 10, 13, 12, 15, 14, 1, 0));
	}
	
	public void testLiftPlanCanBeCreatedFromPatternAndKeys() {
		// Schlein calls it a "peg plan", but it is a liftplan everywhere
		// else in here.
		List<Integer> pattern = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11);
		List<List<Boolean>> key1 = Arrays.asList(
				Arrays.asList(true, true, true, false),
				Arrays.asList(true, true, false, true),
				Arrays.asList(true, false, true, true),
				Arrays.asList(false, true, true, true)
				);
		List<List<Boolean>> key2 = Arrays.asList(
				Arrays.asList(true, false, false, false),
				Arrays.asList(false, true, false, false),
				Arrays.asList(false, false, true, false),
				Arrays.asList(false, false, false, true)
				);		
		int width=3;
		// boolean[] here for easy interfacing to WeftPick.
		List<boolean[]> liftplan =
				NetworkDraft.Liftplan (pattern, key1, key2, width); 
		
		// Should be true, true true from col 0-2 key1, then false from col 3 of key2, then 2 repeats of key2.
		assertThat(ArrayUtils.toObject(liftplan.get(0)), is(arrayContaining(true, true, true, false, true, false, false, false, true, false, false, false)));
	}
	
	// String the lot together 
	public void testNetworkAccess(){
		NetworkDraft draft = new NetworkDraft();
		
		draft.setInitialRows (4);
		draft.setInitialCols(4);
		
		assertThat(draft.getInitial().size(), is(4));
		assertThat(draft.getKey1().size(), is(4));
		assertThat(draft.getKey1().get(0).size(), is(4));
		assertThat(draft.getKey2().size(), is(4));
		assertThat(draft.getKey2().get(0).size(), is(4));

		draft.setPatternLineRows(16);
		draft.setPatternLineCols(24);

		draft.setInitialRow(0, 3);
		draft.setInitialRow(1, 2);
		draft.setInitialRow(2, 1);
		draft.setInitialRow(3, 0);
		draft.setPatternLineRow(0, 14);
		draft.setPatternLineRow(1, 14);
		draft.setPatternLineRow(2, 15);
		draft.setPatternLineRow(3, 15);
		draft.setPatternLineRow(4, 15);
		draft.setPatternLineRow(5, 15);
		draft.setPatternLineRow(6, 15);
		draft.setPatternLineRow(7, 0);
		draft.setPatternLineRow(8, 1);
		draft.setPatternLineRow(9, 2);
		draft.setPatternLineRow(10, 3);
		draft.setPatternLineRow(11, 4);
		draft.setPatternLineRow(12, 5);
		draft.setPatternLineRow(13, 6);
		draft.setPatternLineRow(14, 7);
		draft.setPatternLineRow(15, 8);
		draft.setPatternLineRow(16, 8);
		draft.setPatternLineRow(17, 9);
		draft.setPatternLineRow(18, 10);
		draft.setPatternLineRow(19, 11);
		draft.setPatternLineRow(20, 12);
		draft.setPatternLineRow(21, 13);
		draft.setPatternLineRow(22, 14);
		draft.setPatternLineRow(23, 15);
		
		assertThat(draft.getKey1().size(), is(4));
		assertThat(draft.getKey1().get(0).size(), is(4));
		
		assertThat(draft.Threading(16), 
				contains(15, 14,  1,  0, 15,  2, 1, 0, 
						  3,  2,  5,  4,  7,  6, 9, 8, 
					  	 11, 10, 13, 12, 15, 14, 1, 0));
		
		draft.setRibbonWidth(3);
		for (int x =0; x < 4; ++x) {
			for (int y=0; y<4; ++y) {
				draft.setKey1(x, y, true);
			}
		}
		
		List<boolean[]> liftplan = draft.Liftplan(16);
		assertThat (ArrayUtils.toObject(liftplan.get(0)), 
				is(arrayContaining(
						true, false, false, false, 
						false, false, false, false,
						false, false, false, false,
						false, false, true, true)));
		assertThat (ArrayUtils.toObject(liftplan.get(7)), 
				is(arrayContaining(
						true, true, true, false, 
						false, false, false, false,
						false, false, false, false,
						false, false, false, false)));	
		}
	
	public void testResizeInitial() {
		NetworkDraft draft = new NetworkDraft();
		
		draft.setInitialRows (4);
		draft.setInitialCols(4);
		
		assertThat(draft.getInitial().size(), is(4));
		assertThat(draft.getKey1().size(), is(4));
		assertThat(draft.getKey1().get(0).size(), is(4));
		assertThat(draft.getKey2().size(), is(4));
		assertThat(draft.getKey2().get(0).size(), is(4));
		
		draft.setInitialRow(0, 3);
		draft.setInitialRow(1, 2);
		draft.setInitialRow(2, 1);
		draft.setInitialRow(3, 0);
		
		draft.setKey1 (0,0,true);
		draft.setKey1 (1,1,true);
		draft.setKey1 (2,2,true);
		draft.setKey1 (3,3,true);
		
		draft.setKey2 (0,3,true);
		draft.setKey2 (1,2,true);
		draft.setKey2 (2,1,true);
		draft.setKey2 (3,0,true);
		
		draft.setInitialCols(6);
		draft.setInitialRows(5);
		
		assertThat(draft.getInitial().size(), is(6));
		assertThat(draft.getKey1().size(), is(6));
		assertThat(draft.getKey1().get(0).size(), is(5));
		assertThat(draft.getKey2().size(), is(6));
		assertThat(draft.getKey2().get(0).size(), is(5));
		assertThat(draft.getInitial(), contains(3,2,1,0,0,0));
		assertThat(draft.getKey1().get(0), contains(true, false, false, false, false));
		assertThat(draft.getKey2().get(0), contains(false, false, false, true, false));
	}
	
	private class Listener implements PropertyChangeListener {
		public boolean notified = false;
		public PropertyChangeEvent event;
		public void propertyChange(PropertyChangeEvent evt) {
			notified = true;
			event = evt;
		}
	};
	
	public void testNotifyKeyChange() {
		NetworkDraft draft = new NetworkDraft();
		draft.setInitialRows (4);
		draft.setInitialCols(4);
		
		Listener l = new Listener();
		draft.addPropertyChangeListener("key1", l);
		draft.setKey1(1, 1, true);
		assertThat(l.notified, is(true));
	}
}
