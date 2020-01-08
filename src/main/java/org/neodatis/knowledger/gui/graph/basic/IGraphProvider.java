package org.neodatis.knowledger.gui.graph.basic;

import org.neodatis.knowledger.core.implementation.entity.Connector;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;



/** Interface of the prefuse graph builders
 * 
 * @author olivier s
 *
 */
public interface IGraphProvider {
	/**Returns the current graph
	 * 
	 * @return 
	 */
	Graph getCurrentGraph();
	
	/** Builds a new graph*/
	Graph buildNewGraph() throws Exception;
	Node buildNode(long id,String label, int type);
	Edge buildEdge(Node node1, Node node2, String label);
	boolean removeNode(Node node);
	boolean removeEdge(Edge edge);
	public Node createInstanceNode(String id, String label);
	public Node createConceptNode(String id, String label);
	public RelationNode createRelation(Node node1, Connector connector, String relationId, Node node2);

}
