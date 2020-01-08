/* 
 * $RCSfile: Graph.java,v $
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

public class Graph {
    List graphRelations;
    public Graph() {
        super();
        graphRelations=new ArrayList();
    }
    public void add(GraphRelation relation){
        graphRelations.add(relation);
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(graphRelations);
        return buffer.toString();
    }

}
