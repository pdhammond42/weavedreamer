package com.jenkins.weavingsimulator.datatypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The NetworkDraft class holds the algorithms and data for Netwok drafting. An
 * instance of this class controls a WeavingDraft, building the peg plan and
 * threading according to the Network drafting rules. It hodls the necessary
 * data to do that - pattern line, keys, inital etc - and exposes them to the
 * GUI via grid models.
 * 
 * Since the data types needed are simple - mostly List<int> - I am intending to
 * do this is a fairly procedural style.
 * 
 * See "Network Drafting - an Introduction", Alice Schlein, Bridgewater Press
 * 1994.
 * 
 * @author pete
 * 
 */
public class NetworkDraft {
	/**
	 * The selected column in each row of the Initial.
	 */
	private List<Integer> intital;
	
	/**
	 * The selected column in each row of the pattern line.
	 */
	private List<Integer> patternLine;
	
	public NetworkDraft (WeavingDraft draft) {

	}
	
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
		List<Integer> ret = new ArrayList<Integer>();
		int max = Collections.max(line) + 1;
		for (Integer i : line) {
			ret.add(i * height / max);
		}
		return ret;
	}
	
	/**
	 * Given an Initial and a Pattern line, creates a Threading line by applying
	 * the pattern line to a network formed by repeating the initial to fit
	 * the dimensions of the pattern. It follows from that that the pattern line
	 * should be reduced to the appropriate height before calling.
	 * @param pattern Pattern line
	 * @param initial Initial grid
	 * @return A set of shaft threadings.
	 */
	public static List<Integer> Threading(List<Integer> pattern, List<Integer> initial) {
		List<Integer> threading = new ArrayList<Integer>();
		int initialRows = Collections.max(initial) + 1;
		int patternRows = Collections.max(pattern) + 1;
		for (int i = 0; i < pattern.size(); i++) {
			int networkRow = initial.get(i % initial.size());
			// I'm sure there is a mathsy way of doing this but this is all 
			// I can think of...
			int row = pattern.get(i);
			while (row > networkRow) {
				networkRow += initialRows;
			}
			threading.add(networkRow % patternRows);
		}
		return threading;
	}
}
