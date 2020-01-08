package org.neodatis.knowledger.core.implementation.entity;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;

/**
 */
public class Connector extends Entity{
    public Connector(){
        super(null);
    }
    public Connector(String name){
        super(null,name);
    }
    public Connector(KnowledgeBase knowledgeBase,String name){
        super(knowledgeBase,name);
    }

    public String getName(){
    	return getIdentifier();
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getName());
        return buffer.toString();
    }

    public Relation connectTo(Connector connector, Entity entity) throws Exception {
		return getKnowledgeBase().createRelation(this, connector, entity);
	}
    
    public boolean isSubConnectorOf(Connector connector) throws Exception{
        return getKnowledgeBase().queryRelations(this,getKnowledgeBase().getIsSubRelationOfConnector(),connector).size()>0;
    }
    public boolean isSuperConnectorOf(Connector connector) throws Exception{
        return getKnowledgeBase().queryRelations(connector,getKnowledgeBase().getIsSubRelationOfConnector(),this).size()>0;
    }
}
