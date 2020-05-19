package com.jenkins.weavedreamer;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;

/**
 * Window to provide a slightly less baffling initial user experience.
 */
public class GettingStartedWindow extends JInternalFrame {
    private boolean dontShow = false;

    public GettingStartedWindow() {
        setFrameIcon(new ImageIcon(getClass().getResource("icon-24.png")));

        JPanel panel = new JPanel();
        this.add(panel);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints;

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        //panel.add(new JEditorPane("text/html", loadContent()));

        try {
            JEditorPane p = new JEditorPane(getClass().getResource("getting_started.html"));
            p.setEditable(false);
            // The page is loaded asynchronously. No point packing before the page is loaded
            // and the size is known. Then, it packs height first so we get a window that is tall
            // enough for the content rendered in a narrow width, but also wide enough to render the lines
            // as expected, so large grey bands top and bottom., The second pack shrinks the height to fit.
            p.addPropertyChangeListener("page", e -> {
                pack();
                pack();
            });
            panel.add(p, constraints);
        } catch (IOException e) {
            panel.add(new JEditorPane("text/plain", "Oops, can't load welcome text"), constraints);
        }
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

        // Just in case the page loads before the buttons are added.
        pack();

        this.setResizable(true);
        setTitle("Getting Started");
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
        return !dontShow;
    }
}
