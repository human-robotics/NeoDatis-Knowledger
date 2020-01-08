package org.neodatis.knowledger.gui.graph;

import java.awt.Color;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.gui.GuiLogger;
import org.neodatis.knowledger.gui.graph.panel.KnowledgerGraphPanel;
import org.neodatis.tool.ILogger;


/**
 * A class to contain all graph editor configuration like visualization
 * configuration
 * 
 * @author olivier s
 * 
 */
public class Configuration {
	private String name;
	private KnowledgeBase knowledgeBase;

	private VisualizationConfiguration visualizationConfiguration;

	private KnowledgerGraphPanel knowledgerGraphPanel;

	private int conceptFontSize = 12;

	private int instanceFontSize = 10;

	private int connectorFontSize = 10;

	private Color conceptColor = Color.BLUE;

	private Color instanceColor = Color.MAGENTA;

	private Color connectorColor = Color.GRAY;
	private Color connectorLabelColor = Color.BLACK;

	private Color edgeColor = Color.LIGHT_GRAY;
	
	private int nodeDistance = -25;

	// private String language =
	// System.getProperty("language")!=null?System.getProperty("language"):"en";
	//private String language = "en";

	/**
	 * Used to know which property/relation of the obejct must be used to check
	 * if it has an url (web page) that describes it
	 */
	private String externalUrlRelationName = "url";

	private ILogger logger = new GuiLogger();

	public Configuration(KnowledgeBase kb, String name) {
		this.name = name;
		this.knowledgeBase = kb;
		this.visualizationConfiguration = new VisualizationConfiguration();
		try {
			// Hide IsSubRelationOfConnector
			this.getVisualizationConfiguration().getRelationQuery().addConnectorToExclude(kb.getIsSubRelationOfConnector());
			// Hide Object
			//this.getVisualizationConfiguration().getRelationQuery().addLeftEntityToExclude(kb.getRootObject());
			//this.getVisualizationConfiguration().getRelationQuery().addRightEntityToExclude(kb.getRootObject());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public VisualizationConfiguration getVisualizationConfiguration() {
		return visualizationConfiguration;
	}

	public void setVisualizationConfiguration(VisualizationConfiguration visualizationConfiguration) {
		this.visualizationConfiguration = visualizationConfiguration;
	}

	public KnowledgerGraphPanel getKnowledgerGraphPanel() {
		return knowledgerGraphPanel;
	}

	public void setKnowledgerGraphPanel(KnowledgerGraphPanel knowledgerGraphPanel) {
		this.knowledgerGraphPanel = knowledgerGraphPanel;
	}

	public Color getConceptColor() {
		return conceptColor;
	}

	public void setConceptColor(Color conceptColor) {
		this.conceptColor = conceptColor;
	}

	public Color getConnectorColor() {
		return connectorColor;
	}

	public void setConnectorColor(Color connectorColor) {
		this.connectorColor = connectorColor;
	}

	public Color getInstanceColor() {
		return instanceColor;
	}

	public void setInstanceColor(Color instanceColor) {
		this.instanceColor = instanceColor;
	}

	public Color getEdgeColor() {
		return edgeColor;
	}

	public void setEdgeColor(Color edgeColor) {
		this.edgeColor = edgeColor;
	}

	public int getConnectorFontSize() {
		return connectorFontSize;
	}

	public void setConnectorFontSize(int connectorFontSize) {
		this.connectorFontSize = connectorFontSize;
	}

	public int getConceptFontSize() {
		return conceptFontSize;
	}

	public void setConceptFontSize(int conceptFontSize) {
		this.conceptFontSize = conceptFontSize;
	}

	public int getInstanceFontSize() {
		return instanceFontSize;
	}

	public void setInstanceFontSize(int instanceFontSize) {
		this.instanceFontSize = instanceFontSize;
	}

	/** Language is global*/
	public String getLanguage() {
		return GlobalConfiguration.getLanguage();
	}

	public void setLanguage(String language) {
		GlobalConfiguration.setLanguage(language);
	}

	public String getExternalUrlRelationName() {
		return externalUrlRelationName;
	}

	public void setExternalUrlRelationName(String externalUrlRelationName) {
		this.externalUrlRelationName = externalUrlRelationName;
	}

	public ILogger getLogger() {
		return logger;
	}

	public int getNodeDistance() {
		return nodeDistance;
	}

	public void setNodeDistance(int nodeDistance) {
		this.nodeDistance = nodeDistance;
	}

	public Color getConnectorLabelColor() {
		return connectorLabelColor;
	}

	public void setConnectorLabelColor(Color connectorLabelColor) {
		this.connectorLabelColor = connectorLabelColor;
	}
}
