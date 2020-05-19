package com.jenkins.uat;

import com.jenkins.weavedreamer.NetworkWindow;
import com.jenkins.weavedreamer.PasteSpecialWindow;
import com.jenkins.weavedreamer.TiledViewFrame;
import com.jenkins.weavedreamer.WeavingDraftPropertiesDialog;
import com.jenkins.weavedreamer.WeavingGridControl;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.MouseButton;
import org.assertj.swing.finder.JFileChooserFinder;
import org.assertj.swing.fixture.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.event.InputEvent.SHIFT_MASK;
import static org.assertj.swing.data.TableCell.row;
import static org.assertj.swing.finder.WindowFinder.findDialog;
import static org.assertj.swing.finder.WindowFinder.findFrame;

public class AppDriver {

    private FrameFixture mainWindow;

    public AppDriver(FrameFixture mainWindow) {
        this.mainWindow = mainWindow;
    }

    //
    // Actions
    //
    public void newDraft(final int harnesses, final int treadles, final int ends, final int picks, final String palette) {
        mainWindow.menuItemWithPath("File", "New").click();
        DialogFixture properties = findDialog(WeavingDraftPropertiesDialog.name).using(mainWindow.robot());

        properties.textBox("numTreadlesField").setText(Integer.toString(treadles));
        properties.textBox("numHarnessesField").setText(Integer.toString(harnesses));
        properties.textBox("numWarpEndsField").setText(Integer.toString(ends));
        properties.textBox("numWeftPicksField").setText(Integer.toString(picks));
        properties.comboBox("palettes_combo").selectItem(palette);
        properties.button("OK").click();
    }

    public void newNetwork(final int harnesses, final int ends, final int picks, final String palette) {
        mainWindow.menuItemWithPath("File", "New").click();
        DialogFixture properties = findDialog(WeavingDraftPropertiesDialog.name).using(mainWindow.robot());

        properties.checkBox("network").check();
        properties.textBox("numHarnessesField").setText(Integer.toString(harnesses));
        properties.textBox("numWarpEndsField").setText(Integer.toString(ends));
        properties.textBox("numWeftPicksField").setText(Integer.toString(picks));
        properties.comboBox("palettes_combo").selectItem(palette);
        properties.button("OK").click();
    }

    public void editDraftProperties(final int harnesses, final int treadles, final int ends, final int picks, final String palette) {
        mainWindow.menuItemWithPath("Edit", "Edit Properties").click();
        DialogFixture properties = findDialog(WeavingDraftPropertiesDialog.name).using(mainWindow.robot());

        properties.textBox("numTreadlesField").setText(Integer.toString(treadles));
        properties.textBox("numHarnessesField").setText(Integer.toString(harnesses));
        properties.textBox("numWarpEndsField").setText(Integer.toString(ends));
        properties.textBox("numWeftPicksField").setText(Integer.toString(picks));
        properties.comboBox("palettes_combo").selectItem(palette);
        properties.button("OK").click();
    }

    public void saveAs(final File name) throws IOException {
        mainWindow.menuItemWithPath("File", "Save As ...").click();
        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser()
                .using(mainWindow.robot());

        fileChooser.fileNameTextBox().setText(name.getCanonicalPath());
        fileChooser.approve();
    }

    public void open(final File name) throws IOException {
        mainWindow.menuItemWithPath("File", "Open").click();
        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser()
                .using(mainWindow.robot());

        fileChooser.fileNameTextBox().setText(name.getCanonicalPath());
        fileChooser.approve();
    }

    public void close() {
        mainWindow.internalFrame("WeavingDraftWindow").close();
    }

    public void closeApp() {
        mainWindow.close();
    }

    public void savePalette(final String name) {
        mainWindow.menuItemWithPath("Edit", "Save Palette ...").click();
        DialogFixture nameBox = findDialog(new GenericTypeMatcher<Dialog>(Dialog.class) {
            @Override
            protected boolean isMatching(Dialog dialog) {
                return dialog.getTitle() == "Input";
            }
        }).using(mainWindow.robot());

        nameBox.textBox().setText(name);

        nameBox.button(new GenericTypeMatcher<JButton>(JButton.class) {
            @Override
            protected boolean isMatching(JButton button) {
                return button.getText() == "OK";
            }
        }).click();
    }

    public TiledView showTiledView() {
        mainWindow.menuItemWithPath("View", "Tiled View").click();
        return new TiledView(findFrame(TiledViewFrame.name).using(mainWindow.robot()));
    }

    void toggleTieup(final int r, final int c) {
        tieUpGrid().cell(row(r).column(c)).click();
    }

    void setPick(final int r, final int c) {
        treadlingDraftGrid().cell(row(r).column(c)).click();
    }

    void dragPick(final int startRow, final int startColumn, final int endRow, final int endColumn) {
        drag(treadlingDraftGrid(), startRow, startColumn, endRow, endColumn);
    }

    void selectPick(final int startRow, final int startColumn, final int endRow, final int endColumn) {
        drag(treadlingDraftGrid(), startRow, startColumn, endRow, endColumn, SHIFT_MASK);
    }

    void setPickColour(final int row) {
        pickColorGrid().cell(row(row).column(0)).click();
    }

    void dragPickColour(final int start, final int end) {
        drag(pickColorGrid(), start, 0, end, 0);
    }

    void selectColour(final int colour) {
        paletteGrid().cell(row(0).column(colour)).click();
    }

    void setThreading(final int r, final int c) {
         threadingDraftGrid().cell(row(r).column(c)).click();
    }

    void setThreadingCell(final int r, final int c) {
        threadingDraftGrid().cell(row(r).column(c)).click();
    }

    void dragThreading(final int startRow, final int startColumn, final int endRow, final int endColumn) {
        drag(threadingDraftGrid(), startRow, startColumn, endRow, endColumn);
    }

    void selectThreading(final int startRow, final int startColumn, final int endRow, final int endColumn) {
        drag(threadingDraftGrid(), startRow, startColumn, endRow, endColumn, SHIFT_MASK);
    }

    void setThreadingColour(final int column) {
        warpEndColorGrid().cell(row(0).column(column)).click();
    }

    void dragThreadingColour(final int start, final int end) {
        drag(warpEndColorGrid(), 0, start, 0, end);
    }

    void pasteThreading(final int r, final int c) {
        JPopupMenuFixture popup = threadingDraftGrid().showPopupMenuAt(row(r).column(c));
        popup.menuItemWithPath("Paste").click();
    }

    void pasteTreadling(final int r, final int c) {
        JPopupMenuFixture popup = treadlingDraftGrid().showPopupMenuAt(row(r).column(c));
        popup.menuItemWithPath("Paste").click();
    }

    public void pasteThreading(final int r, final int c, int rowMultiplier, int colMultiplier,
            boolean transpose, boolean reflectV, boolean reflectH) {
        JPopupMenuFixture popup = threadingDraftGrid().showPopupMenuAt(row(r).column(c));
        popup.menuItemWithPath("Paste Special...").click();

        DialogFixture pasteSpecial = findDialog(PasteSpecialWindow.name).using(mainWindow.robot());
        pasteSpecial.textBox("scale_v").setText(Integer.toString(rowMultiplier));
        pasteSpecial.textBox("scale_h").setText(Integer.toString(colMultiplier));
        if (reflectH) {
            pasteSpecial.checkBox("mirror_h").check();
        }
        if (reflectV) {
            pasteSpecial.checkBox("mirror_v").check();
        }
        if (transpose) {
            pasteSpecial.checkBox("transpose").check();
        }
        pasteSpecial.button("ok").click();
    }

    void zoomIn() {
        mainWindow.menuItemWithPath("View", "Zoom In").click();
    }

    void zoomOut() {
        mainWindow.menuItemWithPath("View", "Zoom Out").click();
    }

    public void undo() {
        mainWindow.menuItemWithPath("Edit", "Undo").click();
    }

    public void redo() {
        mainWindow.menuItemWithPath("Edit", "Redo").click();
    }

    public void setInitial(int r, int c) {
        initialGrid().cell(row(r).column(c)).click();
    }

    public void setKey1(int r, int c) {
        key1Grid().cell(row(r).column(c)).click();
    }

    public void setKey2(int r, int c) {
        key2Grid().cell(row(r).column(c)).click();
    }

    public void setPatternLine(int r, int c) {
        patternLineGrid().cell(row(r).column(c)).click();
    }

    public void setPatternGridSize(int r,int c){
        patternLineRows().setText(Integer.toString(r));
        patternLineCols().setText(Integer.toString(c));
        patternLineRows().click();
    }
            
    
    
    class TiledView {

        FrameFixture window;

        public TiledView(FrameFixture window) {
            this.window = window;
        }

        public void close() {
            window.close();
        }

        public void hasColour(int row, int column, Color color) {
            window.table("grid").backgroundAt(row(row).column(column)).requireEqualTo(color);
        }
    }

    public void openprintdialog() {
        mainWindow.menuItemWithPath("File", "Print").click();

    }

    public void SelectPrintFunction(String PrintType) {
        DialogFixture PrintSelectBox = findDialog(new GenericTypeMatcher<Dialog>(Dialog.class) {

            @Override
            protected boolean isMatching(Dialog t) {
                return t.getTitle() == "Select Print Item";
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }).using(mainWindow.robot());

        PrintSelectBox.radioButton(PrintType).click();
        
        /* I Don't see ow to do this next bit safely - Need to check for a OS specific dialog with unknown yype and button parameters 
        DAH 
        */
        
        /*
        PrintSelectBox.button("Print").click();
        
        DialogFixture PrintBox = findDialog(new GenericTypeMatcher<Dialog>(Dialog.class) {

            @Override
            protected boolean isMatching(Dialog t) {
                return t.getTitle() == "Print";
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }).using(mainWindow.robot());
        
        PrintBox.button("Cancel").click();
        
        */
        
    }

    public void closeprintdialog() {
        DialogFixture PrintBox = findDialog(new GenericTypeMatcher<Dialog>(Dialog.class) {

            @Override
            protected boolean isMatching(Dialog t) {
                return t.getTitle() == "Select Print Item";
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }).using(mainWindow.robot());

        PrintBox.button("Cancel").click();

    }

    //
    // Accessors for external assertions
    //
    public int zoomLevel() {
        return ((WeavingGridControl) weavingPatternGrid().target()).getSquareWidth();
    }

    public String aboutBoxText() {
        mainWindow.menuItemWithPath("Help", "About").click();
        DialogFixture aboutBox = findDialog(new GenericTypeMatcher<JDialog>(JDialog.class) {
            @Override
            protected boolean isMatching(JDialog pane) {
                return pane.getTitle() == "About";
            }
        }).using(mainWindow.robot());
        String text = (String) aboutBox.optionPane().target().getMessage();
        aboutBox.button().click();
        return text;
    }

    //
    // Assertions
    //
    public void hasHarnesses(int h) {
        tieUpGrid().requireRowCount(h);
        threadingDraftGrid().requireRowCount(h);
    }

    public void hasEnds(int e) {
        threadingDraftGrid().requireColumnCount(e);
        weavingPatternGrid().requireColumnCount(e);
        warpEndColorGrid().requireColumnCount(e);
    }

    public void hasPicks(int p) {
        pickColorGrid().requireRowCount(p);
        weavingPatternGrid().requireRowCount(p);
        treadlingDraftGrid().requireRowCount(p);
    }

    public void hasTreadles(int t) {
        treadlingDraftGrid().requireColumnCount(t);
        tieUpGrid().requireColumnCount(t);
    }

    public void draftIs(final int r, final int c, final Color expected) {
        weavingPatternGrid().cell(row(r).column(c)).background().requireEqualTo(expected);
    }

    public void pickIs(final int r, final int c, final Color expected) {
        treadlingDraftGrid().cell(row(r).column(c)).background().requireEqualTo(expected);
    }

    public void endIs(final int r, final int c, final Color expected) {
        threadingDraftGrid().cell(row(r).column(c)).background().requireEqualTo(expected);
    }

    public void harnessIs(final int r, final int c, final Color expected) {
        //int r = threadingDraftGrid().rowCount() - 1 - h;
        
        threadingDraftGrid().cell(row(r).column(c)).background().requireEqualTo(expected);
    }

    public void paletteIs(int index, Color color) {
        paletteGrid().cell(row(0).column(index)).background().requireEqualTo(color);
    }

    public void statusBarContains(String text) {
        mainWindow.textBox("statusBar").requireText(text);
    }

    public void initialIs(int r, int c, Color expected) {
        initialGrid().cell(row(r).column(c)).background().requireEqualTo(expected);
    }

    //
    // UI element access
    //
    private JTableFixture weavingPatternGrid() {
        return mainWindow.table("weavingPatternGrid");
    }

    public JTableFixture threadingDraftGrid() {
        return mainWindow.table("threadingDraftGrid");
    }

    private JTableFixture warpEndColorGrid() {
        return mainWindow.table("warpEndColorGrid");
    }

    private JTableFixture treadlingDraftGrid() {
        return mainWindow.table("treadlingDraftGrid");
    }

    private JTableFixture pickColorGrid() {
        return mainWindow.table("pickColorGrid");
    }

    private JTableFixture tieUpGrid() {
        return mainWindow.table("tieUpGrid");
    }

    private JTableFixture paletteGrid() {
        return mainWindow.table("paletteGrid");
    }

    private JTableFixture initialGrid() {
        return mainWindow.table("initialGrid");
    }

    private JTableFixture key1Grid() {
        return mainWindow.table("key1Grid");
    }

    private JTableFixture key2Grid() {
        return mainWindow.table("key2Grid");
    }

    private JTableFixture patternLineGrid() {
        return mainWindow.table("patternLineGrid");
    }
    
    private JTextComponentFixture patternLineRows(){
     return mainWindow.textBox("patternLineRows");}
    
        private JTextComponentFixture patternLineCols(){
     return mainWindow.textBox("patternLineCols");}
    //
    // Helpers
    //

    private void drag(JTableFixture table,
            final int startRow,
            final int startColumn,
            final int endRow,
            final int endColumn) {
        drag(table, startRow, startColumn, endRow, endColumn, 0);
    }

    private void drag(JTableFixture table,
            final int startRow,
            final int startColumn,
            final int endRow,
            final int endColumn,
            final int mod) {
        WeavingGridControl jtable = (WeavingGridControl) table.target();
        final int startX = (int) ((startColumn + 0.5) * jtable.getSquareWidth());
        final int startY = (int) ((startRow + 0.5) * jtable.getSquareWidth());
        final int endX = (int) ((endColumn + 0.5) * jtable.getSquareWidth());
        final int endY = (int) ((endRow + 0.5) * jtable.getSquareWidth());

        // Autoscrolling will prevent the second drag being handled, so
        // suppress it.
        jtable.setAutoscrolls(false);

        mainWindow.robot().moveMouse(jtable, startX, startY);
        mainWindow.robot().pressModifiers(mod);
        mainWindow.robot().pressMouse(jtable, new Point(startX, startY), MouseButton.LEFT_BUTTON);
        final int nsteps = Math.max(Math.abs(endColumn - startColumn), Math.abs(endRow - startRow)) * 2;
        for (int i = 0; i != nsteps; ++i) {
            mainWindow.robot().moveMouse(jtable,
                    startX + (endX - startX) * i / nsteps,
                    startY + (endY - startY) * i / nsteps);
        }
        mainWindow.robot().moveMouse(jtable, endX, endY);
        mainWindow.robot().releaseMouseButtons();
        mainWindow.robot().releaseModifiers(mod);
    }
}
