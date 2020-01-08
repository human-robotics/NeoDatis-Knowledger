/* 
 * $RCSfile: NewObjectPanel.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.gui.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.gui.ComboBuilder;
import org.neodatis.knowledger.gui.EntityWrapper;
import org.neodatis.knowledger.gui.component.SearchableComboBox;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.tool.DLogger;


/**
 * <p>
 * 
 * </p>
 * 
 */
public class NewObjectPanel extends JPanel implements ActionListener, Observer {
	public final static String CREATE_CONCEPT = "create-concept";
	public final static String CREATE_INSTANCE = "create-instance";
	public final static String CREATE_CONNECTOR = "create-connector";
	private final static String CREATE = "create";
	
	private ButtonGroup buttonGroupObjectType;
	private JRadioButton rbConceptType;
	private JRadioButton rbInstanceType;
	private JRadioButton rbConnector;
	private JTextField tfObjectIdentifier;
	
	private JComboBox cbObjectType;
	
	private JLabel lbRelationLabel;

	private JPanel specificPanelForInstance;
	private JPanel specificPanelForRelation;
	private JComboBox cbRelationIsBidirectional;
	private JPanel dataPanel;
	private JButton okButton;

	private IKnowledgeBase knowledgeBase;
	private String currentAction;
	private KnowledgerObject knowledgerObjectToSelect;

	public NewObjectPanel(IKnowledgeBase knowledgeBase,String action) throws Exception {
		super();
		this.knowledgeBase = knowledgeBase;
		knowledgeBase.addObserver(this);
		this.currentAction = action;
		init();
	}
	public NewObjectPanel(IKnowledgeBase knowledgeBase,String action,KnowledgerObject knowledgerObject) throws Exception {
		super();
		this.knowledgeBase = knowledgeBase;
		knowledgeBase.addObserver(this);
		this.currentAction = action;
		this.knowledgerObjectToSelect = knowledgerObject;
		init();
	}

	private void init() {

		buttonGroupObjectType = new ButtonGroup();
		rbConceptType = new JRadioButton(Messages.getString("concept"));
		rbConceptType.setActionCommand(CREATE_CONCEPT);
		rbConceptType.addActionListener(this);

		rbInstanceType = new JRadioButton(Messages.getString("instance"));
		rbInstanceType.setActionCommand(CREATE_INSTANCE);
		rbInstanceType.addActionListener(this);

		rbConnector = new JRadioButton(Messages.getString("connector"));
		rbConnector.setActionCommand(CREATE_CONNECTOR);
		rbConnector.addActionListener(this);

		buttonGroupObjectType.add(rbConceptType);
		buttonGroupObjectType.add(rbInstanceType);
		buttonGroupObjectType.add(rbConnector);
	
		JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		radioPanel.add(rbConceptType);
		radioPanel.add(rbInstanceType);
		radioPanel.add(rbConnector);
	
		tfObjectIdentifier = new JTextField(10);
		cbObjectType = new SearchableComboBox();
		
		dataPanel = new JPanel(new BorderLayout());
		
		cbObjectType.addItem("");
		cbObjectType.addItem("This combo is empty!!!!!!!");
		cbObjectType.addItem("");
		cbObjectType.addItem("This combo is empty!!!!!!!");

		
		lbRelationLabel = new JLabel("relation.name");


		JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		pane.add(tfObjectIdentifier);
		pane.add(lbRelationLabel);
		pane.add(cbObjectType);

		dataPanel.add(pane, BorderLayout.NORTH);

		setLayout(new BorderLayout(5, 5));
		add(radioPanel, BorderLayout.NORTH);
		add(dataPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		okButton = new JButton(Messages.getString("create"));
		okButton.setActionCommand(CREATE);
		okButton.addActionListener(this);

		buttonPanel.add(okButton);

		add(buttonPanel, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(400, 150));

		specificPanelForInstance = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		specificPanelForRelation = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

		cbRelationIsBidirectional = new JComboBox();
		cbRelationIsBidirectional.addItem(Messages.getString("no"));
		cbRelationIsBidirectional.addItem(Messages.getString("yes"));

		specificPanelForRelation.add(new JLabel(Messages.getString("bidirectional")));
		specificPanelForRelation.add(cbRelationIsBidirectional);
		
		if(currentAction!=null){
			if(currentAction.equals(CREATE_CONCEPT)){
				selectCreateConceptMode();
			}
			if(currentAction.equals(CREATE_INSTANCE)){
				selectCreateInstanceMode();
			}
			if(currentAction.equals(CREATE_CONNECTOR)){
				selectCreateConnectorMode();
			}
		}
	}

	private String getNewObjectIdentifier() {
		return tfObjectIdentifier.getText();
	}

	private Entity getObject() {
		return (Entity) ((EntityWrapper)cbObjectType.getSelectedItem()).getDialoyObject();
	}

	private boolean relationIsBidirectional() {
		return cbRelationIsBidirectional.getSelectedItem().toString().equals(Messages.getString("yes"));
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		System.out.println(e.getActionCommand());
		boolean somethingHasChanged = false;

		if (action.equals(CREATE_CONCEPT)) {
			selectCreateConceptMode();
			somethingHasChanged = true;
		}
		if (action.equals(CREATE_INSTANCE)) {
			selectCreateInstanceMode();
			somethingHasChanged = true;
		}
		if (action.equals(CREATE_CONNECTOR)) {
			selectCreateConnectorMode();
			somethingHasChanged = true;
		}

		if (action.equals(CREATE)) {
			
		    if(getObject().getIdentifier().length()==0){
		        JOptionPane.showMessageDialog(this,"Right part can not be empty","Error",JOptionPane.WARNING_MESSAGE);
		        return ;
		    }
		    DLogger.debug("creating " + buttonGroupObjectType.getSelection().getActionCommand());
			if (currentAction.equals(CREATE_CONCEPT)) {
				DLogger.debug("creating Concept " + getNewObjectIdentifier() + " as subclass of " + getObject().getIdentifier());
				Concept concept = null;
				try {
					concept = ((Concept) getObject()).getSubConcept(getNewObjectIdentifier());
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
			}
			if (currentAction.equals(CREATE_INSTANCE)) {
				DLogger.debug("creating instance " + getNewObjectIdentifier() + " as instance of " + getObject().getIdentifier());
				Instance instance = null;
				try {
					instance = ((Concept) getObject()).newInstance(getNewObjectIdentifier());
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
			}
			if (currentAction.equals(CREATE_CONNECTOR)) {
				DLogger.debug("creating relation type " + getNewObjectIdentifier() + " as sub connector of " + getObject().getIdentifier());
				
				try {
					Connector connector = knowledgeBase.getConnectorFromName(getNewObjectIdentifier(),GetMode.CREATE_IF_DOES_NOT_EXIST);
					knowledgeBase.createRelation(connector,knowledgeBase.getConnectorFromName(IKnowledgeBase.CONNECTOR_IS_SUB_RELATION_OF,GetMode.CREATE_IF_DOES_NOT_EXIST),getObject());
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
			}
			okButton.setText(Messages.getString("created!"));

		}

		if (somethingHasChanged) {
		    //dataPanel.repaint();
		}
	}

	/**
	 * 
	 */
	private void selectCreateConnectorMode() {
		currentAction = CREATE_CONNECTOR;
		lbRelationLabel.setText(Messages.getString("is-subrelation-of"));
		cbObjectType = ComboBuilder.fillObjectCombo(cbObjectType, knowledgeBase, false, knowledgerObjectToSelect, false, false, true);
		dataPanel.remove(specificPanelForInstance);
		dataPanel.remove(specificPanelForRelation);
		dataPanel.add(specificPanelForRelation, BorderLayout.CENTER);
		dataPanel.setSize(specificPanelForRelation.getSize());
		buttonGroupObjectType.setSelected(rbConnector.getModel(),true);

	}


/**
	 * 
	 */
	private void selectCreateInstanceMode() {
		currentAction = CREATE_INSTANCE;
		lbRelationLabel.setText(Messages.getString("is-instance-of"));
		cbObjectType = ComboBuilder.fillObjectCombo(cbObjectType, knowledgeBase, false, knowledgerObjectToSelect, true, false, false);
		dataPanel.remove(specificPanelForInstance);
		dataPanel.remove(specificPanelForRelation);
		dataPanel.add(specificPanelForInstance, BorderLayout.CENTER);
		dataPanel.setSize(specificPanelForInstance.getSize());
		buttonGroupObjectType.setSelected(rbInstanceType.getModel(),true);

	}

	/**
	 * 
	 */
	private void selectCreateConceptMode() {
		currentAction = CREATE_CONCEPT;
		lbRelationLabel.setText(Messages.getString("is-subclass-of"));
		cbObjectType = ComboBuilder.fillObjectCombo(cbObjectType, knowledgeBase, false, knowledgerObjectToSelect, true, false, false);
		dataPanel.remove(specificPanelForInstance);
		dataPanel.remove(specificPanelForRelation);
		dataPanel.add(specificPanelForInstance, BorderLayout.CENTER);
		dataPanel.setSize(specificPanelForRelation.getSize());
		buttonGroupObjectType.setSelected(rbConceptType.getModel(),true);
	}

	/** Observer implementation
	 */
	public void update(Observable o, Object updatedObject) {
		DLogger.debug("NewObjectPanel : Knowledge base has been updated");
		// TODO use updated object to check if update is needed
		// Reload data

		if (currentAction.equals(CREATE_CONCEPT)) {
			cbObjectType = ComboBuilder.fillObjectCombo(cbObjectType, knowledgeBase, false, getObject(), true, false, false);
		}
		if (currentAction.equals(CREATE_INSTANCE)) {
			cbObjectType = ComboBuilder.fillObjectCombo(cbObjectType, knowledgeBase, false, getObject(), true, false, false);
		}
		if (currentAction.equals(CREATE_CONNECTOR)) {
			cbObjectType = ComboBuilder.fillObjectCombo(cbObjectType, knowledgeBase, false, getObject(), false, false, true);
		}

		cbObjectType.repaint();
	}

	public static void display(KnowledgerObject knowledgerObjectToSelect,String mode) throws Exception{
		IKnowledgeBase kb = knowledgerObjectToSelect.getKnowledgeBase();

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Object Creator");
		NewObjectPanel panel = new NewObjectPanel(kb,mode, knowledgerObjectToSelect);
		frame.getContentPane().add(new JScrollPane(panel));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	    
	}
	public static void main(String[] args) throws Exception {
	    display(null,NewObjectPanel.CREATE_CONCEPT);
	}

}
