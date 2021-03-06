package com.jenkins.weavedreamer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PasteSpecialWindow extends javax.swing.JDialog {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JCheckBox transpose;
    private JCheckBox mirror_v;
    private JCheckBox mirror_h;
    private JFormattedTextField scale_h;
    private JFormattedTextField scale_v;
    private JFormattedTextField skip_h;
    private JFormattedTextField skip_v;
    private JFormattedTextField repeat_h;
    private JFormattedTextField repeat_v;
    private JButton ok;
    private JButton cancel;

    private NonNegativeIntFormatter formatter = new NonNegativeIntFormatter();

    private boolean okClicked = false;

    public static final String name = "pasteSpecialWindow";

    public PasteSpecialWindow(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setName(name);
    }

    private GridBagConstraints makeConstraints(int width) {
        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = width;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        return gridBagConstraints;
    }

    private void initComponents() {
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        transpose = new JCheckBox();
        transpose.setText("Transpose");
        transpose.setName("transpose");
        getContentPane().add(transpose, makeConstraints(GridBagConstraints.REMAINDER));

        mirror_v = new JCheckBox();
        mirror_v.setText("Reflect top-bottom");
        mirror_v.setName("mirror_v");
        getContentPane().add(mirror_v, makeConstraints(GridBagConstraints.REMAINDER));

        mirror_h = new JCheckBox();
        mirror_h.setText("Reflect left-right");
        mirror_h.setName("mirror_h");
        getContentPane().add(mirror_h, makeConstraints(GridBagConstraints.REMAINDER));

        JLabel shlabel = new JLabel();
        shlabel.setText("Scale across");
        getContentPane().add(shlabel, makeConstraints(GridBagConstraints.RELATIVE));

        scale_h = new JFormattedTextField(formatter);
        scale_h.setName("scale_h");
        scale_h.setColumns(2);
        scale_h.setText("1");
        getContentPane().add(scale_h, makeConstraints(GridBagConstraints.REMAINDER));

        JLabel svlabel = new JLabel();
        svlabel.setText("Scale down");
        getContentPane().add(svlabel, makeConstraints(GridBagConstraints.RELATIVE));

        scale_v = new JFormattedTextField(formatter);
        scale_v.setName("scale_v");
        scale_v.setColumns(2);
        scale_v.setText("1");
        getContentPane().add(scale_v, makeConstraints(GridBagConstraints.REMAINDER));

        JLabel rhlabel = new JLabel();
        rhlabel.setText("Repeat across");
        getContentPane().add(rhlabel, makeConstraints(GridBagConstraints.RELATIVE));

        repeat_h = new JFormattedTextField(formatter);
        repeat_h.setName("repeat_h");
        repeat_h.setColumns(2);
        repeat_h.setText("1");
        getContentPane().add(repeat_h, makeConstraints(GridBagConstraints.REMAINDER));

        JLabel rvlabel = new JLabel();
        rvlabel.setText("Repeat down");
        getContentPane().add(rvlabel, makeConstraints(GridBagConstraints.RELATIVE));

        repeat_v = new JFormattedTextField(formatter);
        repeat_v.setName("repeat_v");
        repeat_v.setColumns(2);
        repeat_v.setText("1");
        getContentPane().add(repeat_v, makeConstraints(GridBagConstraints.REMAINDER));

        ok = new JButton();
        ok.setText("OK");
        ok.setName("ok");
        ok.addActionListener(evt -> onOk(evt));
        getContentPane().add(ok, makeConstraints(GridBagConstraints.RELATIVE));

        cancel = new JButton();
        cancel.setText("Cancel");
        cancel.setName("cancel");
        cancel.addActionListener(evt -> onCancel(evt));
        getContentPane().add(cancel, makeConstraints(GridBagConstraints.RELATIVE));

        pack();
    }

    protected void onCancel(ActionEvent evt) {
        okClicked = false;
        setVisible(false);
    }

    protected void onOk(ActionEvent evt) {
        okClicked = true;
        setVisible(false);
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public boolean isTranspose() {
        return transpose.isSelected();
    }

    public boolean isReflectH() {
        return mirror_h.isSelected();
    }

    public boolean isReflectV() {
        return mirror_v.isSelected();
    }

    public int getScaleH() {
        return Integer.parseInt(scale_h.getText());
    }

    public int getScaleV() {
        return Integer.parseInt(scale_v.getText());
    }

    public int getRepeatH() {
        return Integer.parseInt(repeat_h.getText());
    }

    public int getRepeatV() {
        return Integer.parseInt(repeat_v.getText());
    }
}
