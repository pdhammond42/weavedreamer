package com.jenkins.weavingsimulator.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jenkins.weavingsimulator.datatypes.NetworkDraft;

public class PatternLineModel extends AbstractWeavingDraftModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	NetworkDraft network;	

	public PatternLineModel(EditingSession session) {
		super(session);
		network = session.getDraft().getNetwork();

		network.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                String propName = ev.getPropertyName();
                if (propName.equals("patternLineRows") || propName.equals("patternLine")) {
                    fireTableDataChanged();
                }
                else if (propName.equals("patternLineCols")) {
                	fireTableStructureChanged();
                }
            }
		});
	}

	public int getRowCount() {
		return network.getPatternLineRows();	
	}

	public int getColumnCount() {
		return network.getPatternLineCols();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return network.getPatternLine(columnIndex) == rowIndex;
	}

	@Override
	protected Command getSetValueCommand(Object aValue, int row, int column) {
		// Value is ignored. A click sets the cell. Undo restores the previous
		// value in that column.
		final int c = column;
		final int r = row;
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
