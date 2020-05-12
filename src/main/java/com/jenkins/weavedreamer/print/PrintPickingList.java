/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.weavedreamer.print;

import com.jenkins.weavedreamer.WeavingDraftWindow;
import com.jenkins.weavedreamer.models.EditingSession;
import com.jenkins.weavingsimulator.datatypes.WeftPick;
import java.awt.*;
import java.awt.print.*;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author David
 */
public class PrintPickingList extends AbstractWeaveDreamerPrintable {

    private final int squaresize = 7;
    private final int pickTextSpacing = 13;
    private final Font headerFont = new Font("Arial", Font.BOLD, 12);
    private final Font textFont = new Font("Arial", Font.PLAIN, 8);

    private int rememberedPageIndex = -1;
    private int rememberedPick = 0;
    private int currentpick = 0;
    private boolean rememberedEOF = false;

    public PrintPickingList(EditingSession session, WeavingDraftWindow draftWindow) {
        super(session, draftWindow);
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex)
            throws PrinterException {

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
                rememberedPick = currentpick;
            } else {
                currentpick = rememberedPick;
            }

            g.setColor(Color.black);

            int x = (int) pf.getImageableX() + 10;
            int y = (int) pf.getImageableY() + 20;
            // Title lineif (currentpick>= draft.getPicks().size()){

            if (currentpick >= draft.getPicks().size()) {
                return Printable.NO_SUCH_PAGE;
            }
            g.setFont(headerFont);
            g.drawString("WeaveDreamer: " + draft.getName() + ",Picking List, page: " + (pageIndex + 1), x, y);
            g.setFont(textFont);
            // Generate as many lines as will fit in imageable area 
            y += 36;

            while (y + 12 < pf.getImageableY() + pf.getImageableHeight()) {
                if (currentpick < draft.getPicks().size()) {
                    WeftPick pick = draft.getPicks().get(currentpick);
                    int numtreadles = pick.getTreadles().length;
                    for (int j = 0; j < numtreadles; j++) {
                        if (pick.isTreadleSelected(j)) {
                            g.setColor(Color.GRAY);
                            g.fillRect(x + 15 + squaresize * j, y - squaresize, squaresize, squaresize);
                        }
                        g.setColor(Color.BLACK);
                        g.drawRect(x + 15 + squaresize * j, y - squaresize, squaresize, squaresize);
                        g.drawString((pick.isTreadleSelected(j) ? Integer.toString(j + 1) : "."),
                                x + 15 + (numtreadles + 1) * squaresize + j * pickTextSpacing, y);
                    }

                    g.setColor(pick.getColor());
                    g.fillRect(x, y - squaresize, squaresize, squaresize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y - squaresize, squaresize, squaresize);

                    if (currentpick % 10 == 9) {
                        y += 12;
                    }
                    y += 12;
                    currentpick++;
                } else {
                    rememberedEOF = true;
                    break;
                }

            }
            return Printable.PAGE_EXISTS;
        } catch (Exception e) {
            return Printable.NO_SUCH_PAGE;
        }
    }
}
