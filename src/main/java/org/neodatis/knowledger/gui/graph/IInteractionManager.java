/* 
 * $RCSfile: IInteractionManager.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph;

import javax.swing.JPopupMenu;

import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.gui.graph.basic.IGraphProvider;
import org.neodatis.knowledger.gui.graph.defaults.ActionManagerHelper;
import org.neodatis.knowledger.gui.graph.panel.KnowledgerGraphPanel;
import org.neodatis.tool.ILogger;

import prefuse.visual.VisualItem;


public interface IInteractionManager extends IFilter , ICenterOn, IColorer{
	
	public void init() throws Exception;
	
    MenuEntry [] buildMenuEntry(VisualItem vi,String id, int type,String name);
    String getToolTip(String id, int type,String name);
    boolean hasPopupMenu();
    boolean isLocalAction();
    String execute(MenuEntry menuEntry, int x, int y);
    
    public JPopupMenu buildPopup(VisualItem vi,String id, int type,String name,int x,int y);
    public IGraphProvider getGraphProvider();
    public IPersistentManager getPersistentManager();
    public IActionManager getActionManager();
    public KnowledgerGraphPanel getKnowledgerGraphPanel();
    
    public void setPersistentManager(IPersistentManager persistentManager);
    public void setActionManager(IActionManager actionManager);
    public void setActionManagerHelper(ActionManagerHelper actionManagerHelper);
    public void setKnowledgerGraphPanel(KnowledgerGraphPanel knowledgerGraphPanel);
    public void setLogger(ILogger logger);
    public void setGraphProvider(IGraphProvider graphProvider);
    
    // For web frame update
    public String [] getFramesToUpdate(int objectType, String id);
    public String getServletNameForFrameUpdate();
    
    public void manage(VisualItem vi, String id, int type, String label, int x, int y);
    public boolean canEdit();
    public boolean canDragAndDrop(String sourceObjectId);
    public ConnectorList getConnectorListOnDragOnDrop(String sourceObjectId);
    
   
}
