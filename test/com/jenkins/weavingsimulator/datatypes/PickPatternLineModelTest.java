package com.jenkins.weavingsimulator.datatypes;

import com.jenkins.weavingsimulator.models.EditingSession;
import com.jenkins.weavingsimulator.models.PickPatternLineModel;

import junit.framework.TestCase;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PickPatternLineModelTest extends TestCase {
	private WeavingDraft draft;
	private EditingSession session;
	
	public void setUp() {
		draft = new WeavingDraft("TestDraft");
		session = new EditingSession(draft);
	}
	
	public void testConstructionFromSession () {
		PickPatternLineModel model = new PickPatternLineModel(session);
		assertThat(model.getRowCount(), is(draft.getPicks().size()));
		//assertThat(model.getColumnCount(), is(draft.))
	}
}
