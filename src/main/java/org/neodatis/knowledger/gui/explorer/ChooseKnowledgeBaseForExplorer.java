package org.neodatis.knowledger.gui.explorer;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.gui.panel.ChooseKnowledeBasePanel;

public class ChooseKnowledgeBaseForExplorer extends ChooseKnowledeBasePanel {

	public ChooseKnowledgeBaseForExplorer() throws Exception {
		super();
	}

	public void databaseIs(KnowledgeBase knowledgeBase) {
		try {
			KnowledgeExplorerPanel.display(knowledgeBase,"NeoDatis Knowledger");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
