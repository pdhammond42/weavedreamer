package com.jenkins.weavedreamer;

import com.jenkins.weavedreamer.models.AbstractWeavingDraftModel;
import com.jenkins.weavedreamer.models.CellSelectionTransform;
import com.jenkins.weavedreamer.models.CellSelectionTransforms;
import com.jenkins.weavedreamer.models.EditingSession;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Menu functionality for pasting.
 *
 * @author pete
 */
public class PasteMenu {
    EditingSession session;
    AbstractWeavingDraftModel model;
    int row;
    int column;

    public PasteMenu(AbstractWeavingDraftModel model,
                     int row, int column) {
        this.model = model;
        this.row = row;
        this.column = column;
    }

    public void show(Component invoker, final int x, final int y) {
        if (model.supportsPaste()) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem item;

            item = new JMenuItem("Paste");
            item.addActionListener(e -> model.pasteSelection(row, column, CellSelectionTransforms.Null()));
            item.setEnabled(model.canPaste());
            menu.add(item);

            final Point loc = invoker.getLocationOnScreen();
            item = new JMenuItem("Paste Special...");
            item.addActionListener(e -> showPasteSpecial(loc.x + x, loc.y + y));
            item.setEnabled(model.canPaste());
            menu.add(item);

            menu.show(invoker, x, y);
        }
    }

    public void showPasteSpecial(int x, int y) {
        PasteSpecialWindow dlg = new PasteSpecialWindow(null, true);
        dlg.setLocation(x, y);
        dlg.setVisible(true);
        if (dlg.isOkClicked()) {
            List<CellSelectionTransform> transforms = new ArrayList<CellSelectionTransform>();
            if (dlg.getScaleH() > 1) transforms.add(CellSelectionTransforms.ScaleHorizontal(dlg.getScaleH()));
            if (dlg.getScaleV() > 1) transforms.add(CellSelectionTransforms.ScaleVertical(dlg.getScaleV()));
            if (dlg.isReflectV()) transforms.add(CellSelectionTransforms.ReflectVertical());
            if (dlg.isReflectH()) transforms.add(CellSelectionTransforms.ReflectHorizontal());
            if (dlg.isTranspose()) transforms.add(CellSelectionTransforms.Transpose());
            if (dlg.getRepeatH() > 1) transforms.add(CellSelectionTransforms.RepeatHorizontal(dlg.getRepeatH()));
            if (dlg.getRepeatV() > 1) transforms.add(CellSelectionTransforms.RepeatVertical(dlg.getRepeatV()));
            model.pasteSelection(row, column, CellSelectionTransforms.Combine(transforms));
        }
    }
}
