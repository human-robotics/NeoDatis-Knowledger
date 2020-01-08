/* 
 * $RCSfile: SimpleQueryPanel.java,v $
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.neodatis.knowledger.core.factory.RelationCriteriaFactory;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.interfaces.entity.IRelationCriteria;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.gui.ComboBuilder;
import org.neodatis.knowledger.gui.EntityWrapper;
import org.neodatis.knowledger.gui.component.SearchableComboBox;


/**
 * <p>
 * 
 * </p>
 * 
 */
public class SimpleQueryPanel extends JPanel implements ActionListener {

	private static final String ACTION_SEARCH = "search";
	private static final String ACTION_GRAPH = "graph";
	private IKnowledgeBase knowledgeBase;
	private JComboBox cbLeftEntity;
	private JComboBox cbConnector;
	private JComboBox cbRightEntity;
	private JTextArea textArea;


	/**
     * @throws Exception
     * 
     */
    public SimpleQueryPanel(IKnowledgeBase knowledgeBase) throws Exception {
        super();
        init(knowledgeBase,null);
    }

	private void init(IKnowledgeBase knowledgeBase, Entity entity) throws Exception {
	    this.knowledgeBase = knowledgeBase;
		FlowLayout flayout = new FlowLayout(FlowLayout.CENTER, 5, 5);

		cbLeftEntity = new SearchableComboBox();
		cbRightEntity = new SearchableComboBox();
		cbConnector = new SearchableComboBox();
		
		loadCombosData(entity);

		JButton btSearch = new JButton(new ImageIcon("images/search.png"));
		btSearch.addActionListener(this);
		btSearch.setActionCommand(ACTION_SEARCH);
		
		JButton btGraph = new JButton(new ImageIcon("images/graph.png"));
		btGraph.addActionListener(this);
		btGraph.setActionCommand(ACTION_GRAPH);

		textArea = new JTextArea(5,20);
		
		JPanel comboPanel = new JPanel();
		comboPanel.add(cbLeftEntity);
		comboPanel.add(cbConnector);
		comboPanel.add(cbRightEntity);
		comboPanel.add(btSearch);
		comboPanel.add(btGraph);
		
		setLayout(new BorderLayout(10,10));
		add(comboPanel,BorderLayout.NORTH);
		add(new JScrollPane(textArea),BorderLayout.CENTER);
		
		
		
	}
	
	private void loadCombosData(Entity initialObject) {
		Entity selectedEntity = null; 
		JComboBox comboBox = null;
		
		if(initialObject!=null){
			selectedEntity = initialObject;
		}else{
			selectedEntity = (Entity) cbLeftEntity.getSelectedItem();
		}
		cbLeftEntity.removeAllItems();
		cbLeftEntity = ComboBuilder.fillObjectCombo(cbLeftEntity, knowledgeBase, false, null);
		
		cbRightEntity.removeAllItems();
		cbRightEntity = ComboBuilder.fillObjectCombo(cbRightEntity, knowledgeBase, false, null);

		selectedEntity = (Entity) cbConnector.getSelectedItem();
		cbConnector.removeAllItems();
		cbConnector = ComboBuilder.fillConnectorsCombo(cbConnector, knowledgeBase, false,selectedEntity);
		
	}

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Entity leftEntity = (Entity)((EntityWrapper)cbLeftEntity.getSelectedItem()).getDialoyObject(); 
        Connector connector = (Connector) ((EntityWrapper)cbConnector.getSelectedItem()).getDialoyObject();
        Entity rightEntity = (Entity)((EntityWrapper) cbRightEntity.getSelectedItem()).getDialoyObject();
        
        if(leftEntity!=null && leftEntity.getIdentifier() != null && leftEntity.getIdentifier().length()==0){
            leftEntity = null;
        }
        if(connector!=null &&  connector.getName().length()==0){
            connector = null;
        }
        if(rightEntity!=null && rightEntity.getIdentifier()!=null && rightEntity.getIdentifier().length()==0){
            rightEntity = null;
        }
        
        IRelationCriteria criteria = RelationCriteriaFactory.getRelationCriteria(leftEntity,connector,rightEntity);
        
        RelationList relations = null;
        
        try {
            relations = knowledgeBase.getRelationList(criteria);
            
            if(e.getActionCommand().equals(ACTION_SEARCH)){
            	textArea.setText(relations.toString());
            }else{
            	throw new RuntimeException("AnimateGraph not yet implemented");
            	//new AnimateGraph(relations);
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            textArea.setText("Error : " + e1.getMessage());
        }
        
        
        
    }
    
    public static void display(IKnowledgeBase knowledgeBase) throws Exception{
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Query");
		SimpleQueryPanel panel = new SimpleQueryPanel(knowledgeBase);
		frame.getContentPane().add(new JScrollPane(panel));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	    
	}
	public static void main(String[] args) throws Exception {
	    display(null);
	}

}
