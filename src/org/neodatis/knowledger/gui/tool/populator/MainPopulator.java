package org.neodatis.knowledger.gui.tool.populator;

import java.util.Arrays;

import javax.swing.JFrame;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;


public class MainPopulator {
	public static void main(String[] args) throws Exception {
		
		KnowledgeBase kb = KnowledgeBaseFactory.getInstance("test",KnowledgeBaseType.IN_MEMORY);
		Concept base = kb.getConceptFromName("GrammaticalEntity",GetMode.CREATE_IF_DOES_NOT_EXIST);
		String [] array = {"olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador","olivier","aisa","televisão","computador"};
 
		EntityList entities = base.getSubConcepts();
		ConnectorList connectors = new ConnectorList();
		connectors.add(kb.getIsInstanceOfConnector());
		//connectors.add(ConnectorFactory.getSubclassOf());
		
		JFrame frame = new JFrame("Dialogy Concept Definition");
		PopulatorPanel panel = new PopulatorPanel(kb,Arrays.asList(array),connectors, entities);
		
		frame.getContentPane().add(panel);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.pack();
		frame.show();
		
	}
}
