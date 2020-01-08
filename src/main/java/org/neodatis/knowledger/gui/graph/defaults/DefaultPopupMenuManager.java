/* 
 * $RCSfile: DefaultPopupMenuManager.java,v $
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

import org.neodatis.knowledger.gui.graph.IActionManager;
import org.neodatis.knowledger.gui.graph.IPopupManager;
import org.neodatis.knowledger.gui.graph.MenuEntry;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;
import org.neodatis.knowledger.gui.tool.Messages;

import prefuse.visual.VisualItem;


/**
 * Class that builds all the graph popup menus
 * @author olivier s
 *
 */
public class DefaultPopupMenuManager implements ActionListener , IPopupManager{
    
    private IActionManager actionManager;
    
    public DefaultPopupMenuManager(IActionManager actionManager){
        this.actionManager = actionManager;
    }
    private MenuEntry [] buildMenuEntry(VisualItem vi,String id, int type,String name) {

        MenuEntry [] menuEntry = null;
        
        switch (type) {
		case GraphConstants.CONCEPT:
            menuEntry = new MenuEntry[6];
            menuEntry[0] = new MenuEntry(vi,id,type,name,Messages.getString("popup.new.subconcept"),DefaultActionManager.ACTION_NEW_SUB_CONCEPT);
            menuEntry[1] = new MenuEntry(vi,id,type,name,Messages.getString("popup.new.instance"),DefaultActionManager.ACTION_NEW_INSTANCE);
            menuEntry[2] = new MenuEntry(vi,id,type,name,Messages.getString("popup.new.connected.concept"),DefaultActionManager.ACTION_NEW_CONNECTED_CONCEPT);
            menuEntry[3] = new MenuEntry(vi,id,type,name,Messages.getString("popup.delete.concept"),DefaultActionManager.ACTION_DELETE_CONCEPT);
            menuEntry[4] = new MenuEntry(vi,id,type,name,Messages.getString("popup.center.on"),DefaultActionManager.ACTION_CENTER_ON_CONCEPT);
            menuEntry[5] = new MenuEntry(vi,id,type,name,Messages.getString("popup.open"),DefaultActionManager.ACTION_OPEN_IN_EXTERNAL_BROWSER);
            return menuEntry;
		case GraphConstants.INSTANCE:
            menuEntry = new MenuEntry[5];
            menuEntry[0] = new MenuEntry(vi,id,type,name,Messages.getString("popup.new.connected.instance"),DefaultActionManager.ACTION_NEW_CONNECTED_INSTANCE);
            menuEntry[1] = new MenuEntry(vi,id,type,name,Messages.getString("popup.delete.instance"),DefaultActionManager.ACTION_DELETE_INSTANCE);
            menuEntry[2] = new MenuEntry(vi,id,type,name,Messages.getString("pupup.center.on.instance"),DefaultActionManager.ACTION_CENTER_ON_INSTANCE);
            menuEntry[3] = new MenuEntry(vi,id,type,name,Messages.getString("popup.center.on.concept"),DefaultActionManager.ACTION_CENTER_ON_CONCEPT);
            menuEntry[4] = new MenuEntry(vi,id,type,name,Messages.getString("popup.open"),DefaultActionManager.ACTION_OPEN_IN_EXTERNAL_BROWSER);
            return menuEntry;
		case GraphConstants.CONNECTOR:
            menuEntry = new MenuEntry[5];
            menuEntry[0] = new MenuEntry(vi,id,type,name,Messages.getString("popup.delete.relation"),DefaultActionManager.ACTION_DELETE_RELATION);
            menuEntry[1] = new MenuEntry(vi,id,type,name,Messages.getString("popup.center.on.relation"),DefaultActionManager.ACTION_CENTER_ON_RELATION);
            menuEntry[2] = new MenuEntry(vi,id,type,name,Messages.getString("popup.center.on.left"),DefaultActionManager.ACTION_CENTER_ON_LEFT);
            menuEntry[3] = new MenuEntry(vi,id,type,name,Messages.getString("popup.center.on.right"),DefaultActionManager.ACTION_CENTER_ON_RIGHT);
            menuEntry[4] = new MenuEntry(vi,id,type,name,Messages.getString("popup.open"),DefaultActionManager.ACTION_OPEN_IN_EXTERNAL_BROWSER);
            return menuEntry;
        }
        return new MenuEntry[0];
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
        	actionManager.execute(item.getMenuEntry(),item.getXX(),item.getYY());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
    }
}
