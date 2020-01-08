package org.neodatis.knowledger.core.implementation.entity;

import java.util.Date;
import java.util.UUID;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;

/**
 * <p>
 * The super class of all knowledger objects
 * 
 * </p>
 * 
 */
public abstract class KnowledgerObject {

	private String id;
	private KnowledgeBase knowledgeBase;
	protected Date creationDate;
	private String identifier;

	public KnowledgerObject() {
	}

	public KnowledgerObject(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
		if (knowledgeBase.getType().needsUniqueIdGeneration()) {
			id = UUID.randomUUID().toString();
		}
		creationDate = new Date();
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}

	public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;

		if (knowledgeBase != null && id == null && knowledgeBase.getType().needsUniqueIdGeneration()) {
			id = UUID.randomUUID().toString();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(java.lang.Object object) {
		// check the type of the object
		if (object == null || !(object instanceof KnowledgerObject ) ){
			return false;
		}
		KnowledgerObject knowledgerObject = null;

		try {
			knowledgerObject = (KnowledgerObject) object;
		} catch (Exception e) {
			return false;
		}
		if (knowledgerObject.getId() ==null || getId() == null) {
			return false;
		}
		if (getId() == knowledgerObject.getId()) {
			return true;
		}
		return false;
	}

	/**
	 * @return Returns the creationDate.
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            The creationDate to set.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return Returns the identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 *            The identifier to set.
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


}