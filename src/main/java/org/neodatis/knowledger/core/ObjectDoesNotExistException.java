/* 
 * $RCSfile: ObjectDoesNotExistException.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core;

/**
 * <p>
 * 
 * </p>
 * 
 */
public class ObjectDoesNotExistException extends Exception {

    private long id;
    private String name;
    private String type;
    /**
     * 
     */
    public ObjectDoesNotExistException(long id) {
        super();
        this.id = id;
        
    }

    /**
     * @param message
     */
    public ObjectDoesNotExistException(String name) {
        super();
        this.name = name;
    }

    /**
     * @param message
     */
    public ObjectDoesNotExistException(String name,String type) {
        super();
        this.name = name;
        this.type = type;
    }
    

    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
        if(type==null){
            type = "Object";
        }
        if(id!=0){
            return type + " with id " + id + " does not exist";
        }else{
            return type + " with name " + name + " does not exist";
        }
    }
    
    
}
