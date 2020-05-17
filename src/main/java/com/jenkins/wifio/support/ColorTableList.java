/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.wifio.support;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author David
 */
public class ColorTableList {

    public ArrayList<Color> colortable;

    public ColorTableList() {
        this.colortable = new <Color> ArrayList();
    }

    public void add(Color color) {
        //System.out.printf("Check for %s\n",  color);
        if (!colorinlist(color)) {
            //System.out.printf("Add %s\n",  color);
            colortable.add(color);
        }
    }

    private boolean iscolorequal(Color C1, Color C2) {

        if ((C1.getRed() == C2.getRed()) && (C1.getBlue() == C2.getBlue()) && (C1.getGreen() == C2.getGreen())) {
            return true;
        } else {
            return false;
        }

    }

    private boolean colorinlist(Color color) {
        for (int i = 0; i < colortable.size(); i++) {
            if (iscolorequal(color, colortable.get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param color
     * @return index in color table adds color to table if not present
     */
    public final int getindex(Color color) {

        add(color);

        for (int i = 0; i < colortable.size(); i++) {
            if (iscolorequal(color, colortable.get(i))) {
                return i + 1;
            }
        }

        return -1;
    }

}
