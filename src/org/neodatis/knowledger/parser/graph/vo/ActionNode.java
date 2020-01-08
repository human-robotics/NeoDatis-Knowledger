/* 
 * $RCSfile: ActionNode.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.parser.graph.vo;

public class ActionNode extends GraphNode {
    /**
     * 
     */
    public ActionNode() {
        super();
    }
    public ActionNode(String name) {
        super(name);
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[@").append(getName()).append("]");
        return buffer.toString();
    }

}
