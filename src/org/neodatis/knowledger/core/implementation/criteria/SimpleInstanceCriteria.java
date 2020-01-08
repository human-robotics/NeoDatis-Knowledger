package org.neodatis.knowledger.core.implementation.criteria;

import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.interfaces.entity.IInstanceCriteria;

public class SimpleInstanceCriteria extends AbstractCriteria implements IInstanceCriteria {

	// private String instanceIdentifier;
	private String instanceValue;

	public SimpleInstanceCriteria(String instanceValue) {
		// this.instanceIdentifier = instanceIdentifier;
		this.instanceValue = instanceValue;
	}

	public boolean match(KnowledgerObject object) {
		if (object.getClass() != Instance.class) {
			return false;
		}
		Instance instance = (Instance) object;

		if(instance.getValue()==null){
			return false;
		}
		if(instanceValue.indexOf("%")!=-1){
			return instance.getValue().toLowerCase().matches(instanceValue.replaceAll("%","(.)*").toLowerCase());
		}
		boolean match = instance.getValue().equalsIgnoreCase(instanceValue);
		return match;
	}

	public static void main(String[] args) {
		String text = "olivier smadja";
		
		boolean b = text.matches("olivier smadja");
		System.out.println(b);

		boolean b1 = text.matches("olivie(.)*");
		System.out.println(b1);
}
}
