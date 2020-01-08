package org.neodatis.knowledger.core.implementation.knowledgebase.remote.serialization;

import java.util.Date;
import java.util.StringTokenizer;

import org.neodatis.knowledger.Knowledger;
import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.LazyRelation;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;


public class RelationSerializer implements ISerializer {
	public String TYPE = "R";
	
	private boolean isServerSide;
	
	public RelationSerializer(boolean serverSide){
		isServerSide = serverSide;
	}
	

	public String toString(Object object) {

		Relation relation = (Relation) object;

		StringBuffer buffer = new StringBuffer();
		buffer.append(getType()).append(SEPARATOR);

		buffer.append(relation.getKnowledgeBase().getId()).append(SEPARATOR);
		buffer.append(relation.getId()).append(SEPARATOR);
		buffer.append(relation.getLeftEntity().getId()).append(SEPARATOR);
		buffer.append(relation.getConnector().getId()).append(SEPARATOR);
		buffer.append(relation.getRightEntity().getId()).append(SEPARATOR);
		buffer.append(dateFormat.format(relation.getCreationDate()));

		return buffer.toString();
	}

	public Object fromString(String data) throws Exception {

		String tmp = null;
		StringTokenizer tokenizer = new StringTokenizer(data, SEPARATOR);
		

		String type = (String) tokenizer.nextElement();
		if (!type.equals(getType())) {
			throw new RuntimeException("Trying to de-serialize a Relation but found a " + type);
		}
		String knowledgeBaseIdentifier = (String) tokenizer.nextElement();
		String id = null;
		tmp = (String) tokenizer.nextElement();
		if (tmp != null && !tmp.equals("null")) {
			id = tmp;
		}
		String leftEntityId = (String) tokenizer.nextElement();
		String connectorId= (String) tokenizer.nextElement();
		String rightEntityId = (String) tokenizer.nextElement();
		tmp = (String) tokenizer.nextElement();
		Date creationDate = tmp == null ? null : dateFormat.parse(tmp);
		
		Relation relation = null;
		if(!isServerSide){
			relation = new LazyRelation(Knowledger.getBase(knowledgeBaseIdentifier) ,id,leftEntityId,connectorId,rightEntityId);
		}else{
			KnowledgeBase kb = KnowledgeBaseFactory.internalGetOpenInstance(knowledgeBaseIdentifier);
			
			relation = new Relation((Entity) kb.getObjectFromId(leftEntityId),kb.getConnectorFromId(connectorId),(Entity)kb.getObjectFromId(rightEntityId));
			relation.setId(id);
			relation.setKnowledgeBase(kb);			
		}
		
		relation.setCreationDate(creationDate);
		return relation;

	}

	protected String getType() {
		return TYPE;
	}
}
