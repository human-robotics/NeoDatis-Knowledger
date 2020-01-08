/* 
 * $RCSfile: ConceptFactory.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.factory;

import org.neodatis.knowledger.core.implementation.entity.Concept;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class ConceptFactory {

    public static Concept create(String name){
        return new Concept(name);
    }
}
