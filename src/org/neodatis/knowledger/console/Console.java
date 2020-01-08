package org.neodatis.knowledger.console;

/*
 * neodatis 2004 - www.neodatis.com
 * Created on Oct 24, 2004
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import org.neodatis.knowledger.core.CanNotDeleteObjectWithReferencesException;
import org.neodatis.knowledger.core.factory.EntityFactory;
import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.ConceptList;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.interfaces.entity.ICriteria;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.tool.DLogger;




/**
 * @author olivier
 *  
 */
public class Console {

    private IKnowledgeBase knowledgeBase ;

	
    
    private static final char LIST_CONCEPTS = '1';
	private static final char NEW_CONCEPT = '2';
	private static final char FIND_CONCEPT = '3';

	private static final char LIST_INSTANCES = '4';
	private static final char NEW_INSTANCE = '5';
	private static final char FIND_INSTANCE = '6';
	private static final char LIST_CONNECTORS = '7';
	private static final char NEW_CONNECTOR = '8';
	private static final char DELETE_CONNECTOR = '9';
	private static final char LIST_RELATIONS = 'a';
	private static final char NEW_RELATION = 'b';
	private static final char DELETE_RELATION = 'c';
	private static final char QUERY = 'q';
	private static final char SUBCLASSES = 's';
	private static final char SUPERCLASSES = 'r';
	private static final char SUBCONNECTORS = 't';
	private static final char SUPERCONNECTORS = 'u';
	private static final char INSTANCES = 'i';
	private static final char CONCEPT_EXPLORER = 'e';
	private static final char RELATION_TYPE_EXPLORER = 'f';
	private static final char ALL_ABOUT = 'z';
	private static final char ALL_KB = 'x';
	private static final char EXIT = '0';
	private static final char CONCEPT_DETAIL = 'g';
	private static final char INSTANCE_DETAIL = 'h';

	public Console() throws Exception  {
	    init();
	}
	
	private void init() throws Exception{
	    knowledgeBase = KnowledgeBaseFactory.getInstance("default", KnowledgeBaseType.DATABASE);
	}
	public void displayOptions() {

		DLogger.info("\n\t\t\tDIALOGY - KNOWLEDGER CONSOLE\n");

		DLogger.info("* CONCEPT     : " + getLabel("(1)list") + " " + getLabel("(2)new") + " " + getLabel("(3)find")+ " " + getLabel("(g)detail"));

		DLogger.info("* INSTANCE    : " + getLabel("(4)list") + " " + getLabel("(5)new") + " " + getLabel("(6)find")+ " " + getLabel("(h)detail"));

		DLogger.info("* CONNECTOR   : " + getLabel("(7)list") + " " + getLabel("(8)new") + " " + getLabel("(9)delete") + " " + "(t)sub-connectors (u)super-connectors");

		DLogger.info("* RELATION    : " + getLabel("(a)list") + " " + getLabel("(b)new") + " " + getLabel("(c)delete") + " (q)query  (s)sub-classes  (r)super-classes  (i)instances");
		//Logger.info("\t z-all about");
		DLogger.info("* KB          : (x) display");

		//Logger.info("* Explorer \t\t\te-show Concept Explorer\tf-relation
        // type Explorer");

		DLogger.info(getLabel("* exit-0"));

		DLogger.info("\nChoice ? ");
	}

	public char getUserOption() {
		String input = getUserInput(null);
		if (input == null || input.length() == 0) {
			return '@';
		}
		return input.charAt(0);
	}

	private void manageUserOption(char userOption) throws Exception {
		switch (userOption) {
			case LIST_CONCEPTS :
				listConcepts();
				break;
			case NEW_CONCEPT :
				newConcept();
				break;
			case FIND_CONCEPT :
				findConcept();
				break;
			case CONCEPT_DETAIL :
				detailConcept();
				break;
			case LIST_INSTANCES :
				listInstances();
				break;
			case NEW_INSTANCE :
				newInstance();
				break;
			case FIND_INSTANCE :
				findInstances();
				break;
			case INSTANCE_DETAIL :
				detailInstance();
				break;
			case LIST_CONNECTORS :
				listConnectors(true);
				break;
			case NEW_CONNECTOR :
				newConnector();
				break;
			case DELETE_CONNECTOR :
				deleteConnector();
				break;
			case LIST_RELATIONS :
				listRelations();
				break;
			case NEW_RELATION :
				newRelation();
				break;
			case DELETE_RELATION :
				deleteRelation();
				break;
			case QUERY :
				query();
				break;
			case SUBCLASSES :
				subClasses();
				break;
			case SUPERCLASSES :
				superClasses();
				break;
			case SUBCONNECTORS :
				subConnectors();
				break;
			case SUPERCONNECTORS :
				superConnectors();
				break;
				
			case INSTANCES :
				instances();
				break;
			case ALL_ABOUT :
				allAbout();
				break;
			case CONCEPT_EXPLORER :
				displayConceptExplorer();
				break;
			case RELATION_TYPE_EXPLORER :
				displayRelationTypesExplorer();
				break;
			case ALL_KB :
				displayKnowledgeBase();
				break;
			case EXIT :
				System.exit(0);
				break;

		}
		pause();
		
	}

		
	/**
     *  
     */
	private void displayKnowledgeBase() {
		DLogger.info(getLabel("All Knowledge Base"));
		DLogger.info(knowledgeBase.toString());
	}
	 
	private void allAbout() {
		System.out.println("Not implemented");
		/*
         * String name = getUserInput(getLabel("you.want.to.know.all.about"));
         * 
         * IObject object = getSafeObject(name,"object");
         * 
         * RelationList resultList = new RelationList();
         * resultList.addAll(kbUtility.query(object,null,null));
         * resultList.addAll(kbUtility.query(null,null,object));
         * 
         * Relation relation = null;
         * 
         * for (int i = 0; i < resultList.size(); i++) { relation =
         * resultList.getRelation(i); Logger.info("" + (i + 1) + ") " +
         * relation); }
         */
	}
	private void displayConceptExplorer() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								//KnowledgeExplorerPanel.displayConcepts();
							    System.out.println("Not implemented");
							}
						});
	}
	
	private void displayRelationTypesExplorer() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								//KnowledgeExplorerPanel.displayRelationTypes();
							    System.out.println("Not implemented");
							}
						});
	}
	

	private void instances() throws Exception {
		String conceptName = getUserInput(getLabel("instances of"));
		String sDepth = getUserInput(getLabel("depth"));

		int depth = (sDepth != null && sDepth.length() > 0 ? Integer.parseInt(sDepth) : 10);

		if (knowledgeBase.existConcept(conceptName,true)) {
			List l = knowledgeBase.getConceptFromName(conceptName,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST).getInstances(depth);
			DLogger.info(l);
		} else {
			DLogger.info("Concept does not exist");
		}
	}

	/**
     * display super classes of one concept
	 * @throws Exception 
     *  
     */
	private void superClasses() throws Exception {
		String conceptName = getUserInput(getLabel("super classes of"));
		String sDepth = getUserInput(getLabel("depth"));

		int depth = (sDepth != null && sDepth.length() > 0 ? Integer.parseInt(sDepth) : 10);

		if (knowledgeBase.existConcept(conceptName,true)) {
			List l = knowledgeBase.getSuperConceptsOf(knowledgeBase.getConceptFromName(conceptName,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST), depth);
			DLogger.info(l);
		} else {
			DLogger.info("Concept does not exist");
		}
	}
	/**
     * display super relation types of one relation type
	 * @throws Exception 
     *  
     */
	private void superConnectors() throws Exception {
		String name = getUserInput(getLabel("super connectors"));
		String sDepth = getUserInput(getLabel("depth"));

		int depth = (sDepth != null && sDepth.length() > 0 ? Integer.parseInt(sDepth) : 10);

		if (knowledgeBase.existConnector(name,true)) {
		    Connector connector = knowledgeBase.getConnectorFromName(name,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
			List l = knowledgeBase.getSuperConnectorsOf(connector, depth);
			DLogger.info(l);
		} else {
			DLogger.error("Connector does not exist");
		}
	}
	/**
     * Displays sub classes of one concept
	 * @throws Exception 
     *  
     */
	private void subClasses() throws Exception {
		String conceptName = getUserInput(getLabel("subclasses of"));
		String sDepth = getUserInput(getLabel("depth"));

		int depth = (sDepth != null && sDepth.length() > 0 ? Integer.parseInt(sDepth) : 10);

		if (knowledgeBase.existConcept(conceptName,true)) {
			List l = knowledgeBase.getConceptFromName(conceptName,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST).getSubConcepts(depth);
			DLogger.info(l);
		} else {
			DLogger.info("Concept does not exist");
		}
	}
	/**
     * Displays sub relation types of a relation type
	 * @throws Exception 
     *  
     */
	private void subConnectors() throws Exception {
		String name = getUserInput(getLabel("sub connector of"));
		String sDepth = getUserInput(getLabel("depth"));

		int depth = (sDepth != null && sDepth.length() > 0 ? Integer.parseInt(sDepth) : 10);

		if (knowledgeBase.existConnector(name,true)) {
		    Connector connector = knowledgeBase.getConnectorFromName(name,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
			List l = knowledgeBase.getSubConnectorsOf(connector, depth);
			DLogger.info(l);
		} else {
			DLogger.error("Connector does not exist ");
		}
	}
	/**
     * Execute a simple query
     * 
     * @throws Exception
     * @throws Exception
     *  
     */
	private void query() throws Exception, Exception {
	    System.out.println("Not implemented");
	    
		String leftPart = getUserInput(getLabel("left.part.of.relation"));
		String connectorName = getUserInput(getLabel("connector.type"));
		String rightPart = getUserInput(getLabel("right.part.of.relation"));

		Entity leftPartObject = null;
		Connector connector = null;
		Entity rightPartObject = null;

		if (leftPart != null && leftPart.length() > 0) {
			leftPartObject = getSafeObject(leftPart, getLabel("left.part"));
		}

		if (connectorName != null && connectorName.length() > 0) {
			connector = knowledgeBase.getConnectorFromName(connectorName,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
		}

		if (rightPart != null && rightPart.length() > 0) {
			rightPartObject = getSafeObject(rightPart, getLabel("right.part"));
		}

		RelationList relationList = knowledgeBase.queryRelations(leftPartObject, connector, rightPartObject);

		DLogger.info(getLabel("query.result") + " : ");
		for (int i = 0; i < relationList.size(); i++) {
			DLogger.info("" + (i + 1) + ") " + relationList.get(i));
		}

		// Ask if user wants to do something with the result
		String action =
			getUserInput(getLabel("select.an.action (extract left(l)/extract relation type(rt)/extract right(r)/delete(d))"));

		if (action == null || action.length() == 0) {
			return;
		}

		if (action.equalsIgnoreCase("d")) {
			knowledgeBase.deleteRelations(relationList);
			DLogger.info(getLabel("these.objects.have.been.deleted"));
			for (int i = 0; i < relationList.size(); i++) {
				DLogger.info("" + (i + 1) + ") " + relationList.get(i));
			}
			return;
		}
		/*
         * List objectList = null; if (action.equalsIgnoreCase("l")) {
         * objectList = relationList.extractLeftObjectsFromRelation(); } if
         * (action.equalsIgnoreCase("r")) { objectList =
         * relationList.extractRightObjectsFromRelation(); } if
         * (action.equalsIgnoreCase("rt")) { objectList =
         * relationList.extractRelationTypesFromRelation(); }
         * 
         * Logger.info(getLabel("action.result") + " : "); for (int i = 0; i <
         * objectList.size(); i++) { Logger.info("" + (i + 1) + ") " +
         * relationList.get(i)); }
         *  // Ask if user wants to do something with the result action =
         * getUserInput(getLabel("select.an.action (delete(d))"));
         * 
         * if (action == null || action.length() == 0) { return; }
         * 
         * if (action.equalsIgnoreCase("d")) {
         * 
         * try { kbUtility.deleteObjects(objectList);
         * Logger.info(getLabel("these.objects.have.been.deleted")); for (int i =
         * 0; i < relationList.size(); i++) { Logger.info("" + (i + 1) + ") " +
         * relationList.get(i)); } } catch
         * (CanNotDeleteObjectWithReferencesException e) { e.printStackTrace(); } }
         */
		
	}

	/**
     *  
     */
	private void deleteRelation() {
		DLogger.info("Not implemented");
	}
	/**
     * Creates a new relation (binary) : <left object> <relation><right object>
     * 
     * @throws Exception
     *  
     */
	private void newRelation() throws Exception {

		// Ask the user the left part
		String leftPart = getUserInput(getLabel("left.part.of.relation"));
		DLogger.info("");
		// Ask the user the connector
		DLogger.info(getLabel("available.connectors"));
		// Displays available relation types
		listConnectors(false);
		DLogger.info("");
		String connectorName = getUserInput(getLabel("which.connector"));

		// Ask the user the right part
		// TODO right with list
		String rightPart = getUserInput(getLabel("right.part.of.relation"));

		Entity leftPartEntity = null;
		Entity rightPartEntity = null;
		Connector connectorObject = null;

		leftPartEntity = getSafeObject(leftPart, getLabel("left.entity"));
		rightPartEntity = getSafeObject(rightPart, getLabel("right.entity"));
		connectorObject = knowledgeBase.getConnectorFromName(connectorName,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);

		
		String mandatory = getUserInput(getLabel("is mandatory(yes/no)"));
		boolean isMandatory = mandatory.equals("yes");

		String sReliability = getUserInput(getLabel("reliability"));
		int reliability = Integer.parseInt(sReliability);
		
		
		DLogger.info("Creating " + leftPartEntity + " " + connectorObject + " " + rightPartEntity);
		String yesNo = getUserInput(getLabel("Ok (y/n)"));

		// Confirm creation
		if (yesNo.equalsIgnoreCase("y")) {
		    Relation relation = EntityFactory.getRelationInstance();
		    relation.setConnector(connectorObject);
		    relation.setLeftEntity(leftPartEntity);
		    relation.setRightEntity(rightPartEntity);
		    //relation.setReliabity(reliability);
			knowledgeBase.createRelation(relation);
		}

	}
	/**
     * @param objectName
     * @param label
     * @return
     * @throws Exception
     */
	private Entity getSafeObject(String objectName, String label) throws Exception {

		ICriteria criteria = null;
		// Create criteria with name
		DLogger.info("getObjects with criteria not implemented");
	    EntityList entities = knowledgeBase.getEntitiesByName(objectName);
		
	    Entity entity = null;

		if (entities.isEmpty()) {
			throw new Exception("object does not exist");
		} else {
			if (entities.size() > 1) {
				DLogger.info("");
				DLogger.info(getLabel("there.exist.more.than.one.object.choose.the.") + label);
				DLogger.info("");
				displayObjectList(entities, true);
				String sid = getUserInput(getLabel("choose.id"));
				if (sid != null && sid.length() > 0) {
					entity = entities.getEntityFromId(sid);
				}
			} else {
				entity = entities.getEntity(0);
			}
		}
		return entity;
	}
	/**
     * @param name
     * @return
	 * @throws Exception 
     */
	private Entity createObjectAskingType(String name) throws Exception {
		do {
			String createAs =
				getUserInput("object.does.not.exist, do.you.want.to.create.as.concept(c),instance(i),connector(con) or exit(e)");
			if (createAs != null) {
				if (createAs.equalsIgnoreCase("c")) {
					return knowledgeBase.getConceptFromName(name,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
				}
				if (createAs.equalsIgnoreCase("i")) {
					return knowledgeBase.getInstanceFromValue(name,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
				}
				if (createAs.equalsIgnoreCase("con")) {
					return knowledgeBase.getConnectorFromName(name,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
				}
				if(createAs.equalsIgnoreCase("e")) {
					throw new RuntimeException("query canceled");
				}
			}
			DLogger.info("Digit.c,i or con");
		} while (true);
	}
	/**
     * List all the relations of the KB
     *  
     */
	private void listRelations() {
		DLogger.info(getLabel("relation.list") + " : ");
		Relation relation = null;
		for (int i = 0; i < knowledgeBase.getRelationList().size(); i++) {
			relation = (Relation) knowledgeBase.getRelationList().get(i);
			DLogger.info(relation.toString() );
		}
	}
	/**
     * @throws Exception
     *  
     */
	private void deleteConnector() throws Exception {
		Connector connector = null;
		for (int i = 0; i < knowledgeBase.getConnectorList().size(); i++) {
			connector = (Connector) knowledgeBase.getConnectorList().get(i);
			DLogger.info(connector.getId() + " - " + connector);
		}

		String sid = getUserInput(getLabel("digit.id.of.connetor.to.delete"));
		long id = Long.parseLong(sid);
		try {
			knowledgeBase.deleteConnectorWithId(id);
		} catch (CanNotDeleteObjectWithReferencesException e) {
			DLogger.info(e.getMessage());
		}
	}
	/**
     * Creates a new connector
     * 
     * @throws Exception
     */
	private void newConnector() throws Exception {
		Connector connector = EntityFactory.getConnectorInstance();
		String connectorName = getUserInput(getLabel("connector.name"));
		connector.setCreationDate(new Date());
		connector.setIdentifier(connectorName);
		
		knowledgeBase.createConnector(connector);

		DLogger.info(connector+ " " + getLabel(" created "));
	}
	/**
     * List of relation types
     * 
     * @param pause
     */
	private void listConnectors(boolean pause) {
		DLogger.info(getLabel("connector.list") + " : ");
		displayList(knowledgeBase.getConnectorList());
		if (pause) {
			pause();
		}
	}
	/**
     * Deleta an instance - not yet implemented
     * 
     * @throws Exception
     *  
     */
	private void deleteInstance() throws Exception {
		KnowledgerObject object = null;

		for (int i = 0; i < knowledgeBase.getInstanceList().size(); i++) {
			object = knowledgeBase.getInstanceList().getInstance(i);
			DLogger.info(object.getId() + " - " + object);
		}

		String sid = getUserInput(getLabel("digit.id.of.instance.to.delete"));
		if (sid != null && sid.length() > 0) {
			long id = Long.parseLong(sid);
			try {
				knowledgeBase.deleteInstanceWithId(id);
			} catch (CanNotDeleteObjectWithReferencesException e) {
				DLogger.info(e.getMessage());
			}
		}
	}
	/**
     * Creates a new Instance
     * 
     * @throws Exception
     *  
     */
	private void newInstance() throws Exception {
		Relation relation = null;
		String instanceName = getUserInput(getLabel("instance.name"));
		String instanceValue = getUserInput(getLabel("instance.value"));

		Instance instance = EntityFactory.getInstanceInstance();
		instance.setIdentifier(instanceName);
		instance.setValue(instanceValue);
		knowledgeBase.createInstance(instance);
		
		String whatIsInstance = getUserInput(instanceName + " is-instance-of ");
		Connector connector = knowledgeBase.getIsInstanceOfConnector();

		if (whatIsInstance != null && whatIsInstance.length() != 0) {
			Concept concept = knowledgeBase.getConceptFromName(whatIsInstance,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
			relation = EntityFactory.getRelationInstance(instance,connector,concept,null);
			knowledgeBase.createRelation(relation);
			DLogger.info(relation + getLabel(" created "));
		}
	}
	/**
     * List all instances
     */
	private void listInstances() {
		DLogger.info(getLabel("instance.list") + " : ");
		Instance instance = null;
		for (int i = 0; i < knowledgeBase.getInstanceList().size(); i++) {
			instance = knowledgeBase.getInstanceList().getInstance(i);
			DLogger.info(instance);
			continue;
			
//			Logger.info(instance.getName());
//			
//			if(instance.getConceptType()!=null){
//				Logger.info(" is-a " + instance.getConceptType().getName());
//			}else {
//				Logger.info(" : ?" );
//			}
//			
//			Logger.info(" type not implemented");
//			if(instance.getValue()!=null && instance.getValue().length()>0){
//				Logger.info(" - value = " + instance.getValue());
//			}
//			
//			Logger.info(" - " + instance.getId() + "\t" + instance.getCreationDate());
//			Logger.info("");
			
		}
	}
	/**
     * List all concepts
     */
	private void listConcepts() {
		
		KnowledgerObject o = null;
		DLogger.info(getLabel("concept.list") + " : ");
		
		for(int i=0;i<knowledgeBase.getConceptList().size();i++){
			o = knowledgeBase.getConceptList().getConcept(i);
			DLogger.info(o.getIdentifier() + "\t\t\t"+o.getId() + "\t" + o.getCreationDate());
		}
	}
	/**
     * Modify one concept - test this method
     */
	private void modifyConcept() throws Exception {

		boolean bHasChanged = false;
		KnowledgerObject object = null;

		DLogger.info(getLabel("choose.the.dialog.unit"));

		for (int i = 0; i < knowledgeBase.getConceptList().size(); i++) {
			object = knowledgeBase.getConceptList().getConcept(i);
			DLogger.info(object.getId() + " - " + object);
		}

		String sid = getUserInput(getLabel("digit.id.of.concept.to.modify"));
		long id = Long.parseLong(sid);
		object = knowledgeBase.getConceptFromId(id);

		String newConceptName = getUserInput(getLabel("new.concept.name") + " " + object.getIdentifier());

		if (newConceptName.length() != 0) {
			object.setIdentifier(newConceptName);
			bHasChanged = true;
		}

		if (bHasChanged) {
		    DLogger.info("update not implemented");
			//PersistenceManager.getInstance(true).saveOrUpdateAndClose(object);
		}
	}
	/**
     * Delete on concept
     * 
     * @throws Exception
     *  
     */
	private void findConcept() throws Exception {
		KnowledgerObject object = null;

		String partOfConceptName = getUserInput(getLabel("digit.part.of.concept.name"));
		
		DLogger.info("Find by name not implemented - loading all concepts");

		ConceptList conceptList = knowledgeBase.getConceptList();
		for (int i = 0; i < conceptList.size(); i++) {
			object = conceptList.getConcept(i);
			DLogger.info(object.getId() + " - " + object);
		}

		String sid = getUserInput(getLabel("digit.id.of.concept.to.delete.or.all.to.delete.all"));
		if (sid != null && sid.length() > 0) {
			if (sid.equalsIgnoreCase("all")) {
				try {
					knowledgeBase.deleteConcepts(conceptList);
				} catch (CanNotDeleteObjectWithReferencesException e) {
					DLogger.info(e.getMessage());
				}
			} else {
				long id = Long.parseLong(sid);
				try {
					knowledgeBase.getConceptFromId(id).delete();
				} catch (CanNotDeleteObjectWithReferencesException e) {
					DLogger.info(e.getMessage());
				}
			}
		}
	}
	
	public void detailConcept() throws Exception{
		KnowledgerObject object = null;

		String partOfConceptName = getUserInput(getLabel("digit.part.of.concept.name"));
		
		DLogger.info("Find by name not implemented - loading all concepts");

		ConceptList conceptList = knowledgeBase.getConceptList();
		for (int i = 0; i < conceptList.size(); i++) {
			object = conceptList.getConcept(i);
			DLogger.info(object.getId() + " - " + object);
		}

		String sid = getUserInput(getLabel("digit.id.of.concept.to.detail"));
		if (sid != null && sid.length() > 0) {
			long id = Long.parseLong(sid);
			Concept concept = knowledgeBase.getConceptFromId(id);
			DLogger.info("Concept Name : " + concept.getIdentifier());
			DLogger.info("Concept Id   : " + concept.getId());
			DLogger.info("Creation Date: " + concept.getCreationDate());
			DLogger.info("User         : " + concept.getUser());
		}
	}
	
	public void detailInstance() throws Exception{
		KnowledgerObject object = null;

		String partOfInstanceName = getUserInput(getLabel("digit.part.of.insatnce.identifier"));
		
		DLogger.info("Find by name not implemented - loading all instances");

		InstanceList instanceList = knowledgeBase.getInstanceList();
		for (int i = 0; i < instanceList.size(); i++) {
			object = instanceList.getInstance(i);
			DLogger.info(object.getId() + " - " + object);
		}

		String sid = getUserInput(getLabel("digit.id.of.instance.to.detail"));
		if (sid != null && sid.length() > 0) {
			long id = Long.parseLong(sid);
			Instance instance = knowledgeBase.getInstanceFromId(id);
			DLogger.info("Instance Name : " + instance.getIdentifier());
			DLogger.info("Instance Type : " + instance.getConcept());			
			DLogger.info("Instance Id   : " + instance.getId());
			DLogger.info("Instance Value: " + instance.getValue());
			DLogger.info("Creation Date: " + instance.getCreationDate());
			DLogger.info("User         : " + instance.getUser());
		}
	}

	/**
     * find instances
     * 
     * @throws Exception
     *  
     */
	private void findInstances() throws Exception {
		KnowledgerObject object = null;

		String partOfInstanceName = getUserInput(getLabel("digit.part.of.instance.name"));
		DLogger.info("Find by name not implemented - loading all concepts");
		InstanceList instanceList = knowledgeBase.getInstanceList();
		for (int i = 0; i < instanceList.size(); i++) {
			object = (KnowledgerObject) instanceList.getInstance(i);
			DLogger.info(object.getId() + " - " + object);
		}

		String sid = getUserInput(getLabel("digit.id.of.instance.to.delete.or.all.to.delete.all"));
		if (sid != null && sid.length() > 0) {
			if (sid.equalsIgnoreCase("all")) {
				try {
					knowledgeBase.deleteInstances(instanceList);
				} catch (CanNotDeleteObjectWithReferencesException e) {
					DLogger.info(e.getMessage());
				}
			} else {
				long id = Long.parseLong(sid);
				try {
					knowledgeBase.deleteInstanceWithId(id);
				} catch (CanNotDeleteObjectWithReferencesException e) {
					DLogger.info(e.getMessage());
				}
			}
		}
	}

	/**
     * Creates a new concept
     * 
     * @throws Exception
     *  
     */
	private void newConcept() throws Exception {

		Relation relation = null;
		String whatIsConcept = null;
		String conceptName = getUserInput(getLabel("concept.name"));

		Concept concept = EntityFactory.getConceptInstance();
		concept.setIdentifier(conceptName);
		knowledgeBase.createConcept(concept);
		Connector connector = knowledgeBase.getIsSubclassOfConnector();

		do {
			// Ask user some help to define the new concept
			whatIsConcept = getUserInput(conceptName + " is-subclass-of ");

			if (whatIsConcept != null && whatIsConcept.length() != 0) {
				Entity o2 = knowledgeBase.getConceptFromName(whatIsConcept,GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
				relation = EntityFactory.getRelationInstance(concept, connector, o2,null);
				knowledgeBase.createRelation(relation);
				DLogger.info(relation + getLabel(" created "));
				conceptName = whatIsConcept;
			}

		} while (whatIsConcept != null && whatIsConcept.length() != 0);
	}

	/**
     * Waits for a user key stroke
     *  
     */
	public void pause() {
		DLogger.info("\n" + getLabel("press.one.key") + " > ");
		getUserInput(null);
	}

	/**
     * Get a key from the user
     * 
     * @param question
     * @return
     */
	public String getUserInput(String question) {

		if (question != null) {
			DLogger.info(question + " ? ");
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String sInput = "-";
		try {
			sInput = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sInput;
	}

	/**
     * Executes the Console
     * 
     * @throws Exception
     */
	public void run() throws Exception {
		char userOption = '0';

		do {
			displayOptions();
			userOption = getUserOption();
			try{
				manageUserOption(userOption);
			}catch(Exception e){
				DLogger.info("Error : " + e.getMessage());
				e.printStackTrace();
				pause();
			}			
		} while (userOption != 0);

	}

	/**
     * Display a list
     * 
     * @param list
     */
	private void displayList(List list) {
		for (int i = 0; i < list.size(); i++) {
			DLogger.info(list.get(i));
		}
	}

	private void displayObjectList(List list, boolean withId) {
		Entity object = null;
		for (int i = 0; i < list.size(); i++) {
			object = (Entity) list.get(i);
			DLogger.info(object.getIdentifier() + "(type not impl." + ")" + (withId ? " " + object.getId() : ""));
		}
	}

	private void newUser() throws Exception {
		String userName = getUserInput(getLabel("user.name"));
		String userEmail = getUserInput(getLabel("user.email"));
		//IUser user = new User(userName, userEmail);
		//PersistenceManager.getInstance(true).saveAndClose(user);
		DLogger.info("new user not implemented");
	}

	private void newLanguage() throws Exception {
		String languageName = getUserInput(getLabel("language.name"));
		/*
         * Language language = new Language(languageName);
         * PersistenceManager.getInstance(true).saveAndClose(language);
         */
		DLogger.info("new language not implemented");
		
	}

	private String getLabel(String text) {
		return text.replace('.', ' ');
	}

	public static void main(String[] args) throws Exception, ClassNotFoundException {
		Console console = new Console();
		console.run();
	}

}
