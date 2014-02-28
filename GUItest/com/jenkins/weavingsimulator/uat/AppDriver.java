package com.jenkins.weavingsimulator.uat;

import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.objogate.wl.Query;
import com.objogate.wl.internal.PropertyQuery;
import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JMenuBarDriver;
import com.objogate.wl.swing.driver.JMenuDriver;
import com.objogate.wl.swing.driver.JMenuItemDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTextFieldDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import static com.objogate.wl.swing.matcher.ComponentMatchers.*;
import static org.hamcrest.Matchers.*;

public class AppDriver extends JFrameDriver {
	@SuppressWarnings("unchecked")
    public AppDriver() {
        super(new GesturePerformer(), new AWTEventQueueProber(), named("Weaving Simulator"), showingOnScreen());
    }

	public void clickFileNewMenu () {
		// We should be able to use file.clickLeftButtonOn,
		// but it fails to find the center of New. Works for the other buttons.
		// Until I ever find out why, this workaround works.
		JMenuDriver file = fileMenu();
		file.click();
		JMenuItemDriver b = file.menuItem(withButtonText("New"));
		b.moveMouseToOffset(5, 10);
		b.click();
	}
	
	public PropertiesWindow propertiesWindow() {
		return new PropertiesWindow(this);
	}
	
	public void hasHarnesses (int h) {
		tieUpGrid().has(rowCount(), equalTo(h));
		threadingDraftGrid().has(rowCount(), equalTo(h));
	}
	
	public void hasEnds(int e) {
		threadingDraftGrid().has(columnCount(), equalTo(e));
		weavingPatternGrid().has(columnCount(), equalTo(e));
		warpEndColorGrid().has(columnCount(), equalTo(e));
	}
	
	public void hasPicks(int p) {
		pickColorGrid().has(rowCount(), equalTo(p));
		weavingPatternGrid().has(rowCount(), equalTo(p));
		treadlingDraftGrid().has(rowCount(), equalTo(p));
	}
	
	public void hasTreadles(int t) {
		treadlingDraftGrid().has(columnCount(), equalTo(t));
		tieUpGrid().has(columnCount(), equalTo(t));
	}
	
	@SuppressWarnings("unchecked")
	private JMenuDriver fileMenu() {
		return new JMenuBarDriver(this).menu(withButtonText("File"));
	}
	
	private JTableDriver weavingPatternGrid(){
		return grid("weavingPatternGrid");
	}
	
	private JTableDriver threadingDraftGrid(){
		return grid("threadingDraftGrid");
	}
	
	private JTableDriver warpEndColorGrid(){
		return grid("warpEndColorGrid");
	}
	
	private JTableDriver treadlingDraftGrid(){
		return grid("treadlingDraftGrid");
	}

	private JTableDriver pickColorGrid(){
		return grid("pickColorGrid");
	}
	
	private JTableDriver tieUpGrid(){
		return grid("tieUpGrid");
	}
	
	private JTableDriver grid(String name) {
		return new JTableDriver(this, the(JTable.class, named(name)));
	}
	
	public class PropertiesWindow {
		JFrameDriver driver;
		
		public PropertiesWindow(AppDriver app) {
			driver = app;
		}
		
		public void setTreadles(String t) {
			replaceText(treadles(), t);
		}
		
		public void setHarnesses(String t) {
			replaceText(harnesses(), t);
		}
		
		public void setEnds(String t) {
			replaceText(ends(), t);
		}
		
		public void setPicks(String t) {
			replaceText(picks() ,t);
		}
		
		public void clickOk() {
			ok().click();
		}
		
		private JTextFieldDriver treadles() {
			return field("numTreadlesField");	
		}
		
		private JTextFieldDriver harnesses() {
			return field("numHarnessesField");	
		}

		private JTextFieldDriver ends() {
			return field("numWarpEndsField");	
		}

		private JTextFieldDriver picks() {
			return field("numWeftPicksField");	
		}

		private JTextFieldDriver field(String name) {
			return new JTextFieldDriver (driver, 
					the(JTextField.class, named(name)));				
		}
		
		private JButtonDriver ok() {
			return new JButtonDriver(driver, the (JButton.class, withButtonText("OK")));
		}
	}
	
	private static void pause(int t) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
	
    public static Query<JTable, Integer> rowCount() {
        return new PropertyQuery<JTable, Integer>("row count") {
            public Integer query(JTable component) {
                return component.getRowCount();
            }
        };
    }

    public static Query<JTable, Integer> columnCount() {
        return new PropertyQuery<JTable, Integer>("column count") {
            public Integer query(JTable component) {
                return component.getColumnCount();
            }
        };
    }
    
    public static void replaceText (JTextFieldDriver f, String s) {
    	// JTextFieldDriver.replaceAllText does not seem to replace text
    	// very reliably. After a couple of runs, trying to create a 
    	// 242424 x 202020 thread draft tends to take a while.
    	
    	f.replaceAllText(s);
    	f.hasText(s);
    	/*
        f.focusWithMouse();
    	System.err.println("clear");
        f.clearText();
    	System.err.println("cleared");
        f.isEmpty();

        f.typeText(s);
    	System.err.println("typed");
        f.hasText(s);
        */
    }
}
