package org.neodatis.knowledger.gui.parser;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;
import org.neodatis.knowledger.gui.panel.ChooseKnowledeBasePanel;

public class ChooseKnowledgeBaseToTestQuery extends ChooseKnowledeBasePanel {

	public ChooseKnowledgeBaseToTestQuery() throws Exception {
		super();
	}

	public void databaseIs(KnowledgeBase knowledgeBase) {
		ICriteria criteria = null;
		try {
	        QueryLexer lexer = new QueryLexer(System.in);
	        DialogyQueryParser parser = new DialogyQueryParser(lexer);

	        while(true){
	            criteria = parser.query();
	            System.out.println("result of select x where " + criteria + " : ");
	            System.out.println(knowledgeBase.query(criteria));

	        }

			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
