/* 
 * $RCSfile: InstanceFactory.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.factory;

import org.neodatis.knowledger.core.implementation.entity.Instance;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class InstanceFactory {

    public static Instance create(String name){
        return new Instance(name);
    }
}
