/* 
 * $RCSfile: NewConceptPanel.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.gui.ComboBuilder;
import org.neodatis.knowledger.gui.EntityWrapper;
import org.neodatis.knowledger.gui.component.SearchableComboBox;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.tool.DLogger;


public class NewConceptPanel extends JPanel implements ActionListener, Observer {
	public final static String CREATE_CONCEPT = "create-concept";

	private final static String CREATE = "create";

	private JComboBox cbObjectType;

	private JTextField tfObjectIdentifier;

	private JButton okButton;

	private IKnowledgeBase knowledgeBase;

	private KnowledgerObject knowledgerObjectToSelect;

	public NewConceptPanel(IKnowledgeBase knowledgeBase) throws Exception {
		super();
		this.knowledgeBase = knowledgeBase;
		knowledgeBase.addObserver(this);
		init();
	}

	public NewConceptPanel(IKnowledgeBase knowledgeBase, KnowledgerObject knowledgerObject) throws Exception {
		super();
		this.knowledgeBase = knowledgeBase;
		knowledgeBase.addObserver(this);
		this.knowledgerObjectToSelect = knowledgerObject;
		init();
	}

	private void init() {

		setBorder(new EmptyBorder(10,10,10,10));
		tfObjectIdentifier = new JTextField(10);
		cbObjectType = new SearchableComboBox();
		cbObjectType = ComboBuilder.fillObjectCombo(cbObjectType, knowledgeBase, false, knowledgerObjectToSelect, true, false, false);

		JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		pane.add(tfObjectIdentifier);
		pane.add(new JLabel(IKnowledgeBase.CONNECTOR_IS_SUB_CLASS_OF));
		pane.add(cbObjectType);

		setLayout(new BorderLayout(5, 5));
		JPanel buttonPanel = new JPanel();
		okButton = new JButton(Messages.getString("create"));
		okButton.setActionCommand(CREATE);
		okButton.addActionListener(this);

		buttonPanel.add(okButton);

		add(new JLabel("<html><body>This panel is used to create a new <br>concept as sub concept of another one<HR></body></html>"),BorderLayout.NORTH);
		add(pane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

	}

	private String getNewObjectIdentifier() {
		return tfObjectIdentifier.getText();
	}

	private Entity getObject() {
		return (Entity) ((EntityWrapper) cbObjectType.getSelectedItem()).getDialoyObject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		System.out.println(e.getActionCommand());
		boolean somethingHasChanged = false;

		if (action.equals(CREATE)) {

			if (getObject().getIdentifier().length() == 0) {
				JOptionPane.showMessageDialog(this, "Right part can not be empty", "Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
			DLogger.debug("creating Concept " + getNewObjectIdentifier() + " as subclass of " + getObject().getIdentifier());
			Concept concept = null;
			try {
				concept = ((Concept) getObject()).getSubConcept(getNewObjectIdentifier());
				okButton.setText(Messages.getString("created!"));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (somethingHasChanged) {
			// dataPanel.repaint();
		}
	}

	/**
	 * 
	 */
	

	/**
	 * Observer implementation
	 */
	public void update(Observable o, Object updatedObject) {
		DLogger.debug("NewObjectPanel : Knowledge base has been updated");
		// TODO use updated object to check if update is needed
		// Reload data

			cbObjectType = ComboBuilder.fillObjectCombo(cbObjectType, knowledgeBase, false, getObject(), true, false, false);
		cbObjectType.repaint();
	}

	public static void display(KnowledgerObject knowledgerObjectToSelect) throws Exception {
		IKnowledgeBase kb = knowledgerObjectToSelect.getKnowledgeBase();

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Concept Creator");
		JPanel panel = new NewConceptPanel(kb, knowledgerObjectToSelect);
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] args) throws Exception {
		display(null);
	}

}
