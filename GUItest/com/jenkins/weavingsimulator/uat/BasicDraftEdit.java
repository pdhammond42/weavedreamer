package com.jenkins.weavingsimulator.uat;

public class BasicDraftEdit extends WeavingTestCase {
	public void testNewDraft() {
		weavingUI.clickFileNewMenu();
		weavingUI.propertiesWindow().setTreadles("12");
		weavingUI.propertiesWindow().setHarnesses("8");
		weavingUI.propertiesWindow().setEnds("24");
		weavingUI.propertiesWindow().setPicks("20");
		weavingUI.propertiesWindow().clickOk();
		
		weavingUI.hasTreadles(12);
		weavingUI.hasHarnesses(8);
		weavingUI.hasEnds(24);
		weavingUI.hasPicks(20);
	}
}
