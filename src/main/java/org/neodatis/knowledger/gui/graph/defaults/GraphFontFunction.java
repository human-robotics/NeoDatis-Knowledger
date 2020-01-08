package org.neodatis.knowledger.gui.graph.defaults;

import java.awt.Font;

import org.neodatis.knowledger.gui.graph.Configuration;
import org.neodatis.knowledger.gui.graph.basic.GraphConstants;

import prefuse.action.assignment.FontAction;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;


public class GraphFontFunction extends FontAction{

	private Configuration configuration;
	
	public GraphFontFunction(Configuration configuration){
		this.configuration = configuration;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.berkeley.guir.prefuse.action.assignment.FontFunction#getFont(edu.berkeley.guir.prefuse.VisualItem)
	 */
	public Font getFont(VisualItem item) {

		int type = item.getInt(GraphConstants.TYPE);

		switch (type) {
		case GraphConstants.CONCEPT:
			return FontLib.getFont("SansSerif", Font.PLAIN, configuration.getConceptFontSize());
		case GraphConstants.CONNECTOR:
			return FontLib.getFont("SansSerif", Font.PLAIN, configuration.getConnectorFontSize());
		case GraphConstants.INSTANCE:
			FontLib.getFont("SansSerif", Font.PLAIN, configuration.getInstanceFontSize());
		}
		return FontLib.getFont("SansSerif", Font.PLAIN, configuration.getInstanceFontSize());
	}

}
