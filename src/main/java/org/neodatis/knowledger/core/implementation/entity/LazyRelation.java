package org.neodatis.knowledger.core.implementation.entity;

import java.util.List;

import org.neodatis.knowledger.core.ObjectDoesNotExistException;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;


/**
 * This is a relation. But instead of having the reference to its objects
 * (left entities, Connector, and right entities) maintain its ids. When needed,
 * gets the real objects from the ids
 */
public class LazyRelation extends Relation {

	transient String[] leftIds;

	transient String connectorId;

	transient String[] rightIds;

	public LazyRelation(KnowledgeBase base, String id, String leftId, String connectorid, String rightId) {
		super();
		setKnowledgeBase(base);
		setId(id);
		leftIds = new String[1];
		rightIds = new String[1];
		leftIds[0] = leftId;
		connectorId = connectorid;
		rightIds[0] = rightId;
	}

	public Connector getConnector() {
		if (connector == null) {
			try {
				connector = getKnowledgeBase().getConnectorFromId(connectorId);
			} catch (ObjectDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return connector;
	}

	public List getLeftEntities() {
		if (leftEntities == null) {
			initLeftEntitiesFromIds();
		}
		return leftEntities;
	}

	private void initLeftEntitiesFromIds() {
		KnowledgeBase kb = getKnowledgeBase();
		leftEntities = new EntityList();
		for (int i = 0; i < leftIds.length; i++) {
			leftEntities.add(kb.getObjectFromId(leftIds[i]));
		}
	}

	private void initRightEntitiesFromIds() {
		KnowledgeBase kb = getKnowledgeBase();
		rightEntities = new EntityList();
		for (int i = 0; i < rightIds.length; i++) {
			rightEntities.add(kb.getObjectFromId(rightIds[i]));
		}
	}

	public Entity getLeftEntity() {
		if (leftEntities == null) {
			initLeftEntitiesFromIds();
		}
		return super.getLeftEntity();
	}

	public EntityList getRightEntities() {
		if (rightEntities == null) {
			initRightEntitiesFromIds();
		}
		return rightEntities;
	}

	public Entity getRightEntity() {
		if (rightEntities == null) {
			initRightEntitiesFromIds();
		}
		return super.getRightEntity();
	}
	
	public String toString() {
		// defensive toString
		if(leftEntities==null || rightEntities==null){
			StringBuffer buffer = new StringBuffer();
			buffer.append(leftIds[0]).append(" ").append(connectorId).append(" ").append(rightIds[0]);
			return buffer.toString(); 
		}
		return super.toString();
	}

}
