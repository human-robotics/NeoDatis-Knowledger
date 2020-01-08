package org.neodatis.knowledger.core.implementation.criteria;

import org.neodatis.knowledger.core.implementation.entity.Connector;


public class RelationConnectorCriteria extends RelationCriteria {
	
	public RelationConnectorCriteria(String connectorName){
		super(null,connectorName,null);
	}
	public RelationConnectorCriteria(Connector connector){
		super(null,connector,null);
	}
}
