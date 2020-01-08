/* 
 * $RCSfile: ConnectorNode.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.parser.graph.vo;

public class ConnectorNode extends GraphNode {
    /**
     * 
     */
    public ConnectorNode() {
        super();
    }
    public ConnectorNode(String name) {
        super(name);
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("(").append(getName()).append(")");
        return buffer.toString();
    }

}
