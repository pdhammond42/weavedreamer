package com.jenkins.weavingsimulator.uat;

import org.uispec4j.UISpecTestCase;
import org.uispec4j.interception.MainClassAdapter;

import com.jenkins.weavingsimulator.WeavingSimulatorApp;

class WeavingTestCase extends UISpecTestCase {
   public void setUp() {
	   setAdapter(new MainClassAdapter(WeavingSimulatorApp.class, new String[0]));
   }

   public void tearDown() {
    
   }
}