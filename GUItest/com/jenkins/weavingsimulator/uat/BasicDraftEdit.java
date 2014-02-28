package com.jenkins.weavingsimulator.uat;

public class BasicDraftEdit extends WeavingTestCase {
	public void testNewDraft() {
		
		AppDriver ui = new AppDriver(getMainWindow());
		
		AppDriver.PropertiesWindow props = ui.clickFileNewMenu();

		props.setTreadles("12");
		props.setHarnesses("8");
		props.setEnds("24");
		props.setPicks("20");
		props.clickOk();

		AppDriver.pause(1000);
		ui.hasTreadles(12);
		ui.hasHarnesses(8);
		ui.hasEnds(24);
		ui.hasPicks(20);
	}
}
