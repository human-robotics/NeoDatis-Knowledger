/* 
 * $RCSfile: GraphNode.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.parser.graph.vo;

/**
 * <p>
 * 
 * </p>
 * 
 */
public abstract class GraphNode {
    protected String name;
    public GraphNode(){
    }
    
    public GraphNode(String name){
        this.name=name;
    }
    
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
