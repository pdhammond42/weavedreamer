/*
 * PalettePanel.java
 * 
 * Created on January 15, 2005, 11:36 PM
 *  
 * Copyright 2005 Adam P. Jenkins
 * 
 * This file is part of WeavingSimulator
 * 
 * WeavingSimulator is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * WeavingSimulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WeavingSimulator; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


package com.jenkins.weavingsimulator;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionListener;

import com.jenkins.weavingsimulator.datatypes.Palette;
import com.jenkins.weavingsimulator.models.EditingSession;
import com.jenkins.weavingsimulator.models.PaletteModel;


/**
 *
 * @author ajenkins
 */
public class PalettePanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EditingSession session;
    
    private GridControl paletteGrid;
    private JDialog colorChooserDialog;
    private JColorChooser colorChooser;
    private JButton addColorBtn;
    
    /** Creates a new instance of PalettePanel */
    public PalettePanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
    
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                session.getPalette().setColor(
                        session.getPalette().getSelection(),
                        colorChooser.getColor());
            }
        };
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        };
        
        colorChooser = new JColorChooser();
        colorChooserDialog = JColorChooser.createDialog(this,
                "Choose Palette Color",
                true, // modal dialog
                colorChooser,
                okListener,
                cancelListener);
        
        addColorBtn = new JButton("+");
        addColorBtn.setToolTipText(
                "Add a color to the palette");
        addColorBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Palette p = session.getPalette();
            	p.setNumColors(p.getNumColors()+1);
            }
        });
        addColorBtn.setEnabled(true);
        addColorBtn.setName("addColorBtn");
                
        paletteGrid = new GridControl();
        paletteGrid.setSquareWidth(20);
        paletteGrid.getSelectionModel().addListSelectionListener(
                new PaletteSelectionListener());
        paletteGrid.setCellSelectionEnabled(true);
        paletteGrid.setEnabled(false);
        paletteGrid.addMouseListener(new MouseListener() {
        	public void mouseClicked(MouseEvent e) {
        		if (e.getClickCount() == 2) {
        			colorChooserDialog.setVisible(true);
        		}
        	}
        	public void mouseEntered(MouseEvent e) {
        	}
        	public void mouseExited(MouseEvent e) {
        	}
        	public void mousePressed(MouseEvent e) {
        	}
        	public void mouseReleased(MouseEvent e) {
        	}
        });
        paletteGrid.setToolTipText(
                "Click to select color. Double click to change.");
        add(paletteGrid, BorderLayout.CENTER);
        add(addColorBtn, BorderLayout.PAGE_END);
        paletteGrid.setName("paletteGrid");
    }
    
    private class PaletteSelectionListener implements ListSelectionListener {
        public void valueChanged(javax.swing.event.ListSelectionEvent e) {
            if (e.getValueIsAdjusting())
                return;
  
            // getSelectedRow can return -1, but that's ok because -1 means the
            // same thing to setSelection -- nothing is selected.
            session.getPalette().setSelection(paletteGrid.getSelectedRow());
            //changeColorBtn.setEnabled (paletteGrid.getSelectedRow() != -1);
        }
        
    }

    public EditingSession getSession() {
        return session;
    }

    public void setSession(EditingSession session) {
        if (this.session != null)
            throw new IllegalStateException("Can't reset session");
        this.session = session;
        paletteGrid.setModel(new PaletteModel(session.getPalette()));
        session.addPropertyChangeListener(EditingSession.PALETTE_PROPERTY,
                new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                paletteGrid.setModel(new PaletteModel((Palette)ev.getNewValue()));
            }
        });
        paletteGrid.setRowSelectionInterval(0,0);
        paletteGrid.setEnabled(true);
    }
}
