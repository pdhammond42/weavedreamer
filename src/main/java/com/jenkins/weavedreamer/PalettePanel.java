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


package com.jenkins.weavedreamer;

import com.jenkins.weavedreamer.models.EditingSession;
import com.jenkins.weavedreamer.models.PaletteColorChangeCommand;
import com.jenkins.weavedreamer.models.PaletteModel;
import com.jenkins.weavingsimulator.datatypes.Palette;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
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

    /**
     * Creates a new instance of PalettePanel
     */
    public PalettePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        ActionListener okListener = e -> session.execute(
                new PaletteColorChangeCommand(session.getPalette(), colorChooser.getColor()));
        ActionListener cancelListener = e -> {
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
        addColorBtn.addActionListener(e -> {
            Palette p = session.getPalette();
            p.setNumColors(p.getNumColors() + 1);
        });
        addColorBtn.setEnabled(true);
        addColorBtn.setName("addColorBtn");

        paletteGrid = new GridControl();
        paletteGrid.setAutoCreateColumnsFromModel(true);
        paletteGrid.setSquareWidth(20);

        paletteGrid.getColumnModel().addColumnModelListener(new TableColumnModelListener() {

            @Override
            public void columnAdded(TableColumnModelEvent e) {
                paletteGrid.changeSelection(0, paletteGrid.getModel().getColumnCount() - 1, false, false);
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
                session.getPalette().setSelection(paletteGrid.getSelectedColumn());
            }
        });

        paletteGrid.setCellSelectionEnabled(true);
        paletteGrid.setEnabled(false);
        paletteGrid.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    colorChooser.setColor(session.getPalette().getColor(
                            session.getPalette().getSelection()));
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
        add(addColorBtn, BorderLayout.EAST);
        paletteGrid.setName("paletteGrid");

        addColorBtn.setPreferredSize(new Dimension(paletteGrid.getSquareWidth(), paletteGrid.getSquareWidth()));
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
                ev -> paletteGrid.setModel(new PaletteModel((Palette) ev.getNewValue())));
        paletteGrid.changeSelection(0, 0, false, false);
        paletteGrid.setEnabled(true);
    }
}
