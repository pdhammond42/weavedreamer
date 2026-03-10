/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.wifio;


import com.jenkins.weavedreamer.datatypes.WIFIO;
import com.jenkins.weavingsimulator.datatypes.Palette;
import com.jenkins.weavingsimulator.datatypes.Treadle;
import com.jenkins.weavingsimulator.datatypes.WarpEnd;

import com.jenkins.weavingsimulator.datatypes.WeavingDraft;
import com.jenkins.weavingsimulator.datatypes.WeftPick;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

/**
 *
 * @author David
 */
public class WifFileWriteTest extends TestCase {

    private WeavingDraft draft = new WeavingDraft();


    public WifFileWriteTest(String testName) {
        super(testName);
    }

    protected void setUp() {

    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(WifFileWriteTest.class);

        return suite;
    }

    public void testWriteheadersTest() throws IOException, ConfigurationException {
        INIConfiguration wif;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba;
        InputStream bais;
        draft = new WeavingDraft();
        WIFIO io = new WIFIO();
        io.writeWeavingDraft(draft, baos);

        bais = new ByteArrayInputStream(baos.toByteArray());

        wif = new INIConfiguration();
        wif.read(new StringReader(baos.toString()));
        assertThat(wif.getString("wif.version"), is("1.1"));
        assertThat(wif.getString("wif.source program"), is("WeaveDreamer"));

        // colour 

    }

    public void testWriteWarp() throws IOException, ConfigurationException {
        INIConfiguration wif;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba;
        InputStream bais;
        draft = new WeavingDraft();
        draft.setNumHarnesses(2);

        draft.getTreadles().add(new Treadle());
        draft.getTreadles().add(new Treadle());

        List<WarpEnd> ends = new LinkedList<WarpEnd>();
        ends.add(new WarpEnd(Color.BLACK, 1));
        ends.add(new WarpEnd(Color.RED, -1));
        ends.add(new WarpEnd(Color.RED, 0));
        draft.setEnds(ends);

        WIFIO io = new WIFIO();
        io.writeWeavingDraft(draft, baos);
        //System.out.println(baos.toString());
        wif = new INIConfiguration();
        wif.read(new StringReader(baos.toString()));

        // headings 
        assertThat(wif.getString("contents.warp"), is("true"));
        assertThat(wif.getString("contents.warp colors"), is("true"));

        // sizes
        assertThat(wif.getString("warp.threads"), is("3"));
        assertThat(wif.getSection("threading").size(), is(3));

        //warp colours         
        String warpPalletIndex;
        warpPalletIndex = wif.getString("warp colors.1");
        assertThat(wif.getString("color table."+ warpPalletIndex), is("0,0,0"));

        warpPalletIndex = wif.getString("warp colors.2");
        assertThat(wif.getString("color table."+warpPalletIndex), is("255,0,0"));
        // threading
        assertThat(wif.getString("threading.1"), is("2"));
        assertThat(wif.getString("threading.2"), is(""));
        assertThat(wif.getString("threading.3"), is("1"));

    }


    public void testColorPalette() throws IOException, ConfigurationException {
        INIConfiguration wif;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba;
        InputStream bais;
        draft = new WeavingDraft();
        draft.setNumHarnesses(2);

        draft.getTreadles().add(new Treadle());
        draft.getTreadles().add(new Treadle());
        Palette newPalette;


        newPalette = new Palette();
        newPalette.setColors(Arrays.asList(Color.WHITE, Color.BLACK, Color.GREEN));

        draft.setPalette(newPalette);

        List<WarpEnd> ends = new LinkedList<>();
        ends.add(new WarpEnd(Color.BLACK, 1)); // dups palette 
        ends.add(new WarpEnd(Color.RED, 0));
        ends.add(new WarpEnd(Color.RED, 0));  // check for non dup color
        ends.add(new WarpEnd(Color.WHITE, 0));  // dups palette; check not duplicated
        ends.add(new WarpEnd(Color.WHITE, 0));
        ends.add(new WarpEnd(Color.BLUE, 0));
        draft.setEnds(ends);

        WIFIO io = new WIFIO();
        io.writeWeavingDraft(draft, baos);

        wif = new INIConfiguration();
        wif.read(new StringReader(baos.toString()));

        // headings 
        assertThat(wif.getString("contents.color palette"), is("true"));
        assertThat(wif.getString("contents.color table"), is("true"));

        // sizes
        assertThat(wif.getSection("color table").size(), is(5));
        assertThat(wif.getString("color palette.entries"), is("5"));
        // expected coloors in there 
        assertThat(wif.getString("color table.1"), anyOf(is("0,0,0")
                , is("255,255,255")
                , is("255,0,0")
                , is("0,0,255")
                , is("0,255,0")));

        assertThat(wif.getString("color table.2"), anyOf(is("0,0,0")
                , is("255,255,255")
                , is("255,0,0")
                , is("0,0,255")
                , is("0,255,0")));

        assertThat(wif.getString("color table.3"), anyOf(is("0,0,0")
                , is("255,255,255")
                , is("255,0,0")
                , is("0,0,255")
                , is("0,255,0")));

        assertThat(wif.getString("color table.4"), anyOf(is("0,0,0")
                , is("255,255,255")
                , is("255,0,0")
                , is("0,0,255")
                , is("0,255,0")));

        assertThat(wif.getString("color table.5"), anyOf(is("0,0,0")
                , is("255,255,255")
                , is("255,0,0")
                , is("0,0,255")
                , is("0,255,0")));


        // palette scalling there
        assertThat(wif.getString("color palette.range"), is("0,255"));
    }


    public void testWriteWeftTreadling() throws IOException, ConfigurationException {
        INIConfiguration wif;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba;
        InputStream bais;
        draft = new WeavingDraft();
        draft.setProperties(3, 4, 5, 2, false, false);
        List<WeftPick> picks = new LinkedList<WeftPick>();
        picks.add(new WeftPick(Color.BLACK, 4, 0));
        picks.add(new WeftPick(Color.RED, 4, 1));
        picks.add(new WeftPick(Color.RED, 4));
        draft.setPicks(picks);


        WIFIO io = new WIFIO();
        io.writeWeavingDraft(draft, baos);

        wif = new INIConfiguration();
        wif.read(new StringReader(baos.toString()));

        // sizes
        assertThat(wif.getString("weaving.shafts"), is("3"));
        assertThat(wif.getString("weaving.treadles"), is("4"));
        // headings
        assertThat(wif.getString("contents.weft"), is("true"));
        assertThat(wif.getString("contents.weft colors"), is("true"));
        assertThat(wif.getString("contents.liftplan"), is(not("true")));
        assertThat(wif.getString("contents.treadling"), is("true"));

        // sizes

        assertThat(wif.getString("weft.threads"), is("3"));
        assertThat(wif.getSection("treadling").size(), is(3));


        // colors
        String weftPalletIndex;
        weftPalletIndex = wif.getString("weft colors.1");
        assertThat(wif.getString("color table."+weftPalletIndex), is("0,0,0"));

        weftPalletIndex = wif.getString("weft colors.2");
        assertThat(wif.getString("color table."+weftPalletIndex), is("255,0,0"));
        // treadling 
        assertThat(wif.getString("treadling.1"), is("1"));
        assertThat(wif.getString("treadling.2"), is("2"));
        assertThat(wif.getString("treadling.3"), is(""));
    }


    public void testWriteWeftLiftPlan() throws IOException, ConfigurationException {
        INIConfiguration wif;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba;
        InputStream bais;
        draft = new WeavingDraft();
        draft.setProperties(4, 4, 5, 2, true, false);
        List<WeftPick> picks = new LinkedList<WeftPick>();
        picks.add(new WeftPick(Color.BLACK, 4, 0));
        picks.add(new WeftPick(Color.RED, 4, 1));
        picks.add(new WeftPick(Color.BLUE, 4, 1, 0));
        picks.add(new WeftPick(Color.BLUE, 4));
        draft.setPicks(picks);


        WIFIO io = new WIFIO();
        io.writeWeavingDraft(draft, baos);

        wif = new INIConfiguration();
        wif.read(new StringReader(baos.toString()));

        // headings
        assertThat(wif.getString("contents.weft"), is("true"));
        assertThat(wif.getString("contents.tieup"), is("true"));
        assertThat(wif.getString("contents.weft colors"), is("true"));
        assertThat(wif.getString("contents.liftplan"), is("true"));
        assertThat(wif.getString("contents.treadling"), is(not("true")));


        assertThat(wif.getString("weft.threads"), is("4"));
        assertThat(wif.getSection("liftplan").size(), is(4));

        // colours        
        String weftPalletIndex;
        weftPalletIndex = wif.getString("weft colors.1");
        assertThat(wif.getString("color table."+ weftPalletIndex), is("0,0,0"));

        weftPalletIndex = wif.getString("weft colors.2");
        assertThat(wif.getString("color table."+ weftPalletIndex), is("255,0,0"));


        // lifts
        assertThat(wif.getString("liftplan.1"), is("1"));
        assertThat(wif.getString("liftplan.2"), is("2"));
        assertThat(wif.getString("liftplan.3"), is("1,2"));
        assertThat(wif.getString("liftplan.4"), is(""));
        // liftplan tieup 
        assertThat(wif.getString("tieup.1"), is("1"));
        assertThat(wif.getString("tieup.2"), is("2"));
        assertThat(wif.getString("tieup.3"), is("3"));
        assertThat(wif.getString("tieup.4"), is("4"));
    }


    public void testWriteTieUp() throws IOException, ConfigurationException {
        INIConfiguration wif;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba;
        InputStream bais;
        draft = new WeavingDraft();
        //draft.setProperties(4, 4, 5, 2, false, false);

        ArrayList<Treadle> newTreadles = new <Treadle>ArrayList();
        newTreadles.add(new Treadle(Arrays.asList(0, 2)));
        newTreadles.add(new Treadle(List.of(1)));
        newTreadles.add(new Treadle(List.of()));
        draft.setTreadles(newTreadles);


        WIFIO io = new WIFIO();
        io.writeWeavingDraft(draft, baos);
        wif = new INIConfiguration();
        wif.read(new StringReader(baos.toString()));


        assertThat(wif.getString("contents.tieup"), is("true"));
        // num loaded
        assertThat(wif.getSection("tieup").size(), is(3));

        assertThat(wif.getString("tieup.1"), is("1,3"));
        assertThat(wif.getString("tieup.2"), is("2"));
        assertThat(wif.getString("tieup.3"), is(""));
    }
}

