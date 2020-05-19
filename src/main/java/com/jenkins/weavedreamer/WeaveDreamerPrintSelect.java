/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.weavedreamer;

import com.jenkins.weavedreamer.models.EditingSession;
import com.jenkins.weavedreamer.print.*;

import java.awt.print.PrinterJob;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author David
 */
public class WeaveDreamerPrintSelect extends java.awt.Dialog {

    /**
     * Creates new form WeaveDreamerPrintSelect
     */
    public WeaveDreamerPrintSelect(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        // do the selection action
        jRadioButtonPrintPatternActionPerformed(new java.awt.event.ActionEvent("", 0, ""));
        //PrintFormatter = PrintPickingList.class;

    }

    private void initComponents() {
        this.setTitle("Select Print Item");
        javax.swing.ButtonGroup buttonGroup1 = new javax.swing.ButtonGroup();
        javax.swing.JButton jButtonPrint = new javax.swing.JButton();
        javax.swing.JButton jButtonCancel = new javax.swing.JButton();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JRadioButton jRadioButtonPrintThreading = new javax.swing.JRadioButton();
        javax.swing.JRadioButton jRadioButtonPrintDraft = new javax.swing.JRadioButton();
        javax.swing.JRadioButton jRadioButtonPrintTieup = new javax.swing.JRadioButton();
        javax.swing.JRadioButton jRadioButtonPrintPicking = new javax.swing.JRadioButton();
        javax.swing.JRadioButton jRadioButtonPrintPattern = new javax.swing.JRadioButton();

        setBackground(java.awt.SystemColor.control);
        setLocation(new java.awt.Point(100, 100));
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jButtonPrint.setText("Print");
        jButtonPrint.setName("Print");
        jButtonPrint.addActionListener(evt -> jButtonPrintActionPerformed(evt));

        jButtonCancel.setText("Cancel");
        jButtonCancel.setName("Cancel");
        jButtonCancel.addActionListener(evt -> jButtonCancelActionPerformed(evt));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Print Selection"));

        buttonGroup1.add(jRadioButtonPrintThreading);
        jRadioButtonPrintThreading.setText("Threading");
        jRadioButtonPrintThreading.setName("Threading");
        jRadioButtonPrintThreading.addActionListener(this::jRadioButtonPrintThreadingActionPerformed);

        buttonGroup1.add(jRadioButtonPrintDraft);
        jRadioButtonPrintDraft.setText("Draft");
        jRadioButtonPrintDraft.setName("Draft");
        jRadioButtonPrintDraft.addActionListener(this::jRadioButtonPrintDraftActionPerformed);

        buttonGroup1.add(jRadioButtonPrintTieup);
        jRadioButtonPrintTieup.setText("TieUp");
        jRadioButtonPrintTieup.setName("TieUp");
        jRadioButtonPrintTieup.addActionListener(this::jRadioButtonPrintTieupActionPerformed);

        buttonGroup1.add(jRadioButtonPrintPicking);
        jRadioButtonPrintPicking.setText("Picking");
        jRadioButtonPrintPicking.setName("Picking");
        jRadioButtonPrintPicking.addActionListener(this::jRadioButtonPrintPickingActionPerformed);

        buttonGroup1.add(jRadioButtonPrintPattern);
        jRadioButtonPrintPattern.setSelected(true);
        jRadioButtonPrintPattern.setText("Pattern");
        jRadioButtonPrintPattern.setName("Pattern");
        jRadioButtonPrintPattern.addActionListener(this::jRadioButtonPrintPatternActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jRadioButtonPrintPattern, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jRadioButtonPrintDraft, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jRadioButtonPrintPicking, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jRadioButtonPrintThreading, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jRadioButtonPrintTieup, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jRadioButtonPrintPattern)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButtonPrintDraft)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButtonPrintThreading)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButtonPrintPicking)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButtonPrintTieup))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, jRadioButtonPrintPattern, jRadioButtonPrintPicking, jRadioButtonPrintThreading, jRadioButtonPrintTieup);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(26, 26, 26)
                                                .addComponent(jButtonPrint)
                                                .addGap(30, 30, 30)
                                                .addComponent(jButtonCancel))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(22, 22, 22)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButtonPrint)
                                        .addComponent(jButtonCancel))
                                .addGap(22, 22, 22))
        );

        pack();
    }

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    private void jRadioButtonPrintTieupActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        PrintFormatter = PrintTieup.class;

    }

    private void jButtonPrintActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        PrinterJob job = PrinterJob.getPrinterJob();
        AbstractWeaveDreamerPrintable pf2 = null;
        //jButtonPrint.setEnabled(false);

        try {
            //try{
            Constructor ct;
            ct = PrintFormatter.getDeclaredConstructor(session.getClass(), draftWindow.getClass());
            pf2 = (AbstractWeaveDreamerPrintable) ct.newInstance(session, draftWindow);
        } catch (NoSuchMethodException | SecurityException ex) {
            //Logger.getLogger(WeaveDreamerPrintSelect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(WeaveDreamerPrintSelect.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.setVisible(false);

        try {
            if (job.printDialog() && pf2 != null) {
                job.setPrintable(pf2);
                job.print();
            }
        } catch (Exception e) {
            /* handle exception */
        } finally {
            //jButtonPrint.setEnabled(true);
            this.setVisible(true);
        }

        // I think is better to leave selector open so user can print more
        //this.setVisible(false);
        //this.dispose();

    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        this.dispose();

    }

    private void jRadioButtonPrintPatternActionPerformed(java.awt.event.ActionEvent evt) {
        PrintFormatter = PrintPattern.class;

    }

    private void jRadioButtonPrintPickingActionPerformed(java.awt.event.ActionEvent evt) {
        PrintFormatter = PrintPickingList.class;


    }

    private void jRadioButtonPrintThreadingActionPerformed(java.awt.event.ActionEvent evt) {
        PrintFormatter = PrintThreadingList.class;
    }

    private void jRadioButtonPrintDraftActionPerformed(java.awt.event.ActionEvent evt) {
        PrintFormatter = PrintUIWindow.class;
    }


    public int PrintWeavingDraft(EditingSession session, WeavingDraftWindow draftWindow) {
        this.session = session;
        this.draftWindow = draftWindow;

        // show the form 
        this.setVisible(true);
        return 1;
    }


    private EditingSession session;
    //private AbstractWeaveDreamerPrintable PrintFormatter; 
    private Class PrintFormatter;
    private WeavingDraftWindow draftWindow;
}
