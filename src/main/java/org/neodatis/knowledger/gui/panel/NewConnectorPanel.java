/* 
 * $RCSfile: NewConnectorPanel.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.gui.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.knowledger.gui.ComboBuilder;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.tool.DLogger;


/**
 * <p>
 * 
 * </p>
 * 
 */
public class NewConnectorPanel extends JPanel implements ActionListener, Observer {
	
	private IKnowledgeBase knowledgeBase;
	private JTextField tfNewRelationTypeName;
	private JComboBox cbRelationTypesCombo;
	private JButton okButton;
	private JButton cancelButton;
	
	public static String APPLY = "apply";
	public static String CANCEL = "cancel";
	

	public NewConnectorPanel(IKnowledgeBase knowledgeBase) {
		super();
		this.knowledgeBase = knowledgeBase;
		knowledgeBase.addObserver(this);
		init();
	}

	private void init() {
		FlowLayout flayout = new FlowLayout(FlowLayout.CENTER,5,5);
		
		tfNewRelationTypeName = new JTextField(15);
		
		cbRelationTypesCombo = new JComboBox();
		
		loadCombosData();

		// Set panel layout
		setLayout(new BorderLayout());
		
		// build a panel for data
		JPanel dataPanel = new JPanel(flayout , true);

		dataPanel.add(tfNewRelationTypeName);
		dataPanel.add(new JLabel(Messages.getString("extends")));
		dataPanel.add(cbRelationTypesCombo);
		
		// build panel for buttons
		JPanel buttonPanel = new JPanel(flayout,true);
		
		okButton = new JButton(Messages.getString("apply"));
		okButton.setActionCommand(APPLY);
		okButton.addActionListener(this);
		
		cancelButton = new JButton(Messages.getString("cancel"));
		cancelButton.setActionCommand(CANCEL);
		cancelButton.addActionListener(this);
		
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		add(dataPanel,BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.SOUTH);
	}

	private void loadCombosData(){
		Object selectedObject = cbRelationTypesCombo.getSelectedItem();
		cbRelationTypesCombo.removeAllItems();
		cbRelationTypesCombo = ComboBuilder.fillConnectorsCombo(cbRelationTypesCombo ,knowledgeBase, false,selectedObject);
	}
	public static void main(String[] args) throws Exception {
		IKnowledgeBase kb = KnowledgeBaseFactory.getInstance("test",KnowledgeBaseType.IN_MEMORY);
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Dialogy Explorer");
		NewConnectorPanel panel = new NewConnectorPanel(kb);
		//panel.seto
		frame.getContentPane().add(new JScrollPane(panel));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public String getNewRelationTypeName(){
		return tfNewRelationTypeName.getText();
	}
	
	public Connector getConnector(){
		Object object = cbRelationTypesCombo.getSelectedItem();
		return (Connector) object;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(APPLY)){
			DLogger.debug("Creating Connector " + getNewRelationTypeName() + " as a sub connector of " + getConnector() );
			try {
                knowledgeBase.createConnector(getConnector());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
		}
	}

	/** Observer implementation
	 */
	public void update(Observable o, Object updatedObject) {
		DLogger.debug("NewConnectorPanel : Knowledge base has been updated");
		// TODO use updated object to check if update is needed
		// Reload data
		updateCombos();
	}

	/**
	 * 
	 */
	private void updateCombos() {
		loadCombosData();
		// repaint
		cbRelationTypesCombo.repaint();
	}

}
