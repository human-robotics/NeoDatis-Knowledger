/* 
 * $RCSfile: TestKnowledgeBase.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation.knowledgebase.test;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;

import junit.framework.TestCase;


/**
 * <p>
 * 
 * </p>
 * 
 */
public class TestKnowledgeBase extends TestCase {

    public TestKnowledgeBase() {
        System.out.println("CONSTRUTOR TEST KNOWLEDGE BASE");
	}

	private IKnowledgeBase getKB() throws Exception {
		return KnowledgeBaseFactory.getInstance("junit-kb",KnowledgeBaseType.ODB);
        /*
        List l = getSession().createQuery("from " + DbPersistentKnowledgeBase.class.getName() + " kb where kb.name = 'junit-kb'").list();
		if (l.isEmpty()) {
			IKnowledgeBase knowledgeBase = new DbPersistentKnowledgeBase("junit-kb");
			knowledgeBase.setLanguage(getLanguage());
            getSession().save(knowledgeBase);
            getSession().connection().commit();
			return knowledgeBase;
		}
		return (IKnowledgeBase) l.get(0);*/
	}

    public void testGetConceptFromName() throws Exception{
        IKnowledgeBase knowledgeBase = getKB();
        System.out.println(knowledgeBase);

        Concept root = knowledgeBase.getRootObject();

        Concept human = root.getSubConcept("Human");
        int nbinstances = human.getDirectInstances().size();
        Instance instance = human.newInstance();
        instance.setProperty("name",root.getSubConcept("Name").newInstance("olivier.smadja"));
        
        assertEquals(nbinstances+1,human.getDirectInstances().size());
        
    }
    
    public void testDeleteConcept() throws Exception{
        IKnowledgeBase knowledgeBase = getKB();
        System.out.println(knowledgeBase);

        Concept root = knowledgeBase.getRootObject();

        Concept human = root.getSubConcept("Human");
        int nbConcepts = root.getSubConcepts().size();
        human.delete();
        
        assertEquals(nbConcepts-1,root.getSubConcepts().size());
        
    }
    public void tes2tGetInstance() throws Exception{
    	IKnowledgeBase knowledgeBase = getKB();
        Instance instance = knowledgeBase.getRootObject().newInstance();
        instance.setIdentifier("oli1");
        instance.setValue("45");
        knowledgeBase.createInstance(instance);
        //KObject o = knowledgeBase.getInstanceFromValue("45",GetMode.RETURN_NULL_IF_DOES_NOT_EXIST);
        //System.out.println(o);
    }
    public void tes2tRelation() throws Exception{
        IKnowledgeBase knowledgeBase = getKB();
        
        Concept concept = knowledgeBase.getConceptFromName("Woman",GetMode.CREATE_IF_DOES_NOT_EXIST);
        Concept name = knowledgeBase.getConceptFromName("Name",GetMode.CREATE_IF_DOES_NOT_EXIST);
        Instance aisa = concept.newInstance("aisa");
        Instance aisaName = name.newInstance("Aisa Galvao Smadja");
        aisa.setProperty("name",aisaName);
        
    }

    public void testRelation2() throws Exception{
        IKnowledgeBase knowledgeBase = getKB();
        
        Concept concept = knowledgeBase.getConceptFromName("Woman",GetMode.CREATE_IF_DOES_NOT_EXIST);
        Concept name = knowledgeBase.getConceptFromName("Name",GetMode.CREATE_IF_DOES_NOT_EXIST);
        Connector connector = knowledgeBase.getConnectorFromName("has",GetMode.CREATE_IF_DOES_NOT_EXIST);
        
        Relation relation = new Relation(concept,connector,name);
        relation.setMinCardinality(1);
        relation.setMaxCardinality(10);
        relation.setOptional(false);
        
        knowledgeBase.createRelation(relation);
        
    }
}
