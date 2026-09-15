package com.jenkins.weavedreamer;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SelectedFileChooser extends JFileChooser {



  
    
      
    
    private static final long serialVersionUID = 1L;

	public SelectedFileChooser(String string) {
		super(string);
	}
		// TODO Auto-generated constructor stub
	

	public int showSaveDialog(Component parent ){
        int retval;
            retval = super.showSaveDialog(parent);
            if (retval == this.APPROVE_OPTION){
             
                if (!getFileFilter().accept(getSelectedFile())){
 
                    if (getFileFilter() instanceof FileNameExtensionFilter){
                        FileNameExtensionFilter selectedFilter= (FileNameExtensionFilter) getFileFilter();
                        setSelectedFile(new File(getSelectedFile() + "." + selectedFilter.getExtensions()[0] ));
                    }
                 // should we drop out if we cant make a valid file ?
                }
                
                
            File newFile =  getSelectedFile();
            if (newFile.exists() && newFile.isFile()){
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null, "Overwrite " + newFile.getName() ,"Warning",dialogButton);
                if (dialogResult==JOptionPane.YES_OPTION){
                    
                    return this.APPROVE_OPTION;
                }
                else{
                
                    return this.CANCEL_OPTION;   
                }  
                
            }
        }
             
    
        return retval; 
        }
}
            