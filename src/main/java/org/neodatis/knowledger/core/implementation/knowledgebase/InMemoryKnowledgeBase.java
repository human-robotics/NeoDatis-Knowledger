/* 
 * $RCSfile: InMemoryKnowledgeBase.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.core.implementation.knowledgebase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;



/**
 * <p>
 * 
 * </p>
 * 
 */
public class InMemoryKnowledgeBase extends KnowledgeBaseImpl {
    public InMemoryKnowledgeBase() throws Exception{
        super();
    }

	public InMemoryKnowledgeBase(String name) throws Exception{
        super(name,KnowledgeBaseType.IN_MEMORY);
    }
    public void close(){
    }
    public List getInitialConceptList() throws Exception {
        return new ArrayList();
    }
    public List getInitialInstanceList() throws Exception {
    	return new ArrayList();
    }
    public List getInitialConnectorList() throws Exception {
    	return new ArrayList();
    }
    public List getInitialRelationList() throws Exception {
    	return new ArrayList();
    }
    public List getInitialPropositionList() throws Exception {
    	return new ArrayList();
    }    
}

