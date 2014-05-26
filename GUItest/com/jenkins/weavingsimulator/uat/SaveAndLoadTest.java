package com.jenkins.weavingsimulator.uat;

import java.awt.Color;
import java.io.File;

public class SaveAndLoadTest extends WeavingTestCase {
	public void testLoadAndSave() {
		File leftover = new File("testdata/103.wsml");
		if (leftover.exists()) leftover.delete();
		
		ui.open("testdata/103.wif");
		ui.draftIs(0, 0, orange);
		ui.draftIs(0, 1, blue);
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs (0, 1, Color.WHITE);
		ui.endIs (0,0, Color.BLACK);
		ui.endIs (0,1, Color.WHITE);
		
		ui.saveAs("testdata/103.wsml");
		ui.close();
		
		ui.open("testdata/103.wsml");
		ui.draftIs(0, 0, orange);
		ui.draftIs(0, 1, blue);		
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs (0, 1, Color.WHITE);
		ui.endIs (0, 0, Color.BLACK);
		ui.endIs (0, 1, Color.WHITE);
	}
	
	public void testLoadFileSavedFrom2_5() {
		ui.open("testdata/103-2.5.wsml");
		ui.draftIs(0, 0, orange);
		ui.draftIs(0, 1, blue);				
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs (0, 1, Color.WHITE);
		ui.endIs (0, 0, Color.BLACK);
		ui.endIs (0, 1, Color.WHITE);
	}
	
	public void testLoadAndSaveLiftplan() {
		File leftover = new File("testdata/56737.wsml");
		if (leftover.exists()) leftover.delete();
		
		ui.open("testdata/56737.wif");
		ui.draftIs(0, 0, red);
		ui.draftIs(0, 127, white);
		ui.draftIs(295, 127, red);
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs (2, 2, Color.WHITE);
		ui.endIs (0, 0, Color.BLACK);
		ui.endIs (0, 1, Color.WHITE);
		
		ui.saveAs("testdata/56737.wsml");
		ui.close();
		
		ui.open("testdata/56737.wsml");
		ui.draftIs(0, 0, red);
		ui.draftIs(0, 127, white);
		ui.draftIs(295, 127, red);			
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs (2, 2, Color.WHITE);
		ui.endIs (0, 0, Color.BLACK);
		ui.endIs (0, 1, Color.WHITE);
	}
	
	public void testLoadNoColours() {
		ui.open("testdata/12233.wif");
		ui.draftIs(0, 0, Color.WHITE);
		ui.draftIs(0, 47, Color.BLACK);
	}
}
