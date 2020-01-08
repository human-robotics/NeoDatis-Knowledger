package org.neodatis.knowledger.core.implementation.entity;

import org.neodatis.knowledger.core.implementation.criteria.AbstractCriteria;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;

public class And extends AbstractCriteria {

	private ICriteria criteria1;

	private ICriteria criteria2;

	public And(ICriteria criteria1, ICriteria criteria2) {
		this.criteria1 = criteria1;
		this.criteria2 = criteria2;
	}

	public boolean match(KnowledgerObject object) {
		return criteria1.match(object) && criteria2.match(object);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(criteria1).append(" and ").append(criteria2);
		return buffer.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
