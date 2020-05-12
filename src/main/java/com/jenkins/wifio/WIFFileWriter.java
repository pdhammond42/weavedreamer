/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.wifio;

import com.jenkins.weavedreamer.models.EditingSession;
import com.jenkins.weavingsimulator.datatypes.Treadle;
import com.jenkins.wifio.support.ColorTableList;
import com.jenkins.weavingsimulator.datatypes.WarpEnd;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import com.jenkins.weavingsimulator.datatypes.WeftPick;

import java.util.Iterator;
import java.util.List;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import org.ini4j.Ini;

/**
 *
 * @author David
 */
public class WIFFileWriter {

    private WeavingDraft draft;
    private ColorTableList palette;
    //private ArrayList<WIFexportsection> sectionlist;
    private OutputStream outstream;
    private WIFFile wif;

    public WIFFileWriter() {
        palette = new ColorTableList();
        wif = new WIFFile();

    }

    public void exportWIF(WeavingDraft draft, OutputStream outs) throws IOException {
        outstream = outs;
        this.draft = draft;
        writeWIF();
    }

    public void exportWIF(WeavingDraft draft) throws IOException {
        outstream = System.out;
        this.draft = draft;
        writeWIF();
    }

    public void writeWIF() throws IOException {
        if (draft != null) {
            writeWIFheader();
            writetWeavingInfo();
            getDrafttPalette();
            writetieup();
            writeWarp();
            writeWeft();
            writeColorTable();

            wif.WriteWif(outstream);
        }
    }

    private void writeWIFheader() throws IOException {

        wif.setStringField("WIF", "Version", "1.1");
        wif.setStringField("WIF", "Version", "1.1");
        wif.setStringField("WIF", "Date", "Jan 1 2020");
        wif.setStringField("WIF", "Davelopers", "1.1");
        wif.setStringField("WIF", "Source Program", "1.1");
        wif.setStringField("WIF", "Source Version", "1.1");
    }

    private void writetieup() {
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

    private void writetWeavingInfo() {
        //wif.put("CONTENTS", "WEAVING", "true");
        wif.setIntField("WEAVING", "Shafts", draft.getNumHarnesses());
        wif.setIntField("WEAVING", "Treadles", draft.getTreadles().size());
    }

    private void writeColorTable() {
        Color pcolour;
        int pcounter = 0;
        Iterator<Color> palletteIterator = palette.colortable.listIterator();
        while (palletteIterator.hasNext()) {
            pcounter++;
            pcolour = palletteIterator.next();
            wif.setColorField("COLOR TABLE", Integer.toString(pcounter), pcolour);
            //pexport.exportitems.add(String.format("%d=%d,%d,%d", pcounter, pcolour.getRed(), pcolour.getGreen(), pcolour.getBlue()));
        }

        wif.setStringField("COLOR PALETTE", "Range", "0,255");
        wif.setIntField("COLOR PALETTE", "Entries", pcounter);

    }

    private void getDrafttPalette() {
        for (int i = 0; i < draft.getPalette().getNumColors(); i++) {
            palette.add(draft.getPalette().getColor(i));
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
            wif.setIntField("THREADING", Integer.toString(endcounter), (we.getHarnessId() + 1));
            wif.setIntField("WARP COLORS", Integer.toString(endcounter), palette.getindex(we.getColor()));
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
            wif.setIntField("WEFT COLORS", Integer.toString(pickcounter), palette.getindex(wp.getColor()));
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

        wif.setIntField("WEFT", "Threads", draft.getPicks().size());
        wif.setBooleanField("WEFT", "Palette", true);

        if (draft.getIsLiftplan()) {
            lifttype = "LIFTPLAN";
        } else {
            lifttype = "TREADLING";
        }
        int pickcounter = 0;

        //wif.put("CONTENTS", lifttype, "true");
        //wif.put("CONTENTS", "WEFT COLORS", "true");
        //System.out.println(PickList.size());
        Iterator<WeftPick> PickIterator = PickList.iterator();
        while (PickIterator.hasNext()) {
            wp = PickIterator.next();
            pickcounter++;
            wif.setBoolArrayField(lifttype, Integer.toString(pickcounter), wp.getTreadles());
            wif.setIntField("WEFT COLORS", Integer.toString(pickcounter), palette.getindex(wp.getColor()));
        }

    }

}
