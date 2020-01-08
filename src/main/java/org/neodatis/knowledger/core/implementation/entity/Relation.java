package org.neodatis.knowledger.core.implementation.entity;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;


/** a relation connect some entities to other using a connector:
 * 
 * <pre>
 * 
 * Example:
 * The following is a simple relation : NeoBotis is-a Company
 * where NeoBotis is the left part, Company is the right part and 'is-a'  is the connector
 * 
 * In a relation, left part and right part may contain more than one entity
 *    
 * 
 * 
 * 
 * 
 * </pre>
 */
public class Relation extends KnowledgerObject{

	/** the proposition this relaton is linked to */
	private Proposition proposition;

	/** The left part entities*/
	protected EntityList leftEntities;

	/** The right part entities */
	protected EntityList rightEntities;

	/** The relation connector*/
	protected Connector connector;

	private String connectorAlias;

	/** a relation may be mandatory or not
	 * 
	 * Example : Man has-a Wife
	 * */
	private boolean mandatory;

	/** The confiability of the relation*/
	private int confiability;
	
	private int minCardinality;
	private int maxCardinality;
    private int cardinality;

	public Relation() {
		super(null);
        this.cardinality = 1;
	}

	public Relation(KnowledgeBase knowledgeBase, Entity leftObject, Connector connector, Entity rightObject) {
		this(knowledgeBase, leftObject, connector, null, rightObject, 1, 1, true);
	}

	public Relation(KnowledgeBase knowledgeBase, Entity leftObject, Connector connector, String connectorAlias, Entity rightObject, int minCardinality, int maxCardinality, boolean mandatory) {
		super(knowledgeBase);
		this.leftEntities = new EntityList();
		this.rightEntities = new EntityList();
		leftEntities.add(leftObject);
		rightEntities.add(rightObject);
		this.connector = connector;
		this.connectorAlias = connectorAlias;
		this.creationDate = new Date();
		this.minCardinality = minCardinality;
		this.maxCardinality = maxCardinality;
		this.mandatory = mandatory;
        this.cardinality = 1;
	}

	public Relation(Entity leftObject, Connector connector, Entity rightObject) {
		this(null, leftObject, connector, rightObject);
	}

	public String getId() {
		return super.getId();
	}

	public KnowledgeBase getKnowledgeBase() {
		return super.getKnowledgeBase();
	}

	public Connector getConnector() {
		return connector;
	}

	public List getLeftEntities() {
		return leftEntities;
	}

	public Entity getLeftEntity() {
		Iterator iterator = leftEntities.iterator();
		if (!iterator.hasNext()) {
			throw new RuntimeException("Knowledge base " + getKnowledgeBase().getName() + " is inconsistent : relation id " + getId() + " do not have left part!");
		}
		return (Entity) iterator.next();
	}

	public EntityList getRightEntities() {
		return rightEntities;
	}

	public Entity getRightEntity() {
	    return (Entity) rightEntities.iterator().next();
	}

	public Proposition getProposition() {
		return proposition;
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	public void setProposition(Proposition proposition) {
		this.proposition = proposition;
	}

	public void setLeftEntities(EntityList list) {
		leftEntities = list;
	}

	public void setRightEntities(EntityList list) {
		rightEntities = list;
	}

	public void setLeftEntity(Entity entity) {
        if(leftEntities==null){
            leftEntities = new EntityList();
        }
		leftEntities.add(entity);
	}

	public void setRightEntity(Entity entity) {
	    if(rightEntities!=null){
            rightEntities = new EntityList();
        }
        
		rightEntities.add(entity);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();

		try {
			if (getLeftEntity().isInstance()) {
				if(getKnowledgeBase()!=null && getConnector().isSubConnectorOf(getKnowledgeBase().getIsAttributeConnector())) {
					if(getConnectorAlias()!=null){
						buffer.append(getLeftEntity()).append(".").append(getConnectorAlias()).append(" = ").append(getRightEntity());
					}else{
						buffer.append(getLeftEntity()).append(".").append(getConnector()).append(" = ").append(getRightEntity());
					}
				} else {
					buffer.append(getLeftEntity()).append(" ").append(getConnector()).append(" ").append(getRightEntity());
				}
			}
			if (getLeftEntity().isConcept()) {
				buffer.append(getLeftEntity()).append(" ").append(getConnector()).append(" ").append(getRightEntity());
				if(connectorAlias!=null){
					buffer.append(" : ").append(connectorAlias);
				}
			}
			if(buffer.length()==0){
				buffer.append(getLeftEntity()).append(" ").append(getConnector()).append(" (").append(cardinality).append(") ").append(getRightEntity());
			}
            //buffer.append(" : "+getLeftEntity().getId()).append(" ").append(getConnector().getId()).append(" ").append(getRightEntity().getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			buffer.append(getLeftEntity()).append(" ").append(getConnector()).append(" ").append(getRightEntity());
		}

		return buffer.toString();
	}

	public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
		/*
		 * if(leftEntity!=null){ leftEntity.setKnowledgeBase(knowledgeBase); }
		 */
		if (connector != null) {
			connector.setKnowledgeBase(knowledgeBase);
		}
		/*
		 * if(rightEntity!=null){ rightEntity.setKnowledgeBase(knowledgeBase); }
		 */
		super.setKnowledgeBase(knowledgeBase);
	}

	public String getConnectorAlias() {
		return connectorAlias;
	}

	public void setConnectorAlias(String connectorAlias) {
		this.connectorAlias = connectorAlias;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public int getConfiability() {
		return confiability;
	}

	public void setConfiability(int confiability) {
		this.confiability = confiability;
	}

	public int getMaxCardinality() {
		return maxCardinality;
	}

	public void setMaxCardinality(int maxCardinality) {
		this.maxCardinality = maxCardinality;
	}

	public int getMinCardinality() {
		return minCardinality;
	}

	public void setMinCardinality(int minCardinality) {
		this.minCardinality = minCardinality;
	}

    /**
     * @return the cardinality
     */
    public int getCardinality() {
        return cardinality;
    }

    /**
     * @param cardinality the cardinality to set
     */
    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }
    

}
