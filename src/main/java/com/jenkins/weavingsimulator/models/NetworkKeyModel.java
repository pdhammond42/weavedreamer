package com.jenkins.weavingsimulator.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jenkins.weavingsimulator.datatypes.NetworkDraft;

public class NetworkKeyModel extends AbstractWeavingDraftModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	NetworkDraft network;	
	
	public interface KeyModelAdapter {
		String getKeyProperty();
		void set (int col, int row, boolean value);
		boolean get (int col, int row);
	}
	
	final KeyModelAdapter adapter;
	
	public NetworkKeyModel(EditingSession session, KeyModelAdapter anAdapter) {
		super(session);
		this.adapter = anAdapter;
		
		network = session.getDraft().getNetwork();

		network.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                String propName = ev.getPropertyName();
                if (propName.equals("intitialRows") || propName.equals(adapter.getKeyProperty())) {
                    fireTableDataChanged();
                }
                else if (propName.equals("initialCols")) {
                	fireTableStructureChanged();
                }
            }
        });
	}

	public int getRowCount() {
		return network.getInitialRows();
	}

	public int getColumnCount() {
		return network.getInitialCols();
	}
	
	@Override
	protected Command getSetValueCommand(Object aValue, int row, int column) {
		// A click toggles the cell. Undo restores the previous
		// value in that column.
		final int c = column;
		final int r = row;
		final boolean old_val = adapter.get(column, row);
		return new Command (){
			public void execute() {
		        adapter.set(c, r, !old_val);
			}

			public void undo() {
				adapter.set(c, r, old_val);
			}
		};
	}

	@Override
	public EditedValueProvider getEditedValueProvider() {
    	return new SetValueProvider();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return adapter.get(columnIndex, rowIndex);
	}
	
    public Class<?> getColumnClass(int columnIndex) {
        return Boolean.class;
    }    
}
