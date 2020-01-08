package org.neodatis.knowledger.gui;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.knowledger.web.KnowledgerObjectPageBuilder;

import junit.framework.TestCase;


public class TestBuildHtml extends TestCase {
	public void test1() throws Exception{
		KnowledgeBase kb = KnowledgeBaseFactory.getInstance("build",KnowledgeBaseType.ODB);
		Concept car = kb.getRootObject().getSubConcept("Car");
		String sId = String.valueOf(car.getId());
		
		KnowledgerObjectPageBuilder builder = new KnowledgerObjectPageBuilder("build",sId);
		String html = builder.buildHtml();
		assertTrue(html.length() > 0);
	}

}
