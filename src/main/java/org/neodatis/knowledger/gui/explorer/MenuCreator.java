/* 
 * $RCSfile: MenuCreator.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.gui.explorer;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.neodatis.knowledger.core.implementation.criteria.RelationConnectorCriteria;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.implementation.entity.KObject;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.tool.DLogger;


/**
 * <p>
 * 
 * </p>
 *  
 */
public class MenuCreator {
	
	private ActionListener actionListener;
	/**
	 * 
	 * @param object
	 * @return
	 */
	public JPopupMenu createPopupForConcept(ActionListener actionListener , KnowledgerObject object) {

		this.actionListener=actionListener;
		
		JMenuItem menuItem = null;

		String objectName = object.getIdentifier();

		JPopupMenu popupMenu = new JPopupMenu();
		menuItem = new JMenuItem(objectName);
		popupMenu.add(menuItem);

		menuItem = buildMenuItem( KnowledgeExplorerTree.NEW_SUB_CONCEPT,Messages.getString("new.concept") , object);
		popupMenu.add(menuItem);

		menuItem = buildMenuItem( KnowledgeExplorerTree.NEW_INSTANCE,Messages.getString("new.instance") , object);
		popupMenu.add(menuItem);

		menuItem = buildMenuItem( KnowledgeExplorerTree.NEW_RELATION,Messages.getString("new.relation") , object);
		popupMenu.add(menuItem);

		popupMenu.addSeparator();
		
		menuItem = buildMenuItem( KnowledgeExplorerTree.DELETE_CONCEPT,Messages.getString("delete.concept") , object);
		popupMenu.add(menuItem);

		menuItem = buildMenuItem( KnowledgeExplorerTree.DELETE_RELATION,Messages.getString("delete.relation.with.father") , object);
		popupMenu.add(menuItem);

		return popupMenu;
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	public JPopupMenu createPopupForInstance(ActionListener actionListener , KnowledgerObject object) {

		Instance instance = (Instance) object;
		this.actionListener=actionListener;
		
		JMenuItem menuItem = null;
		JMenu propertiesMenu = null;
		JMenu onePropertyMenu = null;

		String objectName = object.getIdentifier();

		JPopupMenu popupMenu = new JPopupMenu();
		menuItem = new JMenuItem(objectName);
		popupMenu.add(menuItem);

        propertiesMenu = new JMenu(Messages.getString("Properties"));
        
        
        
        ICriteria criteria = new RelationConnectorCriteria(IKnowledgeBase.CONNECTOR_IS_SUB_CLASS_OF).not().and(new RelationConnectorCriteria(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF).not());
        RelationList relationList = null;
		try {
			relationList = instance.getConcept().getRelationList().filter(criteria);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Relation relation = null;
        String connectorName = null;
        for(int r=0;r<relationList.size();r++){
        	relation = relationList.getRelation(r);
        	connectorName = relation.getConnectorAlias()==null?relation.getConnector().getIdentifier():relation.getConnectorAlias();
        	onePropertyMenu = new JMenu(Messages.getString("set") + " " + connectorName);
        	propertiesMenu.add(onePropertyMenu);
        	
        	Entity rightPart = relation.getRightEntity();
        	if(rightPart.isConcept()){
        		Concept concept = (Concept) rightPart;
        		InstanceList instanceList = null;
				try {
					instanceList = concept.getInstances();
	        		for(int i=0;i<instanceList.size();i++){
	        			Instance oneInstance = instanceList.getInstance(i);
	        			DLogger.info("connector name = " + connectorName);
	        			Connector connector = object.getKnowledgeBase().getConnectorFromName(connectorName,GetMode.CREATE_IF_DOES_NOT_EXIST);
	        			onePropertyMenu.add(buildMenuItem(KnowledgeExplorerTree.CREATE_RELATION,oneInstance.getIdentifier(),new Relation(object.getKnowledgeBase(),object,connector,oneInstance)));
	        		}
	        		onePropertyMenu.add(buildMenuItem(KnowledgeExplorerTree.NEW_INSTANCE,Messages.getString("create.a.new") + " " + concept.getIdentifier(),concept));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
        }

        popupMenu.add(propertiesMenu);

        menuItem = buildMenuItem( KnowledgeExplorerTree.NEW_RELATION,Messages.getString("new.relation") , object);
		popupMenu.add(menuItem);

		popupMenu.addSeparator();
		
		menuItem = buildMenuItem( KnowledgeExplorerTree.DELETE_RELATION,Messages.getString("delete.relation.with.father") , object);
		popupMenu.add(menuItem);

		menuItem = buildMenuItem( KnowledgeExplorerTree.DELETE_INSTANCE,Messages.getString("delete") , object);
		popupMenu.add(menuItem);

		return popupMenu;
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	public JPopupMenu createPopupForRelation(ActionListener actionListener , Relation relation) {

		this.actionListener=actionListener;
		
		JMenuItem menuItem = null;

		JPopupMenu popupMenu = new JPopupMenu();

		menuItem = buildMenuItem( KnowledgeExplorerTree.GO_TO,Messages.getString("go.to") + " " + relation.getRightEntity(), relation.getRightEntity());
		popupMenu.add(menuItem);

        menuItem = buildMenuItem( KnowledgeExplorerTree.GO_TO,Messages.getString("go.to") + " " + relation.getLeftEntity(), relation.getLeftEntity());
        popupMenu.add(menuItem);

        menuItem = buildMenuItem( KnowledgeExplorerTree.DELETE_RELATION,Messages.getString("delete.relation.with.father") , relation);
		popupMenu.add(menuItem);

		popupMenu.addSeparator();
		
		menuItem = buildMenuItem( KnowledgeExplorerTree.DELETE_CONCEPT,Messages.getString("delete.relation") , relation);
		popupMenu.add(menuItem);

		return popupMenu;
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	public JPopupMenu createPopupForConnector(ActionListener actionListener , Connector connector) {

		this.actionListener=actionListener;
		
		JMenuItem menuItem = null;

		String objectName = connector.getName();

		JPopupMenu popupMenu = new JPopupMenu();
		menuItem = new JMenuItem(objectName);
		popupMenu.add(menuItem);

		menuItem = buildMenuItem( KnowledgeExplorerTree.NEW_CONNECTOR,Messages.getString("new.connector.type") , connector);
		popupMenu.add(menuItem);
		
		menuItem = buildMenuItem( KnowledgeExplorerTree.NEW_RELATION,Messages.getString("new.relation") , connector);
		popupMenu.add(menuItem);

		

		popupMenu.addSeparator();

		menuItem = buildMenuItem( KnowledgeExplorerTree.DELETE_RELATION,Messages.getString("delete.relation.with.father") , connector);
		popupMenu.add(menuItem);

		menuItem = buildMenuItem( KnowledgeExplorerTree.DELETE_CONNECTOR,Messages.getString("delete") , connector);
		popupMenu.add(menuItem);

		return popupMenu;
	}
	
	/**
	 * @param string
	 * @param object
	 * @return
	 */
	private JMenuItem buildMenuItem(int action ,String label, KnowledgerObject object) {
		JMenuItem menuItem = new JMenuItem(label);
		menuItem.setActionCommand(""+action);
		menuItem.putClientProperty(KnowledgeExplorerTree.OBJECT_KEY,object);
		menuItem.addActionListener(actionListener);
		return menuItem;
	}
	

}