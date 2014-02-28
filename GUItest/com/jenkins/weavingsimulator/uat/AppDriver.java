package com.jenkins.weavingsimulator.uat;

import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.Table;
import org.uispec4j.TextBox;
import org.uispec4j.Button;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

public class AppDriver{
	private Window mainWindow;
    public AppDriver(Window mainWindow) {
    	this.mainWindow=mainWindow;
    }

    public void newDraft(final int harnesses, final int treadles, final int ends, final int picks) {
    	WindowInterceptor
    	.init(mainWindow.getMenuBar()
    			.getMenu("File")
    			.getSubMenu("New")
    			.triggerClick())
    			.process(new WindowHandler() {
    				public Trigger process(Window window) {
    					window.getTextBox("numTreadlesField").setText(Integer.toString(treadles));
    					window.getTextBox("numHarnessesField").setText(Integer.toString(harnesses));
    					window.getTextBox("numWarpEndsField").setText(Integer.toString(ends));
    					window.getTextBox("numWeftPicksField").setText(Integer.toString(picks));
    					return window.getButton("OK").triggerClick();
    				}
    			})
    			.run();
    }

	public PropertiesWindow clickFileNewMenu () {
		return new PropertiesWindow(WindowInterceptor.getModalDialog(
				mainWindow.getMenuBar()
				.getMenu("File")
				.getSubMenu("New")
				.triggerClick()));
	}
		
	public void hasHarnesses (int h) {
		tieUpGrid().rowCountEquals(h);
		threadingDraftGrid().rowCountEquals(h);
	}
	
	public void hasEnds(int e) {
		threadingDraftGrid().columnCountEquals(e);
		weavingPatternGrid().columnCountEquals(e);
		warpEndColorGrid().columnCountEquals(e);
	}
	
	public void hasPicks(int p) {
		pickColorGrid().rowCountEquals(p);
		weavingPatternGrid().rowCountEquals(p);
		treadlingDraftGrid().rowCountEquals(p);
	}
	
	public void hasTreadles(int t) {
		treadlingDraftGrid().columnCountEquals(t);
		tieUpGrid().columnCountEquals(t);
	}
	
	private Table weavingPatternGrid(){
		return mainWindow.getTable("weavingPatternGrid");
	}
	
	private Table threadingDraftGrid(){
		return mainWindow.getTable("threadingDraftGrid");
	}
	
	private Table warpEndColorGrid(){
		return mainWindow.getTable("warpEndColorGrid");
	}
	
	private Table treadlingDraftGrid(){
		return mainWindow.getTable("treadlingDraftGrid");
	}

	private Table pickColorGrid(){
		return mainWindow.getTable("pickColorGrid");
	}
	
	private Table tieUpGrid(){
		return mainWindow.getTable("tieUpGrid");
	}
	
	public class PropertiesWindow {
		Window window;
		
		public PropertiesWindow(Window w) {
			window = w;
		}
		
		public void setTreadles(String t) {
			treadles().setText(t);
		}
		
		public void setHarnesses(String t) {
			harnesses().setText(t);
		}
		
		public void setEnds(String t) {
			ends().setText(t);
		}
		
		public void setPicks(String t) {
			picks().setText(t);
		}
		
		public void clickOk() {
			ok().click();
		}
		
		private TextBox treadles() {
			return window.getTextBox("numTreadlesField");	
		}
		
		private TextBox harnesses() {
			return window.getTextBox("numHarnessesField");	
		}

		private TextBox ends() {
			return window.getTextBox("numWarpEndsField");	
		}

		private TextBox picks() {
			return window.getTextBox("numWeftPicksField");	
		}
		
		private Button ok() {
			return window.getButton("OK");
		}
	}
	
	public static void pause(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
		}
	}
}
