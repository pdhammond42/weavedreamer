/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.weavedreamer.print;

/**
 *
 * @author David
 */
import com.jenkins.weavedreamer.WeavingDraftWindow;
import com.jenkins.weavedreamer.models.EditingSession;
import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.awt.print.*;

public class PrintUIWindow extends AbstractWeaveDreamerPrintable {

    //JInternalFrame frameToPrint;
    JPanel frameToPrint;
   private Font headerFont = new Font("Arial", Font.BOLD, 12);
    
    private Font textFont = new Font("Arial", Font.PLAIN, 8);

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws
            PrinterException {

        if (page > 0) {
            /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        float scaleX;
        float scaleY;
        float scale;
        float headerheight = 20;

        
        Font fnt = new Font("Courier", Font.PLAIN, 10);

        Point componentLocation = null;
        float internalborder = 5;

        Point printOrig = new Point(99999, 99999);

        float frameWidth = frameToPrint.getPreferredSize().width;
        float frameHeight = frameToPrint.getPreferredSize().height;

        for (int comp = 0; comp < frameToPrint.getComponentCount(); comp++) {
            componentLocation = frameToPrint.getComponent(comp).getLocation();
            printOrig.x = Math.min(printOrig.x, componentLocation.x);
            printOrig.y = Math.min(printOrig.y, componentLocation.y);
        }

        scaleX = ((float) pf.getImageableWidth() - 2 * internalborder) / frameWidth;
        scaleY = ((float) pf.getImageableHeight() - 2 * internalborder - headerheight) / frameHeight;

        scale = Math.min(scaleX, scaleY);

        float translateX = (float) pf.getImageableX() + internalborder - printOrig.x * scale;
        float translateY = (float) pf.getImageableY() + internalborder + headerheight - printOrig.y * scale;

        g.setColor(Color.black);
        g.setFont(headerFont);
        int x = (int) pf.getImageableX() + 10;
        int y = (int) pf.getImageableY() + 12;

        g.drawString("WeaveDreamer: " + draft.getName() + " Draft, page: 1", x, y);
        g.setFont(textFont);
        AffineTransform Tx;
        Tx = new AffineTransform(scale, 0.0, 0.0, scale, translateX, translateY);
        g2d.transform(Tx);

        frameToPrint.printComponents(g);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }

    public PrintUIWindow(EditingSession session, WeavingDraftWindow draftWindow) {
        super(session, draftWindow);

        frameToPrint = draftWindow.getdraftPanel();

    }
}
