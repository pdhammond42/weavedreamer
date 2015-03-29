package com.jenkins.weavingsimulator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jenkins.weavingsimulator.models.EditingSession;
import com.jenkins.weavingsimulator.models.ThreadingDraftModel;
import com.jenkins.weavingsimulator.models.TieUpModel;

public class NetworkWindow extends EditingSessionWindow {
	private JPanel panel1;
	private JPanel panel2;
	private JFormattedTextField initialRows;
	private JFormattedTextField initialCols;
	private JFormattedTextField patternLineRows;
	private JFormattedTextField patternLineCols;
	private JCheckBox shaftRule;
	private JComboBox compressionRule;
	private GridControl initialGrid;
	private GridControl patternLineGrid;
	private GridControl key1;
	private GridControl key2;
	
	private NonNegativeIntFormatter formatter = new NonNegativeIntFormatter();
	
	public NetworkWindow (EditingSession session)
	{
		super(session);
		
		setClosable(true);
		setMaximizable(true);
		setResizable(true);
		
		initComponents();
		
		setTitle ("network");
	}
	
	private void initComponents() {
		getContentPane().setLayout(new GridBagLayout());
		panel1 = new JPanel(new GridBagLayout());
		panel2 = new JPanel(new GridBagLayout());
		getContentPane().add(panel1, makeConstraints(0,0));
		getContentPane().add(panel2, makeConstraints(0,1));

		addLabel("Initial", 0, 0, panel1);
		
		initialRows = new JFormattedTextField(formatter);
		initialRows.setName("initialRows");
		initialRows.setColumns(2);
		initialRows.setText("4");
		panel1.add(initialRows, makeConstraints(1, 0));	

        addLabel("x", 2, 0, panel1);

		initialCols = new JFormattedTextField(formatter);
		initialCols.setName("initialCols");
		initialCols.setColumns(2);
		initialCols.setText("4");
		panel1.add(initialCols, makeConstraints(3, 0));
        
		addLabel("Pattern line", 0, 1, panel1);
		
		patternLineRows = new JFormattedTextField(formatter);
		patternLineRows.setName("patternLineRows");
		patternLineRows.setColumns(2);
		patternLineRows.setText("4");
		panel1.add(patternLineRows, makeConstraints(1, 1));	

        addLabel("x", 2, 1, panel1);

        patternLineCols = new JFormattedTextField(formatter);
        patternLineCols.setName("patternLineCols");
        patternLineCols.setColumns(2);
        patternLineCols.setText("4");
        panel1.add(patternLineCols, makeConstraints(3, 1));
        
        shaftRule = new JCheckBox();
        shaftRule.setText("Use shaft rule");
        shaftRule.setName("shaftRule");
        panel1.add(shaftRule, makeConstraints(0, 2, 2));
        
        addLabel ("Compression rule", 0, 3, panel1);
        compressionRule = new JComboBox();
        compressionRule.addItem("Telescope");
        compressionRule.addItem("Digitize");
        compressionRule.setName("compressionRule");
        panel1.add(compressionRule, makeConstraints(1, 3, 3));

        addLabel ("Initial", 0, 0, panel2);
        initialGrid = new GridControl();
        initialGrid.setName("initialGrid");
        initialGrid.setModel(new TieUpModel(getSession()));
        panel2.add(initialGrid, makeConstraints(0,1));
     
        addLabel ("Key 1", 1, 0, panel2);
        key1 = new GridControl();
        key1.setName("key1");
        key1.setModel(new TieUpModel(getSession()));
        panel2.add(key1, makeConstraints(1,1));

        addLabel ("Key 2", 2, 0, panel2);
        key2 = new GridControl();
        key2.setName("key2");
        key2.setModel(new TieUpModel(getSession()));
        panel2.add(key2, makeConstraints(2,1));
        
        addLabel ("Pattern line", 0, 3, panel2);
        patternLineGrid = new GridControl();
        patternLineGrid.setName("patternLineGrid");
        patternLineGrid.setModel(new ThreadingDraftModel(getSession()));
        panel2.add(patternLineGrid, makeConstraints(0, 4, GridBagConstraints.REMAINDER));
        
        pack();
	}
	
	private void addLabel (String text, int x, int y, JPanel panel) {
        JLabel label = new JLabel();
        label.setText(text);
        panel.add(label, makeConstraints(x, y));			
	}
	
	private GridBagConstraints makeConstraints(int x, int y) {
		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = x;
        gridBagConstraints.gridy = y;
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        return gridBagConstraints;
	}
	
	private GridBagConstraints makeConstraints (int x, int y, int width)
	{
		GridBagConstraints c = makeConstraints(x, y);
		c.gridwidth = width;
		return c;
	}
}
