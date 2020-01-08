package org.neodatis.knowledger.gui.graph.basic;

import java.awt.Color;

import org.neodatis.knowledger.gui.graph.Configuration;

import prefuse.action.assignment.DataColorAction;
import prefuse.visual.VisualItem;


public class DefaultEdgeLineColorAction extends DataColorAction {

	private Configuration configuration;

	public DefaultEdgeLineColorAction(Configuration configuration,String group, String dataField, int dataType, String colorField, int[] palette) {
		super(group, dataField, dataType, colorField, palette);
		this.configuration = configuration;		
	}


	public DefaultEdgeLineColorAction(String group, String dataField, int dataType, String colorField) {
		super(group, dataField, dataType, colorField);
		// TODO Auto-generated constructor stub
	}


	public int getColor(VisualItem item) {
		if(configuration!=null ){
			return configuration.getEdgeColor().getRGB();
		}
		return Color.WHITE.getRGB();
	}
}
