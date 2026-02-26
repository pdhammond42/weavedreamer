package com.jenkins.weavedreamer.models;

/**
 * AbstractApp is the interface to application-side state provided
 * by the application class.
 */
public interface AbstractApp {
    PasteGrid getSelectedCells();
    void setSelectedCells(PasteGrid selectedCells);
}
