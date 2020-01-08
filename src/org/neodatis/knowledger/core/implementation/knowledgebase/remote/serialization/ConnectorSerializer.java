package org.neodatis.knowledger.core.implementation.knowledgebase.remote.serialization;

import java.util.StringTokenizer;

import org.neodatis.knowledger.Knowledger;
import org.neodatis.knowledger.core.implementation.entity.Connector;


public class ConnectorSerializer implements ISerializer{
	public String TYPE = "CO";
	
    public String toString(Object object){
    	
    	Connector connector = (Connector) object;
    	
        StringBuffer buffer = new StringBuffer();
        buffer.append(getType()).append(SEPARATOR);

        buffer.append(connector.getKnowledgeBase().getId()).append(SEPARATOR);
        buffer.append(connector.getId()).append(SEPARATOR);
        buffer.append(connector.getIdentifier()).append(SEPARATOR);
        buffer.append(dateFormat.format(connector.getCreationDate()));
        
        return buffer.toString();
    }
    
    public Object fromString(String data) throws Exception{
        
    	String tmp = null;
        StringTokenizer tokenizer = new StringTokenizer(data,SEPARATOR);
        Connector connector = new Connector();

        String type = (String) tokenizer.nextElement();
        if(!type.equals(getType())){
            throw new RuntimeException("Trying to de-serialize a Connector but found a " + type);
        }
        connector.setKnowledgeBase(Knowledger.getBase((String) tokenizer.nextElement()));
        tmp = (String) tokenizer.nextElement();
        if(tmp!=null && !tmp.equals("null")){
        	connector.setId(tmp);
        }
        connector.setIdentifier((String) tokenizer.nextElement());
        
        tmp = (String) tokenizer.nextElement();
        connector.setCreationDate(tmp==null?null:dateFormat.parse(tmp));
        return connector;
        
    }

    
	protected String getType(){
    	return TYPE;
    }
}
