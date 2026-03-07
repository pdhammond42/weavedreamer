package com.jenkins.weavedreamer.models;

import com.jenkins.weavingsimulator.datatypes.Palette;
import junit.framework.TestCase;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PaletteColorDeleteCommandTest extends TestCase {

    public void testColorCanBeDeleted() {
        Palette p = new Palette(3);
        p.setColor(0, Color.RED);
        p.setColor(1, Color.WHITE);
        p.setColor(2, Color.BLUE);
        p.setSelection(1);
        Command c = new PaletteColorDeleteCommand(p, 1);
        c.execute();
        assertEquals(Arrays.asList(Color.RED, Color.BLUE), p.getColors());
        assertEquals(-1, p.getSelection());
    }

    public void testColorCanBeReSet() {
        Palette p = new Palette(3);
        p.setColor(0, Color.RED);
        p.setColor(1, Color.WHITE);
        p.setColor(2, Color.BLUE);

        Command c = new PaletteColorDeleteCommand(p, 1);
        c.execute();
        c.undo();
        assertEquals(Arrays.asList(Color.RED, Color.WHITE, Color.BLUE), p.getColors());
    }
}
