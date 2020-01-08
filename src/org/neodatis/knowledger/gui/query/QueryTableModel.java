package org.neodatis.knowledger.gui.query;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.neodatis.knowledger.core.implementation.criteria.EntityCriteria;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;


public class QueryTableModel extends DefaultTableModel {

	
	public static final String LOGICAL_OPERATOR_AND = "and";
	public static final String LOGICAL_OPERATOR_OR = "or";
	public static final String LOGICAL_OPERATOR_NOTHING = "-";
	
	public static final String TITLE_BINDED_OBJECT = "Object";
	public static final String TITLE_PROPERTY = "Property";
	public static final String TITLE_VALUE = "Value";
	public static final String TITLE_LOGICAL_OPERATOR = "Logical Operator";
	public static final String TITLE_COMPARE_OPERATOR = "Compare Operator";
	
	public static final String COMPARE_OPERATOR_EQUAL = "=";
	

	
	public static final int COLUMN_BINDED_OBJECT = 0;
	public static final int COLUMN_PROPERTY = 1;
	public static final int COLUMN_COMPARE_OPERATOR = 2;
	public static final int COLUMN_VALUE = 3;
	public static final int COLUMN_LOGICAL_OPERATOR = 4;
	
	protected List logicalOperators;
	protected List compareOperators;
	protected List criteriaList;
	
	private static final String UNKNOWN_LABEL = "?";
	
	
	public QueryTableModel() {
		init();
	}

	private void init(){
		criteriaList = new ArrayList();
		logicalOperators = new ArrayList();
		compareOperators = new ArrayList();
	}

	public EntityCriteria getCriteria(int index){
		return (EntityCriteria) criteriaList.get(index);
	}
	
	public int getColumnCount() {
		return 5;
	}

	public String getColumnName(int column) {
		switch (column) {
		case COLUMN_BINDED_OBJECT:
			return TITLE_BINDED_OBJECT;
		case COLUMN_LOGICAL_OPERATOR:
			return TITLE_LOGICAL_OPERATOR;
		case COLUMN_COMPARE_OPERATOR:
			return TITLE_COMPARE_OPERATOR;
		case COLUMN_PROPERTY:
			return TITLE_PROPERTY;
		case COLUMN_VALUE:
			return TITLE_VALUE;
		default:
			return "Unknown";
		}
	}

	public int getRowCount() {
		if(criteriaList==null){
			return 0;
		}
		return criteriaList.size();
	}

	public Object getValueAt(int row, int column) {
		switch (column) {
		case COLUMN_BINDED_OBJECT:
			return "x.";
		case COLUMN_LOGICAL_OPERATOR:
			return logicalOperators.get(row);
		case COLUMN_COMPARE_OPERATOR:
			return compareOperators.get(row);
		case COLUMN_PROPERTY:
			return getCriteria(row).getConnectorName();
		case COLUMN_VALUE:
			return getCriteria(row).getValue();
		default:
			return "Unknown";
		}
	}

	public void setValueAt(Object object, int row, int column) {
		switch (column) {
		case COLUMN_LOGICAL_OPERATOR:
			logicalOperators.set(row,object.toString());
			break;
		case COLUMN_COMPARE_OPERATOR:
			compareOperators.set(row,object.toString());
			break;
		case COLUMN_PROPERTY:
			EntityCriteria ac = (EntityCriteria) criteriaList.get(row);
			ac.setConnectorName(object.toString());
			break;
		case COLUMN_VALUE:
			EntityCriteria ac2 = (EntityCriteria) criteriaList.get(row);
			ac2.setValue(object.toString());
			break;
		default:
			System.out.println("unimplemented");
		}
	}

	public List getCriteriaList() {
		return criteriaList;
	}
	
	public ICriteria getResultingCriteria(){
		ICriteria criteria = null;
		ICriteria resultingCriteria = (ICriteria) criteriaList.get(0);
		
		for(int i=1;i<criteriaList.size();i++){
			criteria = (ICriteria) criteriaList.get(i);
			if(logicalOperators.get(i-1).equals(LOGICAL_OPERATOR_AND)){
				resultingCriteria = resultingCriteria.and(criteria);	
			}				
			if(logicalOperators.get(i-1).equals(LOGICAL_OPERATOR_OR)){
				resultingCriteria = resultingCriteria.or(criteria);	
			}				
		}
		
		return resultingCriteria;
	}

	public List getLogicalOperators() {
		return logicalOperators;
	}
	
	public void clear(){
		criteriaList.clear();
		logicalOperators.clear();
		compareOperators.clear();
		fireTableDataChanged();
	}
	public void addCriteria(){
		logicalOperators.add(LOGICAL_OPERATOR_NOTHING);
		compareOperators.add(COMPARE_OPERATOR_EQUAL);
		criteriaList.add(new EntityCriteria(null,null));
		fireTableRowsInserted(criteriaList.size()-1,criteriaList.size()-1);
	}

}
