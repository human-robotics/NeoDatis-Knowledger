/* 
 * $RCSfile: PropositionList.java,v $
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

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;


/**
 * <p>
 * 
 * </p>
 * 
 */
public class PropositionList extends ArrayList{

    public PropositionList(){
        super();
    }

    public PropositionList(KnowledgeBase knowledgeBase,Collection collection){
    	Iterator iterator = collection.iterator();
    	KnowledgerObject knowledgerObject = null;
    	while(iterator.hasNext()){
    		knowledgerObject = (KnowledgerObject)iterator.next();
    		knowledgerObject.setKnowledgeBase(knowledgeBase);
    		add(knowledgerObject);
    	}
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.core.interfaces.entity.IPropositionList#getProposition(int)
     */
    public Proposition getProposition(int index) {
        return (Proposition) get(index);
    }

}
