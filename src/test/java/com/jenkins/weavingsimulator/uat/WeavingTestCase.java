package com.jenkins.weavingsimulator.uat;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

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
		try {
			setAdapter(new MainClassAdapter(WeavingSimulatorApp.class,
					new String[0]));

			ui = new AppDriver(getMainWindow());
		} 	catch (RuntimeException e) {
			if(e.getCause() instanceof InvocationTargetException) {
				InvocationTargetException te = (InvocationTargetException)e.getCause();
				if (te.getTargetException().getMessage().contains("UISpecToolkit cannot be cast")) {
					throw new ClassCastException ("Failed to initialise the toolkit.\n" +
							"Set the VM argument '-Dawt.toolkit=net.java.openjdk.cacio.ctc.CTCToolkit'" +
							"to avoid this");
				}
			}
			throw(e);
		}
	}

	public void tearDown() {

	}
}