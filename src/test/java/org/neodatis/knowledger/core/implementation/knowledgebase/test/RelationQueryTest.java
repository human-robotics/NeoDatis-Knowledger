package org.neodatis.knowledger.core.implementation.knowledgebase.test;

import java.util.List;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.query.RelationQuery;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;

import junit.framework.TestCase;


public class RelationQueryTest extends TestCase {
	public void test1() throws Exception{
        // Gets the knowledge base - will be persisted in database - if it does not exist - creates it 
        IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance("testRelationQuery",KnowledgeBaseType.ODB);
        
        // Gets the root of all concepts - If it does not exist, creates it
        Concept root = knowledgeBase.getRootObject();
        
        // Gets the Human concept as sub-concept of root - creates it if does not exist
        Concept human = root.getSubConcept("Human");
        
        System.out.println("relation lists : " + knowledgeBase.getRelationList()+"\n\n");
        
        RelationQuery query = new RelationQuery();
        List l = query.executeOn(knowledgeBase.getRelationList());
        System.out.println(l);
        assertEquals(4,l.size());
        
        System.out.println();
        
        System.out.println(knowledgeBase.getRootConnector());
        
        query.addConnectorToExclude(knowledgeBase.getIsSubRelationOfConnector());
        l = query.executeOn(knowledgeBase.getRelationList());
        System.out.println(l);
        assertEquals(1,l.size());
        
        query = new RelationQuery();
        query.addRelationToExclude(new Relation(null,knowledgeBase.getIsSubclassOfConnector(),knowledgeBase.getRootObject()));
        
        System.out.println();
        l = query.executeOn(knowledgeBase.getRelationList());
        System.out.println(l);
        assertEquals(3,l.size());
        
        
	}

}
