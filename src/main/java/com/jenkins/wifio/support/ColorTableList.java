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
    public ArrayList colortable = new ArrayList();
    
    public ColorTableList(){

    }
    public void add(Color color){
        if (!colorinlist(color)){
            colortable.add(colortable.size(),color);
        }
    }
       
    private boolean colorinlist(Color color){
    for (int i=0;i<colortable.size();i++ ){
        if (colortable.get(i)==color){
        return true;}
    }
        
    return false;
            }
    
    /**
     *
     * @param color
     * @return index in color table
     * adds color to table if not present 
     */
    public final int getindex(Color color){
        
        add(color);
        
        for (int i=0;i<colortable.size();i++ ){
            if (colortable.get(i)==color){
            return i+1;}
    }
    
    return -1;
    }
    
}
