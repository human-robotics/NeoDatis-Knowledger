/* 
 * $RCSfile: RemoteODBPersistentManager.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph.defaults;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;

/**
 * The persistent manager using ODB (Dialogy Object Database)
 * @author olivier s
 *
 */
public class RemoteODBPersistentManager extends ODBPersistentManager {
    
    public RemoteODBPersistentManager(String containerFileName, String name,String host, String port) throws Exception{
    	super();
    	knowledgeBase = KnowledgeBaseFactory.getInstance(containerFileName, name,KnowledgeBaseType.REMOTE,host,port);
    }
}
