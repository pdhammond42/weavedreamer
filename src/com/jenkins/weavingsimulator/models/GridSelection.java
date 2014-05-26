package com.jenkins.weavingsimulator.models;

/** Represents a selected set of cells in a grid, usually
 * either the threading or treadling.
 * @author pete
 *
 */
public class GridSelection {
	private int startRow;
	private int startColumn;
	private int endRow;
	private int endColumn;
	
	GridSelection () {
		startRow = 0;
		startColumn = 0;
		endRow = 0;
		endColumn = 0;
	}
	
	GridSelection (int startRow, int startColumn, int endRow, int endColumn) {
		this.startRow = startRow;
		this.startColumn = startColumn;
		this.endRow = endRow;
		this.endColumn = endColumn;
	}
	
	int getRows() {
		return endRow - startRow;
	}
	
	int getColumns() {
		return endColumn - startColumn;
	}
	
	int getStartRow() {
		return startRow;
	}
	
	int getStartColumn() {
		return startColumn;
	}
	
	boolean contains(int row, int column) {
		return row >= startRow && row < endRow &&
				column >= startColumn && column < endColumn;
	}
}
