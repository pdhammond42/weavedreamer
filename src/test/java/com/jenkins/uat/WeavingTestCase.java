package com.jenkins.uat;

import com.jenkins.weavedreamer.WeaveDreamerApp;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Before;

import java.awt.*;

abstract class WeavingTestCase extends AssertJSwingJUnitTestCase {
	// Colours used in the 103.wif test piece.
	protected final Color orange = new Color(0xcc674f);
	protected final Color blue = new Color(0x66ccff);
	// Colours in 56737 test
	protected final Color red = new Color(0xed0303);
	protected final Color white = new Color(0xffffff);
	
	protected AppDriver ui;


	@Override
	public void onSetUp() {
		if (ui == null) {
			WeaveDreamerApp frame = GuiActionRunner.execute(() -> {
				WeaveDreamerApp f = new WeaveDreamerApp();
				f.setVisible(true);
				return f;
			});
			robot().settings().delayBetweenEvents(100);
			robot().settings().eventPostingDelay(100);
			robot().settings().timeoutToBeVisible(200);
			ui = new AppDriver(new FrameFixture(robot(), frame));

		}
    }
}