/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.weavedreamer.print;

import com.jenkins.weavedreamer.WeavingDraftWindow;
import com.jenkins.weavedreamer.models.EditingSession;
import com.jenkins.weavingsimulator.datatypes.WeavingDraft;

import java.awt.print.Printable;

/**
 * @author David
 */
public abstract class AbstractWeaveDreamerPrintable implements Printable {
    protected EditingSession session;
    protected WeavingDraft draft;
    protected WeavingDraftWindow draftWindow;

    public AbstractWeaveDreamerPrintable(EditingSession session, WeavingDraftWindow draftWindow) {
        this.session = session;
        this.draft = session.getDraft();
        this.draftWindow = draftWindow;
    }

}


