package com.jenkins.weavingsimulator;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import com.jenkins.weavingsimulator.models.AbstractWeavingDraftModel;
import com.jenkins.weavingsimulator.models.CopyableWeavingGridModel;
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
	
	public void show (Component invoker, int x, int y) {
		if (model.supportsPaste()) {
			JPopupMenu menu = new JPopupMenu();
			JMenuItem item;
			
	    	item = new JMenuItem("Paste");
	    	item.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					model.pasteSelection(row, column);
				}	    		
	    	});
	    	menu.add(item);
			
	    	item = new JMenuItem("Paste Special...");
	    	item.addActionListener (new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					model.pasteSelection(row, column);
				}	    		
	    	});
	    	
	    	menu.show(invoker, x, y);
		}
	}
}
