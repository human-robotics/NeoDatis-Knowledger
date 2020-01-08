/* 
 * $RCSfile: DefaultInteractionManager.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph.defaults;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.gui.graph.IActionManager;
import org.neodatis.knowledger.gui.graph.IInteractionManager;
import org.neodatis.knowledger.gui.graph.IPersistentManager;
import org.neodatis.knowledger.gui.graph.MenuEntry;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;
import org.neodatis.knowledger.gui.graph.basic.IGraphProvider;
import org.neodatis.knowledger.gui.graph.panel.KnowledgerGraphPanel;
import org.neodatis.tool.ILogger;

import prefuse.visual.VisualItem;


public abstract class DefaultInteractionManager implements IInteractionManager , ActionListener{
    
    private IPersistentManager persistentManager;
    private IGraphProvider graphProvider;
    private KnowledgerGraphPanel knowledgerGraphPanel;
    private ILogger logger;
    private IActionManager actionManager;
    private ActionManagerHelper actionManagerHelper;
    

    

    public DefaultInteractionManager() {
    }

    public MenuEntry[] buildMenuEntry(VisualItem vi, String id, int type, String name) {
        return null;
    }

    public String getToolTip(String id, int type, String name) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(name).append(" (").append(id).append(")");
        return buffer.toString();
    }

    public boolean hasPopupMenu() {
        return true;
    }

    public boolean isLocalAction() {
        return true;
    }

    public String execute(MenuEntry menuEntry , int x, int y) {
        return null;
    }

    public void manage(VisualItem vi, String id, int type, String label, int x, int y){
        if(isLocalAction() && hasPopupMenu()){
            buildPopup(vi, id, type, label, x, y).show(knowledgerGraphPanel.getPrefuseGraphBuilder().getDisplay(), x, y);
        }
        
        if(!isLocalAction() && !hasPopupMenu()){
            manageServletUpdate(vi,id,type,label,x,y);
        }
    }
    private void manageServletUpdate(VisualItem vi, String id, int type, String label, int x, int y) {
        String [] frames = getFramesToUpdate(type, id);
        String servletName = getServletNameForFrameUpdate();
        if(frames!=null){
            for(int i=0;i<frames.length;i++){
                knowledgerGraphPanel.openInExternalBrowser(servletName, getPersistentManager().getKnowledgeBaseName(), id, frames[i]);
            }
        }else{
            System.out.println("No frames were declared for external update");
        }
    }

    /**
     * @return Returns the graphProvider.
     */
    public IGraphProvider getGraphProvider() {
        return graphProvider;
    }

    /**
     * @param graphProvider The graphProvider to set.
     */
    public void setGraphProvider(IGraphProvider graphProvider) {
        this.graphProvider = graphProvider;
    }

    /**
     * @return Returns the persistentManager.
     */
    public IPersistentManager getPersistentManager() {
        return persistentManager;
    }

    /**
     * @param persistentManager The persistentManager to set.
     */
    public void setPersistentManager(IPersistentManager persistentManager) {
        this.persistentManager = persistentManager;
    }

    public JPopupMenu buildPopup(VisualItem vi,String id, int type,String name,int x,int y) {

        System.out.println("Building popup menu for object " + vi.getString(GraphConstants.LABEL));
        MenuEntry[] menuEntries = buildMenuEntry(vi,id,type,name);
        
        JPopupMenu pmenu = new JPopupMenu();

        JMenuItem menuItem = null;
        for (int i = 0; i < menuEntries.length; i++) {
            menuItem = new DefaultMenuItem(menuEntries[i],x,y);
            menuItem.setActionCommand(menuEntries[i].getAction());
            menuItem.addActionListener(this);
            pmenu.add(menuItem);

        }
        return pmenu;
    }
    public void actionPerformed(ActionEvent e) {
        DefaultMenuItem item = (DefaultMenuItem) e.getSource();
        try {
            System.out.println("Action performed triggered");
            execute(item.getMenuEntry(),item.getXX(),item.getYY());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
    }

    public IActionManager getActionManager() {
        return actionManager;
    }

    /**
     * @param actionManager The actionManager to set.
     */
    public void setActionManager(IActionManager actionManager) {
        this.actionManager = actionManager;
    }

    /**
     * @param actionManagerHelper The actionManagerHelper to set.
     */
    public void setActionManagerHelper(ActionManagerHelper actionManagerHelper) {
        this.actionManagerHelper = actionManagerHelper;
    }

    /**
     * @param knowledgerGraphPanel The knowledgerGraphPanel to set.
     */
    public void setKnowledgerGraphPanel(KnowledgerGraphPanel knowledgerGraphPanel) {
        this.knowledgerGraphPanel = knowledgerGraphPanel;
    }

    /**
     * @param logger The logger to set.
     */
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

	/**
	 * @return Returns the knowledgerGraphPanel.
	 */
	public KnowledgerGraphPanel getKnowledgerGraphPanel() {
		return knowledgerGraphPanel;
	}

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.IInteractionManager#getColors()
     */
    public int[] getColors() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.IInteractionManager#getFramesToUpdate(int, long)
     */
    public String[] getFramesToUpdate(int objectType, String id) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.IInteractionManager#getServletNameForFrameUpdate()
     */
    public String getServletNameForFrameUpdate() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.IInteractionManager#getUserSpecificKeyForColoring(int, long)
     */
    public int getUserSpecificKeyForColoring(KnowledgerObject kobject) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.IInteractionManager#centerOn()
     */
    public long centerOn() {
        return -1;
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.IFilter#display(java.lang.String, int, java.lang.String)
     */
    public boolean display(Entity entity) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.IInteractionManager#canEdit()
     */
    public boolean canEdit() {
        return true;
    }
    
    
    
}
