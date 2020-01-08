package org.neodatis.knowledger.core.implementation.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.neodatis.knowledger.core.PropertyDoesNotExistException;
import org.neodatis.knowledger.core.implementation.criteria.RelationConnectorCriteria;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;


/**
 */
public class Entity extends KnowledgerObject{

	//private Map properties;
	private transient RelationList relationList;

	private User user;

	private Set<Label> labels;

	public Entity() {
		super(null);
		relationList = new RelationList();
		labels = new HashSet();
	}

	public Entity(KnowledgeBase knowledgeBase, String identifier) {
		super(knowledgeBase);
		relationList = new RelationList();
		labels = new HashSet();
		setIdentifier(identifier);
		//properties = new HashMap();
	}

	public Entity(KnowledgeBase knowledgeBase) {
		super(knowledgeBase);
		//properties = new HashMap();
		relationList = new RelationList();
		labels = new HashSet();
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Set getLabels() {
		return labels;
	}

	public String getIdentifier() {
		return getCurrentLabel().getText();
	}

	/**@TODO Move labels to an external map*/
	private Label getCurrentLabel() {
		if (labels == null) {
			labels = new HashSet<Label>();
		}
		if (labels.isEmpty()) {
            Label label = new Label();
            label.setEntity(this);
            label.setLanguage(getKnowledgeBase().getLanguage());
            labels.add(label);

			return label;
		}
		for(Label l:labels) {
            if(getKnowledgeBase()==null){
                return l;
            }
			if (l.getLanguage().equals(getKnowledgeBase().getLanguage())) {
				return l;
			}
		}
		throw new RuntimeException("The entity with id " + getId() + " do not have label for language " + getKnowledgeBase().getLanguage().getName());
	}

	public User getUser() {
		return user;
	}

	public void setLabels(Set labels) {
		this.labels = labels;
	}

	public void setCreationDate(Date date) {
		creationDate = date;
	}

	public void setIdentifier(String string) {
		getCurrentLabel().setText(string);
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Relation connectTo(String connectorName, Entity entity) throws Exception {
		return connectTo(getKnowledgeBase().getConnectorFromName(connectorName, GetMode.CREATE_IF_DOES_NOT_EXIST), entity);
	}

	public Relation connectTo(Connector connector, Entity entity) throws Exception {
		//addPropertyInCache(connector, entity);
		Relation relation = getKnowledgeBase().createRelation(this, connector, entity);
		addRelationInCache(relation);
		return relation;
	}

	public Entity setProperty(String propertyName, Entity propertyValue) throws Exception {
		return setProperty(propertyName, propertyValue, false);
	}

	/**
	 * Sets a property for the entity, only if property (connector) already
	 * exist. <code>
	 * olivier.name = 'Olivier'
	 * 
	 * </code>
	 * 
	 * @param propertyName
	 * @param entity
	 * @param onlyIfPropertyNameExist,
	 *            if true, then if will throw exception if property does not
	 *            exist as a connector
	 * @return
	 * @throws Exception
	 */
	public Entity setProperty(String propertyName, Entity propertyValue, boolean onlyIfPropertyNameExist) throws Exception {
		// If the flag is true, then first check if propertyName exist as a
		// relation, if not, throw exception
		if (onlyIfPropertyNameExist) {
			Connector connector = getKnowledgeBase().getConnectorFromName(propertyName, GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
			connectTo(connector, propertyValue);
			return this;
		}
		// If there is no connector with the name of the property, a new
		// connector instance with the propoerty name will be created
		Connector connector = getKnowledgeBase().getConnectorFromName(propertyName, GetMode.CREATE_IF_DOES_NOT_EXIST, getKnowledgeBase().getIsAttributeConnector());
		connectTo(connector, propertyValue);
		return this;
	}

	public Entity getProperty(String propertyName) {

		// If property name has ., this means the propertyName represents
		// relation
		// Example : peter.getProperty("wife.father.name") will return the name
		// of the father of the wife of peter!
		if (propertyName.indexOf(".") != -1) {
			Entity entity = this;
			String aPropertyName = null;
			StringTokenizer tokenizer = new StringTokenizer(propertyName, ".");
			while (tokenizer.hasMoreTokens()) {
				aPropertyName = tokenizer.nextToken();
				entity = entity.getProperty(aPropertyName);
			}
			return entity;
		} else {
			RelationList sublist = getRelationList().filter(new RelationConnectorCriteria(propertyName));
			if(sublist.isEmpty()){
				throw new PropertyDoesNotExistException(this.getId(), propertyName);
			}
			return sublist.getRightEntities().getEntity(0);
		}
	}

	public EntityList getProperties(String propertyName) throws Exception {

		// If property name has ., this means the propertyName represents
		// relation
		// Example : peter.getProperty("wife.father.name") will return the name
		// of the father of the wife of peter!
		if (propertyName.indexOf(".") != -1) {
			Entity entity = this;
			String aPropertyName = null;
			StringTokenizer tokenizer = new StringTokenizer(propertyName, ".");
			while (tokenizer.hasMoreTokens()) {
				aPropertyName = tokenizer.nextToken();
				if (tokenizer.hasMoreTokens()) {
					entity = entity.getProperty(aPropertyName);
				} else {
					break;
				}
			}
			return entity.getProperties(aPropertyName);

		} else {
			RelationList sublist = getRelationList().filter(new RelationConnectorCriteria(propertyName));
			if(sublist.isEmpty()){
				throw new PropertyDoesNotExistException(this.getId(), propertyName);
			}
			return sublist.getRightEntities();
		}
	}

	public RelationList getRelationList() {
		return relationList;
	}

	public boolean hasProperty(String propertyName) {

		try {
			getProperty(propertyName);
			return true;
		} catch (Exception e) {
			//DLogger.info(e.getMessage());
			return false;
		}
	}
	/*
	private void addPropertyInCache(IConnector connector, IEntity entity) {
		properties.put(connector, entity);
		properties.put(connector.getIdentifier(), entity);
	}
	*/
	public void addRelationsInCache(RelationList relations){
		relationList.addAll(relations);
	}
	public void addRelationInCache(Relation relation) {
		//properties.put(relation.getConnector(), relation.getRightEntity());
		//properties.put(relation.getConnector().getIdentifier(), relation.getRightEntity());
		relationList.add(relation);
	}

	public boolean isInstance() {
        return this instanceof Instance;
        /*
		try {
			return getProperty(IKnowledgeBase.CONNECTOR_IS_INSTANCE_OF) != null;
		} catch (RuntimeException e) {
			DLogger.error(e.getMessage());
			return false;
		}
        */
	}
	public boolean isConcept() {
        return this instanceof Concept;
        /*
		try {
			if(getKnowledgeBase()!=null){
                 return getKnowledgeBase().getRootObject().equals(this) || getProperty(IKnowledgeBase.CONNECTOR_IS_SUB_CLASS_OF) != null;
            }
            return getProperty(IKnowledgeBase.CONNECTOR_IS_SUB_CLASS_OF) != null;
		} catch (Exception e) {
			DLogger.error(e.getMessage());
			return false;
		}
        */
	}
    
	public boolean isConnector() {
        return this instanceof Connector;
        /*
        try {
            if(getKnowledgeBase()!=null){
                return getKnowledgeBase().getRootConnector().equals(this) || getProperty(IKnowledgeBase.CONNECTOR_IS_SUB_RELATION_OF) != null;
            }
			return getProperty(IKnowledgeBase.CONNECTOR_IS_SUB_RELATION_OF) != null;
		} catch (Exception e) {
			DLogger.debug(e.getMessage());
			return false;
		}*/
	}

	public List getDefinedRelationsAndProperties(){
		List result = new ArrayList();

		// first gets the superclass
		Entity entity = this.getProperty(IKnowledgeBase.CONNECTOR_IS_SUB_CLASS_OF);
		
		if(entity==null){
			return result;
		}
		
		//result.addAll(getProperties().keySet());		
		
		return result;
		
	}
}
