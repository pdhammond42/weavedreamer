
package com.jenkins.weavingsimulator.datatypes;

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
        
        int numHarnesses = wif.getIntField("WEAVING", "Shafts");
        draft.setNumHarnesses(numHarnesses);
        
        List<Color> palette = readPalette(wif);
        
        List<WarpEnd> ends = readWarp(wif, palette);
        draft.setEnds(ends);
        
        List<Treadle> treadles = readTieup(wif);        
        draft.setTreadles(treadles);
        
        List<WeftPick> picks = readWeft(wif, palette);        
        draft.setPicks(picks);
        
        return draft;
    }
    
    public void writeWeavingDraft(WeavingDraft draft, OutputStream outs) {
       throw new UnsupportedOperationException("writeWeavingDraft unsupported");
    }

    private List<Color> readPalette(WIFFile wif) throws RuntimeException {
        int numColors = wif.getIntField("COLOR PALETTE", "Entries");
        String colorForm = wif.getStringField("COLOR PALETTE", "Form");
        if (!colorForm.equalsIgnoreCase("rgb")) {
            throw new RuntimeException("Don't understand color form '" + colorForm + "'");
        }
        List<Integer> colorRange = wif.getIntListField("COLOR PALETTE", "Range");
        List<Color> palette = new ArrayList<Color>(numColors);

        for (int i = 0; i < numColors; i++) {
            palette.add(wif.getColorField("COLOR TABLE", String.valueOf(i + 1)));
        }

        return palette;
    }

    private List<Treadle> readTieup(WIFFile wif) {

        // read tieup
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
        if (!wif.getBooleanField("WARP", "Palette")) {
            throw new RuntimeException("Don't know how to deal with warp that isn't palette");
        }
        List<WarpEnd> ends = new ArrayList<WarpEnd>(numEnds);
        for (int i = 0; i < numEnds; i++) {
            // the wif file uses 1 based indices
            String endId = String.valueOf(i + 1);
            int harnessId = wif.getIntField("THREADING", endId) - 1;
            int colorIdx = wif.getIntField("WARP COLORS", endId) - 1;
            Color color = palette.get(colorIdx);
            ends.add(new WarpEnd(color, harnessId));
        }

        return ends;
    }

    private List<WeftPick> readWeft(WIFFile wif, List<Color> palette) throws RuntimeException {

        // read weft
        int numPicks = wif.getIntField("WEFT", "Threads");
        if (!wif.getBooleanField("WEFT", "Palette")) {
            throw new RuntimeException("Don't know how to deal with weft that isn't palette");
        }
        List<WeftPick> picks = new ArrayList<WeftPick>(numPicks);
        for (int i = 0; i < numPicks; i++) {
            String pickId = String.valueOf(i + 1);
            int treadleId = wif.getIntField("TREADLING", pickId) - 1;
            int colorIdx = wif.getIntField("WEFT COLORS", pickId) - 1;
            Color color = palette.get(colorIdx);
            picks.add(new WeftPick(color, treadleId));
        }

        return picks;
    }
}
