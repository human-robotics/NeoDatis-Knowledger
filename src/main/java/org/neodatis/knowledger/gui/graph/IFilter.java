/* 
 * $RCSfile: IFilter.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.gui.graph;

import org.neodatis.knowledger.core.implementation.entity.Entity;

public interface IFilter {
    //  To implement a specific user filter
    public boolean display(Entity entity);
}
