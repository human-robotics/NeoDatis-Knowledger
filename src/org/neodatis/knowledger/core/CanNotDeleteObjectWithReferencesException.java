package org.neodatis.knowledger.core;

import org.neodatis.knowledger.core.implementation.entity.RelationList;


public class CanNotDeleteObjectWithReferencesException extends Exception{
    private String info;
    private String id;
    private RelationList relationList;
    public CanNotDeleteObjectWithReferencesException(String info , String id, RelationList relationList){
        super(info + " " + id);
        this.info=info;
        this.id=id;
        this.relationList=relationList;
    }

    public String getMessage(){
        return "Can not delete object with active references : " + info + " " + id;
    }
    
    
    
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return Returns the info.
	 */
	public String getInfo() {
		return info;
	}
	
	
	/**
	 * @return
	 */
	public RelationList getRelationList() {
		return relationList;
	}

}
