package com.jenkins.weavedreamer;

import com.jenkins.weavedreamer.models.EditingSession;

import javax.swing.*;

/**
 * Frame that is also an Editing Session.View, for the tiled view.
 *
 * @author pete
 */
public class TiledViewFrame extends JFrame implements EditingSession.View {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String name = "tiledView";

    public TiledViewFrame(String title) {
        super(title);
        setName(name);
    }

    @Override
    public void closeView() {
        setVisible(false);
    }

    @Override
    public String getViewName() {
        return "Tiled";
    }
}
