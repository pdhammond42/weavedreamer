package com.jenkins.weavingsimulator.models;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jenkins.weavingsimulator.GridControl;
import com.jenkins.weavingsimulator.datatypes.WarpEnd;


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
    	session.setSelectedCells(new SelectedCells (this, selection));
    	thisObjectSettingSelection = false;
    }

	public void pasteSelection(int rowIndex, int columnIndex,
			CellSelectionTransform transform) {
    	SelectedCells selection = transform.Transform(session.getSelectedCells());

    	final int rowcount = Math.min(selection.getRows(), getRowCount() - rowIndex);
    	final int colcount = Math.min(selection.getColumns(), getColumnCount() - columnIndex);
    	
    	for (int row = 0; row != rowcount; row++) {
    		for (int col = 0; col != colcount; col++) {
    				setValueAt (selection.getValue(row, col), row + rowIndex, col + columnIndex);
    		}
    	}
	}
	
    /** Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param	rowIndex	the row whose value is to be queried
     * @param	columnIndex 	the column whose value is to be queried
     * @return	the value Object at the specified cell
     *
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (getBooleanValueAt(rowIndex, columnIndex)) {
        	return Color.BLACK;
        } else if (isSelected(rowIndex, columnIndex)) {
        	return Color.LIGHT_GRAY;
        } else {
        	return Color.WHITE;
        }
    }
    
	/** The usual getValueAt method returns a colur, for use in rendering the
	 * table. This returns a boolean for use in copy/paste.
	 * @param row Row to query
	 * @param column Column to query
	 * @return True if the cell is "on".
	 */
	public abstract boolean getBooleanValueAt(int row, int column);

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

	protected class ToggleEditProvider implements EditedValueProvider {
		private CopyableWeavingGridModel model;
		public ToggleEditProvider (CopyableWeavingGridModel model) {
			this.model = model;
		}
		public Object getValue(int row, int column) {
			return !model.getBooleanValueAt(row, column);
		}
	};
 }
