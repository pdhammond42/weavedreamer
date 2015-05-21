
package com.jenkins.weavingsimulator.datatypes;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.jenkins.wifio.WIFException;
import com.jenkins.wifio.WIFFile;

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
        
        if (wif.getBooleanField("CONTENTS", "WARP", false)) {
        	List<WarpEnd> ends = readWarp(wif, palette);
        	draft.setEnds(ends);
        }
        
        if (wif.getBooleanField("CONTENTS", "TIEUP", false)) {
        	List<Treadle> treadles = readTieup(wif);        
        	draft.setTreadles(treadles);
        }
        
        if(wif.getBooleanField("CONTENTS", "LIFTPLAN", false)) {
        	List<WeftPick> picks = readLiftplan(wif, palette, draft.getNumHarnesses());
        	draft.setIsLiftplan(true);
        	draft.setPicks(picks);
        } else if (wif.getBooleanField("CONTENTS", "WEFT", false)) {
        	List<WeftPick> picks = readWeft(wif, palette, draft.getTreadles().size());        
        	draft.setPicks(picks);
        }
        
        return draft;
    }
    
    public void writeWeavingDraft(WeavingDraft draft, OutputStream outs) {
       throw new UnsupportedOperationException("writeWeavingDraft unsupported");
    }

    private List<Color> readPalette(WIFFile wif) throws RuntimeException {
    	ArrayList<Color> palette = new ArrayList<Color>(0);
    	if (wif.getBooleanField("CONTENTS", "COLOR PALETTE", false)) {
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
    	else
    	{
    		// In the absence of a palette use a simple black and white.
    		palette.add(Color.BLACK);
    		palette.add(Color.WHITE);
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

    private List<WeftPick> readWeft(WIFFile wif, List<Color> palette, int treadles) throws RuntimeException {
        int numPicks = wif.getIntField("WEFT", "Threads");
        List<WeftPick> picks = new ArrayList<WeftPick>(numPicks);
        for (int i = 0; i < numPicks; i++) {
        	String pickId = String.valueOf(i + 1);
        	try {
        		int treadleId = wif.getIntField("TREADLING", pickId) - 1;
        		Color color = readWeftColor(wif, palette, pickId);
        		picks.add(new WeftPick(color, treadles, treadleId));
        	} catch (WIFException e) {
        		break;
        	}
        }

        return picks;
    }
    
    private List<WeftPick> readLiftplan(WIFFile wif, List<Color> palette, int harnesses) {
    	int numPicks = wif.countEntriesInSection("LIFTPLAN");
    	List<WeftPick> picks = new ArrayList<WeftPick>(numPicks);

        for (int i = 0; i < numPicks; i++) {
            String pickId = String.valueOf(i + 1);
            WeftPick pick = new WeftPick(readWeftColor(wif, palette, pickId), harnesses);
            for (int hid : wif.getIntListField("LIFTPLAN", pickId)) {
                // need to convert harness ids to 0 based
                pick.setTreadle(hid - 1, true);
            }
            picks.add(pick);
        }
    		
    	return picks;
    }
    
    private Color readWeftColor(WIFFile wif, List<Color> palette, String pickId)  throws RuntimeException {
		Color color = Color.white; 
		if (wif.hasField("WEFT COLORS", pickId)) {
			int colorIdx = wif.getIntField("WEFT COLORS", pickId) - 1;
			color = palette.get(colorIdx);
		}
		else if (wif.hasField("WEFT", "COLOR")) {
			int colorIdx = wif.getIntField("WEFT", "COLOR") - 1;
			color = palette.get(colorIdx);            
		}
		return color;
    }
}
