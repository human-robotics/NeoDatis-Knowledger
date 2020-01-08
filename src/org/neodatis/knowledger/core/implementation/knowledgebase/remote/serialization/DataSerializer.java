package org.neodatis.knowledger.core.implementation.knowledgebase.remote.serialization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.LazyRelation;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.implementation.knowledgebase.remote.RemoteKnowledgeBaseClient;


public class DataSerializer {
	private static Map<String,ISerializer> serializers = null;
	private static Map<Class, String> keys = null;
	private static DataSerializer instance = null;
	
	public static synchronized DataSerializer getInstance(boolean isServerSide){
		if(instance==null){
			instance = new DataSerializer(isServerSide);
		}
		return instance;
	}
	
	private DataSerializer(boolean isServerSide){
		serializers = new HashMap<String, ISerializer>();
		serializers.put("C",new ConceptSerializer());
		serializers.put("I",new InstanceSerializer());
		serializers.put("CO",new ConnectorSerializer());
		serializers.put("R",new RelationSerializer(isServerSide));		
		
		keys = new HashMap<Class, String>();
		keys.put(Concept.class,"C");
		keys.put(Instance.class,"I");
		keys.put(Connector.class,"CO");
		keys.put(Relation.class,"R");		
		keys.put(LazyRelation.class,"R");

	}
	
	public String toString(KnowledgeBase knowledgeBase){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(toString(knowledgeBase.getInstanceList()));
		buffer.append(toString(knowledgeBase.getConceptList()));
		buffer.append(toString(knowledgeBase.getConnectorList()));
		buffer.append(toString(knowledgeBase.getRelationList()));
		return buffer.toString();
	}
	
	public String toString(List objectList){
		
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<objectList.size();i++){
			buffer.append(toString(objectList.get(i))).append("\n");
		}
		return buffer.toString();
	}
	
	public String toString(Object object){
		
		String key = (String) keys.get(object.getClass());
		
		ISerializer serializer = (ISerializer) serializers.get(key);
		if(serializer!=null){
			return serializer.toString(object);
		}
		
        throw new RuntimeException("toString not implemented for " + object.getClass().getName());
    }
    
    public ObjectContainer fromString(String data) throws Exception{
      
    	ObjectContainer container = new ObjectContainer();
    	String [] lines = data.split("\n");
    	for(int i=0;i<lines.length;i++){
    		if(lines[i]!=null && lines[i].trim().length()>0){
    			container.add(fromOneString(lines[i]));
    		}
    	}
    	return container;
    }
    
    public KnowledgeBase knowledgeBaseFromString(String data,String name, String host, String port,String language) throws Exception{
    	ObjectContainer container = fromString(data);
    	RemoteKnowledgeBaseClient kb = new RemoteKnowledgeBaseClient(name,host,port,language);
    	kb.setInstanceList(container.getInstanceList());
    	kb.setConceptList(container.getConceptList());
    	kb.setConnectorList(container.getConnectorList());
    	kb.setRelationList(container.getRelationList());
    	return kb;
    }
    public Object fromOneString(String data) throws Exception{
    	
        int index = data.indexOf(";");
    	
        if(index==-1){
            return null;
        }
        String type = data.substring(0,index);
    	
    	ISerializer serializer = (ISerializer) serializers.get(type);
		if(serializer!=null){
			return serializer.fromString(data);
		}
        
		throw new RuntimeException("fromString unimplemented for " + type);
        
    }
    
    public static void main(String[] args) {
		String s= "ola1\nola2\n ";
		String [] lines = s.split("\n");
		System.out.println(lines.length);
	}
}
