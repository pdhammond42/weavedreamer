package com.jenkins.uat;

import org.junit.Test;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class UndoTest extends WeavingTestCase {
	@Test
	public void testUndo() throws IOException {
		ui.open(new File("testdata/103.wif"));
		
		// Too many undo should be harmless.
		ui.undo();
		
		ui.setPick (0, 1);
		ui.draftIs(0, 0, blue);
		ui.undo();
		ui.draftIs(0, 0, orange);
		ui.redo();
		ui.draftIs(0, 0, blue);
		ui.undo();
		
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

	@Test
	public void testUndoToStart() {
		ui.newDraft(4, 6, 24, 24, "Monochrome");
		
		ui.setPick (0, 1);
		ui.pickIs(0, 1, Color.black);
		ui.undo();
		ui.pickIs(0,1,Color.white);

		ui.setThreading(1, 0);
		ui.harnessIs(1, 0, Color.black);
		ui.undo();
		ui.harnessIs(1, 0, Color.white);
	}

	@Test
	public void testUndoPasteToStart() {
		ui.newDraft(4, 6, 24, 24, "Monochrome");	
		
		ui.setPick (0, 0);
		ui.selectPick(0, 0, 3, 3);

		ui.pasteTreadling(4, 0);
            
                
		ui.pickIs(4, 0, Color.black);
		ui.undo();	// this appears not to work	
		 ui.pickIs(4, 0, Color.white);
		
		ui.setThreading(3, 0);
		ui.selectThreading(0, 0, 3, 3);
		ui.pasteThreading(0, 4);
		ui.harnessIs(3, 0, Color.black);
		ui.undo(); // this appears not to work
		ui.endIs(3, 4, Color.white);		
	}
}
