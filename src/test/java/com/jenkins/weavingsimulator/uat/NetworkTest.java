package com.jenkins.weavingsimulator.uat;

import java.awt.Color;

public class NetworkTest  extends WeavingTestCase {
	public void testCreateSaveAndLoad() {
		ui.newNetwork(12, 20, 20, "Monochrome");
		ui.setInitial(1,1);
		ui.setInitial(2,2);
		ui.setInitial(3,3);
		
		ui.setKey1(0,0);
		ui.setKey1(0,1);
		ui.setKey1(0,2);
		ui.setKey1(0,3);

		ui.setKey2(3,0);
		ui.setKey2(3,1);
		ui.setKey2(3,2);
		ui.setKey2(3,3);
		
		ui.setPatternLine (3,5);
		
		ui.endIs(0, 0, Color.BLACK);
		ui.endIs(1, 1, Color.BLACK);
		ui.endIs(2, 2, Color.BLACK);
		ui.endIs(3, 3, Color.BLACK);
		ui.endIs(0, 4, Color.BLACK);
		ui.endIs(5, 5, Color.BLACK);

		ui.pickIs(0, 0, Color.BLACK);
		ui.pickIs(0, 1, Color.BLACK);
		ui.pickIs(0, 2, Color.BLACK);
		ui.pickIs(0, 3, Color.BLACK);
		ui.pickIs(3, 3, Color.WHITE);
		ui.pickIs(3, 5, Color.BLACK);
		ui.pickIs(3, 6, Color.BLACK);
		ui.pickIs(4, 2, Color.BLACK);
		ui.pickIs(4, 3, Color.BLACK);
		ui.pickIs(4, 4, Color.WHITE);
		ui.pickIs(4, 5, Color.WHITE);

		ui.saveAs("network.wsml");		
		ui.close();
		ui.open("network.wsml");
		ui.initialIs(0,0,Color.BLACK);
		ui.initialIs(1,1,Color.BLACK);
		ui.initialIs(2,2,Color.BLACK);
		ui.initialIs(3,3,Color.BLACK);
		ui.initialIs(1,2,Color.WHITE);
	}	
}
