package org.neodatis.knowledger.gui.graph;

import java.io.IOException;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.ConceptList;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.ConnectorList;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;



/** The interface used to persist data
 * 
 * @author olivier s
 *
 */
public interface IPersistentManager {

	RelationList getRelations() throws Exception;

	ConceptList getConcepts();

	InstanceList getInstances();
	
	public InstanceList getInstancesOf(Concept concept,boolean direct) throws Exception;
	
	ConnectorList getConnectors();

	Connector getIsAConnector() throws Exception;

	Connector getIsInstanceOfConnector() throws Exception;

	Connector getConnectorFromName(String name) throws Exception;
	Connector getConnectorFromName(String name,GetMode getMode) throws Exception;
	
	Concept newConcept(String conceptName) throws Exception;

	Instance newInstance(String instanceIdentifier) throws Exception;

	Relation newRelation(String subConceptName, String connectorId, String conceptId) throws Exception;

	Relation newRelation(String subConceptName, Connector connector, String conceptId) throws Exception;

	Relation newRelation(Entity entity1, Connector connector, Entity entity2) throws Exception;

	Connector newConnector(String connectorName) throws Exception;

	void updateConceptName(String newName, String conceptId) throws Exception;

	void updateInstanceIdentifier(String newName, String instanceId) throws Exception;

	void updateConnectorName(String newName, String id) throws Exception;

	void deleteConcept(String id) throws Exception;

	void deleteInstance(String id) throws Exception;

	void deleteRelation(String id) throws Exception;

	Concept getConceptById(String id) throws Exception;
	Concept getConceptFromName(String name) throws Exception;
	Concept getConceptFromName(String name,GetMode getMode) throws Exception;
	Instance getInstanceById(String id) throws Exception;

	Entity getEntityById(String id) throws Exception;

	Relation getRelationById(String id) throws Exception;

	void updateConnectorOfRelation(String relationId, String newConnectorName) throws Exception;
	
    Object getKnowledgeBase();
    String getKnowledgeBaseName();
    void close() throws IOException;
    public void setFilter(IFilter filter);
    Instance getInstanceFromValue(String value) throws Exception;
    public RelationList queryRelations(Entity leftEntity, Connector connector, Entity rightEntity);
}