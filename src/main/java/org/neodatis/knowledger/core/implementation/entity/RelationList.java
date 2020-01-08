/* 
 * $RCSfile: RelationList.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;
import org.neodatis.knowledger.core.interfaces.entity.IRelationCriteria;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class RelationList extends ArrayList<Relation> {

	public RelationList() {
		super();
	}

	public RelationList(KnowledgeBase knowledgeBase,
			Collection<Relation> relations) {
		for (Relation r : relations) {
			r.setKnowledgeBase(knowledgeBase);
			add(r);
		}
	}

	public Relation getRelation(int index) {
		return get(index);
	}

	public EntityList getLeftEntities() {
		EntityList entityList = new EntityList();
		for (int i = 0; i < size(); i++) {
			entityList.add(getRelation(i).getLeftEntity());
		}
		return entityList;
	}

	public EntityList getRightEntities() {
		EntityList entityList = new EntityList();
		for (int i = 0; i < size(); i++) {
			entityList.add(getRelation(i).getRightEntity());
		}
		return entityList;
	}

	public ConnectorList getConnectors() {
		ConnectorList connectorList = new ConnectorList();
		for (int i = 0; i < size(); i++) {
			connectorList.add(getRelation(i).getConnector());
		}
		return connectorList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < size(); i++) {
			buffer.append(getRelation(i).toString()).append("\n");
		}

		return buffer.toString();
	}

	public RelationList getRelations(IRelationCriteria criteria) {
		RelationList sublist = new RelationList();
		for (Relation r : this) {
			if (criteria.match(r)) {
				sublist.add(r);
			}
		}
		return sublist;
	}

	public RelationList filter(ICriteria criteria) {
		RelationList sublist = new RelationList();
		for (Relation r : this) {
			if (criteria.match(r)) {
				sublist.add(r);
			}
		}
		return sublist;
	}

}
