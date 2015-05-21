package com.jenkins.weavingsimulator.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jenkins.weavingsimulator.datatypes.NetworkDraft;

public class NetworkInitialModel extends AbstractWeavingDraftModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	NetworkDraft network;

	public NetworkInitialModel(EditingSession session) {
		super(session);
		network = session.getDraft().getNetwork();

		network.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent ev) {
				String propName = ev.getPropertyName();
				if (propName.equals("intitialRows")
						|| propName.equals("initial")) {
					fireTableDataChanged();
				} else if (propName.equals("initialCols")) {
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
		// Value is ignored. A click sets the cell. Undo restores the previous
		// value in that column.
		final int c = column;
		final int r = row;
		final int old_row = network.getInitial(column);
		return new Command() {
			public void execute() {
				network.setInitialRow(c, r);
			}

			public void undo() {
				network.setInitialRow(c, old_row);
			}
		};
	}

	@Override
	public EditedValueProvider getEditedValueProvider() {
		return new SetValueProvider();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return network.getInitial(columnIndex) == rowIndex;
	}

	public Class<?> getColumnClass(int columnIndex) {
		return Boolean.class;
	}
}
