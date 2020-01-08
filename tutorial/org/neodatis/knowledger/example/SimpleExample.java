package org.neodatis.knowledger.example;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.criteria.EntityCriteria;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.implementation.query.RelationQuery;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.odb.tool.IOUtil;


public class SimpleExample {
	public static final String BASE_NAME = "tutorial1";
	
	public void step1() throws Exception{

        IOUtil.deleteFile(BASE_NAME+".knowledger");
		System.out.println("Dialogy Knowledger API Simple Example");
		// Gets the knowledge base - will be persisted in database - if it does not exist - creates it 
		//IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance(BASE_NAME);
		IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance(BASE_NAME);
		
		
		// Gets the root of all concepts - If it does not exist, creates it
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
        Instance pierre = man.newInstance();
		Instance isa = woman.newInstance();
		Instance karine = woman.newInstance();
		
		oliver.connectTo("is-married-to",isa);
		oliver.connectTo("wife",isa);
		
		
		oliver.setProperty("name",name.newInstance("oliver"));
		oliver.setProperty("name",name.newInstance("olive"));
		oliver.setProperty("birthdate",date.newInstance("04/10/1970"));
		oliver.setProperty("address",address.newInstance("rua fonte da saudade 822"));
        
        pierre.setProperty("name",name.newInstance("pierre"));

		
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
				
		// select x where x.husband.name like '%oliv%'
		ICriteria criteria = new EntityCriteria("husband.name","%oliv%");//.or(new EntityCriteria("daughter.mother.name","Aísa Galvão Smadja"));
		EntityList list = knowledgeBase.query(criteria);
		System.out.println(list);

		// select x where x.husband.name like '%oliv%' or daughter.mother.name = 'Aísa Galvão Smadja'
		criteria = new EntityCriteria("husband.name","%oliv%").or(new EntityCriteria("daughter.mother.name","Aísa Galvão Smadja"));
		list = knowledgeBase.query(criteria);
		System.out.println(list);
		
		System.out.println(knowledgeBase.toString());
        knowledgeBase.close();
        
	}

	public void step2() throws Exception{
		System.out.println("Dialogy Knowledger API Simple Example");
		// Gets the knowledge base - will be persisted in database - if it does not exist - creates it 
        IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance(BASE_NAME);
        
        Concept man = knowledgeBase.getConceptFromName("Man", GetMode.CREATE_IF_DOES_NOT_EXIST);
        // First way to query : by example, get all men
        RelationList rlist1 = knowledgeBase.queryRelations(null, knowledgeBase.getIsInstanceOfConnector(),man);
        System.out.println("Query1:\n"+rlist1.getLeftEntities());
        
        // Second way
        RelationQuery query = new RelationQuery();
        query.addRightEntityToInclude(man);
        
        RelationList rlist2 = query.executeOn(knowledgeBase.getRelationList());
        System.out.println("Query2:\n"+rlist2.getLeftEntities());
        
        // Third way : using criteria
        EntityCriteria criteria = new EntityCriteria("wife.husband.name","oliver");
        EntityList elist3 = knowledgeBase.query(criteria);
        System.out.println("Query3:\n"+elist3);
       

        // Fourth way : using KQL : Knowledger Query Language
        EntityList elist4 = knowledgeBase.query("select x where x.wife.husband.name='oliver';");
        System.out.println("Query4:\n"+elist4);
        
        knowledgeBase.close();
        
		
        

		
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Dialogy Knowledger API Simple Example");

		SimpleExample simpleExample = new SimpleExample();
		
		simpleExample.step1();
		
		simpleExample.step2();
		//simpleExample.step3();
		
	}
	
}
