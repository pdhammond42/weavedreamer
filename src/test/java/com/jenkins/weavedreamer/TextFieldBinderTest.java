package com.jenkins.weavedreamer;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.beans.PropertyChangeSupport;

import javax.swing.JFormattedTextField;

import junit.framework.TestCase;

public class TextFieldBinderTest extends TestCase {

	public class ABean {
	    private PropertyChangeSupport propertyChangeSupport =
	            new PropertyChangeSupport(this);
	    
	    public void addPropertyChangeListener(String propertyName, 
	            java.beans.PropertyChangeListener l) {
	        propertyChangeSupport.addPropertyChangeListener(propertyName, l);
	    }

	    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
	        propertyChangeSupport.addPropertyChangeListener(l);
	    }

	    private int foo;
		
	    public void setFoo(int f) {
			int old = foo;
			foo = f;
			propertyChangeSupport.firePropertyChange("foo", old, foo);					
		}
		
		public int getFoo() {
			return foo;
		}
	}
	
	public void testValueUpdatesOnEvent() {
		JFormattedTextField field = new JFormattedTextField();
		ABean bean = new ABean();
		
		bean.addPropertyChangeListener("foo",  new TextFieldBinder (field));
		
		bean.setFoo(42);
		assertThat(field.getText(), is("42"));
	}
}
