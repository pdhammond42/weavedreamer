/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.weavedreamer.print;

import com.jenkins.weavedreamer.WeavingDraftWindow;
import com.jenkins.weavedreamer.WeavingGridControl;
import com.jenkins.weavedreamer.models.EditingSession;
import com.jenkins.weavedreamer.models.WeavingPatternModel;
import java.awt.*;
import java.awt.print.*;
import java.text.MessageFormat;
import javax.swing.JTable;

/**
 *
 * @author David
 */
public class PrintPattern extends AbstractWeaveDreamerPrintable {
    private WeavingGridControl weavingPatternGrid;

    private Font headerFont = new Font("Arial", Font.BOLD, 12);
    
    private Font textFont = new Font("Arial", Font.PLAIN, 8);
    private Printable weavingGridPrintable;


    
    
    
    

    
   public PrintPattern (EditingSession session,WeavingDraftWindow draftWindow){
       super(session,draftWindow);
       
        Font headerFont = new Font("Arial", Font.BOLD, 12);
        weavingPatternGrid = new WeavingGridControl();
        weavingPatternGrid.setModel(new WeavingPatternModel(session));
	weavingPatternGrid.setName("weavingPatternGrid");
	weavingPatternGrid.setIntercellSpacing(new Dimension(0,0));
	weavingPatternGrid.setShowGrid(false);
        weavingPatternGrid.setSquareWidth(10);
        weavingPatternGrid.setSize(weavingPatternGrid.getPreferredSize());
        weavingPatternGrid.setVisible(true);
        weavingPatternGrid.setFont(headerFont);
       
        MessageFormat headerFormat = new MessageFormat("WeaveDreamer "+draft.getName() +  " Pattern Page {0}");
   
        weavingGridPrintable= weavingPatternGrid.getPrintable(JTable.PrintMode.FIT_WIDTH, headerFormat, null);
       

    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException{
        
            return weavingGridPrintable.print(g,pf,pageIndex);
        
    }

}
