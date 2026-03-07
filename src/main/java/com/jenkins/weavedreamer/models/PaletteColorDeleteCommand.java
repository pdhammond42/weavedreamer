package com.jenkins.weavedreamer.models;

import com.jenkins.weavingsimulator.datatypes.Palette;

import java.awt.*;

public class PaletteColorDeleteCommand implements Command {
    int selection;
    Color colour;
    Palette palette;
    public PaletteColorDeleteCommand(Palette p, int cell) {
        palette = p;
        selection = cell;
        colour = p.getColor(selection);
    }

    @Override
    public void execute() {
        palette.remove(selection);
    }

    @Override
    public void undo() {
        palette.insert(selection, colour);
    }
}
