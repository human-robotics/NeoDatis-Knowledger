/* 
 * $RCSfile: SimpleTest.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation.knowledgebase.test;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.criteria.SimpleConceptCriteria;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;

import junit.framework.TestCase;


public class SimpleTest extends TestCase {

    
    public void test1() throws Exception{
        
        // Gets the knowledge base - will be persisted in database - if it does not exist - creates it 
        IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance("test1",KnowledgeBaseType.ODB);
        
        // Gets the root of all concepts - If it does not exist, creates it
        Concept root = knowledgeBase.getRootObject();
        
        // Gets the Human concept as sub-concept of root - creates it if does not exist
        Concept human = root.getSubConcept("Human");
        
        // Gets the Man concept as sub-concept of man - creates it if does not exist
        Concept man = human.getSubConcept("Man");
        man.setIdentifier("man");
        //man.update();
        knowledgeBase.updateConcept(man);
        Concept woman = human.getSubConcept("Woman");
        // creates 3 new instances
        Instance oliver = man.newInstance();
        Instance isa = woman.newInstance();
        System.out.println(knowledgeBase.toString());
 
        knowledgeBase.close();
        
        knowledgeBase = KnowledgeBaseFactory.getInstance("test1",KnowledgeBaseType.ODB);
        System.out.println(knowledgeBase.getConceptList());
        System.out.println(knowledgeBase.getRelationList());
        EntityList l = knowledgeBase.query(new SimpleConceptCriteria("man"));
        assertEquals(1,l.size());
        Entity e = l.getEntity(0);
        assertEquals("man",e.getIdentifier());
        
        Concept c = knowledgeBase.getConceptFromId(man.getId());
        assertEquals("man",c.getIdentifier());
        
        InstanceList il = c.getDirectInstances();
        assertEquals(1,il.size());
        
        EntityList el = knowledgeBase.query("select x where x is-instance-of man");
        assertEquals(1,el.size());
        //knowledgeBase.close();
        
    }

}
