package com.jenkins.weavedreamer.models;

import com.jenkins.weavingsimulator.datatypes.Palette;
import junit.framework.TestCase;

import java.awt.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PaletteColorChangeCommandTest extends TestCase {

    public void testColorCanBeSet() {
        Palette p = new Palette(3);
        p.setSelection(1);
        Command c = new PaletteColorChangeCommand(p, Color.BLUE);
        c.execute();
        assertThat(p.getColor(0), equalTo(Palette.DEFAULT_COLOR));
        assertThat(p.getColor(1), equalTo(Color.BLUE));
        assertThat(p.getColor(2), equalTo(Palette.DEFAULT_COLOR));
    }

    public void testColorCanBeReSet() {
        Palette p = new Palette(3);
        p.setSelection(1);
        p.setColor(1, Color.RED);

        Command c = new PaletteColorChangeCommand(p, Color.BLUE);
        c.execute();
        c.undo();

        assertThat(p.getColor(1), equalTo(Color.RED));
    }
}
