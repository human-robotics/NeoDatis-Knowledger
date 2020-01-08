package org.neodatis.knowledger.core.implementation;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;

import junit.framework.TestCase;


public class TestProperty extends TestCase {
	public void test1() throws Exception{
		
		KnowledgeBase kb = KnowledgeBaseFactory.getInstance("knowledger-property2",KnowledgeBaseType.REMOTE, "localhost","8080");
		Concept neodatis = kb.getConcept("NeoDatis");
		Concept url = kb.getConcept("URL");
		Instance instance = url.newInstance("http://www.neodatis.com");
		//neodatis.setProperty("url",instance);
		neodatis.connectTo("url",instance);
		RelationList rlist = neodatis.getRelationList();
		System.out.println("relation list="+rlist);
		assertTrue(neodatis.hasProperty("url"));
		System.out.println(neodatis.getProperty("url"));
	}
}
