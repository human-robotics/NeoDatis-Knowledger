/* 
 * $RCSfile: ComboBuilder.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;



/**
 * <p>
 * 
 * </p>
 * 
 */
public class ComboBuilder {

	public static JComboBox fillConnectorsCombo(
		JComboBox comboBox,
		IKnowledgeBase knowledgeBase,
		boolean isEditable,
		Object selectedObject) {
		comboBox.removeAllItems();

		comboBox.addItem(EntityWrapper.EMPTY_WRAPPER);
		for (int i = 0; i < knowledgeBase.getConnectorList().size(); i++) {
			comboBox.addItem(new EntityWrapper(knowledgeBase.getConnectorList().getConnector(i)));
		}
		if (selectedObject != null) {
			comboBox.setSelectedItem(new EntityWrapper((KnowledgerObject)selectedObject));
		}

		comboBox.setEditable(isEditable);
		return comboBox;
	}

	
	public static JComboBox fillObjectCombo(
		JComboBox comboBox,
		IKnowledgeBase knowledgeBase,
		boolean isEditable,
		Object selectedObject,
		boolean loadConcepts,
		boolean loadInstances,
		boolean loadConnectionTypes) {
	    
		List allObject = new ArrayList();
		comboBox.removeAllItems();
		comboBox.addItem(EntityWrapper.EMPTY_WRAPPER);
		if (loadConcepts) {
			allObject.addAll(knowledgeBase.getConceptList());
		}

		if (loadInstances) {
			allObject.addAll(knowledgeBase.getInstanceList());
		}

		if (loadConnectionTypes) {
			allObject.addAll(knowledgeBase.getConnectorList());
		}

		for (int i = 0; i < allObject.size(); i++) {
			comboBox.addItem( new EntityWrapper((KnowledgerObject) allObject.get(i)));
		}
		if (selectedObject != null) {
			comboBox.setSelectedItem(new EntityWrapper((KnowledgerObject) selectedObject));
		}

		comboBox.setEditable(isEditable);
		return comboBox;

	}
	public static JComboBox fillObjectCombo(
		JComboBox comboBox,
		IKnowledgeBase knowledgeBase,
		boolean isEditable,
		Object selectedObject) {
		return fillObjectCombo(
			comboBox,
			knowledgeBase,
			isEditable,
			selectedObject,
			true,
			true,
			true);
	}

	public static void main(String[] args) throws Exception {

		IKnowledgeBase kb = KnowledgeBaseFactory.getInstance("test",KnowledgeBaseType.IN_MEMORY);
		JFrame frame = new JFrame("Dialogy Explorer");
		JPanel panel = new JPanel();
		panel.add(fillConnectorsCombo(new JComboBox(), kb, true, null));
		frame.getContentPane().add(new JScrollPane(panel));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}
}
