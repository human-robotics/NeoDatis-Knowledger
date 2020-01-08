package org.neodatis.knowledger.core.implementation.entity;

import java.util.ArrayList;
import java.util.List;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;


/**
 * @hibernate.joined-subclass table="proposition" 
 * @hibernate.joined-subclass-key column="id"
 */
public class Proposition extends Entity{

	private List relations;
	private int reliability;
	
	public Proposition(){
		this(null);
	}

	public Proposition(KnowledgeBase knowledgeBase,Relation relation){
	    this(knowledgeBase);
	    add(relation);
	}
	public Proposition(KnowledgeBase knowledgeBase){
	    super(knowledgeBase);
	    relations = new ArrayList();
	}
	
	public List getRelations() {
		return relations;
	}
	
	public void add(Relation relation){
	    
	}
	/**
	 * @hibernate.roperty column="reliability"
	 */
	public int getReliability() {
		return reliability;
	}

	public void setRelations(List list) {
		relations = list;
	}

	public void setReliability(int i) {
		reliability = i;
	}
	
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getIdentifier()).append(" - ").append(getRelations());        
        
        return buffer.toString();
    }


}
