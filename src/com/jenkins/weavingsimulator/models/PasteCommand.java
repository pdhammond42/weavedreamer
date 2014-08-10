package com.jenkins.weavingsimulator.models;

import com.jenkins.weavingsimulator.models.Command;
import com.jenkins.weavingsimulator.models.SelectedCells;

public class PasteCommand implements Command{
	CopyableWeavingGridModel model;
	int originRow;
	int originColumn;
	int previousHarnessId;
	SelectedCells selection;
	SelectedCells previous;

	
	public PasteCommand (CopyableWeavingGridModel model, int row, int column,
			SelectedCells selection) {
		this.model = model;
		this.originRow = row;
		this.originColumn = column;
		this.selection = selection;
	}
	
	public void execute() {
    	final int rowcount = Math.min(selection.getRows(), model.getRowCount() - originRow);
    	final int colcount = Math.min(selection.getColumns(), model.getColumnCount() - originColumn);
		previous = new SelectedCells (model, 
				new GridSelection(originRow, 
						originColumn, 
						originRow + rowcount, 
						originColumn+colcount));

    	for (int row = 0; row != rowcount; row++) {
    		for (int col = 0; col != colcount; col++) {
    			model.setValueAt (selection.getValue(row, col), row + originRow, col + originColumn);
    		}
    	}	
	}

	public void undo() {
    	final int rowcount = Math.min(previous.getRows(), model.getRowCount() - originRow);
    	final int colcount = Math.min(previous.getColumns(), model.getColumnCount() - originColumn);
    	
    	for (int row = 0; row != rowcount; row++) {
    		for (int col = 0; col != colcount; col++) {
    			model.setValueAt (previous.getValue(row, col), row + originRow, col + originColumn);
    		}
    	}		
	}

}
