package com.jenkins.weavingsimulator.uat;

import java.awt.Color;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.hamcrest.MatcherAssert;

import com.jenkins.weavingsimulator.WeavingSimulatorApp;

import static org.hamcrest.Matchers.*;


public class BasicDraftEditTest extends WeavingTestCase {
	
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
	
	public void testEditProperties () {
		ui.open("testdata/103.wif");
		
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
	
	public void testSavePalette () {
		// Rather ugly having this here but it works.
		try {
			Preferences.userNodeForPackage(WeavingSimulatorApp.class).node("Palettes").clear();
		} catch (BackingStoreException e) {
		}
		
		final String testPaletteName ="blue and orange";
		
		ui.open("testdata/103.wif");
		
		ui.savePalette(testPaletteName);
		ui.close();
		
		ui.newDraft(4, 6, 20, 20, testPaletteName);
		ui.paletteIs(0, blue);
		ui.paletteIs(1, orange);
	}
	
	public void testZoom() {
		ui.open("testdata/103.wif");
		
		final int initialZoom = ui.zoomLevel();
		
		ui.zoomIn();
		MatcherAssert.assertThat(ui.zoomLevel(), greaterThan(initialZoom));
		
		ui.zoomOut();
		MatcherAssert.assertThat(ui.zoomLevel(), equalTo(initialZoom));
		
		ui.zoomOut();
		MatcherAssert.assertThat(ui.zoomLevel(), lessThan(initialZoom));

		// Zoom back in is allowed to miss by 1 pixel due to rounding an odd value.
		ui.zoomIn();
		MatcherAssert.assertThat(ui.zoomLevel(),
				allOf(greaterThan(initialZoom-2), lessThan(initialZoom+1)));	
	}
	
	public void testTileView() {
		ui.open("testdata/103.wif");

		AppDriver.TiledView tiledView = ui.showTiledView();	
		tiledView.hasColour(0, 0, orange);
		tiledView.hasColour(0, 1, blue);
		tiledView.hasColour(0, 32, orange);
		tiledView.hasColour(32, 1, blue);
	}
	
	public void testHelpAbout () {
		ui.checkAboutBox();
	}
	
	public void testPasteBetweenGrids() {
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
}
