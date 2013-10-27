package com.jenkins.weavingsimulator.models;

import java.awt.Color;

/**
 * Models a cell within the weaving pattern grid. color with some additional attributes.
 *
 */
public class WeavingPatternCellModel {
	Color color;
	boolean nextColumnBorder;
	boolean nextRowBorder;
	
	public WeavingPatternCellModel(Color color, boolean nextColumnBorder, boolean nextRowBorder) {
		this.color = color;
		this.nextRowBorder = nextRowBorder;
		this.nextColumnBorder = nextColumnBorder;
	}
	
	public Color color() {
		return this.color;
	}
	
	public boolean nextColumnBorder() {
		return this.nextColumnBorder;
	}
	
	public boolean nextRowBorder () {
		return this.nextRowBorder;
	}
}
