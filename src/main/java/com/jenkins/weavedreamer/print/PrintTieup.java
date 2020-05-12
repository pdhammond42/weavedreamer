/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.weavedreamer.print;

import com.jenkins.weavedreamer.WeavingDraftWindow;
import com.jenkins.weavedreamer.models.EditingSession;
import java.awt.*;
import java.awt.print.*;

/**
 *
 * @author David
 */
public class PrintTieup extends AbstractWeaveDreamerPrintable {

    private int fontHeight = 8;
    
    private Font headerFont = new Font("Arial", Font.BOLD, 12);
    private Font textFont = new Font("Arial", Font.PLAIN, 8);

    public PrintTieup(EditingSession session, WeavingDraftWindow draftWindow) {
        super(session, draftWindow);
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex)
            throws PrinterException {

        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }
        try {
            // For catching IOException      

            int squaresize = 12;
            g.setColor(Color.BLACK);
            int x = (int) pf.getImageableX() + 30;
            int y = (int) pf.getImageableY() + 12;
            g.setFont(headerFont);
            g.drawString("WeaveDreamer: " + draft.getName() + ",Tie Up, page: " + (pageIndex + 1), x, y);
            g.setFont(textFont);
            y += 20;
            for (int i = 0; i < draft.getTreadles().size(); i++) {
                g.drawString(String.format("%d", i + 1), x + i * squaresize, y);
                for (int j = 0; j < draft.getNumHarnesses(); j++) {

                    if (draft.getTreadles().get(i).contains(j)) {

                        g.fillRect(x + (i * squaresize),
                                y + ((draft.getNumHarnesses() - j) * squaresize),
                                squaresize,
                                squaresize);
                    }
                    g.drawRect(x + (i * squaresize),
                            y + ((draft.getNumHarnesses() - j) * squaresize),
                            squaresize,
                            squaresize);

                }

            }

            for (int j = 0; j < draft.getNumHarnesses(); j++) {
                g.drawString(String.format("%d", j + 1), x - 20, y + fontHeight + (draft.getNumHarnesses() - j) * squaresize);

            }

            return Printable.PAGE_EXISTS;
        } catch (Exception e) {
            return Printable.NO_SUCH_PAGE;
        }
    }
}
