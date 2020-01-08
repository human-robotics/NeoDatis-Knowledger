/* 
 * $RCSfile: IActionManager.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph;

import org.neodatis.knowledger.core.implementation.entity.Connector;

import prefuse.data.Node;


/**
 * Interface for actions triggers from popup menu or any other user interaction
 * @author olivier s
 *
 */
public interface IActionManager {
    
    public String execute(MenuEntry menuEntry,int x,int y) throws Exception;
    public String connect(Node node1, Connector connector, Node node2) throws Exception;
    public String newConcept(int x,int y,boolean editing) throws Exception;

}
