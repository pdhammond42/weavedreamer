package com.jenkins.weavingsimulator.models;

/**
 * Represents an operation that can be done to a SelectedCells to implement 
 * Paste Special behaviour.
 * @author pete
 *
 */
public interface CellSelectionTransform {
	/**
	 * Return a SelectedCells that is the given selection transformed.
	 * @param from The SelectedCells to transform
	 * @return the new SelectedCells
	 */
	public PasteGrid Transform(PasteGrid from);
}
