package com.jenkins.weavedreamer;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TextFieldBinder implements PropertyChangeListener {
    JFormattedTextField field;

    public TextFieldBinder(JFormattedTextField field) {
        this.field = field;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        field.setValue(evt.getNewValue());
    }
}
