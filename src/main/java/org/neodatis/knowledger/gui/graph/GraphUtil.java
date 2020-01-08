/* 
 * $RCSfile: GraphUtil.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph;

import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;
import org.neodatis.knowledger.gui.graph.basic.RelationNode;
import org.neodatis.tool.DLogger;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;


/**
 * Basic helper methods to builds the graph - prefuse dependent
 * 
 * @author olivier s
 * 
 */
public class GraphUtil {
	public boolean graphIsDirected = true;
	public Graph graph;
	public KnowledgeBase knowledgeBase;
	
	public GraphUtil(KnowledgeBase knowledgeBase){
		this.knowledgeBase = knowledgeBase;
	}

	public Node createNode(String id, String label, int type) {
		Node node = graph.addNode();
		node.setString(GraphConstants.LABEL, label);
		node.setString(GraphConstants.ID, id);
		node.setInt(GraphConstants.TYPE, type);
		
		StringBuffer buffer = new StringBuffer(label==null?"null":label).append("-").append(id);
		if(label==null || id==null){
			DLogger.error("createNode.warning: id="+id+" - label="+label+" - type="+type);
		}
		node.setString(GraphConstants.TOOLTIP, buffer.toString());
		return node;
	}

	public Node createConceptNode(String id, String label) {
		return createNode(id, label, GraphConstants.CONCEPT);
	}

	public Node createInstanceNode(String id, String label) {
		return createNode(id, label, GraphConstants.INSTANCE);
	}

	public Node createConnectorNode(String id, String label) {
		return createNode(id,label,GraphConstants.CONNECTOR);
		}

	public Node createConnectorNode(Connector connector) {
		return createNode(String.valueOf(connector.getId()),connector.getIdentifier(),GraphConstants.RELATION);
	}

	public RelationNode createRelation(Node node1, Connector connector, String relationId, Node node2) {
		try {
			if (connector == connector.getKnowledgeBase().getIsInstanceOfConnector()) {
				node1.setInt(GraphConstants.TYPE, GraphConstants.INSTANCE);
			}
			if (connector == connector.getKnowledgeBase().getIsSubclassOfConnector()) {
				node1.setInt(GraphConstants.TYPE, GraphConstants.CONCEPT);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (Configurations.getConfiguration(connector.getKnowledgeBase(),"default") .getVisualizationConfiguration().displayConnectorsAsNodes()) {
			Node connectorNode = createConnectorNode(relationId, connector.getIdentifier());
			Edge edge1 = graph.addEdge(node1, connectorNode);
			edge1.setString(GraphConstants.EDGE_TYPE,GraphConstants.EDGE_TYPE_START);
			edge1.setString(GraphConstants.TOOLTIP, connector.getIdentifier());
			edge1.setInt(GraphConstants.TYPE, GraphConstants.CONNECTOR);
			
			Edge edge2 = graph.addEdge(connectorNode, node2);
			edge2.setString(GraphConstants.EDGE_TYPE, GraphConstants.EDGE_TYPE_END);
			edge2.setString(GraphConstants.TOOLTIP, connector.getIdentifier());
			edge2.setInt(GraphConstants.TYPE, GraphConstants.CONNECTOR);
			
			return new RelationNode(node1, connectorNode, node2, edge1, edge2);
		} else {
			Edge edge1 = graph.addEdge(node1, node2);
			edge1.setInt(GraphConstants.TYPE, GraphConstants.CONNECTOR);
			edge1.setString(GraphConstants.LABEL,connector.getIdentifier());			
			edge1.setString(GraphConstants.TOOLTIP, connector.getIdentifier() + " - " + relationId);
			edge1.setString(GraphConstants.ID,relationId);
			return new RelationNode(node1, node2, edge1);
		}
	}

	public RelationNode createTwoEdgesRelation(Node node1, Node connectorNode, Node node2) {

		if (Configurations.getConfiguration(knowledgeBase).getVisualizationConfiguration().displayConnectorsAsNodes()) {
			Edge edge1 = graph.addEdge(node1, connectorNode);
			Edge edge2 = graph.addEdge(connectorNode, node2);
			String connectorLabel = connectorNode.getString(GraphConstants.LABEL);
			edge1.setString(GraphConstants.TOOLTIP, connectorLabel);
			edge1.setInt(GraphConstants.TYPE, GraphConstants.CONNECTOR);
			edge2.setString(GraphConstants.TOOLTIP, connectorLabel);
			edge2.setInt(GraphConstants.TYPE, GraphConstants.CONNECTOR);
			//Configurations.getConfiguration(knowledgeBase).getLogger().debug("Create Relation - double : " + node1.get(GraphConstants.LABEL) + " " + connectorNode.get(GraphConstants.LABEL) + " " + node2.get(GraphConstants.LABEL));
			return new RelationNode(node1, connectorNode, node2, edge1, edge2);
		}
		throw new RuntimeException("createRelation called with illegal state : displayConnectorAsNodes = false, should be true" );
	}
	public RelationNode createRelation(Node node1, String connectorLabel, Node node2,String relationId) {

		if (!Configurations.getConfiguration(knowledgeBase).getVisualizationConfiguration().displayConnectorsAsNodes()) {
			Edge edge = graph.addEdge(node1, node2);
			edge.setInt(GraphConstants.TYPE, GraphConstants.CONNECTOR);
			edge.setString(GraphConstants.CONNECTOR_TYPE, connectorLabel);
			edge.setString(GraphConstants.LABEL, connectorLabel);
			edge.setString(GraphConstants.TOOLTIP, connectorLabel + " - "+relationId);
			edge.setString(GraphConstants.ID, ""+relationId);
			
			
			//Configurations.getConfiguration(knowledgeBase).getLogger().debug("Create Relation - simple : " + node1.get(GraphConstants.LABEL) + " " + connectorLabel + " " + node2.get(GraphConstants.LABEL));
			return new RelationNode(node1, node2, edge);
		}

		throw new RuntimeException("createRelation called with illegal state : displayConnectorAsNodes = true, should be false" );

	}

	public Graph createEmptyGraph() {
		graph = new Graph(graphIsDirected);
		graph.addColumn(GraphConstants.LABEL, String.class);
		graph.addColumn(GraphConstants.TYPE, int.class);
		graph.addColumn(GraphConstants.ID, String.class);
		graph.addColumn(GraphConstants.TOOLTIP, String.class);
		graph.addColumn(GraphConstants.EDGE_TYPE, String.class);
		graph.addColumn(GraphConstants.CONNECTOR_TYPE, String.class);
        graph.addColumn(GraphConstants.USER_KEY, int.class);
		return graph;
	}

	public boolean removeNode(Node node) {
		return graph.removeNode(node);
	}
}
