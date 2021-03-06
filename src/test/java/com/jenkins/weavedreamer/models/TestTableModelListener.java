/*
 * TestTableModelListener.java
 * 
 * Created on January 9, 2005, 11:08 PM
 *  
 * Copyright 2005 Adam P. Jenkins
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


package com.jenkins.weavedreamer.models;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Implementation of TableModelListener for use in unit tests
 * to test if the correct TableModelEvent is being raised.
 *
 * @author ajenkins
 */
class TestTableModelListener implements TableModelListener {
    TableModelEvent event = null;
  
    /// saves a reference to <CODE>e</CODE> in this.event
    public void tableChanged(TableModelEvent e) {
        event = e;
    }
        
}
