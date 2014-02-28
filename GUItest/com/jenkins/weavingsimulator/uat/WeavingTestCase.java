package com.jenkins.weavingsimulator.uat;

import junit.framework.TestCase;

import com.jenkins.weavingsimulator.WeavingSimulatorApp;

class WeavingTestCase extends TestCase {
   protected AppDriver weavingUI;

   public void setUp() {
       WeavingSimulatorApp.main(new String[]{});
       weavingUI = new AppDriver();
   }

   public void tearDown() {
       weavingUI.dispose();
   }
}