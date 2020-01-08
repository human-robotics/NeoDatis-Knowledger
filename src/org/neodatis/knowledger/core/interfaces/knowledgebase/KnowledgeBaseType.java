package org.neodatis.knowledger.core.interfaces.knowledgebase;

import java.io.Serializable;

public class KnowledgeBaseType implements Serializable{
	private String name;
	/** To know if the system must generate a unique id for the objects of this database*/
	private boolean needsUniqueIdGeneration;
	
	public static final KnowledgeBaseType IN_MEMORY = new KnowledgeBaseType("IN_MEMORY",true);
	public static final KnowledgeBaseType DATABASE = new KnowledgeBaseType("DATABASE",false);
	public static final KnowledgeBaseType FILE = new KnowledgeBaseType("FILE",true);
	public static final KnowledgeBaseType ODB = new KnowledgeBaseType("ODB",true);
    public static final KnowledgeBaseType DB4O = new KnowledgeBaseType("DB4O",true);
    public static final KnowledgeBaseType REMOTE = new KnowledgeBaseType("REMOTE",true);
	
    public KnowledgeBaseType(){       
    }
	protected KnowledgeBaseType(String type,boolean needsUniqueIdGeneration){
		this.name= type;
		this.needsUniqueIdGeneration = needsUniqueIdGeneration;
	}

	public String getName() {
		return name;
	}
	/** for jdk 1.5 enum compatibility*/
	public String name(){
		return name;
	}

	public boolean needsUniqueIdGeneration() {
		return needsUniqueIdGeneration;
	}

    public String toString() {
        return name;
    }
	
}
