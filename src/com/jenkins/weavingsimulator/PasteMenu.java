package com.jenkins.weavingsimulator;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.jenkins.weavingsimulator.models.AbstractWeavingDraftModel;
import com.jenkins.weavingsimulator.models.CellSelectionTransform;
import com.jenkins.weavingsimulator.models.CellSelectionTransforms;
import com.jenkins.weavingsimulator.models.EditingSession;


/** Menu functionality for pasting.
 * 
 * @author pete
 *
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
	
	public void show (Component invoker, final int x, final int y) {
		if (model.supportsPaste()) {
			JPopupMenu menu = new JPopupMenu();
			JMenuItem item;
			
	    	item = new JMenuItem("Paste");
	    	item.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					model.pasteSelection(row, column, CellSelectionTransforms.Null());
				}	    		
	    	});
	    	item.setEnabled(model.canPaste());
	    	menu.add(item);

	    	final Point loc = invoker.getLocationOnScreen();
	    	item = new JMenuItem("Paste Special...");
	    	item.addActionListener (new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					showPasteSpecial(loc.x + x, loc.y + y);
				}	    		
	    	});
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
			if(dlg.isReflectV()) transforms.add(CellSelectionTransforms.ReflectVertical());
			if(dlg.isReflectH()) transforms.add(CellSelectionTransforms.ReflectHorizontal());
			if(dlg.isTranspose()) transforms.add(CellSelectionTransforms.Transpose());
			model.pasteSelection(row, column, CellSelectionTransforms.Combine(transforms));
		}
	}
}
