
package com.jenkins.weavingsimulator.datatypes;

import com.jenkins.wifio.WIFException;
import com.jenkins.wifio.WIFFile;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/** Implements reading and writing WeavingDrafts in WIF format
 *
 * @author ajenkins
 */
public class WIFIO {    
    public WeavingDraft readWeavingDraft(InputStream ins) throws IOException
    {
        return readWeavingDraft(new WIFFile(ins));
    }
    
    public WeavingDraft readWeavingDraft(Reader reader) throws IOException
    {
        return readWeavingDraft(new WIFFile(reader));
    }

    public WeavingDraft readWeavingDraft(WIFFile wif) throws IOException {
        WeavingDraft draft = new WeavingDraft("New Draft");
        
        if (wif.hasField("WEAVING", "Shafts"))
        	draft.setNumHarnesses(wif.getIntField("WEAVING", "Shafts"));
        
        List<Color> palette = readPalette(wif);
        draft.setPalette(new Palette(palette, ""));
        
        if (wif.hasField("CONTENTS", "WARP")) {
        	List<WarpEnd> ends = readWarp(wif, palette);
        	draft.setEnds(ends);
        }
        
        if (wif.hasField("CONTENTS", "TIEUP")) {
        	List<Treadle> treadles = readTieup(wif);        
        	draft.setTreadles(treadles);
        }
        
        if(wif.hasField("CONTENTS", "LIFTPLAN")) {
        	List<Treadle> treadles = readLiftplan(wif);
        	draft.setTreadles(treadles);
        }
        
        if (wif.hasField("CONTENTS", "WEFT")) {
        	List<WeftPick> picks = readWeft(wif, palette);        
        	draft.setPicks(picks);
        }
        
        return draft;
    }
    
    public void writeWeavingDraft(WeavingDraft draft, OutputStream outs) {
       throw new UnsupportedOperationException("writeWeavingDraft unsupported");
    }

    private List<Color> readPalette(WIFFile wif) throws RuntimeException {
    	ArrayList<Color> palette = new ArrayList<Color>(0);
    	if (wif.hasField("CONTENTS", "COLOR PALETTE") && wif.getBooleanField("CONTENTS", "COLOR PALETTE")) {
    		if (!wif.hasField("CONTENTS", "COLOR TABLE"))
    			throw new WIFException("A WIF with a COLOR PALETTE must also have a COLOR TABLE");
    		int numColors = wif.getIntField("COLOR PALETTE", "Entries");
    		List<Integer> colorRange = wif.getIntListField("COLOR PALETTE", "Range");
    		palette.ensureCapacity(numColors);
    		try {
    			for (int i = 0; i < numColors; i++) {
    				palette.add(wif.getColorField("COLOR TABLE", String.valueOf(i + 1), colorRange.get(0), colorRange.get(1)));
    			}
    		} catch (WIFException e) {
    			// Be tolerant. The catch is a bit of a blunt instrument but we are assuming
    			// that COLOR PALETTE.ENTRIES doesn't match the actual COLOR TABLE.
    		}
    	}

        return palette;
    }

    private List<Treadle> readTieup(WIFFile wif) {
        int numTreadles = wif.getIntField("WEAVING", "Treadles");
        List<Treadle> treadles = new ArrayList<Treadle>(numTreadles);
        for (int i = 0; i < numTreadles; i++) {
            String treadleId = String.valueOf(i + 1);
            Treadle treadle = new Treadle();
            for (int hid : wif.getIntListField("TIEUP", treadleId)) {
                // need to convert harness ids to 0 based
                treadle.add(hid - 1);
            }
            treadles.add(treadle);
        }

        return treadles;
    }

    private List<Treadle> readLiftplan(WIFFile wif) {
    	int numTreadles = wif.countEntriesInSection("LIFTPLAN");
    	List<Treadle> treadles =new ArrayList<Treadle>(numTreadles);

        for (int i = 0; i < numTreadles; i++) {
            String treadleId = String.valueOf(i + 1);
            Treadle treadle = new Treadle();
            for (int hid : wif.getIntListField("LIFTPLAN", treadleId)) {
                // need to convert harness ids to 0 based
                treadle.add(hid - 1);
            }
            treadles.add(treadle);
        }
    		
    	return treadles;
    }
    
    private List<WarpEnd> readWarp(WIFFile wif, List<Color> palette) throws RuntimeException {

        // read warp
        int numEnds = wif.getIntField("WARP", "Threads");
        List<WarpEnd> ends = new ArrayList<WarpEnd>(numEnds);
        for (int i = 0; i < numEnds; i++) {
            // the wif file uses 1 based indices
            String endId = String.valueOf(i + 1);
            try {
            	int harnessId = wif.getIntField("THREADING", endId) - 1;
            	Color color = Color.black; 
            	if (wif.hasField("WARP COLORS", endId)) {
            		int colorIdx = wif.getIntField("WARP COLORS", endId) - 1;
            		color = palette.get(colorIdx);
            	}
            	else if (wif.hasField("WARP", "COLOR")) {
            		int colorIdx = wif.getIntField("WARP", "COLOR") - 1;
            		color = palette.get(colorIdx);
            	}
            	ends.add(new WarpEnd(color, harnessId));
            } catch (WIFException e) {
            	break;
            }
        }

        return ends;
    }

    private List<WeftPick> readWeft(WIFFile wif, List<Color> palette) throws RuntimeException {
    	boolean liftplan = wif.hasField("CONTENTS", "LIFTPLAN");
        int numPicks = wif.getIntField("WEFT", "Threads");
        List<WeftPick> picks = new ArrayList<WeftPick>(numPicks);
        for (int i = 0; i < numPicks; i++) {
        	String pickId = String.valueOf(i + 1);
        	try {
        		int treadleId = liftplan ? i : wif.getIntField("TREADLING", pickId) - 1;
        		Color color = Color.white; 
        		if (wif.hasField("WEFT COLORS", pickId)) {
        			int colorIdx = wif.getIntField("WEFT COLORS", pickId) - 1;
        			color = palette.get(colorIdx);
        		}
        		else if (wif.hasField("WARP", "COLOR")) {
        			int colorIdx = wif.getIntField("WEFT", "COLOR") - 1;
        			color = palette.get(colorIdx);            
        		}
        		picks.add(new WeftPick(color, treadleId));
        	} catch (WIFException e) {
        		break;
        	}
        }

        return picks;
    }
}
