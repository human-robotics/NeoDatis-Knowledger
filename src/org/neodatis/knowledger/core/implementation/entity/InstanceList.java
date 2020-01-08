/* 
 * $RCSfile: InstanceList.java,v $
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
import java.util.List;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;


/**
 * <p>
 * 
 * </p>
 * 
 */
public class InstanceList extends EntityList {
    public InstanceList(){
        super();
    }
    
    public InstanceList(Collection collection){
        super(collection);
    }

    public InstanceList(KnowledgeBase knowledgeBase,Collection collection){
        super(knowledgeBase,collection);
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.core.interfaces.entity.IInstanceList#getInstance(int)
     */
    public Instance getInstance(int index) {
    	Object o = get(index);
    	if(!( o instanceof Instance)){
    		return null;
    	}
        return (Instance) o; 
    }
    public List getValues(){
        List result = new ArrayList();
        for(int i=0;i<size();i++){
            result.add(getInstance(i).getValue());
        }
        return result;
    }
   
    public InstanceList getInstances(ICriteria criteria){
    	EntityList entityList = filter(criteria);
        return new InstanceList(entityList);
    }
    public Instance getFirst(){
    	return getInstance(0);
    }
}
