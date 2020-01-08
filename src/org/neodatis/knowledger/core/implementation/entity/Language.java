package org.neodatis.knowledger.core.implementation.entity;

import java.io.Serializable;

public class Language implements Serializable {

	private String id;
	private String name;
	
	public Language(){
	}
	
	public Language(String name,String id){
		this.name = name;
		this.id = id;
	}

	public Language(String name){
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}
	
    public String toString() {
        return name;
    }
		
	
	
	
}
