/*
 * NeoDatis 20004 - www.neodatis.com
 * Created on Oct 24, 2004
 *
 */
package org.neodatis.knowledger.core.factory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.neodatis.knowledger.core.implementation.knowledgebase.InMemoryKnowledgeBase;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.implementation.knowledgebase.ODBKnowledgeBase;
import org.neodatis.knowledger.core.implementation.knowledgebase.remote.RemoteKnowledgeBaseClient;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;


/**
 * @author olivier
 * 
 */
public class KnowledgeBaseFactory {
	private static Logger logger = Logger.getLogger(KnowledgeBaseFactory.class);
    /** To store all knowledge base instance , the map key of a knowledge base is the concatenation of name + "-" + type
     * Example : knowledge base name = kb1 and type is db-persistent, the key will be kb1-db-persistent*/
    private static Map knowledgeBases = new HashMap();

    /** Returns a knowledge base with the specified name - assumes that the base has already been opened
     * 
     * @param baseName The base name
     * @return
     */
    public static synchronized KnowledgeBase internalGetOpenInstance(String baseName) {
        return (KnowledgeBase) knowledgeBases.get(baseName);
    }
    public static synchronized KnowledgeBase getInstance(String name) throws Exception {
    	return getInstance(name,KnowledgeBaseType.ODB,null,null);
    }
    public static synchronized KnowledgeBase getInstance(String name,String host,String port) throws Exception {
    	return getInstance(name,KnowledgeBaseType.ODB,host,port);
    }

    public static synchronized KnowledgeBase getInstance(String name, KnowledgeBaseType knowledgeBaseType) throws Exception {
    	return getInstance(name,knowledgeBaseType,null,null);
    }
    public static synchronized void remove(String baseName){
    	knowledgeBases.remove(baseName);
	}
    public static synchronized KnowledgeBase getInstance(String baseName, KnowledgeBaseType knowledgeBaseType,String host,String port) throws Exception {
    	return getInstance(baseName+".knowledger",baseName,knowledgeBaseType,host,port);
    }
    public static synchronized KnowledgeBase getInstance(String containerFilename,String baseName, KnowledgeBaseType knowledgeBaseType) throws Exception {
    	return getInstance(containerFilename,baseName,knowledgeBaseType,null,null);
    }
    public static synchronized KnowledgeBase getInstance(String containerFilename,String baseName, KnowledgeBaseType knowledgeBaseType,String host,String port) throws Exception {
        // Configure ODB not to use modify class - using ASM
    	//Configuration.setUseModifiedClass(false);
    	logger.info("Getting Knowledge base " + baseName + " from container " + containerFilename);
    	// Checks if it already exists
        String cleanName = getCleanName(baseName);
        
        KnowledgeBase knowledgeBase = (KnowledgeBase) knowledgeBases.get(cleanName);
        
        if(knowledgeBase==null){
            if(knowledgeBaseType == KnowledgeBaseType.DATABASE){
                throw new RuntimeException("Database Knowledge base not implemented");
            }
            if(knowledgeBaseType == KnowledgeBaseType.IN_MEMORY){
                knowledgeBase = new InMemoryKnowledgeBase(baseName);
            }
            if(knowledgeBaseType == KnowledgeBaseType.REMOTE){
            	knowledgeBase = RemoteKnowledgeBaseClient.getInstance(baseName,host,port);
            	// Put the base in the map in order to execute the optimize strorage method
            	//TODO check if this can be more generic
            	knowledgeBases.put(cleanName,knowledgeBase);
            	knowledgeBase.optimizeStorage();
            }

            if(knowledgeBaseType == KnowledgeBaseType.FILE){
            	knowledgeBase = FileKnowledgeBaseFactory.getInstance(baseName);
            }
            if(knowledgeBaseType == KnowledgeBaseType.ODB){
            	knowledgeBase = ODBKnowledgeBase.getInstance(containerFilename,baseName);
            }

            if(knowledgeBaseType == KnowledgeBaseType.DB4O){
                throw new Exception("DB4O Knowledge base not implemented");
            }

            if(knowledgeBase == null){
                throw new RuntimeException("Unknown or not implemented Knowledge base type " + knowledgeBaseType.name());
            }
            knowledgeBases.put(cleanName,knowledgeBase);
        }
        return knowledgeBase;
    }
    /** Returns all knowledge base descriptions
     * 
     * @return
     * @throws Exception
     */
    public static synchronized KnowledgeBaseDescription[] getAllKnowledgebaseDescriptions() throws Exception{
    	KnowledgeBaseDescription [] kbd1 = ODBKnowledgeBase.getAllKnowledgeBaseDescriptions();
    	KnowledgeBaseDescription [] kbd2 = FileKnowledgeBaseFactory.getAllKnowledgeBaseDescriptions();
    	KnowledgeBaseDescription [] kbd = new KnowledgeBaseDescription[kbd1.length + kbd2.length ];
    	
    	for(int i=0;i<kbd1.length;i++){
    		kbd[i] = kbd1[i];
    	}
    	for(int i=0;i<kbd2.length;i++){
    		kbd[i+kbd1.length] = kbd2[i];
    	}
    	return kbd;
    	
    	
    }
    public static void register(KnowledgeBase kb){
        String key = kb.getId();
        knowledgeBases.put(key,kb);
    }
    public static String getCleanName(String fullName){
        File file = new File(fullName);
        String cleanName = file.getName();
        int index = cleanName.lastIndexOf(".");
        if(index!=-1){
        	cleanName = cleanName.substring(0,index);
        }
        return cleanName;
    }
}
