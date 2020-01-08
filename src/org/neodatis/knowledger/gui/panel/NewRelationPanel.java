/* 
 * $RCSfile: NewRelationPanel.java,v $
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
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.factory.PropositionFactory;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.entity.Proposition;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
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
public class NewRelationPanel extends JPanel implements ActionListener, Observer {
	
	private KnowledgeBase knowledgeBase;
	private JComboBox cbLeftEntity;
	private JComboBox cbConnector;
	//private JComboBox cbRightPart;
	List rightPartCombos;
	private JComboBox cbIsOptional;
	private JComboBox cbReliability;
	private JTextField tfMinCardinality;
	private JTextField tfMaxCardinality;
	private JTextField tfConnectorAlias;
	
	private Entity leftEntityToSelect;
	private Connector connectorToSelect;
	private Entity rightEntityToSelect;

	
	private JButton newRelationTypeButton;
	private JButton newInstanceButton;
	private JButton newConceptButton;
	private JButton okButton;
	private JButton cancelButton;
	private JButton addRightPartButton;
	private JPanel dataPanel;
	private JPanel optionsPanel;
	private JList rightParts;
	private JLabel relationLabel;
	private DefaultListModel rightPartsModel;

	public static String ACTION_ADD_RIGHT_PART = "add-right-part";
	public static String ACTION_LEFT_PART_HAS_CHANGED = "left-part-has-changed";
	public static String ACTION_CONNECTOR_HAS_CHANGED = "connector-has-changed";
	
	public static String ACTION_NEW_RELATION_TYPE = "new-relation-type";
	public static String ACTION_NEW_INSTANCE = "new-instance";
	public static String ACTION_NEW_CONCEPT = "new-concept";
	public static String ACTION_CREATE = "apply";
	public static String ACTION_CANCEL = "cancel";

	/**Creates a new relation between the the incoming object (defaulting as left part) 
	 * 
	 * @param knowledgeBase
	 */
	public NewRelationPanel(KnowledgeBase knowledgeBase, KnowledgerObject knowledgerObject) {
		super();
		this.knowledgeBase = knowledgeBase;
		knowledgeBase.addObserver(this);
		init(knowledgerObject);
	}

	/**Creates a new relation
	 * 
	 * @param knowledgeBase
	 */
	public NewRelationPanel(KnowledgeBase knowledgeBase) {
		this(knowledgeBase,null);
	}

	private void init(KnowledgerObject knowledgerObject) {
		FlowLayout flayout = new FlowLayout(FlowLayout.CENTER, 5, 5);

		cbLeftEntity = new SearchableComboBox();
		cbConnector = new SearchableComboBox();
		
		cbLeftEntity.addActionListener(this);
		cbLeftEntity.setActionCommand(ACTION_LEFT_PART_HAS_CHANGED);

		cbConnector.addActionListener(this);
		cbConnector.setActionCommand(ACTION_CONNECTOR_HAS_CHANGED);

		
		addRightPartButton = new JButton("+");
		addRightPartButton.setActionCommand(ACTION_ADD_RIGHT_PART);
		addRightPartButton.addActionListener(this);

		loadCombosData(knowledgerObject);
		// Set panel layout
		setLayout(new BorderLayout());

		// Options 
		optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT),true);
		
		cbIsOptional = new JComboBox(new String[]{"yes","no"});
		String [] reliabilityValues = {"1-Low","2-Medium","3-High"};
		cbReliability = new JComboBox(reliabilityValues);
		tfMinCardinality = new JTextField("1",2);
		tfMaxCardinality = new JTextField("1",2);
		tfConnectorAlias = new JTextField("",10);
		
		optionsPanel.add(new JLabel(Messages.getString("optional")));
		optionsPanel.add(cbIsOptional);
		optionsPanel.add(new JLabel(Messages.getString("reliability")));
		optionsPanel.add(cbReliability);
		
		optionsPanel.add(new JLabel(Messages.getString("min.Cardinality")));
		optionsPanel.add(tfMinCardinality);
		
		optionsPanel.add(new JLabel(Messages.getString("max.Cardinality")));
		optionsPanel.add(tfMaxCardinality);

		optionsPanel.add(new JLabel(Messages.getString("alias")));
		optionsPanel.add(tfConnectorAlias);
//
		
		relationLabel = new JLabel("?");
		//String [] values = {"olivier"};//,"aisa","karine","kiko"};
		rightPartsModel = new DefaultListModel();
		rightParts = new JList(rightPartsModel);
		rightParts.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		rightParts.setLayoutOrientation(JList.VERTICAL);
		
		//rightParts.setVisibleRowCount(2);
		
		JScrollPane listScroller = new JScrollPane(rightParts);
		listScroller.setPreferredSize(new Dimension(150, 60));
		// build a panel for data
		dataPanel = new JPanel(flayout, true);

		dataPanel.add(cbLeftEntity);
		dataPanel.add(cbConnector);
		dataPanel.add(listScroller);
		dataPanel.add(addRightPartButton);

		// build panel for buttons
		JPanel buttonPanel = new JPanel(flayout, true);

		newRelationTypeButton = new JButton(Messages.getString("new.connector"));
		newRelationTypeButton.setActionCommand(ACTION_NEW_RELATION_TYPE);
		newRelationTypeButton.addActionListener(this);

		newConceptButton = new JButton(Messages.getString("new.concept"));
		newConceptButton.setActionCommand(ACTION_NEW_CONCEPT);
		newConceptButton.addActionListener(this);
		
		newInstanceButton = new JButton(Messages.getString("new.instance"));
		newInstanceButton.setActionCommand(ACTION_NEW_INSTANCE);
		newInstanceButton.addActionListener(this);


		okButton = new JButton(Messages.getString("create"));
		okButton.setActionCommand(ACTION_CREATE);
		okButton.addActionListener(this);

		cancelButton = new JButton(Messages.getString("cancel"));
		cancelButton.setActionCommand(ACTION_CANCEL);
		cancelButton.addActionListener(this);

		
		buttonPanel.add(newConceptButton);
		buttonPanel.add(newInstanceButton);
		buttonPanel.add(newRelationTypeButton);
		buttonPanel.add(new JLabel(" - "));
		//buttonPanel.add(okButton);
		//buttonPanel.add(cancelButton);
		JPanel createButttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		createButttonPanel.add(okButton);

		JPanel panel = new JPanel(new BorderLayout(20,20));
		panel.setBorder(new CompoundBorder(new EmptyBorder(10,10,10,10),new EtchedBorder(EtchedBorder.LOWERED)));
		panel.add(createButttonPanel,BorderLayout.NORTH);
		panel.add(optionsPanel,BorderLayout.SOUTH);
		panel.add(dataPanel,BorderLayout.CENTER);
		
		add(panel,BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * 
	 */
	private void loadCombosData(KnowledgerObject initialObject) {
		KnowledgerObject selectedObject = null; 
		JComboBox comboBox = null;
		
		if(initialObject!=null){
			selectedObject = initialObject;
		}else{
		    if(cbLeftEntity.getSelectedItem()!=null){
				System.out.println("NewRelationPanel : " + cbLeftEntity.getSelectedItem() + " - " + cbLeftEntity.getSelectedItem().getClass().getName());
				selectedObject = ((EntityWrapper)cbLeftEntity.getSelectedItem()).getDialoyObject();
		    }
		}
		cbLeftEntity.removeAllItems();
		cbLeftEntity = ComboBuilder.fillObjectCombo(cbLeftEntity, knowledgeBase, false, selectedObject);
		
		if( cbConnector.getSelectedItem()!=null && !((EntityWrapper)cbConnector.getSelectedItem()).isEmpty()){
			selectedObject = (KnowledgerObject) getConnector();
		}else{
			selectedObject = null;
		}
		
		cbConnector.removeAllItems();
		cbConnector = ComboBuilder.fillConnectorsCombo(cbConnector, knowledgeBase, false,selectedObject);
		
	}

	
	public static void display(KnowledgeBase knowledgeBase, Entity entity){

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Instance Creator");
		JPanel panel = new NewRelationPanel(knowledgeBase,entity);
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	public static void main(String[] args) throws Exception {
		KnowledgeBase kb = KnowledgeBaseFactory.getInstance("test",KnowledgeBaseType.IN_MEMORY);

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Relation Creator");
		NewRelationPanel panel = new NewRelationPanel(kb);
		//panel.seto
		frame.getContentPane().add(new JScrollPane(panel));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public List getLeftParts() {
		EntityWrapper ew = null;
	    ew = (EntityWrapper) cbLeftEntity.getSelectedItem();
		List list = new ArrayList();
		list.add(ew.getDialoyObject());
		
		return list;
	}
	
	public List getRightParts() {
		List list = new ArrayList();
		EntityWrapper ew = null;
		
		if(rightPartsModel!=null){
			for(int i=0;i<rightPartsModel.getSize();i++){
				ew = (EntityWrapper) rightPartsModel.get(i);
				if(ew.isNotEmpty()){
					list.add(ew.getEntity());
				}
			}
		}		
		return list;
	}
	public Connector getConnector() {
		EntityWrapper wrapper = (EntityWrapper) cbConnector.getSelectedItem();
		return (Connector) wrapper.getDialoyObject();
	}

	public boolean isOptional(){
		return cbIsOptional.getSelectedItem().toString().equals("yes");
	}
	public int getReliability(){
		return Integer.parseInt(cbReliability.getSelectedItem().toString());
	}
	public int getMinCardinality(){
		return Integer.parseInt(tfMinCardinality.getText());
	}
	public int getMaxCardinality(){
		return Integer.parseInt(tfMaxCardinality.getText());
	}
	public String getConnectorAlias(){
		String alias = tfConnectorAlias.getText();
		if(alias==null || alias.length()==0){
			return null;
		}
		return alias;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String windowTitle = null;
		NewObjectPanel newObjectPanel = null;
		boolean createFrame = false;

		if (e.getActionCommand().equals(ACTION_CREATE)) {
			DLogger.debug("Creating Relation " + getLeftParts() + " " + getConnector() + " " + getRightParts());
			Relation relation = new Relation(knowledgeBase,(Entity)getLeftParts().get(0),getConnector(),getConnectorAlias(),(Entity)getRightParts().get(0),getMinCardinality(),getMaxCardinality(),isOptional()); 

			Proposition proposition = PropositionFactory.create(relation);
			try {


			    knowledgeBase.createProposition(proposition);
			    knowledgeBase.createRelation(relation);
//                new KnowledgeUnit() , getLeftParts(), getConnector(), getRightParts(),isMandatory(),getReliability()
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
		}

		if (e.getActionCommand().equals(ACTION_ADD_RIGHT_PART)) {
			addRightPart();
		}

		if (e.getActionCommand().equals(ACTION_NEW_RELATION_TYPE)) {
			windowTitle = Messages.getString("relationType.creator");
			try {
                newObjectPanel = new NewObjectPanel(knowledgeBase,NewObjectPanel.CREATE_CONNECTOR);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
			createFrame = true;
		}

		if (e.getActionCommand().equals(ACTION_NEW_CONCEPT)) {
			windowTitle = Messages.getString("concept.creator");
			try {
                newObjectPanel = new NewObjectPanel(knowledgeBase,NewObjectPanel.CREATE_CONCEPT);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
			createFrame = true;
		}
		if (e.getActionCommand().equals(ACTION_CONNECTOR_HAS_CHANGED) || e.getActionCommand().equals(ACTION_LEFT_PART_HAS_CHANGED)) {
			updateRelationDescription();
		}

		if (e.getActionCommand().equals(ACTION_NEW_INSTANCE)) {
			windowTitle = Messages.getString("instance.creator");
			try {
                newObjectPanel = new NewObjectPanel(knowledgeBase,NewObjectPanel.CREATE_INSTANCE);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
			createFrame = true;
		}

		if (createFrame) {
			JFrame frame = new JFrame(Messages.getString("relationType.creator"));
			frame.getContentPane().add(new JScrollPane(newObjectPanel));
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		}

	}

	private void addRightPart() {
		List c = new ArrayList();
		c.addAll(knowledgeBase.getConceptList());
		c.addAll(knowledgeBase.getInstanceList());
		c.addAll(knowledgeBase.getConnectorList());
		EntityWrapper [] entities = new EntityWrapper[c.size()];
		for(int i=0;i<c.size();i++){
			entities[i] = new EntityWrapper((KnowledgerObject)c.get(i));
		}
		EntityWrapper entityWrapper = (EntityWrapper)JOptionPane.showInputDialog(
		                    this,
		                    "Choose an entity to connect to ","choose",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    entities,""
		                    );
		if(entityWrapper!=null){
			rightPartsModel.addElement(entityWrapper);
		}
		updateRelationDescription();
	}

	private void updateRelationDescription() {
		
		if(okButton==null){
			return;
		}
		String leftLabel = "?";
		String connectorLabel = "?";
		StringBuffer rightLabel = new StringBuffer("?");
		
		StringBuffer buffer = new StringBuffer();
		
		EntityWrapper left = (EntityWrapper) cbLeftEntity.getSelectedItem();
		if(left!=null && left.isNotEmpty()){
			leftLabel = left.getEntity().toString();
		}
		
		EntityWrapper connector = (EntityWrapper) cbConnector.getSelectedItem();
		if(connector!=null && connector.isNotEmpty()){
			connectorLabel = connector.getEntity().toString();
		}
				
		EntityWrapper right = null;
		if(rightPartsModel!=null){
			rightLabel.delete(0,1);
			for(int i=0;i<rightPartsModel.getSize();i++){
				right = (EntityWrapper) rightPartsModel.get(i);
				if(right.isNotEmpty()){
					if(i!=0){rightLabel.append(" , ");}
					rightLabel.append(right.getEntity().toString());
				}
			}
		}
		
		buffer.append(leftLabel).append(" ").append( connectorLabel).append(" ").append(rightLabel);
		
		//relationLabel.setText(buffer.toString());
		okButton.setText(Messages.getString("create ") + buffer.toString());
	}

	/** Observer implementation
	 */
	public void update(Observable o, Object updatedObject) {
		DLogger.debug("NewRelationPanel : Knowledge base has been updated");
		// TODO use updated object to check if update is needed

		// Reload data
		updateCombos();
	}

	/**
	 * 
	 */
	private void updateCombos() {
		loadCombosData(null);
		// repaint
		cbLeftEntity.repaint();
		cbConnector.repaint();
	}

}
