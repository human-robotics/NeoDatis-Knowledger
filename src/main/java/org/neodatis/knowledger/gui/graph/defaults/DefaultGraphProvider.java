package org.neodatis.knowledger.gui.graph.defaults;

import java.util.HashMap;
import java.util.Map;

import org.neodatis.knowledger.core.implementation.entity.ConceptList;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.gui.graph.Configurations;
import org.neodatis.knowledger.gui.graph.GraphUtil;
import org.neodatis.knowledger.gui.graph.IInteractionManager;
import org.neodatis.knowledger.gui.graph.IPersistentManager;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;
import org.neodatis.knowledger.gui.graph.basic.IGraphProvider;
import org.neodatis.knowledger.gui.graph.basic.RelationNode;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;


public class DefaultGraphProvider implements IGraphProvider {
	private IPersistentManager persistentManager;
    private IInteractionManager interactionManager;
	private Graph graph;
	private GraphUtil graphUtil;

	public DefaultGraphProvider(IPersistentManager persistentManager,IInteractionManager interactionManager) {
		this.persistentManager = persistentManager;
        this.interactionManager = interactionManager;
		this.graphUtil = new GraphUtil((KnowledgeBase) persistentManager.getKnowledgeBase());
	}

	
	public Graph buildNewGraph() throws Exception {
		Map map = new HashMap();
		graph = graphUtil.createEmptyGraph();
		Relation relation = null;
		Entity entity1 = null;

		Node node1 = null;
		Node node2 = null;
		Node connectorNode = null;
        boolean useUserColors = interactionManager.getColors()!=null;

		ConceptList conceptList = persistentManager.getConcepts();
		InstanceList instanceList = persistentManager.getInstances();
		RelationList relationList = persistentManager.getRelations();

		if (Configurations.getConfiguration(persistentManager.getKnowledgeBaseName()).getVisualizationConfiguration().displayOrphanConcepts()) {
			// Creates nodes for concepts
			for (int i = 0; i < conceptList.size(); i++) {
				entity1 = conceptList.getEntity(i);
				node1 = graphUtil.createConceptNode(String.valueOf(entity1.getId()), entity1.getIdentifier());
				map.put(entity1, node1);
			}
		}
		if (Configurations.getConfiguration(persistentManager.getKnowledgeBaseName()).getVisualizationConfiguration().displayOrphanInstances()) {

			// Creates nodes for instances
			for (int i = 0; i < instanceList.size(); i++) {
				entity1 = instanceList.getEntity(i);
				node1 = graphUtil.createInstanceNode(String.valueOf(entity1.getId()), entity1.toString());
				map.put(entity1, node1);
			}
		}

		// System.out.println(list);
		for (int i = 0; i < relationList.size(); i++) {
			relation = relationList.getRelation(i);
			System.out.println(relation);
			
			node1 = (Node) map.get(relation.getLeftEntity());
			if (node1 == null) {
				if (relation.getLeftEntity().isConcept()) {
					node1 = graphUtil.createConceptNode(String.valueOf(relation.getLeftEntity().getId()), relation.getLeftEntity().getIdentifier());
				}
				if (relation.getLeftEntity().isInstance()) {
					node1 = graphUtil.createInstanceNode(String.valueOf(relation.getLeftEntity().getId()), relation.getLeftEntity().toString());
				}
				if (relation.getLeftEntity().isConnector()) {
					node1 = graphUtil.createConnectorNode(String.valueOf(relation.getLeftEntity().getId()), relation.getLeftEntity().toString());
				}
				if (node1 == null) {
					node1 = graphUtil.createConceptNode(String.valueOf(relation.getLeftEntity().getId()), relation.getLeftEntity().getIdentifier());
				}
				map.put(relation.getLeftEntity(), node1);
                
			}
            if(useUserColors){
                node1.setInt(GraphConstants.USER_KEY, interactionManager.getUserSpecificKeyForColoring(relation.getLeftEntity()));
            }

			node2 = (Node) map.get(relation.getRightEntity());
			if (node2 == null) {
				if (relation.getRightEntity().isConcept()) {
					node2 = graphUtil.createConceptNode(String.valueOf(relation.getRightEntity().getId()), relation.getRightEntity().getIdentifier());
				}
				if (relation.getRightEntity().isInstance()) {
					node2 = graphUtil.createInstanceNode(String.valueOf(relation.getRightEntity().getId()), relation.getRightEntity().toString());
				}
				if (relation.getRightEntity().isConnector()) {
					node2 = graphUtil.createConnectorNode(String.valueOf(relation.getRightEntity().getId()), relation.getRightEntity().getIdentifier());
				}
				if (node2 == null) {
					node2 = graphUtil.createConceptNode(String.valueOf(relation.getRightEntity().getId()), relation.getRightEntity().getIdentifier());
				}
				map.put(relation.getRightEntity(), node2);
			}
            if(useUserColors){
                node2.setInt(GraphConstants.USER_KEY, interactionManager.getUserSpecificKeyForColoring(relation.getRightEntity()));
            }

			// Sets the id of the relation
			if(Configurations.getConfiguration(persistentManager.getKnowledgeBaseName()).getVisualizationConfiguration().displayConnectorsAsNodes()){
				// Only creates the connector node if we need
				connectorNode = graphUtil.createConnectorNode(String.valueOf(relation.getId()), relation.getConnector().getIdentifier());
                if(useUserColors){
                    connectorNode.setInt(GraphConstants.USER_KEY, interactionManager.getUserSpecificKeyForColoring(relation.getConnector()));
                }

				RelationNode relationNode = graphUtil.createTwoEdgesRelation(node1, connectorNode, node2);

                if(useUserColors){
                    int key = interactionManager.getUserSpecificKeyForColoring(relation);
                    relationNode.getEdge1().setInt(GraphConstants.USER_KEY, key);
                    relationNode.getEdge2().setInt(GraphConstants.USER_KEY, key);
                }
                
                
			}else{
				RelationNode relationNode = graphUtil.createRelation(node1, relation.getConnector().getIdentifier() , node2, relation.getId());
                int key = interactionManager.getUserSpecificKeyForColoring(relation);
                relationNode.getEdge1().setInt(GraphConstants.USER_KEY, key);

			}
			
			
		}

		return graph;

	}

	/*
	 * public static List getConnectorList(){ if(connectorList.isEmpty()){
	 * connectorList.add("é um"); connectorList.add("é instância de");
	 * connectorList.add("nova relação"); } return connectorList; }
	 * 
	 * public static void addConnector(String connectorName){
	 * if(!connectorList.contains(connectorName)){
	 * connectorList.add(connectorName); } }
	 */
	public static String getConnectorId(String connectorName) {
		return connectorName;
	}

	public Node buildNode(long id, String label, int type) {
		return graphUtil.createNode(String.valueOf(id),label,type);
	}

	public Edge buildEdge(Node node1, Node node2, String label) {
		//return graphUtil.createRelation(node1,node2,String.valueOf(id),label,type);
		return null;
	}

	public Graph getCurrentGraph() {
		return graph;
	}

	public boolean removeNode(Node node) {
		return graph.removeNode(node);
	}
	public boolean removeEdge(Edge edge) {
		return graph.removeEdge(edge);
	}


	public GraphUtil getGraphUtil() {
		return graphUtil;
	}


	public Node createInstanceNode(String id, String label) {
		return graphUtil.createInstanceNode(id,label);
	}


	public Node createConceptNode(String id, String label) {
		return graphUtil.createConceptNode(id,label);
	}


	public RelationNode createRelation(Node node1, Connector connector, String relationId, Node node2) {
		return graphUtil.createRelation(node1,connector,relationId,node2);
	}

}
