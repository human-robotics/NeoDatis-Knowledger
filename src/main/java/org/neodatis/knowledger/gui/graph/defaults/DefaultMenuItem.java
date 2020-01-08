/* 
 * $RCSfile: DefaultMenuItem.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph.defaults;

import javax.swing.JMenuItem;

import org.neodatis.knowledger.gui.graph.MenuEntry;


public class DefaultMenuItem extends JMenuItem {
    private MenuEntry menuEntry;
    private int xx;
    private int yy;
    
    public DefaultMenuItem(MenuEntry menuEntry,int x,int y){
        super(menuEntry.getText());
        this.menuEntry = menuEntry;
        this.xx = x;
        this.yy = y;
    }
    
    public String toString() {
        return menuEntry.toString();
    }

    /**
     * @return Returns the menuEntry.
     */
    public MenuEntry getMenuEntry() {
        return menuEntry;
    }

    /**
     * @return Returns the x.
     */
    public int getXX() {
        return xx;
    }

    /**
     * @return Returns the y.
     */
    public int getYY() {
        return yy;
    }
    

}
