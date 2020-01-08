/* 
 * $RCSfile: KnowledgerTreeRenderer.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.gui.explorer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.entity.LazyRelation;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.gui.explorer.node.DialogyTreeNode;
import org.neodatis.knowledger.gui.tool.ToolsForGui;



/**
 * <p>
 * 
 * </p>
 * 
 */
public class KnowledgerTreeRenderer extends DefaultTreeCellRenderer {
	
	
	Icon conceptIcon = ToolsForGui.createWebImageIcon("/images/conceptIcon.gif");
	Icon instanceIcon = ToolsForGui.createWebImageIcon("/images/instanceIcon.gif");
	Icon valueOfInstanceIcon = ToolsForGui.createWebImageIcon("/images/valueIcon.gif");
	Icon relationIcon = ToolsForGui.createWebImageIcon("/images/relationIcon.gif");
	Icon connectorIcon = ToolsForGui.createWebImageIcon("/images/connectorIcon.gif");

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(
		JTree tree,
		Object value,
		boolean sel,
		boolean expanded,
		boolean leaf,
		int row,
		boolean hasFocus) {
		// TODO Auto-generated method stub
		super.getTreeCellRendererComponent(
			tree,
			value,
			sel,
			expanded,
			leaf,
			row,
			hasFocus);
		
		if(value instanceof DialogyTreeNode){
			KnowledgerObject knowledgerObject = (KnowledgerObject) ((DialogyTreeNode)value).getUserObject();
		    
			if(knowledgerObject.getClass() == Concept.class){
				setIcon(conceptIcon);
				setToolTipText("concept - " + knowledgerObject.getId());
				return this;
			}
			if(knowledgerObject.getClass() == Instance.class){
				setIcon(instanceIcon);
				Instance instance = (Instance) knowledgerObject;
				setToolTipText("instance - " + knowledgerObject.getId() + " - value = " + instance.getValue());
				return this;
			}
			if(knowledgerObject.getClass() == Connector.class){
				setIcon(connectorIcon);
				setToolTipText("connector - " + knowledgerObject.getId());
				return this;
			}
			if(knowledgerObject.getClass() == Relation.class || knowledgerObject.getClass() == LazyRelation.class){
				setIcon(relationIcon);
				setToolTipText("relation - " + knowledgerObject.getId());
				return this;
			}
			setIcon(valueOfInstanceIcon);
			setToolTipText("value - " + knowledgerObject);
			return this;
		}
		return this;
	}

}
