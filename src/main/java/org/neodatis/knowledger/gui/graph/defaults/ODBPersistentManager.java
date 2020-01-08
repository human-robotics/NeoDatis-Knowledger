/* 
 * $RCSfile: ODBPersistentManager.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph.defaults;

import java.io.IOException;
import java.util.Iterator;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.ConceptList;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.implementation.query.RelationQuery;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.knowledger.gui.graph.Configuration;
import org.neodatis.knowledger.gui.graph.Configurations;
import org.neodatis.knowledger.gui.graph.IFilter;
import org.neodatis.knowledger.gui.graph.IPersistentManager;


/**
 * The persistent manager using ODB (Dialogy Object Database)
 * @author olivier s
 *
 */
public class ODBPersistentManager implements IPersistentManager {
    protected KnowledgeBase knowledgeBase;
    protected IFilter filter;
    
    public ODBPersistentManager() throws Exception{
        
    }
    public ODBPersistentManager(String containerFileName, String baseName) throws Exception{
       	/*
    	if(baseName.indexOf(".odb")!=-1){
        	baseName = name.replaceAll(".odb","");
        }
        */
    	knowledgeBase = KnowledgeBaseFactory.getInstance(containerFileName,baseName,KnowledgeBaseType.ODB);
    }
    public ODBPersistentManager(String baseName) throws Exception{
    	knowledgeBase = KnowledgeBaseFactory.getInstance(baseName,KnowledgeBaseType.ODB);
    }
    
    
    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#getRelations()
	 */
    public RelationList getRelations() throws Exception{
    	if(filter!=null){
            Relation relation = null;
            RelationList allRelations = knowledgeBase.getRelationList();
            RelationList relations = new RelationList();
            Iterator iterator = allRelations.iterator();
            Entity leftEntity = null;
            Entity rightEntity = null;
            while(iterator.hasNext()){
                relation = (Relation) iterator.next();
                leftEntity = relation.getLeftEntity();
                if(!filter.display(leftEntity)){
                    continue;
                }
                rightEntity = relation.getRightEntity();
                if(!filter.display(rightEntity)){
                    continue;
                }
                relations.add(relation);
            }
            return relations;
            
        }else{
            Configuration configuration = Configurations.getConfiguration(knowledgeBase);
            RelationQuery query = configuration.getVisualizationConfiguration().getRelationQuery();
            
            String sQuery = configuration.getVisualizationConfiguration().getQuery();
            if(sQuery!=null && sQuery.length()!=0){
                EntityList entityList = knowledgeBase.query(sQuery);
                query.addEntitiesToInclude(entityList);
            }
            return query.executeOn(knowledgeBase.getRelationList());
        }
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#getConcepts()
	 */
    public ConceptList getConcepts(){
        if(filter==null){
            return knowledgeBase.getConceptList();
        }
        
        Concept concept = null;
        ConceptList allConcepts = knowledgeBase.getConceptList();
        ConceptList concepts = new ConceptList();
        Iterator iterator = allConcepts.iterator();
        while(iterator.hasNext()){
            concept = (Concept) iterator.next();
            if(filter.display(concept)){
                concepts.add(concept);
            }
        }
        return concepts;
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#getInstances()
	 */
    public InstanceList getInstances(){
        if(filter==null){
            return knowledgeBase.getInstanceList();
        }
        
        Instance instance = null;
        InstanceList allInstances = knowledgeBase.getInstanceList();
        InstanceList instances = new InstanceList();
        Iterator iterator = allInstances.iterator();
        while(iterator.hasNext()){
            instance = (Instance) iterator.next();
            if(filter.display(instance)){
                instances.add(instance);
            }
        }
        return instances;
    }

    public InstanceList getInstancesOf(Concept concept,boolean direct) throws Exception{
    	if(direct){
    		return concept.getDirectInstances();
    	}
    	return concept.getInstances();
    }
    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#getConnectors()
	 */
    public ConnectorList getConnectors(){
        return knowledgeBase.getConnectorList();
    }
    
    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#getIsAConnector()
	 */
    public Connector getIsAConnector() throws Exception{
        return knowledgeBase.getIsSubclassOfConnector();
    }
    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#getIsInstanceOfConnector()
	 */
    public Connector getIsInstanceOfConnector() throws Exception{
        return knowledgeBase.getIsInstanceOfConnector();
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#newConcept(java.lang.String)
	 */
    public Concept newConcept(String conceptName) throws Exception {
        
        Concept concept = new Concept(conceptName);
        concept = knowledgeBase.createConcept(concept);
        System.out.println("KB:Creating new subconcept " + conceptName + " with id " + concept.getId());
        return concept;
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#newInstance(java.lang.String)
	 */
    public Instance newInstance(String instanceIdentifier) throws Exception{
        Instance instance = new Instance();
        instance.setIdentifier(instanceIdentifier);
        
        instance = knowledgeBase.createInstance(instance);
        System.out.println("KB:Creating new instance : id =" + instance.getId());
        return instance;
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#newRelation(java.lang.String, java.lang.String, java.lang.String)
	 */
    public Relation newRelation(String subConceptName,String connectorId,String conceptId) throws Exception {
        System.out.println("KB:Creating new relation (" + subConceptName  + " " + connectorId + " " +  conceptId+")");
        Connector connector = knowledgeBase.getConnectorFromId(connectorId);
        return newRelation(subConceptName,connector,conceptId);
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#newRelation(java.lang.String, org.neodatis.knowledger.core.implementation.entity.Connector, java.lang.String)
	 */
    public Relation newRelation(String subConceptName,Connector connector,String conceptId) throws Exception{
        System.out.println("KB:Creating new relation (" + subConceptName  + " " + connector.getIdentifier() + " " +  conceptId+")");
        Concept lConcept = new Concept(knowledgeBase,subConceptName);
        Concept rConcept = knowledgeBase.getConceptFromId(conceptId);
        Relation r = knowledgeBase.createRelation(lConcept,connector,rConcept);
        return r;
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#newRelation(org.neodatis.knowledger.core.implementation.entity.Entity, org.neodatis.knowledger.core.implementation.entity.Connector, org.neodatis.knowledger.core.implementation.entity.Entity)
	 */
    public Relation newRelation(Entity entity1,Connector connector,Entity entity2) throws Exception{
        System.out.println("KB:Creating new relation (" + entity1.getIdentifier()  + " " + connector.getIdentifier() + " " +  entity2.getIdentifier()+")");
        Relation r = knowledgeBase.createRelation(entity1,connector,entity2);
        return r;
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#newConnector(java.lang.String)
	 */
    public Connector newConnector(String connectorName) throws Exception {
        System.out.println("KB:Creating new connector " + connectorName);
        Connector connector = knowledgeBase.createConnector(connectorName,knowledgeBase.getRootConnector());
        return connector;
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#updateConceptName(java.lang.String, java.lang.String)
	 */
    public void updateConceptName(String newName, String conceptId) throws Exception {
        System.out.println("KB:Updating concept " + conceptId  + " with " + newName);
        Concept concept = knowledgeBase.getConceptFromId(conceptId);
        concept.setIdentifier(newName);
        knowledgeBase.updateConcept(concept);
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#updateInstanceIdentifier(java.lang.String, java.lang.String)
	 */
    public void updateInstanceIdentifier(String newName, String instanceId) throws Exception {
        System.out.println("KB:Updating instance " + instanceId  + " with identifier " + newName);
        Instance instance = knowledgeBase.getInstanceFromId(instanceId);
        instance.setIdentifier(newName);
        knowledgeBase.updateInstance(instance);

    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#updateConnectorName(java.lang.String, java.lang.String)
	 */
    public void updateConnectorName(String newName, String id) throws Exception {
        System.out.println("KB:Updating connector " + id  + " with " + newName);
        Connector connector = knowledgeBase.getConnectorFromId(id);
        connector.setIdentifier(newName);
        knowledgeBase.updateConnector(connector);
        
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#deleteConcept(java.lang.String)
	 */
    public void deleteConcept(String id) throws Exception {
        System.out.println("KB:Deleting concept " + id);
        knowledgeBase.deleteConceptWithId(id);
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#deleteInstance(java.lang.String)
	 */
    public void deleteInstance(String id) throws Exception {
        System.out.println("KB:Deleting instance " + id);
        knowledgeBase.deleteInstanceWithId(id);
    }

    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#deleteRelation(java.lang.String)
	 */
    public void deleteRelation(String id) throws Exception {
        System.out.println("KB:Deleting relation " + id);    
        knowledgeBase.deleteRelationWithId(id);
    }
    
    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#getConceptById(java.lang.String)
	 */
    public Concept getConceptById(String id) throws Exception{
        return knowledgeBase.getConceptFromId(id);
    }
    public Instance getInstanceById(String id) throws Exception{
        return knowledgeBase.getInstanceFromId(id);
    }
    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#getEntityById(java.lang.String)
	 */
    public Entity getEntityById(String id) throws Exception{
        return (Entity) knowledgeBase.getObjectFromId(id);
    }
    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#getRelationById(java.lang.String)
	 */
    public Relation getRelationById(String id) throws Exception{
        return knowledgeBase.getRelationFromId(id);
    }
    
    /* (non-Javadoc)
	 * @see org.neodatis.knowledger.graph.core.IPersistentManager#updateConnectorOfRelation(java.lang.String, java.lang.String)
	 */
    public void updateConnectorOfRelation(String relationId,String newConnectorName) throws Exception{
        Relation relation = knowledgeBase.getRelationFromId(relationId);
        Connector connector = knowledgeBase.createConnector(newConnectorName,knowledgeBase.getIsAttributeConnector());
        relation.setConnector(connector);
        knowledgeBase.updateRelation(relation);
        
    }
    
    public Object getKnowledgeBase(){
    	return knowledgeBase;
    }
	public String getKnowledgeBaseName() {
		return knowledgeBase.getId();
	}
	public void close() throws IOException {
		knowledgeBase.close();
		
	}
	public Concept getConceptFromName(String name) throws Exception {
		return knowledgeBase.getConceptFromName(name);
	}
	public Concept getConceptFromName(String name,GetMode getMode) throws Exception {
		return knowledgeBase.getConceptFromName(name,getMode);
	}
	public Instance getInstanceFromName(String name,GetMode getMode) throws Exception {
		return knowledgeBase.getInstanceFromValue(name,getMode);
	}

	public Connector getConnectorFromName(String name) throws Exception {
		return knowledgeBase.getConnectorFromName(name,GetMode.CREATE_IF_DOES_NOT_EXIST);
	}
	public Connector getConnectorFromName(String name,GetMode getMode) throws Exception {
		return knowledgeBase.getConnectorFromName(name,getMode);
	}
    /**
     * @return the filter
     */
    public IFilter getFilter() {
        return filter;
    }
    /**
     * @param filter the filter to set
     */
    public void setFilter(IFilter filter) {
        this.filter = filter;
    }
	public Instance getInstanceFromValue(String value) throws Exception {
		return knowledgeBase.getInstanceFromValue(value, GetMode.CREATE_IF_DOES_NOT_EXIST);
	}
	public RelationList queryRelations(Entity leftEntity, Connector connector, Entity rightEntity) {
		return knowledgeBase.queryRelations(leftEntity, connector, rightEntity);
	}
    
    
}
