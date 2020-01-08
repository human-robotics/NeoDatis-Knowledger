/* 
 * $RCSfile: KnowledgeBase.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.core.implementation.knowledgebase;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

import org.neodatis.knowledger.core.CanNotDeleteObjectWithReferencesException;
import org.neodatis.knowledger.core.ObjectDoesNotExistException;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.ConceptList;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.entity.Language;
import org.neodatis.knowledger.core.implementation.entity.Proposition;
import org.neodatis.knowledger.core.implementation.entity.PropositionList;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.interfaces.entity.IConceptCriteria;
import org.neodatis.knowledger.core.interfaces.entity.IConnectorCriteria;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;
import org.neodatis.knowledger.core.interfaces.entity.IPropositionCriteria;
import org.neodatis.knowledger.core.interfaces.entity.IRelationCriteria;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;

/**
 * <p>
 * 
 * </p>
 * 
 * @todo In get...FromName use a three state mode :
 *       throwExceptionIfDoesNotexist,
 *       CreateIfDoesNotExist,ReturnNullIfDoesNotExist
 * 
 */
public interface KnowledgeBase {

	public void setDebug(boolean debug);
	// To specificy the behaviour for get from name methods
	//public enum GetMode {THROW_EXCEPTION_IF_DOES_NOT_EXIST,CREATE_IF_DOES_NOT_EXIST,RETURN_NULL_IF_DOES_NOT_EXIST};
	//public enum KnowledgeBaseType {IN_MEMORY,DATABASE,FILE};

    public static final String ROOT_OBJECT_NAME = "Object";
    
    public static final String ROOT_CONNECTOR_NAME = "is-connected-to";
    public static final String CONNECTOR_IS_INSTANCE_OF = "is-instance-of";
    public static final String CONNECTOR_IS_A = "is-a";
    public static final String CONNECTOR_IS_SUB_CLASS_OF = "is-subclass-of";
    public static final String CONNECTOR_IS_SUB_RELATION_OF = "is-subrelation-of";
    public static final String CONNECTOR_IS_INVERSE_OF = "is-inverse-of";
    public static final String CONNECTOR_IS_EQUIVALENT_TO = "is-equivalent-to";
    public static final String CONNECTOR_ATTRIBUTE = "attribute-connector";
    public static final String CONNECTOR_HAS_A = "has-a";
    public static final String CONNECTOR_HAS_A_LIST_OF = "has-a-list-of";

    
    public ConceptList getConceptList();
    public ConnectorList getConnectorList();
    public InstanceList getInstanceList();
    public PropositionList getPropositionList();
    public RelationList getRelationList();
    
    public EntityList getAllEntities();

    public void deleteObject(KnowledgerObject object) throws Exception;
    public EntityList getEntities(ICriteria criteria);


    public String getName();
    public KnowledgeBaseType getType();
    
    public KnowledgerObject create(KnowledgerObject object) throws Exception;
    public KnowledgerObject update(KnowledgerObject object) throws Exception;
    public KnowledgerObject delete(KnowledgerObject object) throws Exception;
    
	//Concept
	Concept createConcept(Concept concept) throws Exception ;
	public Concept updateConcept(Concept concept) throws Exception;
	public Concept deleteConcept(Concept concept) throws Exception;
	public void deleteConcepts(ConceptList conceptList)  throws CanNotDeleteObjectWithReferencesException, Exception;
	public boolean existConcept(String conceptName,boolean exactSearch);
	public ConceptList getConcepts(IConceptCriteria conceptCriteria);
	public Concept getConceptFromName(String name,GetMode mode) throws Exception;
	public Concept getConceptFromId(String id) throws ObjectDoesNotExistException;


	//Instance
	//public Instance createInstance(Instance instance) throws Exception;
	//public Instance getInstanceOf(String instanceName,String value,Concept instanceConcept) throws Exception;
	//public Instance getInstanceOf(String instanceName,String value,String instanceConceptName) throws Exception;
	//public Instance createInstanceOf(Instance instance,Concept concept) throws Exception;
	//public IInstanceList createInstancesOf(IInstanceList instances, Concept concept) throws Exception;
		
	public Instance updateInstance(Instance instance) throws Exception;
	public Instance deleteInstance(Instance instance) throws Exception;
	public void deleteInstances(InstanceList instanceList ) throws CanNotDeleteObjectWithReferencesException, Exception;
	public Instance deleteInstanceWithId(String id) throws CanNotDeleteObjectWithReferencesException, Exception;
	
	//public IInstanceList getInstancesOf(Concept concept, int depth) throws ObjectDoesNotExistException, Exception;
	//public boolean isInstanceOf(Instance instance,Concept concept, int depth) throws ObjectDoesNotExistException, Exception;
	public Instance getInstanceFromValue(String value, GetMode mode) throws ObjectDoesNotExistException, Exception;
	public Instance getInstanceFromId(String id) throws ObjectDoesNotExistException;
    //public Concept getInstanceConcept(Instance instance) throws Exception;


	//Proposition
	public Proposition createProposition(Proposition proposition) throws Exception;
	public Proposition updateProposition(Proposition proposition) throws Exception;
	public Proposition deleteProposition(Proposition proposition) throws Exception;
	public void deletePropositions(PropositionList propositionList) throws CanNotDeleteObjectWithReferencesException, Exception ;
	public Proposition deletePropositionWithId(String id)  throws CanNotDeleteObjectWithReferencesException, Exception;
	public boolean existProposition(String propositionIdentifier,boolean exactSearch);
	
	public PropositionList getPropositions(IPropositionCriteria propositionCriteria);
	public Proposition getPropositionFromName(String name) throws ObjectDoesNotExistException;
	public Proposition getPropositionFromName(String name,GetMode mode) throws ObjectDoesNotExistException;
	public Proposition getPropositionFromId(String id) throws ObjectDoesNotExistException;
	

	//Connector
	public Connector createConnector(Connector connector) throws Exception;
	public Connector createConnector(String connectorName) throws Exception;
	public Connector createConnector(String connectorName,Connector superConnector) throws Exception;
	public Connector createConnector(Connector connector,Connector superConnector) throws Exception;
	public Connector updateConnector(Connector connector) throws Exception;
	public Connector deleteConnector(Connector connector) throws Exception;
	public void deleteConnectors(ConnectorList connectorList) throws CanNotDeleteObjectWithReferencesException, Exception;
	public Connector deleteConnectorWithId(String id) throws CanNotDeleteObjectWithReferencesException, Exception;
	public boolean existConnector(String connectorName,boolean exactSearch);
	
	public ConnectorList getConnectors(IConnectorCriteria connectorCriteria);
	public Connector getConnectorFromName(String name,GetMode mode) throws Exception;
	/**
	 * @param name The name of the connector to return
	 * @param mode The behavior in case it does not exist
	 * @param defaultSuperConnector the super connector of the connector in case of having to create it
	 * */
	public Connector getConnectorFromName(String name,GetMode mode,Connector defaultSuperConnector) throws Exception;
	public Connector getConnectorFromId(String id) throws ObjectDoesNotExistException;
	public ConnectorList getSubConnectorsOf(Connector connector,int depth) throws ObjectDoesNotExistException, Exception;
	public ConnectorList getSuperConnectorsOf(Connector connector,int depth) throws Exception;
	
	// Relation
	public Relation createRelation(Relation relation) throws Exception;
	public Relation createRelation(Entity leftentity,Connector connector,Entity rightEntity) throws Exception;	
	public Relation updateRelation(Relation relation) throws Exception;
	public Relation deleteRelation(Relation relation) throws Exception ;
	public void deleteRelations(RelationList relationList) throws CanNotDeleteObjectWithReferencesException, Exception;
	public Relation deleteRelationWithId(String id) throws CanNotDeleteObjectWithReferencesException, Exception;
	public Relation getRelationFromId(String id) throws ObjectDoesNotExistException;
	public RelationList getRelationList(IRelationCriteria relationCriteria);
	public RelationList getRelationsOf(KnowledgerObject object);
	
    public EntityList query(String dqlQuery) throws Exception;
    public EntityList query(ICriteria criteria);
    public InstanceList queryInstances(ICriteria criteria);
    public ConceptList queryConcepts(ICriteria criteria);
	public RelationList queryRelations(Entity leftEntity, Connector connector, Entity rightEntity);
	public EntityList getEntitiesByName(String name);
	public EntityList getLostEntities();
	
	public void addObserver(Observer observer);
	
	public Concept getRootObject() throws Exception;
	public Connector getRootConnector() throws Exception;
	
    public Connector getIsInstanceOfConnector() throws Exception;
    public Connector getIsSubclassOfConnector() throws Exception;
    public Connector getIsAConnector() throws Exception;
    public Connector getIsSubRelationOfConnector() throws Exception;
    public Connector getIsAttributeConnector() throws Exception;
    public Connector getHasAConnector() throws Exception;
    public Connector getHasAListOfConnector() throws Exception;
    public Connector getIsInverseOfConnector() throws Exception;
    public Connector getIsEquivalentToConnector() throws Exception;
    
    
	public Instance createInstance(Instance instance) throws Exception;
	public ConceptList getSuperConceptsOf(Concept conceptFromName, int depth) throws Exception;
	
	public Language getLanguage();
    public void setLanguage(Language language);
    public void close() throws Exception;

	public abstract String getId();
	
    public abstract List getInitialConceptList() throws Exception;
    public abstract List getInitialInstanceList()  throws Exception;
    public abstract List getInitialConnectorList()  throws Exception;
    public abstract List getInitialRelationList()  throws Exception;
    public abstract List getInitialPropositionList()  throws Exception;


}