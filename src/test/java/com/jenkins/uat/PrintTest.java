/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.uat;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author David
 */
public class PrintTest extends WeavingTestCase {
    @Test
    public void PrintDialog() throws IOException {
		File wsml = new File("testdata/103.wsml");
		if (wsml.exists()) wsml.delete();

		File wif = new File("testdata/103.wif");

		ui.open(wif);
                
                ui.openprintdialog();
                
                ui.SelectPrintFunction("Draft");
                
                ui.SelectPrintFunction("Threading");
                ui.SelectPrintFunction("Picking");
                ui.SelectPrintFunction("TieUp");
                ui.SelectPrintFunction("Pattern");
                ui.closeprintdialog();
                
                ui.close();
                        
                
                
    } 
    
}
