package com.jenkins.weavingsimulator.uat;

public class UndoTest extends WeavingTestCase {
	public void testUndo() {
		ui.open("testdata/103.wif");
		
		// Too many undo should be harmless.
		ui.undo();
		
		ui.setPick (0, 1);
		ui.draftIs(0, 0, blue);
		ui.undo();
		ui.draftIs(0, 0, orange);
		
		ui.setThreading(1, 0);
		ui.draftIs(0, 0, blue);
		ui.undo();
		ui.draftIs(0, 0, orange);

		ui.toggleTieup (0, 1);
		ui.draftIs(1, 0, orange);
		ui.undo();
		ui.draftIs(1, 0, blue);	
		
		ui.selectColour(0);
		ui.setThreadingColour(0);
		ui.draftIs(0, 0, blue);
		ui.undo();
		ui.draftIs(0, 0, orange);	
		
		ui.selectColour(1);
		ui.setPickColour(0);
		ui.draftIs(0, 1, orange);
		ui.undo();
		ui.draftIs(0, 1, blue);	
	}

}
