package com.jenkins.weavedreamer.models;

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
		if (startRow > endRow) {
			this.startRow = endRow + 1;
			this.endRow = startRow + 1;
		} else {
			this.startRow = startRow;
			this.endRow = endRow;
		}

		if (startColumn > endColumn) {
			this.startColumn = endColumn + 1;
			this.endColumn = startColumn + 1;
		} else {
			this.startColumn = startColumn;
			this.endColumn = endColumn;
		}
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
