package com.jenkins.weavingsimulator.models;

import com.jenkins.weavingsimulator.datatypes.WeavingDraft;

/** Extends the weaving draft grid model to provide copy and paste 
 * functionality via the session object.
 * @author pete
 *
 */
public abstract class CopyableWeavingGridModel extends AbstractWeavingDraftModel {
	
	private EditingSession session;
    private GridSelection selection;

	public CopyableWeavingGridModel (EditingSession session) {
		super (session.getDraft());
		this.session = session;
        selection = new GridSelection ();
    }
	    
    /** Sets the selection to be the rows [startRow..endRow)
        and the columns [startColumn..endColumn)
     */
    public void showSelection (int startRow, int startColumn, int endRow, int endColumn) {
    	selection = new GridSelection (startRow, startColumn, endRow, endColumn);
    	fireTableDataChanged();
    }
    
    /** Copies the selection to the session
     * 
     */
    public void copySelection() {
    	session.setSelectedCells(new SelectedCells (draft.getPicks(), selection));
    }
    
    
    public void pasteSelection (int rowIndex, int columnIndex) {
    	SelectedCells selection = session.getSelectedCells();
    	for (int row = 0; row != selection.getRows(); row++) {
    		for (int col = 0; col != selection.getColumns(); col++) {
    			if (selection.getValue(row, col)) {
    				setValueAt (null, row + rowIndex, col + columnIndex);
    			}
    		}
    	}
    }
    
    protected boolean isSelected (int row, int column) {
    	return selection.contains(row, column);
    }
}
