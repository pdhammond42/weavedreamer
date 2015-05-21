package com.jenkins.weavingsimulator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;

public class TextFieldBinder implements PropertyChangeListener{
	JFormattedTextField field;
	
	public TextFieldBinder(JFormattedTextField field) {
		this.field = field;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		field.setValue(evt.getNewValue());
	}
}
