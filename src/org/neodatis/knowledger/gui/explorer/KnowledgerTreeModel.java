/* 
 * $RCSfile: KnowledgerTreeModel.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.explorer;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.ConceptList;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;


/**
 * <p>
 * 
 * </p>
 * 
 */
public class KnowledgerTreeModel extends DefaultTreeModel implements Observer {

	private static final String ROOT_CONCEPT_NAME = "Object";

	private static final String ROOT_CONNECTOR_NAME = "is-connected-to";

	private static final String CONCEPT_NODE_NAME = "Concepts&Instances";

	private static final String CONNECTOR_NODE_NAME = "Connectors";

	private static final String LOST_ENTITIES_NODE_NAME = "Lost entities";

	private IKnowledgeBase knowledgeBase;

	/*
	 * protected DialogyTreeNode conceptualRootNode; protected DialogyTreeNode
	 * connectorRootNode; protected DialogyTreeNode lostEntitiesRootNode;
	 */
	private List basicNodes;

	public KnowledgerTreeModel(IKnowledgeBase knowledgeBase) {
		super(new DefaultMutableTreeNode("Knowledger"));
		System.out.println("Root node is " + getRoot().getClass().getName());
		this.knowledgeBase = knowledgeBase;
		//this.knowledgeBase.addObserver(this);

		// conceptualRootNode = new InformationTreeNode(CONCEPT_NODE_NAME);
		// connectorRootNode = new InformationTreeNode(CONNECTOR_NODE_NAME);
		// lostEntitiesRootNode = new
		// InformationTreeNode(LOST_ENTITIES_NODE_NAME);
		basicNodes = new ArrayList();
		basicNodes.add(CONCEPT_NODE_NAME);
		basicNodes.add(CONNECTOR_NODE_NAME);
		basicNodes.add(LOST_ENTITIES_NODE_NAME);

	}

	// ////////////// Fire events //////////////////////////////////////////////

	/**
	 * The only event raised by this model is TreeStructureChanged with the root
	 * as path, i.e. the whole tree has changed.
	 */
	protected void fireTreeStructureChanged() {
		int len = getTreeModelListeners().length;
		System.out.println("Calling listeners");
		TreeModelEvent e = new TreeModelEvent(this, getPathToRoot((TreeNode) getRoot()));
		for (int i = 0; i < len; i++) {
			((TreeModelListener) getTreeModelListeners()[i]).treeStructureChanged(e);
		}
	}

	/**
	 * Returns the child of parent at index index in the parent's child array.
	 */
	public Object getChild(Object parent, int index) {
		KnowledgerObject knowledgerObject = null;

		if (parent == getRoot()) {
			return basicNodes.get(index);
		}
		if (parent.toString().equals(CONCEPT_NODE_NAME)) {
			try {
				return knowledgeBase.getRootObject();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (parent.equals(CONNECTOR_NODE_NAME)) {
			try {
				return knowledgeBase.getRootConnector();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (parent.equals(LOST_ENTITIES_NODE_NAME)) {
			return knowledgeBase.getLostEntities().get(index);
		}

		if (parent.getClass() == Concept.class) {
			ConceptList conceptList = null;
			InstanceList instanceList = null;
			try {
				Concept concept = (Concept) parent;
				conceptList = concept.getSubConcepts();
				instanceList = concept.getInstances();
				if (index < conceptList.size()) {
					return conceptList.get(index);
				}
				index = index - conceptList.size();
				return instanceList.get(index);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "Error Node";
			}

		}
		if (parent.getClass() == Connector.class) {
			ConnectorList connectorList = null;

			try {
				knowledgerObject = (KnowledgerObject) parent;
				connectorList = knowledgeBase.getSubConnectorsOf((Connector) knowledgerObject, 1);
				return connectorList.getConnector(index);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "error node";
			}

		}

		return "unknown";
	}

	/**
	 * Returns the number of children of parent.
	 */
	public int getChildCount(Object parent) {
		KnowledgerObject knowledgerObject = null;
		if (parent == getRoot()) {
			return 3;
		}
		if (parent.equals(CONCEPT_NODE_NAME)) {
			return 1;
		}
		if (parent.equals(CONNECTOR_NODE_NAME)) {
			return 1;
		}
		if (parent.equals(LOST_ENTITIES_NODE_NAME)) {
			return knowledgeBase.getLostEntities().size();
		}

		if (parent.getClass() == Concept.class) {
			ConceptList conceptList = null;
			InstanceList instanceList = null;
			try {
				knowledgerObject = (KnowledgerObject) parent;
				Concept concept = (Concept) parent;
				conceptList = concept.getSubConcepts();
				instanceList = concept.getInstances();
				return conceptList.size() + instanceList.size();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}

		}
		if (parent.getClass() == Connector.class) {
			ConnectorList connectorList = null;

			try {
				knowledgerObject = (KnowledgerObject) parent;
				connectorList = knowledgeBase.getSubConnectorsOf((Connector) knowledgerObject, 1);
				return connectorList.size();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}

		}
		return 0;
	}

	/**
	 * Returns the index of child in parent.
	 */
	public int getIndexOfChild(Object parent, Object child) {
		KnowledgerObject knowledgerObject = (KnowledgerObject) parent;

		if (parent.getClass() == Concept.class) {
			ConceptList conceptList = null;
			InstanceList instanceList = null;
			try {
				knowledgerObject = (KnowledgerObject) parent;
				Concept concept = (Concept) parent;
				conceptList = concept.getSubConcepts();
				instanceList = concept.getInstances();
				return Math.max(conceptList.indexOf(child), instanceList.indexOf(child));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}

		}
		if (parent.getClass() == Connector.class) {
			ConnectorList connectorList = null;
			try {
				knowledgerObject = (KnowledgerObject) parent;
				connectorList = knowledgeBase.getSubConnectorsOf((Connector) knowledgerObject, 1);
				return connectorList.indexOf(child);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return 0;
	}

	/**
	 * Returns true if node is a leaf.
	 */
	public boolean isLeaf(Object node) {
		return getChildCount(node) == 0;
	}

	/**
	 * Messaged when the user has altered the value for the item identified by
	 * path to newValue. Not used by this model.
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
		System.out.println("*** valueForPathChanged : " + path + " --> " + newValue);
		System.out.println(" Class of Object " + path.getLastPathComponent().getClass().getName());
		Object modifiedObject = path.getLastPathComponent();

		if (modifiedObject.getClass() == Concept.class) {
			KnowledgerObject knowledgerObject = (KnowledgerObject) modifiedObject;
			knowledgerObject.setIdentifier(newValue.toString());
			try {
				knowledgeBase.updateConcept((Concept) knowledgerObject);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		if (modifiedObject.getClass() == Instance.class) {
			Instance knowledgerObject = (Instance) modifiedObject;
			knowledgerObject.setIdentifier(newValue.toString());
			try {
				// TODO Check this!
                knowledgeBase.updateInstance(knowledgerObject);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		if (modifiedObject.getClass() == Concept.class) {
			Concept knowledgerObject = (Concept) modifiedObject;
			knowledgerObject.setIdentifier(newValue.toString());
			try {
				knowledgeBase.updateConcept(knowledgerObject);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

	}

	public static void main(String[] args) throws Exception {
		IKnowledgeBase kb = KnowledgeBaseFactory.getInstance("test",KnowledgeBaseType.IN_MEMORY);
		JTree tree = new JTree(new KnowledgerTreeModel(kb));
		tree.setPreferredSize(new Dimension(600, 400));
		JFrame frame = new JFrame("Dialogy");
		JPanel panel = new JPanel();
		panel.add(new JScrollPane(tree));
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.show();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		System.out.println("KnowledgerTreeModel : Knowledge base has changed");
		try {
			fireTreeStructureChanged();
			// fireTreeStructureChanged(getRootConcept());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}