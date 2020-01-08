/* 
 * $RCSfile: ConceptList.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation.entity;

import java.util.Collection;
import java.util.Iterator;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;


/**
 * <p>
 * 
 * </p>
 * 
 */
public class ConceptList extends EntityList{

    public ConceptList(){
        super();
    }
    public ConceptList(KnowledgeBase knowledgeBase, Collection collection){
        super(knowledgeBase,collection);
    }
    /* (non-Javadoc)
     * @see org.neodatis.knowledger.core.interfaces.entity.IConceptList#getConcept(int)
     */
    public Concept getConcept(int index) {
        return (Concept) get(index);
    }
    

    public ConceptList getConcepts(ICriteria criteria){
    	ConceptList sublist = new ConceptList();
    	Iterator iterator = iterator();
    	Concept concept = null;
    	while(iterator.hasNext()){
    		concept = (Concept) iterator.next();
    		if(criteria.match(concept)){
    			sublist.add(concept);
    		}
    	}
    	return sublist;
    }

 

}
