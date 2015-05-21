package com.jenkins.weavingsimulator.models;


public class PasteCommand implements Command{
	CopyableWeavingGridModel model;
	PasteGrid selection;
	PasteGrid previous;


	/** Creates a new PasteCommand that will paste the selection into the
	 * model at row, column, and store the previous state of the model in the
	 * undoSelection for undo. undoSelection is needed because the threading model,
	 * and the treadling model in non-liftplan, need more than jsut the pasted area
	 * to be able to undo.
	 * @param model The model to act on
	 * @param row The row to paste into
	 * @param column The column to paste into
	 * @param selection The selection to paste
	 * @param undoSelection The cells that need to be stored to allow undo.
	 */
	public PasteCommand (CopyableWeavingGridModel model, PasteGrid selection) {
		this.model = model;
		this.selection = selection;
		previous = model.getUndoSelection(selection);
	}
	
	public void execute() {
		paste(selection);
	}

	public void undo() {
		paste(previous);
	}
	
	private void paste (PasteGrid cells) {
    	final int rowcount = Math.min(cells.getRows(), model.getRowCount() - cells.getStartRow());
    	final int colcount = Math.min(cells.getColumns(), model.getColumnCount() - cells.getStartColumn());
    	
    	for (int row = 0; row != rowcount; row++) {
    		for (int col = 0; col != colcount; col++) {
    			model.setBooleanValueAt (cells.getValue(row, col), 
    					row + cells.getStartRow(), 
    					col + cells.getStartColumn());
    		}
    	}		
	}

}
