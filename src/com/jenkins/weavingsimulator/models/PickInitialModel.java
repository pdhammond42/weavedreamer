package com.jenkins.weavingsimulator.models;

public class PickInitialModel extends CopyableWeavingGridModel {

	public PickInitialModel(EditingSession session) {
		super(session);
		// TODO Auto-generated constructor stub
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
	public boolean getBooleanValueAt(int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBooleanValueAt(boolean value, int row, int column) {
		// TODO Auto-generated method stub

	}

	@Override
	protected PasteGrid getUndoSelection(PasteGrid selection) {
		// TODO Auto-generated method stub
		return null;
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

}
