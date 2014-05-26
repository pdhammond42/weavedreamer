package com.jenkins.weavingsimulator.models;

import java.util.List;

import com.jenkins.weavingsimulator.datatypes.WeftPick;

/** A set of cells selected from a grid model, which can be
 * transformed and pasted into a model.
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
	
	int getRows() {
		return rows.length;
	}
	
	int getColumns() { 
		return rows[0].length;
	}
	
	boolean getValue(int row, int column) {
		return rows[row][column];
	}
}
