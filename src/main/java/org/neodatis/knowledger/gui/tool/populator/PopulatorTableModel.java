package org.neodatis.knowledger.gui.tool.populator;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;


public class PopulatorTableModel extends DefaultTableModel {

	public static final String TITLE_LEFT_ENTITY = "Left Entity";
	public static final String TITLE_CONNECTOR = "Connector";
	public static final String TITLE_RIGHT_ENTITY = "Right Entity";

	public static final int COLUMN_LEFT_ENTITY = 0;
	public static final int COLUMN_CONNECTOR = 1;
	public static final int COLUMN_RIGHT_ENTITY = 2;
	
	private static final String UNKNOWN_LABEL = "?";
	
	/** List of concept names to be defined */
	private List unknowConcepts;

	private List definedConnectors;
	private List definedEntities;
	
	private List pluggableAutomaticPopulators; 

	public PopulatorTableModel(List conceptNamesToBeDefined,List availableConnectors) {
		this(conceptNamesToBeDefined,availableConnectors,new ArrayList(),new ArrayList());
	}

	public PopulatorTableModel(List conceptNamesToBeDefined,List availableConnectors,List availableeEntities,IPluggableAutomaticPopulator automaticPopulator) {
		List populators = new ArrayList();
		populators.add(automaticPopulator);
		init(conceptNamesToBeDefined,availableConnectors,availableeEntities,populators);
	}

	public PopulatorTableModel(List conceptNamesToBeDefined,List availableConnectors,List availableEntities,List pluggablePopulators) {
		init(conceptNamesToBeDefined,availableConnectors,availableEntities,pluggablePopulators);
	}
	private void init(List conceptNamesToBeDefined,List availableConnectors,List availableEntities,List pluggablePopulators){
		this.unknowConcepts = conceptNamesToBeDefined;
		this.pluggableAutomaticPopulators = new ArrayList();
		this.pluggableAutomaticPopulators.addAll(pluggablePopulators);
		this.definedEntities = new ArrayList();
		this.definedConnectors = new ArrayList();
		
		for(int i=0;i<unknowConcepts.size();i++){
			definedEntities.add(UNKNOWN_LABEL);
			
			// If there is only one connector, already set it
			if(availableConnectors.size()==1){
				definedConnectors.add(availableConnectors.get(0));
			}else{
				definedConnectors.add(UNKNOWN_LABEL);
			}
		}
		executeAutomaticPopulators(availableConnectors,availableEntities);
	}
	private void executeAutomaticPopulators(List availableConnectors,List availableEntities) {
		IPluggableAutomaticPopulator populator = null;
		for(int j=0;j<pluggableAutomaticPopulators.size();j++){
			populator = (IPluggableAutomaticPopulator) pluggableAutomaticPopulators.get(j);
			for(int i=0;i<unknowConcepts.size();i++){
				populator.execute(i,unknowConcepts,availableConnectors,availableEntities,definedConnectors,definedEntities);
			}
		}
	}

	public void register(List pluggableAutomaticPopulators){
		for(int i=0;i<pluggableAutomaticPopulators.size();i++){
			register((IPluggableAutomaticPopulator)pluggableAutomaticPopulators.get(i));
		}
	}

	public void register(IPluggableAutomaticPopulator pluggableAutomaticPopulator){
		pluggableAutomaticPopulators.add(pluggableAutomaticPopulator);
	}

	public int getColumnCount() {
		return 3;
	}

	public String getColumnName(int column) {
		switch (column) {
		case COLUMN_LEFT_ENTITY:
			return TITLE_LEFT_ENTITY;
		case COLUMN_CONNECTOR:
			return TITLE_CONNECTOR;
		case COLUMN_RIGHT_ENTITY:
			return TITLE_RIGHT_ENTITY;
		default:
			return "Unknown";
		}
	}

	public int getRowCount() {
		if(unknowConcepts==null){
			return 0;
		}
		return unknowConcepts.size();
	}

	public Object getValueAt(int row, int column) {
		switch (column) {
		case COLUMN_LEFT_ENTITY:
			return unknowConcepts.get(row);
		case COLUMN_CONNECTOR:
			return definedConnectors.get(row);
		case COLUMN_RIGHT_ENTITY:
			return definedEntities.get(row);
		default:
			return "Unknown";
		}
	}

	public void setValueAt(Object object, int row, int column) {
		switch (column) {
		case COLUMN_LEFT_ENTITY:
			break;
		case COLUMN_RIGHT_ENTITY:
			definedEntities.set(row,object);
			System.out.println("Defining right entity " + object + " for row " + (row+1));
			break;
		case COLUMN_CONNECTOR:
			definedConnectors.set(row,object);
			System.out.println("Defining connector " + object + " for row " + (row+1));
			break;
		default:
			System.out.println("unimplemented");
		}
	}
	
	public boolean isDefined(int row){
		return !getValueAt(row,COLUMN_CONNECTOR).toString().equals(UNKNOWN_LABEL) && !getValueAt(row,COLUMN_RIGHT_ENTITY).toString().equals(UNKNOWN_LABEL); 
	}
	
	public String getUnknownConcept(int row){
		return (String) unknowConcepts.get(row);
	}
	public KnowledgerObject getRightEntity(int row){
		return (KnowledgerObject)definedEntities.get(row);
	}
	public Connector getConnector(int row){
		System.out.println("Dialogy Object : " + (Connector) definedConnectors.get(row));
		return (Connector) definedConnectors.get(row);
	}

}
