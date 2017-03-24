package com.jenkins.weavingsimulator;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;

/**
 * Window to provide a slightly less baffling initial user experience.
 */
public class GettingStartedWindow extends JInternalFrame {
    private boolean dontShow = false;

    public GettingStartedWindow() {
        JPanel panel = new JPanel();
        this.add(panel);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints;

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(new JEditorPane("text/html", loadContent()), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener((ev) -> close());
        panel.add(closeButton, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        JCheckBox dontShowButton = new JCheckBox("Don't show this again");
        dontShowButton.addActionListener((ev) -> dontShow = true);
        panel.add(dontShowButton, constraints);

        pack();

        this.setResizable(true);
    }

    private void close() {

        try {
            this.setClosed(true);
        } catch (PropertyVetoException ex) {
        }
    }

    private String loadContent() {
        InputStream resource = getClass().getResourceAsStream("getting_started.html");
        if (resource != null) {
            return new BufferedReader(new InputStreamReader(resource))
                    .lines().collect(joining("\n"));
        } else {
            return "";
        }
    }

    public boolean getShow() {
        return ! dontShow;
    }
}
