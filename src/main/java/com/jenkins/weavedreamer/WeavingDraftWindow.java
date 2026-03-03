/*
 * WeavingDraftWindow.java
 *
 * Created on April 5, 2003, 4:47 AM
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

package com.jenkins.weavedreamer;

import com.jenkins.weavedreamer.models.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.HORIZONTAL;

/**
 * @author ajenkins
 */
public class WeavingDraftWindow extends EditingSessionWindow {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates new form WeavingDraftWindow
     */
    public WeavingDraftWindow(EditingSession session) {
        super(session);
        initComponents();
        setFrameIcon(new ImageIcon(getClass().getResource("icon-24.png")));
        // stop auto closing
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane1.getHorizontalScrollBar().setUnitIncrement(16);

        for (GridControl g : grids) {
            g.setSquareWidth(15);
        }

        weavingPatternGrid.setModel(new WeavingPatternModel(session));
        weavingPatternGrid.setName("weavingPatternGrid");
        weavingPatternGrid.setIntercellSpacing(new Dimension(0, 0));
        weavingPatternGrid.setShowGrid(false);

        threadingDraftGrid.setModel(new ThreadingDraftModel(session));
        threadingDraftGrid.setName("threadingDraftGrid");
        threadingDraftGrid.setToolTipText("Set shaft");

        warpEndColorGrid.setModel(new WarpEndColorModel(session));
        warpEndColorGrid.setName("warpEndColorGrid");
        warpEndColorGrid.setToolTipText("Set warp colour");

        tieUpGrid.setModel(new TieUpModel(session));
        tieUpGrid.setName("tieUpGrid");
        tieUpGrid.setToolTipText("Set tie-up");

        treadlingDraftGrid.setModel(new TreadlingDraftModel(session));
        treadlingDraftGrid.setName("treadlingDraftGrid");
        treadlingDraftGrid.setToolTipText("Set treadle");

        pickColorGrid.setModel(new StepColorModel(session));
        pickColorGrid.setName("pickColorGrid");
        pickColorGrid.setToolTipText("Set weft colour");

        StatusBarModel sbModel = new StatusBarModel();
        sbModel.listen((AbstractWeavingDraftModel) treadlingDraftGrid.getModel());
        sbModel.listen((AbstractWeavingDraftModel) threadingDraftGrid.getModel());
        sbModel.listen((AbstractWeavingDraftModel) pickColorGrid.getModel());
        sbModel.listen((AbstractWeavingDraftModel) warpEndColorGrid.getModel());
        sbModel.listen((AbstractWeavingDraftModel) tieUpGrid.getModel());

        statusPanel = new JPanel();
        statusPanel.setLayout(new java.awt.GridBagLayout());

        statusBar = new StatusBarControl(sbModel);
        statusBar.setName("statusBar");
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = HORIZONTAL;
        statusPanel.add(statusBar, gridBagConstraints);
        statusBar.setText("0,0");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.9;
        statusPanel.add(palettePanel, gridBagConstraints);

        getContentPane().add(statusPanel, BorderLayout.NORTH);
        setName("WeavingDraftWindow");

        pack();
        palettePanel.setSession(getSession());

        /*
        getContentPane().addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                var border = 200;
                var gridSize = weavingPatternGrid.getBounds();
                var winSize = getContentPane().getBounds();
                var size = new Dimension(gridSize.width, gridSize.height);
                if (gridSize.height > winSize.height - border) {
                    size.height = winSize.height - border;
                }
                if (gridSize.width > winSize.width - border) {
                    size.width = winSize.width - border;
                }
                centreView.setExtentSize(size);
                topView.setExtentSize(new Dimension(size.width, topView.getHeight()));
                rightView.setExtentSize(new Dimension(rightView.getWidth(), size.height));
            }


        });

         */
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * This has been modified beyond recognition of the form editor,
     * don't try to use it with Netbeans.
     */
    protected void initComponents() {
        jScrollPane1 = new JScrollPane();
        draftPanel = new JPanel();
        warpEndColorGrid = new WeavingGridControl();
        threadingDraftGrid = new WeavingGridControl();
        tieUpGrid = new WeavingGridControl();
        weavingPatternGrid = new WeavingGridControl();
        treadlingDraftGrid = new WeavingGridControl();
        pickColorGrid = new WeavingGridControl();
        palettePanel = new PalettePanel();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        JScrollBar vscroll = new JScrollBar();
        JScrollBar hscroll = new JScrollBar(JScrollBar.HORIZONTAL);

        topView = new JViewport() {
            @Override
            public Dimension getPreferredSize() {
                var border = tieUpGrid.getWidth() * 2;

                var s = super.getPreferredSize();
                var w = this.getParent().getWidth();
                if (s.width > w - border) {
                    s.width = w - border;
                }
                return s;
            }
        };

        rightView = new JViewport() {
            @Override
            public Dimension getPreferredSize() {
                var border = tieUpGrid.getHeight() *2;
                var s = super.getPreferredSize();
                var h = this.getParent().getHeight();
                if (s.height > h - border) {
                    s.height = h - border;
                }
                return s;
            }
        };

        centreView = new JViewport() {
            @Override
            public Dimension getPreferredSize() {
                var s = super.getPreferredSize();
                var border = tieUpGrid.getWidth() * 2;
                var w = this.getParent().getWidth();
                if (s.width > w - border) {
                    s.width = w - border;
                    hscroll.setEnabled(true);
                } else {
                    hscroll.setEnabled(false);
                }
                border = tieUpGrid.getHeight() *2;
                var h = this.getParent().getHeight();
                if (s.height > h - border) {
                    s.height = h - border;
                    vscroll.setEnabled(true);
                } else {
                    vscroll.setEnabled(false);
                }
                return s;
            }
        };

        vscroll.addAdjustmentListener(e -> {
            rightView.setViewPosition(new Point(
                    0,
                    centreView.getHeight() * vscroll.getValue() / vscroll.getMaximum()
            ));
            centreView.setViewPosition(new Point(
                    centreView.getViewPosition().x,
                    centreView.getHeight() * vscroll.getValue() / vscroll.getMaximum()
            ));
        });

        hscroll.addAdjustmentListener(e -> {
            topView.setViewPosition(new Point(
                    centreView.getWidth() * hscroll.getValue() / hscroll.getMaximum(),
                    0
            ));
            centreView.setViewPosition(new Point(
                    centreView.getWidth() * hscroll.getValue() / hscroll.getMaximum(),
                    centreView.getViewPosition().y
            ));
        });


        JPanel right = new JPanel();
        right.setLayout(new java.awt.GridBagLayout());
        insertComponent(right, pickColorGrid, 1, 0);
        insertComponent(right, treadlingDraftGrid, 0, 0);
        rightView.setView(right);

        JPanel centrePanel = new JPanel();
        centrePanel.setLayout(new java.awt.BorderLayout());
        centrePanel.add(weavingPatternGrid);
        centreView.setView(centrePanel);

        JPanel top = new JPanel();
        top.setLayout(new java.awt.GridBagLayout());
        insertComponent(top, warpEndColorGrid, 0, 0);
        insertComponent(top, threadingDraftGrid, 0, 1);
        topView.setView(top);

        draftPanel.setLayout(new java.awt.GridBagLayout());

        insertComponent(draftPanel, hscroll, 0, 0);
        insertComponent(draftPanel, topView, 0, 1);
        insertComponent(draftPanel, centreView, 0, 2);

        insertComponent(draftPanel, tieUpGrid, 1, 1);
        insertComponent(draftPanel, rightView, 1, 2);
        insertComponent(draftPanel, vscroll, 2, 2);

        getContentPane().add(draftPanel, BorderLayout.CENTER);
    }

    // Inserts the given component into the control grid on the main scroll panel.
    private void insertComponent(JPanel where, Component comp, int gridX, int gridY) {
        var gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = gridX;
        gridBagConstraints.gridy = gridY;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        where.add(comp, gridBagConstraints);
    }

    public void zoomIn() {
        int square = grids.get(0).getSquareWidth() * 2;
        for (GridControl g : grids) {
            g.setSquareWidth(square);
        }
    }

    public void zoomOut() {
        int square = grids.get(0).getSquareWidth() / 2;
        for (GridControl g : grids) {
            g.setSquareWidth(square);
        }
    }

    public JPanel getdraftPanel() {
        return draftPanel;
    }

    // Nasty code alert: these are protected so the derived class can
    // put them into the correct place in the grid layout.
    protected JPanel draftPanel;
    protected JPanel statusPanel;
    protected JScrollPane jScrollPane1;
    protected PalettePanel palettePanel;
    protected WeavingGridControl pickColorGrid;
    protected WeavingGridControl threadingDraftGrid;
    protected WeavingGridControl tieUpGrid;
    protected WeavingGridControl treadlingDraftGrid;
    protected WeavingGridControl warpEndColorGrid;
    protected GridControl weavingPatternGrid;

    private JViewport topView;
    private JViewport rightView;
    private JViewport centreView;

    private StatusBarControl statusBar;

    //private javax.swing.JFrame tiledViewFrame = null;
    //private WeavingPatternPanel wpanel = null;

    private ArrayList<GridControl> grids = new ArrayList<GridControl>();

    @Override
    public String getViewName() {
        return "Draft";
    }
}
