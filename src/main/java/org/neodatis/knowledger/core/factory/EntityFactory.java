/* 
 * $RCSfile: EntityFactory.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.core.factory;

import java.util.Date;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.Proposition;
import org.neodatis.knowledger.core.implementation.entity.Relation;


/**
 * <p>
 * 
 * </p>
 * 
 */
public class EntityFactory {
    
    
    private static String ENTITY_CONCEPT ="Concept";
    private static String ENTITY_INSTANCE ="Instance";
    private static String ENTITY_PROPOSITION ="Proposition";
    private static String ENTITY_RELATION ="Relation";
    private static String ENTITY_CONNECTOR ="Connector";
    
    public static Concept getConceptInstance() throws Exception{
        Concept concept = (Concept) Basic.getInstance(ENTITY_CONCEPT); 
        concept.setCreationDate(new Date());
        return concept;
    }
    public static Instance getInstanceInstance() throws Exception{
        Instance instance = (Instance) Basic.getInstance(ENTITY_INSTANCE);
        instance.setCreationDate(new Date());
        return instance;
    }
    public static Proposition getPropositionInstane() throws Exception{
        Proposition proposition = (Proposition) Basic.getInstance(ENTITY_PROPOSITION);
        proposition.setCreationDate(new Date());
        return proposition;
    }
    public static Relation getRelationInstance(Entity leftEntity, Connector connector, Entity rightEntity,Proposition proposition) throws Exception{
        Relation relation = getRelationInstance();
        relation.setLeftEntity(leftEntity);
        relation.setConnector(connector);
        relation.setRightEntity(rightEntity);
        relation.setProposition(proposition);
        return relation;
    }

    public static Relation getRelationInstance() throws Exception{
        return (Relation) Basic.getInstance(ENTITY_RELATION);
    }
    /**
     * @return
     * @throws Exception
     */
    public static Connector getConnectorInstance() throws Exception {
        Connector connector = (Connector) Basic.getInstance(ENTITY_CONNECTOR);
        connector.setCreationDate(new Date());
        return connector;
    }
    
}
