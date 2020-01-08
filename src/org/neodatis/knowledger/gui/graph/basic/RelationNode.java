/* 
 * $RCSfile: RelationNode.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph.basic;

import prefuse.data.Edge;
import prefuse.data.Node;

/**
 * A simple value object to keep graphic data about a relation 
 * 
 * <pre>
 * - The left node : node1
 * - The connector node
 * - The right node : node2
 * - The two edges : edge1 and edge2
 * 
 * When the Knowledger is configured as 'show connector as node =  false', node2 and edge2 are null 
 * </pre>
 * @author olivier s
 *
 */
public class RelationNode {
    private Node node1;
    private Node node2;
    private Node ConnectorNode;
    private Edge edge1;
    private Edge edge2; 
    
    private boolean displayConnectorAsNode;
    
    
    
    public RelationNode(Node node1, Node node, Node node2,Edge edge1,Edge edge2) {
        // TODO Auto-generated constructor stub
        this.node1 = node1;
        ConnectorNode = node;
        this.node2 = node2;
        this.edge1 = edge1;
        this.edge2 = edge2;
        displayConnectorAsNode = true;
    }
    
    public RelationNode(Node node1, Node node2,Edge edge1) {
        // TODO Auto-generated constructor stub
        this.node1 = node1;
        this.node2 = node2;
        this.edge1 = edge1;
        displayConnectorAsNode = false;
    }
    /**
     * @return Returns the connectorNode.
     */
    public Node getConnectorNode() {
        return ConnectorNode;
    }
    /**
     * @return Returns the node1.
     */
    public Node getNode1() {
        return node1;
    }
    /**
     * @return Returns the node2.
     */
    public Node getNode2() {
        return node2;
    }
    /**
     * @return Returns the edge1.
     */
    public Edge getEdge1() {
        return edge1;
    }
    /**
     * @return Returns the edge2.
     */
    public Edge getEdge2() {
        return edge2;
    }

	public boolean displayConnectorAsNode() {
		return displayConnectorAsNode;
	}

	public void setDisplayConnectorAsNode(boolean displayConnectorAsNode) {
		this.displayConnectorAsNode = displayConnectorAsNode;
	}
    
    
    

}
