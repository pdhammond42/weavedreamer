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

import java.awt.*;
import java.util.ArrayList;

import com.jenkins.weavedreamer.models.AbstractWeavingDraftModel;
import com.jenkins.weavedreamer.models.EditingSession;
import com.jenkins.weavedreamer.models.StatusBarModel;
import com.jenkins.weavedreamer.models.StepColorModel;
import com.jenkins.weavedreamer.models.ThreadingDraftModel;
import com.jenkins.weavedreamer.models.TieUpModel;
import com.jenkins.weavedreamer.models.TreadlingDraftModel;
import com.jenkins.weavedreamer.models.WarpEndColorModel;
import com.jenkins.weavedreamer.models.WeavingPatternModel;

import javax.swing.*;

import static java.awt.GridBagConstraints.*;

/**
 * 
 * @author ajenkins
 * 
 */public class WeavingDraftWindow extends EditingSessionWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates new form WeavingDraftWindow */
	public WeavingDraftWindow(EditingSession session) {
		super(session);
		initComponents();
		setFrameIcon(new ImageIcon(getClass().getResource("icon-24.png")));
                // stop auto closing 
                setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
		jScrollPane1.getHorizontalScrollBar().setUnitIncrement(16);

		for (GridControl g: grids) {
			g.setSquareWidth(15);
		}
		
		weavingPatternGrid.setModel(new WeavingPatternModel(session));
		weavingPatternGrid.setName("weavingPatternGrid");
		weavingPatternGrid.setIntercellSpacing(new Dimension(0,0));
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
		sbModel.listen((AbstractWeavingDraftModel)treadlingDraftGrid.getModel());
		sbModel.listen((AbstractWeavingDraftModel)threadingDraftGrid.getModel());
		sbModel.listen((AbstractWeavingDraftModel)pickColorGrid.getModel());
		sbModel.listen((AbstractWeavingDraftModel)warpEndColorGrid.getModel());
		sbModel.listen((AbstractWeavingDraftModel)tieUpGrid.getModel());

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
                

		draftPanel.setLayout(new java.awt.GridBagLayout());
		insertComponent(warpEndColorGrid, 1, 0);
		insertComponent(threadingDraftGrid, 1, 1);
		insertComponent(tieUpGrid, 2, 1);
		insertComponent(weavingPatternGrid, 1, 2);
		insertComponent(treadlingDraftGrid, 2, 2);
		insertComponent(pickColorGrid, 3,2);

		jScrollPane1.setViewportView(draftPanel);

		getContentPane().add(jScrollPane1, BorderLayout.CENTER);
	}
	
	// Inserts the given component into the control grid on the main scroll panel.
	private void insertComponent (Component comp, int gridX, int gridY) {
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = gridX;
		gridBagConstraints.gridy = gridY;
		gridBagConstraints.insets = new java.awt.Insets(5, 5,5, 5);
        draftPanel.add(comp, gridBagConstraints);
	}
	
	// Overload to ensure the control gets added to the list of zoomable grids.
	private void insertComponent (GridControl comp, int gridX, int gridY) {
		insertComponent((Component)comp, gridX, gridY);
		grids.add(comp);
	}

	public void zoomIn() {
		int square = grids.get(0).getSquareWidth() * 2;
		for (GridControl g: grids) {
			g.setSquareWidth(square);
		}
	}

	public void zoomOut() {
		int square =  grids.get(0).getSquareWidth() / 2;
		for (GridControl g: grids) {
			g.setSquareWidth(square);
		}
	}
	
        public JPanel getdraftPanel(){
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

	private StatusBarControl statusBar;

	//private javax.swing.JFrame tiledViewFrame = null;
	//private WeavingPatternPanel wpanel = null;
	
	private ArrayList<GridControl> grids = new ArrayList<GridControl>();

	@Override
	public String getViewName() {
		return "Draft";
	}
}
