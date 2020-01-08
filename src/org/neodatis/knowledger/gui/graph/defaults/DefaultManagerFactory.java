/* 
 * $RCSfile: DefaultManagerFactory.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph.defaults;

import org.neodatis.knowledger.gui.GuiLogger;
import org.neodatis.knowledger.gui.graph.IActionManager;
import org.neodatis.knowledger.gui.graph.IInteractionManager;
import org.neodatis.knowledger.gui.graph.IManagerFactory;
import org.neodatis.knowledger.gui.graph.IPersistentManager;
import org.neodatis.knowledger.gui.graph.basic.IGraphProvider;
import org.neodatis.knowledger.gui.graph.panel.KnowledgerGraphPanel;
import org.neodatis.tool.ILogger;


public class DefaultManagerFactory implements IManagerFactory {

    private static final String DEFAULT_INTERACTION_MANAGER_CLASS_NAME = "org.neodatis.knowledger.gui.graph.defaults.KnowledgerInteractionManager";
    private static String interactionClassName;
    private IPersistentManager persistentManager;
    private IActionManager actionManager;
    private IGraphProvider graphProvider;
    private IInteractionManager interactionManager;
    private ILogger logger;
    private static boolean hasDefaultInteractionManager = true;
    
    public DefaultManagerFactory(KnowledgerGraphPanel graphPanel,String containerFileName, String knowledgeBaseName,String host,String port) throws Exception{
    	logger = new GuiLogger();
        if(host==null || port == null){
    		persistentManager = new ODBPersistentManager(containerFileName,knowledgeBaseName);
    	}else{
        	persistentManager = new RemoteODBPersistentManager(containerFileName,knowledgeBaseName,host,port);    		
    	}

        //interactionManager = new KnowledgerInteractionManager(persistentManager,graphProvider,graphPanel,logger,actionManager);
        if(interactionClassName==null){
            interactionClassName = DEFAULT_INTERACTION_MANAGER_CLASS_NAME;
            hasDefaultInteractionManager = true;
        }
        interactionManager = (IInteractionManager) Class.forName(interactionClassName).newInstance();

        graphProvider = new DefaultGraphProvider(persistentManager,interactionManager);
    	actionManager = new DefaultActionManager(graphProvider,persistentManager,graphPanel);
        
        interactionManager.setPersistentManager(persistentManager);
        interactionManager.setActionManager(actionManager);
        interactionManager.setLogger(logger);
        interactionManager.setKnowledgerGraphPanel(graphPanel);
        interactionManager.setGraphProvider(graphProvider);
        interactionManager.init();
        if(!hasDefaultInteractionManager){
        	persistentManager.setFilter(interactionManager);
        }
        //interactionManager = new MyInteractionManager(persistentManager,graphProvider,graphPanel,logger,actionManager);
    }

    public IPersistentManager getPersistentManager() {
        return persistentManager;
    }

    public IGraphProvider getGraphProvider(){
        return graphProvider;
    }
    /**
     * @return Returns the interactionManager.
     */
    public IInteractionManager getInteractionManager() {
        return interactionManager;
    }
    
    public static void setInteractionManagerClassName(String className){
        interactionClassName = className;
    }
    
}
