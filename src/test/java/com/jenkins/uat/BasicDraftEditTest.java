package com.jenkins.uat;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.jenkins.weavedreamer.WeaveDreamerApp;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class BasicDraftEditTest extends WeavingTestCase {
	@Test
	public void testNewDraft() {
		
		final int harnesses = 12;
		final int treadles = 8;
		final int ends = 22;
		final int picks = 18;
		ui.newDraft(harnesses, treadles, ends, picks, "Monochrome");

		ui.hasHarnesses(harnesses);
		ui.hasTreadles(treadles);
		ui.hasEnds(ends);
		ui.hasPicks(picks);
	}

	@Test
	public void testEditWeave() {
		ui.newDraft(4, 6, 20, 20, "Monochrome");
		
		ui.toggleTieup (0, 0);
		ui.toggleTieup (1, 0);
		ui.toggleTieup (1, 1);
		ui.toggleTieup (2, 1);
		ui.toggleTieup (2, 2);
		ui.toggleTieup (3, 2);
		ui.toggleTieup (3, 3);
		ui.toggleTieup (0, 3);
		
		ui.setPick (0, 0);
		ui.setPick (1, 1);
		ui.setPick (2, 2);
		ui.setPick (3, 3);
		ui.dragPick(4, 0, 7, 3);
		
		ui.setThreading(0, 0);
		ui.setThreading(1, 1);
		ui.setThreading(2, 2);
		ui.setThreading(3, 3);
		ui.dragThreading(0, 4, 3, 7);
		
		ui.selectColour(2);
		ui.setPickColour(0);
		ui.setPickColour(1);
		ui.setPickColour(2);
		ui.setPickColour(3);
		ui.dragPickColour(4, 7);
		
		ui.selectColour(1);
		ui.setThreadingColour(0);
		ui.setThreadingColour(1);
		ui.setThreadingColour(2);
		ui.setThreadingColour(3);
		ui.dragThreadingColour(4, 7);
		
		ui.draftIs(0, 0, Color.darkGray);
		ui.draftIs(0, 1, Color.darkGray);
		ui.draftIs(0, 2, Color.lightGray);
		ui.draftIs(0, 3, Color.lightGray);
		ui.draftIs(0, 4, Color.darkGray);
		ui.draftIs(0, 5, Color.darkGray);
		ui.draftIs(0, 6, Color.lightGray);
		ui.draftIs(0, 7, Color.lightGray);	

		ui.draftIs(0, 0, Color.darkGray);
		ui.draftIs(1, 0, Color.lightGray);
		ui.draftIs(2, 0, Color.lightGray);
		ui.draftIs(3, 0, Color.darkGray);
		ui.draftIs(4, 0, Color.darkGray);
		ui.draftIs(5, 0, Color.lightGray);
		ui.draftIs(6, 0, Color.lightGray);	
		ui.draftIs(0, 0, Color.darkGray);		
	}

	@Test
	public void testEditProperties () throws IOException {
		ui.open(new File("testdata/103.wif"));
		
		final int harnesses = 12;
		final int treadles = 8;
		final int ends = 22;
		final int picks = 18;
		ui.editDraftProperties(harnesses, treadles, ends, picks, "Spring");

		ui.hasHarnesses(harnesses);
		ui.hasTreadles(treadles);
		ui.hasEnds(ends);
		ui.hasPicks(picks);
	}

	@Test
	public void testSavePalette () throws IOException {
		// Rather ugly having this here but it works.
		try {
			Preferences.userNodeForPackage(WeaveDreamerApp.class).node("Palettes").clear();
		} catch (BackingStoreException e) {
		}
		
		final String testPaletteName ="blue and orange";
		
		ui.open(new File("testdata/103.wif"));
		
		ui.savePalette(testPaletteName);
		ui.close();
		
		ui.newDraft(4, 6, 20, 20, testPaletteName);
		ui.paletteIs(0, blue);
		ui.paletteIs(1, orange);
	}

	@Test
	public void testZoom() throws IOException {
		ui.open(new File("testdata/103.wif"));
		
		final int initialZoom = ui.zoomLevel();
		
		ui.zoomIn();
		assertThat(ui.zoomLevel(), greaterThan(initialZoom));
		
		ui.zoomOut();
		assertThat(ui.zoomLevel(), equalTo(initialZoom));
		
		ui.zoomOut();
		assertThat(ui.zoomLevel(), lessThan(initialZoom));

		// Zoom back in is allowed to miss by 1 pixel due to rounding an odd value.
		ui.zoomIn();
		assertThat(ui.zoomLevel(),
				allOf(greaterThan(initialZoom-2), lessThan(initialZoom+1)));	
	}

	@Test
	public void testTileView() throws Exception{
		File file = new java.io.File("testdata/103.wif");
		ui.open(file);

		AppDriver.TiledView tiledView = ui.showTiledView();	
		tiledView.hasColour(0, 0, orange);
		tiledView.hasColour(0, 1, blue);
		tiledView.hasColour(0, 32, orange);
		tiledView.hasColour(32, 1, blue);
	}

	@Test
	public void testHelpAbout () {
		assertThat(ui.aboutBoxText(), containsString("Weaving Simulator 0.2"));
	}

	@Test
	public void testPastePickToEnd() {
		ui.newDraft(4, 6, 20, 20, "Monochrome");
		
		ui.setPick (0, 0);
		ui.setPick (1, 1);
		ui.setPick (2, 2);
		ui.setPick (3, 3);
		ui.selectPick(0, 0, 4, 4);
		
		ui.pasteThreading (0,0);
		ui.endIs(0, 0, Color.BLACK);
		ui.endIs(1, 1, Color.BLACK);
		ui.endIs(2, 2, Color.BLACK);
	}

	@Test
	public void testPasteEndToPick() {
		ui.newDraft(4, 6, 20, 20, "Monochrome");
		
		ui.setThreading(0, 0);
		ui.setThreading (1, 1);
		ui.setThreading (2, 2);
		ui.setThreading (3, 3);
		ui.selectThreading(0, 0, 4, 4);
		
		ui.pasteTreadling (0,0);
		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs(1, 1, Color.BLACK);
		ui.pickIs(2, 2, Color.BLACK);
		ui.pickIs(3, 3, Color.BLACK);
	}

	@Test
	public void testPasteTranspose() {
		ui.newDraft(4, 6, 20, 20, "Monochrome");
		
		ui.setPick (0, 0);
		ui.setPick (1, 1);
		ui.setPick (2, 2);
		ui.setPick (3, 1);
		ui.selectPick(0, 0, 4, 4);
		
		ui.pasteThreading (0, 0, 1, 1, true, false, false);
		ui.endIs(2, 0, Color.BLACK);
		ui.endIs(1, 1, Color.BLACK);
		ui.endIs(2, 2, Color.BLACK);
		ui.endIs(3, 3, Color.BLACK);
	}
}
