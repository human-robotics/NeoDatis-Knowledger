/* 
 * $RCSfile: CenterAction.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph;

import prefuse.action.Action;

public class CenterAction extends Action {
    private IInteractionManager interactionManager;
    public CenterAction(IInteractionManager manager){
        this.interactionManager = manager;
    }
    public void run(double arg0) {
        System.out.println("Executing center on action");
        long id = interactionManager.centerOn();
        interactionManager.getKnowledgerGraphPanel().centerOn(id);

    }

}
