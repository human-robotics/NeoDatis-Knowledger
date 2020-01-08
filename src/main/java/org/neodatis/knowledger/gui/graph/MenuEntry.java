/* 
 * $RCSfile: MenuEntry.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph;

import prefuse.visual.VisualItem;

/**
 * A value object associated to a popup menu to remember data of the object that has been clicked
 * 
 * @author olivier s
 *
 */
public class MenuEntry {
    private String text;
    private String action;
    private String objectName;
    private String objectId;
    private int objectType;
    private VisualItem vi;
    
    public MenuEntry(VisualItem vi,String id,int type, String name, String text,String action){
        this.objectId = id;
        this.objectName = name;
        this.objectType = type;
        this.text = text;
        this.action = action;
        this.vi = vi;
    }
    
    
    public String getAction() {
        return action;
    }
    public String getText() {
        return text;
    }


    /**
     * @return Returns the objectId.
     */
    public String getObjectId() {
        return objectId;
    }


    /**
     * @return Returns the objectName.
     */
    public String getObjectName() {
        return objectName;
    }


    /**
     * @return Returns the objectType.
     */
    public int getObjectType() {
        return objectType;
    }
    
    public String toString() {
        return "Id="+objectId + " - Type=" + objectType + " - Name=" + objectName + " - Action="+action;
    }


    /**
     * @return Returns the object.
     */
    public VisualItem getVisuaItem() {
        return vi;
    }
    
}
