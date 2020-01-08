package org.neodatis.knowledger.gui.tool.populator;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.neodatis.knowledger.core.factory.RelationFactory;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.nlp.core.NLPAutomaticPopulator;


public class PopulatorPanel extends JPanel implements ActionListener{
	
	private static final String ACTION_SAVE = "save";
	private JTable populatorTable;
	private PopulatorTableModel model;
	private KnowledgeBase knowledgeBase;
	
	public PopulatorPanel(KnowledgeBase knowledgeBase,List conceptNamesToBeDefined, ConnectorList availableConnectors, EntityList availableEntities){
		init(knowledgeBase, conceptNamesToBeDefined, availableConnectors, availableEntities);
	}
	
	private void init(KnowledgeBase knowledgeBase, List conceptNamesToBeDefined, ConnectorList availableConnectors, EntityList availableEntities){
		this.knowledgeBase = knowledgeBase;
		model = new PopulatorTableModel(conceptNamesToBeDefined,availableConnectors,availableEntities,new NLPAutomaticPopulator());
		populatorTable = new JTable(model);
		setTableEditors(availableConnectors,availableEntities);

        populatorTable.setRowHeight(25);
		//populatorTable.setPreferredSize(new Dimension(350,400));
        
		setLayout(new BorderLayout(10,10));
		add(new JLabel(""+conceptNamesToBeDefined.size() + " " + Messages.getString("unknowns elements")),BorderLayout.NORTH);
        add(new JScrollPane(populatorTable),BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton(Messages.getString("save"));
        saveButton.setActionCommand(ACTION_SAVE);
        saveButton.addActionListener(this);
        buttonPanel.add(saveButton);
        
        add(buttonPanel,BorderLayout.SOUTH);
        
	}
	
	private void setTableEditors(ConnectorList availableConnectors, EntityList availableEntities){
		// Define right entity editor
		TableColumn column = populatorTable.getColumnModel().getColumn(PopulatorTableModel.COLUMN_RIGHT_ENTITY);
		Object [] entities = new Object[availableEntities.size()];
		
		JComboBox comboBox = new JComboBox(availableEntities.toArray(entities));
        column.setCellEditor(new DefaultCellEditor(comboBox));

		// Define connector editor
		column = populatorTable.getColumnModel().getColumn(PopulatorTableModel.COLUMN_CONNECTOR);
		entities = new Object[availableConnectors.size()];
		comboBox = new JComboBox(availableConnectors.toArray(entities));
        column.setCellEditor(new DefaultCellEditor(comboBox));
	}

	public void actionPerformed(ActionEvent actionEvent) {
		if(actionEvent.getActionCommand().equals(ACTION_SAVE)){
			try {
				save();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void save() throws Exception {
		
		Instance instance = null;
		Connector connector = null;
		Entity rightEntity = null;
		for(int i=0;i<model.getRowCount();i++){
			if(model.isDefined(i)){
				//@todo assuming that left entity is an instance, it could other type like, connector or concept
				instance = new Instance(model.getUnknownConcept(i));
				knowledgeBase.createInstance(instance);
				
				// create the relation
				Relation relation = RelationFactory.create(knowledgeBase,instance,model.getConnector(i),(Entity) model.getRightEntity(i));
				
				knowledgeBase.createRelation(relation);
				//System.out.println(model.getUnknownConcept(i) + " is instance of " + model.getConcept(i));
			}
		}
		
	}
	

}
