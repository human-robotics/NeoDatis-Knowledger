package org.neodatis.knowledger.gui.graph.basic;

import prefuse.Constants;
import prefuse.render.EdgeRenderer;

public class DefaultEdgeRenderer extends EdgeRenderer {

	public DefaultEdgeRenderer() {
		super(Constants.EDGE_TYPE_LINE,Constants.EDGE_ARROW_FORWARD);
	}

	public int getArrowHeadHeight() {
		return 2;
	}

	public int getArrowHeadWidth() {
		return 2;
	}
}
