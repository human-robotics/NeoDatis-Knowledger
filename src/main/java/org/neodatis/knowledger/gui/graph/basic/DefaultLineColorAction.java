package org.neodatis.knowledger.gui.graph.basic;

import java.awt.Color;

import org.neodatis.knowledger.gui.graph.Configuration;

import prefuse.action.assignment.DataColorAction;
import prefuse.visual.VisualItem;


public class DefaultLineColorAction extends DataColorAction {

	private Configuration configuration;

	public DefaultLineColorAction(Configuration configuration,String group, String dataField, int dataType, String colorField, int[] palette) {
		super(group, dataField, dataType, colorField, palette);
		this.configuration = configuration;		
	}


	public DefaultLineColorAction(String group, String dataField, int dataType, String colorField) {
		super(group, dataField, dataType, colorField);
		// TODO Auto-generated constructor stub
	}


	public int getColor(VisualItem item) {
		int type = item.getInt(GraphConstants.TYPE);
		if(configuration!=null){
			if(type == GraphConstants.CONCEPT && configuration.getVisualizationConfiguration().conceptHaveBox()){
				return configuration.getConceptColor().getRGB();
			}
			if(type == GraphConstants.INSTANCE && configuration.getVisualizationConfiguration().instanceHaveBox()){
				return configuration.getInstanceColor().getRGB();
			}
		}
			
		return Color.WHITE.getRGB();
	}
}
