/* 
 * $RCSfile: EntityWrapper.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class EntityWrapper {
	public static EntityWrapper EMPTY_WRAPPER = new EntityWrapper(null);
	
	private KnowledgerObject knowledgerObject;
    public EntityWrapper(KnowledgerObject knowledgerObject){
        this.knowledgerObject = knowledgerObject;
    }
    
    public boolean isEmpty(){
    	return knowledgerObject == null;
    }
    public boolean isNotEmpty(){
    	return !isEmpty();
    }
    public String toString(){
        
    	if(isEmpty()){
    		return "";
    	}
    	
    	StringBuffer buffer = new StringBuffer();
        if(knowledgerObject.getId()!=-1){
            if(knowledgerObject.getClass()==Instance.class){
                Instance instance = (Instance)knowledgerObject;
                try {
                    buffer.append(knowledgerObject.toString()).append(" : ").append(instance.getConcept().getIdentifier());
                	//buffer.append(instance.getConcept().getIdentifier()).append(" : ").append(knowledgerObject.toString());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println(e.getMessage());
                    buffer.append(knowledgerObject.toString()).append(" : ").append(getType(knowledgerObject));
                }
            }else{
            	buffer.append(knowledgerObject.getIdentifier());                
            }
        }else{
            buffer.append(knowledgerObject.getIdentifier());
        }
        return buffer.toString();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if(obj == null  || obj.getClass() != EntityWrapper.class){
            return false;
        }
        EntityWrapper ew = (EntityWrapper) obj;
        if(isEmpty() || ew.isEmpty()){
        	return false;
        }
        return knowledgerObject.equals(ew.getDialoyObject());
    }
    /**
     * @param knowledgerObject
     * @return
     */
    private String getType(KnowledgerObject knowledgerObject) {
        if(knowledgerObject.getClass() == Concept.class){
            return "Concept";
        }
        if(knowledgerObject.getClass() == Instance.class){
            return "Instance";
        }
        if(knowledgerObject.getClass() == Connector.class){
            return "Connector";
        }
        return "Unknown";
    }
    public KnowledgerObject getDialoyObject(){
        return knowledgerObject;
    }
    /**@todo Which is the right ?
     * 
     * @return
     */
    public Entity getEntity(){
    	return (Entity) knowledgerObject;
    }
    
}
