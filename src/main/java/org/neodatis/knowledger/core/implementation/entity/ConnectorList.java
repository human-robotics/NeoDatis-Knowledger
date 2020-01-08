/* 
 * $RCSfile: ConnectorList.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation.entity;

import java.util.Collection;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;


/**
 * <p>
 * 
 * </p>
 * 
 */
public class ConnectorList extends EntityList{

    public ConnectorList(){
        super();
    }
    public ConnectorList(Collection collection){
        super(collection);
    }
    public ConnectorList(KnowledgeBase knowledgeBase,Collection collection){
        super(knowledgeBase,collection);
    }

    /* (non-Javadoc)
     * @see org.neodatis.knowledger.core.interfaces.entity.IConnectorList#getConnector(int)
     */
    public Connector getConnector(int index) {
        return (Connector) get(index);
    }
    
    public ConnectorList getConnectors(ICriteria criteria){
        EntityList entityList = filter(criteria);
        return new ConnectorList(entityList);
    }

}
