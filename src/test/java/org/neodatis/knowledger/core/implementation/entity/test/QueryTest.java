/* 
 * $RCSfile: QueryTest.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:29 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation.entity.test;

import java.util.List;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.criteria.EntityCriteria;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;

import junit.framework.TestCase;


public class QueryTest extends TestCase {
  
    IKnowledgeBase  createKnowledgeDatabase(String kbName) throws Exception{
        System.out.println("Dialogy Knowledger API Simple Example");
        // Gets the knowledge base - will be persisted in database - if it does not exist - creates it 
        IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance(kbName,KnowledgeBaseType.IN_MEMORY);
        
        
//      Gets the root of all concepts - If it does not exist, creates it
        Concept root = knowledgeBase.getRootObject();
        
        // Gets the Human concept as sub-concept of root - creates it if does not exist
        Concept human = root.getSubConcept("Human");
        
        // Gets the Man concept as sub-concept of man - creates it if does not exist
        Concept man = human.getSubConcept("Man");
        Concept woman = human.getSubConcept("Woman");
        Concept name = root.getSubConcept("Name");
        Concept date = root.getSubConcept("Date");
        Concept address = root.getSubConcept("Address");
        
        // creates 3 new instances
        Instance oliver = man.newInstance();
        Instance isa = woman.newInstance();
        Instance karine = woman.newInstance();

        
        oliver.connectTo("is-married-to",isa);
        oliver.connectTo("wife",isa);
        
        
        oliver.setProperty("name",name.newInstance("oliver"));
        oliver.setProperty("name",name.newInstance("olive"));
        oliver.setProperty("birthdate",date.newInstance("04/10/1970"));
        oliver.setProperty("address",address.newInstance("rua fonte da saudade 822"));
        
        isa.setProperty("husband",oliver);
        isa.setProperty("name",name.newInstance("isa"));
        
        karine.setProperty("name",name.newInstance("karine"));
        karine.setProperty("father",oliver).setProperty("mother",isa);
        
        oliver.setProperty("daughter",karine);
        isa.setProperty("daughter", karine);
        
        System.out.println("Names of oliver = " + oliver.getProperties("name"));
        System.out.println("address of oliver = " + oliver.getProperty("address"));
        
        System.out.println("Name of Oliver's Wife (1)= " + oliver.getProperty("wife").getProperties("name"));
        System.out.println("Name of Oliver's Wife (2)= " + oliver.getProperty("wife.name"));
        System.out.println("Names of Oliver (1)= " + oliver.getProperties("wife.husband.name"));
        System.out.println("Names of Oliver (2)= " + oliver.getProperties("wife.husband.daughter.father.daughter.mother.husband.name"));

        return knowledgeBase;
    }
    
    
    public void test1() throws Exception{
        
        IKnowledgeBase knowledgeBase = createKnowledgeDatabase("test1");
        // select x where x.husband.name like '%oliv%'
        ICriteria criteria = new EntityCriteria("husband.name","%oliv%");//.or(new EntityCriteria("daughter.mother.name","Aísa Galvão Smadja"));
        EntityList list = knowledgeBase.query(criteria);
        System.out.println(list);

        // select x where x.husband.name like '%oliv%' or daughter.mother.name = 'Aísa Galvão Smadja'
        criteria = new EntityCriteria("husband.name","%oliv%").or(new EntityCriteria("daughter.mother.name","Aísa Galvão Smadja"));
        list = knowledgeBase.query(criteria);
        System.out.println(list);
        
        // select x where x is instance of Name;
        criteria = new EntityCriteria(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF,"Name");
        List objects = knowledgeBase.query(criteria);
        System.out.println(objects);
        assertEquals(4,objects.size());

        // select x where x is-instance-of Name and x like oli%;
        criteria = new EntityCriteria(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF,"Name");
        criteria = criteria.and( new EntityCriteria("oli%"));
        objects = knowledgeBase.query(criteria);
        System.out.println(objects);
        assertEquals(2,objects.size());

        // select x where x is-instance-of Man and x.name like oli%;
        criteria = new EntityCriteria(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF,"Man");
        criteria = criteria.and( new EntityCriteria("name","oli%"));
        objects = knowledgeBase.query(criteria);
        System.out.println(objects);
        assertEquals(1,objects.size());

        // select x where x is-instance-of Man and x.wife.name like is%;
        criteria = new EntityCriteria(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF,"Man");
        criteria = criteria.and( new EntityCriteria("wife.name","is%"));
        objects = knowledgeBase.query(criteria);
        System.out.println(objects);
        assertEquals(1,objects.size());

        // select x where x is-instance-of Man or x is-instance-of Woman;
        criteria = new EntityCriteria(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF,"Man");
        criteria = criteria.or( new EntityCriteria(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF,"Woman"));
        objects = knowledgeBase.query(criteria);
        System.out.println(objects);
        assertEquals(3,objects.size());

        // select x where (x is-instance-of Man or x is-instance-of Woman) and x.daughter.name = karine;
        criteria = new EntityCriteria(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF,"Man");
        criteria = criteria.or( new EntityCriteria(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF,"Woman"));
        criteria = criteria.and( new EntityCriteria("daughter.name","karine"));
        objects = knowledgeBase.query(criteria);
        System.out.println(objects);
        assertEquals(2,objects.size());

    }
    public void test2() throws Exception{
        
        IKnowledgeBase knowledgeBase = createKnowledgeDatabase("test2");
        /*
        // select x where x.husband.name like '%oliv%'
        ICriteria criteria = new EntityCriteria("husband.name","%oliv%");//.or(new EntityCriteria("daughter.mother.name","Aísa Galvão Smadja"));
        IEntityList list = knowledgeBase.query(criteria);
        System.out.println(list);

        // select x where x.husband.name like '%oliv%' or daughter.mother.name = 'Aísa Galvão Smadja'
        criteria = new EntityCriteria("husband.name","%oliv%").or(new EntityCriteria("daughter.mother.name","Aísa Galvão Smadja"));
        list = knowledgeBase.query(criteria);
        System.out.println(list);
        */
        // select x where x is instance of Name;
        List objects = knowledgeBase.query("select x where x is-instance-of Name;");
        System.out.println(objects);
        assertEquals(4,objects.size());

        // select x where x is-instance-of Name and x like oli%;
        objects = knowledgeBase.query("select x where x is-instance-of Name and x = oli%;");
        System.out.println(objects);
        assertEquals(2,objects.size());

        // select x where x is-instance-of Man and x.name like oli%;
        objects = knowledgeBase.query("select x where x is-instance-of Man and x.name = oli%;");
        System.out.println(objects);
        assertEquals(1,objects.size());

        // select x where x is-instance-of Man and x.wife.name like is%;
        objects = knowledgeBase.query("select x where x is-instance-of Man and x.wife.name = is%;");
        System.out.println(objects);
        assertEquals(1,objects.size());

        // select x where x is-instance-of Man or x is-instance-of Woman;
        objects = knowledgeBase.query("select x where x is-instance-of Man or x is-instance-of Woman;");
        System.out.println(objects);
        assertEquals(3,objects.size());

        // select x where (x is-instance-of Man or x is-instance-of Woman) and x.daughter.name = karine;
        objects = knowledgeBase.query("select x where (x is-instance-of Man or x is-instance-of Woman) and x.daughter.name = karine;");
        System.out.println(objects);
        assertEquals(2,objects.size());

    }

}
