package org.neodatis.knowledger.core.implementation.knowledgebase;

/**
 * The basic interface to load/store a knowledge base
 * @author olivier
 *
 */
public interface KnowledgeBaseIO {
	
	/** Loads a knowledge base with the specific id*/
	KnowledgeBase load(String knowledgeBaseId);
	
	/** Stores the knowledge */
	void store(KnowledgeBase base);

}
