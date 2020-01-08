package org.neodatis.knowledger.gui.graph;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.implementation.query.RelationQuery;



/**A value object to keep track of the current graphic configuration
 * 
 * <pre>
 * 	It has a name and various graphic configuration like displaying orphan concept nodes, instance nodes... 
 * </pre>
 * 
 * @author olivier s
 *
 */
public class VisualizationConfiguration {
	/** A relation query is a kind of filer of object that will be displayed*/
	private RelationQuery relationQuery;
	/** if true, orphan concepts (concepts that do not have any relation with any objects) will be displayed*/
	private boolean displayOrphanConcepts;
	/** if true, orphan instances (instances that do not have any relation with any objects) will be displayed*/
	private boolean displayOrphanInstances;
	/** if true, connector of a relation will be displayed as a node*/
	private boolean displayConnectorsAsNodes;
	
	private boolean conceptHaveBox;
	private boolean instanceHaveBox;
	private boolean displayLabelOnEdges;
	
	/** A query that can be used to select a specific group of object - using Knowledger query Language*/
	private String query;
	/** A map of colors used to display edges*/
	private Map entityColors;
	
	private Map edgeLabelsToHide;
	
	public VisualizationConfiguration(){
		relationQuery = new RelationQuery();
		entityColors = new HashMap();
		edgeLabelsToHide = new HashMap();
		edgeLabelsToHide.put(KnowledgeBase.CONNECTOR_IS_SUB_CLASS_OF,Boolean.TRUE);
		edgeLabelsToHide.put(KnowledgeBase.CONNECTOR_IS_INSTANCE_OF,Boolean.TRUE);
		
		this.displayOrphanConcepts = true;
		this.displayOrphanInstances = true;
		this.displayConnectorsAsNodes = false;
		this.conceptHaveBox = true;
		this.instanceHaveBox = false;
		this.displayLabelOnEdges = true;
	}

	public RelationQuery getRelationQuery() {
		return relationQuery;
	}

	public void setRelationQuery(RelationQuery relationQuery) {
		this.relationQuery = relationQuery;
	}

	public boolean displayOrphanConcepts() {
		return displayOrphanConcepts;
	}

	public void setDisplayOrphanConcepts(boolean displayOrphanConcepts) {
		this.displayOrphanConcepts = displayOrphanConcepts;
	}

	public boolean displayOrphanInstances() {
		return displayOrphanInstances;
	}

	public void setDisplayOrphanInstances(boolean displayOrphanInstances) {
		this.displayOrphanInstances = displayOrphanInstances;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean displayConnectorsAsNodes() {
		return displayConnectorsAsNodes;
	}

	public void setDisplayConnectorsAsNodes(boolean displayConnectorsAsNodes) {
		this.displayConnectorsAsNodes = displayConnectorsAsNodes;
	}

	public Color getColorOf(Entity entity) {
		return getColorOf(entity.getIdentifier());
	}
	public Color getColorOf(String identifier) {
		Color color = (Color) entityColors.get(identifier);
		if(color==null){
			return null;
		}
		return color;
	}

	public boolean conceptHaveBox() {
		return conceptHaveBox;
	}

	public void setConceptHaveBox(boolean conceptHaveBox) {
		this.conceptHaveBox = conceptHaveBox;
	}

	public boolean instanceHaveBox() {
		return instanceHaveBox;
	}

	public void setInstanceHaveBox(boolean instanceHaveBox) {
		this.instanceHaveBox = instanceHaveBox;
	}

	public void setColorOf(Entity entity, Color color) {
		entityColors.put(entity.getIdentifier(),color);
		System.out.println("Setting color " + color.getRGB() + " for " + entity.getIdentifier());
	}

	public Map getEdgeLabelsToHide() {
		return edgeLabelsToHide;
	}

	public void setEdgeLabelsToHide(Map edgeLabelsToHide) {
		this.edgeLabelsToHide = edgeLabelsToHide;
	}

	public boolean displayLabelOnEdges() {
		return displayLabelOnEdges;
	}

	public void setDisplayLabelOnEdges(boolean displayLabelOnEdges) {
		this.displayLabelOnEdges = displayLabelOnEdges;
	}
	
	

}
