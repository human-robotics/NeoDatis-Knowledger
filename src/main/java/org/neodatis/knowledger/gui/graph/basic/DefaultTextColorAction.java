package org.neodatis.knowledger.gui.graph.basic;

import java.awt.Color;

import org.neodatis.knowledger.gui.graph.Configuration;

import prefuse.action.assignment.DataColorAction;
import prefuse.visual.VisualItem;


public class DefaultTextColorAction extends DataColorAction {

	private Configuration configuration;

	public DefaultTextColorAction(Configuration configuration, String group, String dataField, int dataType, String colorField, int[] palette) {
		super(group, dataField, dataType, colorField, palette);
		this.configuration = configuration;

	}

	public DefaultTextColorAction(String group, String dataField, int dataType, String colorField) {
		super(group, dataField, dataType, colorField);
		// TODO Auto-generated constructor stub
	}

	public int getColor(VisualItem item) {
		int type = item.getInt(GraphConstants.TYPE);
		if (configuration != null) {
			if(item.isHighlighted()){
				return Color.RED.getRGB();
			}
			// Check if color has been overriden for this entity
			String label = item.getString(GraphConstants.LABEL);
			Color c = configuration.getVisualizationConfiguration().getColorOf(label);
			if(c!=null){
				return c.getRGB();
			}
			switch (type) {
			case GraphConstants.CONCEPT:
				return configuration.getConceptColor().getRGB();
			case GraphConstants.INSTANCE:
				return configuration.getInstanceColor().getRGB();
			case GraphConstants.CONNECTOR:
				return configuration.getConnectorColor().getRGB();
			case GraphConstants.UNKNOWN:
				return Color.ORANGE.getRGB();
			default:
				return Color.BLACK.getRGB();
			}

		} else {
			switch (type) {
			case GraphConstants.CONCEPT:
				return Color.BLUE.getRGB();
			case GraphConstants.INSTANCE:
				return Color.MAGENTA.getRGB();
			case GraphConstants.CONNECTOR:
				return Color.BLACK.getRGB();
			case GraphConstants.UNKNOWN:
				return Color.ORANGE.getRGB();
			default:
				return Color.BLACK.getRGB();
			}
		}
	}
}
