/*
 * BeanTestUtils.java
 * 
 * Created on January 13, 2005, 2:05 AM
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

package com.jenkins.weavedreamer.datatypes;

import java.beans.PropertyChangeEvent;
import java.util.Map;

import org.junit.Assert;

import java.beans.IndexedPropertyChangeEvent;

/**
 * 
 * @author ajenkins
 */
class BeanTestUtils {
    /**
     * used to check if a propertyChange event was correctly fired, by asserting
     * that the properties of the PropertyChangeEvent are set correctly.
     */
    public static void assertPropertyChangeFired(
            Map<String, PropertyChangeEvent> events, String propName,
            Object oldValue, Object newValue, int index) {
        PropertyChangeEvent evt = events.get(propName);
        Assert.assertNotNull(evt);
        Assert.assertEquals("property name", propName, evt.getPropertyName());
        Assert.assertEquals("old property value", oldValue, evt.getOldValue());
        Assert.assertEquals("new property value", newValue, evt.getNewValue());
        if (index != -1) {
            Assert.assertTrue(evt instanceof IndexedPropertyChangeEvent);
            Assert.assertEquals(index, ((IndexedPropertyChangeEvent) evt)
                    .getIndex());
        } else
            Assert.assertFalse(evt instanceof IndexedPropertyChangeEvent);
    }
}
