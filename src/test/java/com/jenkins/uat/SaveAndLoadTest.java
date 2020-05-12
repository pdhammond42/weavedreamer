package com.jenkins.uat;

import org.junit.Test;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class SaveAndLoadTest extends WeavingTestCase {
	@Test
	public void testLoadAndSave() throws IOException {
		File wsml = new File("testdata/103.wsml");
		if (wsml.exists()) wsml.delete();

		File wif = new File("testdata/103.wif");

		ui.open(wif);
		ui.draftIs(0, 0, orange);
		ui.draftIs(0, 1, blue);
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs (0, 1, Color.WHITE);
		ui.harnessIs (0,0, Color.BLACK);
		ui.harnessIs (0,1, Color.WHITE);
		
		ui.saveAs(wsml);
		ui.close();

		ui.open(wsml);
		ui.draftIs(0, 0, orange);
		ui.draftIs(0, 1, blue);		
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs (0, 1, Color.WHITE);
		ui.harnessIs (0, 0, Color.BLACK);
		ui.harnessIs (0, 1, Color.WHITE);
	}

	@Test
	public void testLoadFileSavedFrom2_5() throws IOException {
		ui.open(new File("testdata/103-2.5.wsml"));

		ui.draftIs(0, 0, orange);
		ui.draftIs(0, 1, blue);				
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs (0, 1, Color.WHITE);
		ui.harnessIs (0, 0, Color.BLACK);
		ui.harnessIs (0, 1, Color.WHITE);
	}

	@Test
	public void testLoadFileSavedFrom2_7() throws IOException {
		ui.open(new File("testdata/103-2.7.wsml"));

		ui.draftIs(0, 0, orange);
		ui.draftIs(0, 1, blue);				
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs (0, 1, Color.WHITE);
		ui.pickIs(1, 0, Color.WHITE);
		ui.pickIs (1, 1, Color.BLACK);
		ui.harnessIs (0, 0, Color.BLACK);
		ui.harnessIs (0, 1, Color.WHITE);
	}

	@Test
	public void testLoadAndSaveLiftplan() throws IOException {
		File wsml = new File("testdata/56737.wsml");
		if (wsml.exists()) wsml.delete();

		File wif = new File("testdata/56737.wif");

		ui.open(wif);
                // swapped red and white readbacks 

		ui.draftIs(0, 0, white);
		ui.draftIs(0, 127, red);
		ui.draftIs(295, 127, red);
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs (2, 2, Color.WHITE);
		ui.harnessIs (0, 0, Color.BLACK);
		ui.harnessIs (0, 1, Color.WHITE);
		
		ui.saveAs(wsml);
		ui.close();

		ui.open(wsml);
		ui.draftIs(0, 0, white);
		ui.draftIs(0, 127, red);
		ui.draftIs(295, 127, red);			
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs (2, 2, Color.WHITE);
		ui.harnessIs (0, 0, Color.BLACK);
		ui.harnessIs (0, 1, Color.WHITE);
	}

	@Test
	public void testLoadNetworkFrom2_8() throws IOException {
		File wsml = new File("testdata/network-2.8.wsml");
		ui.open(wsml);

		ui.draftIs(0, 0, Color.WHITE);
		ui.draftIs(0, 1, Color.BLACK);
	}

	@Test
	public void testLoadNoColours() throws IOException {
		ui.open(new File("testdata/12233.wif"));

		ui.draftIs(0, 0, Color.BLACK);
		ui.draftIs(0, 47, Color.WHITE);
	}
}
