package com.jenkins.weavingsimulator.models;

/** A set of cells that can be pasted into a model.
 * Note that since they can be transformed, it is not necessarily 
 * an actually selected set of cells.
 * @author pete
 *
 */
public class PasteGrid {
	boolean[][] rows;
	int startRow = 0;
	int startColumn = 0;

	/** Construct a SelectedCells object by copying the indicated selection
	 * from the given grid model.
	 * @param model Model to copy the cells from
	 * @param selection Which cells to copy
	 */
	PasteGrid (CopyableWeavingGridModel model, GridSelection selection) {
		rows = new boolean[selection.getRows()][selection.getColumns()];
		for (int row = 0; row != selection.getRows(); row++) {
			for (int col = 0; col != selection.getColumns(); col++) {
				rows[row][col] = model.getBooleanValueAt(
						row + selection.getStartRow(),
						col + selection.getStartColumn());
			}
		}
	}
	
	/** Construct a SelectedCells object with the given capacity,
	 * with no cells initially selected.
	 * @param rows Number of rows to allocate
	 * @param columns Number of columns to allocate
	 */
	public PasteGrid (int rows, int columns) {
		this.rows = new boolean[rows][columns];
	}
	
	public PasteGrid() {
		rows = new boolean[0][0];
	}
	
	/** Returns the number of rows in the selection */
	public int getRows() {
		return rows.length;
	}
	
	/** Returns the number of columns in the selection */
	public int getColumns() { 
		return rows.length > 0 ? rows[0].length : 0;
	}
	
	/** Sets the origin of the paste grid. A grid only gets an
	 * origin when it is about to be pasted, when it is copied it 
	 * could go anywhere.
	 */
	public void setOrigin(int startRow, int startColumn) {
		this.startRow = startRow;
		this.startColumn = startColumn;
	}
	
	public int getStartRow() {
		return startRow;
	}
	
	public int getStartColumn () {
		return startColumn;
	}
	
	/** Gets the value at the requested index. */
	public boolean getValue(int row, int column) {
		return rows[row][column];
	}

	/** Sets the value at the requested index */
	public void setValue(int row, int column, boolean value) {
		rows[row][column] = value;
	}
	
	/** Returns a representation of the selection in the grid, mainly 
	* for testing.
	*/
	public String toString () {
		StringBuilder s = new StringBuilder();
		for (int r = 0; r != getRows(); r++) {
			for (int c = 0; c != getColumns(); ++c) {
				s.append(rows[r][c] ? '*' : '.');
			}
			s.append(';');
		}
		return s.toString();
	}
}
