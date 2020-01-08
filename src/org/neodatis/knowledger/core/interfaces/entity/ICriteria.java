/*
 * Created on Oct 24, 2004
 *
 * neodatis 2004 - www.neodatis.com
 */
package org.neodatis.knowledger.core.interfaces.entity;

import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;

/**
 * @author olivier
 * An interface for all criterias
 *
 */
public interface ICriteria {
	boolean match(KnowledgerObject object);
	ICriteria and(ICriteria criteria);
	ICriteria or(ICriteria criteria);
	ICriteria not();
}
