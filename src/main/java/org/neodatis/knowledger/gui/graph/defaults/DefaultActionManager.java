/* 
 * $RCSfile: DefaultActionManager.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph.defaults;

import java.awt.Point;

import javax.swing.JOptionPane;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.gui.GuiLogger;
import org.neodatis.knowledger.gui.graph.Configurations;
import org.neodatis.knowledger.gui.graph.IActionManager;
import org.neodatis.knowledger.gui.graph.IPersistentManager;
import org.neodatis.knowledger.gui.graph.MenuEntry;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;
import org.neodatis.knowledger.gui.graph.basic.IGraphProvider;
import org.neodatis.knowledger.gui.graph.basic.RelationNode;
import org.neodatis.knowledger.gui.graph.panel.KnowledgerGraphPanel;
import org.neodatis.knowledger.gui.graph.panel.NameAndConnectorChooseDialog;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.tool.ILogger;

import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.Tuple;


/**
 * Class that manages all user actions triggered from a contextual menu on the
 * graph
 * 
 * @author olivier s
 * 
 */
public class DefaultActionManager implements IActionManager {
	public static final String ACTION_NEW_CONNECTED_CONCEPT = "new-connected-concept";

	public static final String ACTION_NEW_CONNECTED_INSTANCE = "new-connected-instance";

	public static final String ACTION_NEW_SUB_CONCEPT = "new-sub-concept";

	public static final String ACTION_NEW_INSTANCE = "new-instance";

	public static final String ACTION_DELETE_CONCEPT = "delete-concept";

	public static final String ACTION_CENTER_ON_CONCEPT = "center-on-concept";

	public static final String ACTION_DELETE_INSTANCE = "delete-instance";

	public static final String ACTION_CENTER_ON_INSTANCE = "center-on-instance";

	public static final String ACTION_DELETE_RELATION = "delete-relation";

	public static final String ACTION_CENTER_ON_RELATION = "center-on-relation";

	public static final String ACTION_CENTER_ON_LEFT = "center-on-left";

	public static final String ACTION_CENTER_ON_RIGHT = "center-on-right";

	public static final String ACTION_OPEN_IN_EXTERNAL_BROWSER = "open-in-external-browser";

	public static final String DEFAULT_CONCEPT_NAME = "?";

	public IPersistentManager persistentManager;

	public IGraphProvider graphProvider;

	public KnowledgerGraphPanel graphPanel;

	private ILogger logger;

	public DefaultActionManager(IGraphProvider graphProvider, IPersistentManager manager, KnowledgerGraphPanel graphPanel) {
		this.persistentManager = manager;
		this.graphProvider = graphProvider;
		this.graphPanel = graphPanel;
		logger = new GuiLogger();
	}

	public String execute(MenuEntry menuEntry, int x, int y) throws Exception {
		System.out.println("Executing action for " + menuEntry + " at " + x + " , " + y);

		if (menuEntry.getAction().equals(ACTION_NEW_SUB_CONCEPT)) {
			return newSubConcept(menuEntry, x, y);
		}
		if (menuEntry.getAction().equals(ACTION_NEW_CONNECTED_CONCEPT)) {
			return newConnectedConcept(menuEntry, x, y);
		}
		if (menuEntry.getAction().equals(ACTION_DELETE_CONCEPT)) {
			deleteConcept(menuEntry);
		}
		if (menuEntry.getAction().equals(ACTION_DELETE_INSTANCE)) {
			deleteInstance(menuEntry);
		}
		if (menuEntry.getAction().equals(ACTION_DELETE_RELATION)) {
			deleteRelation(menuEntry);
		}
		if (menuEntry.getAction().equals(ACTION_NEW_INSTANCE)) {
			return newInstance(menuEntry, x, y);
		}
		if (menuEntry.getAction().equals(ACTION_NEW_CONNECTED_INSTANCE)) {
			return newConnectedInstance(menuEntry, x, y);
		}
		if (menuEntry.getAction().equals(ACTION_CENTER_ON_CONCEPT) || menuEntry.getAction().equals(ACTION_CENTER_ON_INSTANCE) || menuEntry.getAction().equals(ACTION_CENTER_ON_RELATION)) {
			centerOn(x, y);
		}
		if (menuEntry.getAction().equals(ACTION_OPEN_IN_EXTERNAL_BROWSER)) {
			openInExternalBrowser(menuEntry, x, y);
		}
		return null;

	}

	private void centerOn(int x, int y) {
		Point p = new Point(x, y);
		graphPanel.getPrefuseGraphBuilder().getDisplay().animatePanTo(p, 2000);
        

	}

	private void openInExternalBrowser(MenuEntry menuEntry, int x, int y) {
		Node node = (Node) menuEntry.getVisuaItem().getSourceTuple();
		String property = Configurations.getConfiguration(persistentManager.getKnowledgeBaseName()).getExternalUrlRelationName();
		String sId = node.getString(GraphConstants.ID);
		long id = Long.parseLong(sId);
		String externalUrl = null;
		Entity entity;
		try {
			entity = persistentManager.getEntityById(sId);
			System.out.println("RelationList of " + entity.getIdentifier() + "=" + entity.getRelationList());

			if (entity.hasProperty(property)) {
				externalUrl = entity.getProperty(property).getIdentifier();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		graphPanel.openInExternalBrowser(externalUrl, persistentManager.getKnowledgeBaseName(), sId,"detail");
	}

	public String newSubConcept(MenuEntry menuEntry, int x, int y) throws Exception {

		Object o = menuEntry.getVisuaItem().getSourceTuple();
		Node father = (Node) o;

		String conceptName = (String) JOptionPane.showInputDialog(null, Messages.getString("label.ask.concept.name"), Messages.getString("label.info"), JOptionPane.PLAIN_MESSAGE);
		if(conceptName==null || conceptName.length() == 0){
			return null;
		}

		Connector connector = persistentManager.getIsAConnector();
		Concept subConcept = persistentManager.newConcept(conceptName);
		Concept fatherConcept = persistentManager.getConceptById(father.getString(GraphConstants.ID));

		Node newNode = graphProvider.createConceptNode(String.valueOf(subConcept.getId()), conceptName);

		Relation relation = persistentManager.newRelation(subConcept, connector, fatherConcept);
		RelationNode relationNode = graphProvider.createRelation(newNode, connector, String.valueOf(relation.getId()), father);

		graphPanel.refresh();
		return subConcept.getId();
	}
	
	public String newConnectedConcept(MenuEntry menuEntry, int x, int y) throws Exception {

		Object o = menuEntry.getVisuaItem().getSourceTuple();
		Node father = (Node) o;
		Concept mainConcept = persistentManager.getConceptById(father.getString(GraphConstants.ID));
		
		NameAndConnectorChooseDialog dialog = new NameAndConnectorChooseDialog(persistentManager,mainConcept.getIdentifier(),NameAndConnectorChooseDialog.TYPE_CONCEPT);
		dialog.setLocation(x,y);
		dialog.setVisible(true);
		if(!dialog.isOk()){
			return null;
		}

		Connector connector = dialog.getConnector();
		Concept newConcept = persistentManager.newConcept(dialog.getObjectName());
		

		Node newNode = graphProvider.createConceptNode(String.valueOf(newConcept.getId()), newConcept.getIdentifier());

		Relation relation = persistentManager.newRelation(mainConcept , connector, newConcept);
		RelationNode relationNode = graphProvider.createRelation(father, connector, String.valueOf(relation.getId()), newNode);

		graphPanel.refresh();
		return newConcept.getId();
	}

	public void deleteConcept(MenuEntry menuEntry) throws Exception {

		Node node = (Node) menuEntry.getVisuaItem().getSourceTuple();

		String id = node.getString(GraphConstants.ID);
		persistentManager.deleteConcept(id);
		boolean deleted = graphProvider.removeNode(node);
		if (deleted) {
			graphPanel.refresh();
		}
		logger.debug("Deleting concept " + id);
	}

	public void deleteInstance(MenuEntry menuEntry) throws Exception {
		Node node = (Node) menuEntry.getVisuaItem().getSourceTuple();

		String id = node.getString(GraphConstants.ID);
		persistentManager.deleteInstance(id);
		boolean deleted = graphProvider.removeNode(node);
		if (deleted) {
			graphPanel.refresh();
		}
		logger.debug("Deleting instance " + id);
	}

	public void deleteRelationFromRelatioNode(MenuEntry menuEntry) throws Exception {

		Node relationNode = (Node) menuEntry.getVisuaItem().getSourceTuple();
		String id = relationNode.getString(GraphConstants.ID);
		persistentManager.deleteRelation(id);
		boolean deleted = graphProvider.removeNode(relationNode);

		logger.debug("deleting Relation id " + id + " - " + deleted);
	}

	public void deleteRelation(MenuEntry menuEntry) throws Exception {

        Object o = menuEntry.getVisuaItem().getSourceTuple();
        Tuple relationNode = (Tuple) menuEntry.getVisuaItem().getSourceTuple();
		Edge edge = (Edge)o;
		persistentManager.deleteRelation(menuEntry.getObjectId());
		boolean deleted = graphProvider.removeEdge(edge);
		logger.debug("deleting Relation id " + menuEntry.getObjectId() + " - " + deleted);
	}
	public String newInstance(MenuEntry menuEntry, int x, int y) throws Exception {

		Node conceptNode = (Node) menuEntry.getVisuaItem().getSourceTuple();
		menuEntry.getVisuaItem().setFixed(true);

		String name = (String) JOptionPane.showInputDialog(null, Messages.getString("label.ask.instance.name"), Messages.getString("label.info"), JOptionPane.PLAIN_MESSAGE);
		if(name==null || name.length()==0){
			return null;
		}
		Connector connector = persistentManager.getIsInstanceOfConnector();
		Instance instance = persistentManager.newInstance(name);
		Concept concept = persistentManager.getConceptById(conceptNode.getString(GraphConstants.ID));
		Node newNode = graphProvider.createInstanceNode(String.valueOf(instance.getId()), name);

		Relation relation = persistentManager.newRelation(instance, connector, concept);
		RelationNode relationNode = graphProvider.createRelation(newNode, connector, String.valueOf(relation.getId()), conceptNode);

		graphPanel.refresh();
		logger.debug("creating new instance of concept " + concept.getIdentifier());
		return instance.getId();
	}

	public String newConnectedInstance(MenuEntry menuEntry, int x, int y) throws Exception {

		Object o = menuEntry.getVisuaItem().getSourceTuple();
		Node father = (Node) o;
		Instance mainInstance = persistentManager.getInstanceById(father.getString(GraphConstants.ID));
		
		NameAndConnectorChooseDialog dialog = new NameAndConnectorChooseDialog(persistentManager,mainInstance.getIdentifier(),NameAndConnectorChooseDialog.TYPE_INSTANCE);
		dialog.setLocation(x,y);
		dialog.setVisible(true);
		if(!dialog.isOk()){
			return null;
		}

		Connector connector = dialog.getConnector();
		Instance newInstance = persistentManager.newInstance(dialog.getObjectName());
		

		Node newNode = graphProvider.createInstanceNode(String.valueOf(newInstance.getId()), newInstance.getIdentifier());

		Relation relation = persistentManager.newRelation(mainInstance , connector, newInstance);
		RelationNode relationNode = graphProvider.createRelation(father, connector, String.valueOf(relation.getId()), newNode);

		graphPanel.refresh();
		return newInstance.getId();

	}

	public String connect(Node node1, Connector connector, Node node2) throws Exception {

		Entity entity1 = persistentManager.getEntityById(node1.getString(GraphConstants.ID));
		Entity entity2 = persistentManager.getEntityById(node2.getString(GraphConstants.ID));

		Relation relation = persistentManager.newRelation(entity1, connector, entity2);

		RelationNode relationNode = graphProvider.createRelation(node1, connector, String.valueOf(relation.getId()), node2);

		graphPanel.refresh();
		logger.debug("connecting " + entity1.getIdentifier() + " to " + entity2.getIdentifier() + " using " + connector.getId() + " connector");
		return relation.getId();
	}

	public String newConcept(int x, int y, boolean editing) throws Exception {

		boolean canCreateIsolatedConcept = persistentManager.getConcepts().isEmpty();
		
		if(!canCreateIsolatedConcept){
			JOptionPane.showMessageDialog(null,Messages.getString("info.create.concept.from.other.concept"));
			return null;
		}
		String newConceptName = (String) JOptionPane.showInputDialog(null, Messages.getString("label.ask.concept.name"), Messages.getString("label.info"), JOptionPane.PLAIN_MESSAGE);

		if(newConceptName==null || newConceptName.length()==0){
			return null;
		}

		Concept concept = persistentManager.newConcept(newConceptName);
		Node node = graphProvider.createConceptNode(String.valueOf(concept.getId()), newConceptName);
		/**
		 * int row = node.getRow(); Table t =
		 * (Table)graphPanel.getPrefuseGraphBuilder().getVisualization().getGroup("graph.nodes");
		 * NodeItem ni = (NodeItem)t.getTuple(row);
		 */
		graphPanel.refresh();
		logger.debug("creating new orphan concept with id " + concept.getId());
		return concept.getId();
	}

	public void updateConnectorOfRelation(String relationId, String newConnectorName) throws Exception {
		persistentManager.updateConnectorOfRelation(relationId, newConnectorName);
		logger.debug("updating connector name to " + newConnectorName);
	}

}