/* 
 * $RCSfile: PropositionFactory.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.factory;

import org.neodatis.knowledger.core.implementation.entity.Proposition;
import org.neodatis.knowledger.core.implementation.entity.Relation;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class PropositionFactory {

    public static Proposition create(Relation relation){
        Proposition proposition = new Proposition(null);
        proposition.add(relation);
        return proposition;
    }
}
