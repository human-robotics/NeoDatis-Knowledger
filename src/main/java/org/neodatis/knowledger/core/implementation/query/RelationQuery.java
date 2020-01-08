/* 
 * $RCSfile: RelationQuery.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:29 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation.query;

import java.util.ArrayList;
import java.util.List;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.criteria.RelationCriteria;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.tool.DLogger;



/**@todo convert this class to criteria interface
 * <p>
 * 
 * </p>
 * 
 */
public class RelationQuery {
	
	private List leftEntitiesToInclude;
	private List leftEntitiesToExclude;
	private List rightEntitiesToInclude;
	private List rightEntitiesToExclude;
	private List connectorsToInclude;
	private List connectorsToExclude;
	private List relationCriteriaToExclude;
	private List relationCriteriaToInclude;

	
	public RelationQuery(){
		init();
	}
	private void init(){
		leftEntitiesToInclude = new ArrayList();
		leftEntitiesToExclude = new ArrayList();
		rightEntitiesToInclude = new ArrayList();
		rightEntitiesToExclude = new ArrayList();
		connectorsToInclude = new ArrayList();
		connectorsToExclude = new ArrayList();
		relationCriteriaToExclude = new ArrayList();
		relationCriteriaToInclude = new ArrayList();
	}

	public void addLeftEntityToInclude(Object object){
		leftEntitiesToInclude.add(object);	
	}
	public void addLeftEntityToExclude(Object object){
		leftEntitiesToExclude.add(object);	
	}
	public void addRightEntityToInclude(Object object){
		rightEntitiesToInclude.add(object);	
	}
	public void addRightEntityToExclude(Object object){
		rightEntitiesToExclude.add(object);	
	}
	public void addEntityToExclude(Object object){
		addLeftEntityToExclude(object);
		addRightEntityToExclude(object);
	}
	public void addEntitiesToExclude(List objects){
		leftEntitiesToExclude.addAll(objects);
		rightEntitiesToExclude.addAll(objects);
	}
	public void addEntitiesToInclude(List objects){
		leftEntitiesToInclude.addAll(objects);
		rightEntitiesToInclude.addAll(objects);
	}
	public void addConnectorToInclude(Object object){
		connectorsToInclude.add(object);	
	}
	public void addConnectorToExclude(Object object){
		connectorsToExclude.add(object);	
	}
	
	public void addRelationToExclude(Relation relation){
		relationCriteriaToExclude.add(new RelationCriteria(relation));
	}
	public void addRelationToInclude(Relation relation){
		relationCriteriaToInclude.add(new RelationCriteria(relation));
	}
	
	public boolean isExcluded(Object object){
		return leftEntitiesToExclude.contains(object) || rightEntitiesToExclude.contains(object) || connectorsToExclude.contains(object);
	}
	public boolean isIncluded(Object object){
		return leftEntitiesToInclude.contains(object) || rightEntitiesToInclude.contains(object) || connectorsToInclude.contains(object);
	}
	public RelationList executeOn(RelationList relationList) {
		RelationList result = new RelationList();
		boolean match = true;
		Relation relation = null;

		RelationCriteria criteria = null;
		
		for (int i = 0; i < relationList.size(); i++) {
			relation = relationList.getRelation(i);
			match = true;
			
			match = matchRelationCriteria(relation);
			if(!match){
				continue;
			}
			if(relation==null){
				DLogger.debug("query : Relation of index " + i + " is null");
				continue;
			}

			// If it was specified objects to be include
			if(!leftEntitiesToInclude.isEmpty()){
				if(!leftEntitiesToInclude.contains(relation.getLeftEntity())){
					// entering here means that relation can not be included
					continue;				
				}
			}
			// If it was specified objects to be exclude
			if(!leftEntitiesToExclude.isEmpty()){
				if(leftEntitiesToExclude.contains(relation.getLeftEntity())){
					// entering here means that relation can not be included
					continue;				
				}
			}

			// If it was specified objects to be include
			if(!rightEntitiesToInclude.isEmpty()){
				if(!rightEntitiesToInclude.contains(relation.getRightEntity())){
					// entering here means that relation can not be included
					continue;				
				}
			}
			// If it was specified objects to be exclude
			if(!rightEntitiesToExclude.isEmpty()){
				if(rightEntitiesToExclude.contains(relation.getRightEntity())){
					// entering here means that relation can not be included
					continue;				
				}
			}

			// If it was specified objects to be include
			if(!connectorsToInclude.isEmpty()){
				if(!connectorsToInclude.contains(relation.getConnector())){
					// entering here means that relation can not be included
					continue;				
				}
			}
			// If it was specified objects to be exclude
			if(!connectorsToExclude.isEmpty()){
				if(connectorsToExclude.contains(relation.getConnector())){
					// entering here means that relation can not be included
					continue;				
				}
			}

			// If the program reach, the relations has passed all restrictions
			result.add(relation);
		}

		return result;
	}


	private boolean matchRelationCriteria(Relation relation) {
		for(int i=0;i<relationCriteriaToExclude.size();i++){
			RelationCriteria r = (RelationCriteria) relationCriteriaToExclude.get(i);
			boolean match = r.match(relation);
			// the criteria must be excluded, if match is true, then this relation is out
			if(match){
				return false;
			}
		}
		for(int i=0;i<relationCriteriaToInclude.size();i++){
			RelationCriteria r = (RelationCriteria) relationCriteriaToExclude.get(i);
			boolean match = r.match(relation);
			// the criteria must be included, if match is false, then this relation is out
			if(!match){
				return false;
			}
		}
		return true;
	}
	public static void main(String[] args) throws Exception {
		IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance("default",KnowledgeBaseType.IN_MEMORY);
		
		RelationQuery query = new RelationQuery();
		
		//query.addRelationTypeToExclude(knowledgeBase.getSubclassOfRelationType());
		//query.addRelationTypeToExclude(knowledgeBase.getInstanceOfRelationType());
		//query.addRelationTypeToExclude(knowledgeBase.getSubrelationOfRelationType());
		//query.addLeftObjectToInclude(knowledgeBase.getConcept("Man"));
		query.addConnectorToInclude(knowledgeBase.getIsSubclassOfConnector());
		query.addRightEntityToInclude(knowledgeBase.getConceptFromName("Object",GetMode.CREATE_IF_DOES_NOT_EXIST));
		
		
		
		System.out.println(query.executeOn(knowledgeBase.getRelationList()));
	}

}
