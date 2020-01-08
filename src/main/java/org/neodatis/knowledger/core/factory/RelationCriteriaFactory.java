/* 
 * $RCSfile: RelationCriteriaFactory.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.factory;

import org.neodatis.knowledger.core.implementation.criteria.RelationCriteria;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.interfaces.entity.IRelationCriteria;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class RelationCriteriaFactory {
    public static IRelationCriteria getRelationCriteria(KnowledgerObject leftObject, Connector connector , KnowledgerObject rightObject){
        return new RelationCriteria(leftObject,connector,rightObject);
    }
}
