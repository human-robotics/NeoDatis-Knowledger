/* 
 * $RCSfile: PopupListener.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.gui.explorer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.entity.LazyRelation;
import org.neodatis.knowledger.core.implementation.entity.Relation;



/**
 * <p>
 * 
 * </p>
 * 
 */
public class PopupListener extends MouseAdapter {
	private KnowledgeExplorerTree knowledgeExplorer;
	
	    PopupListener(KnowledgeExplorerTree knowledgeExplorer) {
	    	super();
	    	this.knowledgeExplorer=knowledgeExplorer;
	    }

	    public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {

	        Object object = null;
        	//System.out.println("Click on " + e.getComponent().getClass());
        	/*
	        if(e.getComponent().getClass() == JTree.class){
        	    JTree tree = (JTree) e.getComponent();
        	    System.out.println( tree.getLastSelectedPathComponent()  + " / " + tree.getLastSelectedPathComponent().getClass().getName());
        	    object = tree.getLastSelectedPathComponent();
        	}
        	*/
	        if (e.isPopupTrigger()) {
	        	System.out.println("Popup ok");
	        	if(e.getComponent().getClass() == JTextArea.class){
	        		JTextArea area = (JTextArea) e.getComponent();
	        		//new MenuCreator().createPopup(new Concept(area.getText())).show(e.getComponent(),e.getX(), e.getY());
	        	}
	        	
	        	if(e.getComponent().getClass() == JTree.class){
	        		JTree tree = (JTree) e.getComponent();
					object =  tree.getLastSelectedPathComponent();
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
					
					if(node.getUserObject().getClass() == Concept.class){
						new MenuCreator().createPopupForConcept(knowledgeExplorer,(KnowledgerObject) node.getUserObject()).show(e.getComponent(),e.getX(), e.getY());
						return;
					}
					
					if(node.getUserObject().getClass()== Instance.class){
						new MenuCreator().createPopupForInstance(knowledgeExplorer,(KnowledgerObject) node.getUserObject()).show(e.getComponent(),e.getX(), e.getY());
						return;
					}

					if(node.getUserObject().getClass()== Relation.class || node.getUserObject().getClass() == LazyRelation.class){
						new MenuCreator().createPopupForRelation(knowledgeExplorer,(Relation) node.getUserObject()).show(e.getComponent(),e.getX(), e.getY());
						return;
					}

					if(node.getUserObject().getClass()== Connector.class){
						new MenuCreator().createPopupForConnector(knowledgeExplorer,(Connector)node.getUserObject()).show(e.getComponent(),e.getX(), e.getY());
						return;
					}

					System.out.println("not implemented");
	        	}
	        }
	    }

}
