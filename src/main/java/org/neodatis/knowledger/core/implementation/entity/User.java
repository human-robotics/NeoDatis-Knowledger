package org.neodatis.knowledger.core.implementation.entity;

import java.util.Date;

/**
 * 
 */
public class User {
	
	private Integer id;
	private String name;
	private String email;
	private Date creationDate;

	public User() {
        creationDate = new Date();
    }

    /**
	 * @hibernate.id column="id" generator-class="native" unsaved-value="null"
	 */
	public Integer getId() {
		return id;
	}	
	
	/**
	 * @hibernate.property column="email"
	 */	
	public String getEmail() {
		return email;
	}

	/**
	 * @hibernate.property column="name"
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @hibernate.property column="creation_date"
	 */
	public Date getCreationDate() {
		return creationDate;
	}	

	public void setEmail(String string) {
		email = string;
	}

	public void setId(Integer integer) {
		id = integer;
	}

	public void setName(String string) {
		name = string;
	}

	public void setCreationDate(Date date) {
		creationDate = date;
	}
}
