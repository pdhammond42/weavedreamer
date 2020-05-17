/*
 * WIFFile.java
 *
 * Created on February 5, 2006, 3:15 PM
 */

package com.jenkins.wifio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		checkFormat();
	}
        public WIFFile(){
                 wif = new Ini();
        }
	public WIFFile(InputStream ins) throws WIFException, IOException {
		wif = normalizeWif(new Ini(ins));
		checkFormat();
	}

	private void checkFormat() throws WIFException {
		// A WIF file must contain WIF and CONTENTS sections.
		if (wif.get("contents") == null) {
			throw new WIFException("No CONTENTS section");
		}
		;
		if (wif.get("wif") == null) {
			throw new WIFException("No WIF section");
		}
		;

	}

	private String getKey(String sectionName, String key,
			boolean stripTrailingComment) {
		Ini.Section section = wif.get(sectionName.toLowerCase());
		if (section == null) {
			throw new WIFException("No section '" + sectionName + "'");
		} else {
			String value = section.get(key.toLowerCase());
			if (value == null  ){
				throw new WIFNoValueException("<"+sectionName + ">:<"+key+"> is empty");
                        }
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
                if (!hasField("CONTENTS", sectionName)  && !("wif".equalsIgnoreCase(sectionName))){
                    wif.put("contents",sectionName,"true");
        
        }
                // add check to add sectionname to contents
                
		wif.put(sectionName,key, value);
	}

	public String getStringField(String section, String key) {
		return getKey(section, key);
	}

	private static Pattern trueRx = Pattern.compile("true|on|yes|1",
			Pattern.CASE_INSENSITIVE);
	private static Pattern falseRx = Pattern.compile("false|off|no|0",
			Pattern.CASE_INSENSITIVE);

	public boolean hasField(String sectionName, String key) {
		Ini.Section section = wif.get(sectionName.toLowerCase());
		return section != null && section.get(key.toLowerCase()) != null;
	}

	public boolean getBooleanField(String section, String key) {
		String value = getKey(section, key);

		if (trueRx.matcher(value).matches())
			return true;
		else if (falseRx.matcher(value).matches())
			return false;
		else
			throw new WIFException("Value '" + value + "' is not a boolean");
	}

	/** Returns the boolean value of the field Key in Section.
	 * If the key or the section do not exist it returns deflt. (unlike the
	 * two parameter version which will throw). 
	 * @param section Section to get value from
	 * @param key Key of value to get
	 * @param deflt Value to return if the value is not found
	 * @return The value, or deflt.
	 */
	public boolean getBooleanField(String section, String key, boolean deflt) {
		if (hasField(section, key))
			return getBooleanField(section, key);
		return deflt;
	}
	
	public double getDoubleField(String section, String key) {
		String value = getKey(section, key);
                if (value.length()==0)
                {throw new WIFNoValueException("Value '" + key + "' is empty"); }
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException numberFormatException) {
			throw new WIFException("Value '" + value + "' is not a double");
		}
	}

	public int getIntField(String section, String key) {
		String value = getKey(section, key);
		if (value.length()==0)
                {throw new WIFNoValueException("Value '" + key + "' is empty"); }
                try {
			return Integer.parseInt(value);
		} catch (NumberFormatException numberFormatException) {
			throw new WIFException("Value '" + value + "' is not a integer");
		}
	}

	public List<Integer> getIntListField(String section, String key) {
		String value = getKey(section, key);
                if (value.length()==0)
                {throw new WIFNoValueException("Value '" + key + "' is empty"); }
		String[] vals = value.split(",");

		List<Integer> ints = new ArrayList<Integer>(vals.length);
                if (vals.length>0){
                    for (String val : vals) {
                            try{
                            ints.add(Integer.valueOf(val));}
                            catch (Exception e)
                            
                            {   }
                            
                    }
                }
		return ints;
	}

	public char getSymbolField(String section, String key) {
		String value = getKey(section, key);
                if (value.length()==0)
                {throw new WIFNoValueException("Value '" + key + "' is empty"); }

		// a symbol can be a bare character, a character surrounded by single
		// quotes, or a #CODE. So these are equivalent: a, 'a', #97
		if (value.length() == 1) {
			return value.charAt(0);
		} else if (value.matches("\\'.\\'")) {
			return value.charAt(1);
		} else if (value.matches("\\#\\d+")) {
			return (char) Integer.parseInt(value.substring(1));
		} else {
			throw new WIFException("Value '" + value + "' is not a symbol");
		}
	}
	
	public int countEntriesInSection(String section) {
            try{
		return wif.get(section.toLowerCase()).size();
            }
            catch (Exception e){
                    return 0;}
            
	}

	public java.awt.Color getColorField(String section, String key, Integer min, Integer max) {
        List <Integer> rgb;
            try {
             rgb = getIntListField(section, key);
        }
        catch(WIFNoValueException e){
            throw new WIFException("Value is not a color");
        }
        
        
        if (rgb.size() != 3) {
            throw new WIFException("Value '"+ rgb + "' is not a color");
        }
        		
        return new java.awt.Color(
        		normalizeColor(rgb.get(0), min, max), 
        		normalizeColor(rgb.get(1), min, max),
        		normalizeColor(rgb.get(2), min, max));
    }

 
	public void setBooleanField(String section, String key, boolean val) {
		setKey(section, key, String.valueOf(val));
	}

	public void setColorField(String section, String key, java.awt.Color val) {
		List<Integer> rgb = Arrays.asList(val.getRed(), val.getGreen(),
				val.getBlue());
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
        public void setIntListFieldOneBased(String section, String key, List<Integer> rgb) {
                Object rgbarray[];
                rgbarray = rgb.toArray();
                for (int i=0;i<rgbarray.length;i++){
                    rgbarray[i] = (int)rgbarray[i]+1;
                }
                Arrays.sort(rgbarray);
		setKey(section, key, StringUtils.join(rgbarray, ','));
	}
        
        
        public void setBoolArrayField(String section, String key, boolean rgb[]) {
            String value="";
            int c;
            for (c=0;c<rgb.length;c++)
                if (rgb[c]){
                    if(""==value){
                        value = Integer.toString(c+1);
                    }
                    else{
                        value += "," + Integer.toString(c+1);
                    }
                }
            setKey(section, key, value);
	}
        
        
	public void setStringField(String section, String key, String val) {
		setKey(section, key, val);
	}

	public void setSymbolField(String section, String key, char val) {
		setKey(section, key, "#" + (int) val);
	}

	private Ini normalizeWif(Ini ini) {
		// make all section and field names lower case
		Ini normIni = new Ini();
		for (Map.Entry<String, Ini.Section> e : ini.entrySet()) {
			Ini.Section normSec = normIni.add(e.getKey().toLowerCase());
			for (Map.Entry<String, String> e2 : e.getValue().entrySet()) {
				normSec.put(e2.getKey().toLowerCase(), e2.getValue());
			}
		}
		return normIni;
	}
	
	private int normalizeColor(Integer c, Integer min, Integer max)
	{
		// Normalise a colour entry in range [min, max] to the
		// range [0, 255].
		return (c - min) * 255 / max;
	}

        public void WriteWif(OutputStream OutStream) throws IOException{
            
            wif.store(OutStream);
        
        }
}
