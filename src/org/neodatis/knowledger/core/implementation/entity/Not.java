package org.neodatis.knowledger.core.implementation.entity;

import org.neodatis.knowledger.core.implementation.criteria.AbstractCriteria;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;

public class Not extends AbstractCriteria {

	private ICriteria criteria;
	
	public Not(ICriteria criteria){
		this.criteria = criteria;
	}
	public boolean match(KnowledgerObject object) {
		return !criteria.match(object);
	}
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" not ").append(criteria);
		return buffer.toString();
	}


}
