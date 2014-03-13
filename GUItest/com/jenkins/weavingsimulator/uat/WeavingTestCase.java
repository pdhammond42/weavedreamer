package com.jenkins.weavingsimulator.uat;

import org.uispec4j.UISpecTestCase;
import org.uispec4j.interception.MainClassAdapter;

import com.jenkins.weavingsimulator.WeavingSimulatorApp;

class WeavingTestCase extends UISpecTestCase {

	protected AppDriver ui;
	
	public void setUp() {
		setAdapter(new MainClassAdapter(WeavingSimulatorApp.class,
				new String[0]));
		
		ui = new AppDriver(getMainWindow());
	}

	public void tearDown() {

	}
}