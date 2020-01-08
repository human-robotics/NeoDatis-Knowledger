/* 
 * $RCSfile: GraphRelation.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.parser.graph.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class GraphRelation {
    List nodes;
    
    public GraphRelation(){
        nodes=new ArrayList();
    }
    public GraphRelation(GraphNode leftPart){
        nodes=new ArrayList();
        nodes.add(leftPart);
    }
    public GraphRelation(GraphNode leftPart,ConnectorNode connector,GraphNode rightPart){
        nodes=new ArrayList();
        nodes.add(leftPart);
        nodes.add(connector);
        nodes.add(rightPart);
    }
    public void add(GraphNode node){
        nodes.add(node);
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<nodes.size();i++){
            if(i!=0){
                buffer.append("->");
            }
            buffer.append(nodes.get(i));
        }
        return buffer.toString();
    }
}
