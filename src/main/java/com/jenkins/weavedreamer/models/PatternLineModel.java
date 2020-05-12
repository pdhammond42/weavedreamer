package com.jenkins.weavedreamer.models;

import com.jenkins.weavingsimulator.datatypes.NetworkDraft;
import java.awt.Rectangle;

public class PatternLineModel extends AbstractWeavingDraftModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	NetworkDraft network;	

	public PatternLineModel(EditingSession session) {
		super(session);
		network = session.getDraft().getNetwork();

		network.addPropertyChangeListener(ev -> {
            String propName = ev.getPropertyName();
            if (propName.equals("patternLineRows") || propName.equals("patternLine")) {
                fireTableDataChanged();
            }
            else if (propName.equals("patternLineCols")) {
                fireTableStructureChanged();
            }
        });
	}

        private int getharnessId(int row){
            return getRowCount()- row-1;
        
        //return row;
        }
        
        public Rectangle getCurrentDisplayCell() {
            Rectangle cursorSelection;
            cursorSelection= this.getCurrentCell();

            return new Rectangle(cursorSelection.x,this.getharnessId(cursorSelection.y),cursorSelection.width,cursorSelection.height);
    }
        
        
	public int getRowCount() {
		return network.getPatternLineRows();	
	}

	public int getColumnCount() {
		return network.getPatternLineCols();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
                int r= getharnessId(rowIndex);
                
		return network.getPatternLine(columnIndex) == r;
	}

	@Override
	protected Command getSetValueCommand(Object aValue, int row, int column) {
		// Value is ignored. A click sets the cell. Undo restores the previous
		// value in that column.
		final int c = column;
		final int r = getharnessId(row);
		final int old_row = network.getPatternLine(column);
		return new Command (){
			public void execute() {
		        network.setPatternLineRow(c, r);
			}

			public void undo() {
		        network.setPatternLineRow(c, old_row);
			}
		};
	}

	@Override
	public EditedValueProvider getEditedValueProvider() {
		return new SetValueProvider();
	}

    public Class<?> getColumnClass(int columnIndex) {
        return Boolean.class;
    }  
}
