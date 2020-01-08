/* 
 * $RCSfile: NewInstancePanel.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.gui.ComboBuilder;
import org.neodatis.knowledger.gui.EntityWrapper;
import org.neodatis.knowledger.gui.component.SearchableComboBox;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.tool.DLogger;


public class NewInstancePanel extends JPanel implements ActionListener, Observer {
	public final static String CREATE_CONCEPT = "create-instance";

	private final static String CREATE = "create";
	private final static String CREATE_RELATION = "create-relation";

	private JComboBox cbObjectType;
	private JTextField tfInstanceValue;
	private JTextField tfInstanceName;	

	private JButton okButton;
	private JButton btCreateRelation;

	private KnowledgeBase knowledgeBase;

	private KnowledgerObject knowledgerObjectToSelect;
	private Instance createdInstance;

	public NewInstancePanel(KnowledgeBase knowledgeBase) throws Exception {
		super();
		this.knowledgeBase = knowledgeBase;
		knowledgeBase.addObserver(this);
		init();
	}

	public NewInstancePanel(KnowledgeBase knowledgeBase, KnowledgerObject knowledgerObject) throws Exception {
		super();
		this.knowledgeBase = knowledgeBase;
		knowledgeBase.addObserver(this);
		this.knowledgerObjectToSelect = knowledgerObject;
		init();
	}

	private void init() {

		setBorder(new EmptyBorder(10,10,10,10));
		tfInstanceValue = new JTextField(10);
		tfInstanceName = new JTextField(10);
		cbObjectType = new SearchableComboBox();
		cbObjectType = ComboBuilder.fillObjectCombo(cbObjectType, knowledgeBase, false, knowledgerObjectToSelect, true, false, false);

		JPanel pane = new JPanel(new GridLayout(3,2,5,5));
		
		pane.add(new JLabel(Messages.getString("create.an.instance.of")));
		pane.add(cbObjectType);
		pane.add(new JLabel(Messages.getString("optional.value(for terminal type)")));
		pane.add(tfInstanceValue);
		pane.add(new JLabel(Messages.getString("optional.name(instance.name)")));
		pane.add(tfInstanceName);

		setLayout(new BorderLayout(5, 5));
		JPanel buttonPanel = new JPanel();
		okButton = new JButton(Messages.getString("create"));
		okButton.setActionCommand(CREATE);
		okButton.addActionListener(this);
		
		btCreateRelation = new JButton("Connect instance to other objects");
		btCreateRelation.setActionCommand(CREATE_RELATION);
		btCreateRelation.addActionListener(this);
		btCreateRelation.setEnabled(false);

		buttonPanel.add(okButton);
		buttonPanel.add(btCreateRelation);

		add(new JLabel("<html><body>This panel is used to create a new <br>instance of a concept. Choose the concept<br>and set the value<HR></body></html>"),BorderLayout.NORTH);
		add(pane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

	}

	private String getNewObjectIdentifier() {
		return (tfInstanceValue.getText().length()>0?tfInstanceValue.getText():null);
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
		Concept type = null;

		if (action.equals(CREATE)) {

			if (getObject().getIdentifier().length() == 0) {
				JOptionPane.showMessageDialog(this, "Right part can not be empty", "Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
			DLogger.debug("creating Instance " + getNewObjectIdentifier() + " as instance of " + getObject().getIdentifier());
			try {
				type = (Concept) getObject();
				createdInstance = type.newInstance(getNewObjectIdentifier());
				if(tfInstanceName.getText().length()>0){
					String instanceName = tfInstanceName.getText();
					Concept name = type.getKnowledgeBase().getConceptFromName("Name",GetMode.CREATE_IF_DOES_NOT_EXIST);
					createdInstance.setProperty("name",name.newInstance(instanceName));
				}
				okButton.setText(Messages.getString("instance " + createdInstance.getId() + " created!"));
				btCreateRelation.setEnabled(true);
				btCreateRelation.setText("Connect instance " + createdInstance.getId() + " to other objects");
				repaint();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (action.equals(CREATE_RELATION)) {
			NewRelationPanel.display(knowledgeBase,createdInstance);
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
		DLogger.debug("NewInstancePanel : Knowledge base has been updated");
		// TODO use updated object to check if update is needed
		// Reload data

		cbObjectType = ComboBuilder.fillObjectCombo(cbObjectType, knowledgeBase, false, getObject(), true, false, false);
		cbObjectType.repaint();
	}

	public static void display(KnowledgerObject knowledgerObjectToSelect) throws Exception {
		KnowledgeBase kb = knowledgerObjectToSelect.getKnowledgeBase();

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Instance Creator");
		JPanel panel = new NewInstancePanel(kb, knowledgerObjectToSelect);
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] args) throws Exception {
		display(null);
	}

}
