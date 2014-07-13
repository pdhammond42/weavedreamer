package com.jenkins.weavingsimulator.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/** Extends the weaving draft grid model to provide copy and paste 
 * functionality via the session object.
 * @author pete
 *
 */
public abstract class CopyableWeavingGridModel extends AbstractWeavingDraftModel {
	
	private EditingSession session;
    private GridSelection selection;
	private boolean thisObjectSettingSelection = false;

	public CopyableWeavingGridModel (EditingSession session) {
		super (session.getDraft());
		this.session = session;
        selection = new GridSelection ();
        session.addPropertyChangeListener(EditingSession.SELECTION_PROPERTY, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (!thisObjectSettingSelection) showSelection(0,0,0,0);
			}
		});
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
    	thisObjectSettingSelection = true;
    	session.setSelectedCells(new SelectedCells (draft.getPicks(), selection));
    	thisObjectSettingSelection = false;
    }

	public void pasteSelection(int rowIndex, int columnIndex,
			CellSelectionTransform transform) {
    	SelectedCells selection = transform.Transform(session.getSelectedCells());

    	final int rowcount = Math.min(selection.getRows(), getRowCount() - rowIndex);
    	final int colcount = Math.min(selection.getColumns(), getColumnCount() - columnIndex);
    	
    	for (int row = 0; row != rowcount; row++) {
    		for (int col = 0; col != colcount; col++) {
    			if (selection.getValue(row, col)) {
    				setValueAt (null, row + rowIndex, col + columnIndex);
    			}
    		}
    	}
	}

    protected boolean isSelected (int row, int column) {
    	return selection.contains(row, column);
    }    

    public boolean supportsPaste() {
    	return true;
    }
    
    public boolean canPaste() {
    	return session.getSelectedCells().getColumns() != 0 && 
    			session.getSelectedCells().getRows() != 0;
    }
 }
