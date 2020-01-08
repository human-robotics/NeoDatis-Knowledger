package org.neodatis.knowledger.gui.graph.basic.example;

import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;
import org.neodatis.knowledger.gui.graph.basic.IGraphProvider;
import org.neodatis.knowledger.gui.graph.basic.RelationNode;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;


public class MockGraphProvider implements IGraphProvider {
	private Graph graph;
	private static KnowledgerGraphBuilder kgb;

	public MockGraphProvider(KnowledgerGraphBuilder kgb) {
		this.kgb = kgb;
	}

	/** Gets the current graph
	 * 
	 */
	public Graph getCurrentGraph(){
		return graph;
	}
	
	/** Build a new graph*/
	public Graph buildNewGraphSimple() throws Exception {
		graph = new Graph(true);

		graph.addColumn("label", String.class);
		graph.addColumn("type", Integer.TYPE);
		graph.addColumn("id", Long.TYPE, new Long(0));
		graph.addColumn("tooltip", String.class);

		Node object = buildNode(1, "Object", GraphConstants.CONCEPT);
		Node animal = buildNode(2, "Animal", GraphConstants.CONCEPT);
		Edge edge1 = buildEdge(animal, object, "is a");

		return graph;
	}
	
	/** Build a new graph*/
	public Graph buildNewGraph() throws Exception {
		graph = new Graph(true);

		graph.addColumn("label", String.class);
		graph.addColumn("type", Integer.TYPE);
		graph.addColumn("id", Long.TYPE, new Long(0));
		graph.addColumn("tooltip", String.class);

		Node object = buildNode(1, "Object", GraphConstants.CONCEPT);
		Node animal = buildNode(2, "Animal", GraphConstants.CONCEPT);
		Node olivier = buildNode(3, "olivier", GraphConstants.INSTANCE);
		Node aisa = buildNode(4, "aísa", GraphConstants.INSTANCE);
		Node karine = buildNode(5, "karine", GraphConstants.INSTANCE);

		Edge edge1 = buildEdge(animal, object, "is a");
		Edge edge2 = buildEdge(olivier, animal, "is instance of");
		Edge edge3 = buildEdge(aisa, animal, "is instance of");
		Edge edge4 = buildEdge(karine, animal, "is instance of");
		Edge edge5 = buildEdge(aisa, olivier, "is married to");
		Edge edge6 = buildEdge(olivier, karine, "is father of");
		Edge edge7 = buildEdge(aisa, karine, "is mother of");

		return graph;
	}

	public Node buildNode(long id, String label, int type) {
		Node node = graph.addNode();
		node.setString("label", label);
		node.setString("tooltip", label + " - " + id);
		node.setInt("type", type);
		node.setLong("id", id);
		return node;
	}

	public Edge buildEdge(Node node1, Node node2, String label) {
		if (kgb.mode == 0) {
			Edge edge = graph.addEdge(node1, node2);
			edge.set("label", label);
			edge.set("tooltip", label);
			edge.setInt("type", GraphConstants.CONNECTOR);
			return edge;
		}
		Node connectorNode = buildNode(0, label, GraphConstants.CONNECTOR);
		Edge edge1 = graph.addEdge(node1, connectorNode);
		Edge edge2 = graph.addEdge(connectorNode, node2);
		edge1.set("label", label + " start");
		edge1.set("tooltip", label + " start");
		edge1.setInt("type", GraphConstants.CONNECTOR);

		edge2.set("label", label + " end");
		edge2.set("tooltip", label + " end");
		edge2.setInt("type", GraphConstants.CONNECTOR);
		return edge1;

	}

	public boolean removeNode(Node node) {
		return graph.removeNode(node);
	}
	public boolean removeEdge(Edge edge) {
		return graph.removeEdge(edge);
	}

	public Node createInstanceNode(String id, String label) {
		return buildNode(Long.parseLong(id),label,GraphConstants.INSTANCE);
	}

	public Node createConceptNode(String id, String label) {
		return buildNode(Long.parseLong(id),label,GraphConstants.CONCEPT);
	}

	public RelationNode createRelation(Node node1, Connector connector, String relationId, Node node2) {
		return null;
	}

}
