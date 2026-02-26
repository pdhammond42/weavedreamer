package com.jenkins.weavedreamer.models;

public class FakeApp implements AbstractApp{
    private PasteGrid grid = new PasteGrid();
    @Override
    public PasteGrid getSelectedCells() {
        return grid;
    }

    @Override
    public void setSelectedCells(PasteGrid selectedCells) {
        this.grid = selectedCells;
    }
}
