package com.jenkins.weavingsimulator;

import java.text.ParseException;

// formatter for JFormattedTextField which only allows integers >= 0.
public class NonNegativeIntFormatter 
    extends javax.swing.JFormattedTextField.AbstractFormatter {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	java.text.NumberFormat format = java.text.NumberFormat.getIntegerInstance();
    
    /** Parses <code>text</code> returning an arbitrary Object. Some
     * formatters may return null.
     *
     * @throws ParseException if there is an error in the conversion
     * @param text String to convert
     * @return Object representation of text
     *
     */
    public Object stringToValue(String text) throws ParseException {
        Number val = format.parse(text);
    	if (val.intValue() < 0)
            throw new ParseException("Negative numbers not allowed", 0);
        return val;
    }
    
    /** Returns the string value to display for <code>value</code>.
     *
     * @throws ParseException if there is an error in the conversion
     * @param value Value to convert
     * @return String representation of value
     *
     */
    public String valueToString(Object value) throws ParseException {
        if (value == null)
            return "";
        else
            return format.format(value);
    }
}