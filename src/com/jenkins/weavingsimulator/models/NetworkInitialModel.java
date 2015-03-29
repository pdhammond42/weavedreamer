package com.jenkins.weavingsimulator.models;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NetworkInitialModel extends AbstractWeavingDraftModel {

	public NetworkInitialModel(EditingSession session) {
		super(session);
		
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	protected Command getSetValueCommand(Object aValue, int row, int column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EditedValueProvider getEditedValueProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
