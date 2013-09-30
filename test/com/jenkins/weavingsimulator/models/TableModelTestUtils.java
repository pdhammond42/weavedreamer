/*
 * TableModelTestUtils.java
 * 
 * Created on January 11, 2005, 6:28 PM
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

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import junit.framework.Assert;

/**
 *
 * @author ajenkins
 */
class TableModelTestUtils {
    /**
     * Asserts that the table cell specified by row and col is included
     * in the cells specified by event, and that event is an UPDATE event.
     * It is OK if event also includes other elements than the specified one.
     */
    public static void assertTableCellUpdateEvent(
            TableModelEvent event, int row, int col) 
    {
        Assert.assertNotNull("event not null", event);
        Assert.assertTrue("row is included: "+row,
                event.getFirstRow() == TableModelEvent.HEADER_ROW ||
                (event.getFirstRow() <= row && event.getLastRow() >= row));
        Assert.assertTrue("column is included: "+col, 
                event.getColumn() == TableModelEvent.ALL_COLUMNS ||
                event.getColumn() == col);
        Assert.assertEquals("event type", 
                TableModelEvent.UPDATE, event.getType());
    }
    
    /**
     * Asserts that event specifies an update to only the cell
     * specified by row and col.
     */
    public static void assertOneTableCellUpdateEvent(
            TableModelEvent event, int row, int col) 
    {
        Assert.assertNotNull("event not null", event);
        Assert.assertEquals("firstRow", row, event.getFirstRow());
        Assert.assertEquals("lastRow", row, event.getLastRow());
        Assert.assertEquals("column", col, event.getColumn());
        Assert.assertEquals("event type is UPDATE",
                TableModelEvent.UPDATE, event.getType());
    }
    
    /**
     * Asserts that all cells in model are included in event.
     */
    public static void assertAllTableCellsUpdateEvent(
            TableModelEvent event, TableModel model) 
    {
        Assert.assertNotNull("event not null", event);
        Assert.assertTrue("all rows", 
                event.getFirstRow() == TableModelEvent.HEADER_ROW ||
                (event.getFirstRow() == 0 && 
                 event.getLastRow() >= model.getRowCount() - 1));
        Assert.assertTrue("all columns",
                event.getColumn() == TableModelEvent.ALL_COLUMNS);
        Assert.assertEquals("event type is UPDATE",
                TableModelEvent.UPDATE, event.getType());
    }
    
    /**
     * Asserts that one column of model is included in event.
     */
    public static void assertTableColumnUpdateEvent(
            TableModelEvent event, TableModel model, int col) 
    {
        Assert.assertNotNull("event not null", event);
        Assert.assertTrue("column "+col, 
                event.getColumn() == col || 
                event.getColumn() == TableModelEvent.ALL_COLUMNS);
        Assert.assertTrue("rows", 
                event.getFirstRow() == TableModelEvent.HEADER_ROW ||
                (event.getFirstRow() == 0 && 
                 event.getLastRow() >= model.getRowCount() - 1));
        Assert.assertEquals("event type is UPDATE",
                TableModelEvent.UPDATE, event.getType());
    }
    
    /**
     * Assert that on and only one row of model is included in event.
     */
    public static void assertTableRowUpdateEvent(
            TableModelEvent event, TableModel model, int row) 
    {
        Assert.assertNotNull("event not null", event);
        Assert.assertTrue("row "+row,
                event.getFirstRow() == TableModelEvent.HEADER_ROW ||
                (event.getFirstRow() <= row && event.getLastRow() >= row));
        Assert.assertEquals("column",
                TableModelEvent.ALL_COLUMNS, event.getColumn());
        Assert.assertEquals("event type is UPDATE",
                TableModelEvent.UPDATE, event.getType());
    }
}
