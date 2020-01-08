package org.neodatis.knowledger.core.implementation.criteria;

import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.interfaces.entity.IInstanceCriteria;

public class SimpleEntityCriteria extends AbstractCriteria implements IInstanceCriteria {

	private String value;

	public SimpleEntityCriteria(String value) {
		this.value = value;
	}

	public boolean match(KnowledgerObject object) {
        if(!Entity.class.isAssignableFrom(object.getClass())){
            return false;
        }		
        Entity entity = (Entity) object;

		if(value.indexOf("%")!=-1){
			return entity.getIdentifier().toLowerCase().matches(value.replaceAll("%","(.)*").toLowerCase());
		}
		return entity.getIdentifier().equalsIgnoreCase(value);
	}

	public static void main(String[] args) {
		String text = "olivier smadja";
		
		boolean b = text.matches("olivier smadja");
		System.out.println(b);

		boolean b1 = text.matches("olivie(.)*");
		System.out.println(b1);
}
}
