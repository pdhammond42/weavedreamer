package com.jenkins.weavingsimulator.models;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.TestCase;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.apache.commons.lang.ArrayUtils.*;
import com.jenkins.weavingsimulator.datatypes.WeftPick;

public class WeftPickTest extends TestCase {
	public void testRestorePick() {
		WeftPick pick = new WeftPick (Color.RED, 5, 2);
		assertThat (pick.getColor(), is(Color.RED));
		assertThat (toObject(pick.getTreadles()), arrayContaining (false, false, true, false, false));
		assertThat (pick.isTreadleSelected(0), is(false));
		assertThat (pick.isTreadleSelected(2), is(true));
	}
	
	public void testChangesAreNotified() {
		WeftPick pick = new WeftPick (Color.RED, 4, 2);
		Listener listener = new Listener();
		pick.addPropertyChangeListener(listener);
		pick.setTreadle(1, true);
		assertThat(toObject((boolean[])listener.after), arrayContaining(false, true, true, false));
		assertThat(toObject((boolean[])listener.before), arrayContaining(false, false, true, false));
		assertThat(listener.name, is ("treadles"));
		assertThat(toObject(pick.getTreadles()), arrayContaining(false, true, true, false));
	}
	
	public void testTreadlesAreSealed() {
		WeftPick pick = new WeftPick (Color.RED, 4, 2);
		pick.getTreadles()[1] = true;
		assertThat(toObject(pick.getTreadles()), arrayContaining(false, false, true, false));		
	}
	
	public void testTreadleCanTakeMultipleSelections() {
		WeftPick pick = new WeftPick (Color.RED, 4, 2, 3);
		assertThat(toObject(pick.getTreadles()), arrayContaining(false, false, true, true));		
	}
	
	public void testTreadleCanTakeNoSelection() {
		WeftPick pick = new WeftPick (Color.RED, 4);
		assertThat(toObject(pick.getTreadles()), arrayContaining(false, false, false, false));		
	}

	public void testReduceTreadleCount() {
		WeftPick pick = new WeftPick (Color.RED, 6, 2, 3, 5);
		Listener listener = new Listener();
		pick.addPropertyChangeListener(listener);
		pick.setTreadleCount(3);
		assertThat(toObject(pick.getTreadles()), arrayContaining(false, false, true));
		assertThat(toObject((boolean[])listener.before), arrayContaining (false, false, true, true, false, true));
	}
	
	public void testIncreaseTreadleCount() {
		WeftPick pick = new WeftPick (Color.RED, 3, 2);
		pick.setTreadleCount(5);
		assertThat(toObject(pick.getTreadles()), arrayContaining(false, false, true, false, false));
	}
	
	public void testConstructWithOnlyTreadleCount() {
		WeftPick pick = new WeftPick (2);
		assertThat(pick.getColor(), is(Color.WHITE));
		assertThat(toObject(pick.getTreadles()), arrayContaining(false, false));
	}
	
	public void testSetTreadleUnique() {
		WeftPick pick = new WeftPick (Color.RED, 3, 2);
		pick.setTreadleUnique(1);
		assertThat(toObject(pick.getTreadles()), arrayContaining(false, true, false));
	}
	
	private class Listener implements PropertyChangeListener {	
		public Object before;
		public Object after;
		public String name;
		public void propertyChange(PropertyChangeEvent evt) {
			before = evt.getOldValue();
			after = evt.getNewValue();
			name = evt.getPropertyName();
		}
	}
}
