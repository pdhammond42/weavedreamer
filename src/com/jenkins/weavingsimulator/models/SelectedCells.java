package com.jenkins.weavingsimulator.models;

/** A set of cells selected from a grid model, which can be
 * transformed and pasted into a model.
 * Note that since they can be transformed, it is not necessarily 
 * an actually selected set of cells.
 * @author pete
 *
 */
public class SelectedCells {
	boolean[][] rows;

	/** Construct a SelectedCells object by copying the indicated selection
	 * from the given grid model.
	 * @param model Model to copy the cells from
	 * @param selection Which cells to copy
	 */
	SelectedCells (CopyableWeavingGridModel model, GridSelection selection) {
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
	public SelectedCells (int rows, int columns) {
		this.rows = new boolean[rows][columns];
	}
	
	public SelectedCells() {
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
