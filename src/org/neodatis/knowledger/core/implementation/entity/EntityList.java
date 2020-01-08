/* 
 * $RCSfile: EntityList.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;


/**
 * <p>
 * 
 * </p>
 * 
 */
public class EntityList extends ArrayList<Entity>{

    public EntityList(){
        super();
    }
    public EntityList(KnowledgeBase knowledgeBase,Collection<Entity> collection){
    	for( Entity e: collection){
    		e.setKnowledgeBase(knowledgeBase);
    		add(e);
    	}
    }
    public EntityList(Collection collection){
        super(collection);
    }

    public Entity getEntity(int index) {
        return (Entity) get(index);
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.core.interfaces.entity.IEntityList#getEntityFromId(long)
     */
    public Entity getEntityFromId(String id) {
        for(int i=0;i<size();i++){
            if(getEntity(i).getId().equals(id)){
                return getEntity(i);
            }
        }
        return null;
    }

 
    public List<String> getIdentifiers(){
        List<String> result = new ArrayList<String>();
        for(int i=0;i<size();i++){
            result.add(getEntity(i).getIdentifier());
        }
        return result;
    }
    
	public EntityList filter(ICriteria criteria) {
		EntityList sublist = new EntityList();
		for (Entity r : this) {
			if (criteria.match(r)) {
				sublist.add(r);
			}
		}
		return sublist;
	}
    public ConceptList getConcepts(){
    	ConceptList clist = new ConceptList();
    	Entity entity = null;
    	for(int i=0;i<size();i++){
    		entity = getEntity(i);
    		if(entity.isConcept()){
    			clist.add(entity);
    		}
    	}
    	return clist;
    }
    public InstanceList getInstances(){
    	InstanceList ilist = new InstanceList();
    	Entity entity = null;
    	for(int i=0;i<size();i++){
    		entity = getEntity(i);
    		if(entity.isInstance()){
    			ilist.add(entity);
    		}
    	}
    	return ilist;
    }
    public ConnectorList getConnectors(){
    	ConnectorList clist = new ConnectorList();
    	Entity entity = null;
    	for(int i=0;i<size();i++){
    		entity = getEntity(i);
    		if(entity.isConnector()){
    			clist.add(entity);
    		}
    	}
    	return clist;
    }
}
