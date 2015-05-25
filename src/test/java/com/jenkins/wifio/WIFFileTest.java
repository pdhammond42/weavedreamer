/*
 * WIFFileTest.java
 * JUnit based test
 *
 * Created on February 5, 2006, 3:16 PM
 */

package com.jenkins.wifio;

import java.awt.Color;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import junit.framework.*;

/**
 *
 * @author ajenkins
 */
public class WIFFileTest extends TestCase {

    private WIFFile wif = null;
    private InputStream wifStream = null;

    // A valid WIF section for use in test cases
    private static final String WIF_SECTION = "[WIF]\n" + "Version=1.1 ; the version\n" + "Date=April 20, 1997  ; the date\n" + "Developers=adam@thejenkins.org ; an email address\n" + "Source Program=Weaving Simulator\n";

    private String PRIVSECTION = "PRIVATE WEAVESIM TEST";

    public WIFFileTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        wifStream = getClass().getResourceAsStream("sample.wif");
        wif = new WIFFile(wifStream);
    }

    protected void tearDown() throws Exception {
        if (wifStream != null) {
            wifStream.close();
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(WIFFileTest.class);

        return suite;
    }

    /** Used by other test methods to assert that the WIFFile will throw a
     * WIFException given contents, which is a WIF file contents with some
     * sort of format error.
     */
    private void assertBadFormat(String contents) throws Exception {
        boolean threw = false;
        try {
            wif = new WIFFile(new StringReader(contents));
        } catch (WIFException e) {
            threw = true;
        }
        assertTrue(threw);
    }

    private void assertGoodFormat(String contents) throws Exception {
	wif = new WIFFile(new StringReader(contents));
    }

    public void testReadWithMissingWIFSection() throws Exception {
        String contents = "; a comment\n" + "\n" + "[CONTENTS]\n" + "COLOR PALETTE=false\n";

        assertBadFormat(contents);
    }

    public void testReadWithMissingContentsSection() throws Exception {
        String contents = WIF_SECTION;
        assertBadFormat(contents);
    }

    public void testSectionCanbeExcluded() throws Exception {
        String contents = WIF_SECTION + "[CONTENTS]\n" + "COLOR PALETTE=no\n";
        assertGoodFormat(contents);
    }

    public void testGetStringField() throws Exception {
        assertEquals("Handweaving.net Pattern Digitizer", wif.getStringField("WIF", "Source Program"));
    }

    public void testGetTrueBooleanField() throws Exception {
        assertEquals("true", true, wif.getBooleanField(PRIVSECTION, "booltrue"));
        assertEquals("on", true, wif.getBooleanField(PRIVSECTION, "boolon"));
        assertEquals("yes", true, wif.getBooleanField(PRIVSECTION, "boolyes"));
        assertEquals("1", true, wif.getBooleanField(PRIVSECTION, "bool1"));
    }

    public void testGetFalseBooleanField() throws Exception {
        assertEquals("false", false, wif.getBooleanField(PRIVSECTION, "boolfalse"));
        assertEquals("off", false, wif.getBooleanField(PRIVSECTION, "booloff"));
        assertEquals("no", false, wif.getBooleanField(PRIVSECTION, "boolno"));
        assertEquals("0", false, wif.getBooleanField(PRIVSECTION, "bool0"));
    }

    public void testGetBooleanWithComment() throws Exception {
        assertEquals(false, wif.getBooleanField(PRIVSECTION, "boolcomment"));
    }

    public void testGetRealNumberField() throws Exception {
        assertEquals(1.1, wif.getDoubleField("WIF", "Version"));
    }

    public void testGetRealNumberWtihComment() throws Exception {
        assertEquals(4.2, wif.getDoubleField(PRIVSECTION, "realcomment"));
    }

    public void testGetIntField() throws Exception {
        assertEquals(4, wif.getIntField("COLOR PALETTE", "Entries"));
    }

    public void testGetIntList() throws Exception {
        List<Integer> expected = Arrays.asList(16, 14, 10, 8, 2);
        assertEquals(expected, wif.getIntListField("TIEUP", "16"));
    }

    public void testGetSymbol() throws Exception {
        char expected = 'a';
        assertEquals(expected, wif.getSymbolField(PRIVSECTION, "symbolcode"));
        assertEquals(expected, wif.getSymbolField(PRIVSECTION, "symbolchar"));
        assertEquals(expected, wif.getSymbolField(PRIVSECTION, "symbolquotedchar"));
    }
    
    public void testGetColor() throws Exception {
        Color expected = new Color(160, 9, 16);
        assertEquals(expected, wif.getColorField("COLOR TABLE", "1", 0, 255));
    }
    
    public void testCaseInsensitive() throws Exception {
        Color expected = new Color(160, 9, 16);
        // test with section name different case
        assertEquals(expected, wif.getColorField("color table", "1", 0, 255));
        
        // test with property name different case
        assertEquals(4, wif.getIntField("COLOR PALETTE", "ENTRIES"));
    }
}
