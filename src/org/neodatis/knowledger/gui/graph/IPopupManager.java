/* 
 * $RCSfile: IPopupManager.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph;

import javax.swing.JPopupMenu;

import prefuse.visual.VisualItem;


/** The interface of the Popup menu builder
 * 
 * @author olivier s
 *
 */
public interface IPopupManager {
    public JPopupMenu buildPopup(VisualItem vi,String id, int type,String label,int x,int y);
}
