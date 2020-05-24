package com.jenkins.weavedreamer.datatypes;

import com.jenkins.weavingsimulator.datatypes.*;
import com.jenkins.wifio.WIFException;
import com.jenkins.wifio.WIFFile;
import com.jenkins.wifio.WIFNoValueException;
import com.jenkins.wifio.support.ColorTableList;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implements reading and writing WeavingDrafts in WIF format
 *
 * @author ajenkins
 */
public class WIFIO {

    private WIFFile wif;
    private WeavingDraft draft;
    private ColorTableList outpalette;

    public WeavingDraft readWeavingDraft(InputStream ins) throws IOException {
        this.wif = new WIFFile(ins);
        return readWeavingDraft();
    }

    public WeavingDraft readWeavingDraft(Reader reader) throws IOException {
        this.wif = new WIFFile(reader);
        return readWeavingDraft();
    }

    public WeavingDraft readWeavingDraft() {
        draft = new WeavingDraft("New Draft");

        if (wif.hasField("WEAVING", "Shafts")) {
            draft.setNumHarnesses(wif.getIntField("WEAVING", "Shafts"));
        }

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

        if (wif.getBooleanField("CONTENTS", "LIFTPLAN", false)) {
            List<WeftPick> picks = readWeft(wif, palette, draft.getNumHarnesses(), "LIFTPLAN");
            draft.setIsLiftplan(true);
            draft.setPicks(picks);
        } else if (wif.getBooleanField("CONTENTS", "WEFT", false)) {
            List<WeftPick> picks = readWeft(wif, palette, draft.getTreadles().size(), "TREADLING");
            draft.setPicks(picks);
        }

        return draft;
    }

    public void writeWeavingDraft(WeavingDraft draft, OutputStream outs) throws IOException {
        outpalette = new ColorTableList();
        wif = new WIFFile();
        this.draft = draft;

        if (draft != null) {
            writeWIFheader();
            writeWeavingInfo();
            getDraftPalette();
            writeTieup();
            writeWarp();
            writeWeft();
            writeColorTable();

            wif.WriteWif(outs);
        }

    }

    private List<Color> readPalette(WIFFile wif) throws RuntimeException {
        ArrayList<Color> palette = new ArrayList<Color>(0);
        if (wif.getBooleanField("CONTENTS", "COLOR PALETTE", false)) {
            if (!wif.hasField("CONTENTS", "COLOR TABLE")) {
                throw new WIFException("A WIF with a COLOR PALETTE must also have a COLOR TABLE");
            }
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
        } else {
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
            try {
                for (int hid : wif.getIntListField("TIEUP", treadleId)) {
                    // need to convert harness ids to 0 based
                    treadle.add(hid - 1);
                }
            } catch (WIFNoValueException e) {
            }
            treadles.add(treadle);
        }

        return treadles;
    }

    private List<WarpEnd> readWarp(WIFFile wif, List<Color> palette) throws RuntimeException {
        int numEnds;
        int numThreadingThreads = wif.countEntriesInSection("THREADING");
        int numWarpThreads = wif.getIntField("WARP", "Threads");

        // shoud do somehting to pick the bigger one and leave blank threads 
        /* Changed to agree with standard which allows for sparse data in the WIF File

         */
        //numEnds = numThreadingThreads;
        numEnds = numWarpThreads;

        List<WarpEnd> ends = new ArrayList<WarpEnd>(numEnds);
        int harnessId = -1;
        String endId = "";
        for (int i = 0; i < numEnds; i++) {
            // the wif file uses 1 based indices
            endId = String.valueOf(i + 1);
            try {
                harnessId = wif.getIntField("THREADING", endId) - 1;
            } catch (WIFNoValueException e) {
                harnessId = -1;
            }

            ends.add(new WarpEnd(readWarpColor(wif, palette, endId), harnessId));

        }

        return ends;
    }

    private List<WeftPick> readWeft(WIFFile wif, List<Color> palette, int treadles, String WeftDataSection) {
        int numPicks;

        int numWeftElements = wif.countEntriesInSection(WeftDataSection);

        int numWeftThreads = wif.getIntField("WEFT", "Threads");

        // should check to see if there is a discrpancy here 
        /* Changed to agree with standard which allows for sparse data in the WIF File

         */
        //numPicks = numWeftElements;
        numPicks = numWeftThreads;
        List<WeftPick> picks = new ArrayList<WeftPick>(numPicks);

        for (int i = 0; i < numPicks; i++) {
            String pickId = String.valueOf(i + 1);
            WeftPick pick = new WeftPick(readWeftColor(wif, palette, pickId), treadles);
            try {
                for (int hid : wif.getIntListField(WeftDataSection, pickId)) {
                    // need to convert harness ids to 0 based
                    pick.setTreadle(hid - 1, true);
                }
            } catch (WIFNoValueException e) {
            }

            picks.add(pick);
        }

        return picks;
    }

    private Color readWeftColor(WIFFile wif, List<Color> palette, String pickId) throws RuntimeException {

        Color color = Color.BLACK;

        if (wif.hasField("WEFT", "COLOR")) {
            try {
                int colorIdx = wif.getIntListField("WEFT", "COLOR").get(0) - 1;
                color = palette.get(colorIdx);
            } catch (WIFNoValueException | IndexOutOfBoundsException e) {
                // leave it at white
            }
        }

        if (wif.hasField("WEFT COLORS", pickId)) {
            try {
                int colorIdx = wif.getIntListField("WEFT COLORS", pickId).get(0) - 1;
                color = palette.get(colorIdx);
            } catch (WIFNoValueException | IndexOutOfBoundsException e) {
                //default to the one read from WEFT               
            }
        }

        return color;
    }

    private Color readWarpColor(WIFFile wif, List<Color> palette, String EndId) throws RuntimeException {

        Color color = Color.WHITE;

        if (wif.hasField("WARP", "COLOR")) {
            try {
                int colorIdx = wif.getIntListField("WARP", "COLOR").get(0) - 1;
                color = palette.get(colorIdx);
            } catch (WIFNoValueException | IndexOutOfBoundsException e) {
                // leave it at white
            }
        }

        if (wif.hasField("WARP COLORS", EndId)) {
            try {
                int colorIdx = wif.getIntListField("WARP COLORS", EndId).get(0) - 1;
                color = palette.get(colorIdx);
            } catch (WIFNoValueException | IndexOutOfBoundsException e) {
                //default to the one read from WEFT               
            }
        }

        return color;
    }

    private void writeWIFheader() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String versionString;

        versionString = this.getClass().getPackage().getImplementationVersion();
        if (versionString == null) {
            versionString = "Unknown";

        }

        wif.setStringField("WIF", "Version", "1.1");
        wif.setStringField("WIF", "Date", dtf.format(now));
        wif.setStringField("WIF", "Developers", "1.1");
        wif.setStringField("WIF", "Source Program", "WeaveDreamer");
        wif.setStringField("WIF", "Source Version", versionString);
    }

    private void writeTieup() {
        Treadle ittreadle;
        int tcounter = 0;
        String Tstr;
        Iterator<Treadle> treadleiterator = draft.getTreadles().listIterator();

        //wif.setBooleanField("CONTENTS", "TIEUP", true);
        while (treadleiterator.hasNext()) {
            Tstr = "";
            ittreadle = treadleiterator.next();
            tcounter++;
            wif.setIntListFieldOneBased("TIEUP", Integer.toString(tcounter), ittreadle);
        }

    }

    private void writeWeavingInfo() {
        //wif.put("CONTENTS", "WEAVING", "true");
        wif.setIntField("WEAVING", "Shafts", draft.getNumHarnesses());
        wif.setIntField("WEAVING", "Treadles", draft.getTreadles().size());
        wif.setBooleanField("Weaving", "Rising Shed", true);
    }

    private void writeColorTable() {
        Color pcolour;
        int pcounter = 0;
        Iterator<Color> palletteIterator = outpalette.colortable.listIterator();
        while (palletteIterator.hasNext()) {
            pcounter++;
            pcolour = palletteIterator.next();
            wif.setColorField("COLOR TABLE", Integer.toString(pcounter), pcolour);
            //pexport.exportitems.add(String.format("%d=%d,%d,%d", pcounter, pcolour.getRed(), pcolour.getGreen(), pcolour.getBlue()));
        }

        wif.setStringField("COLOR PALETTE", "Range", "0,255");
        wif.setIntField("COLOR PALETTE", "Entries", pcounter);

    }

    private void getDraftPalette() {
        List<Color> cols;
        if (draft.getPalette() != null) {
            cols = draft.getPalette().getColors();
            for (int i = 0; i < cols.size(); i++) {
                outpalette.add((cols.get(i)));
            }
        }
    }

    private void writeWarp() {

        List EndList = draft.getEnds();
        int endcounter = 1;
        WarpEnd we;
        wif.setIntField("WARP", "Threads", draft.getEnds().size());
        wif.setBooleanField("WARP", "Palette", true);

        Iterator<WarpEnd> WarpEndIterator = EndList.iterator();
        while (WarpEndIterator.hasNext()) {
            we = WarpEndIterator.next();
            if (we.getHarnessId() != -1) {
                wif.setIntField("THREADING", Integer.toString(endcounter), (we.getHarnessId() + 1));
            } else {
                wif.setStringField("THREADING", Integer.toString(endcounter), "");
            }
            wif.setIntField("WARP COLORS", Integer.toString(endcounter), outpalette.getindex(we.getColor()));
            endcounter++;
        }
    }

    private void writeTreadles() {

        List PickList = draft.getPicks();
        WeftPick wp;
        Color pickcol;
        String Tstr = "";
        int pickcounter = 0;
        Iterator<WeftPick> PickIterator = PickList.iterator();
        while (PickIterator.hasNext()) {
            wp = PickIterator.next();
            pickcounter++;
            wif.setIntField("WEFT COLORS", Integer.toString(pickcounter), outpalette.getindex(wp.getColor()));
            for (int i = 0; i < wp.getTreadles().length; i++) {
                if (wp.isTreadleSelected(i)) {
                    wif.setIntField("TREADLING", Integer.toString(pickcounter), i + 1);
                }
            }
        }
    }

    private void writeWeft() {
        List PickList = draft.getPicks();
        WeftPick wp;
        String lifttype;
        int pickcounter = 0;

        wif.setIntField("WEFT", "Threads", draft.getPicks().size());
        wif.setBooleanField("WEFT", "Palette", true);

        if (draft.getIsLiftplan()) {
            lifttype = "LIFTPLAN";
        } else {
            lifttype = "TREADLING";
        }
        Iterator<WeftPick> PickIterator = PickList.iterator();
        while (PickIterator.hasNext()) {
            wp = PickIterator.next();
            pickcounter++;
            wif.setBoolArrayField(lifttype, Integer.toString(pickcounter), wp.getTreadles());
            wif.setIntField("WEFT COLORS", Integer.toString(pickcounter), outpalette.getindex(wp.getColor()));
        }

    }

}
