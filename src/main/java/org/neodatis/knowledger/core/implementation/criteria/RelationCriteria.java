/* 
 * $RCSfile: RelationCriteria.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation.criteria;

import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.entity.LazyRelation;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.interfaces.entity.IRelationCriteria;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class RelationCriteria extends AbstractCriteria implements IRelationCriteria {
    private static final int MODE_OBJECT = 0;
    private static final int MODE_STRING = 1;
	
	private KnowledgerObject leftObject;
    private Connector connector;
    private KnowledgerObject rightObject;
    private String leftObjectIdentifier;
    private String connectorIdentifier;
    private String rightObjectIdentifier;
    private int mode; 
    /**
     * 
     */
    public RelationCriteria(KnowledgerObject leftObject,Connector connector,KnowledgerObject rightObject) {
        this.leftObject = leftObject;
        this.connector = connector;
        this.rightObject = rightObject;
        mode = MODE_OBJECT;
    }
    public RelationCriteria(String leftObject,String connector,String rightObject) {
    	leftObjectIdentifier = leftObject;
    	connectorIdentifier = connector;
    	rightObjectIdentifier = rightObject;
    	mode = MODE_STRING;
    }

    public RelationCriteria(Relation relation) {
    	this(relation.getLeftEntity(),relation.getConnector(),relation.getRightEntity());
	}
	public KnowledgerObject getLeftObject() {
        return leftObject;
    }

    public Connector getConnector() {
        return connector;
    }

    public KnowledgerObject getRightObject() {
        return rightObject;
    }

    public String getLeftObjectIdentifier() {
        return leftObjectIdentifier;
    }

    public String getConnectorIdentifier() {
        return connectorIdentifier;
    }

    public String getRightObjectIdentifier() {
        return rightObjectIdentifier;
    }

	public boolean match(KnowledgerObject object) {
		
		if(object.getClass() != Relation.class && object.getClass() != LazyRelation.class){
			return false;
		}
		Relation relation = (Relation) object;
		
		if(mode==MODE_OBJECT){
			return 
				(getLeftObject()  == null || relation.getLeftEntity().equals(getLeftObject())) && 
				(getConnector()   == null || relation.getConnector().equals(getConnector())) && 
				(getRightObject() == null || relation.getRightEntity().equals(getRightObject()));
		}else {
			return 
			(getLeftObjectIdentifier()  == null || relation.getLeftEntity().getIdentifier().equals(getLeftObjectIdentifier())) && 
			(getConnectorIdentifier()   == null || relation.getConnector().getIdentifier().equals(getConnectorIdentifier())) && 
			(getRightObjectIdentifier() == null || relation.getRightEntity().getIdentifier().equals(getRightObjectIdentifier()));
		}
		
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		if(mode==MODE_STRING){
			buffer.append(leftObjectIdentifier).append(" ").append(connectorIdentifier).append(" ").append( rightObjectIdentifier);
		}else{
			buffer.append(getLeftObject()).append(" ").append(getConnector()).append(" ").append( getRightObject());
		}
		
		return buffer.toString();
	}
}
