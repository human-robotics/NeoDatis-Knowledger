package org.neodatis.knowledger.core.implementation.knowledgebase.remote.serialization;

import java.util.StringTokenizer;

import org.neodatis.knowledger.Knowledger;
import org.neodatis.knowledger.core.implementation.entity.Concept;


public class ConceptSerializer implements ISerializer{
	public String TYPE = "C";
	
    public String toString(Object object){
    	
    	Concept concept = (Concept) object;
    	
        StringBuffer buffer = new StringBuffer();
        buffer.append(getType()).append(SEPARATOR);

        buffer.append(concept.getKnowledgeBase().getId()).append(SEPARATOR);
        buffer.append(concept.getId()).append(SEPARATOR);
        buffer.append(concept.getIdentifier()).append(SEPARATOR);
        buffer.append(dateFormat.format(concept.getCreationDate()));
        
        return buffer.toString();
    }
    
    public Object fromString(String data) throws Exception{
        
    	String tmp = null;
        StringTokenizer tokenizer = new StringTokenizer(data,SEPARATOR);
        Concept concept = new Concept();

        String type = (String) tokenizer.nextElement();
        if(!type.equals(getType())){
            throw new RuntimeException("Trying to de-serialize a Concept but found a " + type);
        }
        
        concept.setKnowledgeBase(Knowledger.getBase((String) tokenizer.nextElement()));
        tmp = (String) tokenizer.nextElement();
        if(tmp!=null && !tmp.equals("null")){
        	concept.setId(tmp);
        }
        concept.setIdentifier((String) tokenizer.nextElement());
        
        tmp = (String) tokenizer.nextElement();
        concept.setCreationDate(tmp==null?null:dateFormat.parse(tmp));
        return concept;
        
    }

    
	protected String getType(){
    	return TYPE;
    }
}
