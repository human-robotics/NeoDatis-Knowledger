/* 
 * $RCSfile: RelationFactory.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.factory;

import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class RelationFactory {

    public static Relation create(KnowledgeBase knowledgeBase,Entity leftObject , Connector connector , Entity rightObject){
        return new Relation(knowledgeBase,leftObject,connector,rightObject);
    }

}
