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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import org.ini4j.Ini;

/**
 *
 * @author David
 */
public class WifFileWriteTest extends TestCase{
    
    private WeavingDraft draft = new WeavingDraft();
    
    
    public WifFileWriteTest(String testName) {
        super(testName);
    }  
    
       protected void setUp() throws java.lang.Exception {
        
    }

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(WifFileWriteTest.class);
        
        return suite;
    }
    
  public void testWriteheadersTest()throws IOException {
        Ini wif ;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba;
        InputStream bais;
        draft = new WeavingDraft();      
        WIFIO io = new WIFIO(); 
        io.writeWeavingDraft(draft,baos);  
       
        bais= new ByteArrayInputStream(baos.toByteArray());

        wif = new Ini(new StringReader(baos.toString()));
        assertThat(wif.get("wif", "version"),is("1.1"));
        assertThat(wif.get("wif", "source program"),is("WeaveDreamer"));
        
        // colour 
                        
    }   
  
public void testWriteWarp()throws IOException {
        Ini wif ;
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
        ends.add(new WarpEnd(Color.RED,0));
        draft.setEnds(ends);
        
        WIFIO io = new WIFIO(); 
        io.writeWeavingDraft(draft,baos);  
        bais= new ByteArrayInputStream(baos.toByteArray());
        //System.out.println(baos.toString());
        wif = new Ini(new StringReader(baos.toString()));
        
       // headings 
        assertThat(wif.get("contents", "warp"),is("true"));
        assertThat(wif.get("contents", "warp colors"),is("true"));
        
        // sizes
        assertThat(wif.get("warp", "threads"),is("3"));
        assertThat(wif.get("threading").size(),is(3));
        
        //warp colours         
        String warpPalletIndex;
        warpPalletIndex = wif.get("warp colors","1");
        assertThat(wif.get("color table",warpPalletIndex), is("0,0,0")  );

        warpPalletIndex = wif.get("warp colors","2");
        assertThat(wif.get("color table",warpPalletIndex), is("255,0,0")  );   
        // threading
        assertThat(wif.get("threading","1"), is("2"));
        assertThat(wif.get("threading","2"), is(""));
        assertThat(wif.get("threading","3"), is("1"));
                 
    }   

 
  public void testColorPalette()throws IOException {
        Ini wif ;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba;
        InputStream bais;
        draft = new WeavingDraft();   
        draft.setNumHarnesses(2); 
        
        draft.getTreadles().add(new Treadle());
        draft.getTreadles().add(new Treadle());
        Palette newPalette;
                       
        
        newPalette = new Palette();
        newPalette.setColors(Arrays.asList(Color.WHITE,Color.BLACK,Color.GREEN));
        
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
        io.writeWeavingDraft(draft,baos);  
        bais= new ByteArrayInputStream(baos.toByteArray());

        wif = new Ini(new StringReader(baos.toString()));
        
       // headings 
        assertThat(wif.get("contents", "color palette"),is("true"));
        assertThat(wif.get("contents", "color table"),is("true"));
        
        // sizes
        assertThat(wif.get("color table").size(),is(5));
        assertThat(wif.get("color palette","entries"),is("5")); 
        // expected coloors in there 
        assertThat(wif.get("color table","1"),anyOf(is("0,0,0")
                                             ,is("255,255,255")
                                             ,is("255,0,0")
                                             ,is("0,0,255")
                                             ,is("0,255,0")));
        
        assertThat(wif.get("color table","2"),anyOf(is("0,0,0")
                                             ,is("255,255,255")
                                             ,is("255,0,0")
                                             ,is("0,0,255")
                                             ,is("0,255,0")));

        assertThat(wif.get("color table","3"),anyOf(is("0,0,0")
                                             ,is("255,255,255")
                                             ,is("255,0,0")
                                             ,is("0,0,255")
                                             ,is("0,255,0")));

        assertThat(wif.get("color table","4"),anyOf(is("0,0,0")
                                             ,is("255,255,255")
                                             ,is("255,0,0")
                                             ,is("0,0,255")
                                             ,is("0,255,0")));

        assertThat(wif.get("color table","5"),anyOf(is("0,0,0")
                                             ,is("255,255,255")
                                             ,is("255,0,0")
                                             ,is("0,0,255")
                                             ,is("0,255,0")));
        

        
        // palette scalling there
        assertThat(wif.get("color palette","range"),is("0,255"));
        

        

        
        
                
                        
    }   
  
 
     
  public void testWriteWeftTreadling()throws IOException {
        Ini wif ;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba;
        InputStream bais;
        draft = new WeavingDraft();   
        draft.setProperties(3, 4, 5, 2, false, false);
        List<WeftPick> picks = new LinkedList<WeftPick>();
        picks.add(new WeftPick(Color.BLACK,4,0));
        picks.add(new WeftPick(Color.RED,4,1));
        picks.add(new WeftPick(Color.RED,4));
        draft.setPicks(picks);
        
        
        WIFIO io = new WIFIO(); 
        io.writeWeavingDraft(draft,baos);  
        bais= new ByteArrayInputStream(baos.toByteArray());
        // System.out.println(baos.toString());
        wif = new Ini(new StringReader(baos.toString()));
        
        // sizes
        assertThat(wif.get("weaving","shafts"),is("3"));
        assertThat(wif.get("weaving","treadles"),is("4"));
        // headings
        assertThat(wif.get("contents", "weft"),is("true"));
        assertThat(wif.get("contents", "weft colors"),is("true"));
        assertThat(wif.get("contents", "liftplan"),is(not("true")));
        assertThat(wif.get("contents", "treadling"),is("true"));
       
        // sizes
        
        assertThat(wif.get("weft", "threads"),is("3"));
        assertThat(wif.get("treadling").size(),is(3));
        

        // colors
        String weftPalletIndex;
        weftPalletIndex = wif.get("weft colors","1");
        assertThat(wif.get("color table",weftPalletIndex), is("0,0,0")  );

        weftPalletIndex = wif.get("weft colors","2");
        assertThat(wif.get("color table",weftPalletIndex), is("255,0,0")  );   
        // treadling 
        assertThat(wif.get("treadling","1"), is("1"));
        assertThat(wif.get("treadling","2"), is("2"));
        assertThat(wif.get("treadling","3"), is(""));
        
              
                        
    }   


  public void testWriteWeftLiftPlan()throws IOException {
        Ini wif ;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba;
        InputStream bais;
        draft = new WeavingDraft();   
        draft.setProperties(4, 4, 5, 2, true, false);
        List<WeftPick> picks = new LinkedList<WeftPick>();
        picks.add(new WeftPick(Color.BLACK,4,0));
        picks.add(new WeftPick(Color.RED,4,1));
        picks.add(new WeftPick(Color.BLUE,4,1,0));
        picks.add(new WeftPick(Color.BLUE,4));
        draft.setPicks(picks);
        
        
        WIFIO io = new WIFIO(); 
        io.writeWeavingDraft(draft,baos);  
        bais= new ByteArrayInputStream(baos.toByteArray());
        //System.out.println(baos.toString());
        wif = new Ini(new StringReader(baos.toString()));
        
        // headings
        assertThat(wif.get("contents", "weft"),is("true"));
        assertThat(wif.get("contents", "tieup"),is("true"));
        assertThat(wif.get("contents", "weft colors"),is("true"));
        assertThat(wif.get("contents", "liftplan"),is("true"));
        assertThat(wif.get("contents", "treadling"),is(not("true")));
        
        
        
        assertThat(wif.get("weft", "threads"),is("4"));
        assertThat(wif.get("liftplan").size(),is(4));
        
        // colours        
        String weftPalletIndex;
        weftPalletIndex = wif.get("weft colors","1");
        assertThat(wif.get("color table",weftPalletIndex), is("0,0,0")  );

        weftPalletIndex = wif.get("weft colors","2");
        assertThat(wif.get("color table",weftPalletIndex), is("255,0,0")  );   
        
        
        // lifts
        assertThat(wif.get("liftplan","1"), is("1"));
        assertThat(wif.get("liftplan","2"), is("2"));
        assertThat(wif.get("liftplan","3"), is("1,2"));
        assertThat(wif.get("liftplan","4"), is(""));
        // liftplan tieup 
        assertThat(wif.get("tieup","1"), is("1"));
        assertThat(wif.get("tieup","2"), is("2"));
        assertThat(wif.get("tieup","3"), is("3"));
        assertThat(wif.get("tieup","4"), is("4"));
        
       
        
        
              
                        
    }  

  
  
  public void testWriteTieUp()throws IOException {
        Ini wif ;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba;
        InputStream bais;
        draft = new WeavingDraft();   
        //draft.setProperties(4, 4, 5, 2, false, false);

        ArrayList <Treadle> newTreadles = new <Treadle> ArrayList();
        newTreadles.add(new Treadle(Arrays.asList(0, 2)));
        newTreadles.add(new Treadle(Arrays.asList(1)));
        newTreadles.add(new Treadle(Arrays.asList()));
        draft.setTreadles(newTreadles);
        
        
        WIFIO io = new WIFIO(); 
        io.writeWeavingDraft(draft,baos);  
        bais= new ByteArrayInputStream(baos.toByteArray());
        // System.out.println(baos.toString());
        wif = new Ini(new StringReader(baos.toString()));
        
        
  
        assertThat(wif.get("contents", "tieup"),is("true"));
        // num loaded
        assertThat(wif.get("tieup").size(), is(3));
        
        assertThat(wif.get("tieup","1"), is("1,3"));
        assertThat(wif.get("tieup","2"), is("2"));
        assertThat(wif.get("tieup","3"), is(""));
                                 
    } 
}

