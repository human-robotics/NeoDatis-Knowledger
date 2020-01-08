/* 
 * $RCSfile: HelpDeskInteractionManager.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:56:11 $
 * 
 * Copyright 2003 Cetip
 */
package test;

import java.awt.Color;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.gui.graph.defaults.KnowledgerInteractionManager;


public class HelpDeskInteractionManager extends KnowledgerInteractionManager {

   

    public boolean canDragAndDrop(String sourceObjectId) {
    	return true;
	}

	public ConnectorList getConnectorListOnDragOnDrop(String sourceObjectId) {
		try {
			ConnectorList clist = new ConnectorList();
			clist.add(getPersistentManager().getConnectorFromName("is-instance-of"));
			return clist;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ConnectorList();
	}

	public boolean display(Entity entity) {
        if(entity.isInstance()){
            return true;
        }
        if(true){
            return true;
        }
        
        if(entity.getIdentifier().equalsIgnoreCase("Conteudo")){
            return false;
        }
        if(entity.getIdentifier().equalsIgnoreCase("Atividade Colaborativa")){
            return false;
        }
        if(entity.getIdentifier().equalsIgnoreCase("Avaliacao")){
            return false;
        }

        return true;
        /*
        if(entity.getIdentifier().startsWith("C")){
            return true;
        }
        return false;
        */
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.defaults.KnowledgerInteractionManager#centerOn()
     */
    public long centerOn() {
        try {
            Concept root = getPersistentManager().getConceptFromName("HelpDesk");
            return root.getId();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } 
        
        
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.defaults.KnowledgerInteractionManager#hasPopupMenu()
     */
    public boolean hasPopupMenu() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.defaults.KnowledgerInteractionManager#isLocalAction()
     */
    public boolean isLocalAction() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.defaults.DefaultInteractionManager#canEdit()
     */
    public boolean canEdit() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.defaults.DefaultInteractionManager#getColors()
     */
    public int[] getColors() {
        int [] colors = {Color.BLUE.getRGB(),Color.MAGENTA.getRGB(),Color.BLACK.getRGB(),Color.ORANGE.getRGB(),Color.GRAY.getRGB()};
        return colors;
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.gui.graph.defaults.DefaultInteractionManager#getUserSpecificKeyForColoring(int, java.lang.String)
     */
    public int getUserSpecificKeyForColoring(KnowledgerObject kobject) {
        if(kobject instanceof Instance){
            return 0;
        }
        if(kobject instanceof Concept){
            return 1;
        }
        if(kobject instanceof Connector){
            return 2;
        }
        if(kobject instanceof Relation){
            return 3;
        }
        return 4;
    }
    
    
}
