/* 
 * $RCSfile: MockPersistentManager.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.neodatis.knowledger.gui.EntityWrapper;



/**
 * For test purpose. Replacing a real persistent manager
 * @author olivier s
 *
 */
public class MockPersistentManager implements IPersistentManager {
    private static long nextId = 1;
    public Concept newConcept(String conceptName) {
        System.out.println("Creating new subconcept " + conceptName);
        return null;
    }

    public String newInstanceOf(String instanceIdentifier, String conceptId) {
        System.out.println("Creating new instance of " + instanceIdentifier  + " " + conceptId);
        return getNextId();
    }

    public Relation newRelation(String subConceptName,String connectorId,String conceptId){
        System.out.println("Creating new relation (" + subConceptName  + " " + connectorId + " " +  conceptId+")");
        return null;
    }

    public Relation newRelation(String subConceptName,Connector connector,String conceptId){
        System.out.println("Creating new relation (" + subConceptName  + " " + connector.getIdentifier() + " " +  conceptId+")");
        return null;
    }

    public Connector newConnector(String connectorName) {
        System.out.println("Creating new connector " + connectorName);
        return null;
    }

    public void updateConceptName(String newName, String conceptId) {
        System.out.println("Updating concept " + conceptId  + " with " + newName);

    }

    public void updateInstanceIdentifier(String newName, String instanceId) {
        System.out.println("Updating instance " + instanceId  + " with identifier " + newName);

    }

    public void updateConnectorName(String newName, String id) {
        System.out.println("Updating connector " + id  + " with " + newName);
    }

    public void deleteConcept(String id) {
        System.out.println("Deleting concept " + id);
    }

    public void deleteInstance(String id) {
        System.out.println("Deleting instance " + id);
    }

    public void deleteRelation(String id) {
        System.out.println("Deleting relation " + id);        
    }

    private String getNextId(){
        return ""+nextId++;
    }
    
    public RelationList getRelations(){
        Connector isa = new Connector("é um");
        isa.setId("1");
        
        Concept living = new Concept("Ser Vivo");
        living.setId("2");
        Concept animal = new Concept("Animal");
        animal.setId("3");
        Concept human = new Concept("Humano");
        human.setId("4");
        Concept man = new Concept("Homem");
        man.setId("5");
        Concept woman = new Concept("Mulher");
        woman.setId("6");
        
        RelationList list = new RelationList();
        Relation relation = new Relation(woman,isa,human);
        relation.setId("7");
        list.add(relation);
        
        relation = new Relation(man,isa,human);
        relation.setId("8");
        list.add(relation);
        
        relation = new Relation(human,isa,animal);
        relation.setId("9");
        list.add(relation);
        
        relation = new Relation(animal,isa,living);
        relation.setId("10");
        list.add(relation);

        return list;
    }

    public ConnectorList getConnectors(){
        ConnectorList list = new ConnectorList();
        Connector isAConnector = new Connector("é um");
        isAConnector.setId("100");
        Connector isInstanceOfConnector = new Connector("é instância de");
        isAConnector.setId("101");
        
        list.add(isAConnector);
        list.add(isInstanceOfConnector);
        
        return list;
        
    }
    public List getConnectorWrappers(){
        Connector connector = null;
        EntityWrapper wrapper = null;
        // TODO : build it once only!
        List result = new ArrayList();
        ConnectorList list = getConnectors();
        for(int i=0;i<list.size();i++){
            connector = list.getConnector(i);
            wrapper = new EntityWrapper(connector);
            result.add(wrapper);            
        }
        return result;
        
    }

    public Instance newInstance(String instanceIdentifier) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public Relation newRelation(Entity entity1, Connector connector, Entity entity2) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public Concept getConceptById(String id) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public Connector getIsAConnector() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public Connector getIsInstanceOfConnector() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public Entity getEntityById(String id) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public Relation getRelationById(String id) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public void updateConnectorOfRelation(String relationId, String newConnectorName) throws Exception {
        // TODO Auto-generated method stub
        
    }

	public ConceptList getConcepts() {
		// TODO Auto-generated method stub
		return null;
	}

	public InstanceList getInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	public InstanceList getInstancesOf(Concept concept, boolean direct) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getKnowledgeBase(){
		return null;
	}
	public String getKnowledgeBaseName() {
		return "mock-base";
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public Instance getInstanceById(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Connector getConnectorFromName(String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Connector getConnectorFromName(String name, GetMode getMode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Concept getConceptFromName(String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Concept getConceptFromName(String name, GetMode getMode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

    public void setFilter(IFilter filter) {
        // TODO Auto-generated method stub
        
    }

	public Instance getInstanceFromValue(String value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public RelationList queryRelations(Entity leftEntity, Connector connector, Entity rightEntity) {
		// TODO Auto-generated method stub
		return null;
	}

}
