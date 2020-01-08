package org.neodatis.knowledger.gui.graph;

import java.util.HashMap;
import java.util.Map;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;


public class Configurations {
	
	/** To keep track of all configurations*/
	private static Map configurations = new HashMap();
	
	public static Configuration getConfiguration(String knowledgeBaseName){
		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.internalGetOpenInstance(knowledgeBaseName);
		if(knowledgeBase==null){
			throw new RuntimeException("knowledge base with name " + knowledgeBaseName + " does not exist");
		}
		return getConfiguration(knowledgeBase,"default");
	}

	public static Configuration getConfiguration(String knowledgeBaseName, String configurationName){
		return getConfiguration(KnowledgeBaseFactory.internalGetOpenInstance(knowledgeBaseName),configurationName);
	}
	public static Configuration getConfiguration(KnowledgeBase kb){
		return getConfiguration(kb,"default");
	}
	public static Configuration getConfiguration(KnowledgeBase kb, String configurationName){
		String key = kb.getName() + "-"+configurationName;
		Configuration configuration = (Configuration) configurations.get(key);
		if(configuration==null){
			configuration = new Configuration(kb,configurationName);
			configurations.put(key,configuration);
		}
		return configuration;
	}
}
