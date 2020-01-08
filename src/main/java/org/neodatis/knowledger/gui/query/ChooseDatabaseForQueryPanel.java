package org.neodatis.knowledger.gui.query;

import javax.swing.JFrame;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.gui.panel.ChooseKnowledeBasePanel;


public class ChooseDatabaseForQueryPanel extends ChooseKnowledeBasePanel {

	public ChooseDatabaseForQueryPanel() throws Exception {
		super();
	}

	public void databaseIs(KnowledgeBase knowledgeBase) {
        JFrame frame = new JFrame("Dialogy Knowledger Query");
		QueryPanel panel = new QueryPanel(knowledgeBase);
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.show();
		
	}
	

}
