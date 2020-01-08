package org.neodatis.knowledger.core.implementation.entity;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;

/**
 */
public class Instance extends Entity {
	
    public Instance() {        
    }
    public Instance(Concept concept) {
		super(concept.getKnowledgeBase());
	}

	public Instance(KnowledgeBase knowledgeBase, String value) {
		super(knowledgeBase);
		setIdentifier(value);
	}

	public Instance(String value) {
		this(null, value);
	}

	/**
	 */
	public String getValue() {
		return getIdentifier();
	}

	public void setValue(String string) {
		setIdentifier(string);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		// buffer.append("#").append(getId());
		if (getValue() != null && getValue().length()>0) {
			buffer.append(getValue());
		}else{
			try {
				if(hasProperty("name")){
					buffer.append(getProperty("name"));
				}else{
					buffer.append("#").append(getId());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	

		return buffer.toString();
	}

	public Concept getConcept() throws Exception {
		Concept concept = (Concept) getProperty(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF);
        if(concept==null){
            throw new Exception("Instance does not have type : instance id = " + getId() + "-" + getValue());
        }
        return concept;
	}

	public Instance update() throws Exception {
		return getKnowledgeBase().updateInstance(this);
	}

	public Instance delete() throws Exception {
		return getKnowledgeBase().deleteInstance(this);
	}

	

}
