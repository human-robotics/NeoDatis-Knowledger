package org.neodatis.knowledger.core.implementation.entity;

import java.util.Iterator;
import java.util.List;

import org.neodatis.knowledger.core.implementation.criteria.SimpleConceptCriteria;
import org.neodatis.knowledger.core.implementation.criteria.SimpleInstanceCriteria;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.entity.IConceptCriteria;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;
import org.neodatis.knowledger.core.interfaces.entity.IInstanceCriteria;
import org.neodatis.tool.DLogger;


/**
 */
public class Concept extends Entity{

	protected transient InstanceList instanceList;
	
	public Concept() {
		super(null);
		instanceList = new InstanceList();
	}

	public Concept(String name) {
		this(null, name);
	}

	public Concept(KnowledgeBase knowledgeBase, String name) {
		super(knowledgeBase,name);
		instanceList = new InstanceList();
	}

	public String getName(){
		return getIdentifier();
	}
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getIdentifier());
		return buffer.toString();
	}

	public Instance newInstance() throws Exception{
		Instance instance = getKnowledgeBase().createInstance(new Instance(this));
		instance.connectTo(getKnowledgeBase().getIsInstanceOfConnector(), this);
		addInstanceInCache(instance);
		return instance;		
	}
	public Instance newInstance(String value) throws Exception{
		Instance instance = getKnowledgeBase().createInstance(new Instance(getKnowledgeBase(),value));
		instance.connectTo(getKnowledgeBase().getIsInstanceOfConnector(), this);
		addInstanceInCache(instance);
		return instance;		
	}
	
	public InstanceList getInstances(ICriteria criteria) throws Exception {
		return getInstances().getInstances(criteria);
		
	}

	public InstanceList getInstances(IInstanceCriteria instanceCriteria,int depth) throws Exception {
		InstanceList instances = getInstances(depth);
		return instances.getInstances(instanceCriteria);
	}

	
	public InstanceList getDirectInstances() throws Exception {
		//return getInstances(-1);
		return instanceList;
	}

	public InstanceList getInstances() throws Exception {
		return getInstances(0);
	}
	public InstanceList getInstances(int depth) throws Exception {
		RelationList relationList = null;
		InstanceList instances = new InstanceList();
		ConceptList conceptList = getSubConcepts(depth);

		Connector instanceOfConnector = getKnowledgeBase().getIsInstanceOfConnector();

		// Instances of THE concept
		relationList = getKnowledgeBase().queryRelations(null, instanceOfConnector, this);
		instances.addAll(relationList.getLeftEntities());

		Iterator iterator = conceptList.iterator();
		Concept concept = null;
		while (iterator.hasNext()) {
			concept = (Concept) iterator.next();
			relationList = getKnowledgeBase().queryRelations(null, instanceOfConnector, concept);
			instances.addAll(relationList.getLeftEntities());
		}

		return instances;
	}

	public Concept getSubConcept(String name) throws Exception {
		return getSubConcept(name,0);
	}
	
	public Concept getSubConcept(String name,int depth) throws Exception {
		Concept concept = null;
		ConceptList conceptList = getSubConcepts(new SimpleConceptCriteria(name),depth);

		if (conceptList.isEmpty()) {
			concept = getKnowledgeBase().createConcept(new Concept(getKnowledgeBase(),name));
			concept.connectTo(getKnowledgeBase().getIsSubclassOfConnector(), this);
		} else {
			if (conceptList.size() > 1) {
				DLogger.error("concept " + name + " is duplicated");
			}
			concept = conceptList.getConcept(0);
		}
		return concept;
	}

	public ConceptList getSubConcepts(IConceptCriteria conceptCriteria,int depth) throws Exception {
		ConceptList conceptList = getSubConcepts(depth );
		return conceptList.getConcepts(conceptCriteria);
	}

	public ConceptList getSubConcepts() throws Exception {
		return getSubConcepts(0);
	}
	public ConceptList getSubConcepts(int depth) throws Exception {

		if (depth < 0) {
			return new ConceptList();
		}

		RelationList relationList = getKnowledgeBase().queryRelations(null, getKnowledgeBase().getIsSubclassOfConnector(), this);
		Relation relation = null;
		ConceptList conceptList = new ConceptList();
		Concept thisConcept = null;

		for (int index = 0; index < relationList.size(); index++) {
			relation = relationList.getRelation(index);
			if (relation.getLeftEntity().getClass() == Concept.class) {
				thisConcept = (Concept) relation.getLeftEntity();
				conceptList.add(thisConcept);
				conceptList.addAll(getSubConcepts(depth - 1));
			}
		}
		return conceptList;
	}

	public Concept update() throws Exception {
		return getKnowledgeBase().updateConcept(this);
	}

	public Concept delete() throws Exception {
		return getKnowledgeBase().deleteConcept(this);
	}

	public boolean existInstance(String value) throws Exception {
		return !getInstances(new SimpleInstanceCriteria(value),-1).isEmpty();
	}
	public boolean existInstance(ICriteria criteria) throws Exception{
		return !getInstances().getInstances(criteria).isEmpty();
		
	}

	public InstanceList createInstances(InstanceList instances) throws Exception {
		Iterator  iterator = instances.iterator();
		InstanceList result = new InstanceList();
		Instance instance = null;
		while(iterator.hasNext()){
			instance = (Instance) iterator.next();
			getKnowledgeBase().createInstance(instance);
			result.add(instance);
		}
		return result;
		
	}

	public ConceptList getSuperConcepts(int depth) throws Exception {
		if (depth == 0) {
			return new ConceptList();
		}

		RelationList relationList = getKnowledgeBase().queryRelations(this, getKnowledgeBase().getIsSubclassOfConnector(), null);
		Relation relation = null;
		ConceptList conceptList = new ConceptList();
		Concept thisConcept = null;

		for (int index = 0; index < relationList.size(); index++) {
			relation = relationList.getRelation(index);
			if (relation.getRightEntity().getClass() == Concept.class) {
				thisConcept = (Concept) relation.getLeftEntity();
				conceptList.add(thisConcept);
				conceptList.addAll(getSuperConcepts(depth - 1));
			}
		}
		return conceptList;
	}

	public Concept setInstanceList(List instances) {
		instanceList.addAll(instances);
		return this;
	}

	public void addInstanceInCache(Instance instance) {
		instanceList.add(instance);
	}

}
