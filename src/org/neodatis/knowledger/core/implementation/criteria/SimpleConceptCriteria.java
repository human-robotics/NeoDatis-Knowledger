package org.neodatis.knowledger.core.implementation.criteria;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.interfaces.entity.IConceptCriteria;

public class SimpleConceptCriteria extends AbstractCriteria implements IConceptCriteria {

	private String conceptName;
	
	public SimpleConceptCriteria(String conceptName){
		this.conceptName = conceptName;
	}

	public boolean match(KnowledgerObject object) {
		if(object.getClass()!=Concept.class){
			return false;
		}
		Concept concept = (Concept) object;
		
		boolean match = concept.getName().equalsIgnoreCase(conceptName);
		return match;
	}

}
