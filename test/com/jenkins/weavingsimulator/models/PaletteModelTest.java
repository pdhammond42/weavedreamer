/*
 * PaletteModelTest.java
 * JUnit based test
 * 
 * Created on January 13, 2005, 11:34 AM
 *  
 * Copyright 2005 Adam P. Jenkins
 * 
 * This file is part of WeavingSimulator
 * 
 * WeavingSimulator is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * WeavingSimulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WeavingSimulator; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


package com.jenkins.weavingsimulator.models;

import com.jenkins.weavingsimulator.datatypes.Palette;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import junit.framework.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author ajenkins
 */
public class PaletteModelTest extends TestCase {
    private Palette palette;
    private PaletteModel model;
    private TestTableModelListener listener;
    
    public PaletteModelTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
        palette = new Palette(
                Arrays.asList(Color.RED, Color.GREEN, Color.BLUE), "");
        model = new PaletteModel(palette);
        listener = new TestTableModelListener();
        model.addTableModelListener(listener);
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(PaletteModelTest.class);
        
        return suite;
    }

    public void testGetValueAt() {
        for (int row = 0; row < model.getRowCount(); row++) {
            assertEquals("row="+row, 
                    palette.getColor(row), model.getValueAt(row, 0));
        }
    }

    public void testGetRowCount() {
        assertEquals(palette.getNumColors(), model.getRowCount());
    }

    public void testGetColumnCount() {
        assertEquals(1, model.getColumnCount());
    }

    public void testSetValueAt() {
        model.setValueAt(Color.CYAN, 0, 0);
        assertEquals(Color.CYAN, palette.getColor(0));
    }

    public void testGetColumnClass() {
        for (int col = 0; col < model.getColumnCount(); col++) {
            assertEquals("col="+col, Color.class, model.getColumnClass(col));
        }
    }

    public void testIsCellEditable() {
        for (int r = 0; r < model.getRowCount(); r++) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                assertTrue("row="+r+",col="+c, model.isCellEditable(r, c));
            }
        }
    }
    
    public void testNotifyListenerOnSet() {
        palette.setColor(1, Color.CYAN);
        TableModelTestUtils.assertOneTableCellUpdateEvent(listener.event, 1, 0);
    }
    
    public void testNotifyListenerOnNumColorsChanged() {
        palette.setNumColors(5);
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    public void testNotifyListenerOnColorsChanged() {
        palette.setColors(Arrays.asList(Color.RED, Color.WHITE, Color.BLUE));
        TableModelTestUtils.assertAllTableCellsUpdateEvent(listener.event, model);
    }
    
    public void testNoExtraNotifications() {
        palette.setSelection(1);
        assertNull(listener.event);
    }
    
    public void testSerialisation() throws IOException, ClassNotFoundException {
    	// More of a test of my learning the library.
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	ObjectOutputStream oos = new ObjectOutputStream(os);
    	oos.writeObject(palette);
    	
    	ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    	ObjectInputStream ois = new ObjectInputStream(is);
    	Palette p = (Palette)(ois.readObject());
    	
    	assertThat(p.getColors(), equalTo(palette.getColors()));
    }
}
