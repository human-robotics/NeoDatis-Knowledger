package org.neodatis.knowledger.core.implementation.entity;




/**
 * @hibernate.subclass discriminator-value="0"
 * 
 * @author olivier s
 *
 */
public class LeftRelationPart extends RelationPart {
    public LeftRelationPart(){
        super();
    }
    public LeftRelationPart(Relation relation, Entity entity,int order){
        super(relation,entity,LEFT,new Integer(order));
    }

}
