package com.jenkins.weavingsimulator;

import com.jenkins.weavingsimulator.models.*;

public class NetworkDraftWindow extends WeavingDraftWindow {

	public NetworkDraftWindow(EditingSession session) {
		super(session);
		
		threadingPatternGrid.setModel(new ThreadingPatternLineModel(session));
		threadingPatternGrid.setName("threadingPatternGrid");
		threadingPatternGrid.setToolTipText("Set pattern line");
		
		threadingInitialGrid.setModel(new ThreadingInitialModel(session));
		threadingInitialGrid.setName("threadingInitialGrid");
		threadingInitialGrid.setToolTipText("Set initial for threading");
		
		pickPatternGrid.setModel(new PickPatternLineModel(session));
		pickPatternGrid.setName("pickPatternGrid");
		pickPatternGrid.setToolTipText("Set pattern line");

		pickInitialGrid.setModel(new PickInitialModel(session));
		pickInitialGrid.setName("pickInitialGrid");
		pickInitialGrid.setToolTipText("Set initial for picks");
	}

	protected void initComponents () {
		threadingPatternGrid = new WeavingGridControl();
		threadingInitialGrid = new WeavingGridControl();
		pickPatternGrid = new WeavingGridControl();
		pickInitialGrid = new WeavingGridControl();
		super.initComponents();
	}
	
	protected void layoutComponents() {
		insertComponent(warpEndColorGrid, 1, 0);
		
		insertComponent(threadingInitialGrid, 0, 1);
		insertComponent(threadingPatternGrid, 1, 1);
		
		insertComponent(threadingDraftGrid, 1, 2);
		insertComponent(tieUpGrid, 2, 2);
		insertComponent(pickInitialGrid, 3, 2);
		
		insertComponent(palettePanel, 0, 3);	
		insertComponent(weavingPatternGrid, 1, 3);
		insertComponent(treadlingDraftGrid, 2, 3);
		insertComponent(pickPatternGrid, 3, 3);
		insertComponent(pickColorGrid, 4,3);
	}
	
	protected WeavingGridControl threadingPatternGrid;
	protected WeavingGridControl threadingInitialGrid;
	protected WeavingGridControl pickPatternGrid;
	protected WeavingGridControl pickInitialGrid;
}
