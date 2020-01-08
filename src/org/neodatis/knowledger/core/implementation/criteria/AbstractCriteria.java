package org.neodatis.knowledger.core.implementation.criteria;

import org.neodatis.knowledger.core.implementation.entity.And;
import org.neodatis.knowledger.core.implementation.entity.Not;
import org.neodatis.knowledger.core.implementation.entity.Or;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;

public abstract class AbstractCriteria implements ICriteria {

	public ICriteria and(ICriteria criteria) {
		return new And(this,criteria);
	}

	public ICriteria or(ICriteria criteria) {
		return new Or(this,criteria);
	}

	public ICriteria not() {
		return new Not(this);
	}
}
