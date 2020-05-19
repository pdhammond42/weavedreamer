/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.weavedreamer.print;

import com.jenkins.weavedreamer.WeavingDraftWindow;
import com.jenkins.weavedreamer.models.EditingSession;
import com.jenkins.weavingsimulator.datatypes.WarpEnd;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author David
 */
public class PrintThreadingList extends AbstractWeaveDreamerPrintable {

    private Font headerFont = new Font("Arial", Font.BOLD, 12);

    private Font textFont = new Font("Arial", Font.PLAIN, 8);
    private int rememberedPageIndex = -1;
    private int rememberedThread = 0;
    private int currentThread = 0;
    private boolean rememberedEOF = false;

    public PrintThreadingList(EditingSession session, WeavingDraftWindow draftWindow) {
        super(session, draftWindow);

    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) {

        try {
            // For catching IOException      
            if (pageIndex != rememberedPageIndex) {
                // First time we've visited this page 
                rememberedPageIndex = pageIndex;
                // If encountered EOF on previous page, done  
                if (rememberedEOF) {
                    return Printable.NO_SUCH_PAGE;
                }
                // Save current position in input file 
                rememberedThread = currentThread;
            } else {
                currentThread = rememberedThread;
            }

            g.setColor(Color.black);
            g.setFont(headerFont);

            int x = (int) pf.getImageableX() + 10;
            int y = (int) pf.getImageableY() + 12;
            // Title lineif (currentpick>= draft.getPicks().size()){

            if (currentThread >= draft.getEnds().size()) {
                return Printable.NO_SUCH_PAGE;
            }
            g.drawString("WeaveDreamer: " + draft.getName() + " Threading List, page: " + (pageIndex + 1), x, y);
            g.setFont(textFont);
            // Generate as many lines as will fit in imageable area 
            y += 36;

            while (y + 12 < pf.getImageableY() + pf.getImageableHeight()) {
                if (currentThread < draft.getEnds().size()) {
                    WarpEnd currentend = draft.getEnds().get(currentThread);
                    int numharnesses = draft.getNumHarnesses();
                    String Picklist[] = new String[numharnesses];

                    for (int j = 0; j < numharnesses; j++) {
                        Picklist[j] = (Integer.toString((j + 1) * (currentend.getHarnessId() == j ? 1 : 0)));
                        if (!"0".equals(Picklist[j])) {
                            g.fillRect(x + 15 + 10 * j, y - 10, 10, 10);
                        }

                        g.drawRect(x + 15 + 10 * j, y - 10, 10, 10);

                    }
                    String printString;
                    printString = String.format("%4d = %s ", (currentThread + 1), StringUtils.join(Picklist, ','));

                    g.setColor(currentend.getColor());
                    g.fillRect(x, y - 10, 10, 10);

                    g.setColor(Color.BLACK);
                    g.drawString(printString, x + 15 + (numharnesses + 1) * 10, y);
                    if (currentThread % 10 == 9) {
                        y += 12;
                    }
                    y += 12;
                    currentThread++;
                } else {
                    rememberedEOF = true;
                    break;
                }

            }
            return Printable.PAGE_EXISTS;
        } catch (Exception e) {
            Logger.getLogger(PrintThreadingList.class.getName()).log(Level.SEVERE, null, e);
            return Printable.NO_SUCH_PAGE;
        }
    }
}
