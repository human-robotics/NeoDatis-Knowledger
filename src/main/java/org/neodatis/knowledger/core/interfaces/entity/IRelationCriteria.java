/*
 * Created on Oct 24, 2004
 *
 */
package org.neodatis.knowledger.core.interfaces.entity;

import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;

/**
 * @author olivier
 *
 */
public interface IRelationCriteria extends ICriteria{
    KnowledgerObject getLeftObject();
    Connector getConnector();
    KnowledgerObject getRightObject();
}
