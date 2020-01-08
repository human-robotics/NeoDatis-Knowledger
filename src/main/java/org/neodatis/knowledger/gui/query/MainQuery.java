package org.neodatis.knowledger.gui.query;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;


public class MainQuery {
	public static void main(String[] args) throws Exception {
		
		IKnowledgeBase kb = KnowledgeBaseFactory.getInstance("test",KnowledgeBaseType.IN_MEMORY);
		JFrame frame = new JFrame("Choose Knowledge base");
		JPanel panel = new ChooseDatabaseForQueryPanel();
		
		frame.getContentPane().add(panel);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.pack();
		frame.show();
		
	}
}
