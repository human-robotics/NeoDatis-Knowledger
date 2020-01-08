package org.neodatis.knowledger.core.implementation.entity;

import java.io.Serializable;

public abstract class RelationPart implements Serializable {

    protected final static int LEFT=0;
    protected final static int RIGHT=1;
	private Long id;
	private Relation relation;
	private Entity entity;
	private Integer order;
	private int type;
	
	public RelationPart(){
	}
    public RelationPart(Relation relation,Entity entity,int type,Integer order){
        this.relation = relation;
        this.entity = entity;
        this.type = type;
        this.order = order;
    }
	
	public Entity getEntity() {
		return entity;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public RelationPart(String text){
	}

	public Long getId() {
		return id;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
}
