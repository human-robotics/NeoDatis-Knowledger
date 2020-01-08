package org.neodatis.knowledger.core.implementation.entity;

import java.io.Serializable;


/** Label is a class to manage entities translation. 
 * 
 * An entities does not have a label. Instead it has a list of label for each language. A label is the translation of an entity for specific language
 *  
 * @author olivier
 *
 */
public class Label implements Serializable {

	/** The id of the Label*/
	private String id;
	/** The text of the label*/
	private String text;
	/** The language of the label*/
	private Language language;
	/** The entity this label translates*/
    private Entity entity;
	
	public Label(){
	}
	
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Label(String text){
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setId(String id) {
		this.id = id;
	}

    /**
     * @return Returns the entity.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @param entity The entity to set.
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
	
		
	
    public String toString() {
    	return text;
    }
	
}
