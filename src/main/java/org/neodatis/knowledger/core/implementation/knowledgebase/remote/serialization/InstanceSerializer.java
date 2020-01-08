package org.neodatis.knowledger.core.implementation.knowledgebase.remote.serialization;

import java.util.StringTokenizer;

import org.neodatis.knowledger.Knowledger;
import org.neodatis.knowledger.core.implementation.entity.Instance;


public class InstanceSerializer implements ISerializer {
	public String TYPE = "I";

	public String toString(Object object) {

		Instance instance = (Instance) object;

		StringBuffer buffer = new StringBuffer();
		buffer.append(getType()).append(SEPARATOR);

		buffer.append(instance.getKnowledgeBase().getId()).append(SEPARATOR);
		buffer.append(instance.getId()).append(SEPARATOR);
		buffer.append(instance.getIdentifier()).append(SEPARATOR);
		buffer.append(dateFormat.format(instance.getCreationDate())).append(SEPARATOR);
		return buffer.toString();
	}

	public Object fromString(String data) throws Exception {

		String tmp = null;
		StringTokenizer tokenizer = new StringTokenizer(data, SEPARATOR);
		Instance instance = new Instance();

		String type = (String) tokenizer.nextElement();
		if (!type.equals(getType())) {
			throw new RuntimeException("Trying to de-serialize an Instance but found a " + type);
		}
		instance.setKnowledgeBase(Knowledger.getBase((String) tokenizer.nextElement()));
		tmp = (String) tokenizer.nextElement();
		if (tmp != null && !tmp.equals("null")) {
			instance.setId(tmp);
		}
		instance.setIdentifier((String) tokenizer.nextElement());

		tmp = (String) tokenizer.nextElement();
		instance.setCreationDate(tmp == null ? null : dateFormat.parse(tmp));
		return instance;

	}

	protected String getType() {
		return TYPE;
	}
}
