package org.neodatis.knowledger.core.implementation.knowledgebase;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.neodatis.knowledger.core.CanNotDeleteObjectWithReferencesException;
import org.neodatis.knowledger.core.ObjectDoesNotExistException;
import org.neodatis.knowledger.core.factory.KnowledgeBaseDescription;
import org.neodatis.knowledger.core.factory.RelationCriteriaFactory;
import org.neodatis.knowledger.core.factory.RelationFactory;
import org.neodatis.knowledger.core.implementation.criteria.RelationCriteria;
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
import org.neodatis.knowledger.core.implementation.query.RelationQuery;
import org.neodatis.knowledger.core.interfaces.entity.IConceptCriteria;
import org.neodatis.knowledger.core.interfaces.entity.IConnectorCriteria;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;
import org.neodatis.knowledger.core.interfaces.entity.IInstanceCriteria;
import org.neodatis.knowledger.core.interfaces.entity.IPropositionCriteria;
import org.neodatis.knowledger.core.interfaces.entity.IRelationCriteria;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.ExternalRepository;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.knowledger.gui.tool.Messages;
import org.neodatis.knowledger.parser.query.KnowledgerQueryParser;
import org.neodatis.knowledger.parser.query.QueryLexer;
import org.neodatis.tool.DLogger;

public class KnowledgeBaseImpl  implements KnowledgeBase{
	transient protected static Logger logger = Logger.getLogger(KnowledgeBaseImpl.class);

	protected transient ConceptList conceptList;

	protected transient InstanceList instanceList;

	protected transient ConnectorList connectorList;

	protected transient PropositionList propositionList;

	protected transient RelationList relationList;

	protected Language language;
	
	protected transient boolean debug;

	// private IUserList userList;
    public static final Language defaultLanguage = new Language("default","1");

	/** contains the name, the type and the id () of the knowledge base */
	protected KnowledgeBaseDescription knowledgeBaseDescription;

	/**
	 * To be able to reflect all Knowledge base change in another repository
	 * like a database, a file, etc
	 */
	transient protected ExternalRepository externalRepository;

	protected KnowledgeBaseImpl() throws Exception {
		debug = false;
	}

	protected KnowledgeBaseImpl(String name, KnowledgeBaseType type) throws Exception {
		init(name, type,null);
	}

	/**
	 * 
	 * @param name
	 *            The name of the knowledge base
	 * @param type
	 *            The type of the knowledge base - DbPersistent, FilePersistent,
	 *            InMemory
	 * @throws Exception
	 */
	protected void init(String name, KnowledgeBaseType type,ExternalRepository externalRepository) throws Exception {
		if(knowledgeBaseDescription==null){
		    this.knowledgeBaseDescription = new KnowledgeBaseDescription(name,type);
        }
		this.externalRepository = externalRepository;

		knowledgeBaseDescription.setName(name);
		knowledgeBaseDescription.setType(type);
		setLanguage(new Language("default","1"));

		init();
	}

	protected void init() throws Exception {
		debug = false;
		if(knowledgeBaseDescription==null){
		    this.knowledgeBaseDescription = new KnowledgeBaseDescription();
        }
        
        conceptList = new ConceptList();
        conceptList.addAll(getInitialConceptList());
        
        instanceList = new InstanceList();
        instanceList.addAll(getInitialInstanceList());
        
        connectorList = new ConnectorList();
        connectorList.addAll(getInitialConnectorList());
        
        relationList = new RelationList();
        relationList.addAll(getInitialRelationList());
        
        propositionList = new PropositionList();
        propositionList.addAll(getInitialPropositionList());
        
        // to be sure that root object and root connector will exist
        /*
        getRootObject();
        getRootConnector();
        getIsAConnector();
        getIsAttributeConnector();
        getIsEquivalentToConnector();
        getIsInstanceOfConnector();
        getIsInverseOfConnector();
        getIsSubclassOfConnector();
        getIsSubRelationOfConnector();
        */
	}

	public static void initTransient() {
		if (logger == null) {
			logger = Logger.getLogger(KnowledgeBase.class);
		}
	}

	public void close(){
		if(externalRepository!=null){
			externalRepository.close();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Knowledgebase ").append(getName()).append(":\n");
		buffer.append("Concept List = ").append(conceptList).append("\n");
		buffer.append("Instance List = ").append(instanceList).append("\n");
		buffer.append("Connector List = ").append(connectorList).append("\n");
		buffer.append("Relation List = ").append(relationList).append("\n");
		buffer.append("Proposition List = ").append(propositionList).append("\n");
		return buffer.toString();
	}

	public Concept addConcept(Concept concept) {
		concept.setKnowledgeBase(this);
		conceptList.add(concept);
		notifyKnowledgeBaseChange();
		return concept;
	}

	public Instance addInstance(Instance instance) {
		instance.setKnowledgeBase(this);
		instanceList.add(instance);
		notifyKnowledgeBaseChange();
		return instance;
	}

	public Connector addConnector(Connector connector) {
		connector.setKnowledgeBase(this);
		connectorList.add(connector);
		notifyKnowledgeBaseChange();
		return connector;
	}

	public Relation addRelation(Relation relation) {
		relation.setKnowledgeBase(this);
		relationList.add(relation);
		notifyKnowledgeBaseChange();
		return relation;
	}

	public Proposition addProposition(Proposition proposition) {
		proposition.setKnowledgeBase(this);
		propositionList.add(proposition);
		notifyKnowledgeBaseChange();
		return proposition;
	}

	public void removeConcept(Concept concept) {
		conceptList.remove(concept);
		notifyKnowledgeBaseChange();
	}

	public void removeInstance(Instance instance) {
		instanceList.remove(instance);
		notifyKnowledgeBaseChange();
	}

	public void removeConnector(Connector connector) {
		connectorList.remove(connector);
		notifyKnowledgeBaseChange();
	}

	public void removeRelations(RelationList relations) {
		relationList.removeAll(relations);
		notifyKnowledgeBaseChange();
	}

	public void removeRelation(Relation relation) {
		relationList.remove(relation);
		notifyKnowledgeBaseChange();
	}

	public void removeProposition(Proposition proposition) {
		propositionList.remove(proposition);
		notifyKnowledgeBaseChange();
	}

	public EntityList getEntities(ICriteria criteria) {

		EntityList resultList = new EntityList();
		if (criteria == null) {
			return resultList;
		}
		resultList.addAll(getConceptList());
		resultList.addAll(getInstanceList());
		resultList.addAll(getConnectorList());

		return resultList;
	}

	public void deleteObject(KnowledgerObject object) throws Exception {
		deleteObject(object.getId(), true);
	}

	/**
	 * Delete one entity
	 * 
	 * @param entityId
	 * @param deleteRelationsToo
	 * @throws Exception
	 */
	public void deleteObject(String objectId, boolean deleteRelationsToo) throws Exception {
		KnowledgerObject object = getObjectFromId(objectId);

		// First check if entity has any references
		RelationList relationList = getRelationsOf(object);

		if (!relationList.isEmpty() && !deleteRelationsToo) {
			throw new CanNotDeleteObjectWithReferencesException("Concept - " + object.getIdentifier(), objectId, relationList);
		}

		if (externalRepository != null) {
			externalRepository.delete(relationList);
			externalRepository.delete(object);
		}
		getRelationList().removeAll(relationList);
		getConceptList().remove(object);
		getInstanceList().remove(object);
		getRelationList().remove(object);

		// advise others that object has changed
		// myNotyfyObservers();

	}

	public Concept createConcept(Concept concept) throws Exception {
		// @TODO Check getMode to see if object must be created
		concept.setKnowledgeBase(this);
		if (externalRepository != null) {
			externalRepository.create(concept);
		}
		addConcept(concept);
		return concept;
	}

	public Concept getConcept(String conceptName) throws Exception {
		// @TODO Check getMode to see if object must be created
		// Concept concept = new Concept(this,conceptName);
		// return createConcept(concept);
		return getConceptFromName(conceptName, GetMode.CREATE_IF_DOES_NOT_EXIST);
	}

	public Concept createConceptAsSubConceptOf(Concept concept, Concept superConcept) throws Exception {
		if (externalRepository != null) {
			externalRepository.create(concept);
		}
		addConcept(concept);

		Relation relation = new Relation(this, concept, getIsSubclassOfConnector(), superConcept);
		if (externalRepository != null) {
			externalRepository.create(relation);
		}
		addRelation(relation);
		return concept;

	}

	public Concept createConceptAsSubConceptOf(String conceptName, Concept superConcept) throws Exception {
		Concept concept = new Concept(this, conceptName);
		return createConceptAsSubConceptOf(concept, superConcept);
	}

	public Concept createConceptAsSubConceptOf(String conceptName, String superConceptName) throws Exception {
		Concept concept = new Concept(this, conceptName);
		Concept superConcept = new Concept(this, superConceptName);
		return createConceptAsSubConceptOf(concept, superConcept);
	}

	public Concept updateConcept(Concept concept) throws Exception {
		if (externalRepository != null) {
			externalRepository.update(concept);
		}
		return concept;
	}

	public Concept deleteConcept(Concept concept) throws Exception {
		RelationList rlist1 = queryRelations(concept,null,null);
		rlist1.addAll(queryRelations(null,null,concept));
		logger.info("Deleting relations " + rlist1 + " before deleting " + concept);
		if (externalRepository != null) {
			externalRepository.delete(rlist1);
			externalRepository.delete(concept);
		}
		removeRelations(rlist1);
		removeConcept(concept);		
		return concept;
	}

	public void deleteConcepts(ConceptList conceptList) throws Exception {
		if (externalRepository != null) {
			externalRepository.delete(conceptList);
		}
		getConceptList().removeAll(conceptList);
	}

	public Concept deleteConceptWithId(String id) throws Exception {
		Concept concept = getConceptFromId(id);
		return deleteConcept(concept);
	}

	public boolean existConcept(String conceptName, boolean exactSearch) {
		try {
			return getConceptFromName(conceptName, GetMode.RETURN_NULL_IF_DOES_NOT_EXIST) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public ConceptList getConcepts(IConceptCriteria conceptCriteria) {
		// TODO Use criteria
		return getConceptList();
	}

	public Concept getConceptFromName(String name) throws Exception {
		return getConceptFromName(name, GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
	}

	public Concept getConceptFromName(String name, GetMode mode) throws Exception {
		Concept concept = null;
		for (int i = 0; i < getConceptList().size(); i++) {
			concept = getConceptList().getConcept(i);
			if (concept.getIdentifier() != null && concept.getIdentifier().equals(name)) {
				return concept;
			}
		}
		if (mode == GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST) {
			throw new ObjectDoesNotExistException(name);
		}
		if (mode == GetMode.RETURN_NULL_IF_DOES_NOT_EXIST) {
			return null;
		}

		return createConcept(new Concept(this, name));
	}

	public Concept getConceptFromId(String id) throws ObjectDoesNotExistException {
		Concept concept = null;
		for (int i = 0; i < getConceptList().size(); i++) {
			concept = getConceptList().getConcept(i);
			if (concept.getId().equals(id)) {
				return concept;
			}
		}
		throw new ObjectDoesNotExistException(id);
	}

	public ConceptList getSubConceptsOf(Concept concept, int depth) throws Exception {
		if (depth == 0) {
			return new ConceptList();
		}

		RelationList relationList = queryRelations(null, getIsSubclassOfConnector(), concept);
		Relation relation = null;
		ConceptList conceptList = new ConceptList();
		Concept thisConcept = null;
		for (int index = 0; index < relationList.size(); index++) {
			relation = relationList.getRelation(index);
			if (relation.getLeftEntity().getClass() == Concept.class) {
				thisConcept = (Concept) relation.getLeftEntity();
				conceptList.add(thisConcept);
				conceptList.addAll(getSubConceptsOf(thisConcept, depth - 1));
			}
		}
		return conceptList;
	}

	public ConceptList getSuperConceptsOf(Concept concept, int depth) throws Exception {

		if (depth == 0) {
			return new ConceptList();
		}
		RelationList relationList = queryRelations(concept, getIsSubclassOfConnector(), null);
		Relation relation = null;
		ConceptList conceptList = new ConceptList();
		Concept thisConcept = null;
		for (int index = 0; index < relationList.size(); index++) {
			relation = relationList.getRelation(index);
			if (relation.getRightEntity().getClass() == Concept.class) {
				thisConcept = (Concept) relation.getRightEntity();
				conceptList.add(thisConcept);
				conceptList.addAll(getSuperConceptsOf(thisConcept, depth - 1));
			}
		}
		return conceptList;
	}

	public Instance createInstance(Instance instance) throws Exception {
		instance.setKnowledgeBase(this);
		if (externalRepository != null) {
			externalRepository.create(instance);
		}
		addInstance(instance);
		return instance;

	}

	public Instance updateInstance(Instance instance) throws Exception {
		if (externalRepository != null) {
			externalRepository.update(instance);
		}
		return instance;
	}

	public Instance deleteInstance(Instance instance) throws Exception {
		
		RelationList rlist1 = queryRelations(instance,null,null);
		rlist1.addAll(queryRelations(null,null,instance));
		logger.info("Deleting relations " + rlist1 + " before deleting " + instance);
		
		if (externalRepository != null) {
			externalRepository.delete(rlist1);
			externalRepository.delete(instance);
		}
		removeRelations(rlist1);
		removeInstance(instance);
		return instance;
	}

	public void deleteInstances(InstanceList instanceList) throws Exception {
		if (externalRepository != null) {
			externalRepository.delete(instanceList);
		}
		getInstanceList().removeAll(instanceList);
	}

	public Instance deleteInstanceWithId(String id) throws Exception {
		Instance instance = getInstanceFromId(id);
		deleteInstance(instance);
		return instance;
	}

	public boolean existInstanceOf(String instanceIdentifier, boolean exactSearch) {
		// TODO Auto-generated method stub
		return false;
	}

	public InstanceList getInstances(IInstanceCriteria instanceCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public Concept getInstanceConcept(Instance instance) throws Exception {
		RelationList relationList = queryRelations(instance, getIsInstanceOfConnector(), null);
		if (relationList.isEmpty()) {
			throw new Exception("Instance does not have type : instance id = " + instance.getId());
		}
		if (relationList.size() > 1) {
			throw new Exception("Instance have more than one type: instance id = " + instance.getId());
		}

		return (Concept) relationList.getRelation(0).getRightEntity();

	}

	public Instance getInstanceFromValue(String value, GetMode mode) throws ObjectDoesNotExistException, Exception {
		Instance instance = null;
		for (int i = 0; i < getInstanceList().size(); i++) {
			instance = getInstanceList().getInstance(i);
			if (instance.getValue().equals(value)) {
				return instance;
			}
		}
		if (mode == GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST) {
			throw new ObjectDoesNotExistException(value, "Instance");
		}

		if (mode == GetMode.CREATE_IF_DOES_NOT_EXIST) {
			// @todo create as instance of root
			return createInstance(new Instance(value));
		}
		return null;

	}

	public Instance getInstanceFromId(String id) throws ObjectDoesNotExistException {
		Instance instance = null;
		for (int i = 0; i < getInstanceList().size(); i++) {
			instance = getInstanceList().getInstance(i);
			if (instance.getId().equals(id)) {
				return instance;
			}
		}
		throw new ObjectDoesNotExistException(id);
	}

	public Proposition createProposition(Proposition proposition) throws Exception {
		proposition.setKnowledgeBase(this);
		if (externalRepository != null) {
			externalRepository.create(proposition);
		}
		addProposition(proposition);
		return proposition;
	}

	public Proposition updateProposition(Proposition proposition) throws Exception {
		if (externalRepository != null) {
			externalRepository.update(proposition);
		}
		return proposition;
	}

	public Proposition deleteProposition(Proposition proposition) throws Exception {
		if (externalRepository != null) {
			externalRepository.delete(proposition);
		}
		removeProposition(proposition);
		return proposition;
	}

	public void deletePropositions(PropositionList propositionList) throws Exception {
		if (externalRepository != null) {
			externalRepository.delete(propositionList);
		}
		getPropositionList().removeAll(propositionList);
	}

	public Proposition deletePropositionWithId(String id) throws Exception {
		Proposition object = getPropositionFromId(id);

		if (externalRepository != null) {
			externalRepository.delete(object);
		}
		removeProposition(object);
		return object;
	}

	public boolean existProposition(String propositionIdentifier, boolean exactSearch) {
		try {
			return getPropositionFromName(propositionIdentifier, GetMode.RETURN_NULL_IF_DOES_NOT_EXIST) != null;
		} catch (Exception e) {
		}
		return false;
	}

	public PropositionList getPropositions(IPropositionCriteria propositionCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public Proposition getPropositionFromName(String name) throws ObjectDoesNotExistException {
		return getPropositionFromName(name, GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
	}

	public Proposition getPropositionFromName(String name, GetMode mode) throws ObjectDoesNotExistException {
		Entity entity = null;
		for (int i = 0; i < getPropositionList().size(); i++) {
			entity = getPropositionList().getProposition(i);
			if (entity.getIdentifier().equals(name)) {
				return (Proposition) entity;
			}
		}
		if (mode == GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST) {
			throw new ObjectDoesNotExistException(name);
		}
		// if (mode == GetMode.RETURN_NULL_IF_DOES_NOT_EXIST) {
		return null;
		// }

	}

	public Proposition getPropositionFromId(String id) throws ObjectDoesNotExistException {
		Entity entity = null;
		for (int i = 0; i < getPropositionList().size(); i++) {
			entity = getPropositionList().getProposition(i);
			if (entity.getId().equals(id)) {
				return (Proposition) entity;
			}
		}
		throw new ObjectDoesNotExistException(id);
	}

	public Connector createConnector(Connector connector) throws Exception {
		connector.setKnowledgeBase(this);
		if (externalRepository != null) {
			externalRepository.create(connector);
		}
		addConnector(connector);
		return connector;
	}

	public Connector createConnector(Connector connector, Connector superConnector) throws Exception {
		connector.setKnowledgeBase(this);
		if (externalRepository != null) {
			externalRepository.create(connector);
		}
		addConnector(connector);

		if (superConnector != null) {
			Relation relation = RelationFactory.create(this, connector, getIsSubRelationOfConnector(), superConnector);
			if (externalRepository != null) {
				externalRepository.create(relation);
			}
			addRelation(relation);
		}

		return connector;
	}

	public Connector createConnector(String connectorName) throws Exception {
		return createConnector(new Connector(this, connectorName));
	}

	public Connector createConnector(String connectorName, Connector superConnector) throws Exception {
		return createConnector(new Connector(this, connectorName), superConnector);
	}

	public Connector updateConnector(Connector connector) throws Exception {
		if (externalRepository != null) {
			externalRepository.update(connector);
		}
		return connector;
	}

	public Connector deleteConnector(Connector connector) throws Exception {
		if (externalRepository != null) {
			externalRepository.delete(connector);
		}
		removeConnector(connector);
		return connector;
	}

	public void deleteConnectors(ConnectorList connectorList) throws Exception {
		if (externalRepository != null) {
			externalRepository.delete(connectorList);
		}
		getConnectorList().removeAll(connectorList);
	}

	public Connector deleteConnectorWithId(String id) throws Exception {
		Connector object = getConnectorFromId(id);

		if (externalRepository != null) {
			externalRepository.delete(object);
		}
		removeConnector(object);
		return object;
	}

	public boolean existConnector(String connectorName, boolean exactSearch) {
		try {
			return getConnectorFromName(connectorName, GetMode.RETURN_NULL_IF_DOES_NOT_EXIST) != null;
		} catch (Exception e) {
			return false;
		}

	}

	public ConnectorList getConnectors(IConnectorCriteria connectorCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	

	public Connector getConnectorFromId(String id) throws ObjectDoesNotExistException {
		Entity entity = null;
		for (int i = 0; i < getConnectorList().size(); i++) {
			entity = getConnectorList().getConnector(i);
			if (entity.getId().equals(id)) {
				return (Connector) entity;
			}
		}
		throw new ObjectDoesNotExistException(id);
	}

	public ConnectorList getSubConnectorsOf(Connector connector, int depth) throws Exception {
		if (depth == 0) {
			return new ConnectorList();
		}
		Connector subclassConnector = getIsSubRelationOfConnector();
		RelationList relationList = queryRelations(null, subclassConnector, connector);
		Relation relation = null;
		ConnectorList connectorList = new ConnectorList();
		Connector thisConnector = null;

		for (int index = 0; index < relationList.size(); index++) {
			relation = relationList.getRelation(index);
			if (relation.getLeftEntity().getClass() == Connector.class) {
				thisConnector = (Connector) relation.getLeftEntity();
				connectorList.add(thisConnector);
				connectorList.addAll(getSubConnectorsOf(thisConnector, depth - 1));
			}
		}
		return connectorList;
	}

	public ConnectorList getSuperConnectorsOf(Connector connector, int depth) throws Exception {
		if (depth == 0) {
			return new ConnectorList();
		}
		Connector subclassConnector = getIsSubclassOfConnector();
		RelationList relationList = queryRelations(connector, subclassConnector, null);
		Relation relation = null;
		ConnectorList connectorList = new ConnectorList();
		Connector thisConnector = null;

		for (int index = 0; index < relationList.size(); index++) {
			relation = relationList.getRelation(index);
			if (relation.getRightEntity().getClass() == Connector.class) {
				thisConnector = (Connector) relation.getRightEntity();
				connectorList.add(thisConnector);
				connectorList.addAll(getSuperConnectorsOf(thisConnector, depth - 1));
			}
		}
		return connectorList;
	}

	public RelationList getRelationsOf(KnowledgerObject object) {
		IRelationCriteria relationCriteria = RelationCriteriaFactory.getRelationCriteria(object, null, null);
		RelationList list = getRelationList(relationCriteria);

		relationCriteria = RelationCriteriaFactory.getRelationCriteria(null, null, object);
		list.addAll(getRelationList(relationCriteria));

		return list;

	}

	public Relation createRelation(Relation relation) throws Exception {
		relation.setKnowledgeBase(this);

		// First check if relation exist
		RelationList relationList = queryRelations(relation.getLeftEntity(), relation.getConnector(), relation.getRightEntity());

		if (!relationList.isEmpty()) {
			for (int r = 0; r < relationList.size(); r++) {
				Relation existingRelation = relationList.getRelation(r);
				if ((existingRelation.getConnectorAlias() == null && relation.getConnectorAlias() == null)
						|| (existingRelation.getConnectorAlias() != null && relation.getConnectorAlias() != null && existingRelation.getConnectorAlias().equals(relation.getConnectorAlias()))) {
					logger.info("warning : Relation " + relation + " already exist");
					return relationList.getRelation(0);
				}
			}
		}

		if (externalRepository != null) {
			externalRepository.create(relation);
		}
		addRelation(relation);
		return relation;
	}

	public Relation createRelation(Entity leftentity, Connector connector, Entity rightEntity) throws Exception {
		return createRelation(new Relation(this, leftentity, connector, rightEntity));
	}

	public Relation updateRelation(Relation relation) throws Exception {
		if (externalRepository != null) {
			externalRepository.update(relation);
		}
		return relation;
	}

	public Relation deleteRelation(Relation relation) throws Exception {
		if (externalRepository != null) {
			externalRepository.delete(relation);
		}
		removeRelation(relation);
		return relation;
	}

	public void deleteRelations(RelationList relationList) throws Exception {
		if (externalRepository != null) {
			externalRepository.delete(relationList);
		}
		getRelationList().removeAll(relationList);
	}

	public Relation deleteRelationWithId(String id) throws Exception {
		Relation object = getRelationFromId(id);

		if (externalRepository != null) {
			externalRepository.delete(object);
		}
		removeRelation(object);
		return object;
	}

	public Relation getRelationFromId(String id) throws ObjectDoesNotExistException {
		for (int i = 0; i < getRelationList().size(); i++) {
			if (getRelationList().getRelation(i).getId().equals(id)) {
				return getRelationList().getRelation(i);
			}
		}
		throw new ObjectDoesNotExistException(id);
	}

	public RelationList getRelationList() {
		return relationList;
	}

	public RelationList getRelationList(IRelationCriteria relationCriteria) {
		if (relationCriteria.getClass() == RelationCriteria.class) {
			RelationQuery query = new RelationQuery();
			if (relationCriteria.getLeftObject() != null) {
				query.addLeftEntityToInclude(relationCriteria.getLeftObject());
			}
			if (relationCriteria.getConnector() != null) {
				query.addConnectorToInclude(relationCriteria.getConnector());
			}
			if (relationCriteria.getRightObject() != null) {
				query.addRightEntityToInclude(relationCriteria.getRightObject());
			}
			return query.executeOn(getRelationList());
		}
		return new RelationList();
	}

	public EntityList query(String dqlQuery) throws Exception {
		QueryLexer lexer = new QueryLexer(new StringReader(dqlQuery));
		KnowledgerQueryParser parser = new KnowledgerQueryParser(lexer);
		return query(parser.query());
	}

	public EntityList query(ICriteria criteria) {
		if(debug){
		    logger.debug("kql:executing:" + criteria.toString());
        }
		EntityList entities = new EntityList();
		entities.addAll(queryInstances(criteria));
		entities.addAll(queryConcepts(criteria));
		entities.addAll(queryConnectors(criteria));
		return entities;
	}

	public InstanceList queryInstances(ICriteria criteria) {
		return getInstanceList().getInstances(criteria);
	}

	public ConceptList queryConcepts(ICriteria criteria) {
		return getConceptList().getConcepts(criteria);
	}

	public ConnectorList queryConnectors(ICriteria criteria) {
		return getConnectorList().getConnectors(criteria);
	}

	public RelationList queryRelations(Entity leftEntity, Connector connector, Entity rightEntity) {
		RelationList result = new RelationList();
		boolean matchLeft = true;
		boolean matchConnector = true;
		boolean matchRight = true;
		Relation relation = null;

		for (int i = 0; i < getRelationList().size(); i++) {
			relation = (Relation) getRelationList().get(i);

			if (relation == null) {
				DLogger.debug("query : Relation of index " + i + " is null");
				continue;
			}

			matchLeft = true;
			matchConnector = true;
			matchRight = true;

			if (leftEntity != null) {
				matchLeft = relation.getLeftEntity().equals(leftEntity);
			}
			if (connector != null) {
				matchConnector = relation.getConnector().equals(connector);
			}
			if (rightEntity != null) {
				matchRight = relation.getRightEntity().equals(rightEntity);
			}

			if (matchLeft && matchConnector && matchRight) {
				result.add(relation);
			}
		}

		return result;
	}

	public EntityList getEntitiesByName(String name) {
		EntityList entities = getAllEntities();
		EntityList result = new EntityList();

		for (int i = 0; i < entities.size(); i++) {
			System.out.println(entities.getEntity(i).getIdentifier());
			if (entities.getEntity(i).getIdentifier() != null && entities.getEntity(i).getIdentifier().equalsIgnoreCase(name)) {
				result.add(entities.getEntity(i));
			}
		}
		return result;
	}

	/** Returns all entities that are not link to other */
	public EntityList getLostEntities() {
		EntityList lostEntities = new EntityList(getAllEntities());
		lostEntities.removeAll(getPropositionList());
		lostEntities.removeAll(getRelationList().getLeftEntities());
		return lostEntities;
	}

	public Concept getRootObject() throws Exception {
		return getConceptFromName(Messages.getString(ROOT_OBJECT_NAME), GetMode.CREATE_IF_DOES_NOT_EXIST);
	}

	public Connector getRootConnector() throws Exception {
		return getConnectorFromName(Messages.getString(ROOT_CONNECTOR_NAME), GetMode.CREATE_IF_DOES_NOT_EXIST, null);
	}

	protected void notifyKnowledgeBaseChange() {
		//setChanged();
		//notifyObservers(this);
	}

	public String getId() {
		return knowledgeBaseDescription.getId();
	}

	public void setId(String id) {
		knowledgeBaseDescription.setId(id);
	}

	public String getName() {
		return knowledgeBaseDescription.getName();
	}

	public void setName(String name) {
		knowledgeBaseDescription.setName(name);
	}

	public KnowledgeBaseType getType() {
		return knowledgeBaseDescription.getType();
	}

	public void setType(KnowledgeBaseType type) {
		knowledgeBaseDescription.setType(type);
	}

	public Connector getIsInstanceOfConnector() throws Exception {
		/** @todo cache these connectors */
		return getConnectorFromName(Messages.getString(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF), GetMode.CREATE_IF_DOES_NOT_EXIST);
	}

	public Connector getIsSubclassOfConnector() throws Exception {
		return getConnectorFromName(Messages.getString(IKnowledgeBase.CONNECTOR_IS_SUB_CLASS_OF), GetMode.CREATE_IF_DOES_NOT_EXIST);
	}

	public Connector getIsAConnector() throws Exception {
		return getConnectorFromName(Messages.getString(IKnowledgeBase.CONNECTOR_IS_A), GetMode.CREATE_IF_DOES_NOT_EXIST);
	}

	public Connector getIsSubRelationOfConnector() throws Exception {
		return getConnectorFromName(Messages.getString(IKnowledgeBase.CONNECTOR_IS_SUB_RELATION_OF), GetMode.CREATE_IF_DOES_NOT_EXIST);
	}

	public Connector getIsAttributeConnector() throws Exception {
		return getConnectorFromName(Messages.getString(IKnowledgeBase.CONNECTOR_ATTRIBUTE), GetMode.CREATE_IF_DOES_NOT_EXIST);
	}

	public Connector getHasAConnector() throws Exception {
		return getConnectorFromName(Messages.getString(IKnowledgeBase.CONNECTOR_HAS_A), GetMode.CREATE_IF_DOES_NOT_EXIST);
	}

	public Connector getHasAListOfConnector() throws Exception {
		return getConnectorFromName(Messages.getString(IKnowledgeBase.CONNECTOR_HAS_A_LIST_OF), GetMode.CREATE_IF_DOES_NOT_EXIST);
	}

	public Connector getIsInverseOfConnector() throws Exception {
		return getConnectorFromName(Messages.getString(IKnowledgeBase.CONNECTOR_IS_INVERSE_OF), GetMode.CREATE_IF_DOES_NOT_EXIST);
	}

	public Connector getIsEquivalentToConnector() throws Exception {
		return getConnectorFromName(Messages.getString(IKnowledgeBase.CONNECTOR_IS_EQUIVALENT_TO), GetMode.CREATE_IF_DOES_NOT_EXIST);
	}

	/**
	 * Set all instances properties in the instances and all the instances in
	 * their respective concept
	 * 
	 * @throws Exception
	 * 
	 * 
	 */
	public void optimizeStorage() throws Exception {
		Concept concept = null;
		Instance instance = null;
		if(debug){
			DLogger.info("Optimizing storage of " + conceptList.size() + " concepts");
		}
		for (int i = 0; i < conceptList.size(); i++) {
			concept = conceptList.getConcept(i);

			// Sets proporties/relations of the the concept
			concept.addRelationsInCache(queryRelations(concept, null, null));

			if(debug){
				DLogger.info("\nConcept '" + concept.getName() + "' have " + concept.getRelationList().size() + " relations/properties : " + concept.getRelationList());
			}

			// Sets instances : all objects x where x is-instance-of concept
			concept.setInstanceList(queryRelations(null, getIsInstanceOfConnector(), concept).getLeftEntities());

			if(debug){
				DLogger.info("Concept '" + concept.getName() + "' have " + concept.getDirectInstances().size() + " direct instances : " + concept.getDirectInstances());
			}

			// for all instances, sets its properties
			InstanceList instanceList = concept.getDirectInstances();
			for (int j = 0; j < instanceList.size(); j++) {
				instance = instanceList.getInstance(j);
				if(instance!=null){
					instance.addRelationsInCache(queryRelations(instance, null, null));
					if(debug){
						DLogger.info("Instance " + instance.getId() + " have " + instance.getRelationList().size() + " relations/properties : " + instance.getRelationList());
					}
				}else{
					if(debug){
						DLogger.info(instanceList.getEntity(j).getIdentifier() +" is linked to " + concept.getName() + " by relation is instance of => This is not consistent as " + instanceList.getEntity(j).getIdentifier() + " is a " + instanceList.getEntity(j).getClass().getName());
					}
				}
			}
		}
	}

	/**
	 * @return
	 */
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

    
    public Connector getConnectorFromName(String name, GetMode mode) throws Exception {
        return getConnectorFromName(name, mode, getRootConnector());
    }

    public Connector getConnectorFromName(String name, GetMode mode, Connector defaultSuperConnector) throws Exception {
        Entity entity = null;
        for (int i = 0; i < getConnectorList().size(); i++) {
            entity = getConnectorList().getConnector(i);
            if (entity.getIdentifier().equals(name)) {
                return (Connector) entity;
            }
        }
        if (mode == GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST) {
            throw new ObjectDoesNotExistException(name, "Connector");
        }
        if (mode == GetMode.RETURN_NULL_IF_DOES_NOT_EXIST) {
            return null;
        }
        return createConnector(name, defaultSuperConnector);

    }
    
    
    public String getIdentifier(){
        return knowledgeBaseDescription.getIdentifier();
    }

   
    public void addObserver(Observer observer){
        
    }

	public void setConceptList(ConceptList conceptList) {
		this.conceptList = conceptList;
	}

	public void setConnectorList(ConnectorList connectorList) {
		this.connectorList = connectorList;
	}

	public void setInstanceList(InstanceList instanceList) {
		this.instanceList = instanceList;
	}

	public void setRelationList(RelationList relationList) {
		this.relationList = relationList;
	}
    

	public KnowledgerObject create(KnowledgerObject object) throws Exception{
		if(object instanceof Instance){
			return createInstance((Instance) object);
		}
		if(object instanceof Concept){
			return createConcept((Concept) object);
		}
		if(object instanceof Connector){
			return createConnector((Connector) object);
		}
		if(object instanceof Relation){
			return createRelation((Relation) object);
		}
		throw new RuntimeException("Object of type " + object.getClass().getName() +" can not be stored in knowledger Base!");

	}
	
	public KnowledgerObject update(KnowledgerObject object) throws Exception{
		if(object instanceof Instance){
			return updateInstance((Instance) object);
		}
		if(object instanceof Concept){
			return updateConcept((Concept) object);
		}
		if(object instanceof Connector){
			return updateConnector((Connector) object);
		}
		if(object instanceof Relation){
			return updateRelation((Relation) object);
		}
		throw new RuntimeException("Object of type " + object.getClass().getName() +" can not be updated in knowledger Base!");

	}
	public KnowledgerObject delete(KnowledgerObject object) throws Exception{
		if(object instanceof Instance){
			return deleteInstance((Instance) object);
		}
		if(object instanceof Concept){
			return deleteConcept((Concept) object);
		}
		if(object instanceof Connector){
			return deleteConnector((Connector) object);
		}
		if(object instanceof Relation){
			return deleteRelation((Relation) object);
		}
		throw new RuntimeException("Object of type " + object.getClass().getName() +" can not be deleted from knowledger Base!");

	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	protected void clear(){
		conceptList.clear();
		instanceList.clear();
		connectorList.clear();
		propositionList.clear();
		relationList.clear();
	}

	public KnowledgerObject getObjectFromId(String id) {
		KnowledgerObject object = null;
	
		for (int i = 0; i < getConceptList().size(); i++) {
			object = getConceptList().getConcept(i);
			if (object.getId().equals(id)){
				return object;
			}
		}
	
		for (int i = 0; i < getInstanceList().size(); i++) {
			object = getInstanceList().getInstance(i);
			if (object.getId().equals(id)){
				return object;
			}
		}
	
		for (int i = 0; i < getConnectorList().size(); i++) {
			object = getConnectorList().getConnector(i);
			if (object.getId()== id) {
				return object;
			}
		}
		
		for (int i = 0; i < getRelationList().size(); i++) {
			object = getRelationList().getRelation(i);
			if (object.getId()== id) {
				return object;
			}
		}
		
		
		throw new RuntimeException("object with id " + id + " does not exist in knowledge base " + getIdentifier());
	}

	public EntityList getAllEntities() {
		EntityList entities = new EntityList();
		entities.addAll(conceptList);
		entities.addAll(instanceList);
		entities.addAll(connectorList);
		entities.addAll(propositionList);
		return entities;
	}

}
