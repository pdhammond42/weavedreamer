package com.jenkins.weavingsimulator.uat;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;

import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
import javax.swing.JTable;

import org.uispec4j.Mouse;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.Table;
import org.uispec4j.TextBox;
import org.uispec4j.Button;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import org.uispec4j.interception.FileChooserHandler;
import static org.uispec4j.assertion.UISpecAssert.assertThat;

public class AppDriver{
	private Window mainWindow;
    public AppDriver(Window mainWindow) {
    	this.mainWindow=mainWindow;
    }

    // 
    // Actions 
    //
    public void newDraft(final int harnesses, final int treadles, final int ends, final int picks) {
    	WindowInterceptor
    	.init(mainWindow.getMenuBar()
    			.getMenu("File")
    			.getSubMenu("New")
    			.triggerClick())
    			.process(new WindowHandler() {
    				public Trigger process(Window window) {
    					getJTextBox(window, "numTreadlesField").setValue(treadles);
    					getJTextBox(window, "numHarnessesField").setValue(harnesses);
    					getJTextBox(window, "numWarpEndsField").setValue(ends);
    					getJTextBox(window, "numWeftPicksField").setValue(picks);
    					return window.getButton("OK").triggerClick();
    				}
    			})
    			.run();
    }
    
    public void saveAs (final String name) {
    	WindowInterceptor
    	.init(mainWindow.getMenuBar()
    			.getMenu("File")
    			.getSubMenu("Save As ...")
    			.triggerClick())
    			.process(FileChooserHandler.init().select(name))
    			.run();   	
    }
    
    public void open (final String name) {
    	WindowInterceptor
    	.init(mainWindow.getMenuBar()
    			.getMenu("File")
    			.getSubMenu("Open")
    			.triggerClick())
    			.process(FileChooserHandler.init().select(name))
    			.run();   	    	
    }

    public void close() {
    	JRootPane root = (JRootPane) mainWindow.getAwtComponent().getComponents()[0];
    	JDesktopPane desktop =  (JDesktopPane) root.getContentPane().getComponent(0);
    	JInternalFrame frame = (JInternalFrame) desktop.getComponent(0);
    	new Window(frame).dispose();
    }
    
    void toggleTieup(final int row, final int column) {
    	tieUpGrid().click(row, column);
    }
    
    void setPick (final int  row, final int column) {
    	treadlingDraftGrid().click(row, column);
    }
    
    void dragPick(final int startRow, final int startColumn, final int endRow, final int endColumn) {
    	drag (treadlingDraftGrid(), startRow, startColumn, endRow, endColumn);
    }   
    
    void setPickColour (final int row) {
    	pickColorGrid().click(row, 0);
    }
    
    void dragPickColour (final int start, final int end) {
    	drag (pickColorGrid(), start, 0, end, 0);
    }
    
    void selectColour(final int row) {
    	paletteGrid().click(row, 0);
    }
    
    void setThreading (final int row, final int column){
    	threadingDraftGrid().click(row,  column);
    }
    
    void dragThreading(final int startRow, final int startColumn, final int endRow, final int endColumn) {
    	drag (threadingDraftGrid(), startRow, startColumn, endRow, endColumn);
    }   
    
    void setThreadingColour (final int column) {
    	warpEndColorGrid().click(0, column);
    }
    
    void dragThreadingColour(final int start, final int end) {
    	drag(warpEndColorGrid(), 0, start, 0, end);
    }
	//
	// Assertions
	//
		
	public void hasHarnesses (int h) {
		assertThat(tieUpGrid().rowCountEquals(h));
		assertThat(threadingDraftGrid().rowCountEquals(h));
	}
	
	public void hasEnds(int e) {
		assertThat(threadingDraftGrid().columnCountEquals(e));
		assertThat(weavingPatternGrid().columnCountEquals(e));
		assertThat(warpEndColorGrid().columnCountEquals(e));
	}
	
	public void hasPicks(int p) {
		assertThat(pickColorGrid().rowCountEquals(p));
		assertThat(weavingPatternGrid().rowCountEquals(p));
		assertThat(treadlingDraftGrid().rowCountEquals(p));
	}
	
	public void hasTreadles(int t) {
		assertThat(treadlingDraftGrid().columnCountEquals(t));
		assertThat(tieUpGrid().columnCountEquals(t));
	}
	
	public void draftIs (final int row, final int column, final Color expected) {
		assertThat(weavingPatternGrid().backgroundNear(row, column, expected));
	}
	
	//
	// UI element access
	//
    private static JFormattedTextField getJTextBox(Window window, String name) {
    	return (JFormattedTextField)(window.getTextBox(name).getAwtComponent());
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

	private Table paletteGrid(){
		return mainWindow.getTable("paletteGrid");
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
	
	//
	// Helpers
	//
	private static void drag (Table table, final int startRow, final int startColumn, final int endRow, final int endColumn){
    	JTable jtable = table.getAwtComponent();
    	final int startX = xOfColumn(jtable, startColumn);
    	final int startY = yOfRow(jtable, startRow);
    	final int endX = xOfColumn(jtable, endColumn); 
    	final int endY = yOfRow(jtable, endRow);
    	// Autoscrolling will prevent the second drag being handled, so
    	// suppress it.
    	jtable.setAutoscrolls(false);
    	Mouse.drag(table, startX, startY);
    	Mouse.drag(table, endX, endY);
    	Mouse.released(table, endX, endY);
	}
	
	private static int xOfColumn(JTable table, final int column) {
		Point point = new Point(0, 1);
		int colOfPoint = 0;
		do {
			colOfPoint = table.columnAtPoint(point);
			if (colOfPoint == column) return point.x;
			point.x += 2;
		} while (colOfPoint != -1);
		return 0;
	}

	private static int yOfRow(JTable table, final int row) {
		Point point = new Point(1,0);
		int rowOfPoint = 0;
		do {
			rowOfPoint = table.rowAtPoint(point);
			if (rowOfPoint == row) return point.y;
			point.y += 2;
		} while (rowOfPoint != -1);
		return 0;
	}
	
	public static void pause(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
		}
	}
}
