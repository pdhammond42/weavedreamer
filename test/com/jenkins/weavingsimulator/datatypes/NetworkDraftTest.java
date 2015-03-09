package com.jenkins.weavingsimulator.datatypes;

import java.awt.Color;
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
	
	@SuppressWarnings("unchecked")
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
	}
}
