package org.neodatis.knowledger.core.implementation.remote;

import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.knowledgebase.remote.serialization.DataSerializer;
import org.neodatis.knowledger.core.implementation.knowledgebase.remote.serialization.ObjectContainer;

import junit.framework.TestCase;


public class TestSerialization extends TestCase {
	
	public void testConcept() throws Exception{
		Concept concept = new Concept("animal");
		concept.setId(1);
		String serial = DataSerializer.getInstance(false).toString(concept);
		assertTrue(serial.startsWith("C;null;1;animal;"));
		
		ObjectContainer container = DataSerializer.getInstance(false).fromString(serial);
		Concept concept2 = (Concept) container.getConceptList().get(0);
		assertEquals(concept,concept2);
	}
	public void testInstance() throws Exception{
		Instance instance = new Instance("kiko");
		instance.setId(2);
		String serial = DataSerializer.getInstance(false).toString(instance);
		System.out.println(serial);
		assertTrue(serial.startsWith("I;null;2;kiko;"));
		
		ObjectContainer container = DataSerializer.getInstance(false).fromString(serial);
		Instance instance2 = (Instance) container.getInstanceList().get(0);
		assertEquals(instance,instance2);

	}
	public void testConnector() throws Exception{
		Connector connector = new Connector("is-a");
		connector.setId(2);
		String serial = DataSerializer.getInstance(false).toString(connector);
		assertTrue(serial.startsWith("CO;null;2;is-a"));
		
		ObjectContainer container = DataSerializer.getInstance(false).fromString(serial);
		Connector connector2 = (Connector) container.getConnectorList().get(0);
		assertEquals(connector,connector2);

	}
	public void testRelation() throws Exception{
		Concept concept1 = new Concept("Human");
		Concept concept2 = new Concept("Animal");
		Connector connector = new Connector("is-a");
		concept1.setId(1);
		concept2.setId(3);
		connector.setId(2);
		Relation relation = new Relation(concept1,connector,concept2);
		relation.setId(5);
		
		String serial = DataSerializer.getInstance(false).toString(relation);
		assertTrue(serial.startsWith("R;null;5;1;2;3"));
		
		ObjectContainer container = DataSerializer.getInstance(false).fromString(serial);
		Relation relation2 = (Relation) container.getRelationList().get(0);
		assertEquals(relation,relation2);

	}
}
