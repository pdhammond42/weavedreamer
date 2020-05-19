package com.jenkins.uat;

import org.junit.Test;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import static org.assertj.swing.data.TableCell.row;

public class NetworkTest  extends WeavingTestCase {
	@Test
	public void testCreateSaveAndLoad() throws IOException {
                int harnesses=12;
                int ends = 20;
                int picks = 20;
                
                int pgrows=16;
                int pgcols=20;
                
		ui.newNetwork(harnesses, ends, picks, "Monochrome");
                
                ui.setPatternGridSize(pgrows, pgcols);
                
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
		
		ui.setPatternLine (pgrows-1-3,pgcols-1-5);
		// thise in grid coordinates
                System.out.println("Test");
                //ui.harnessIs(0, 0, Color.BLACK);
		ui.harnessIs(harnesses-1-0, ends-1-0, Color.BLACK);
		ui.harnessIs(harnesses-1-1, ends-1-1, Color.BLACK);
		ui.harnessIs(harnesses-1-2, ends-1-2, Color.BLACK);
		ui.harnessIs(harnesses-1-3, ends-1-3, Color.BLACK);
		ui.harnessIs(harnesses-1-0, ends-1-4, Color.BLACK);
		ui.harnessIs(harnesses-1-5, ends-1-5, Color.BLACK);

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

		ui.saveAs(new File("network.wsml"));
		ui.close();
		ui.open(new File("network.wsml"));
		ui.initialIs(0,0,Color.BLACK);
		ui.initialIs(1,1,Color.BLACK);
		ui.initialIs(2,2,Color.BLACK);
		ui.initialIs(3,3,Color.BLACK);
		ui.initialIs(1,2,Color.WHITE);
	}	
}
