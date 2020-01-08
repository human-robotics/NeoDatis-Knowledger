/*
 * Created on Dec 22, 2004
 *
 *
 */
package org.neodatis.knowledger.core.implementation.knowledgebase.remote.serialization;

import java.util.ArrayList;
import java.util.List;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.ConceptList;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.implementation.entity.LazyRelation;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.entity.RelationList;


/**
 * @author olivier
 *
 * */
public class ObjectContainer {
	private ConceptList conceptList;
	private InstanceList instanceList;
	private ConnectorList connectorList;
	private RelationList relationList;
	
	public boolean hasConcepts(){
		return !conceptList.isEmpty();
	}
	public boolean hasInstances(){
		return !instanceList.isEmpty();
	}
	public boolean hasConnectors(){
		return !connectorList.isEmpty();
	}
	public boolean hasRelations(){
		return !relationList.isEmpty();
	}
	
	public List getAllObjects(){
		List<Entity> allObjects = new ArrayList<Entity>();
		allObjects.addAll(conceptList);
		allObjects.addAll(instanceList);
		allObjects.addAll(connectorList);
		allObjects.addAll(relationList);
		return allObjects;
	}
	
	/**
	 * 
	 */
	public ObjectContainer() {
		conceptList = new ConceptList();
		instanceList = new InstanceList();
		connectorList = new ConnectorList();
		relationList = new RelationList();
	}
	
	public void add(Object object){
	    if(object==null){
	        return;
	    }
		if(object.getClass()==Concept.class){
			conceptList.add(object);
			return;
		}
		if(object.getClass()==Instance.class){
			instanceList.add(object);
			return;
		}
		if(object.getClass()==Connector.class){
			connectorList.add(object);
			return;
		}
		if(object.getClass()==Relation.class || object.getClass()==LazyRelation.class){
			relationList.add(object);
			return;
		}
		throw new RuntimeException("ObjectContainer can not handle " + object.getClass().getName() + " Objects");
	}
	public ConceptList getConceptList() {
		return conceptList;
	}
	public void setConceptList(ConceptList conceptList) {
		this.conceptList = conceptList;
	}
	public ConnectorList getConnectorList() {
		return connectorList;
	}
	public void setConnectorList(ConnectorList connectorList) {
		this.connectorList = connectorList;
	}
	public InstanceList getInstanceList() {
		return instanceList;
	}
	public void setInstanceList(InstanceList instanceList) {
		this.instanceList = instanceList;
	}
	public RelationList getRelationList() {
		return relationList;
	}
	public void setRelationList(RelationList relationList) {
		this.relationList = relationList;
	}
	
}
