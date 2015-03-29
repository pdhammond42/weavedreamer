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

package com.jenkins.weavingsimulator;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import com.jenkins.weavingsimulator.models.AbstractWeavingDraftModel;
import com.jenkins.weavingsimulator.models.EditingSession;
import com.jenkins.weavingsimulator.models.StatusBarModel;
import com.jenkins.weavingsimulator.models.StepColorModel;
import com.jenkins.weavingsimulator.models.ThreadingDraftModel;
import com.jenkins.weavingsimulator.models.TieUpModel;
import com.jenkins.weavingsimulator.models.TreadlingDraftModel;
import com.jenkins.weavingsimulator.models.WarpEndColorModel;
import com.jenkins.weavingsimulator.models.WeavingPatternModel;

/**
 * 
 * @author ajenkins
 * 
 */public class WeavingDraftWindow extends EditingSessionWindow {

	/** Creates new form WeavingDraftWindow */
	public WeavingDraftWindow(EditingSession session) {
		super(session);
		initComponents();

		jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
		jScrollPane1.getHorizontalScrollBar().setUnitIncrement(16);

		for (GridControl g: grids) {
			g.setSquareWidth(15);
		}
		
		WeavingDraft draft = session.getDraft();
		
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
		
		statusBar = new StatusBarControl(sbModel);
		statusBar.setName("statusBar");
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
		statusBar.setText("0,0");
		
		setName("WeavingDraftWindow");
		
		fileChangedHandler(getSession().getFile());
		draftModifiedChangedHandler(getSession().isDraftModified());
		palettePanel.setSession(getSession());
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * This has been modified beyond recognition of the form editor, 
	 * don't try to use it with Netbeans.
	 * A specialisation of this class should create its component objects,
	 * then call the base class method. 
	 */
	protected void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jScrollPane1 = new javax.swing.JScrollPane();
		jPanel1 = new javax.swing.JPanel();
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

		jPanel1.setLayout(new java.awt.GridBagLayout());
		layoutComponents();
		
		jScrollPane1.setViewportView(jPanel1);
		getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

		pack();
	}
	
	/** Insert the components into the display. An override should 
	 * insert the base class's components into the specialisation's layout. 
	 * The implementation should use insertComponent to actually do the layout.
	 */
	protected void layoutComponents() {
		insertComponent(warpEndColorGrid, 1, 0);
		insertComponent(threadingDraftGrid, 1, 1);
		insertComponent(tieUpGrid, 2, 1);
		insertComponent(weavingPatternGrid, 1, 2);
		insertComponent(treadlingDraftGrid, 2, 2);
		insertComponent(pickColorGrid, 3,2);
		insertComponent(palettePanel, 0,2);		
	}
	
	// Inserts the given component into the control grid on the main scroll panel.
	protected void insertComponent (Component comp, int gridX, int gridY) {
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = gridX;
		gridBagConstraints.gridy = gridY;
		gridBagConstraints.insets = new java.awt.Insets(5, 5,5, 5);
		jPanel1.add(comp, gridBagConstraints);		
	}
	
	// Overload to ensure the control gets added to the list of zoomable grids.
	protected void insertComponent (GridControl comp, int gridX, int gridY) {
		insertComponent((Component)comp, gridX, gridY);
		grids.add(comp);
	}

	private void fileChangedHandler(File file) {
		if (file != null)
			setTitle(file.getName());
	}

	public void displayTiledView() {
		if (tiledViewFrame == null) {
			wpanel = new WeavingPatternPanel(getSession().getDraft());
			wpanel.setName("draftPanel");
			tiledViewFrame = new javax.swing.JFrame(getTitle());
			tiledViewFrame.getContentPane().add(wpanel);
			tiledViewFrame.pack();
			tiledViewFrame.setSize(400, 400);
			addPropertyChangeListener(
					javax.swing.JInternalFrame.TITLE_PROPERTY,
					new java.beans.PropertyChangeListener() {
						public void propertyChange(
								java.beans.PropertyChangeEvent e) {
							tiledViewFrame.setTitle((String) e.getNewValue());
						}
					});
		}
		tiledViewFrame.setVisible(true);
	}

	public void hideTiledView() {
		if (tiledViewFrame != null)
			tiledViewFrame.setVisible(false);
	}

	private void draftModifiedChangedHandler(boolean draftModified) {
		if (draftModified) {
			setTitle(getTitle() + " *");
		} else {
			String oldTitle = getTitle();
			if (oldTitle.endsWith(" *"))
				setTitle(oldTitle.substring(0, oldTitle.length() - 2));
		}
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
	
	// Nasty code alert: these are protected so the derived class can
	// put them into the correct place in the grid layout.
	protected javax.swing.JPanel jPanel1;
	protected javax.swing.JScrollPane jScrollPane1;
	protected PalettePanel palettePanel;
	protected WeavingGridControl pickColorGrid;
	protected WeavingGridControl threadingDraftGrid;
	protected WeavingGridControl tieUpGrid;
	protected WeavingGridControl treadlingDraftGrid;
	protected WeavingGridControl warpEndColorGrid;
	protected GridControl weavingPatternGrid;

	private StatusBarControl statusBar;

	private javax.swing.JFrame tiledViewFrame = null;
	private WeavingPatternPanel wpanel = null;
	
	private ArrayList<GridControl> grids = new ArrayList<GridControl>();
}
