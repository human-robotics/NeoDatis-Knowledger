/* 

 */
package org.neodatis.knowledger.gui.graph.defaults;

import javax.swing.JOptionPane;

import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.gui.graph.MenuEntry;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;
import org.neodatis.knowledger.gui.tool.Messages;

import prefuse.visual.VisualItem;


public class KnowledgerInteractionManager extends DefaultInteractionManager {


    public KnowledgerInteractionManager() {
    
        super();
    }

    public void init() throws Exception {
	}
    
    public MenuEntry [] buildMenuEntry(VisualItem vi,String id, int type,String name) {

        MenuEntry [] menuEntry = null;
        
        switch (type) {
        case GraphConstants.CONCEPT:
            menuEntry = new MenuEntry[5];
            menuEntry[0] = new MenuEntry(vi,id,type,name,Messages.getString("popup.new.subconcept"),DefaultActionManager.ACTION_NEW_SUB_CONCEPT);
            menuEntry[1] = new MenuEntry(vi,id,type,name,Messages.getString("popup.new.instance"),DefaultActionManager.ACTION_NEW_INSTANCE);
            menuEntry[2] = new MenuEntry(vi,id,type,name,Messages.getString("popup.new.connected.concept"),DefaultActionManager.ACTION_NEW_CONNECTED_CONCEPT);
            menuEntry[3] = new MenuEntry(vi,id,type,name,Messages.getString("popup.delete.concept"),DefaultActionManager.ACTION_DELETE_CONCEPT);
            menuEntry[4] = new MenuEntry(vi,id,type,name,Messages.getString("popup.center.on"),DefaultActionManager.ACTION_CENTER_ON_CONCEPT);
            //menuEntry[5] = new MenuEntry(vi,id,type,name,Messages.getString("popup.open"),DefaultActionManager.ACTION_OPEN_IN_EXTERNAL_BROWSER);
            return menuEntry;
        case GraphConstants.INSTANCE:
            menuEntry = new MenuEntry[4];
            menuEntry[0] = new MenuEntry(vi,id,type,name,Messages.getString("popup.new.connected.instance"),DefaultActionManager.ACTION_NEW_CONNECTED_INSTANCE);
            menuEntry[1] = new MenuEntry(vi,id,type,name,Messages.getString("popup.delete.instance"),DefaultActionManager.ACTION_DELETE_INSTANCE);
            menuEntry[2] = new MenuEntry(vi,id,type,name,Messages.getString("pupup.center.on.instance"),DefaultActionManager.ACTION_CENTER_ON_INSTANCE);
            menuEntry[3] = new MenuEntry(vi,id,type,name,Messages.getString("popup.center.on.concept"),DefaultActionManager.ACTION_CENTER_ON_CONCEPT);
            //menuEntry[4] = new MenuEntry(vi,id,type,name,Messages.getString("popup.open"),DefaultActionManager.ACTION_OPEN_IN_EXTERNAL_BROWSER);
            return menuEntry;
        case GraphConstants.CONNECTOR:
            menuEntry = new MenuEntry[4];
            menuEntry[0] = new MenuEntry(vi,id,type,name,Messages.getString("popup.delete.relation"),DefaultActionManager.ACTION_DELETE_RELATION);
            menuEntry[1] = new MenuEntry(vi,id,type,name,Messages.getString("popup.center.on.relation"),DefaultActionManager.ACTION_CENTER_ON_RELATION);
            menuEntry[2] = new MenuEntry(vi,id,type,name,Messages.getString("popup.center.on.left"),DefaultActionManager.ACTION_CENTER_ON_LEFT);
            menuEntry[3] = new MenuEntry(vi,id,type,name,Messages.getString("popup.center.on.right"),DefaultActionManager.ACTION_CENTER_ON_RIGHT);
            //menuEntry[4] = new MenuEntry(vi,id,type,name,Messages.getString("popup.open"),DefaultActionManager.ACTION_OPEN_IN_EXTERNAL_BROWSER);
            return menuEntry;
        }
        return new MenuEntry[0];
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.defaults.DefaultInteractionManager#execute(org.neodatis.knowledger.gui.graph.MenuEntry)
     */
    public String execute(MenuEntry menuEntry,int x,int y) {
        try {
            return getActionManager().execute(menuEntry,x,y);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error:" + e.getMessage());
        }
        return null;
    }

    public String getToolTip(String id, int type, String name) {
        return super.getToolTip(id,type,name);
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.defaults.DefaultInteractionManager#hasPopupMenu()
     */
    public boolean hasPopupMenu() {
        // TODO Auto-generated method stub
        return super.hasPopupMenu();
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.defaults.DefaultInteractionManager#isLocalAction()
     */
    public boolean isLocalAction() {
        // TODO Auto-generated method stub
        return super.isLocalAction();
    }

    public long centerOn() {
        return -1;
    }

	public boolean canDragAndDrop(String sourceObjectId) {
		return true;
	}

	public ConnectorList getConnectorListOnDragOnDrop(String sourceObjectId) {
		return getPersistentManager().getConnectors();
	}

    

}
