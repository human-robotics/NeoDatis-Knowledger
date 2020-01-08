package org.neodatis.knowledger.gui.explorer;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.neodatis.knowledger.core.CanNotDeleteObjectWithReferencesException;
import org.neodatis.knowledger.core.ObjectDoesNotExistException;
import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.factory.RelationCriteriaFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.implementation.query.RelationQuery;
import org.neodatis.knowledger.core.interfaces.entity.IRelationCriteria;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.knowledger.gui.explorer.node.DialogyTreeNode;
import org.neodatis.knowledger.gui.explorer.node.InformationTreeNode;
import org.neodatis.knowledger.gui.panel.NewConceptPanel;
import org.neodatis.knowledger.gui.panel.NewInstancePanel;
import org.neodatis.knowledger.gui.panel.NewObjectPanel;
import org.neodatis.knowledger.gui.panel.NewRelationPanel;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.tool.DLogger;


/**
 * A knowledge explorer
 */

public class KnowledgeExplorerTree implements TreeSelectionListener, TreeModelListener, ActionListener, Observer {

	public final static String OBJECT_KEY = "object-key";

	public final static String KB_KEY = "knowledge-base";

	public final static int NEW_SUB_CONCEPT = 1;

	public final static int NEW_INSTANCE = 2;

	public final static int NEW_RELATION = 3;

	public final static int DELETE_CONCEPT = 4;

	public final static int GO_TO = 5;

	public final static int NEW_CONNECTOR = 6;

	public final static int DELETE_RELATION = 7;

	public final static int DELETE_INSTANCE = 8;

	public final static int DELETE_CONNECTOR = 9;
    public final static int NEW_PROPERTY = 10;
    public final static int SET_PROPERTY = 11;

	public static final int CREATE_RELATION = 12;

	private KnowledgeBase knowledgeBase;

	protected DefaultMutableTreeNode rootNode;

	protected DefaultMutableTreeNode conceptualRootNode;

	protected DefaultMutableTreeNode connectorRootNode;

	protected DefaultMutableTreeNode lostEntitiesRootNode;

	protected DefaultTreeModel treeModel;

	private JTree jtree;

	private List alreadyFoundNodes;

	private List roots;

	private List rootNodes;
	
	private String title;

	/**
	 * Construtor
	 * 
	 * @param theKnowledgeBaseUtility
	 * @param connectorTypeToBuildHierarchy
	 * @throws Exception
	 *
	public KnowledgeExplorerTree(IKnowledgeBase theKnowledgeBase) throws Exception {
		init(theKnowledgeBase);
	}*/

	public KnowledgeExplorerTree(KnowledgeBase theKnowledgeBase,  List roots,String title) throws Exception {
		this.roots = roots;
		rootNodes = new ArrayList();
		init(theKnowledgeBase,title);
	}

	private void init(KnowledgeBase theKnowledgeBase,String title) throws Exception {
		this.knowledgeBase = theKnowledgeBase;
		theKnowledgeBase.addObserver(this);
		buildTree(title);
	}

	private void buildTree(String title) throws Exception {
		this.title = title;
		 
		if (rootNode == null) {
			// Builds the root node
			rootNode = new DefaultMutableTreeNode(title);

			treeModel = new DefaultTreeModel(rootNode);
			treeModel.addTreeModelListener(this);
			jtree = new JTree(treeModel);
			jtree.setCellRenderer(new KnowledgerTreeRenderer());
			ToolTipManager.sharedInstance().registerComponent(jtree);

			jtree.addTreeSelectionListener(this);
			jtree.setExpandsSelectedPaths(true);
			jtree.setScrollsOnExpand(true);
			jtree.setRootVisible(true);
			// Add listener to the text area so the popup menu can come up.
			MouseListener popupListener = new PopupListener(this);
			jtree.addMouseListener(popupListener);
			jtree.setEditable(true);
		} else {
			rootNode.setUserObject(title);
			jtree.removeAll();
			rootNode.removeAllChildren();
			treeModel.reload();
		}

		DialogyTreeNode node = null;
		Entity entity = null;
		for (int i = 0; i < roots.size(); i++) {
			entity = (Entity) roots.get(i);
			node = new DialogyTreeNode(entity);
			rootNodes.add(node);
			treeModel.insertNodeInto(node, rootNode, rootNode.getChildCount());
			
			if(entity.getClass() == Concept.class){
				fillNode(node, entity, knowledgeBase.getIsSubclassOfConnector());
			}
			if(entity.getClass() == Connector.class ){
				fillNode(node, entity, knowledgeBase.getIsSubRelationOfConnector());
			}
			if(entity.getClass() == Instance.class ){
				fillRelationsOfOneInstance(node, (Instance)entity);
			}
			
		}
		
		//fillNode(connectorNode, rootConnector, knowledgeBase.getIsSubRelationOfConnector());
		//fillLostEntities(lostEntitiesRootNode);

		treeModel.nodeStructureChanged(rootNode);

	}

	/**
	 * @param rootNode2
	 */
	private void fillLostEntities(DefaultMutableTreeNode rootNode) {
		DefaultMutableTreeNode node = null;

		EntityList entityList = knowledgeBase.getLostEntities();

		for (int i = 0; i < entityList.size(); i++) {
			node = createNode(entityList.getEntity(i));
			// Adds the node the root node
			treeModel.insertNodeInto(node, rootNode, rootNode.getChildCount());
		}
	}

	/**
	 * Returns the tree of the explorer
	 * 
	 * @return
	 */
	public JTree getTree() {
		return jtree;
	}

	/**
	 * Returns a panel with the tree
	 * 
	 * @return The panel
	 */
	public JPanel getPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(getTree());
		return panel;
	}

	/**
	 * Method to build a node
	 * 
	 * @param currentNode
	 * @param concept
	 * @throws ObjectDoesNotExistException
	 */
	private void fillNode(DefaultMutableTreeNode currentNode, Entity currentObject, Connector connectorToBuildHierarchy) throws Exception {
		Entity entity = null;
		DefaultMutableTreeNode node = null;

		// Sub classes list of depth 1
		IRelationCriteria relationCriteria = RelationCriteriaFactory.getRelationCriteria(null, connectorToBuildHierarchy, currentObject);
		List list = knowledgeBase.getRelationList(relationCriteria).getLeftEntities();

		if (currentObject.getClass() == Concept.class) {
			buildInstancesForNode(currentNode, (Concept) currentObject);
		}

		// Parse each sub class
		// Create the node of the sub class
		// And calls the fillNode recursively
		for (int i = 0; i < list.size(); i++) {
			entity = (Entity) list.get(i);
			// Creates the node for this concept
			node = createNode(entity);
			// Adds the node the root node
			treeModel.insertNodeInto(node, currentNode, currentNode.getChildCount());

			// Get all relations of this 'concept'
			relationCriteria = RelationCriteriaFactory.getRelationCriteria(entity, null, null);
			List l = knowledgeBase.getRelationList(relationCriteria);

			// System.out.println("Relations of " + entity + " are " + l);
			Relation relation = null;
			for (int j = 0; j < l.size(); j++) {
				relation = (Relation) l.get(j);

				if (!relation.getConnector().equals(connectorToBuildHierarchy) && !relation.getConnector().equals(knowledgeBase.getIsInstanceOfConnector())) {

					treeModel.insertNodeInto(new DialogyTreeNode(relation), node, node.getChildCount());

				}
			}
			// Fills the rest of the tree
			fillNode(node, entity, connectorToBuildHierarchy);
		}
	}

	/**
	 * @param object
	 * @return
	 */
	private DefaultMutableTreeNode createNode(Entity entity) {
		return new DialogyTreeNode(entity);
	}

	/**
	 * Adds the instances (and its relations) of a concept node
	 * 
	 * @param currentNode
	 * @param concept
	 * @throws ObjectDoesNotExistException
	 */
	private void buildInstancesForNode(DefaultMutableTreeNode currentNode, Concept concept) throws Exception {
		Entity entity = null;
		DialogyTreeNode instanceNode = null;
		Relation relation = null;

		// Gets the instances of depth 0 - direct instances
		List instanceList = concept.getDirectInstances();

		// System.out.println("Instances of " + concept + " are " +
		// instanceList.toString());
		// Parse the list
		for (int i = 0; i < instanceList.size(); i++) {

			// Gets the instance
			entity = (Entity) instanceList.get(i);
			// Creates a tree node for the instance
			instanceNode = new DialogyTreeNode(entity);
			// Adds the instance node to the tree
			treeModel.insertNodeInto(instanceNode, currentNode, currentNode.getChildCount());

			/**
			 * 05/07/2004 : do not need this anymore - instance with value
			 * already display its value // If the instance has a value then
			 * adds a value node to the instance node if (instance.getValue() !=
			 * null && instance.getValue().length() > 0) { valueNode = new
			 * ValueTreeNode("value=" + instance.getValue()); // inserts the
			 * value node as a son of the instance node
			 * treeModel.insertNodeInto(valueNode, instanceNode,
			 * instanceNode.getChildCount()); }
			 */

			// For each instance look for relations
			IRelationCriteria relationCriteria = RelationCriteriaFactory.getRelationCriteria(entity, null, null);
			List l = knowledgeBase.getRelationList(relationCriteria);

			// Parse all the relations
			for (int j = 0; j < l.size(); j++) {
				// Gets the relation
				relation = (Relation) l.get(j);

				if (!relation.getConnector().equals(knowledgeBase.getIsSubclassOfConnector()) && !relation.getConnector().equals(knowledgeBase.getIsInstanceOfConnector())) {

					treeModel.insertNodeInto(new DialogyTreeNode(relation), instanceNode, instanceNode.getChildCount());

				}
			}
			// For each instance look for inverse relations
			relationCriteria = RelationCriteriaFactory.getRelationCriteria(null, null, entity);
			l = knowledgeBase.getRelationList(relationCriteria);

			// Parse all the relations
			for (int j = 0; j < l.size(); j++) {
				// Gets the relation
				relation = (Relation) l.get(j);

				if (!relation.getConnector().equals(knowledgeBase.getIsSubclassOfConnector()) && !relation.getConnector().equals(knowledgeBase.getIsInstanceOfConnector())) {
					treeModel.insertNodeInto(new DialogyTreeNode(relation), instanceNode, instanceNode.getChildCount());
				}
			}
		}
	}

	private void fillRelationsOfOneInstance(DialogyTreeNode instanceNode, Instance instance) throws Exception{
		Relation relation = null;
		// For each instance look for inverse relations
		IRelationCriteria  relationCriteria = RelationCriteriaFactory.getRelationCriteria(null, null, instance);
		List l = knowledgeBase.getRelationList(relationCriteria);

		// Parse all the relations
		for (int j = 0; j < l.size(); j++) {
			// Gets the relation
			relation = (Relation) l.get(j);

			if (!relation.getConnector().equals(knowledgeBase.getIsSubclassOfConnector()) && !relation.getConnector().equals(knowledgeBase.getIsInstanceOfConnector())) {
				treeModel.insertNodeInto(new DialogyTreeNode(relation), instanceNode, instanceNode.getChildCount());
			}
		}

	}
	
	
	/**
	 * 
	 * @param theRootName
	 * @param theKnowledgeBase
	 * @throws Exception
	 */
	public static void display(KnowledgeBase theknowledgeBase,List roots,String title) throws Exception {

		KnowledgeExplorerTree ke = new KnowledgeExplorerTree(theknowledgeBase,roots,title);

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Dialogy Explorer");
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(ke.getTree()), BorderLayout.CENTER);

		frame.getContentPane().add(new JScrollPane(ke.getTree()));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public void searchNode(String textToSearch, boolean focus, boolean continueSearch) {

		if (!continueSearch || alreadyFoundNodes == null) {
			alreadyFoundNodes = new ArrayList();
		}

		DefaultMutableTreeNode node = rsearch(rootNode, textToSearch);
		if (node == null) {
			DLogger.info("Node " + textToSearch + " does not exist");
			Toolkit.getDefaultToolkit().beep();
		} else {
			// To remember that this node has been found
			alreadyFoundNodes.add(node);
			if (focus) {
				focusNode(node);
			}
		}
	}

	public void searchNode(Long objectId, boolean focus, boolean continueSearch) {
		searchNode(objectId.longValue(), focus, continueSearch);
	}

	public void searchNode(long objectId, boolean focus, boolean continueSearch) {

		if (!continueSearch || alreadyFoundNodes == null) {
			alreadyFoundNodes = new ArrayList();
		}

		DefaultMutableTreeNode node = rsearch(rootNode, objectId);
		if (node == null) {
			DLogger.info("Node " + objectId + " does not exist");
			Toolkit.getDefaultToolkit().beep();
		} else {
			if (focus) {
				focusNode(node);
			}
		}
	}

	public void focusNode(DefaultMutableTreeNode node) {
		TreePath treePath = new TreePath(treeModel.getPathToRoot(node));
		jtree.scrollPathToVisible(treePath);
		jtree.setSelectionPath(treePath);
		jtree.expandPath(treePath);

	}

	private DefaultMutableTreeNode rsearch(DefaultMutableTreeNode node, String textToSearch) {
		int numberOfChild = node.getChildCount();
		DefaultMutableTreeNode currentNode = null;
		DefaultMutableTreeNode foundNode = null;

		for (int i = 0; i < numberOfChild; i++) {
			currentNode = (DefaultMutableTreeNode) node.getChildAt(i);
			if (currentNode.getUserObject().toString().toUpperCase().indexOf(textToSearch.toUpperCase()) != -1) {
				if (!alreadyFoundNodes.contains(currentNode)) {
					return currentNode;
				}
			}
			foundNode = rsearch(currentNode, textToSearch);

			if (foundNode != null && !alreadyFoundNodes.contains(foundNode)) {
				return foundNode;
			}
		}
		return null;
	}

	private DefaultMutableTreeNode rsearch(DefaultMutableTreeNode node, String objectId) {
		int numberOfChild = node.getChildCount();
		DefaultMutableTreeNode currentNode = null;
		DefaultMutableTreeNode foundNode = null;
		KnowledgerObject object = null;

		for (int i = 0; i < numberOfChild; i++) {
			currentNode = (DefaultMutableTreeNode) node.getChildAt(i);

			// If the node is an information one, do not use it for search
			if (currentNode.getClass() == InformationTreeNode.class) {
				continue;
			}
			try {
				object = (KnowledgerObject) currentNode.getUserObject();
				if (object.getId().equals(objectId)) {
					return currentNode;
				}

			} catch (RuntimeException e) {
				System.out.println(currentNode.toString() + " is not a data node");
			}
			foundNode = rsearch(currentNode, objectId);

			if (foundNode != null) {
				return foundNode;
			}
		}
		return null;
	}

	public void refresh() throws Exception {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jtree.getLastSelectedPathComponent();
		buildTree(this.title);
		if (node != null) {
			if (isDialogyObject(node.getUserObject())) {
				searchNode(((Entity) node.getUserObject()).getId(), true, false);
			}
		}
	}
	public void refresh(List roots,String title) throws Exception {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jtree.getLastSelectedPathComponent();
		this.roots = roots;
		buildTree(title);
		if (node != null) {
			if (isDialogyObject(node.getUserObject())) {
				searchNode(((Entity) node.getUserObject()).getId(), true, false);
			}
		}
	}

	private boolean isDialogyObject(Object object) {
		try {
			Entity entity = (Entity) object;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		KnowledgeBase kb = KnowledgeBaseFactory.getInstance("test", KnowledgeBaseType.IN_MEMORY);
		List roots = new ArrayList();
		roots.add(kb.getRootObject());
		roots.add(kb.getRootConnector());
		display(kb,roots,"Dialogy Query");
	}

	/**
	 * @param e
	 * 
	 */
	public void valueChanged(TreeSelectionEvent e) {
		Object o = jtree.getLastSelectedPathComponent();
		if (o != null) {
			System.out.println("Node Type = " + o.getClass().getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesChanged(TreeModelEvent e) {
		System.out.println("treeNodesChanged - not implemented in KnowledgeExplorerTree");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesInserted(TreeModelEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesRemoved(TreeModelEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
	 */
	public void treeStructureChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().getClass() == JMenuItem.class) {
			JMenuItem menuItem = (JMenuItem) e.getSource();
			Object object = menuItem.getClientProperty(OBJECT_KEY);
			KnowledgerObject knowledgerObject = (KnowledgerObject) object;
			System.out.println(e.getActionCommand() + " " + knowledgerObject + " id = " + knowledgerObject.getId());
			try {
				manageAction(Integer.parseInt(e.getActionCommand()), object);
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ObjectDoesNotExistException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}

	/**
	 * @param action
	 * @param parent
	 * @throws Exception
	 */
	private void manageAction(int action, Object object) throws Exception {
		KnowledgerObject selectedDialogyObject = (KnowledgerObject) object;
		Entity selectedEntity = null;
		switch (action) {
		case NEW_SUB_CONCEPT:
			try {
				NewConceptPanel.display(selectedDialogyObject);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case NEW_CONNECTOR:
			selectedEntity = (Entity) object;
			NewObjectPanel.display(selectedDialogyObject, NewObjectPanel.CREATE_CONNECTOR);
			break;
		case NEW_INSTANCE:
			try {
				NewInstancePanel.display(selectedDialogyObject);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case NEW_RELATION:
			JFrame.setDefaultLookAndFeelDecorated(true);
			JFrame frame = new JFrame("Relation Creator");
			NewRelationPanel panel = new NewRelationPanel(knowledgeBase, selectedDialogyObject);
			frame.getContentPane().add(new JScrollPane(panel));
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
			break;
		case GO_TO:
			DLogger.debug("searching for object with " + selectedDialogyObject.getId());
			searchNode(selectedDialogyObject.getId(), true, false);
			break;
		case DELETE_INSTANCE:
		case DELETE_CONNECTOR:
		case DELETE_CONCEPT:
			try {
				deleteObject(selectedDialogyObject);
			} catch (Exception e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
			DialogyTreeNode node = (DialogyTreeNode) jtree.getLastSelectedPathComponent();
			treeModel.removeNodeFromParent(node);
			break;
		case DELETE_RELATION:
			try {
				deleteSubClassOfRelation(selectedDialogyObject);
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			DialogyTreeNode node2 = (DialogyTreeNode) jtree.getLastSelectedPathComponent();
			treeModel.removeNodeFromParent(node2);
			break;
		case CREATE_RELATION:
			knowledgeBase.createRelation((Relation) object);
			break;
		}
	}

	/**
	 * @param selectedObject
	 * @throws Exception
	 */
	private void deleteSubClassOfRelation(KnowledgerObject selectedDialogyObject) throws Exception {

		RelationQuery query = new RelationQuery();
		query.addLeftEntityToInclude(selectedDialogyObject);
		if (selectedDialogyObject.getClass() == Connector.class) {
			query.addConnectorToInclude(knowledgeBase.getIsSubRelationOfConnector());
		} else {
			query.addConnectorToInclude(knowledgeBase.getIsSubclassOfConnector());
		}

		// TODO also restrict right part object
		RelationList relationList = query.executeOn(knowledgeBase.getRelationList());

		if (!relationList.isEmpty()) {

			int choice = JOptionPane.showConfirmDialog(null, Messages.getString("do.you.really.want.to.delete") + " " + relationList.getRelation(0), Messages.getString("confirm"), JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (choice == JOptionPane.OK_OPTION) {

				// Deletes the first relation
				knowledgeBase.deleteRelation(relationList.getRelation(0));
			}

		}
	}

	/**
	 * @param parent
	 * @throws Exception
	 */
	private void deleteObject(Object object) throws Exception {
		int choice = JOptionPane.showConfirmDialog(null, Messages.getString("do.you.really.want.to.delete") + " " + object.toString(), Messages.getString("confirm"), JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (choice == JOptionPane.OK_OPTION) {

			if (object instanceof KnowledgerObject) {
				Entity entity = (Entity) object;
				// Checks all the relations this object have
				RelationList relationList = knowledgeBase.getRelationsOf(entity);

				if (!relationList.isEmpty()) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < relationList.size(); i++) {
						sb.append("\n").append(relationList.get(i));
					}

					choice = JOptionPane.showConfirmDialog(null, Messages.getString("delete.associations") + " " + sb.toString(), Messages.getString("confirm"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

					if (choice == JOptionPane.OK_OPTION) {
						try {
							knowledgeBase.delete(entity);

						} catch (CanNotDeleteObjectWithReferencesException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					RelationList relations = new RelationList();
					relations.add(object);
					knowledgeBase.deleteRelations(relations);
				}
			}
		}

	}

	/**
	 * Observer implementation
	 */
	public void update(Observable o, Object updatedObject) {
		System.out.println("KnowledgeExplorerTree : Knowledge base has been updated");
		try {
			refresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public KnowledgeBase getknowledgeBase() {
		return knowledgeBase;
	}

}