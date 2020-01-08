package org.neodatis.knowledger.core.implementation.entity;



/**
 * @hibernate.subclass discriminator-value="1"
 * 
 * @author olivier s
 *
 */
public class RightRelationPart extends RelationPart {
    public RightRelationPart(){
        super();
    }
    public RightRelationPart(Relation relation,Entity entity,int order){
        super(relation,entity,RIGHT,new Integer(order));
    }

}
