package com.jenkins.weavingsimulator.uat;

import java.awt.Color;

import org.uispec4j.UISpecTestCase;
import org.uispec4j.interception.MainClassAdapter;

import com.jenkins.weavingsimulator.WeavingSimulatorApp;

abstract class WeavingTestCase extends UISpecTestCase {
	// Colours used in the 103.wif test piece.
	protected final Color orange = new Color(0xcc674f);
	protected final Color blue = new Color(0x66ccff);
	// Colours in 56737 test
	protected final Color red = new Color(0xe90303);
	protected final Color white = new Color(0xffffff);
	
	protected AppDriver ui;
	
	public void setUp() {
		setAdapter(new MainClassAdapter(WeavingSimulatorApp.class,
				new String[0]));
		
		ui = new AppDriver(getMainWindow());
	}

	public void tearDown() {

	}
}