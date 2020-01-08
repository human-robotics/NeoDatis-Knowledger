/* 
 * $RCSfile: IManagerFactory.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph;

import org.neodatis.knowledger.gui.graph.basic.IGraphProvider;


/**The interfac to obtain all managers
 * 
 * @author olivier s
 *
 */
public interface IManagerFactory {
    /** To manage user interaction*/
	IInteractionManager getInteractionManager();
    /** To manage The persistent layer*/
    IPersistentManager getPersistentManager();
    /** To manage grapgh building and modifying*/
    IGraphProvider getGraphProvider();

}
