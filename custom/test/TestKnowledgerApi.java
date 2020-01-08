/*
 * Created on 26/05/2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package test;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.criteria.EntityCriteria;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;

public class TestKnowledgerApi {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		KnowledgeBase kb = KnowledgeBaseFactory.getInstance("t4");
		
		ICriteria criteria = new EntityCriteria("id","10").and(new EntityCriteria("id.is-instance-of","Identificador"));
		EntityList list = kb.query(criteria);
		System.out.println(list);
	}

}
