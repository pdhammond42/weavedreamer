/*
 * WeavingPatternPanel.java
 * 
 * Created on April 8, 2003, 12:28 AM
 *  
 * Copyright 2003 Adam P. Jenkins
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


package com.jenkins.weavingsimulator;

import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import com.jenkins.weavingsimulator.models.EditingSession;
import com.jenkins.weavingsimulator.models.TiledTableModelAdapter;
import com.jenkins.weavingsimulator.models.WeavingPatternModel;
import java.awt.Dimension;
import javax.swing.table.TableModel;

/**
 *
 * @author  ajenkins
 */
public class WeavingPatternPanel extends javax.swing.JPanel {    
    /** Creates new form WeavingPatternPanel */
    public WeavingPatternPanel(WeavingDraft draft) {
        initComponents();
        WeavingPatternModel wmodel = new WeavingPatternModel(new EditingSession(draft));
        TableModel model = new TiledTableModelAdapter(wmodel);
        weavingPatternGrid.setModel(model);
        weavingPatternGrid.setSquareWidth(10);
        weavingPatternGrid.setName("grid");
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        weavingPatternGrid = new com.jenkins.weavingsimulator.GridControl();

        setLayout(new java.awt.BorderLayout());

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        weavingPatternGrid.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        weavingPatternGrid.setRowSelectionAllowed(false);
        add(weavingPatternGrid, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        TiledTableModelAdapter tmodel = 
            (TiledTableModelAdapter)weavingPatternGrid.getModel();
        TableModel model = tmodel.getModel();
        
        if (model.getRowCount() == 0 || model.getColumnCount() == 0)
            return;
        
        // compute how many rows and columns of wmodel are needed to fill 
        // component
        int tileWidth = model.getColumnCount() * weavingPatternGrid.getSquareWidth();
        int tileHeight = model.getRowCount() * weavingPatternGrid.getSquareWidth();
        Dimension dim = getSize();
        int numTileRows = (dim.height + tileHeight - 1) / tileHeight;
        int numTileCols = (dim.width + tileWidth - 1) / tileWidth;
        if (tmodel.getTiledRowCount() != numTileRows)
            tmodel.setTiledRowCount(numTileRows);
        if (tmodel.getTiledColumnCount() != numTileCols)
            tmodel.setTiledColumnCount(numTileCols);
    }//GEN-LAST:event_formComponentResized
    
    /** Getter for property draft.
     * @return Value of property draft.
     *
     */
    public WeavingDraft getDraft() {
        TiledTableModelAdapter model = 
            (TiledTableModelAdapter)weavingPatternGrid.getModel();
        WeavingPatternModel wmodel = (WeavingPatternModel)model.getModel();
        return wmodel.getDraft();
    }      
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jenkins.weavingsimulator.GridControl weavingPatternGrid;
    // End of variables declaration//GEN-END:variables
    
    
}
