/* 
 * $RCSfile: SearchableComboBox.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:29 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class SearchableComboBox extends JComboBox implements KeyListener{
	

	/**
	 * @param aModel
	 */
	public SearchableComboBox(ComboBoxModel aModel) {
		super(aModel);
		init();
	}

	/**
	 * @param items
	 */
	public SearchableComboBox(Object[] items) {
		super(items);
		init();
	}

	/**
	 * @param items
	 */
	public SearchableComboBox(Vector items) {
		super(items);
		init();
	}

	/**
	 * 
	 */
	public SearchableComboBox() {
		super();
		init();
	}
	
	private void init(){
		setKeySelectionManager(new MyKeySelectionManager());
		//addActionListener(this);
		//addKeyListener(this);
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Searchable Combo Tester");
		
		SearchableComboBox box = new SearchableComboBox();
		box.setEditable(true);
		box.addItem("olivier");
		box.addItem("elohim");
		box.addItem("eloise");
		box.addItem("pierre");
		box.addItem("karine");
		box.addItem("aisa");
		JPanel panel = new JPanel();
		panel.add(box);
		//panel.seto
		frame.getContentPane().add(new JScrollPane(panel));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		System.out.println("action performed : " + e.getActionCommand());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
		System.out.println("key typed");
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		System.out.println("key pressed");
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		System.out.println("key released");
		
	}

}


//This key selection manager will handle selections based on multiple keys.
 class MyKeySelectionManager implements JComboBox.KeySelectionManager {
	 long lastKeyTime = 0;
	 String pattern = "";
    
	 public int selectionForKey(char aKey, ComboBoxModel model) {
		 System.out.println(".");
		 // Find index of selected item
		 int selIx = 01;
		 Object sel = model.getSelectedItem();
		 if (sel != null) {
			 for (int i=0; i<model.getSize(); i++) {
				 if (sel.equals(model.getElementAt(i))) {
					 selIx = i;
					 break;
				 }
			 }
		 }
    
		 // Get the current time
		 long curTime = System.currentTimeMillis();
    
		 // If last key was typed less than 300 ms ago, append to current pattern
		 if (curTime - lastKeyTime < 1000) {
			 pattern += ("" + aKey).toLowerCase();
		 } else {
			 pattern = ("" + aKey).toLowerCase();
		 }
    
		 // Save current time
		 lastKeyTime = curTime;
    
		 // Search forward from current selection
		 for (int i=selIx+1; i<model.getSize(); i++) {
			 String s = model.getElementAt(i).toString().toLowerCase();
			 if (s.startsWith(pattern)) {
				 return i;
			 }
		 }
    
		 // Search from top to current selection
		 for (int i=0; i<selIx ; i++) {
			 if (model.getElementAt(i) != null) {
				 String s = model.getElementAt(i).toString().toLowerCase();
				 if (s.startsWith(pattern)) {
					 return i;
				 }
			 }
		 }
		 return -1;
	 }
 }
 