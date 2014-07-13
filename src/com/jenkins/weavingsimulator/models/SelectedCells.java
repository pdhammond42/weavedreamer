package com.jenkins.weavingsimulator.models;

import java.util.List;

import com.jenkins.weavingsimulator.datatypes.WeftPick;

/** A set of cells selected from a grid model, which can be
 * transformed and pasted into a model.
 * Note that since they can be transformed, it is not necessarily 
 * an actually selected set of cells.
 * @author pete
 *
 */
public class SelectedCells {
	boolean[][] rows;
	
	SelectedCells (List<WeftPick> picks, GridSelection selection) {
		rows = new boolean[selection.getRows()][selection.getColumns()];
		for (int row = 0; row != selection.getRows(); row++) {
			for (int col = 0; col != selection.getColumns(); col++) {
				rows[row][col] = picks.get(row + selection.getStartRow())
						.isTreadleSelected(col + selection.getStartColumn());
			}
		}
	}
	
	SelectedCells (int rows, int columns) {
		this.rows = new boolean[rows][columns];
	}
	
	SelectedCells() {
		rows = new boolean[0][0];
	}
	
	int getRows() {
		return rows.length;
	}
	
	int getColumns() { 
		return rows.length > 0 ? rows[0].length : 0;
	}
	
	boolean getValue(int row, int column) {
		return rows[row][column];
	}

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
