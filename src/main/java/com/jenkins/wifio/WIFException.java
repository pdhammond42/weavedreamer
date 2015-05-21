/*
 * WIFException.java
 *
 * Created on February 5, 2006, 3:19 PM
 */

package com.jenkins.wifio;

/** Exception class thrown by WIFFile class to indicate problems with a 
 * WIF file format.
 *
 * @author ajenkins
 */
public class WIFException extends java.lang.RuntimeException {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * Creates a new instance of <code>WIFException</code> without detail message.
     */
    public WIFException() {
    }
    
    
    /**
     * Constructs an instance of <code>WIFException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public WIFException(String msg) {
        super(msg);
    }
}
