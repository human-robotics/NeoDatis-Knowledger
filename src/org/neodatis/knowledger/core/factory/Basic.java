/* 
 * $RCSfile: Basic.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2004 Dialogy
 */
package org.neodatis.knowledger.core.factory;

import java.util.Properties;
import java.util.ResourceBundle;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class Basic {

    public static String getClassName(String entityName){
        Properties properties = new Properties();
        ResourceBundle bundle = ResourceBundle.getBundle("factory");
        return bundle.getString(entityName);
    }
    public static Object getInstance(String entityName) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        Class myclass = Class.forName( getClassName(entityName));
        return myclass.newInstance();
    }
}
