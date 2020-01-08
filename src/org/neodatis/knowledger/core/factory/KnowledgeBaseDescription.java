package org.neodatis.knowledger.core.factory;

import java.io.File;
import java.io.Serializable;

import org.neodatis.knowledger.core.implementation.entity.Language;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;


public class KnowledgeBaseDescription implements Serializable{

	/** The name of the database */
	protected String name;

	/** The type of the knowledge base*/
	protected KnowledgeBaseType type;
	
	protected String id;
    protected Language language;

	
	public KnowledgeBaseDescription() {
	}

	/**
     * @return Returns the language.
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * @param language The language to set.
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    public KnowledgeBaseDescription(String name, KnowledgeBaseType type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public KnowledgeBaseType getType() {
		return type;
	}

	public void setType(KnowledgeBaseType type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String toString() {
		return getName();
	}

    public String getIdentifier(){
        File file = new File(name);
        String cleanName = file.getName();
        int index = cleanName.lastIndexOf(".");
        if(index!=-1){
        	cleanName = cleanName.substring(0,index);
        }

        return cleanName;
    }
	
}
