package com.jenkins.weavedreamer.models;

import com.jenkins.weavingsimulator.datatypes.Palette;

import java.awt.*;

public class PaletteColorChangeCommand implements Command {
    private final Palette palette;
    private final int index;
    private final Color previousColor;
    private final Color newColor;

    public PaletteColorChangeCommand (Palette palette, Color newColor) {
        this.newColor = newColor;
        this.palette = palette;
        this.index = palette.getSelection();
        this.previousColor = palette.getColor(index);
    }

    @Override
    public void execute() {
        palette.setColor(index, newColor);
    }

    @Override
    public void undo() {
        palette.setColor(index, previousColor);
    }
}


