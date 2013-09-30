/*
 * WIFFile.java
 *
 * Created on February 5, 2006, 3:15 PM
 */

package com.jenkins.wifio;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.ini4j.Ini;

/**
 *
 * @author ajenkins
 */
public class WIFFile {
    private Ini wif;
    
    public WIFFile(Reader reader) throws WIFException, IOException {
        wif = normalizeWif(new Ini(reader));
    }
    
    public WIFFile(InputStream ins) throws WIFException, IOException {
        wif = normalizeWif(new Ini(ins));
    }

    private String getKey(String sectionName, String key, 
            boolean stripTrailingComment) {
        Ini.Section section = wif.get(sectionName.toLowerCase());
        if (section == null) {
            throw new WIFException("No section '" + sectionName + "'");
        } else {
            String value = section.get(key.toLowerCase());
            if (value == null)
                throw new WIFException("Section '" + sectionName + "' " 
                        + "does not contain key '" + key + "'");
            if (stripTrailingComment) {
                int idx = value.indexOf(';');
                if (idx != -1)
                    value = value.substring(0, idx);
            }
            return value.trim();
        }
    }
    
    private String getKey(String sectionName, String key) {
        return getKey(sectionName, key, true);
    }

    private void setKey(String sectionName, String key, String value) {
        sectionName = sectionName.toLowerCase();
        key = key.toLowerCase();
        Ini.Section section = wif.get(sectionName);
        if (section == null) {
            section = wif.add(sectionName);
        }
        section.put(key, value);
    }
    
    public String getStringField(String section, String key) {
        return getKey(section, key);
    }

    private static Pattern trueRx = 
            Pattern.compile("true|on|yes|1", Pattern.CASE_INSENSITIVE);
    private static Pattern falseRx =
            Pattern.compile("false|off|no|0", Pattern.CASE_INSENSITIVE);
    
    public boolean getBooleanField(String section, String key) {
        String value = getKey(section, key);
        
        if (trueRx.matcher(value).matches())
            return true;
        else if (falseRx.matcher(value).matches())
            return false;
        else
            throw new WIFException("Value '" + value + "' is not a boolean");
    }

    public double getDoubleField(String section, String key) {
        String value = getKey(section, key);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException numberFormatException) {
            throw new WIFException("Value '" + value + "' is not a double");
        }
    }

    public int getIntField(String section, String key) {
        String value = getKey(section, key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException numberFormatException) {
            throw new WIFException("Value '" + value + "' is not a integer");
        }
    }

    public List<Integer> getIntListField(String section, String key) {
        String value = getKey(section, key);
        String[] vals = value.split(",");
        
        List<Integer> ints = new ArrayList<Integer>(vals.length);
        for (String val : vals) {
            ints.add(new Integer(val));
        }
        
        return ints;
    }

    public char getSymbolField(String section, String key) {
        String value = getKey(section, key);
        
        // a symbol can be a bare character, a character surrounded by single 
        // quotes, or a #CODE.  So these are equivalent: a, 'a', #97
        if (value.length() == 1) {
            return value.charAt(0);
        } else if (value.matches("\\'.\\'")) {
            return value.charAt(1);
        } else if (value.matches("\\#\\d+")) {
            return (char)Integer.parseInt(value.substring(1));
        }
        else {
            throw new WIFException("Value '" + value + "' is not a symbol");
        }
    }
    
    public java.awt.Color getColorField(String section, String key) {
        List<Integer> rgb = getIntListField(section, key);
        if (rgb.size() != 3) {
            throw new WIFException("Value '"+ rgb + "' is not a color");
        }
        return new java.awt.Color(rgb.get(0), rgb.get(1), rgb.get(2));
    }
    
    public void setBooleanField(String section, String key, boolean val) {
        setKey(section, key, String.valueOf(val));
    }
    
    public void setColorField(String section, String key, java.awt.Color val) {
        List<Integer> rgb = 
                Arrays.asList(val.getRed(), val.getGreen(), val.getBlue());
        setIntListField(section, key, rgb);
    }
    
    public void setDoubleField(String section, String key, double val) {
        setKey(section, key, String.valueOf(val));
    }
    
    public void setIntField(String section, String key, int val) {
        setKey(section, key, String.valueOf(val));
    }
    
    public void setIntListField(String section, String key, List<Integer> rgb) {
        setKey(section, key, StringUtils.join(rgb.toArray(), ','));
    }

    public void setStringField(String section, String key, String val) {
        setKey(section, key, val);
    }
    
    public void setSymbolField(String section, String key, char val) {
        setKey(section, key, "#" + (int)val);
    }

    private Ini normalizeWif(Ini ini) {
        // make all section and field names lower case
        Ini normIni = new Ini();
        for (Map.Entry<String, Ini.Section> e : ini.entrySet()) {
            Ini.Section normSec = normIni.add(e.getKey().toLowerCase());
            for (Map.Entry<String,String> e2 : e.getValue().entrySet()) {
                normSec.put(e2.getKey().toLowerCase(), e2.getValue());
            }
        }
        return normIni;
    }

}
