/* 
 * $RCSfile: EntityCriteria.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation.criteria;

import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class EntityCriteria extends AbstractCriteria {
	private String connectorName;
	private String value;

	/**
	 * 
	 */
	public EntityCriteria(String connectorName, String value) {
		this.connectorName = connectorName.replaceAll("'", "");
		this.value = value.replaceAll("'", "");
	}

    public EntityCriteria(String value) {
        this.connectorName = null;
        this.value = value.replaceAll("'", "");
    }

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if(connectorName==null){
            buffer.append("x").append("=").append(value);
        }else{
            buffer.append("x ").append(connectorName).append("=").append(value);            
        }

		return buffer.toString();
	}

	public boolean match(KnowledgerObject object) {
		
        if(!Entity.class.isAssignableFrom(object.getClass())){
            return false;
        }

        Entity entity = (Entity) object;
        
        if(connectorName==null){
            EntityList entityList = new EntityList();
            entityList.add(entity);
            return entityList.filter(new SimpleEntityCriteria(value)).size() > 0;
        }
        
		if (entity.hasProperty(connectorName)) {
			EntityList properties=null;
			try {
				properties = entity.getProperties(connectorName);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
            return properties.filter(new SimpleEntityCriteria(value)).size() > 0;
		}
		return false;
	}

	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String propertyName) {
		this.connectorName = propertyName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
