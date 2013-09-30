package com.jenkins.weavingsimulator.datatypes;

import junit.framework.*;

import com.jenkins.wifio.WIFException;
import com.jenkins.wifio.WIFFile;
import com.jenkins.weavingsimulator.datatypes.*;

import java.awt.Color;
import java.io.*;

public class WifIOTest extends TestCase {
	String minimal = "[WIF]\n" +
	    "Version=1.1\n"+                 
	    "Date=April 20, 1997\n"+         
	    "Developers=wif@mhsoft.com\n"+   
	    "Source Program=WeavingSImulator test\n"+
	    "[CONTENTS]\n";

    // A minimal WIF file as specified should not raise an exception.
    public void testMinimalWif () throws IOException {
    	WIFIO io = new WIFIO();
    	WeavingDraft draft = io.readWeavingDraft(new StringReader(minimal));
    };
    
    // A minimal usable WIF file can be read. Specifically,
    // the standard does not require WARP COLORS, WEFT COLORS or COLOR PALETE
    // entries.
    public void testMinimalTwillWif() throws IOException {
    	String twill = minimal + 
    			"Weaving=yes\n" +
    			"Tieup=yes\n"+
    			"Threading=yes\n"+
    			"Treadling=yes\n"+
    			"Warp=yes\n"+
    			"Weft=yes\n"+
    			"[WEAVING]\n"+
    			"Shafts=2\n"+
    			"Treadles=2\n"+
    			"[WARP]\n"+
    			"Threads=2\n"+
    			"[WEFT]\n"+
    			"Threads=2\n"+
    			"[THREADING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TREADLING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TIEUP]\n"+
    			"1=2\n"+
    			"2=1\n";
    	WIFIO io = new WIFIO();
    	WeavingDraft draft = io.readWeavingDraft(new StringReader(twill));
    	Assert.assertEquals(draft.getPicks().size(), 2);
    	Assert.assertEquals(draft.getPicks().get(0).getColor(), Color.white);
    	Assert.assertEquals(draft.getEnds().get(0).getColor(), Color.black);
    }
    
    public void testColorCanBeReadFromWarpAndWeft () throws IOException {
    	String twill = minimal + 
    			"Color palette=yes\n"+
    			"Color Table=yes\n"+
    			"Weaving=yes\n" +
    			"Tieup=yes\n"+
    			"Threading=yes\n"+
    			"Treadling=yes\n"+
    			"Warp=yes\n"+
    			"Weft=yes\n"+
    			"[COLOR PALETTE]\n"+
    			"Range=255\n"+
    			"Entries=2\n"+
    			"[COLOR TABLE]\n"+
    			"1=255,0,0\n"+
    			"2=0,255,0\n"+
    			"[WEAVING]\n"+
    			"Shafts=2\n"+
    			"Treadles=2\n"+
    			"[WARP]\n"+
    			"Threads=2\n"+
    			"Color=1\n"+
    			"[WEFT]\n"+
    			"Threads=2\n"+
    			"Color=2\n"+
    			"[THREADING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TREADLING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TIEUP]\n"+
    			"1=2\n"+
    			"2=1\n";
    	WIFIO io = new WIFIO();
    	WeavingDraft draft = io.readWeavingDraft(new StringReader(twill));
    	Assert.assertEquals(2, draft.getPicks().size());
    	Assert.assertEquals(Color.green, draft.getPicks().get(0).getColor());
    	Assert.assertEquals(Color.red, draft.getEnds().get(0).getColor());
    }
    
    public void testColorPaletteButNoTableIsError () throws IOException {
    	String twill = minimal + 
    			"Color palette=yes\n"+
    			"Weaving=yes\n" +
    			"Tieup=yes\n"+
    			"Threading=yes\n"+
    			"Treadling=yes\n"+
    			"Warp=yes\n"+
    			"Weft=yes\n"+
    			"[COLOR PALETTE]\n"+
    			"Range=255\n"+
    			"Entries=2\n"+
    			"[COLOR TABLE]\n"+
    			"1=255,0,0\n"+
    			"2=0,255,0\n"+
    			"[WEAVING]\n"+
    			"Shafts=2\n"+
    			"Treadles=2\n"+
    			"[WARP]\n"+
    			"Threads=2\n"+
    			"Color=1\n"+
    			"[WEFT]\n"+
    			"Threads=2\n"+
    			"Color=2\n"+
    			"[THREADING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TREADLING]\n"+
    			"1=1\n"+
    			"2=2\n"+
    			"[TIEUP]\n"+
    			"1=2\n"+
    			"2=1\n";
    	WIFIO io = new WIFIO();
    	try
    	{
    		io.readWeavingDraft(new StringReader(twill));
    		Assert.assertTrue(false);
    	} catch (WIFException e) {
    	
    	}

    }
};