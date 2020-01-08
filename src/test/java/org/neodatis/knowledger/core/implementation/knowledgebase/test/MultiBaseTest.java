package org.neodatis.knowledger.core.implementation.knowledgebase.test;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;

import junit.framework.TestCase;


public class MultiBaseTest extends TestCase {
	
	public void test1() throws Exception{
		String containerFile = "container.knowledger";
		String base1Name = "base1";
		String base2Name = "base2";
		String base3Name = "base3";
		
		KnowledgeBase base1 = KnowledgeBaseFactory.getInstance(containerFile,base1Name,KnowledgeBaseType.ODB);
		Concept animal1 = base1.getRootObject().getSubConcept("Animal");
		Instance kiko = animal1.newInstance("kiko");
		base1.close();
		
		
		KnowledgeBase base2 = KnowledgeBaseFactory.getInstance(containerFile,base2Name,KnowledgeBaseType.ODB);
		Concept animal2 = base2.getRootObject().getSubConcept("Animal");
		Instance raphael = animal2.newInstance("raphael");
		base2.close();
		
		KnowledgeBase base3 = KnowledgeBaseFactory.getInstance(containerFile,base3Name,KnowledgeBaseType.ODB);
		Concept animal3 = base3.getRootObject().getSubConcept("Animal");
		Instance kika = animal3.newInstance("kika");
		base3.close();
		
		System.out.println("\n\nbase1");
		System.out.println(base1);
		System.out.println("\n\nbase2");
		System.out.println(base2);
		System.out.println("\n\nbase3");
		System.out.println(base3);

	}

}
